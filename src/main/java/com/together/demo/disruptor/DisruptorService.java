package com.together.demo.disruptor;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.together.demo.pojo.vo.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * disruptor 容器服务 初始化生成disruptor和关闭disruptor
 * @author maxiaoguang
 */
@Slf4j
@Component
public class DisruptorService {

    /**
     *ExecutorService executorService= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
     */

    /**
     * 使用下面的方式创建线程
     */
    private static Integer processors = Runtime.getRuntime().availableProcessors();
    private static ThreadPoolExecutor executorService  = new ThreadPoolExecutor(processors, processors+1, 10L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10000));

    Disruptor<OrderEvent> disruptor = new Disruptor<OrderEvent>(new EventFactory<OrderEvent>() {
        @Override
        public OrderEvent newInstance() {
            return new OrderEvent();
        }
    }, 1024, executorService, ProducerType.SINGLE, new YieldingWaitStrategy());

    @PostConstruct
    public void init(){
        log.info("============初始化disruptor============ ");
        disruptor.handleEventsWith(new Consumer());
        disruptor.start();
    }

    public Disruptor<OrderEvent> startDisruptor(){
        return disruptor;
    }

    @PreDestroy
    void shutdownDisruptor(){
        disruptor.shutdown();
        executorService.shutdown();
        log.info(" disruptor和线程组关闭 ...");
    }
}
