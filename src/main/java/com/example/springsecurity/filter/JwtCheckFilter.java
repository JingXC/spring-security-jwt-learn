package com.example.springsecurity.filter;


import com.example.springsecurity.entity.SysUser;
import com.example.springsecurity.util.JwtUtils;
import com.example.springsecurity.vo.HttpResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtCheckFilter extends OncePerRequestFilter {

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        //如果是登录请求，直接放行
        if (requestURI.equals("/login")){
            doFilter(request,response,filterChain);
            return;
        }
        String strAuth = request.getHeader("Authorization");
        if(StringUtils.isEmpty(strAuth)){
            HttpResult httpResult = HttpResult.builder()
                    .code(0)
                    .msg("Authorization 为空")
                    .build();
            printToken(request,response, httpResult);
            return;
        }
        String jwtToken = strAuth.replace("bearer ", "");
        if(StringUtils.containsWhitespace(jwtToken)){
            HttpResult httpResult = HttpResult.builder()
                    .code(0)
                    .msg("jwt 为空")
                    .build();
            printToken(request,response, httpResult);
            return;
        }
        //校验jwt
        boolean b = jwtUtils.verifyToken(jwtToken);
        if (!b){
            HttpResult httpResult = HttpResult.builder()
                    .code(0)
                    .msg("jwt非法")
                    .build();
            printToken(request,response, httpResult);
            return;
        }

        //判断redis中是否存在jwt
        String token = stringRedisTemplate.opsForValue().get("loginJwtToken:" + jwtToken);
        if (StringUtils.isEmpty(token)){
            HttpResult httpResult = HttpResult.builder()
                    .code(0)
                    .msg("您已退出，请重新登录")
                    .build();
            printToken(request,response, httpResult);
            return;
        }


        //从jwt中获取用户信息
        String userInfoFromToken = jwtUtils.getUserInfoFromToken(jwtToken);
        List<String> authListFromToken = jwtUtils.getUserAuthFromToken(jwtToken);

        SysUser sysUser = objectMapper.readValue(userInfoFromToken, SysUser.class);//反序列化成用户信息
        List<SimpleGrantedAuthority> authorityList = authListFromToken.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        //用户名密码认证token
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(sysUser,null,authorityList);
        //把token放入安全上下文 securityContext
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        doFilter(request,response,filterChain);
    }

    private void printToken(HttpServletRequest request, HttpServletResponse response, HttpResult httpResult) throws IOException {
        String strResponse = objectMapper.writeValueAsString(httpResult);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.println(strResponse);
        writer.flush();
    }
}
