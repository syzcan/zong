package com.zong.media.dao;

import java.util.List;

import com.zong.media.bean.MediaPhotoItem;
import com.zong.core.bean.Page;
import com.zong.core.bean.PageData;

public interface MediaPhotoItemMapper {

	/**
	 * 根据对象主键查询
	 * @param photoItem
	 */
	MediaPhotoItem load(MediaPhotoItem photoItem);
	
	/**
	 * 根据条件分页查询
	 * 
	 * @param page
	 * @return
	 */
	List<MediaPhotoItem> findMediaPhotoItemPage(Page page);
	
	/**
	 * 根据条件查询全部
	 * 
	 * @param pd
	 * @return
	 */
	List<MediaPhotoItem> findMediaPhotoItem(PageData pd);
	
	/**
	 * 根据对象主键删除
	 * @param photoItem
	 */
	void delete(MediaPhotoItem photoItem);
	
	/**
	 * 插入对象全部属性的字段
	 * @param photoItem
	 */
	void insert(MediaPhotoItem photoItem);
	
	/**
	 * 根据主键更新对象不为空属性的字段
	 * @param photoItem
	 */
	void update(MediaPhotoItem photoItem);
	
}