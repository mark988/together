package com.together.demo.pojo.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 微信退款请求VO
 * @author mark
 */
@Data
public class WechatRefundReqVo implements Serializable {

    /**
     * 本系统的订单号
     * */
    @NotNull
    private String outTradeNo;

    /**
     * 商户退款单号
     */
    @NotNull
    private String outRefundNo;
    /**
     * 订单金额
     * */
    @NotNull
    private Double totalFee;
    /**
     * 退款金额
     */
    @NotNull
    private Double refundFee;

    @NotNull
    private Integer orderVersion;

}
