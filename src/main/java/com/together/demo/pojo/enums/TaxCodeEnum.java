package com.together.demo.pojo.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Function:
 * @author mxg
 * @Date: 2018/12/26 18:38
 */
public enum TaxCodeEnum {
//
    ALL("cn","中国交税","TaxForCN"),
    ONLINE("fr","法国交税","TaxForFR"),
    SHUTDOWN("jp","日本交税","TaxForJP")
    ;

    /**
     * 枚举值码
     * */
    private final String commandType;

    /**
     * 枚举描述
     * */
    private final String desc;

    /**
     * 实现类
     */
    private final String clazz ;


    /**
     * 构建一个 。
     * @param commandType 枚举值码。
     * @param desc 枚举描述。
     */
     TaxCodeEnum(String commandType, String desc, String clazz) {
        this.commandType = commandType;
        this.desc = desc;
        this.clazz = clazz ;
    }

    /**
     * 得到枚举值码。
     * @return 枚举值码。
     */
    public String getCommandType() {
        return commandType;
    }
    /**
     * 获取 class。
     * @return class。
     */
    public String getClazz() {
        return clazz;
    }

    /**
     * 得到枚举描述。
     * @return 枚举描述。
     */
    public String getDesc() {
        return desc;
    }

    /**
     * 得到枚举值码。
     * @return 枚举值码。
     */
    public String code() {
        return commandType;
    }

    /**
     * 得到枚举描述。
     * @return 枚举描述。
     */
    public String message() {
        return desc;
    }

    /**
     * 获取全部枚举值码。
     *
     * @return 全部枚举值码。
     */
    public static Map<String,String> getAllStatusCode() {
        Map<String,String> map = new HashMap<String, String>(16) ;
        for (TaxCodeEnum status : values()) {
            map.put(status.getCommandType(),status.getDesc()) ;
        }
        return map;
    }

    public static Map<String,String> getAllClazz() {
        Map<String,String> map = new HashMap<String, String>(16) ;
        for (TaxCodeEnum status : values()) {
            map.put(status.getCommandType().trim(),"com.together.demo.designPattern.strategy." + status.getClazz()) ;
        }
        return map;
    }



}