package com.together.demo.designPattern.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaxForCN implements TaxProcess {
    @Override
    public void process(String msg) {
        log.info("中国税率");
    }
}
