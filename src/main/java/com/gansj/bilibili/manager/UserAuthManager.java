package com.gansj.bilibili.manager;

import com.gansj.bilibili.dao.AuthRoleElementOperation;
import com.gansj.bilibili.dao.AuthRoleMenu;
import com.gansj.bilibili.dao.UserRole;
import com.gansj.bilibili.dto.UserAuthoritiesRespDto;
import com.gansj.bilibili.service.AuthRoleService;
import com.gansj.bilibili.service.UserRoleService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Component
public class UserAuthManager {


    @Resource
    private UserRoleService userRoleService;

    @Resource
    private AuthRoleService authRoleService;

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(16,
        100,100L,
        TimeUnit.MILLISECONDS,
        new LinkedBlockingDeque<>(1024),
        new ThreadPoolExecutor.AbortPolicy());


    public UserAuthoritiesRespDto getUserAuthorities(Long userId) {
//        long before = System.currentTimeMillis();
        List<UserRole> userRoleList= userRoleService.getUserRoleByUserId(userId);
        Set<Long> roleIdSet=userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
        List<AuthRoleElementOperation> roleElementOperations=authRoleService.getRoleElementOperations(roleIdSet);
        List<AuthRoleMenu> authRoleMenuList=authRoleService.getAuthRoleMenusByRoleIds(roleIdSet);
        UserAuthoritiesRespDto userAuthorities=new UserAuthoritiesRespDto();
        userAuthorities.setRoleElementOperationList(roleElementOperations);
        userAuthorities.setRoleMenuList(authRoleMenuList);
//        System.out.println("原方法执行用时："+(System.currentTimeMillis()-before));
        return userAuthorities;

    }

    public UserAuthoritiesRespDto asyncGetUserAuthorities(Long userId){
//        long before = System.currentTimeMillis();
        //先new对象用于存储用户权限信息
        UserAuthoritiesRespDto userAuthoritiesRespDto = new UserAuthoritiesRespDto();
        //使用CompletableFuture 实现异步处理任务
        //c1:获取用户角色列表
        CompletableFuture<Set<Long>> c1 = CompletableFuture.supplyAsync(()->{
            List<UserRole> userRoleList = userRoleService.getUserRoleByUserId(userId);
            return userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
        });
        //c2:根据角色ID集合获取页面操作权限列表
        CompletableFuture<Void> c2 = c1.thenAcceptAsync((roleIdSet)->{
            List<AuthRoleElementOperation> roleElementOperations = authRoleService.getRoleElementOperations(roleIdSet);
            userAuthoritiesRespDto.setRoleElementOperationList(roleElementOperations);
        });
        CompletableFuture<Void> c3 = c1.thenAcceptAsync((roleIdSet)->{
            List<AuthRoleMenu> authRoleMenus = authRoleService.getAuthRoleMenusByRoleIds(roleIdSet);
            userAuthoritiesRespDto.setRoleMenuList(authRoleMenus);
        });
        try {
            //等待所有CompletableFuture执行完成
            CompletableFuture.allOf(c1,c2,c3).get();
        }catch (InterruptedException | ExecutionException e){
            throw new RuntimeException(e);
        }
//        System.out.println("异步执行用时："+(System.currentTimeMillis()-before));
        return userAuthoritiesRespDto;
    }
}
