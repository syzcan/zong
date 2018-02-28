package com.zong.core.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传下载工具
 */
public class FileUploadDownload {

	/**
	 * @param file 文件对象
	 * @param filePath 上传物理路径
	 * @return 文件名
	 */
	public static String fileUp(MultipartFile file, String filePath) {
		Random random = new Random();
		String fileName = random.nextInt(10000) + System.currentTimeMillis() + "";
		try {
			// 扩展名格式
			if (file.getOriginalFilename().lastIndexOf(".") >= 0) {
				fileName += file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			}
			copyFile(file.getInputStream(), filePath, fileName);
		} catch (IOException e) {
			System.out.println(e);
		}
		return fileName;
	}

	/**
	 * 写文件到当前目录的upload目录中
	 * 
	 * @param in
	 * @param fileName
	 * @throws IOException
	 */
	private static String copyFile(InputStream in, String dir, String realName) throws IOException {
		File file = new File(dir, realName);
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
		}
		FileUtils.copyInputStreamToFile(in, file);
		return realName;
	}

	// ============下载文件===========start
	/**
	 * @param response
	 * @param filePath 文件完整路径(包括文件名和扩展名)
	 * @param fileName 下载后看到的文件名
	 * @return 文件名
	 */
	public static void fileDownload(final HttpServletResponse response, String filePath, String fileName)
			throws Exception {
		byte[] data = FileUtils.readFileToByteArray(new File(filePath));
		response.reset();
		// utf8防止中文乱码
		response.setHeader("Content-Disposition",
				"attachment; filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
		response.addHeader("Content-Length", "" + data.length);
		response.setContentType("application/octet-stream;charset=UTF-8");
		OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
		outputStream.write(data);
		outputStream.flush();
		outputStream.close();
		response.flushBuffer();
	}
}
