package com.xenoamess.hyperscan.smoke.dual;

import java.util.EnumSet;

public record DualExpression(String pattern, EnumSet<DualExpressionFlag> flags, Integer id) {

    public DualExpression {
        if (pattern == null) {
            throw new NullPointerException("pattern");
        }
        if (flags == null) {
            throw new NullPointerException("flags");
        }
        if (id != null && id < 0) {
            throw new IllegalArgumentException("Expression ID must not be negative: " + id);
        }
    }

    public DualExpression(String pattern, EnumSet<DualExpressionFlag> flags) {
        this(pattern, flags, null);
    }

    public DualExpression(String pattern, DualExpressionFlag flag) {
        this(pattern, EnumSet.of(flag));
    }

    public DualExpression(String pattern, DualExpressionFlag flag, Integer id) {
        this(pattern, EnumSet.of(flag), id);
    }

    public DualExpression(String pattern) {
        this(pattern, EnumSet.noneOf(DualExpressionFlag.class), null);
    }
}
