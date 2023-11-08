package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.entity.system.SysMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysMenuMapper {
    List<SysMenu> selectAll();

    void insert(SysMenu sysMenu);

    void updateById(SysMenu sysMenu);

    void removeById(Long id);

    int countByParentId();

    List<SysMenu> selectListByUserId(Long userid);
}
