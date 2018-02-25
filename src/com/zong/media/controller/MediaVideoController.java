package com.zong.media.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zong.core.bean.Page;
import com.zong.core.bean.Result;
import com.zong.core.controller.BaseController;
import com.zong.core.exception.ServiceException;
import com.zong.core.util.VideoUtil;
import com.zong.media.bean.MediaVideo;
import com.zong.media.service.MediaVideoService;

/**
 * @desc video控制层 视频表
 * @author zong
 * @date 2018年02月19日
 */
@Controller
@RequestMapping(value = "/media/video")
public class MediaVideoController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(MediaVideoController.class);

	@Autowired
	private MediaVideoService videoService;

	/**
	 * 查询video列表
	 */
	@RequestMapping(value = "/list")
	public String list() {
		return "/media/video/video_list";
	}
	
	/**
	 * 新增video页面
	 */
	@RequestMapping(value = "/toAdd")
	public String toAdd() {
		return "/media/video/video_form";
	}

	/**
	 * 修改video页面
	 */
	@RequestMapping(value = "/toEdit")
	public String toEdit() {
		return "/media/video/video_form";
	}
	
	/**
	 * 新增video
	 */
	@ResponseBody
	@RequestMapping(value = "/add")
	public Result add(MediaVideo video) {
		Result result = Result.success();
		try {
			videoService.addMediaVideo(video);
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
	 * 修改video
	 */
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Result edit(MediaVideo video) {
		Result result = Result.success();
		try {
			videoService.editMediaVideo(video);
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
	 * 删除video
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Result delete(MediaVideo video) {
		Result result = Result.success();
		try {
			videoService.deleteMediaVideo(video);
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
	 * 查询video详情data
	 */
	@ResponseBody
	@RequestMapping(value = "/data")
	public Result data(MediaVideo video) {
		Result result = Result.success();
		try {
			video = videoService.loadMediaVideo(video);
			result.put("data", video);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	/**
	 * 查询video列表datas
	 */
	@ResponseBody
	@RequestMapping(value = "/datas")
	public Result datas() {
		Result result = Result.success();
		try {
			Page page = super.getPage();
			List<MediaVideo> videos = videoService.findMediaVideoPage(page);
			result.put("rows", videos).put("total", page.getTotalResult());
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}	
	
	/**
	 * video查看
	 */
	@RequestMapping(value = "/show")
	public String show(MediaVideo video, Model model) {
		model.addAttribute("video", videoService.loadMediaVideo(video));
		return "/media/video/video_show";
	}

	/**
	 * 修改封面
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/setCover")
	public Result setCover(MediaVideo video, String currentTime, HttpServletRequest request) {
		Result result = Result.success();
		try {
			video = videoService.loadMediaVideo(video);
			String videoPath = getRequest().getServletContext().getRealPath(video.getUrl());
			String savePath = getRequest().getServletContext().getRealPath(video.getCover());
			VideoUtil.thumbnail(request.getServletContext().getRealPath("/plugins/ffmpeg.exe"), videoPath, savePath,
					currentTime);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}
}
