package com.zong.admin.common.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zong.core.bean.Result;
import com.zong.core.exception.ServiceException;
import com.zong.core.util.FileUploadDownload;
import com.zong.core.util.MD5Util;
import com.zong.core.util.RandomCode;
import com.zong.core.util.VideoUtil;
import com.zong.core.util.ZConst;
import com.zong.media.util.MP3Info;

/**
 * 文件上传下载、验证码生成等通用类
 * 
 * @author zong
 * 
 */
@Controller
@RequestMapping(value = "/common")
public class CommonController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonController.class);

	/**
	 * uploadify上传附件
	 * 
	 * @param file
	 * @param uploadType 指定上传类型，上传到对应目录下
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public Result uploadFile(MultipartFile file, String uploadType, HttpServletRequest request) {
		Result result = this.upload(file, uploadType, request);
		result.put("fileName", file.getOriginalFilename());
		LOGGER.info("上传 {} 类型文件:{}", uploadType, file.getOriginalFilename());
		return result;
	}

	/**
	 * 
	 * @param file
	 * @param uploadType
	 * @return 返回附件访问路径
	 */
	private Result upload(MultipartFile file, String uploadType, HttpServletRequest request) {
		Result result = Result.success();
		String uploadPath = ZConst.UPLOAD_FILE_PATH;
		if ("picture".equals(uploadType)) {
			uploadPath = ZConst.UPLOAD_PICTURE_PATH;
		} else if ("music".equals(uploadType)) {
			uploadPath = ZConst.UPLOAD_MUSIC_PATH;
		} else if ("video".equals(uploadType)) {
			uploadPath = ZConst.UPLOAD_VIDEO_PATH;
		} else if ("document".equals(uploadType)) {
			uploadPath = ZConst.UPLOAD_DOCUMENT_PATH;
		}
		// 文件目录按时间归类文件夹
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String dateDir = dateFormat.format(new Date());
		String path = uploadPath + "/" + dateDir;
		int randomNum = new Random().nextInt(10000);
		String random = "";
		String extName = "";
		if (file.getOriginalFilename().lastIndexOf(".") >= 0) {
			extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		}
		if (randomNum < 10) {
			random = "000" + randomNum;
		} else if (randomNum < 100) {
			random = "00" + randomNum;
		} else if (randomNum < 1000) {
			random = "0" + randomNum;
		} else {
			random = "" + randomNum;
		}
		path += "/" + System.currentTimeMillis() + "_" + random + extName;
		String savePath = request.getSession().getServletContext().getRealPath("/" + path);
		File f = new File(savePath);
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}
		try {
			file.transferTo(f);
			result.put("filePath", path);
			// 视频文件生成封面缩略图
			if ("video".equals(uploadType)) {
				String cover = path.substring(0, path.lastIndexOf(".")) + ".jpg";
				String ffmpegPath = request.getServletContext().getRealPath("/plugins/ffmpeg.exe");
				if (!new File(ffmpegPath).exists()) {
					throw new ServiceException("ffmpeg.exe不存在:" + ffmpegPath);
				}
				result = VideoUtil.thumbnail(ffmpegPath, savePath,
						savePath.substring(0, savePath.lastIndexOf(".")) + ".jpg", "1");
				result.put("cover", cover);
				result.put("md5", MD5Util.fileMD5(savePath));
			} else if ("music".equals(uploadType)) {
				result = MP3Info.getMusicInfo(f);
			}
			result.put("filePath", path);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	/**
	 * 下载附件
	 * 
	 * @param file
	 */
	@RequestMapping(value = "/downloadFile")
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) {
		try {
			String filePath = "static/js/pintuer.js";
			filePath = request.getSession().getServletContext().getRealPath(filePath);
			String fileName = "拼图文件pintuer.js";
			FileUploadDownload.fileDownload(response, filePath, fileName);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
		}
	}

	/**
	 * 生成图形验证码
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/code")
	public void code(HttpServletRequest request, HttpServletResponse response) {
		RandomCode.getRandcode(request, response);
	}

}
