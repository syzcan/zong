package com.zong.blog.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zong.admin.system.bean.SysUser;
import com.zong.blog.bean.BlogArticle;
import com.zong.blog.bean.BlogCategory;
import com.zong.blog.service.BlogArticleService;
import com.zong.blog.service.BlogCategoryService;
import com.zong.core.bean.Page;
import com.zong.core.bean.PageData;
import com.zong.core.bean.Result;
import com.zong.core.controller.BaseController;
import com.zong.core.exception.ServiceException;
import com.zong.core.util.ZConst;

/**
 * @desc article控制层 文章表
 * @author zong
 * @date 2018年02月18日
 */
@Controller
@RequestMapping(value = "/blog/article")
public class BlogArticleController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(BlogArticleController.class);

	@Autowired
	private BlogArticleService articleService;
	@Autowired
	private BlogCategoryService categoryService;

	/**
	 * 查询article列表
	 */
	@RequestMapping(value = "/list")
	public String list() {
		return "/blog/article/article_list";
	}
	
	/**
	 * 新增article页面
	 */
	@RequestMapping(value = "/toAdd")
	public String toAdd() {
		return "/blog/article/article_form";
	}

	/**
	 * 修改article页面
	 */
	@RequestMapping(value = "/toEdit")
	public String toEdit() {
		return "/blog/article/article_form";
	}
	
	/**
	 * 新增article
	 */
	@ResponseBody
	@RequestMapping(value = "/add")
	public Result add(BlogArticle article) {
		Result result = Result.success();
		try {
			SysUser user = (SysUser)getSession().getAttribute(ZConst.SESSION_USER);
			article.setUserId(user.getId());
			articleService.addBlogArticle(article);
		} catch (ServiceException e) {
			LOGGER.warn(e.getMessage());
			result.error(e);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}
	
	/**
	 * 修改article
	 */
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Result edit(BlogArticle article) {
		Result result = Result.success();
		try {
			articleService.editBlogArticle(article);
		} catch (ServiceException e) {
			LOGGER.warn(e.getMessage());
			result.error(e);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	/**
	 * 删除article
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Result delete(BlogArticle article) {
		Result result = Result.success();
		try {
			articleService.deleteBlogArticle(article);
		} catch (ServiceException e) {
			LOGGER.warn(e.getMessage());
			result.error(e);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}
	
	/**
	 * 查询article详情data
	 */
	@ResponseBody
	@RequestMapping(value = "/data")
	public Result data(BlogArticle article) {
		Result result = Result.success();
		try {
			article = articleService.loadBlogArticle(article);
			result.put("data", article);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	/**
	 * 查询article列表datas
	 */
	@ResponseBody
	@RequestMapping(value = "/datas")
	public Result datas() {
		Result result = Result.success();
		try {
			Page page = super.getPage();
			List<BlogArticle> articles = articleService.findBlogArticlePage(page);
			result.put("rows", articles).put("total", page.getTotalResult());
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}	
	
	@ResponseBody
	@RequestMapping(value = "/cateBox")
	public List<BlogCategory> cateBox() {
		return categoryService.findBlogCategory(new PageData());
	}	
}
