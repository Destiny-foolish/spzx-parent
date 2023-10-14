package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.SysUserService;
import com.atguigu.spzx.manager.service.ValidateCodeService;
import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.system.LoginVo;
import com.atguigu.spzx.model.vo.system.ValidateCodeVo;
import com.atguigu.spzx.utils.AuthContextUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户接口")
@RestController
@RequestMapping(value = "/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService ;

    @Autowired
    private ValidateCodeService validateCodeService;

    //yong hu tuichu
    @Operation(summary = "用户退出")
    @GetMapping(value = "/logout")
    public Result logout(@RequestHeader(name = "token") String token){
        sysUserService.logout(token);
        return Result.build(null , ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "登录的方法")
    @PostMapping(value = "/login")
    public Result<LoginVo> login(@RequestBody LoginDto loginDto) {
        LoginVo loginVo = sysUserService.login(loginDto) ;
        return Result.build(loginVo , ResultCodeEnum.SUCCESS) ;
    }

    //生成图片的验证码
    @Operation(summary = "生成验证码")
    @GetMapping("/generateValidateCode")
    public Result<ValidateCodeVo> generateValidateCode(){
        ValidateCodeVo validateCodeVo = validateCodeService.gnerateValidateCode();
        return Result.build(validateCodeVo,ResultCodeEnum.SUCCESS);
    }

    //获取当前登录的用户信息
    @Operation(summary = "获取用户信息")
    @GetMapping(value = "/getUserInfo")
    public Result<SysUser> getUserInfo(@RequestHeader(name = "token")
                                           String token){
        //从请求头获取token
        //1.getUserInfo(Httpservletrequest request)
        //  String token = request.getHeader("token");
        //2.use @RequestHeader 注解实现
//        SysUser sysUser = sysUserService.getUserInfo(token);
//        return Result.build(sysUser,ResultCodeEnum.SUCCESS);
        return Result.build(AuthContextUtil.get(),ResultCodeEnum.SUCCESS);
    }

}
