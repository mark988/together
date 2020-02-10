package com.together.demo.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 微信退款结果表
 * @author maxiaoguang
 */
@Data
public class RefundNotify {
    private Integer id;
    private String transactionId;
    private String outTradeNo ;
    private String refundId ;
    private String outRefundNo ;
    private Integer totalFee;
    private Integer refundFee;
    private Integer settlementRefundFee ;
    private String refundStatus ;
    private String successTime ;
    private String refundRecvAccout ;
    private String refundAccount ;
    private String refundRequestSource ;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
