package com.xenoamess.hyperscan.smoke.dual;

public interface DualDatabase extends AutoCloseable {

    long getSize();

    @Override
    void close();
}
