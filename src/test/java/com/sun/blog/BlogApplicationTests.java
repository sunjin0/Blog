package com.sun.blog;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.sun.blog.controller.TBlogController;
import com.sun.blog.entity.TBlog;
import com.sun.blog.entity.TComment;
import com.sun.blog.entity.TTag;
import com.sun.blog.entity.TType;
import com.sun.blog.mapper.TBlogMapper;
import com.sun.blog.mapper.TTypeMapper;
import com.sun.blog.service.TBLOGService;
import com.sun.blog.service.TTYPEService;
import com.sun.blog.service.TUSERService;
import com.sun.blog.service.impl.*;
import com.sun.blog.tool.IpToAddressTool;
import com.sun.blog.tool.IpUtil;
import com.sun.blog.tool.Tool;
import org.apache.catalina.WebResourceRoot;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

@SpringBootTest
class BlogApplicationTests {
	@Resource
	private TTypeServiceImpl typeService;
	@Resource
	private TBlogServiceImpl blogService;
	@Resource
	private TTagServiceImpl tagService;
	@Resource
	private TCommentServiceImpl commentService;
	@Test
	void text1(){
		
	}

//		blogService.list(new QueryWrapper<TBlog>().select("id","createTime","title","flag").groupBy("createTime").orderByDesc("createTime"))
	
	void text(){
		Page<TType> page = typeService.page(new Page<>(2,2),null);
		System.out.println("数据为："+page.getRecords().toString());
		System.out.println("总数为："+page.getTotal()+",总页数为："+page.getPages());
		System.out.println("当前页为："+page.getCurrent()+",每页限制："+page.getSize());
		//"一个人的自我修养"
		Page<TBlog> page1 = blogService.getBaseMapper().selectPages(new Page<>(1,4),null , null, null);
		System.out.println(JSON.toJSONString(page));
		System.out.println("数据为："+page.getRecords().toString());
		System.out.println("总数为："+page.getTotal()+",总页数为："+page.getPages());
		System.out.println("当前页为："+page.getCurrent()+",每页限制："+page.getSize());
		String s="[Java,Spring,HTML]";
		String s1 = s.substring(1, s.length() - 1);
		System.out.println("分割后:"+s1);
		String[] split;
		for (int i = 0; i < s1.length(); i++) {
			s1 = s1.replace(" ", "");
		}
		split = s1.split(",");
	}
	void contextLoads() {
		//代码自动生成器 对象
		AutoGenerator generator = new AutoGenerator();
		//1.全局配置
		GlobalConfig config = new GlobalConfig();
		String propertyPath = System.getProperty("user.dir");
		config.setOutputDir(propertyPath+ "/src/main/java");
		config.setAuthor("孙进");
		config.setOpen(false);
		config.setFileOverride(false);
		config.setServiceName("%SService");
		config.setIdType(IdType.ASSIGN_ID);
		config.setDateType(DateType.ONLY_DATE);
		config.setSwagger2(true);
		generator.setGlobalConfig(config);
		//2.数据库配置
		DataSourceConfig dataSourceConfig = new DataSourceConfig();
		dataSourceConfig.setUrl("jdbc:mysql://localhost:3306/gradem?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&&serviceTimezone=GMT%2B8");
		dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
		dataSourceConfig.setUsername("root");
		dataSourceConfig.setPassword("wan521ioy");
		dataSourceConfig.setDbType(DbType.MYSQL);
		generator.setDataSource(dataSourceConfig);
		//3.包的配置
		PackageConfig packageConfig = new PackageConfig();
//		packageConfig.setModuleName("Blog");
		packageConfig.setParent("com.sun.blog");
		packageConfig.setEntity("entity");
		packageConfig.setMapper("mapper");
		packageConfig.setService("service");
		packageConfig.setController("controller");
		generator.setPackageInfo(packageConfig);
		//4.策略配置
		StrategyConfig strategyConfig = new StrategyConfig();
		strategyConfig.setInclude("t_blog","t_comment","t_tag","t_type","t_user");//表名
		strategyConfig.setNaming(NamingStrategy.underline_to_camel);
		strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
		strategyConfig.setEntityLombokModel(true);
		
		strategyConfig.setLogicDeleteFieldName("deleted");
		//自动填充配置
		TableFill gmt_create = new TableFill("gmt_create", FieldFill.INSERT);
		TableFill gmt_modified = new TableFill("gmt_modified", FieldFill.INSERT_UPDATE);
		ArrayList<TableFill> tableFill = new ArrayList<>();
		tableFill.add(gmt_create);
		tableFill.add(gmt_modified);
		strategyConfig.setTableFillList(tableFill);
		//乐观锁
		strategyConfig.setVersionFieldName("version");
		
		strategyConfig.setRestControllerStyle(true);
		strategyConfig.setControllerMappingHyphenStyle(true);
		generator.setStrategy(strategyConfig);
		//执行
		generator.execute();
	}
	
}
