package com.example.springsecurity.service.impl;

import com.example.springsecurity.dao.SysMenuDao;
import com.example.springsecurity.service.SysMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Resource
    private SysMenuDao sysMenuDao;
    @Override
    public List<String> queryPermissionsByUserId(Integer userId) {
        return sysMenuDao.queryPermissionsByUserId(userId);
    }

}
