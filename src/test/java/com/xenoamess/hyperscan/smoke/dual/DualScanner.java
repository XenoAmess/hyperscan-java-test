package com.xenoamess.hyperscan.smoke.dual;

public interface DualScanner extends AutoCloseable {

    long getSize();

    @Override
    void close();
}
