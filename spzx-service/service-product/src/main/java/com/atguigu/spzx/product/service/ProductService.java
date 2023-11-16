package com.atguigu.spzx.product.service;

import com.atguigu.spzx.model.entity.product.ProductSku;

import java.util.List;

public interface ProductService {
    List<ProductSku> findProductSkuBySale();
}
