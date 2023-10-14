package com.atguigu.spzx.utils;

import com.atguigu.spzx.model.entity.system.SysUser;


public class AuthContextUtil {
    //chuangjian threadlocal 对象
    private static final ThreadLocal<SysUser> threadLocal = new ThreadLocal<>();

    //添加数据
    public static void set(SysUser sysUser) {
        threadLocal.set(sysUser);
    }
    //获取数据

    public static SysUser get() {
        return threadLocal.get();
    }

    //删除数据
    public static void remove(){
        threadLocal.remove();
    }

}
