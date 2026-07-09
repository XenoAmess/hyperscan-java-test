package com.xenoamess.hyperscan.smoke.dual;

public record DualMatch(DualExpression matchedExpression, Integer id, long start, long end, String matchedString) {
}
