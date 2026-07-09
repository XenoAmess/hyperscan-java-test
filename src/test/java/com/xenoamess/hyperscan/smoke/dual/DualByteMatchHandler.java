package com.xenoamess.hyperscan.smoke.dual;

@FunctionalInterface
public interface DualByteMatchHandler {

    boolean onMatch(DualExpression expression, long from, long to);
}
