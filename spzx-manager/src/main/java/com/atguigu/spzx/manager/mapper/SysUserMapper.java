package com.atguigu.spzx.manager.mapper;

import com.atguigu.spzx.model.dto.system.SysUserDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserMapper{

    public abstract SysUser selectByUserName(String username);

    List<SysUser> findByPage(SysUserDto sysUserDto);

    public abstract void saveSysUser(SysUser sysUser);

    public abstract SysUser findByUserName(String userName);

    public abstract void updateSysUser(SysUser sysUser);

    public abstract void deleteById(Long userId);

    void doAssign(@Param("userId") Long userId, @Param("roleId") Long roleId);

    void deleteByUserId(Long userId);
}
