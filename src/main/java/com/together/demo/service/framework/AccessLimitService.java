package com.together.demo.service.framework;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.stereotype.Service;

/**
 * @author maxiaoguang
 */
@Service
public class AccessLimitService {

    /**
     * 每秒只发出50个令牌
     */
    RateLimiter rateLimiter = RateLimiter.create(5);

    /**
     * 尝试获取令牌
     * @return
     */
    public boolean tryAcquire(){
        return rateLimiter.tryAcquire();
    }
}
