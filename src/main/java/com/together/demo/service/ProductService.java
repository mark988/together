package com.together.demo.service;

import com.together.demo.pojo.entity.Product;

public interface ProductService {
    int updateProduct(Product product);
    Product findProductById(Integer id);
}
