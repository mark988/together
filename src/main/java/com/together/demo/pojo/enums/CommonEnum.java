package com.together.demo.pojo.enums;

import com.together.demo.exception.InfoInterface;
/**
 * 全局变量定义
 */
public enum CommonEnum implements InfoInterface {
     //
    SUCCESS(0, "成功!"),
    FAIL(-1, "失败!"),
    PARAMETER_IS_NULL(-2, "参数空为空!"),
    PARAMETER_IS_ERRO(-3, "参数有误!"),
    /**
     * 业务定义 10～ 50
     */
    BUSINESS_SHOP(10,"商城"),
    BUSINESS_HELP(20,"互助平台"),
    /**
     * 消息类型定义 51～100
     */
    MESSAGE_TYPE_TIPS(51,"提示消息"),
    MESSAGE_TYPE_NOTICE(52,"通知消息"),
    MESSAGE_TYPE_GROUP_CHAT(53,"群聊消息"),
    MESSAGE_TYPE_SINGLE_CHAT(54,"1对1消息"),

    INSERT_SUCCESS(CommonEnum.SUCCESS.code, "添加成功!"),
    QUERY_SUCCESS(CommonEnum.SUCCESS.code, "修改成功!"),
    MODIFY_SUCCESS(CommonEnum.SUCCESS.code, "修改成功!"),
    DEL_SUCCESS(CommonEnum.SUCCESS.code, "删除成功!"),
    CANCEL_SUCCESS(CommonEnum.SUCCESS.code, "取消成功!"),

    /**
     * 服务端异常
     * */
    INTERNAL_SERVER_ERROR(500100, "服务器内部错误!"),
    NULL_POINTER_EXCEPTION(500101,"空指针异常"),
    SERVER_BUSY(500103,"服务器正忙，请稍后再试!"),
    NOT_FOUND(500104, "未找到该资源!"),
    EXCEPTION_BIND_ERROR(500108,"(绑定异常)参数校验异常：%s"),
    ERROR_SESSION(500111,"没有SESSION"),
    REQUEST_OVER_LIMIT(500999,"请求次数过多")
    /**
     * 登陆异常 5002xx
     * */
    ;

    private Integer code;
    private String  desc;

    CommonEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

}
