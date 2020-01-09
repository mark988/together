package com.together.demo.service;

import com.together.demo.dao.DynamicDao;
import com.together.demo.pojo.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

@Slf4j
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private DynamicDao dynamicSQL;

    @Transactional( rollbackFor = Exception.class)
    @Override
    public void saveOrder(Order order) {
        order.setCreateTime(new Date());
        dynamicSQL.save(order);
    }
}
