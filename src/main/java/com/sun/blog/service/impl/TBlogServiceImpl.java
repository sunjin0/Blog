package com.sun.blog.service.impl;

import com.sun.blog.entity.TBlog;
import com.sun.blog.mapper.TBlogMapper;
import com.sun.blog.service.TBLOGService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 孙进
 * @since 2023-01-02
 */
@Service
public class TBlogServiceImpl extends ServiceImpl<TBlogMapper, TBlog> implements TBLOGService {

}
