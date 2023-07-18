package com.example.springsecurity.config;

import com.example.springsecurity.entity.SysUser;
import com.example.springsecurity.util.JwtUtils;
import com.example.springsecurity.vo.HttpResult;
import com.example.springsecurity.vo.MySecurityUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class MyAutheticationSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //从认证信息中获取登录用户信息
        MySecurityUser mySecurityUser = (MySecurityUser) authentication.getPrincipal();
        SysUser sysUser = mySecurityUser.getSysUser();
        String sysUserInfo = objectMapper.writeValueAsString(sysUser);
        //获取用户的权限信息
        List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) mySecurityUser.getAuthorities();
        List<String> authList = authorities.stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList());
        //生成jwt
        String jwtToken = jwtUtils.createJwt(sysUserInfo, authList);
        HttpResult httpResult = HttpResult.builder()
                .code(200)
                .msg("jwt生成成功")
                .data(jwtToken)
                .build();
        //将jwt放进redis
        stringRedisTemplate.opsForValue().set("loginJwtToken:"+ jwtToken,objectMapper.writeValueAsString(authentication),2, TimeUnit.HOURS);
        printToken(request,response,httpResult);
    }
    private void printToken(HttpServletRequest request, HttpServletResponse response,HttpResult httpResult) throws IOException {
        String strResponse = objectMapper.writeValueAsString(httpResult);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.println(strResponse);
        writer.flush();
    }
}
