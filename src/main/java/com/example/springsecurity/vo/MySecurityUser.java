package com.example.springsecurity.vo;

import com.example.springsecurity.entity.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class MySecurityUser implements UserDetails {

    private final SysUser sysUser;

    public SysUser getSysUser() {
        return sysUser;
    }

    //用于存储权限的list
    private List<SimpleGrantedAuthority> authorityList;
    /**
     * 返回用户所拥有的权限
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorityList;
    }
    public void setAuthorityList(List<SimpleGrantedAuthority> authorityList) {
        this.authorityList = authorityList;
    }

    public MySecurityUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    @Override
    public String getPassword() {
        String myPassword = sysUser.getPassword();
        sysUser.setPassword(null);
        return myPassword;
    }

    @Override
    public String getUsername() {
        return sysUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return sysUser.getAccountNoExpired().equals(1);
    }

    @Override
    public boolean isAccountNonLocked() {
        return sysUser.getAccountNoLocked().equals(1);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return sysUser.getCredentialsNoExpired().equals(1);
    }

    @Override
    public boolean isEnabled() {
        return sysUser.getEnabled().equals(1);
    }
}
