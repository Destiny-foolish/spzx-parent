package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.entity.product.ProductSku;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductSkuMapper {
    void save(ProductSku productSku);
}
