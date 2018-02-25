package com.zong.media.dao;

import java.util.List;

import com.zong.media.bean.MediaVideo;
import com.zong.core.bean.Page;
import com.zong.core.bean.PageData;

public interface MediaVideoMapper {

	/**
	 * 根据对象主键查询
	 * @param video
	 */
	MediaVideo load(MediaVideo video);
	
	/**
	 * 根据条件分页查询
	 * 
	 * @param page
	 * @return
	 */
	List<MediaVideo> findMediaVideoPage(Page page);
	
	/**
	 * 根据条件查询全部
	 * 
	 * @param pd
	 * @return
	 */
	List<MediaVideo> findMediaVideo(PageData pd);
	
	/**
	 * 根据对象主键删除
	 * @param video
	 */
	void delete(MediaVideo video);
	
	/**
	 * 插入对象全部属性的字段
	 * @param video
	 */
	void insert(MediaVideo video);
	
	/**
	 * 根据主键更新对象不为空属性的字段
	 * @param video
	 */
	void update(MediaVideo video);
	
}