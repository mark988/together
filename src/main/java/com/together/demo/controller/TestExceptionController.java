package com.together.demo.controller;

import com.together.demo.exception.GlobalException;
import com.together.demo.pojo.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 *测试接口
 *  
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestExceptionController {

    @GetMapping("/v1")
    public boolean globalException() {
        System.out.println("开始新增...");
        //如果姓名为空就手动抛出一个自定义的异常！
        if(null==null){
            throw  new GlobalException(-1,"用户姓名不能为空！");
        }
        return true;
    }

    @GetMapping("/v2")
    public Result nullPointerException() {
        System.out.println("开始更新...");
        //这里故意造成一个空指针的异常，并且不进行处理
        String str=null;
        try{
            str.equals("111");
        }catch (NullPointerException e){
            log.info(" 异常：{} ",e.getMessage());
            throw new NullPointerException();
        }
        return Result.success();
    }

    @GetMapping("/v3")
    public boolean delete()  {
        System.out.println("开始删除...");
        //这里故意造成一个异常，并且不进行处理
        Integer.parseInt("abc123");
        return true;
    }
}
