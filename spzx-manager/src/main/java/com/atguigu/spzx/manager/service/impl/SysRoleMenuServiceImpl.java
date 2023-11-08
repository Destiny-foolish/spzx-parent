package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.manager.mapper.SysRoleMenuMapper;
import com.atguigu.spzx.manager.service.SysMenuService;
import com.atguigu.spzx.manager.service.SysRoleMenuService;
import com.atguigu.spzx.model.dto.system.AssginMenuDto;
import com.atguigu.spzx.model.entity.system.SysMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleMenuServiceImpl implements SysRoleMenuService {

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    private SysMenuService sysMenuService;
    @Override
    public Map<String, Object> findSysRoleMenuByRoleId(Long roleId) {
        // 查询所有的菜单数据
        List<SysMenu> sysMenuList = sysMenuService.findNodes();
        // 查询当前角色的菜单数据
        List<Long> roleMenuIds = sysRoleMenuMapper.findSysRoleMenuByRoleId(roleId) ;

        //将数据存储到Map中返回
        Map<String, Object> result = new HashMap<>();
        result.put("sysMenuList" , sysMenuList);
        result.put("roleMenuIds" , roleMenuIds);

        return result;
    }

    @Override
    public void doAssign(AssginMenuDto assginMenuDto) {
        // 根据角色的id删除其所对应的菜单数据
        sysRoleMenuMapper.deleteByRoleId(assginMenuDto.getRoleId());
        // 获取菜单的id
        List<Map<String, Number>> list = assginMenuDto.getMenuIdList();
        if(list != null && list.size() > 0){
            sysRoleMenuMapper.doAssign(assginMenuDto);
        }

    }
}
