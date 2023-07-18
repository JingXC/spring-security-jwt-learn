package com.example.springsecurity.service;

import com.example.springsecurity.entity.SysUser;

public interface SysUserService {

    /**
     * 根据用户名获取用户信息
     * @param userName
     * @return
     */
    SysUser getByUserName(String userName);

}
