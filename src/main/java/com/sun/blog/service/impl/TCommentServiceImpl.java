package com.sun.blog.service.impl;

import com.sun.blog.entity.TComment;
import com.sun.blog.mapper.TCommentMapper;
import com.sun.blog.service.TCOMMENTService;
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
public class TCommentServiceImpl extends ServiceImpl<TCommentMapper, TComment> implements TCOMMENTService {

}
