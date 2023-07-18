package com.example.springsecurity.service.impl;

import com.example.springsecurity.entity.SysUser;
import com.example.springsecurity.service.SysMenuService;
import com.example.springsecurity.service.SysUserService;
import com.example.springsecurity.vo.MySecurityUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SecurityUserDetailServiceImpl implements UserDetailsService {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysMenuService sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SysUser sysUser = sysUserService.getByUserName(username);
        if (null==sysUser){
            throw new UsernameNotFoundException("该用户不存在");
        }

        //根据用户id获取该用户所拥有的权限，List<SimpleGrantedAuthority>
        List<String> userPermissions = sysMenuService.queryPermissionsByUserId(sysUser.getUserId());
        //遍历权限，把权限放到
        List<SimpleGrantedAuthority> authorityList=new ArrayList<>();
        for (String userPermission : userPermissions) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(userPermission);
            authorityList.add(simpleGrantedAuthority);
        }

        MySecurityUser securityUser= new MySecurityUser(sysUser);
        securityUser.setAuthorityList(authorityList);
        return securityUser;
    }
}
