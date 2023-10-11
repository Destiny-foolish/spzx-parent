package com.atguigu.spzx.manager.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.alibaba.fastjson2.JSON;
import com.atguigu.spzx.manager.service.ValidateCodeService;
import com.atguigu.spzx.model.vo.system.ValidateCodeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public ValidateCodeVo gnerateValidateCode() {

        //1.通过工具生成图片验证码 hutool
        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(150,48,4,3);
        //2.将验证码存储到redis里面，设置redis的key：uuid redis的value：验证码值
        //3.设置过期时间
        String codeValue = circleCaptcha.getCode();//shegn cheng 4 wei yanzhengma de zhi
        String image = circleCaptcha.getImageBase64();//fan hui tupian yanzhengma

        String key = UUID.randomUUID().toString().replaceAll("-","");

        redisTemplate.opsForValue().set("user:validate" + key,
                codeValue,
                5,
                TimeUnit.MINUTES);
        //4.返回ValidateCodeVo对象
        ValidateCodeVo validateCodeVo = new ValidateCodeVo();
        validateCodeVo.setCodeKey(key);//redis中存储的key
        validateCodeVo.setCodeValue("data:image/png;base64," + image);

        return validateCodeVo;
    }


}
