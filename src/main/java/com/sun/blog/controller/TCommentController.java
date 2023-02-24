package com.sun.blog.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sun.blog.entity.TComment;
import com.sun.blog.service.impl.TCommentServiceImpl;
import com.sun.blog.tool.IpToAddressTool;
import com.sun.blog.tool.IpUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 孙进
 * @since 2023-01-02
 */
@Controller
public class TCommentController {
	@Resource
	private TCommentServiceImpl commentService;
	@RequestMapping(value = "/comments",method = RequestMethod.GET)
	public String comment(TComment comment, HttpServletRequest request){
		String ip = IpUtil.getIpAddr(request);
		comment.setIp(ip);
		comment.setCreateTime(new Date());
		comment.setAds(IpToAddressTool.IpToAddress(ip));
		commentService.save(comment);
		return "redirect:/comments/"+comment.getBlogId();
	}
	@RequestMapping(value = "/comments/{blogId}",method = RequestMethod.GET)
	public String comment(@PathVariable String blogId,Model model){
		Collection<TComment> comments = commentService.list(new QueryWrapper<TComment>().eq("blog_id", blogId).isNull("parent_comment_id").orderByDesc("create_time"));
		for (TComment comment1 : comments) {
			if (comment1.getAds().length() >8) {
				String[] strings = comment1.getAds().split(",");
				comment1.setAds(strings[2].substring(0,2));
			}
			Collection<TComment> commentChildren = commentService.list(new QueryWrapper<TComment>().eq("parent_comment_id", comment1.getId()).orderByAsc("create_time"));
			for (TComment commentChild : commentChildren) {
				if (commentChild.getAds().length() >8) {
					String[] strings2 = commentChild.getAds().split(",");
					commentChild.setAds(strings2[2].substring(0,2));
				}
			}
			comment1.setCommentChildren(commentChildren);
		}
		model.addAttribute("comments",comments);
		return "blog::commentList";
	}
}

