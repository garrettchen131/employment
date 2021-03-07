package cn.sicnu.cs.employment.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.zalando.problem.spring.web.advice.ProblemHandling;

@ControllerAdvice
public class ExceptionHandler implements ProblemHandling {
    @Override
    public boolean isCausalChainsEnabled() {
        return true;
    }

//    @org.springframework.web.bind.annotation.ExceptionHandler(CustomException.class)
//    @ResponseBody
//    public ResultInfo<CustomException> customException(String path, CustomException exception){
//        return ResultInfoUtil.buildError(exception.getCode(),exception.getMessage(),path);
//    }
}
