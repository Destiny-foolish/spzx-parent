package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.manager.mapper.ProductDetailsMapper;
import com.atguigu.spzx.manager.mapper.ProductMapper;
import com.atguigu.spzx.manager.mapper.ProductSkuMapper;
import com.atguigu.spzx.manager.service.ProductService;
import com.atguigu.spzx.model.dto.product.ProductDto;
import com.atguigu.spzx.model.entity.product.Product;
import com.atguigu.spzx.model.entity.product.ProductDetails;
import com.atguigu.spzx.model.entity.product.ProductSku;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper ;

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private ProductDetailsMapper productDetailsMapper;

    @Override
    public PageInfo<Product> findByPage(Integer page, Integer limit, ProductDto productDto) {
        PageHelper.startPage(page , limit) ;
        List<Product> productList =  productMapper.findByPage(productDto) ;
        return new PageInfo<>(productList);
    }

    @Transactional
    @Override
    public void save(Product product) {
        // 保存商品数据
        product.setStatus(0);              // 设置上架状态为0
        product.setAuditStatus(0);         // 设置审核状态为0
        productMapper.save(product);
        // 保存商品sku数据
        List<ProductSku> productSkuList = product.getProductSkuList();
        for(int i=0,size=productSkuList.size(); i<size; i++) {

            // 获取ProductSku对象
            ProductSku productSku = productSkuList.get(i);
            productSku.setSkuCode(product.getId() + "_" + i);       // 构建skuCode

            productSku.setProductId(product.getId());               // 设置商品id
            productSku.setSkuName(product.getName() + productSku.getSkuSpec());
            productSku.setSaleNum(0);                               // 设置销量
            productSku.setStatus(0);
            productSkuMapper.save(productSku);                    // 保存数据

        }

        // 保存商品详情数据
        ProductDetails productDetails = new ProductDetails();
        productDetails.setProductId(product.getId());
        productDetails.setImageUrls(product.getDetailsImageUrls());
        productDetailsMapper.save(productDetails);
    }

    @Override
    public Product getById(Long id) {
        // 根据id查询商品数据
        Product product = productMapper.selectById(id);
        // 根据商品的id查询sku数据
        List<ProductSku> productSkuList = productSkuMapper.selectByProductId(id);
        product.setProductSkuList(productSkuList);
        // 根据商品的id查询商品详情数据
        ProductDetails productDetails = productDetailsMapper.selectByProductId(product.getId());
        product.setDetailsImageUrls(productDetails.getImageUrls());
        // 返回数据
        return product;
    }

    @Override
    public void updateById(Product product) {
        // 修改商品基本数据
        productMapper.updateById(product);
        // 修改商品的sku数据
        List<ProductSku> productSkuList = product.getProductSkuList();
        productSkuList.forEach(productSku -> {
            productSkuMapper.updateById(productSku);
        });
        // 修改商品的详情数据
        ProductDetails productDetails = productDetailsMapper.selectByProductId(product.getId());
        productDetails.setImageUrls(product.getDetailsImageUrls());
        productDetailsMapper.updateById(productDetails);

    }

    @Override
    public void deleteById(Long id) {
        // 根据id删除商品基本数据
        productMapper.deleteById(id);
        // 根据商品id删除商品的sku数据
        productSkuMapper.deleteByProductId(id);
        // 根据商品的id删除商品的详情数据
        productDetailsMapper.deleteByProductId(id);
    }

    @Override
    public void updateAuditStatus(Long id, Integer auditStatus) {
        Product product = new Product();
        product.setId(id);
        if(auditStatus == 1) {
            product.setAuditStatus(1);
            product.setAuditMessage("审批通过");
        } else {
            product.setAuditStatus(-1);
            product.setAuditMessage("审批不通过");
        }
        productMapper.updateById(product);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Product product = new Product();
        product.setId(id);
        if(status == 1) {
            product.setStatus(1);
        } else {
            product.setStatus(-1);
        }
        productMapper.updateById(product);
    }
}
