package com.gansj.bilibili.common;

public enum ErrorCode {


    SUCCESS(0,"ok",""),
    NOT_LOGIN(40100,"未登录",""),
    NO_AUTH(40101,"无权限",""),
    REQUEST_PARAMS_ERROR(40102,"用户请求参数错误",""),
    TOKEN_EXPIRED(40200,"token过期",""),
    FILE_UPLOAD_ERROR(40301,"文件上传失败",""),
    FILE_ERROR(40300,"非法文件",""),
    VIDEO_ERROR(40302,"非法视频",""),
    SYSTEM_ERROR(50000,"系统内部异常","")
    ;

    private final int code;

    private final String msg;

    private final String description;

    ErrorCode(int code, String msg, String description) {
        this.code = code;
        this.msg = msg;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getDescription() {
        return description;
    }
}
