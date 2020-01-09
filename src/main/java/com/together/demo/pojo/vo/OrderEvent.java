package com.together.demo.pojo.vo;

import lombok.Data;
import java.io.Serializable;
/**
 * @author maxiaoguang
 */

@Data
public class OrderEvent implements Serializable {
    private int productId;
    private int userId;
}
