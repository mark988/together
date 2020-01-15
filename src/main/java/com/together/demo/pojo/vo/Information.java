package com.together.demo.pojo.vo;

import lombok.Data;
import java.io.Serializable;

/**
 * 通知的消息
 * @author maxiaoguang
 */
@Data
public class Information implements Serializable {
    private Business business;
    private String  text;
}
