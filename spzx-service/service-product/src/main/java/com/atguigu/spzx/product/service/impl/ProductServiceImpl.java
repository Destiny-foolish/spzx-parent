package com.atguigu.spzx.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.spzx.model.dto.h5.ProductSkuDto;
import com.atguigu.spzx.model.entity.product.Product;
import com.atguigu.spzx.model.entity.product.ProductDetails;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.atguigu.spzx.model.vo.h5.ProductItemVo;
import com.atguigu.spzx.product.mapper.ProductDetailsMapper;
import com.atguigu.spzx.product.mapper.ProductMapper;
import com.atguigu.spzx.product.mapper.ProductSkuMapper;
import com.atguigu.spzx.product.service.ProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductDetailsMapper productDetailsMapper;

    @Override
    public List<ProductSku> findProductSkuBySale() {
        return productSkuMapper.findProductSkuBySale();
    }

    @Override
    public PageInfo<ProductSku> findByPage(Integer page, Integer limit, ProductSkuDto productSkuDto) {
        PageHelper.startPage(page, limit);
        List<ProductSku> productSkuList = productSkuMapper.findByPage(productSkuDto);
        return new PageInfo<>(productSkuList);
    }

    @Override
    public ProductItemVo item(Long skuId) {
        //当前sku信息
        ProductSku productSku = productSkuMapper.getById(skuId);

        //当前商品信息
        Product product = productMapper.getById(productSku.getProductId());

        //同一个商品下面的sku信息列表
        List<ProductSku> productSkuList = productSkuMapper.findByProductId(productSku.getProductId());
        //建立sku规格与skuId对应关系
        Map<String,Object> skuSpecValueMap = new HashMap<>();
        productSkuList.forEach(item -> {
            skuSpecValueMap.put(item.getSkuSpec(), item.getId());
        });

        //商品详情信息
        ProductDetails productDetails = productDetailsMapper.getByProductId(productSku.getProductId());

        ProductItemVo productItemVo = new ProductItemVo();
        productItemVo.setProductSku(productSku);
        productItemVo.setProduct(product);
        productItemVo.setDetailsImageUrlList(Arrays.asList(productDetails.getImageUrls().split(",")));
        productItemVo.setSliderUrlList(Arrays.asList(product.getSliderUrls().split(",")));
        productItemVo.setSpecValueList(JSON.parseArray(product.getSpecValue()));
        productItemVo.setSkuSpecValueMap(skuSpecValueMap);
        return productItemVo;
    }
}
