package com.example.springsecurity.service.impl;

import com.example.springsecurity.dao.SysUserDao;
import com.example.springsecurity.entity.SysUser;
import com.example.springsecurity.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserDao sysUserDao;
    @Override
    public SysUser getByUserName(String userName) {
        return sysUserDao.getByUserName(userName);
    }
}
