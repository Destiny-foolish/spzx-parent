package com.atguigu.spzx.manager.service.impl;

import com.alibaba.fastjson2.JSON;
import com.atguigu.spzx.manager.mapper.SysUserMapper;
import com.atguigu.spzx.manager.service.SysUserService;
import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.system.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public LoginVo login(LoginDto loginDto) {
        //1.首先获取
        String username = loginDto.getUserName();

        SysUser sysUser = sysUserMapper.selectByUserName(username);

        if(sysUser == null){
            throw new RuntimeException("用户名字不存在");
        }
        String db_id = sysUser.getPassword();
        String login_id = loginDto.getPassword();
        login_id = DigestUtils.md5DigestAsHex(login_id.getBytes());

        if(db_id .equals(login_id)) {
            throw new RuntimeException("密码不正确");
        }

        String token = UUID.randomUUID().toString().replaceAll("-","");

        //向redis里面加入数据
        redisTemplate.opsForValue().set("user:login" + token,
                JSON.toJSONString(sysUser),
                7,
                TimeUnit.DAYS);

        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);

        return loginVo;
    }

}
