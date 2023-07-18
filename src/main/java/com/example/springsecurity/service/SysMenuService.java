package com.example.springsecurity.service;

import java.util.List;

public interface SysMenuService {

    List<String> queryPermissionsByUserId(Integer userId);
}
