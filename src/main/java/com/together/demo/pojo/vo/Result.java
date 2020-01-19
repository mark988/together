package com.together.demo.pojo.vo;

import com.alibaba.fastjson.JSONObject;
import com.together.demo.exception.InfoInterface;
import com.together.demo.pojo.enums.CommonEnum;
import lombok.Data;

/**
 * 结果统一返回对象
 * @param <T>
 */
@Data
public class Result<T> {
    /**
     * 响应代码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String desc;

    /**
     * 响应结果
     */
    private T  result;

    public Result() {
    }

    public Result(InfoInterface errorInfo) {
        this.code = errorInfo.getCode();
        this.desc = errorInfo.getDesc();
    }


    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    /**
     * 成功
     *
     * @return
     */
    public static Result success() {
        return success(null);
    }

    /**
     * 成功
     * @param data
     * @return
     */
    public static Result success(Object data) {
        Result rb = new Result();
        rb.setCode(CommonEnum.SUCCESS.getCode());
        rb.setDesc(CommonEnum.SUCCESS.getDesc());
        rb.setResult(data);
        return rb;
    }

    /**
     * 失败
     */
    public static Result error(InfoInterface errorInfo) {
        Result rb = new Result();
        rb.setCode(errorInfo.getCode());
        rb.setDesc(errorInfo.getDesc());
        return rb;
    }

    /**
     * 失败
     */
    public static Result error(Integer code, String desc) {
        Result rb = new Result();
        rb.setCode(code);
        rb.setDesc(desc);
        return rb;
    }

    /**
     * 失败
     */
    public static Result error(String desc) {
        Result rb = new Result();
        rb.setCode(CommonEnum.FAIL.getCode());
        rb.setDesc(desc);
        return rb;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
