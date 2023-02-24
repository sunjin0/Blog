package com.sun.blog.mapper;

import com.sun.blog.entity.TUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 孙进
 * @since 2023-01-02
 */
@Mapper
public interface TUserMapper extends BaseMapper<TUser> {
	
	TUser login(String username, String password);
	List<TUser> selectAll();
}
