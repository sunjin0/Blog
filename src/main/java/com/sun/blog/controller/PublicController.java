package com.sun.blog.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.blog.entity.TBlog;
import com.sun.blog.entity.TComment;
import com.sun.blog.entity.TTag;
import com.sun.blog.entity.TType;
import com.sun.blog.service.impl.TBlogServiceImpl;
import com.sun.blog.service.impl.TCommentServiceImpl;
import com.sun.blog.service.impl.TTagServiceImpl;
import com.sun.blog.service.impl.TTypeServiceImpl;
import com.sun.blog.tool.IpToAddressTool;
import com.sun.blog.tool.MarkdownTool;
import com.sun.blog.tool.Tool;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * 创建者:Sun<br>
 * 创建时间:2023/1/18&nbsp;16:37<br>
 * 描述:com.sun.blog.controller<br>
 */
@Controller
public class PublicController {
	@Resource
	private TBlogServiceImpl blogService;
	@Resource
	private TTypeServiceImpl typeService;
	@Resource
	private TTagServiceImpl tagService;
	@Resource
	private TCommentServiceImpl commentService;
	@RequestMapping(value = "/",method = {RequestMethod.POST,RequestMethod.GET})
	@Transactional
	public String index(Model model){
		Page<TBlog> blogPage = blogService.page(new Page<>(1, 6),new QueryWrapper<TBlog>().select("id","title","create_time","content","views").orderByDesc("create_time"));
		for (TBlog blog : blogPage.getRecords()) {
			if (blog.getContent().length() >250) {
				blog.setContent(blog.getContent().substring(0,250));
			}
			blog.setCreateTime(blog.getCreateTime().substring(0,16));
		}
		Page<TType> distinctName = typeService.page(new Page<>(1, 6), new QueryWrapper<TType>().select("distinct name","count(*) as id").groupBy("name"));
		TTag tag = tagService.getOne(new QueryWrapper<TTag>().eq("id", 0));
		model.addAttribute("tagD", Tool.StringToArray(tag.getTagName().substring(0, 55)));
		model.addAttribute("typeD",distinctName);
		model.addAttribute("blogPage",blogPage);
		return "index";
	}
	
	/**
	 *  利用模板引擎和ajax局部刷新
	 * @param model 数据容器
	 * @param page 页数
	 * @return 渲染后的局部模板
	 */
	@RequestMapping(value = "/blogPage/{page}",method = {RequestMethod.POST,RequestMethod.GET})
	@Transactional
	public String indexPage(Model model, @PathVariable Long page){
		Page<TBlog> blogPage = blogService.page(new Page<>(page, 6),new QueryWrapper<TBlog>().select("id","title","create_time","content","views").orderByDesc("create_time"));
		for (TBlog blog : blogPage.getRecords()) {
			if (blog.getContent().length() >250) {
				blog.setContent(blog.getContent().substring(0,250));
			}
			blog.setCreateTime(blog.getCreateTime().substring(0,16));
		}
		model.addAttribute("blogPage",blogPage);
		return "index::blog";
	}
	
	/**
	 * 搜索框提示
	 * @param codes 模糊查询字段
	 * @return 所有结果
	 */
	@ResponseBody
	@RequestMapping(value = "/search_hint{codes}",method = RequestMethod.GET)
	public String search_hint(@PathVariable String codes){
		Collection<TBlog> list=null;
		if (!Objects.equals(codes, "")) {
			list = blogService.list(new QueryWrapper<TBlog>().select("id","title").like("title", codes));
		}
		return JSON.toJSONString(list);
	}
	
	/**
	 * @param id 博客id
	 * @return 详情页面
	 */
	@Transactional
	@RequestMapping(value = "/blogDetails/{id}",method = RequestMethod.GET)
	public String blogDetails(@PathVariable Long id,Model model){
		TBlog blog = blogService.getBaseMapper().selectsByID(id);
		TBlog blog1 = new TBlog();
		blogService.update(blog1.setViews(blog.getViews()+1), new UpdateWrapper<TBlog>().eq("id", id));
		blog.setCreateTime(blog.getCreateTime().substring(0,16));
		blog.setContent(MarkdownTool.markdownAndIdAndTable(blog.getContent()));
		Collection<TComment> comments = commentService.list(new QueryWrapper<TComment>().eq("blog_id", id).isNull("parent_comment_id").orderByDesc("create_time"));
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
		model.addAttribute("blogDetails",blog);
		model.addAttribute("tags",Tool.StringToArray(blog.getTag().getTagName()));
		return "blog";
	}
	@RequestMapping(value = "/classify/{typeName}",method = RequestMethod.GET)
	public String classification(Model model, @PathVariable String typeName){
		QueryWrapper<TType> qw = new QueryWrapper<>();
		qw.select("distinct name");
		ArrayList<TType> typeList = (ArrayList<TType>) typeService.list(qw);
		Page<TBlog> blogInit = blogService.getBaseMapper().selectByTypeName(new Page<>(1, 2), typeName);
		for (TBlog blog : blogInit.getRecords()) {
			blog.setCreateTime(blog.getCreateTime().substring(0,16));
			if (blog.getContent().length()>=250) {
				blog.setContent(blog.getContent().substring(0,250));
			}
		}
		model.addAttribute("blogInit",blogInit);
		model.addAttribute("typeList",typeList);
		return "classify";
	}
	@RequestMapping(value = "/classify/{name}/{page}",method = RequestMethod.GET)
	public String classificationPage(Model model, @PathVariable String name, @PathVariable Long page){
		Page<TBlog> blogPage = blogService.getBaseMapper().selectByTypeName(new Page<>(page, 2),name );
		for (TBlog blog : blogPage.getRecords()) {
			blog.setCreateTime(blog.getCreateTime().substring(0,16));
			if (blog.getContent().length()>=250) {
				blog.setContent(blog.getContent().substring(0,250));
			}
		}
		QueryWrapper<TType> qw = new QueryWrapper<>();
		qw.select("distinct name");
		ArrayList<TType> typeList = (ArrayList<TType>) typeService.list(qw);
		model.addAttribute("typeList",typeList);
		model.addAttribute("blogInit",blogPage);
		return "classify::typeList";
	}
	@RequestMapping(value = "/labels",method = RequestMethod.GET)
	public String labels(){
		return "labels";
	}
	@RequestMapping(value = "/myself",method = RequestMethod.GET)
	public String myself(){
		return "myself";
	}
	@RequestMapping(value = "/file",method = RequestMethod.GET)
	public String file(Model model){
		ArrayList<TBlog> list = (ArrayList<TBlog>) blogService.list(new QueryWrapper<TBlog>().select("distinct DATE_FORMAT(create_time,'%Y') createTime"));
		ArrayList<ArrayList<TBlog>> blogs = new ArrayList<>();
		for (TBlog blog : list) {
			String createYear = blog.getCreateTime();
			ArrayList<TBlog> blogYear = (ArrayList<TBlog>) blogService.list(new QueryWrapper<TBlog>().select("id", "create_time", "title", "flag").isNotNull("create_time").likeRight("create_time", createYear));
			blogs.add(blogYear);
		}
		model.addAttribute("list",list);
		model.addAttribute("blogYear",blogs);
		return "file";
	}
}
