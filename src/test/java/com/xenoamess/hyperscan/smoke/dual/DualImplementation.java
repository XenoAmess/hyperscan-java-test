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
    };

    public abstract DualApi createAdapter();

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
