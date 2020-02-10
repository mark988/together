package com.together.demo.pojo.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 微信下单请求VO
 * @author mark
 */
@Data
public class WechatPayReqVo implements Serializable {
    /**
     * 用户ID
     * */
    @NotNull
    private Integer uid;
    /**
     * 用户openID
     * */
    @NotNull
    private String openId;

    private String orderSN;
    /**
     * 本系统的订单号
     * */
    @NotNull
    private String outTradeNo;
    /**
     * 商品名称
     * */
    @NotNull
    private String body;
    /**
     * 商品价格
     * */
    @NotNull
    private Double totalFee;

    @NotNull
    private String spbillCreateIp;
}
