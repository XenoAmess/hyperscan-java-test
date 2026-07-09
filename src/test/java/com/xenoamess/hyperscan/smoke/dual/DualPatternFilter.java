package com.xenoamess.hyperscan.smoke.dual;

import java.util.List;
import java.util.regex.Matcher;

public interface DualPatternFilter extends AutoCloseable {

    List<Matcher> filter(String input);

    @Override
    void close();
}
