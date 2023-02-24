package com.sun.blog.aspect;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 创建者:Sun<br>
 * 创建时间:2023/1/1&nbsp;13:44<br>
 * 描述:com.sun.blog.aspect<br>
 */
@Aspect
@Component
public class LogAspect {
	private final Logger logger= LoggerFactory.getLogger(this.getClass());
	@Pointcut("execution(* com.sun.blog.controller.*.*(..))")
	public void log(){
	
	}
	@Before("log()")
	public void doBefore(JoinPoint joinPoint){
		logger.info("...............执行前...............");
		ServletRequestAttributes attributes= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		assert attributes != null;
		HttpServletRequest request = attributes.getRequest();
		//获取请求IP地址，URL
		String url = request.getRequestURI();
		String ip=request.getRemoteAddr();
		//获取使用的类，参数
		String classMethod = joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();
		logger.info("RequestLog:{}",new RequestLog(url,ip,classMethod,args));
	}
	
	@After("log()")
	public void doAfter(){
		logger.info("...............执行后...............");
		
	}
	@AfterReturning(pointcut = "log()",returning = "result")
	public void doAfterReturn(Object result){
		logger.info("Result:{}",result);
	
	}
	
	/**
	 * RequestLog:信息
	 */
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	private  class RequestLog{
		private String url;
		private String ip;
		private String classMethod;
		private Object[] args;
		
	}
}

