package com.together.demo.controller;

import com.lmax.disruptor.dsl.Disruptor;
import com.together.demo.disruptor.Consumer;
import com.together.demo.disruptor.DisruptorService;
import com.together.demo.disruptor.Producer;
import com.together.demo.pojo.entity.Order;
import com.together.demo.pojo.entity.Product;
import com.together.demo.pojo.vo.OrderEvent;
import com.together.demo.pojo.vo.Result;
import com.together.demo.service.OrderService;
import com.together.demo.service.ProductService;
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


    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    /**
     * 使用disruptor 作为队列下单,使用websocket 通知
     * 使用测试 ab -n 1000 -c 1000 http://localhost:8080/disruptor/order/1/7 不会出现超卖情况
     * @param productId
     * @param userId
     */
    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "/order/{productId}/{userId}",method = RequestMethod.GET)
    public  void  createOrder(@PathVariable int  productId,@PathVariable int userId ) {
        Disruptor<OrderEvent> disruptor = disruptorService.startDisruptor();
        Producer p = new Producer(disruptor.getRingBuffer());
        p.onData(productId, userId);
    }

    /**
     * 普通下单作业流程，与上面做一个对比
     * ab -n 10 -c 10 http://localhost:8080/disruptor/1/7 测试结果只卖出两个，其他都没有卖出去
     * @param productId
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "/{productId}/{userId}",method = RequestMethod.GET)
    public Result order(@PathVariable int  productId, @PathVariable int userId ){
        Product product = productService.findProductById(productId);
        product.setNumber(-1);
        int count = productService.updateProduct(product);
        if (count==0){
            log.info(" 卖完了");
            return Result.error("卖完了");
        }
        Order o= new Order();
        o.setProductId (productId);
        o.setUserId(userId);
        orderService.saveOrder(o);

        return Result.success();
    }
}
