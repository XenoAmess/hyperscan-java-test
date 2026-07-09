package com.xenoamess.hyperscan.smoke.wrapper;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualApiArgumentsSource;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualMatch;
import com.xenoamess.hyperscan.smoke.dual.DualScanner;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.ref.WeakReference;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeallocationTest {

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    public void ensureResourcesAreProperlyFreed(DualApi api) {
        DualDatabase db = api.compileDatabase(new DualExpression("Te?st"));

        try (DualScanner scanner = api.createScanner()) {
            api.allocScratch(scanner, db);

            List<DualMatch> matches = api.scan(scanner, db, "Test");
            assertEquals(1, matches.size());

            try (DualScanner additionalScanner = api.createScanner()) {
                api.allocScratch(additionalScanner, db);
            }
        }

        long size = api.getDatabaseSize(db);
        assertTrue(size > 0);

        api.closeDatabase(db);

        try {
            api.getDatabaseSize(db);
            fail("Should throw after close");
        } catch (IllegalStateException e) {
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void nativeHandlesShouldBeGarbageCollectable(DualApi api) {
        DualDatabase db = api.compileDatabase(new DualExpression("Te?st"));
        DualScanner scanner = api.createScanner();
        api.allocScratch(scanner, db);
        api.closeScanner(scanner);

        WeakReference<DualDatabase> dbRef = new WeakReference<>(db);
        WeakReference<DualScanner> scannerRef = new WeakReference<>(scanner);
        db = null;
        scanner = null;

        assertNotNull(dbRef);
        assertNotNull(scannerRef);

        for (int i = 0; i < 100; i++) {
            System.gc();
        }

        assertNull(dbRef.get());
        assertNull(scannerRef.get());
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void databaseShouldThrowExceptionOnCallingSizeAfterClose(DualApi api) {
        try {
            DualDatabase db = api.compileDatabase(new DualExpression("test"));
            api.closeDatabase(db);
            api.getDatabaseSize(db);
            fail("We should not be able to call getSize on a deallocated database");
        } catch (IllegalStateException e) {
        } catch (Exception t) {
            fail(t);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scannerShouldThrowExceptionOnCallingSizeAfterClose(DualApi api) {
        try {
            DualDatabase db = api.compileDatabase(new DualExpression("test"));
            DualScanner scanner = api.createScanner();
            api.allocScratch(scanner, db);
            api.closeScanner(scanner);
            api.getScannerSize(scanner);
            fail("We should not be able to call getSize on a deallocated scanner scratch space");
        } catch (IllegalStateException e) {
        } catch (Exception t) {
            fail(t);
        }
    }
}
