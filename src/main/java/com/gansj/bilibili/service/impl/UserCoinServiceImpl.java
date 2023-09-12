package com.gansj.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gansj.bilibili.dao.UserCoin;
import com.gansj.bilibili.service.UserCoinService;
import com.gansj.bilibili.mapper.UserCoinMapper;
import org.springframework.stereotype.Service;

/**
* @author GSJ
* @description 针对表【user_coin(用户硬币数量表)】的数据库操作Service实现
* @createDate 2023-08-06 15:50:10
*/
@Service
public class UserCoinServiceImpl extends ServiceImpl<UserCoinMapper, UserCoin>
    implements UserCoinService{

}




