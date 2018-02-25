package com.zong.blog.dao;

import java.util.List;

import com.zong.blog.bean.BlogArticle;
import com.zong.core.bean.Page;
import com.zong.core.bean.PageData;

public interface BlogArticleMapper {

	/**
	 * 根据对象主键查询
	 * @param article
	 */
	BlogArticle load(BlogArticle article);
	
	/**
	 * 根据条件分页查询
	 * 
	 * @param page
	 * @return
	 */
	List<BlogArticle> findBlogArticlePage(Page page);
	
	/**
	 * 根据条件查询全部
	 * 
	 * @param pd
	 * @return
	 */
	List<BlogArticle> findBlogArticle(PageData pd);
	
	/**
	 * 根据对象主键删除
	 * @param article
	 */
	void delete(BlogArticle article);
	
	/**
	 * 插入对象全部属性的字段
	 * @param article
	 */
	void insert(BlogArticle article);
	
	/**
	 * 根据主键更新对象不为空属性的字段
	 * @param article
	 */
	void update(BlogArticle article);
	
}