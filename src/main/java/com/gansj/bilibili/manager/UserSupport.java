package com.gansj.bilibili.manager;

import com.gansj.bilibili.common.ErrorCode;
import com.gansj.bilibili.exception.BusinessException;
import com.gansj.bilibili.utils.TokenUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class UserSupport {

    public Long getCurrentUserId(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();

        String token = requestAttributes.getRequest().getHeader("token");
        Long userId = TokenUtil.verifyToken(token);
        if (userId < 0){
            throw new BusinessException(ErrorCode.NO_AUTH,"非法用户");
        }
        return userId;
    }
}
