package com.together.demo.pojo.vo;

import com.together.demo.exception.InfoInterface;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 业务模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Business implements Serializable {
    private String  businessName;
    private Integer businessCode;
    private Integer messageType;

    public Business(InfoInterface infoInterface,Integer messageType){
        this.businessName =infoInterface.getDesc();
        this.businessCode =infoInterface.getCode();
        this.messageType = messageType;
    }
}
