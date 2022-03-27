package com.mwt.result;

public enum ResultCode {
    /**
     * 服务器常用
     */
    SUCCESS("000", "成功"),

    FAIL("001", "服务器异常"),

    PARAMETER_EXCEPTION("002", "参数异常"),

    /**
     * Ａ开头，用户问题
     */
    NO_AUTH("A00", "用户权限不足"),
    /**
     * B开头，数据库问题
     */
    DB_NOT_CONNECTION("B00", "数据库未连接"),

    /**
     * D开头的表示，设备异常
     */
    DEV_NOT_EXIST("D00", "不存在的设备");

    private String code;
    private String message;

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    public static String getMessage(String code) {
        for (ResultCode item : ResultCode.values()) {
            if (item.code().equals(code)) {
                return item.message;
            }
        }
        return "";
    }

    public static String getCode(String name) {
        for (ResultCode item : ResultCode.values()) {
            if (item.name().equals(name)) {
                return item.name();
            }
        }
        return "";
    }
}
