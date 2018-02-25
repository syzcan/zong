package com.zong.media.dao;

import java.util.List;

import com.zong.media.bean.MediaMusic;
import com.zong.core.bean.Page;
import com.zong.core.bean.PageData;

public interface MediaMusicMapper {

	/**
	 * 根据对象主键查询
	 * @param music
	 */
	MediaMusic load(MediaMusic music);
	
	/**
	 * 根据条件分页查询
	 * 
	 * @param page
	 * @return
	 */
	List<MediaMusic> findMediaMusicPage(Page page);
	
	/**
	 * 根据条件查询全部
	 * 
	 * @param pd
	 * @return
	 */
	List<MediaMusic> findMediaMusic(PageData pd);
	
	/**
	 * 根据对象主键删除
	 * @param music
	 */
	void delete(MediaMusic music);
	
	/**
	 * 插入对象全部属性的字段
	 * @param music
	 */
	void insert(MediaMusic music);
	
	/**
	 * 根据主键更新对象不为空属性的字段
	 * @param music
	 */
	void update(MediaMusic music);
	
}