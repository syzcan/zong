package com.zong.media.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zong.media.bean.MediaMusic;
import com.zong.media.service.MediaMusicService;
import com.zong.core.bean.Page;
import com.zong.core.bean.Result;
import com.zong.core.controller.BaseController;
import com.zong.core.exception.ServiceException;

/**
 * @desc music控制层 音乐数据表
 * @author zong
 * @date 2018年02月19日
 */
@Controller
@RequestMapping(value = "/media/music")
public class MediaMusicController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(MediaMusicController.class);

	@Autowired
	private MediaMusicService musicService;

	/**
	 * 查询music列表
	 */
	@RequestMapping(value = "/list")
	public String list() {
		return "/media/music/music_list";
	}
	
	/**
	 * 新增music页面
	 */
	@RequestMapping(value = "/toAdd")
	public String toAdd() {
		return "/media/music/music_form";
	}

	/**
	 * 修改music页面
	 */
	@RequestMapping(value = "/toEdit")
	public String toEdit() {
		return "/media/music/music_form";
	}
	
	/**
	 * 新增music
	 */
	@ResponseBody
	@RequestMapping(value = "/add")
	public Result add(MediaMusic music) {
		Result result = Result.success();
		try {
			musicService.addMediaMusic(music);
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
	 * 修改music
	 */
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Result edit(MediaMusic music) {
		Result result = Result.success();
		try {
			musicService.editMediaMusic(music);
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
	 * 删除music
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Result delete(MediaMusic music) {
		Result result = Result.success();
		try {
			musicService.deleteMediaMusic(music);
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
	 * 查询music详情data
	 */
	@ResponseBody
	@RequestMapping(value = "/data")
	public Result data(MediaMusic music) {
		Result result = Result.success();
		try {
			music = musicService.loadMediaMusic(music);
			result.put("data", music);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	/**
	 * 查询music列表datas
	 */
	@ResponseBody
	@RequestMapping(value = "/datas")
	public Result datas() {
		Result result = Result.success();
		try {
			Page page = super.getPage();
			List<MediaMusic> musics = musicService.findMediaMusicPage(page);
			result.put("rows", musics).put("total", page.getTotalResult());
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}	
}
