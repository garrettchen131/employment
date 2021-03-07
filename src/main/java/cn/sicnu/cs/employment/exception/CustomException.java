package cn.sicnu.cs.employment.exception;

import cn.sicnu.cs.employment.common.Constants;

/**
 * @author CaiKe
 * @create 2021/3/7
 */
public class CustomException extends RuntimeException {
    //异常错误编码
    private int code ;
    //异常信息
    private String message;

    public CustomException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}