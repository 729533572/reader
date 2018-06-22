package com.yn.reader.model;

/**
 * Created by sunxy on 2018/1/8.
 */

public class BaseData {
    private int status;
    private int err_code;
    private String err_msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getErr_code() {
        return err_code;
    }

    public void setErr_code(int err_code) {
        this.err_code = err_code;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }

    @Override
    public String toString() {
        return status+"";
    }
}
