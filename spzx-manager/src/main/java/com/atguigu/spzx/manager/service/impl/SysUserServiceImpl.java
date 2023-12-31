package com.atguigu.spzx.manager.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.atguigu.spzx.common.exception.GguiguException;
import com.atguigu.spzx.manager.mapper.SysRoleUserMapper;
import com.atguigu.spzx.manager.mapper.SysUserMapper;
import com.atguigu.spzx.manager.service.SysUserService;
import com.atguigu.spzx.model.dto.system.AssginRoleDto;
import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.dto.system.SysUserDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.system.LoginVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper ;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;




    @Override
    public LoginVo login(LoginDto loginDto) {

        //xinzeng yanzhengma jiaoyan fuwu
        String captcha = loginDto.getCaptcha();
        String codeKey = loginDto.getCodeKey();

        //genju huoqv de redislimiande key,chaxun redislimain de yanzhengma shifouyizhi
        String redisCode = redisTemplate.opsForValue().get("user:login:validatecode:"+codeKey);

        //首先，比较验证码是否同步，
        if(StrUtil.isEmpty(redisCode) || !StrUtil.equalsIgnoreCase(redisCode,captcha)) {
            //如果不一致，提示用户
            throw new GguiguException(ResultCodeEnum.VALIDATECODE_ERROR);
        }

        //一致，删除redis中的验证码且进行后续比较账号密码
        redisTemplate.delete("user:login:validatecode:" + codeKey);

        //1.首先获取yonghu ming
        String username = loginDto.getUserName();

        SysUser sysUser = sysUserMapper.selectByUserName(username);

        if(sysUser == null){
//            throw new RuntimeException("用户名字不存在");
            throw new GguiguException(ResultCodeEnum.LOGIN_ERROR);
        }

        String db_id = sysUser.getPassword();
        String login_id = loginDto.getPassword();
        login_id = DigestUtils.md5DigestAsHex(login_id.getBytes());

        if(db_id.equals(login_id)) {
//            throw new RuntimeException("密码不正确");
            throw new GguiguException(ResultCodeEnum.LOGIN_ERROR);
        }


        String token = UUID.randomUUID().toString().replaceAll("-","");

        //向redis里面加入数据
        redisTemplate.opsForValue().set("user:login:" + token,
                JSON.toJSONString(sysUser),
                7,
                TimeUnit.DAYS);

        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);

        return loginVo;
    }

    @Override
    public SysUser getUserInfo(String token) {
        String userJson = redisTemplate.opsForValue().get("user:login:" + token);
        //JSON.parseObject(userJson,SysUser.class) 与 JSON.toJSONString(sysUser)互为反过程
        SysUser sysUser = JSON.parseObject(userJson,SysUser.class);
        return sysUser;
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete("user:login:" + token);
    }

    @Override
    public PageInfo<SysUser> findByPage(SysUserDto sysUserDto, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum , pageSize);
        List<SysUser> sysUserList = sysUserMapper.findByPage(sysUserDto) ;
        PageInfo pageInfo = new PageInfo(sysUserList) ;
        return pageInfo;
    }

    @Override
    public void updateSysUser(SysUser sysUser) {
        sysUserMapper.updateSysUser(sysUser);
    }

    @Override
    public void saveSysUser(SysUser sysUser) {
        SysUser dbSysUser = sysUserMapper.findByUserName(sysUser.getUserName());
        if(dbSysUser != null) {
            throw new GguiguException(ResultCodeEnum.USER_NAME_IS_EXISTS);
        }
        String pwd = DigestUtils.md5DigestAsHex(sysUser.getPassword().getBytes());
        sysUser.setPassword(pwd);
        sysUser.setStatus(0);
        sysUserMapper.saveSysUser(sysUser);
    }

    @Override
    public void deleteById(Long userId) {
        sysUserMapper.deleteById(userId);
    }

    @Override
    public void doAssign(AssginRoleDto assginRoleDto) {
        sysRoleUserMapper.deleteByUserId(assginRoleDto.getUserId());
        List<Long> roleIdList = assginRoleDto.getRoleIdList();
        roleIdList.forEach(roleId->{
            sysRoleUserMapper.doAssign(assginRoleDto.getUserId(),roleId);
        });
    }
}
