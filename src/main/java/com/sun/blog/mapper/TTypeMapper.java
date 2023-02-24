package com.sun.blog.mapper;

import com.sun.blog.entity.TType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

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
public interface TTypeMapper extends BaseMapper<TType> {
	List<TType> selectAll();
}
