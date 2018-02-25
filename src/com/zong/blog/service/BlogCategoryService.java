package com.zong.blog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zong.blog.bean.BlogCategory;
import com.zong.blog.dao.BlogCategoryMapper;
import com.zong.core.bean.Page;
import com.zong.core.bean.PageData;

/**
 * @desc category业务实现类
 * @author zong
 * @date 2018年02月18日
 */
@Service
public class BlogCategoryService {
	@Autowired
	private BlogCategoryMapper categoryMapper;

	/**
	 * 新增category
	 * 
	 * @param category
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addBlogCategory(BlogCategory category) throws Exception {
		//category.setId(UUID.randomUUID().toString().trim().replaceAll("-", ""));
		categoryMapper.insert(category);
	}
	
	/**
	 * 删除category
	 * 
	 * @param category
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteBlogCategory(BlogCategory category) throws Exception {
		categoryMapper.delete(category);
	}
	
	/**
	 * 修改category
	 * 
	 * @param category
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void editBlogCategory(BlogCategory category) throws Exception {
		categoryMapper.update(category);
	}

	/**
	 * 根据id查询category
	 * 
	 * @param category
	 * @return
	 */
	public BlogCategory loadBlogCategory(BlogCategory category) {
		return categoryMapper.load(category);
	}

	/**
	 * 分页查询category
	 * 
	 * @param page
	 * @return
	 */
	public List<BlogCategory> findBlogCategoryPage(Page page) {
		return categoryMapper.findBlogCategoryPage(page);
	}
	
	/**
	 * 查询全部category
	 * 
	 * @param pd
	 * @return
	 */
	public List<BlogCategory> findBlogCategory(PageData pd) {
		return categoryMapper.findBlogCategory(pd);
	}
}
