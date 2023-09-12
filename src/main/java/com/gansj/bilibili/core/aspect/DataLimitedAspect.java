package com.gansj.bilibili.core.aspect;

import com.gansj.bilibili.common.ErrorCode;
import com.gansj.bilibili.constant.AuthRoleConstant;
import com.gansj.bilibili.dao.UserMoment;
import com.gansj.bilibili.dao.UserRole;
import com.gansj.bilibili.exception.BusinessException;
import com.gansj.bilibili.manager.UserSupport;
import com.gansj.bilibili.service.UserRoleService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Aspect
@Order(1)
@Component
public class DataLimitedAspect {
    @Resource
    private UserSupport userSupport;
    @Resource
    private UserRoleService userRoleService;

    @Pointcut("@annotation(com.gansj.bilibili.core.annotation.DataLimited)")//切点，切入位置,注解被执行进行切入
    public void check(){

    }

    //切点切入之后执行的逻辑
    @Before("check()")
    public void doBefore(JoinPoint joinPoint){
        Long userid=userSupport.getCurrentUserId();
        List<UserRole> userRoleList= userRoleService.getUserRoleByUserId(userid);
        Set<String> roleCodeSet=userRoleList.stream().map(UserRole::getRoleCode).collect(Collectors.toSet());
        Object[] args=joinPoint.getArgs();
        for(Object arg:args) {
            if (arg instanceof UserMoment) {
                  UserMoment userMoment=(UserMoment) arg;
                  String type=userMoment.getType();
                  if(roleCodeSet.contains(AuthRoleConstant.ROLE_LV0)&&!"0".equals(type)){
                      throw  new BusinessException(ErrorCode.NO_AUTH);
                  }
            }
        }

    }
}
