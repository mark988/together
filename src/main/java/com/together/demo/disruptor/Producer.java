package com.together.demo.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.together.demo.pojo.vo.OrderEvent;

public class Producer {

    private RingBuffer<OrderEvent> ringBuffer;

    public Producer(RingBuffer<OrderEvent> ringBuffer){
        this.ringBuffer = ringBuffer;
    }

    public void onData(int productId,int userId){
       long seq =  ringBuffer.next();
       try {
           OrderEvent orderEvent =  ringBuffer.get(seq);
           orderEvent.setProductId(productId);
           orderEvent.setUserId(userId);
       }finally {
           ringBuffer.publish(seq);
       }

    }
}
