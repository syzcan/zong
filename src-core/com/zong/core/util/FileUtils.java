package com.zong.core.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @desc 文件工具类
 * @author zong
 * @date 2016年11月26日 下午1:19:31
 */
public class FileUtils {
	public static Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

	/**
	 * 获取file文件夹下的后缀为type的所有文件
	 * 
	 * @param file
	 * @param type
	 * @return List<File>
	 */
	public static List<File> listFile(File file, String type) {
		List<File> files = new ArrayList<File>();
		for (File f : file.listFiles()) {
			if (f.isDirectory()) {
				files.addAll(listFile(f, type));
			} else {
				if (type == null || f.getName().indexOf("." + type) > -1) {
					files.add(f);
				}
			}
		}
		return files;
	}

	/**
	 * 获取rootPath目录下的后缀为type的所有文件
	 * 
	 * @param rootPath
	 * @param type
	 * @return List<File>
	 */
	public static List<File> listFile(String rootPath, String type) {
		return listFile(new File(rootPath), type);
	}

	/**
	 * 获取rootPath目录下所有文件
	 * 
	 * @param rootPath
	 * @return List<File>
	 */
	public static List<File> listFile(String rootPath) {
		return listFile(new File(rootPath), null);
	}

	/**
	 * 文件重新写入路径
	 * 
	 * @param sourcePath
	 * @param targetPath
	 */
	public static void repath(String sourcePath, String targetPath) {
		File source = new File(sourcePath);
		if (!source.exists()) {
			return;
		}
		// 删除目标路径
		File target = new File(targetPath);
		if (target.exists()) {
			target.delete();
		}
		source.renameTo(target);
	}

	/**
	 * file文件重命名为newName【带后缀】
	 * 
	 * @param file
	 * @param newName
	 */
	public static void rename(File file, String newName) {
		repath(file.getAbsolutePath(), file.getAbsolutePath().replace(file.getName(), newName));
	}

	/**
	 * 通过window命令行cmd进行文件复制
	 * 
	 * @param sourcePath
	 * @param targetPath
	 */
	public static void cmdCopy(String sourcePath, String targetPath) {
		File toFile = new File(targetPath);
		if (!toFile.getParentFile().exists()) {
			toFile.getParentFile().mkdirs();
		}
		String cmd = "cmd /c copy " + sourcePath + " " + targetPath;
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过window命令行cmd进行文件重命名
	 * 
	 * @param file
	 * @param newName
	 */
	public static void cmdRename(File file, String newName) {
		String sourcePath = file.getAbsolutePath();
		String targetPath = sourcePath.replace(file.getName(), newName);
		File toFile = new File(targetPath);
		toFile.delete();
		// 旧文件可以使用绝对路径，也可以使用相对路径，但是，新文件名不能使用任何路径，只能是新的文件名，即使这个路径就是当前目录
		String cmd = "cmd /c ren " + sourcePath + " " + newName;
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件夹(包括文件夹中的文件内容，文件夹)
	 * 
	 * @param strFolderPath
	 * @return
	 */
	public static boolean removeFolder(String strFolderPath) {
		boolean bFlag = false;
		try {
			if (strFolderPath == null || "".equals(strFolderPath)) {
				return bFlag;
			}
			File file = new File(strFolderPath.toString());
			bFlag = file.delete();
			if (bFlag == Boolean.TRUE) {
				LOGGER.debug("[REMOE_FOLDER:" + file.getPath() + "删除成功!]");
			} else {
				LOGGER.debug("[REMOE_FOLDER:" + file.getPath() + "删除失败]");
			}
		} catch (Exception e) {
			LOGGER.error("FLOADER_PATH:" + strFolderPath + "删除文件夹失败!");
			e.printStackTrace();
		}
		return bFlag;
	}

	/**
	 * 移除所有文件
	 * 
	 * @param strPath
	 */
	public static void removeAllFile(String strPath) {
		File file = new File(strPath);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] fileList = file.list();
		File tempFile = null;
		for (int i = 0; i < fileList.length; i++) {
			if (strPath.endsWith(File.separator)) {
				tempFile = new File(strPath + fileList[i]);
			} else {
				tempFile = new File(strPath + File.separator + fileList[i]);
			}
			if (tempFile.isFile()) {
				tempFile.delete();
			}
			if (tempFile.isDirectory()) {
				removeAllFile(strPath + "/" + fileList[i]);// 下删除文件夹里面的文件
				removeFolder(strPath + "/" + fileList[i]);// 删除文件夹
			}
		}
	}

	/**
	 * 读取文本文件
	 * 
	 * @param filePath
	 * @return 文本内容
	 */
	public static String readTxt(String filePath) {
		StringBuffer sb = new StringBuffer();
		try {
			LOGGER.info("读取文件：" + filePath);
			File file = new File(filePath);
			InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
			BufferedReader br = new BufferedReader(read);
			String s = null;
			while ((s = br.readLine()) != null) {
				sb.append(s + "\n");
			}
			br.close();
		} catch (Exception e) {
			LOGGER.error(e.toString(),e);
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 写入文本内容到文件
	 * 
	 * @param filePath
	 * @param content
	 */
	public static void writeTxt(String filePath, String content) {
		try {
			File file = new File(filePath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			BufferedWriter bw = new BufferedWriter(write);
			bw.write(content);
			bw.close();
		} catch (Exception e) {
			LOGGER.error(e.toString(),e);
			e.printStackTrace();
		}
	}

	/**
	 * 返回类根路径，后面带/
	 * 
	 * @return F:/work-eclipse-space/database-client/build/classes/
	 */
	public static String getClassResources() {
		URL url = FileUtils.class.getClassLoader().getResource("");
		if (File.separator.equals("/")) {
			return url.toString().replace("file:", "");
		}
		return url.toString().replace("file:/", "");
	}

	/**
	 * 返回项目路径
	 * 
	 * @return F:/work-eclipse-space/database-client/
	 */
	public static String getProjectPath() {
		return new StringBuffer(System.getProperty("user.dir").replaceAll("\\\\", "/")).append("/").toString();
	}

	public static void main(String[] args) {
		// 复制加后缀_
		List<File> list = listFile("E:/z/zapi", "java");
		for (File file : list) {
			cmdCopy(file.getAbsolutePath(), file.getAbsolutePath() + "_");
		}

		// 将后缀_文件还原
		// List<File> list = listFile("D:/zapi20170214", "java_");
		// for (File file : list) {
		// rename(file, file.getName().replaceAll("_", ""));
		// }
	}
}
