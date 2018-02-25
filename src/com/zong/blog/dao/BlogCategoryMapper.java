package com.zong.blog.dao;

import java.util.List;

import com.zong.blog.bean.BlogCategory;
import com.zong.core.bean.Page;
import com.zong.core.bean.PageData;

public interface BlogCategoryMapper {

	/**
	 * 根据对象主键查询
	 * @param category
	 */
	BlogCategory load(BlogCategory category);
	
	/**
	 * 根据条件分页查询
	 * 
	 * @param page
	 * @return
	 */
	List<BlogCategory> findBlogCategoryPage(Page page);
	
	/**
	 * 根据条件查询全部
	 * 
	 * @param pd
	 * @return
	 */
	List<BlogCategory> findBlogCategory(PageData pd);
	
	/**
	 * 根据对象主键删除
	 * @param category
	 */
	void delete(BlogCategory category);
	
	/**
	 * 插入对象全部属性的字段
	 * @param category
	 */
	void insert(BlogCategory category);
	
	/**
	 * 根据主键更新对象不为空属性的字段
	 * @param category
	 */
	void update(BlogCategory category);
	
}