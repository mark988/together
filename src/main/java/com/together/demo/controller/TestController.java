package com.together.demo.controller;

import com.together.demo.annotation.AutoIdempotent;
import com.together.demo.annotation.EagleEye;
import com.together.demo.designPattern.strategy.TaxProcess;
import com.together.demo.designPattern.strategy.TaxProcessContext;
import com.together.demo.exception.GlobalException;
import com.together.demo.pojo.vo.Result;
import com.together.demo.service.framework.AccessLimitService;
import com.together.demo.service.framework.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *测试接口
 *
 * @author maxiaoguang
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

    @Autowired
    private TaxProcessContext innerCommandContext;

    /**
     * ce测试策略模式
     * @param country
     * @return
     */
    @RequestMapping(value = "/v6/{country}",method = RequestMethod.GET)
    public String test1(@PathVariable String country){
        TaxProcess instance = innerCommandContext.getInstance(country);
        instance.process(country);
        return "OK";
    }


    @EagleEye(desc = "v7 controller")
    @RequestMapping(value = "/v7",method = RequestMethod.GET)
    public String test2(){

        return "OK";
    }

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/getToken",method = RequestMethod.GET)
    public Result getToken(){
        return Result.success(tokenService.createToken());
    }

    /**
     * 使用幂等性注解
     * @return
     */
    @AutoIdempotent
    @RequestMapping(value = "/go",method = RequestMethod.POST)
    public Result test1(){
        log.info(" request ...");
        return Result.success();
    }
}
