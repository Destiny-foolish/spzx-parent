package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.common.exception.GguiguException;
import com.atguigu.spzx.manager.helper.MenuHelper;
import com.atguigu.spzx.manager.mapper.SysMenuMapper;
import com.atguigu.spzx.manager.mapper.SysRoleMenuMapper;
import com.atguigu.spzx.manager.service.SysMenuService;
import com.atguigu.spzx.model.entity.system.SysMenu;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.system.SysMenuVo;
import com.atguigu.spzx.utils.AuthContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Override
    public List<SysMenu> findNodes() {
        List<SysMenu> list = sysMenuMapper.selectAll();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<SysMenu> treeList = MenuHelper.buildTree(list);
        return treeList;
    }

    @Transactional
    @Override
    public void save(SysMenu sysMenu) {
        sysMenuMapper.insert(sysMenu);
        updateSysRoleMenuIsHalf(sysMenu);
    }

    private void updateSysRoleMenuIsHalf(SysMenu sysMenu) {
        // 查询是否存在父节点
        SysMenu parentNode = sysMenuMapper.selectById(sysMenu.getParentId());

        if (parentNode !=null ) {
            // 将该id的菜单设置为半开
            sysRoleMenuMapper.updateSysRoleMenuIsHalf(parentNode.getId());
            // 递归调用
            updateSysRoleMenuIsHalf(parentNode);
        }

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

    @Override
    public List<SysMenuVo> findUserMenuList() {
        SysUser sysUser = AuthContextUtil.get();
        Long userid = sysUser.getId();

        List<SysMenu> sysMenuList = sysMenuMapper.selectListByUserId(userid);

        List<SysMenu> sysMenuTreeList = MenuHelper.buildTree(sysMenuList);

        return this.buildMenus(sysMenuTreeList);
    }

    private List<SysMenuVo> buildMenus(List<SysMenu> menus){
        List<SysMenuVo> sysMenuVoList = new LinkedList<SysMenuVo>();
        for (SysMenu sysMenu : menus) {
            SysMenuVo sysMenuVo = new SysMenuVo();
            sysMenuVo.setTitle(sysMenu.getTitle());
            sysMenuVo.setName(sysMenu.getComponent());
            List<SysMenu> children = sysMenu.getChildren();
            if (!CollectionUtils.isEmpty(children)) {
                sysMenuVo.setChildren(buildMenus(children));
            }
            sysMenuVoList.add(sysMenuVo);
        }
        return sysMenuVoList;
    }
}
