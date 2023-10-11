package com.atguigu.spzx.common.exception;

import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    //quanju yichang chuli
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(){
        return Result.build(null, ResultCodeEnum.DATA_ERROR);
    }

    @ExceptionHandler(GguiguException.class)
    @ResponseBody
    public Result error(GguiguException e){
        return Result.build(null, e.getResultCodeEnum());
    }

}
