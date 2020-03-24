package com.together.demo.disruptor;

import com.alibaba.fastjson.JSONObject;
import com.lmax.disruptor.EventHandler;
import com.together.demo.pojo.entity.Order;
import com.together.demo.pojo.entity.Product;
import com.together.demo.pojo.vo.Business;
import com.together.demo.pojo.vo.Information;
import com.together.demo.pojo.vo.OrderEvent;
import com.together.demo.service.OrderService;
import com.together.demo.service.ProductService;
import com.together.demo.service.framework.WebSocketServer;
import com.together.demo.config.ApplicationContextHelper;
import com.together.demo.pojo.enums.CommonEnum;
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
    private ProductService productService=(ProductService)ApplicationContextHelper.getBean("productService");

    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {
        log.info(" 消费者处理数据:{} , seq:{} endOfBatch:{} ",event.getProductId(),sequence,endOfBatch );
        Information information= new Information();
        Product product = productService.findProductById(event.getProductId());
        information.setBusiness(new Business(CommonEnum.BUSINESS_SHOP,CommonEnum.MESSAGE_TYPE_TIPS.getCode()));
        if(product==null){
            information.setText("产品ID不存在...");
            WebSocketServer.sendInfo(JSONObject.toJSONString(information),event.getUserId()+"");
            return;
        }
        product.setNumber(-1);
        int count = productService.updateProduct(product);
        if (count==0){
            information.setText("产品已经没有库存啦...");
            WebSocketServer.sendInfo(JSONObject.toJSONString(information),event.getUserId()+"");
            return;
        }
        Order o= new Order();
        o.setProductId (event.getProductId());
        o.setUserId(event.getUserId());
        orderService.saveOrder(o);
        information.setText("下单成功");

        information.setBusiness(new Business(CommonEnum.BUSINESS_SHOP,CommonEnum.MESSAGE_TYPE_NOTICE.getCode()));
        WebSocketServer.sendInfo(JSONObject.toJSONString(information),event.getUserId()+"");
    }

    public int getCount(){
        return count.get();
    }
}
