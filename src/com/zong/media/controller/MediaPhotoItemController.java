package com.zong.media.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zong.media.bean.MediaPhotoItem;
import com.zong.media.service.MediaPhotoItemService;
import com.zong.core.bean.Page;
import com.zong.core.bean.Result;
import com.zong.core.controller.BaseController;
import com.zong.core.exception.ServiceException;

/**
 * @desc photoItem控制层 相册详情，相片表
 * @author zong
 * @date 2018年02月19日
 */
@Controller
@RequestMapping(value = "/media/photoItem")
public class MediaPhotoItemController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(MediaPhotoItemController.class);

	@Autowired
	private MediaPhotoItemService photoItemService;

	/**
	 * 查询photoItem列表
	 */
	@RequestMapping(value = "/list")
	public String list() {
		return "/media/photoItem/photoItem_list";
	}
	
	/**
	 * 新增photoItem页面
	 */
	@RequestMapping(value = "/toAdd")
	public String toAdd() {
		return "/media/photoItem/photoItem_form";
	}

	/**
	 * 修改photoItem页面
	 */
	@RequestMapping(value = "/toEdit")
	public String toEdit() {
		return "/media/photoItem/photoItem_form";
	}
	
	/**
	 * 新增photoItem
	 */
	@ResponseBody
	@RequestMapping(value = "/add")
	public Result add(MediaPhotoItem photoItem) {
		Result result = Result.success();
		try {
			photoItemService.addMediaPhotoItem(photoItem);
		} catch (ServiceException e) {
			LOGGER.warn(e.getMessage());
			result.error(e);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}
	
	/**
	 * 修改photoItem，批量修改
	 */
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Result edit(MediaPhotoItem photoItem) {
		Result result = Result.success();
		try {
			photoItemService.editMediaPhotoItem(photoItem);
		} catch (ServiceException e) {
			LOGGER.warn(e.getMessage());
			result.error(e);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	/**
	 * 删除photoItem
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Result delete(MediaPhotoItem photoItem) {
		Result result = Result.success();
		try {
			photoItemService.deleteMediaPhotoItem(photoItem);
		} catch (ServiceException e) {
			LOGGER.warn(e.getMessage());
			result.error(e);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}
	
	/**
	 * 查询photoItem详情data
	 */
	@ResponseBody
	@RequestMapping(value = "/data")
	public Result data(MediaPhotoItem photoItem) {
		Result result = Result.success();
		try {
			photoItem = photoItemService.loadMediaPhotoItem(photoItem);
			result.put("data", photoItem);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	/**
	 * 查询photoItem列表datas
	 */
	@ResponseBody
	@RequestMapping(value = "/datas")
	public Result datas() {
		Result result = Result.success();
		try {
			Page page = super.getPage();
			List<MediaPhotoItem> photoItems = photoItemService.findMediaPhotoItemPage(page);
			result.put("rows", photoItems).put("total", page.getTotalResult());
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}	
}
