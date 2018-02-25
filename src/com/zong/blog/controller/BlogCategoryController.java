package com.zong.blog.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zong.blog.bean.BlogCategory;
import com.zong.blog.service.BlogCategoryService;
import com.zong.core.bean.PageData;
import com.zong.core.bean.Result;
import com.zong.core.controller.BaseController;
import com.zong.core.exception.ServiceException;

/**
 * @desc category控制层 文章类别表
 * @author zong
 * @date 2018年02月18日
 */
@Controller
@RequestMapping(value = "/blog/category")
public class BlogCategoryController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(BlogCategoryController.class);

	@Autowired
	private BlogCategoryService categoryService;

	/**
	 * 查询category列表
	 */
	@RequestMapping(value = "/list")
	public String list() {
		return "/blog/category/category_list";
	}
	
	/**
	 * 新增category页面
	 */
	@RequestMapping(value = "/toAdd")
	public String toAdd() {
		return "/blog/category/category_form";
	}

	/**
	 * 修改category页面
	 */
	@RequestMapping(value = "/toEdit")
	public String toEdit() {
		return "/blog/category/category_form";
	}
	
	/**
	 * 新增category
	 */
	@ResponseBody
	@RequestMapping(value = "/add")
	public Result add(BlogCategory category) {
		Result result = Result.success();
		try {
			categoryService.addBlogCategory(category);
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
	 * 修改category
	 */
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Result edit(BlogCategory category) {
		Result result = Result.success();
		try {
			categoryService.editBlogCategory(category);
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
	 * 删除category
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Result delete(BlogCategory category) {
		Result result = Result.success();
		try {
			categoryService.deleteBlogCategory(category);
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
	 * 查询category详情data
	 */
	@ResponseBody
	@RequestMapping(value = "/data")
	public Result data(BlogCategory category) {
		Result result = Result.success();
		try {
			category = categoryService.loadBlogCategory(category);
			result.put("data", category);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	/**
	 * 查询category列表datas
	 */
	@ResponseBody
	@RequestMapping(value = "/datas")
	public Result datas() {
		Result result = Result.success();
		try {
			List<BlogCategory> categorys = categoryService.findBlogCategory(new PageData());
			result.put("rows", categorys);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}	
}
