package com.example.springsecurity;

import com.example.springsecurity.dao.SysUserDao;
import com.example.springsecurity.entity.SysUser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class SpringSecurityApplicationTests {

	@Resource
	SysUserDao sysUserDao;

	@Test
	void contextLoads() {

	}

}
