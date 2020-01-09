package com.together.demo.controller;

import com.lmax.disruptor.dsl.Disruptor;
import com.together.demo.disruptor.Consumer;
import com.together.demo.disruptor.DisruptorService;
import com.together.demo.disruptor.Producer;
import com.together.demo.pojo.vo.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 使用disruptor 做队列
 */

@Slf4j
@RequestMapping("/disruptor")
@RestController
public class DisruptorController {

    @Autowired
    private DisruptorService disruptorService;

    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "/order/{productId}/{userId}",method = RequestMethod.GET)
    public  void  createOrder(@PathVariable int  productId,@PathVariable int userId ) {
        Disruptor<OrderEvent> disruptor = disruptorService.startDisruptor();
        Producer p = new Producer(disruptor.getRingBuffer());
        p.onData(productId, userId);
    }
}
