package cn.sicnu.cs.employment.exception;


import cn.sicnu.cs.employment.common.ResultInfo;
import cn.sicnu.cs.employment.common.ResultInfoUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import static cn.sicnu.cs.employment.common.util.RequestUtil.getCurrentUrl;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResultInfo<CustomException> customException( CustomException exception){
        return ResultInfoUtil.buildError(exception.getCode(),exception.getMessage(), getCurrentUrl());
    }

}