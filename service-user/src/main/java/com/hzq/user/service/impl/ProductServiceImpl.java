package com.hzq.user.service.impl;

import com.hzq.user.dao.ProductMapper;
import com.hzq.user.entity.Product;
import com.hzq.user.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by hzq on 16/12/11.
 */
@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductMapper productMapper;

    @Override
    public Product getProductById(Integer productId) {
        return productMapper.getByPk(productId);
    }
}
