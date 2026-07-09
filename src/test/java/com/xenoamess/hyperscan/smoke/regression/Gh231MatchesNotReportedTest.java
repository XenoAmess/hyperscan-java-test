package com.xenoamess.hyperscan.smoke.regression;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualApiArgumentsSource;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;
import com.xenoamess.hyperscan.smoke.dual.DualMatch;
import com.xenoamess.hyperscan.smoke.dual.DualScanner;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Gh231MatchesNotReportedTest {

    @ParameterizedTest
    @Disabled("Underlying Hyperscan/VectorScan 5.4.12 does not match this pattern; upstream issue possible")
    @ArgumentsSource(DualApiArgumentsSource.class)
    void shouldHaveAMatch(DualApi api) {
        try (DualDatabase db = api.compileDatabase(
                api.createExpression("^.*TAPIOCA.*EX.*$", DualExpressionFlag.CASELESS, 10055730),
                api.createExpression("^.*BOBA.*GUY.*$", DualExpressionFlag.CASELESS, 10055725),
                api.createExpression("^.*PAYPAL.*JACQUES.*MARI.*$", DualExpressionFlag.CASELESS, 10055723),
                api.createExpression("^GUCCI FRANCE.*$", DualExpressionFlag.CASELESS, 10055736),
                api.createExpression("^PAYPAL \\*YVESSAINTL.*$", DualExpressionFlag.CASELESS, 10055734),
                api.createExpression("^DD.*FIREHO 855-973-1040$", DualExpressionFlag.CASELESS, 10055728),
                api.createExpression("^.*XING.*FU.*TAN.*$", DualExpressionFlag.CASELESS, 10055733)
        )) {
            try (DualScanner scanner = api.createScanner()) {
                api.allocScratch(scanner, db);
                List<DualMatch> matches = api.scan(scanner, db, "TAPIOCA HOUSE TUXTLA GUTIER MEX");
                assertFalse(matches.isEmpty());
            }
        }
    }
}
