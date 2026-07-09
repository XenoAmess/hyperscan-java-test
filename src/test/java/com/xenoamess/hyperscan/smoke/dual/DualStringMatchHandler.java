package com.xenoamess.hyperscan.smoke.dual;

@FunctionalInterface
public interface DualStringMatchHandler {

    boolean onMatch(DualExpression expression, long from, long to);
}
