package com.sun.blog.config;

import com.sun.blog.config.intercepors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 创建者:Sun<br>
 * 创建时间:2023/1/3&nbsp;19:01<br>
 * 描述:com.sun.blog.config<br>
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Resource
	private LoginInterceptor loginInterceptor;
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// addPathPatterns("/**") 表示拦截所有的请求，
		// excludePathPatterns("/login", "/register") 表示除了登陆与注册之外，因为登陆注册不需要登陆也可以访问
		//"/register","/static/**","/swagger-ui.html","/webjars/**","/swagger-resources/**"
		registry.addInterceptor(loginInterceptor).addPathPatterns("/user/**").excludePathPatterns("/user/login","/","/user/blogUpdate/**");
	}
}
