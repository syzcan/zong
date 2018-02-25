package com.zong.media.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zong.media.bean.MediaPhoto;
import com.zong.media.service.MediaPhotoService;
import com.zong.core.bean.Page;
import com.zong.core.bean.Result;
import com.zong.core.controller.BaseController;
import com.zong.core.exception.ServiceException;

/**
 * @desc photo控制层 相册表
 * @author zong
 * @date 2018年02月19日
 */
@Controller
@RequestMapping(value = "/media/photo")
public class MediaPhotoController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(MediaPhotoController.class);

	@Autowired
	private MediaPhotoService photoService;

	/**
	 * 查询photo列表
	 */
	@RequestMapping(value = "/list")
	public String list() {
		return "/media/photo/photo_list";
	}
	
	/**
	 * 新增photo页面
	 */
	@RequestMapping(value = "/toAdd")
	public String toAdd() {
		return "/media/photo/photo_form";
	}

	/**
	 * 修改photo页面
	 */
	@RequestMapping(value = "/toEdit")
	public String toEdit() {
		return "/media/photo/photo_form";
	}
	
	/**
	 * 新增photo
	 */
	@ResponseBody
	@RequestMapping(value = "/add")
	public Result add(MediaPhoto photo) {
		Result result = Result.success();
		try {
			photoService.addMediaPhoto(photo);
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
	 * 修改photo
	 */
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Result edit(MediaPhoto photo) {
		Result result = Result.success();
		try {
			photoService.editMediaPhoto(photo);
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
	 * 删除photo
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Result delete(MediaPhoto photo) {
		Result result = Result.success();
		try {
			photoService.deleteMediaPhoto(photo);
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
	 * 查询photo详情data
	 */
	@ResponseBody
	@RequestMapping(value = "/data")
	public Result data(MediaPhoto photo) {
		Result result = Result.success();
		try {
			photo = photoService.loadMediaPhoto(photo);
			result.put("data", photo);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	/**
	 * 查询photo列表datas
	 */
	@ResponseBody
	@RequestMapping(value = "/datas")
	public Result datas() {
		Result result = Result.success();
		try {
			Page page = super.getPage();
			List<MediaPhoto> photos = photoService.findMediaPhotoPage(page);
			result.put("rows", photos).put("total", page.getTotalResult());
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}	
}
