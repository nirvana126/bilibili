package com.gansj.bilibili.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gansj.bilibili.common.ErrorCode;
import com.gansj.bilibili.dao.User;
import com.gansj.bilibili.dao.UserInfo;
import com.gansj.bilibili.exception.BusinessException;
import com.gansj.bilibili.mapper.UserInfoMapper;
import com.gansj.bilibili.mapper.UserMapper;
import com.gansj.bilibili.service.UserInfoService;
import com.gansj.bilibili.service.UserService;
import com.gansj.bilibili.utils.MD5Util;
import com.gansj.bilibili.utils.RSAUtil;
import com.gansj.bilibili.utils.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserInfoService userInfoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void userRegister(User user) {
        //1.手机号不能为空
        String phone = user.getPhone();
        if (StringUtils.isBlank(phone)){
            throw new BusinessException(ErrorCode.REQUEST_PARAMS_ERROR,"手机号不能为空！");
        }
        //2.判断用户是否已注册
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone",phone);
        User dbUser = this.getOne(queryWrapper);
        if (dbUser!=null){
            throw new BusinessException(ErrorCode.REQUEST_PARAMS_ERROR,"该手机号已注册");
        }
        //3.获取前端换来的用户密码，进行RSA解密
        String rawPassword;
        try {
             rawPassword = RSAUtil.decrypt(user.getUserPassword());
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"密码解密失败");
        }
        //4.对用户密码进行md5加密存入数据库
        Date date = new Date();
        String salt = String.valueOf(date);
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        user.setPhone(phone);
        user.setUserPassword(md5Password);
        user.setSalt(salt);
        user.setCreateTime(date);
        this.save(user);
        //5.更新userinfo表
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setNick("萌新");
        userInfo.setGender("0");
        userInfo.setBirth("2001-12-6");
        userInfo.setCreateTime(date);
        userInfoService.save(userInfo);

    }

    @Override
    public String userLogin(User user) throws Exception {
        String phone = user.getPhone() == null ? "" : user.getPhone();
        String email = user.getEmail() == null ? "" : user.getEmail();
        if(StringUtils.isBlank(phone) && StringUtils.isBlank(email)){
            throw new BusinessException(ErrorCode.REQUEST_PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone",phone).or().eq("email",email);
        User dbUser = this.getOne(queryWrapper);
        if(dbUser == null){
            throw new BusinessException(ErrorCode.NO_AUTH,"当前用户不存在！");
        }
        String password = user.getUserPassword();
        String rawPassword;
        try{
            rawPassword = RSAUtil.decrypt(password);
        }catch (Exception e){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"密码解密失败！");
        }
        String salt = dbUser.getSalt();
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        if(!md5Password.equals(dbUser.getUserPassword())){
            throw new BusinessException(ErrorCode.REQUEST_PARAMS_ERROR,"密码错误！");
        }
        return TokenUtil.generateToken(dbUser.getId());
    }

    @Override
    public boolean updateUserInfo(UserInfo userInfo) {
        userInfo.setUpdateTime(new Date());
        UpdateWrapper<UserInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id",userInfo.getUserId());
        if (StringUtils.isNotBlank(userInfo.getAvatar())){
            updateWrapper.set("avatar",userInfo.getAvatar());
        }
        if (StringUtils.isNotBlank(userInfo.getBirth())){
            updateWrapper.set("birth",userInfo.getBirth());
        }
        if (StringUtils.isNotBlank(userInfo.getGender())){
            updateWrapper.set("gender",userInfo.getGender());
        }
        if (StringUtils.isNotBlank(userInfo.getNick())){
            updateWrapper.set("nick",userInfo.getNick());
        }
        if (StringUtils.isNotBlank(userInfo.getSign())){
            updateWrapper.set("sign",userInfo.getSign());
        }
        return userInfoService.update(userInfo, updateWrapper);

    }


}