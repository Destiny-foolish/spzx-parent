package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.common.exception.GguiguException;
import com.atguigu.spzx.manager.helper.MenuHelper;
import com.atguigu.spzx.manager.mapper.SysMenuMapper;
import com.atguigu.spzx.manager.service.SysMenuService;
import com.atguigu.spzx.model.entity.system.SysMenu;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> findNodes() {
        List<SysMenu> list = sysMenuMapper.selectAll();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<SysMenu> treeList = MenuHelper.buildTree(list);
        return treeList;
    }

    @Override
    public void save(SysMenu sysMenu) {
        sysMenuMapper.insert(sysMenu);
    }

    @Override
    public void updateById(SysMenu sysMenu) {
        sysMenuMapper.updateById(sysMenu);
    }

    @Override
    public void removeById(Long id) {
        int count = sysMenuMapper.countByParentId();
        if(count > 0){
            throw new GguiguException(ResultCodeEnum.NODE_ERROR);
        }
        sysMenuMapper.removeById(id);
    }
}
