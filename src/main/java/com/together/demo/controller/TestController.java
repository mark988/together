package com.together.demo.controller;

import com.together.demo.exception.GlobalException;
import com.together.demo.pojo.vo.Result;
import com.together.demo.service.AccessLimitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *测试接口
 *  
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

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

    @Autowired
    private AccessLimitService accessLimitService;

    @RequestMapping(value = "/test5",method = RequestMethod.GET)
    public String access(){
        //尝试获取令牌
        if(accessLimitService.tryAcquire()){
            //模拟业务执行200毫秒
            try {
                Thread.sleep(200);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            log.info("access ok");
            return "aceess success [ok]";
        }else{
            log.info("请求速度太快了...");
            return "请求速度太快了...";
        }
    }
}