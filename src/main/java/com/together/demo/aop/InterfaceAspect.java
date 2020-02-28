package com.together.demo.aop;

import com.alibaba.fastjson.JSON;
import com.together.demo.annotation.EagleEye;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Slf4j
@Aspect //定义切面
@Component
public class InterfaceAspect {

    @Pointcut("@annotation(com.together.demo.annotation.EagleEye)")
    public void eagleEye() {

    }

    @Around("eagleEye()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        long begin =System.currentTimeMillis();

        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request =attributes.getRequest();
        Signature signature = pjp.getSignature();;
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method =  methodSignature.getMethod();
        EagleEye eagleEye  = method.getAnnotation(EagleEye.class);
        //接口描述信息
        String desc =  eagleEye.desc();
        log.info("================请求开始==================");
        log.info("请求链接：{}",request.getRequestURL().toString());
        log.info("请求描述：{}",desc);
        log.info("请求类型: {}",request.getMethod());
        log.info("请求方法：{},{}",signature.getDeclaringTypeName(),signature.getName());
        log.info("请求入参：{}", JSON.toJSONString(pjp.getArgs()));
        Object result = pjp.proceed();
        long end = System.currentTimeMillis();
        log.info("请求耗时:{} 毫秒",end-begin);
        log.info("返回结果:{}",JSON.toJSONString(result));
        log.info("================请求结束==================");
        return result;
    }
}
