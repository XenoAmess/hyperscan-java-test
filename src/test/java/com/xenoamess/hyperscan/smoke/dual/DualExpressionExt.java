package com.xenoamess.hyperscan.smoke.dual;

public record DualExpressionExt(long flags, long minOffset, long maxOffset, long minLength, int editDistance, int hammingDistance) {

    public static DualExpressionExt empty() {
        return new DualExpressionExt(0, 0, -1L, 0, 0, 0);
    }
}
