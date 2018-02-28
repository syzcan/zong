package com.zong.core.util;

import org.springframework.context.ApplicationContext;

/**
 * 项目常量
 * 
 * @author zong
 * 
 */
public class ZConst {
	public static final String ZPARAM = "zparam";// 系统application共享参数
	public static final String SESSION_SECURITY_CODE = "sessionSecCode";// 验证码
	public static final String SESSION_USER = "sessionUser"; // session中的用户
	public static final String SESSION_MENUS = "sessionMenus"; // 当前用户权限
	public static final String UPLOAD_ROOT_PATH = "upload";// 上传根路径
	public static final String UPLOAD_PICTURE_PATH = "upload/picture";// 图片上传路径
	public static final String UPLOAD_MUSIC_PATH = "upload/music";// 音乐上传路径
	public static final String UPLOAD_VIDEO_PATH = "upload/video";// 视频上传路径
	public static final String UPLOAD_FILE_PATH = "upload/file";// 一般文件上传路径
	public static final String UPLOAD_DOCUMENT_PATH = "upload/document";// 文档上传路径
	public static final String FFMPEG_PATH = "d:/ffmpeg.exe";// ffmpeg组件路径
	public static final String LOGIN = "/toLogin"; // 登录地址
	public static final String NO_INTERCEPTOR_PATH = ".*/((toLogin)|(login)|(logout)|(code)|(authorize)|(authCallback)|(weixin)|(static)|(upload)|(websocket)|(spring)|(craw)).*"; // 不对匹配该值的访问路径拦截（正则）
	public static final String[] STATIC_RESOURCES = { "jpg", "jpeg", "gif", "png", "bmp", "js", "html","css","ico" };// 静态资源类型
	public static ApplicationContext WEB_APP_CONTEXT = null; // 该值会在web容器启动时由WebAppContextListener初始化

	// =======api接口相关常量start
	/**
	 * app登录授权token参数
	 */
	public static final String APP_TOKEN = "token";
	/**
	 * app授权sign签名参数
	 */
	public static final String APP_SIGN = "sign";
	/**
	 * redis管理token-id关系模块
	 */
	public static final String MODULE_APP_ID_TOKEN = "module_app_id_token";
}
