package com.together.demo.utils;

/**
 * 项目常量类
 * @author maxiaoguang
 */
public interface TogetherConstants {

    Integer SUCCESS  =  0 ;
    Integer FAIL     = -1 ;
    String OPERATION_SUCCESS = "操作成功";
    String OPERATION_FAIL = "操作失败";
    String INSERT_SUCCESS    = "添加成功";
    String QUERY_SUCCESS     = "查询成功";
    String MODIFY_SUCCESS    = "修改成功";
    String DEL_SUCCESS       = "删除成功";
    String PARAMETER_IS_NULL = "参数为空";
    String PARAMETER_IS_ERRO = "参数有误";

    /**
     * 取消操作
     */
    String  CANCEL_SUCCESS = "取消成功";
    String  CANCEL_FAIL    = "取消失败";
}
