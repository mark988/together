package com.together.demo.utils;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;


/**
 * 金钱相关的计算
 *
 * @author Administrator
 */
@Slf4j
public class MoneyCaculateUtil {

    /***
     * 除法
     * **/
    public final static Double division(Double d1, Double d2) {
        BigDecimal b1 = new BigDecimal(d1 * 100d);
        BigDecimal b2 = new BigDecimal(d2 * 100d);
        BigDecimal b3 = b1.divide(b2, 2);
        return b3.doubleValue();
    }

    /**
     * 不丢失精度的方式，计算两个Double数的乘积。
     *
     * @param d1
     * @param d2
     * @return Double 保留小数点后两位
     */
    public final static Double multiply(Double d1, Double d2) {
        BigDecimal b1 = new BigDecimal(d1 * 100d);
        BigDecimal b2 = new BigDecimal(d2 * 100d);
        b1 = b1.multiply(b2).divide(new BigDecimal(10000));
        b1 = b1.setScale(2, BigDecimal.ROUND_HALF_UP);
        return b1.doubleValue();
    }

    public final static BigDecimal multiply(BigDecimal d1, BigDecimal d2) {
        d1 = d1.multiply(d2).divide(new BigDecimal(10000));
        d1 = d1.setScale(2, BigDecimal.ROUND_HALF_UP);
        return d1;
    }

    public final static Double multiply(Double d1, Double d2, Double d3) {
        BigDecimal b1 = new BigDecimal(d1 * 100d);
        BigDecimal b2 = new BigDecimal(d2 * 100d);
        BigDecimal b3 = new BigDecimal(d3 * 100d);
        b1 = b1.multiply(b2).divide(new BigDecimal(10000));
        b1 = b1.setScale(2, BigDecimal.ROUND_HALF_UP);
        return multiply(b1.doubleValue(), d3);
    }

    public final static BigDecimal multiply(BigDecimal d1, BigDecimal d2, BigDecimal d3) {
        return multiply(multiply(d1,d2) ,d3);
    }
    /**
     * 格式化金额
     * @param d1
     * @param format 金额格式化的参数， BigDecimal.ROUND_CEILING:保留2为小数  BigDecimal.ROUND_UP向上取整
     * @return
     */
    public final static Double formatMoney(Double d1, int format) {
        BigDecimal b1 = new BigDecimal(d1);
        b1 = b1.setScale(2, format);
        return b1.doubleValue();
    }

    /**
     * 保留两位小数，后面四舍五入
     * @param d
     * @return
     */
    public final static Double formatDouble2Point(Double d) {
        if (d == null) {
            return null;
        }
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    /**
     * 不丢失精度的方式，计算两个Double数的乘积。
     * @param d1
     * @param d2
     * @return Double 保留小数点后两位
     */
    public final static Double subtract(Double d1, Double d2) {
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        b1 = b1.subtract(b2);
        b1 = b1.setScale(0, BigDecimal.ROUND_CEILING);
        return b1.doubleValue();
    }
}
