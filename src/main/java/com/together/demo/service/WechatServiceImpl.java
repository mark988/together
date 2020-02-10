package com.together.demo.service;

import com.together.demo.config.WechatConfig;
import com.together.demo.pojo.entity.Refund;
import com.together.demo.pojo.vo.Result;
import com.together.demo.pojo.vo.WechatPayReqVo;
import com.together.demo.pojo.vo.WechatRefundReqVo;
import com.together.demo.utils.ClientCustomSSL;
import com.together.demo.utils.MoneyCaculateUtil;
import com.together.demo.utils.MyUtil;
import com.together.demo.utils.PayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class WechatServiceImpl implements WechatService {

    @Value("${ngnixUrl}")
    private String NGNIX_URL;

    @Override
    public Result unifiedOrder(WechatPayReqVo vo) {
        Map<String, Object> payMap = new HashMap<String, Object>();
        if(vo == null){
            return null;
        }
        log.info(" 请求支付参数:{} ",vo.toString());
        /*if( vo.getUid() ==null ){
            return Response.error(-1,"用户为空") ;
        }
        User u = userMapper.selectByPrimaryKey(vo.getUid());
        if(u==null){
            return Response.error(-1,"非法用户") ;
        }
        if( StringUtils.isEmpty( vo.getOpenId() ) ){
            return Response.error(-1,"当前用户没有openID") ;
        }
        if( StringUtils.isEmpty(vo.getSpbillCreateIp()) ){
            return Response.error(-1,"服务器地址为空") ;
        }
        if( StringUtils.isEmpty(vo.getBody()) ){
            return Response.error(-1,"商品名称为空") ;
        }
        if( StringUtils.isEmpty(vo.getOutTradeNo()) ){
            return Response.error(-1,"订单号为空") ;
        }
        if( vo.getTotalFee()==null ){
            return Response.error(-1,"金额为空") ;
        }*/
        //返回给小程序端需要的参数
        try {
            //生成的随机字符串
            String nonce_str = MyUtil.getRandomStringByLength(32);
            //商品名称
            String body = vo.getBody();
            int price = MoneyCaculateUtil.multiply(vo.getTotalFee(),new Double(100)).intValue();
            /**
             * 组装参数，用户生成统一下单接口的签名
             */
            log.info("----------下单接口签名-------");
            Map<String, String> packageParams = new HashMap<>(20);
            packageParams.put("appid", WechatConfig.APPID);
            packageParams.put("mch_id", WechatConfig.MCH_ID);

            packageParams.put("nonce_str", nonce_str);
            packageParams.put("body", body);
            /**
             * 商户订单号,自己的订单ID
             * */
            packageParams.put("out_trade_no", vo.getOutTradeNo());
            /**
             * 支付金额，这边需要转成字符串类型，否则后面的签名会失败
             * */
            packageParams.put("total_fee",String.valueOf(price));
            packageParams.put("spbill_create_ip", vo.getSpbillCreateIp());
            /**
             * 支付成功后的回调地址
             * */
            packageParams.put("notify_url", NGNIX_URL+WechatConfig.NOTIFY_URL);
            packageParams.put("trade_type", WechatConfig.TRADETYPE);
            /**
             * 用户的openID
             **/
            packageParams.put("openid",vo.getOpenId());
            /**
             *  把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
             * */
            packageParams = PayUtil.paraFilter(packageParams);
            String prestr = PayUtil.createLinkString(packageParams);
            log.info("生成字符串:{}" , prestr);
            /**
             * MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
             */
            String mysign = PayUtil.sign(prestr, WechatConfig.KEY, "utf-8").toUpperCase();
            log.info("生成签名:{} ", mysign);
            /**
             * 拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
             */
            String xml = "<xml>" + "<appid>" + WechatConfig.APPID + "</appid>"
                    + "<body><![CDATA[" + body + "]]></body>"
                    + "<mch_id>" + WechatConfig.MCH_ID + "</mch_id>"
                    + "<nonce_str>" + nonce_str + "</nonce_str>"
                    + "<notify_url>" + NGNIX_URL+WechatConfig.NOTIFY_URL + "</notify_url>"
                    + "<openid>" + vo.getOpenId() + "</openid>"
                    + "<out_trade_no>" + vo.getOutTradeNo() + "</out_trade_no>"
                    + "<spbill_create_ip>" + vo.getSpbillCreateIp() + "</spbill_create_ip>"
                    + "<total_fee>" + price + "</total_fee>"//支付的金额，单位：分
                    + "<trade_type>" + WechatConfig.TRADETYPE + "</trade_type>"
                    + "<sign>" + mysign + "</sign>"
                    + "</xml>";
            log.info("最终的提交xml:{}",xml);
            /**
             * 调用统一下单接口，并接受返回的结果
             */
            String result = PayUtil.httpRequest(WechatConfig.UNIFIEDORDER_URL, "POST", xml);
            log.info("result:{}",result);
            /**
             *  将解析结果存储在HashMap中
             **/
            Map map = PayUtil.doXMLParse(result);
            String return_code = (String) map.get("return_code");
            String result_code = (String) map.get("result_code");
            String err_code = (String) map.get("err_code");
            String err_code_des = (String) map.get("err_code_des");

            if (return_code.equals("SUCCESS") || return_code.equals(result_code)) {
                /**
                 * 返回的预付单信息
                 * */
                String prepay_id = (String) map.get("prepay_id");
                payMap.put("nonceStr", nonce_str);
                payMap.put("package", "prepay_id=" + prepay_id);
                Long timeStamp = System.currentTimeMillis() / 1000;
                /**
                 *  这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                 * */
                payMap.put("timeStamp", timeStamp + "");
                /**
                 * 拼接签名需要的参数
                 * **/
                String stringSignTemp = "appId=" + WechatConfig.APPID + "&nonceStr=" + nonce_str + "&package=prepay_id=" + prepay_id + "&signType=MD5&timeStamp=" + timeStamp;
                /**
                 * 再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                 */
                String paySign = PayUtil.sign(stringSignTemp, WechatConfig.KEY, "utf-8").toUpperCase();
                log.info("=======================第二次签名：", paySign + "============ ======");
                payMap.put("paySign", paySign);
            } else {
                log.info("----------统一下单失败-------");
                return  Result.error ("下单失败");
            }

            payMap.put("appid", WechatConfig.APPID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        payMap.put("outTradeNo",vo.getOutTradeNo());
        payMap.put("orderSN",vo.getOrderSN());
        //return Response.body(IlinliConstant.SUCCESS,IlinliConstant.OPERATION_SUCCESS, payMap);
        return Result.success(payMap);
    }

    @Override
    public Result wxRefund(WechatRefundReqVo wechatRefundReqVo) {
       // Map<String, Object> payMap = new HashMap<String, Object>();
        //返回给小程序端需要的参数
        try {
            int totalFee = MoneyCaculateUtil.multiply(wechatRefundReqVo.getTotalFee(),new Double(100)).intValue();
            int refundFee= MoneyCaculateUtil.multiply(wechatRefundReqVo.getRefundFee(),new Double(100)).intValue();
            String nonce_str = MyUtil.getRandomStringByLength(32);

            Map<String, String> packageParams = new HashMap<>(20);
            packageParams.put("appid", WechatConfig.APPID);
            packageParams.put("mch_id", WechatConfig.MCH_ID);
            packageParams.put("nonce_str", nonce_str);


            /**
             * 商户订单号
             * */
            packageParams.put("out_trade_no", wechatRefundReqVo.getOutTradeNo());

            /**
             * 退款订单号
             * */
            packageParams.put("out_refund_no", wechatRefundReqVo.getOutRefundNo());
            /**
             * 订单金额
             * */
            packageParams.put("total_fee",String.valueOf(totalFee));
            packageParams.put("refund_fee", String.valueOf(refundFee));

            packageParams.put("notify_url",  NGNIX_URL+WechatConfig.REFUNDNOTIFY_URL );

            packageParams = PayUtil.paraFilter(packageParams);
            String prestr = PayUtil.createLinkString(packageParams);
            log.info("生成字符串:{}" , prestr);
            /**
             * MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
             */
            String mysign = PayUtil.sign(prestr, WechatConfig.KEY, "utf-8").toUpperCase();
            /**
             * 拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
             */

            String xml = "<xml>"
                    + "<appid>" + WechatConfig.APPID + "</appid>"
                    + "<mch_id>" + WechatConfig.MCH_ID + "</mch_id>"
                    + "<nonce_str>" + nonce_str + "</nonce_str>"
                    + "<sign>" + mysign + "</sign>"
                    + "<out_trade_no>" + wechatRefundReqVo.getOutTradeNo() + "</out_trade_no>"
                    + "<out_refund_no>" + wechatRefundReqVo.getOutRefundNo() + "</out_refund_no>"
                    + "<total_fee>" + totalFee + "</total_fee>"
                    + "<refund_fee>" + refundFee + "</refund_fee>"
                    + "<notify_url>" + NGNIX_URL+WechatConfig.REFUNDNOTIFY_URL + "</notify_url>"
                    + "</xml>";
            log.info("退款最终的提交xml:{}",xml);

            String result  = ClientCustomSSL.doRefund(WechatConfig.REFUND_URL, xml).toString();

            log.info("退款调用后结果=result:{}",result);
            /**
             *  将解析结果存储在HashMap中
             **/
            Map map = PayUtil.doXMLParse(result);
            String return_code = (String) map.get("return_code");
            String result_code = (String) map.get("result_code");
            if (return_code.equals("SUCCESS") && return_code.equals("SUCCESS")) {
                /**
                 * 成功返回的信息
                 * */
                Refund o = new Refund();
                o.setReturnCode((String) map.get("return_code"));
                o.setReturnMsg((String) map.get("return_msg"));
                o.setAppId((String) map.get("appid"));
                o.setMchId((String) map.get("mch_id"));
                o.setNonceStr((String) map.get("nonce_str"));
                o.setSign((String) map.get("sign"));
                o.setResultCode((String) map.get("result_code"));
                o.setTransactionId((String) map.get("transaction_id"));
                o.setOutTradeNo((String) map.get("out_trade_no"));
                o.setOutRefundNo((String) map.get("out_refund_no"));
                o.setRefundChannel((String) map.get("refund_channel"));
                o.setRefundFee((String) map.get("refund_fee"));
                //变更订单状态为完成
                //商户金额-去退款金额
            } else {
                String  errCodeDes = (String) map.get("err_code_des");
                return Result.error(" 退款失败 ");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(" 退款失败 ");
        }
        return  Result.success("退款成功");
    }
}
