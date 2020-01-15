package com.together.demo.exception;

import com.together.demo.pojo.vo.Result;
import com.together.demo.utils.CommonEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;

/**
 * @author maxiaoguang
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理自定义的业务异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = GlobalException.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest req, GlobalException e){
        return Result.error(e.getErrorCode(),e.getErrorDesc());
    }

    /**
     * 处理空指针的异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value =NullPointerException.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest req, NullPointerException e){
        return Result.error(CommonEnum.NULL_POINTER_EXCEPTION);
    }

    /**
     * 处理其他异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value =Exception.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest req, Exception e){
        return Result.error(CommonEnum.INTERNAL_SERVER_ERROR);
    }
}
