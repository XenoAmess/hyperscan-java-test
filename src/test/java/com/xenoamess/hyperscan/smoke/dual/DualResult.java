package com.xenoamess.hyperscan.smoke.dual;

public record DualResult<T>(int code, T value, String message) {

    public boolean isSuccess() {
        return code == 0;
    }

    public static <T> DualResult<T> success(T value) {
        return new DualResult<>(0, value, null);
    }

    public static <T> DualResult<T> error(int code, String message) {
        return new DualResult<>(code, null, message);
    }

    public static <T> DualResult<T> error(int code) {
        return new DualResult<>(code, null, null);
    }
}
