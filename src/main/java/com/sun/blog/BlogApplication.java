package com.sun.blog;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@MapperScan(value = "com.sun.blog.mapper")
public class BlogApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}
	
}
