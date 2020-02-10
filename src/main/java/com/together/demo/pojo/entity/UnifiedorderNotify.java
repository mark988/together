package com.together.demo.pojo.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 支付结束后回调结果表
 * @author mark
 */
@Builder
@Data
public class UnifiedorderNotify {
    private Integer  id;
    private String   appid;
    private String   attach;
    private String   bankType;
    private String   feeType;
    private String   isSubscribe ;
    private String   mchId ;
    private String   nonceStr ;
    private String   openid ;
    private String   outTradeNo ;
    private String   resultCode ;
    private String   returnCode ;
    private String   sign ;
    private String   timeEnd ;
    private String   totalFee ;
    private String   couponFee ;
    private String   couponCount ;
    private String   couponType ;
    private String   couponId ;
    private String   tradeType ;
    private String   transactionId ;
    private Date  createTime ;
}
