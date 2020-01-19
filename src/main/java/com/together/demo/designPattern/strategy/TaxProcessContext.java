package com.together.demo.designPattern.strategy;

import com.together.demo.config.ApplicationContextHelper;
import com.together.demo.pojo.enums.TaxCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * 通过输入不同的代号就能获取到具体的实现
 * @author maxiaoguang
 */
@Slf4j
@Component
public class TaxProcessContext {

    public TaxProcess getInstance(String command) {
        Map<String, String> allClazz = TaxCodeEnum.getAllClazz();
        String clazz = allClazz.get(command.trim());
        TaxProcess taxProcess = null;
        try {
            if (StringUtils.isEmpty(clazz)) {
                clazz = null;
            }
            taxProcess = (TaxProcess) ApplicationContextHelper.getBean(Class.forName(clazz));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taxProcess;
    }
}
