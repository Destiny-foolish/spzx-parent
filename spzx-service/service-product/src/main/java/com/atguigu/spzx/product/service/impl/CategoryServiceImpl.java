package com.atguigu.spzx.product.service.impl;

import com.alibaba.fastjson2.JSON;
import com.atguigu.spzx.model.entity.product.Category;
import com.atguigu.spzx.product.mapper.CategoryMapper;
import com.atguigu.spzx.product.service.CategoryService;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public List<Category> findOneCategory() {
        String categoryOneJaon = redisTemplate.opsForValue().get("category:one");//get里面的都是自己约定的key值
        if(StringUtils.hasText(categoryOneJaon)) {
            List<Category> categoryList = JSON.parseArray(categoryOneJaon, Category.class);
//            log.info("从Redis缓存中查询到了所有的一级分类数据");
            return categoryList ;
        }

        List<Category> categoryList =  categoryMapper.findOneCategory();
        redisTemplate.opsForValue().set("category:one" ,
                JSON.toJSONString(categoryList) ,
                7 , TimeUnit.DAYS);

        return categoryList;
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
