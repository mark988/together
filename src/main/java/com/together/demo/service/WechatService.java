package com.together.demo.service;

import com.together.demo.pojo.vo.Result;
import com.together.demo.pojo.vo.WechatPayReqVo;
import com.together.demo.pojo.vo.WechatRefundReqVo;

public interface WechatService {

    Result unifiedOrder(WechatPayReqVo vo);
    /**
     * 退款
     * @param wechatRefundReqVo
     * @return
     */
    Result wxRefund(WechatRefundReqVo wechatRefundReqVo);
}
