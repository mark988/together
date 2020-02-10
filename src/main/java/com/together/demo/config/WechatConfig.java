package com.together.demo.config;

public class WechatConfig {
    public static final String ATTACH="购买支付";
    /**
     * 小程序appid
     */
    public static final String APPID = "wx85aa09a9006a9abf";
    /**
     * 微信支付的商户id
     */
    public static final String MCH_ID = "1517003791";
    /**
     * 微信支付的商户密钥
     * */
    public static final String KEY = "90AF09C65D253C4CBB69757379930C7X";
    /**
     * 支付成功后的服务器回调url，这里填PayController里的回调函数地址
     */

    public static final String NOTIFY_URL="/wechat/notify";
    public static final String REFUNDNOTIFY_URL="/wechat/refundNotify";
    /**
     * 签名方式，固定值
     */
    public static final String SIGNTYPE = "MD5";
    /**
     * 交易类型，小程序支付的固定值为JSAPI
     */
    public static final String TRADETYPE = "JSAPI";
    /**
     * 微信统一下单接口地址
     * */
    public static final String UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * 微信退款
     */
    public static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
}
