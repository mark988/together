package com.together.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 负责管理 beans 的完整生命周期
 * 这个类一定要放在service包前面，如果放在后面会导致service已经初始化完成但是setApplicationContext
 * 后执行。Springboot启动的时候从上至下依次扫描各个包。这个地方在（lazy=false情况下）与没有关系
 * @author maxiaoguang
 */
@Slf4j
@Component
public class ApplicationContextHelper implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public ApplicationContextHelper() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHelper.applicationContext = applicationContext;
    }

    public static Object getBean(String beanName) {
        return applicationContext != null?applicationContext.getBean(beanName):null;
    }
}
