package com.sun.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.blog.entity.TBlog;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 孙进
 * @since 2023-01-02
 */
@Mapper
public interface TBlogMapper extends BaseMapper<TBlog> {
	
	Page<TBlog> selectPages(Page<TBlog> page, String title, String type, Boolean isbBoolean);
	boolean blogSave(TBlog blog);
	TBlog selectsByID(Long id);
	Page<TBlog> selectByTypeName(IPage<TBlog> page,String TypeName);
}
