package com.example.springsecurity.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysMenuDao {

    List<String> queryPermissionsByUserId(@Param("userId") Integer userId);
}
