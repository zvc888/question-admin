package com.question.admin.utils;

public class ErrorUtil {
    public static String parameterErrorFormat(String field, String msg) {
        return "【" + field + " : " + msg + "】";
    }
}
