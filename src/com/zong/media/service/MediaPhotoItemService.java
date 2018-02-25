package com.zong.media.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zong.core.bean.Page;
import com.zong.core.bean.PageData;
import com.zong.media.bean.MediaPhotoItem;
import com.zong.media.dao.MediaPhotoItemMapper;

/**
 * @desc photoItem业务实现类
 * @author zong
 * @date 2018年02月19日
 */
@Service
public class MediaPhotoItemService {
	@Autowired
	private MediaPhotoItemMapper photoItemMapper;

	/**
	 * 新增photoItem
	 * 
	 * @param photoItem
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addMediaPhotoItem(MediaPhotoItem photoItem) throws Exception {
		photoItem.setId(UUID.randomUUID().toString().trim().replaceAll("-", ""));
		photoItem.setCreateTime(new Date());
		photoItemMapper.insert(photoItem);
	}
	
	/**
	 * 删除photoItem
	 * 
	 * @param photoItem
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteMediaPhotoItem(MediaPhotoItem photoItem) throws Exception {
		photoItemMapper.delete(photoItem);
	}
	
	/**
	 * 修改photoItem
	 * 
	 * @param photoItem
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public void editMediaPhotoItem(MediaPhotoItem photoItem) throws Exception {
		List<MediaPhotoItem> items = photoItem.getItems();
		for (MediaPhotoItem item : items) {
			photoItemMapper.update(item);
		}
	}

	/**
	 * 根据id查询photoItem
	 * 
	 * @param photoItem
	 * @return
	 */
	public MediaPhotoItem loadMediaPhotoItem(MediaPhotoItem photoItem) {
		return photoItemMapper.load(photoItem);
	}

	/**
	 * 分页查询photoItem
	 * 
	 * @param page
	 * @return
	 */
	public List<MediaPhotoItem> findMediaPhotoItemPage(Page page) {
		return photoItemMapper.findMediaPhotoItemPage(page);
	}
	
	/**
	 * 查询全部photoItem
	 * 
	 * @param pd
	 * @return
	 */
	public List<MediaPhotoItem> findMediaPhotoItem(PageData pd) {
		return photoItemMapper.findMediaPhotoItem(pd);
	}
}
