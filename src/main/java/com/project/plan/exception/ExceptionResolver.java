package com.project.plan.exception;

import com.project.plan.common.JsonResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Barry on 2018/4/27.
 */
@ControllerAdvice(annotations = {ResponseBody.class})
public class ExceptionResolver {
    //    @ResponseBody
    @ExceptionHandler(NumberFormatException.class)
    public JsonResult processException(Exception e) {
        System.out.println("-------exception-请求出错-------");
        JsonResult result = JsonResult.failure(e.getMessage());
        return result;
    }
}