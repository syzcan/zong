package com.zong.media.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zong.core.bean.Page;
import com.zong.core.bean.PageData;
import com.zong.media.bean.MediaPhoto;
import com.zong.media.dao.MediaPhotoMapper;

/**
 * @desc photo业务实现类
 * @author zong
 * @date 2018年02月19日
 */
@Service
public class MediaPhotoService {
	@Autowired
	private MediaPhotoMapper photoMapper;

	/**
	 * 新增photo
	 * 
	 * @param photo
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addMediaPhoto(MediaPhoto photo) throws Exception {
		photo.setId(UUID.randomUUID().toString().trim().replaceAll("-", ""));
		photo.setCreateTime(new Date());
		photo.setUpdateTime(new Date());
		photoMapper.insert(photo);
	}
	
	/**
	 * 删除photo
	 * 
	 * @param photo
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteMediaPhoto(MediaPhoto photo) throws Exception {
		photoMapper.delete(photo);
	}
	
	/**
	 * 修改photo
	 * 
	 * @param photo
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void editMediaPhoto(MediaPhoto photo) throws Exception {
		photo.setUpdateTime(new Date());
		photoMapper.update(photo);
	}

	/**
	 * 根据id查询photo
	 * 
	 * @param photo
	 * @return
	 */
	public MediaPhoto loadMediaPhoto(MediaPhoto photo) {
		return photoMapper.load(photo);
	}

	/**
	 * 分页查询photo
	 * 
	 * @param page
	 * @return
	 */
	public List<MediaPhoto> findMediaPhotoPage(Page page) {
		return photoMapper.findMediaPhotoPage(page);
	}
	
	/**
	 * 查询全部photo
	 * 
	 * @param pd
	 * @return
	 */
	public List<MediaPhoto> findMediaPhoto(PageData pd) {
		return photoMapper.findMediaPhoto(pd);
	}
}
