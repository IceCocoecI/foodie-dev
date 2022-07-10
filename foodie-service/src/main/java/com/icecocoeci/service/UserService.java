package com.icecocoeci.service;

/**
 * Created with IntelliJ IDEA.
 *
 * @auther wlf
 * @Date: 2022/06/07/16:54
 * @Description:
 */
public interface UserService {
    /**
    * 判断用户名是否存在
    */
    public boolean quaryUsernameIsExist(String username);
}
