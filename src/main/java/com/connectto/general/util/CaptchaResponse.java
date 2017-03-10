package com.connectto.general.util;

/**
 * Created by htdev01 on 11/26/15.
 */
public class CaptchaResponse {
    public boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "CaptchaResponse{" +
                "success=" + success +
                '}';
    }
}
