package com.together.demo.service;

import com.together.demo.dao.DynamicDao;
import com.together.demo.pojo.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private DynamicDao dynamicDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateProduct(Product product) {
        StringBuffer sb = new StringBuffer();
        sb.append(" update t_product ");
        sb.append(" set version=version+1 ,name =  ?,number = number + ?");
        sb.append(" where id = ?  and version= ? and number>0");
        Object[] param = new Object[]{product.getName(),product.getNumber(),product.getId(),product.getVersion()};
        return dynamicDao.nativeExecuteUpdate(sb.toString(),param);
    }

    @Override
    public Product findProductById(Integer id) {
        Integer[] param = new Integer[]{id};
        StringBuffer sb = new StringBuffer();
        sb.append(" select * from t_product where id = ?  ");
        Product product = null;
        try {
            List<Product> list = dynamicDao.nativeQueryListModel(Product.class, sb.toString(),param);
            product = list!=null?list.get(0):null;
        }catch (Exception e){
            log.info(" 查询数据出现异常：{} ",e.getMessage());
            return null;
        }
        return product;

    }
}
