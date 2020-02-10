package com.together.demo.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 微信退款结果表
 * @author maxiaoguang
 */
@Data
public class Refund {
    private Integer id;
    private String returnCode;
    private String returnMsg ;
    private String appId ;
    private String mchId ;
    private String nonceStr;
    private String sign ;
    private String resultCode ;
    private String transactionId ;
    private String outTradeNo ;
    private String outRefundNo ;
    private String refundChannel ;
    private String refundId ;
    private String refundFee;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
