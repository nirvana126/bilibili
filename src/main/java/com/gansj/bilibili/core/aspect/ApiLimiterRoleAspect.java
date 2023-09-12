package com.gansj.bilibili.core.aspect;

import com.gansj.bilibili.common.ErrorCode;
import com.gansj.bilibili.core.annotation.ApiLimitedRole;
import com.gansj.bilibili.dao.UserRole;
import com.gansj.bilibili.exception.BusinessException;
import com.gansj.bilibili.manager.UserSupport;
import com.gansj.bilibili.service.UserRoleService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
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
public class ApiLimiterRoleAspect {

    @Resource
    private UserSupport userSupport;

    @Resource
    private UserRoleService userRoleService;

    //定义切点
    @Pointcut("@annotation(com.gansj.bilibili.core.annotation.ApiLimitedRole)")
    public void check(){}

    //切点切入以后的执行逻辑
    @Before("check() && @annotation(apiLimitedRole)")
    public void doBefore(JoinPoint joinPoint, ApiLimitedRole apiLimitedRole){
        Long userId = userSupport.getCurrentUserId();
        //获取当前用户的角色编码列表(注意去重)
        List<UserRole> userRoleList = userRoleService.getUserRoleByUserId(userId);
        Set<String> roleCodeSet = userRoleList.stream().map(UserRole::getRoleCode).collect(Collectors.toSet());
        //获取受限的角色编码列表
        String[] limitedRoleCodeList = apiLimitedRole.limitedRoleCodeList();
        Set<String> limitedRoleCodeSet = Arrays.stream(limitedRoleCodeList).collect(Collectors.toSet());
        //取交集
        roleCodeSet.retainAll(limitedRoleCodeSet);
        if (roleCodeSet.size()>0){
            throw new BusinessException(ErrorCode.NO_AUTH,"权限不足！");
        }
    }
}
