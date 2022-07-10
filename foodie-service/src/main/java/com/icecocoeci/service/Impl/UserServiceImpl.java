package com.icecocoeci.service.Impl;

import com.icecocoeci.mapper.UsersMapper;
import com.icecocoeci.pojo.Users;
import com.icecocoeci.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

/**
 * Created with IntelliJ IDEA.
 *
 * @auther wlf
 * @Date: 2022/06/07/21:19
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    public UsersMapper usersMapper;


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean quaryUsernameIsExist(String username) {

        //演示Example
        //针对用户表映射的Example
        Example userExample = new Example(Users.class);
        //创建条件
        Example.Criteria userExampleCriteria = userExample.createCriteria();

        userExampleCriteria.andEqualTo("username",username);

        return false;
    }
}
