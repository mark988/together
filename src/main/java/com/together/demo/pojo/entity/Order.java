package com.together.demo.pojo.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "t_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="product_id")
    private Integer productId;

    @Column(name="user_id")
    private Integer userId;

    @Column(name="create_time")
    private Date  createTime;
}