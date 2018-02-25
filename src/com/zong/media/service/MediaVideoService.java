package com.zong.media.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zong.core.bean.Page;
import com.zong.core.bean.PageData;
import com.zong.media.bean.MediaVideo;
import com.zong.media.dao.MediaVideoMapper;

/**
 * @desc video业务实现类
 * @author zong
 * @date 2018年02月19日
 */
@Service
public class MediaVideoService {
	@Autowired
	private MediaVideoMapper videoMapper;

	/**
	 * 新增video
	 * 
	 * @param video
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addMediaVideo(MediaVideo video) throws Exception {
		video.setId(UUID.randomUUID().toString().trim().replaceAll("-", ""));
		video.setCreateTime(new Date());
		video.setUpdateTime(new Date());
		videoMapper.insert(video);
	}
	
	/**
	 * 删除video
	 * 
	 * @param video
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteMediaVideo(MediaVideo video) throws Exception {
		videoMapper.delete(video);
	}
	
	/**
	 * 修改video
	 * 
	 * @param video
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void editMediaVideo(MediaVideo video) throws Exception {
		video.setUpdateTime(new Date());
		videoMapper.update(video);
	}

	/**
	 * 根据id查询video
	 * 
	 * @param video
	 * @return
	 */
	public MediaVideo loadMediaVideo(MediaVideo video) {
		return videoMapper.load(video);
	}

	/**
	 * 分页查询video
	 * 
	 * @param page
	 * @return
	 */
	public List<MediaVideo> findMediaVideoPage(Page page) {
		return videoMapper.findMediaVideoPage(page);
	}
	
	/**
	 * 查询全部video
	 * 
	 * @param pd
	 * @return
	 */
	public List<MediaVideo> findMediaVideo(PageData pd) {
		return videoMapper.findMediaVideo(pd);
	}
}
