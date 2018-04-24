package com.binger.common.exception;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: zhuyubin
 * Date: 2018/3/26 0026
 * Time: 14:33
 * To change this template use File | Settings | File Templates.
 * Description:
 */
@Data
public class BusinessException extends RuntimeException {
    final static int NormalBusiCode = 001;
    private Integer errorCode;

    public BusinessException() {}

    public BusinessException(Integer errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public static BusinessException create(String message) {
        return new BusinessException(NormalBusiCode, message);
    }

}