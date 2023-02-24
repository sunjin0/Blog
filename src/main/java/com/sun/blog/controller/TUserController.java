package com.sun.blog.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.blog.config.MD5;
import com.sun.blog.entity.TBlog;
import com.sun.blog.entity.TTag;
import com.sun.blog.entity.TType;
import com.sun.blog.entity.TUser;
import com.sun.blog.service.impl.TBlogServiceImpl;
import com.sun.blog.service.impl.TTagServiceImpl;
import com.sun.blog.service.impl.TTypeServiceImpl;
import com.sun.blog.service.impl.TUserServiceImpl;
import com.sun.blog.tool.Tool;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 孙进
 * @since 2023-01-02
 */
@Controller
@RequestMapping("/user")
public class TUserController {
    @Resource
	private TUserServiceImpl userService;
	@Resource
	private TTypeServiceImpl typeService;
	@Resource
	private TBlogServiceImpl blogService;
	@Resource
	private TTagServiceImpl tagService;
	
	/**
	 *
	 * @param username 用户名
	 * @param password 密码
	 *
	 * @return
	 */
	@RequestMapping(value = "/login",method = {RequestMethod.POST,RequestMethod.GET})
	@Transactional
	public String login(@RequestParam String username, @RequestParam String password, HttpServletRequest request, HttpSession session){
		if (Objects.equals(username, "") ||Objects.equals(password, "")) {
			request.setAttribute("Error","请输入账号与密码");
			return "index";
		}else {
			TUser one = userService.login(username,MD5.encrypt(password));
			if (one == null) {
				request.setAttribute("Error","账号或者密码错误");
				return "index";
			} else if (one.getUsername().equals(username)&&one.getPassword().equals(MD5.encrypt(password))) {
				session.setMaxInactiveInterval(60*60*24);//一天
				session.setAttribute("user",username);
				return "admin/admin";
			}else {
				return "index";
			}
		}
	}
	
	@Transactional
	@ResponseBody
	@RequestMapping(value = "/logout",method = RequestMethod.GET)
	public void logout(HttpSession session){
		session.removeAttribute("user");
	}
	
	@Transactional
	@RequestMapping(value = "/release",method = RequestMethod.GET)
	public String release(Model model){
		TTag one = tagService.getOne(new QueryWrapper<TTag>().select("tag_name").eq("id", 0));
		model.addAttribute("Tag",Tool.StringToArray(one.getTagName()));
		return "admin/release";
	}
	@Transactional
	@RequestMapping(value = "/typeInit",method = RequestMethod.GET)
	public String TypeInit(Model model){
		Page<TType> typePage = typeService.page(new Page<>(1, 4), null);
		Collection<TType> tTypes = typeService.list(new QueryWrapper<TType>().select("DISTINCT name"));
		model.addAttribute("pageList",typePage);
		model.addAttribute("names",tTypes);
		return "admin/class-model";
	}
	
	@Transactional
	@RequestMapping(value = "/typePage/{page}",method = RequestMethod.POST)
	public String typePage(Model model,@PathVariable int page){
		BaseMapper<TType> baseMapper = typeService.getBaseMapper();
		ArrayList<TType> tTypes = (ArrayList<TType>) baseMapper.selectList(null);
		Page<TType> typePage = typeService.page(new Page<>(page, 4), null);
		model.addAttribute("pageList",typePage);
		model.addAttribute("names",tTypes);
		return "admin/class-model";
	}
	
	@Transactional
	@RequestMapping(value ="/blog",method = RequestMethod.GET)
	public String blog(){
		return "admin/blog";
	}
	
	/**
	 *
	 * @param page 页数
	 * @return JSON格式数据
	 */
	@RequestMapping(value = "/blogInit{page}",method = RequestMethod.GET)
	@ResponseBody
	@Transactional
	public String blogInit(@PathVariable Long page){
		Page<TBlog> blogPage = blogService.getBaseMapper().selectPages(new Page<>(page, 4), null, null, null);
		
		return JSON.toJSONString(blogPage);
	}
	
	@Transactional
	@RequestMapping(value = "/blogTypeInit",method = RequestMethod.GET)
	@ResponseBody
	public String blogType(){
		ArrayList<TType> tTypes = (ArrayList<TType>) typeService.getBaseMapper().selectAll();
		return JSON.toJSONString(tTypes);
	}
	
	@Transactional
	@RequestMapping(value = "/blogPage{page}",method = {RequestMethod.POST})
	@ResponseBody
	public String blogPage( String title, String type, Boolean isBoolean, @PathVariable Long page){
		if ("".equals(title)) {
			title=null;
		}
		if ("".equals(type)) {
			type=null;
		}
		
		IPage<TBlog> blogIPage = blogService.getBaseMapper().selectPages(new Page<>(page, 4), title, type, isBoolean);
		return JSON.toJSONString(blogIPage);
	}
	
	@Transactional
	@ResponseBody
	@RequestMapping(value = "/blogInsert" ,method = RequestMethod.POST)
	public String blogInsert(String typeName, TBlog blog, String[] tag, HttpServletRequest request){
		String user = (String) request.getSession().getAttribute("user");
		TUser one = userService.getOne(new QueryWrapper<TUser>().eq("username", user));
		blog.setUserId(one.getId());
		blog.setCreateTime(String.valueOf(new Date()));
		if (blog.getShareStatement() == null) {
			blog.setShareStatement(false);
		}
		if (blog.getPublished() == null) {
			blog.setPublished(false);
		}
		if (blog.getRecommend() == null) {
			blog.setRecommend(false);
		}
		if (blog.getCommentabled() == null) {
			blog.setCommentabled(false);
		}
		if (blog.getAppreciation() == null) {
			blog.setAppreciation(false);
		}
		String msg="添加失败";
		boolean save = blogService.getBaseMapper().blogSave(blog);
		boolean save2 = tagService.save(new TTag().setTagName(Arrays.toString(tag)));
		boolean save1 = typeService.save(new TType().setName(typeName));
		if (save&&save1&&save2) {
			msg="添加成功";
		}
		return msg;
	}
	
	@Transactional
	@RequestMapping(value = "/blogUpdatePage/{id}" ,method = RequestMethod.GET)
	public String blogUpdatePage (@PathVariable Long id,Model model){
		TBlog blog = blogService.getBaseMapper().selectsByID(id);
		ArrayList<TType> types = (ArrayList<TType>) typeService.getBaseMapper().selectAll();
		TTag one = tagService.getOne(new QueryWrapper<TTag>().eq("id", 0));
		model.addAttribute("message",blog);
		model.addAttribute("type",types);
		model.addAttribute("tagList",Tool.StringToArray(one.getTagName()));
		model.addAttribute("Tag",JSON.toJSONString(blog.getTag().getTagName()));
		return "admin/update";
	}
	@Transactional
	@ResponseBody
	@RequestMapping(value = "/blogUpdate/{id}",method =RequestMethod.POST)
	public String blogUpdate(@PathVariable String id,String typeName, TBlog blog, String[] tags){
		String msg="修改失败";
		
		boolean update = blogService.saveOrUpdate(blog.setUpdateTime(new Date()), new QueryWrapper<TBlog>().eq("id", id));
		boolean update1 = typeService.saveOrUpdate(new TType().setName(typeName), new QueryWrapper<TType>().eq("id", id));
		boolean update2 = tagService.saveOrUpdate(new TTag().setTagName(Arrays.toString(tags)),new QueryWrapper<TTag>().eq("id",id));
		if (update&&update1&&update2) {
			msg="修改成功";
		}
		return msg;
		
		
	}
}

