package com.sun.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

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
@ApiModel(value="TBlog对象", description="")
public class TBlog implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    //感谢，赞赏
    private Boolean appreciation;
    @ApiParam(name = "可评论的")
    private Boolean commentabled;
    @ApiParam(name = "内容")
    private String content;
    @ApiParam(name = "创建时间")
    private String createTime;
    private String firstPicture;
    @ApiParam(name = "标记")
    private String flag;
    @ApiParam(name = "公开")
    private Boolean published;
    @ApiParam(name = "推荐")
    private Boolean recommend;
    @ApiParam(name = "分享声明")
    private Boolean shareStatement;
    @ApiParam(name = "标题")
    private String title;
    @ApiParam(name = "更改时间")
    private Date updateTime;
    @ApiParam(name = "视图")
    private Integer views;
    
    private Long typeId;

    private Long userId;
//    另一个实体类,
    @TableField(exist = false)
    private TType type;
    @TableField(exist = false)
    private TTag tag;


}
