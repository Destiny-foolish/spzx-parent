package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.manager.mapper.CategoryMapper;
import com.atguigu.spzx.manager.service.CategoryService;
import com.atguigu.spzx.model.entity.product.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public List<Category> findByParentId(Long parentId) {

        // 根据分类id查询它下面的所有的子分类数据
        List<Category> list = categoryMapper.selectByParentId(parentId);
        if(list != null) {
            // 遍历分类的集合，获取每一个分类数据
            list.forEach(item -> {
                // 查询该分类下子分类的数量
                int count = categoryMapper.seleCount(item.getId());

                if(count > 0) {
                    item.setHasChildren(true);
                } else {
                    item.setHasChildren(false);
                }
            });
        }
        return list;
    }
}
