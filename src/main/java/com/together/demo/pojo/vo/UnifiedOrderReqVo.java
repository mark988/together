package com.together.demo.pojo.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 统一下单请求对象
 * @author maxiaoguang
 */
@Data
public class UnifiedOrderReqVo implements Serializable {
    /**
     * 订单号  主键
     * */
    //private String outTradeNo;

    private String orderSN;

    /**
     * 商品 主键
     * */
    private Integer productId;

    /**
     * 商品名称
     */
    @NotNull
    private String body;

    /**
     * 数量
     * */
    @NotNull
    private Integer number;

    /**
     * 单位 -1 未定义 0-箱 1-个
     * */
    private Integer unit;

    /**
     * 商品单价
     */
    private Double unitPrice;

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

    /**
     * 收货人
     */
    @NotNull
    private String receiver;
    /**
     * 收货人电话
     */
    @NotNull
    private String receiverPhone;
    /**
     * 下单用户地址
     * */
    @NotNull
    private String receiverAddress;


}
