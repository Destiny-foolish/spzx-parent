package com.atguigu.spzx.product.service.impl;

import com.atguigu.spzx.model.entity.product.Category;
import com.atguigu.spzx.product.mapper.CategoryMapper;
import com.atguigu.spzx.product.service.CategoryService;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> findOneCategory() {
        return categoryMapper.findOneCategory();
    }

    @Override
    public List<Category> findCategoryTree() {
        //1查询所有分类 返回list集合
        List<Category> all = categoryMapper.findAll();

        //2遍历所有分类list集合,通过条件parentid = 0得到所有一级分类
        List<Category> oneCategoryList =all.stream().filter(item -> item.getParentId().longValue() == 0).collect(Collectors.toList());

        //3遍历所有以及分类list集合,条件判断:id=parentid,得到一级下面二级分类
        if(!CollectionUtils.isEmpty(oneCategoryList)) {
            oneCategoryList.forEach(oneCategory ->{
                List<Category> twoCategoryList = all.stream().filter(item -> item.getParentId().longValue() ==
                        oneCategory.getId().longValue()).collect(Collectors.toList());
                oneCategory.setChildren(twoCategoryList);

                //4遍历所有二级分类,条件判断:id=parentid,得到一级下面三级分类
                if(!CollectionUtils.isEmpty(twoCategoryList)){
                    twoCategoryList.forEach(twoCategory->{
                        List<Category> threeCategoryList = all.stream().filter(item -> item.getParentId().longValue() ==
                                twoCategory.getId().longValue()).collect(Collectors.toList());
                        twoCategory.setChildren(threeCategoryList);
                    });
                }
            });
        }


        return oneCategoryList;
    }
}
