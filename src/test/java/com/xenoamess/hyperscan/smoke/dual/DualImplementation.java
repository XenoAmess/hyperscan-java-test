package com.xenoamess.hyperscan.smoke.dual;

public enum DualImplementation {
    JAVACPP {
        @Override
        public DualApi createAdapter() {
            return new JavaCppAdapter();
        }
    },
    PANAMA {
        @Override
        public DualApi createAdapter() {
            return new PanamaAdapter();
        }
    },
    /**
     * Upstream (gliwka) native, benchmark-only third comparison target.
     * Uses the same JNI bindings as JAVACPP; selected via
     * -Dhyperscan.benchmark.implementation=UPSTREAM together with
     * -Dnative.flavor=gliwka. Not part of dual-run functional tests.
     */
    UPSTREAM {
        @Override
        public DualApi createAdapter() {
            return new JavaCppAdapter();
        }
    };

    public abstract DualApi createAdapter();

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
