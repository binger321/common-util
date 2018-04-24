package com.binger.common.exception;

import com.binger.common.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created with IntelliJ IDEA.
 * User: zhuyubin
 * Date: 2018/3/26 0026
 * Time: 14:33
 * To change this template use File | Settings | File Templates.
 * Description: 异常处理器，在controller层捕获异常，并且打印日志
 */
@ControllerAdvice
public class RestExceptionHandler {
    Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    /**
     * 捕获异常异常 处理500的错误
     * @param ex
     * @return
     */
    @ExceptionHandler
    @ResponseBody
    public ServerResponse<Object> handleExceptionInternal(Exception ex) {
        if (ex instanceof MissingServletRequestParameterException) {
            return new ServerResponse<>(400, "缺少参数");
        }
        if (ex instanceof TypeMismatchException) {
            return new ServerResponse<>(400, "参数类型不正确");
        }
        if (ex instanceof HttpMessageNotReadableException) {
            logger.error(ex.getClass().getName(), ex.getMessage());
            return new ServerResponse<>(500, ex.getMessage());
        }
        if (ex instanceof DataIntegrityViolationException) {
            logger.error(ex.getClass().getName(), ex.getMessage());
            return new ServerResponse<>(500, ex.getMessage());
        }
        if (ex instanceof BusinessException) {
            logger.error(ex.getClass().getName(), ex.getMessage());
            return new ServerResponse<>(500, ex.getMessage());
        }
        if (ex instanceof NullPointerException) {
            logger.error(ex.getClass().getName(), ex.getMessage());
            return new ServerResponse<>(500, "空指针异常");
        }
        logger.error(ex.getClass().getName(), ex.getMessage());
        return new ServerResponse<>(500, "服务器出错，请看后台日志");
    }


}