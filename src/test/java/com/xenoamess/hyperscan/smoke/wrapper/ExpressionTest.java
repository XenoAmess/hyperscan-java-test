package com.xenoamess.hyperscan.smoke.wrapper;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualApiArgumentsSource;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionTest {

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void validExpressionShouldValidate(DualApi api) {
        EnumSet<DualExpressionFlag> flags = EnumSet.of(DualExpressionFlag.CASELESS, DualExpressionFlag.SOM_LEFTMOST);
        DualExpression expression = new DualExpression("Te?st", flags);
        assertTrue(api.validate(expression), "Valid expression should return isValid() = true");
        assertNull(api.getValidationError(expression), "Valid expression should have null error message");
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void invalidExpressionShouldNotValidate(DualApi api) {
        DualExpression invalidExpression = new DualExpression("test\\1", EnumSet.noneOf(DualExpressionFlag.class));
        assertFalse(api.validate(invalidExpression), "Invalid expression should return isValid() = false");
        assertNotNull(api.getValidationError(invalidExpression), "Invalid expression should have a non-null error message");
        assertTrue(api.getValidationError(invalidExpression).length() > 0, "Invalid expression error message should not be empty");
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void constructorWithNullExpressionStringShouldThrow(DualApi api) {
        assertThrows(NullPointerException.class, () -> new DualExpression(null),
                "Constructor should throw NullPointerException for null expression string");
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void constructorWithNullFlagsShouldThrow(DualApi api) {
        assertThrows(NullPointerException.class, () -> new DualExpression("test", (EnumSet<DualExpressionFlag>) null),
                "Constructor should throw NullPointerException for null flags set");
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void constructorWithIdAndNullFlagsShouldThrow(DualApi api) {
        assertThrows(NullPointerException.class, () -> new DualExpression("test", (EnumSet<DualExpressionFlag>) null, 1),
                "Constructor should throw NullPointerException for null flags set (with ID)");
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void constructorWithValidIdShouldSucceed(DualApi api) {
        assertDoesNotThrow(() -> new DualExpression("test", EnumSet.noneOf(DualExpressionFlag.class), 0),
                "Constructor should accept ID 0");
        assertDoesNotThrow(() -> new DualExpression("test", EnumSet.noneOf(DualExpressionFlag.class), 123),
                "Constructor should accept positive ID");
        assertDoesNotThrow(() -> new DualExpression("test", EnumSet.noneOf(DualExpressionFlag.class), null),
                "Constructor should accept null ID");
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void constructorWithNegativeIdShouldThrow(DualApi api) {
        assertThrows(IllegalArgumentException.class, () -> new DualExpression("test", EnumSet.noneOf(DualExpressionFlag.class), -1),
                "Constructor should throw IllegalArgumentException for negative ID");
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void constructorWithSingleFlagShouldSucceed(DualApi api) {
        DualExpression exp = new DualExpression("test", DualExpressionFlag.CASELESS, 1);
        assertEquals("test", exp.pattern());
        assertEquals(EnumSet.of(DualExpressionFlag.CASELESS), exp.flags());
        assertEquals(Integer.valueOf(1), exp.id());
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void equalsAndHashCodeShouldWork(DualApi api) {
        EnumSet<DualExpressionFlag> flags1 = EnumSet.of(DualExpressionFlag.CASELESS, DualExpressionFlag.DOTALL);
        EnumSet<DualExpressionFlag> flags2 = EnumSet.of(DualExpressionFlag.DOTALL, DualExpressionFlag.CASELESS);
        EnumSet<DualExpressionFlag> flags3 = EnumSet.of(DualExpressionFlag.MULTILINE);

        DualExpression expr1 = new DualExpression("pattern1", flags1, 1);
        DualExpression expr1Same = new DualExpression("pattern1", flags2, 1);
        DualExpression expr2 = new DualExpression("pattern2", flags1, 1);
        DualExpression expr3 = new DualExpression("pattern1", flags3, 1);
        DualExpression expr4 = new DualExpression("pattern1", flags1, 2);
        DualExpression expr5 = new DualExpression("pattern1", flags1, null);
        DualExpression expr5Same = new DualExpression("pattern1", flags2, null);
        DualExpression expr6 = new DualExpression("pattern1", flags1);

        assertEquals(expr1, expr1);
        assertEquals(expr1, expr1Same);
        assertEquals(expr1Same, expr1);
        assertEquals(expr1.hashCode(), expr1Same.hashCode());

        assertEquals(expr5, expr5Same);
        assertEquals(expr5Same, expr5);
        assertEquals(expr5.hashCode(), expr5Same.hashCode());

        assertEquals(expr5, expr6);
        assertEquals(expr6, expr5);
        assertEquals(expr5.hashCode(), expr6.hashCode());

        assertNotEquals(expr1, expr2);
        assertNotEquals(expr1, expr3);
        assertNotEquals(expr1, expr4);
        assertNotEquals(expr1, expr5);
        assertNotEquals(expr1, null);
        assertNotEquals(expr1, "pattern1");

        assertNotEquals(expr1.hashCode(), expr2.hashCode());
        assertNotEquals(expr1.hashCode(), expr3.hashCode());
    }
}
