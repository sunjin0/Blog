package com.sun.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author 孙进
 * @since 2023-01-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TComment对象", description="")
public class TComment implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String avatar;

    private String content;

    private Date createTime;

    private String email;

    private String nickname;

    private Long blogId;

    private Long parentCommentId;
    
    private String ads;
    //这个字段不在表里
    @TableField(exist = false)
    private Collection<TComment> commentChildren;
    private String fatherName;
    private String ip;


}
