package com.zong.blog.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zong.blog.bean.BlogArticle;
import com.zong.blog.dao.BlogArticleMapper;
import com.zong.core.bean.Page;
import com.zong.core.bean.PageData;

/**
 * @desc article业务实现类
 * @author zong
 * @date 2018年02月18日
 */
@Service
public class BlogArticleService {
	@Autowired
	private BlogArticleMapper articleMapper;

	/**
	 * 新增article
	 * 
	 * @param article
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addBlogArticle(BlogArticle article) throws Exception {
		article.setId(UUID.randomUUID().toString().trim().replaceAll("-", ""));
		article.setCreateTime(new Date());
		article.setUpdateTime(new Date());
		articleMapper.insert(article);
	}
	
	/**
	 * 删除article
	 * 
	 * @param article
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteBlogArticle(BlogArticle article) throws Exception {
		articleMapper.delete(article);
	}
	
	/**
	 * 修改article
	 * 
	 * @param article
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void editBlogArticle(BlogArticle article) throws Exception {
		article.setUpdateTime(new Date());
		articleMapper.update(article);
	}

	/**
	 * 根据id查询article
	 * 
	 * @param article
	 * @return
	 */
	public BlogArticle loadBlogArticle(BlogArticle article) {
		return articleMapper.load(article);
	}

	/**
	 * 分页查询article
	 * 
	 * @param page
	 * @return
	 */
	public List<BlogArticle> findBlogArticlePage(Page page) {
		return articleMapper.findBlogArticlePage(page);
	}
	
	/**
	 * 查询全部article
	 * 
	 * @param pd
	 * @return
	 */
	public List<BlogArticle> findBlogArticle(PageData pd) {
		return articleMapper.findBlogArticle(pd);
	}
}
