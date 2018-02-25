package com.zong.media.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zong.core.bean.Page;
import com.zong.core.bean.PageData;
import com.zong.media.bean.MediaMusic;
import com.zong.media.dao.MediaMusicMapper;

/**
 * @desc music业务实现类
 * @author zong
 * @date 2018年02月19日
 */
@Service
public class MediaMusicService {
	@Autowired
	private MediaMusicMapper musicMapper;

	/**
	 * 新增music
	 * 
	 * @param music
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addMediaMusic(MediaMusic music) throws Exception {
		music.setId(UUID.randomUUID().toString().trim().replaceAll("-", ""));
		music.setCreateTime(new Date());
		musicMapper.insert(music);
	}
	
	/**
	 * 删除music
	 * 
	 * @param music
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteMediaMusic(MediaMusic music) throws Exception {
		musicMapper.delete(music);
	}
	
	/**
	 * 修改music
	 * 
	 * @param music
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void editMediaMusic(MediaMusic music) throws Exception {
		musicMapper.update(music);
	}

	/**
	 * 根据id查询music
	 * 
	 * @param music
	 * @return
	 */
	public MediaMusic loadMediaMusic(MediaMusic music) {
		return musicMapper.load(music);
	}

	/**
	 * 分页查询music
	 * 
	 * @param page
	 * @return
	 */
	public List<MediaMusic> findMediaMusicPage(Page page) {
		return musicMapper.findMediaMusicPage(page);
	}
	
	/**
	 * 查询全部music
	 * 
	 * @param pd
	 * @return
	 */
	public List<MediaMusic> findMediaMusic(PageData pd) {
		return musicMapper.findMediaMusic(pd);
	}
}
