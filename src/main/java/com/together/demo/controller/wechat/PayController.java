package com.together.demo.controller.wechat;

import com.together.demo.pojo.entity.RefundNotify;
import com.together.demo.pojo.entity.UnifiedorderNotify;
import com.together.demo.pojo.vo.Result;
import com.together.demo.pojo.vo.UnifiedOrderReqVo;
import com.together.demo.pojo.vo.WechatPayReqVo;
import com.together.demo.pojo.vo.WechatRefundReqVo;
import com.together.demo.service.WechatService;
import com.together.demo.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;


@Slf4j
@RequestMapping("/wechat")
@RestController
public class PayController {

    @Autowired
    private WechatService wechatService;


    /**
     * 微信下单接口
     * @param unifiedOrderReqVo
     * **/
    @RequestMapping(value = "/unifiedorder", method = RequestMethod.POST)
    public Result unifiedOrder(@RequestBody UnifiedOrderReqVo unifiedOrderReqVo, HttpServletRequest request) {
        log.info(" 统一下单参数:{}",unifiedOrderReqVo);

        WechatPayReqVo vo = new WechatPayReqVo();
        BeanUtils.copyProperties(unifiedOrderReqVo, vo);

        vo.setSpbillCreateIp(MyUtil.getIpAddr(request));
        vo.setTotalFee(MoneyCaculateUtil.multiply(unifiedOrderReqVo.getUnitPrice().doubleValue(), new Double(unifiedOrderReqVo.getNumber())));
        Result response = wechatService.unifiedOrder(vo);
        return response;
    }

    /**
     * 这里是支付回调接口，微信支付成功后会自动调用
     */
    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    public void unifiedOrderNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info(" -----------------------开始回调----------------------- ");
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        //sb为微信返回的xml
        log.info(" sb:{} ",sb.toString());
        String notityXml = sb.toString();
        String resXml = "";
        Map map = PayUtil.doXMLParse(notityXml);
        String returnCode = (String) map.get("return_code");
        if ("SUCCESS".equals(returnCode)) {
            log.info(" ---------成功---------");
            /**
             * 验证签名是否正确
             * 回调验签时需要去除sign和空值参数
             */
            Map<String, String> validParams = PayUtil.paraFilter(map);
            String prestr = PayUtil.createLinkString(validParams);
            /**
             *  根据微信官网的介绍，此处不仅对回调的参数进行验签，还需要对返回的金额与系统订单的金额进行比对等
             */
            String sign = (String) map.get("sign");
            log.info(" prestr:{}",prestr);
            log.info(" sign:{}",sign);
            // if (PayUtil.verify(prestr, sign, WechatConfig.KEY, "utf-8")) {
            /**注意要判断微信支付重复回调，支付成功后微信会重复的进行回调**/
            UnifiedorderNotify o = new UnifiedorderNotify();
            o.setAppid((String) map.get("appid"));
            o.setAttach((String) map.get("attach"));
            o.setBankType((String) map.get("bank_type"));
            o.setTotalFee((String)map.get("total_fee"));
            o.setCouponCount((String) map.get("coupon_count"));
            o.setCouponFee((String) map.get("coupon_fee"));
            o.setCouponId((String) map.get("coupon_id"));
            o.setCouponType((String) map.get("coupon_type"));
            o.setOutTradeNo((String) map.get("out_trade_no"));
            o.setTransactionId((String) map.get("transaction_id"));
            o.setFeeType((String) map.get("fee_type"));
            o.setIsSubscribe((String) map.get("is_subscribe"));
            o.setMchId((String) map.get("mch_id"));
            o.setNonceStr((String) map.get("nonce_str"));
            o.setResultCode((String) map.get("result_code"));
            o.setReturnCode((String) map.get("return_code"));
            o.setTimeEnd((String) map.get("time_end"));
            o.setTradeType((String) map.get("trade_type"));
            //回调结果保存
            //unifiedorderNotifyService.addUnifiedorderNotify(o);
            /**
             * 处理业务逻辑 更新订单状态
             * */

            log.info("返回给微信提示成功");
            resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                    + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
        } else {
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

    /**
     * 退款接口
     * @param wechatRefundReqVo
     * @return
     */
    @RequestMapping(value = "/refund",method = RequestMethod.POST)
    public Result wxRefund(@RequestBody WechatRefundReqVo wechatRefundReqVo){
        wechatRefundReqVo.setOutRefundNo(UUIDUtil.get32UUID());
        return  wechatService.wxRefund(wechatRefundReqVo);
    }


    /**
     *  退款回调接口
     */
    @RequestMapping(value = "/refundNotify", method = RequestMethod.POST)
    public void refundNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        //sb为微信返回的xml
        String notityXml = sb.toString();
        log.info("订单回调发过来数据:{}" ,notityXml);
        String resXml = "";

        Map map = PayUtil.doXMLParse(notityXml);

        String returnCode = (String) map.get("return_code");

        if ("SUCCESS".equals(returnCode)) {

            String reqInfo = (String) map.get("req_info");
            String decryptData = AESUtil.decryptData(reqInfo);
            Map<String, String> decryptDataMap = PayUtil.doXMLParse(decryptData);

            // 微信订单号
            String transactionId = decryptDataMap.get("transaction_id");

            // 商户系统内部的订单号
            String outTradeNo = decryptDataMap.get("out_trade_no");

            // 微信退款单号
            String refundId = decryptDataMap.get("refund_id");

            // 商户退款单号
            String outRefundNo = decryptDataMap.get("out_refund_no");

            //订单金额
            String totalFee = decryptDataMap.get("total_fee");

            // 申请退款金额
            String refundFee = decryptDataMap.get("refund_fee");

            //退款金额
            String settlementRefundFee = decryptDataMap.get("settlement_refund_fee");

            //  退款状态
            String refundStatus = decryptDataMap.get("refund_status");

            //退款成功时间
            String successTime = decryptDataMap.get("success_time");

            // 退款入账账户
            String refundRecvAccout = decryptDataMap.get("refund_recv_accout");

            //退款资金来源
            String refundAccount = decryptDataMap.get("refund_account");

            //退款发起来源
            String refundRequestSource = decryptDataMap.get("refund_request_source");
            RefundNotify r = new RefundNotify();
            r.setRefundRequestSource(refundRequestSource);
            r.setRefundAccount(refundAccount);
            r.setRefundRecvAccout(refundRecvAccout);
            r.setSuccessTime(successTime);
            r.setRefundStatus(refundStatus);
            r.setSettlementRefundFee(Integer.valueOf(settlementRefundFee));
            r.setRefundFee(Integer.valueOf(refundFee));
            r.setTotalFee(Integer.valueOf(totalFee));
            r.setOutRefundNo(outRefundNo);
            r.setRefundId(refundId);
            r.setOutTradeNo(outTradeNo);
            r.setTransactionId(transactionId);
            //保存回调数据
            //refundNotifyService.addRefundNotify(r);
            /**
             * 更新订单状态
             * */

            /**
             * 通知微信服务器已经支付成功
             **/
            resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                    + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

        } else {
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }
}
