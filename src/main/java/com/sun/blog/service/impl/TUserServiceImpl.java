package com.sun.blog.service.impl;

import com.sun.blog.entity.TUser;
import com.sun.blog.mapper.TUserMapper;
import com.sun.blog.service.TUSERService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 孙进
 * @since 2023-01-02
 */
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements TUSERService {
	@Resource
	private TUserMapper tUserMapper;
	public TUser login(String username,String password){
		return tUserMapper.login(username, password);
	}
	public List<TUser> selectAll(){
		return tUserMapper.selectAll();
	}
	
}
