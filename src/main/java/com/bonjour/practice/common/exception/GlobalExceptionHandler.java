package com.bonjour.practice.common.exception;

import com.bonjour.practice.common.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @authur tc
 * @date 2023/7/24 11:06
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    private String defaultMessage = "系统出错，请与系统管理员联系!";

    //	@Override
    @ExceptionHandler(Exception.class)
    public Result handleException(final Throwable ex) {
        String msg = this.defaultMessage;
        log.error(ex.toString());
        if (ex instanceof DataAccessException) {
            msg = ex.getMessage();
            if (msg == null || msg.equals("")) {
                msg = this.defaultMessage;
            }
        } else if (ex instanceof NullPointerException) {
            msg = "空指针异常！异常发生在：" + ex.getStackTrace()[0].toString();
        } else {
            msg = ex.getMessage();
            if (msg == null || msg.equals("")) {
                msg = ex.getClass().getName() + ": " + ex.getMessage();
            }
        }
        return Result.error(msg);
    }
}
