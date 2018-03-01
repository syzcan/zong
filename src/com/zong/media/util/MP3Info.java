package com.zong.media.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;

import com.zong.core.bean.Result;
import com.zong.core.exception.ServiceException;

/**
 * 获得MP3文件的信息
 * 
 */
public class MP3Info {

	public static void main(String[] args) {
		// TODO 演示
		File dir = new File("d:/KuGou");
		File[] musics = dir.listFiles();
		for (File music : musics) {
			if (!music.isFile() || music.getName().indexOf("mp3") == -1) {
				continue;
			}
			System.out.println("==================start：" + music.getName());
			try {
				MP3Info info = new MP3Info(music);
				info.setCharset("gbk");
				// System.out.println("标题：" + info.getSongName());
				// System.out.println("艺术家：" + info.getArtist());
				// System.out.println("专辑：" + info.getAlbum());
				System.out.println(new String(info.buf, "gbk"));
				// MP3File f = (MP3File) AudioFileIO.read(music);
				// MP3AudioHeader audioHeader = (MP3AudioHeader)
				// f.getAudioHeader();
				// System.out.println("长度：" + audioHeader.getTrackLength());
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("==================end：" + music.getName());
			System.out.println();
		}
	}

	/**
	 * 获取音乐信息map
	 * 
	 * @param music
	 * @return
	 */
	public static Result getMusicInfo(File music) {
		Result result = Result.success();
		try {
			MP3File f = (MP3File) AudioFileIO.read(music);
			MP3AudioHeader audioHeader = (MP3AudioHeader) f.getAudioHeader();
			result.put("length", audioHeader.getTrackLength());
			result.put("size", music.length());
			MP3Info info = new MP3Info(music);
			info.setCharset("gbk");
			result.put("title", info.getSongName());
			result.put("artist", info.getArtist());
			result.put("album", info.getAlbum());
		} catch (Exception e) {
			throw new ServiceException("mp3解析失败:" + music.getAbsolutePath());
		}
		return result;
	}

	private String charset = "utf-8";// 解析MP3信息时用的字符编码

	private byte[] buf;// MP3的标签信息的byte数组

	/**
	 * 实例化一个获得MP3文件的信息的类
	 * 
	 * @param mp3 MP3文件
	 * @throws IOException 读取MP3出错或则MP3文件不存在
	 */
	public MP3Info(File mp3) throws IOException {

		buf = new byte[128];// 初始化标签信息的byte数组

		RandomAccessFile raf = new RandomAccessFile(mp3, "r");// 随机读写方式打开MP3文件
		raf.seek(raf.length() - 128);// 移动到文件MP3末尾
		raf.read(buf);// 读取标签信息

		raf.close();// 关闭文件

		if (buf.length != 128) {// 数据是否合法
			throw new IOException("MP3标签信息数据长度不合法!");
		}

		if (!"TAG".equalsIgnoreCase(new String(buf, 0, 3))) {// 信息格式是否正确
			throw new IOException("MP3标签信息数据格式不正确!");
		}

	}

	/**
	 * 获得目前解析时用的字符编码
	 * 
	 * @return 目前解析时用的字符编码
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * 设置解析时用的字符编码
	 * 
	 * @param charset 解析时用的字符编码
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getSongName() {
		try {
			return new String(buf, 3, 30, charset).trim();
		} catch (UnsupportedEncodingException e) {
			return new String(buf, 3, 30).trim();
		}
	}

	public String getArtist() {
		try {
			return new String(buf, 33, 30, charset).trim();
		} catch (UnsupportedEncodingException e) {
			return new String(buf, 33, 30).trim();
		}
	}

	public String getAlbum() {
		try {
			return new String(buf, 63, 30, charset).trim();
		} catch (UnsupportedEncodingException e) {
			return new String(buf, 63, 30).trim();
		}
	}

	public String getYear() {
		try {
			return new String(buf, 93, 4, charset).trim();
		} catch (UnsupportedEncodingException e) {
			return new String(buf, 93, 4).trim();
		}
	}

	public String getComment() {
		try {
			return new String(buf, 97, 28, charset).trim();
		} catch (UnsupportedEncodingException e) {
			return new String(buf, 97, 28).trim();
		}
	}

}
