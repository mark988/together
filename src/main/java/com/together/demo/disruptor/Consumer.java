package com.together.demo.disruptor;

import com.alibaba.fastjson.JSONObject;
import com.lmax.disruptor.EventHandler;
import com.together.demo.dao.DynamicDao;
import com.together.demo.pojo.entity.Order;
import com.together.demo.pojo.vo.Information;
import com.together.demo.pojo.vo.OrderEvent;
import com.together.demo.service.OrderService;
import com.together.demo.service.WebSocketServer;
import com.together.demo.config.ApplicationContextHelper;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 消费者
 * @author maxiaoguang
 */
@Slf4j
public class Consumer implements EventHandler<OrderEvent> {

    private static AtomicInteger count = new AtomicInteger(0);

    private  OrderService orderService = (OrderService) ApplicationContextHelper.getBean("orderService");


    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {
        log.info(" 消费者处理数据:{} , seq:{} endOfBatch:{} ",event.getProductId(),sequence,endOfBatch );
        Order o= new Order();
        o.setProductId (event.getProductId());
        o.setUserId(event.getUserId());
        orderService.saveOrder(o);
        count.incrementAndGet();
        Information information= new Information();
        information.setText("下单成功");
        information.setType(1);
        WebSocketServer.sendInfo(JSONObject.toJSONString(information),event.getUserId()+"");
    }

    public int getCount(){
        return count.get();
    }
}
