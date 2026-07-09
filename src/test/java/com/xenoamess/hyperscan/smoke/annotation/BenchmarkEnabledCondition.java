package com.xenoamess.hyperscan.smoke.annotation;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

public class BenchmarkEnabledCondition implements ExecutionCondition {

    private static final String PROPERTY = "hyperscan.benchmarks.enabled";

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        String value = System.getProperty(PROPERTY, "true");
        if ("false".equalsIgnoreCase(value)) {
            return ConditionEvaluationResult.disabled("Benchmark disabled by -D" + PROPERTY + "=false");
        }
        return ConditionEvaluationResult.enabled("Benchmark enabled (default); pass -D" + PROPERTY + "=false to skip");
    }
}
