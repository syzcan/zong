package com.zong.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zong.core.bean.Result;

/**
 * 视频转码、剪裁和缩略图生成工具
 * 
 * @author zong
 * 
 */
public class VideoUtil {

	public static Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

	public static Result thumbnail(String ffmpegPath, String videoPath, String savePath, String currentTime) {
		// 创建一个List集合来保存从视频中截取图片的命令
		List<String> commands = new ArrayList<String>();
		commands.add(ffmpegPath);
		commands.add("-ss"); // 添加参数＂-ss＂，该参数指定截取的起始时间
		commands.add(currentTime == null ? "15" : currentTime); // 添加起始时间为第17秒
		commands.add("-i");
		commands.add(videoPath); // 视频路径
		commands.add("-y");
		commands.add("-f");
		commands.add("image2");
		// commands.add("-t"); // 添加参数＂-t＂，该参数指定持续时间
		// commands.add("0.001"); // 添加持续时间为1毫秒
		commands.add("-s"); // 添加参数＂-s＂，该参数指定截取的图片大小
		// 按原分辨率生成截图
		Result data = getVideoInfo(ffmpegPath, videoPath);
		int width = Integer.parseInt(data.getString("width"));
		int height = Integer.parseInt(data.getString("height"));
		if (width > height && width > 400) {
			height = height * 400 / width;
			width = 400;// 最宽为400
		}
		if (height > width && height > 400) {
			width = width * 400 / height;
			height = 400;
		}
		commands.add(width + "*" + height); // 添加截取的图片大小为350*240
		commands.add(savePath); // 添加截取的图片的保存路径

		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commands);
			// builder.redirectErrorStream(true);
			// 如果此属性为 true，则任何由通过此对象的 start() 方法启动的后续子进程生成的错误输出都将与标准输出合并，
			// 因此两者均可使用 Process.getInputStream() 方法读取。这使得关联错误消息和相应的输出变得更容易
			builder.start();
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
		}
		return data;
	}

	/**
	 * 获取视频信息
	 * 
	 * @param ffmpegPath 视频路径
	 * @param videoPath ffmpeg路径
	 * @return
	 */
	public static Result getVideoInfo(String ffmpegPath, String videoPath) {
		Result data = Result.success();
		List<String> commands = new java.util.ArrayList<String>();
		commands.add(ffmpegPath);
		commands.add("-i");
		commands.add(videoPath);
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commands);
			Process process = builder.start();
			// 从输入流中读取视频信息
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			StringBuffer sb = new StringBuffer();
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();

			String info = sb.toString();
			// 从视频信息中解析时长
			String regexDuration = "Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";
			Pattern pattern = Pattern.compile(regexDuration);
			Matcher m = pattern.matcher(info);
			if (m.find()) {
				int time = getTimelen(m.group(1));
				int index = info.indexOf("Stream #0:0");
				if (index < 0) {
					index = info.indexOf("Stream #0.0");
				}
				String widthHeight = info.substring(index).split(",")[2].split("\\[")[0].replace(" ", "");
				data.put("length", time);
				data.put("size", new File(videoPath).length());
				data.put("width", widthHeight.split("x")[0]);
				data.put("height", widthHeight.split("x")[1]);
				LOGGER.info(videoPath + ",视频时长：" + time + ", 开始时间：" + m.group(2) + ",比特率：" + m.group(3) + "kb/s,分辨率："
						+ widthHeight);
			}
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
		}
		return data;
	}

	// 格式:"00:00:10.68"
	private static int getTimelen(String timelen) {
		int min = 0;
		String strs[] = timelen.split(":");
		if (strs[0].compareTo("0") > 0) {
			min += Integer.valueOf(strs[0]) * 60 * 60;// 秒
		}
		if (strs[1].compareTo("0") > 0) {
			min += Integer.valueOf(strs[1]) * 60;
		}
		if (strs[2].compareTo("0") > 0) {
			min += Math.round(Float.valueOf(strs[2]));
		}
		return min;
	}

	public static void main(String[] args) {
		String videoPath = "d:/VR/精选/微露脸巨乳妹子又大又挺道具自慰秀逼逼粉嫩水很多不要错过.mp4";
		Result data = VideoUtil.getVideoInfo("e:/ffmpeg.exe", videoPath);
		int length = (int) data.get("length");
		int count = 1;
		for (int i = length / 60; i < length; i = i + (length / 30)) {
			if (i == 0) {
				i = 1;
			}
			thumbnail("e:/ffmpeg.exe", videoPath, "e:/" + (count++) + ".jpg", i + "");
		}
	}
}
