package com.zong.media.dao;

import java.util.List;

import com.zong.media.bean.MediaPhoto;
import com.zong.core.bean.Page;
import com.zong.core.bean.PageData;

public interface MediaPhotoMapper {

	/**
	 * 根据对象主键查询
	 * @param photo
	 */
	MediaPhoto load(MediaPhoto photo);
	
	/**
	 * 根据条件分页查询
	 * 
	 * @param page
	 * @return
	 */
	List<MediaPhoto> findMediaPhotoPage(Page page);
	
	/**
	 * 根据条件查询全部
	 * 
	 * @param pd
	 * @return
	 */
	List<MediaPhoto> findMediaPhoto(PageData pd);
	
	/**
	 * 根据对象主键删除
	 * @param photo
	 */
	void delete(MediaPhoto photo);
	
	/**
	 * 插入对象全部属性的字段
	 * @param photo
	 */
	void insert(MediaPhoto photo);
	
	/**
	 * 根据主键更新对象不为空属性的字段
	 * @param photo
	 */
	void update(MediaPhoto photo);
	
}