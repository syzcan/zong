package com.zong.core.util;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * spring 容器对象，获取注入bean 对象获取
 * 
 * @author zong
 * 
 */
public class SpringContextUtil implements ApplicationContextAware {
	/**
	 * spring 容器
	 */
	private static ApplicationContext springContext;
	/**
	 * servlet容器
	 */
	private static ServletContext servletContext;
	private static String contextName;
	private static String realPath;
	private static String serverInfo;

	public static void setServletContext(ServletContext servletContext) {
		SpringContextUtil.servletContext = servletContext;
	}

	/**
	 * 实现ApplicationContextAware在applicationContext.
	 * xml及包含的配置文件加载完成后注入ApplicationContext
	 * 此做法能更好的支持非WEB环境，如：单元测试时getSpringContext()避免new
	 * ClassPathXmlApplicationContext("spring/applicationContext.xml")重复加载
	 * 先于setWebApplicationContext被调用
	 * 
	 * @param springContext ApplicationContext
	 */
	@Override
	public void setApplicationContext(ApplicationContext springContext) {
		SpringContextUtil.springContext = springContext;
	}

	/**
	 * 在ContextLoaderListener注入WebApplicationContext 在setApplicationContext后执行
	 * 
	 * @param springContext ApplicationContext
	 */
	public static void setWebApplicationContext(ApplicationContext springContext) {
		if (SpringContextUtil.springContext != springContext) {
			// 理论上与setApplicationContext方法注入的是同一个对象，这里只是为了容错及重复赋值而这样判断
			SpringContextUtil.springContext = springContext;
		}
	}

	public static ApplicationContext getSpringContext() {
		if (springContext == null) {
			if (servletContext != null) {
				springContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			} else {
				springContext = ContextLoader.getCurrentWebApplicationContext();
			}
			if (springContext == null) {
				springContext = new ClassPathXmlApplicationContext("applicationContext.xml");
			}
		}
		return springContext;
	}

	/**
	 * 获取spring注入的bean 对象
	 * 
	 * @param beanName bean名称
	 * @return 实例化对象
	 */
	public static Object getBean(String beanName) {
		return getSpringContext().getBean(beanName);
	}

	/**
	 * 获取spring注入的bean 对象
	 * 
	 * @param 类对象
	 * @return 实例化对象
	 */
	public static <T> T getBean(Class<T> paramClass) {
		return getSpringContext().getBean(paramClass);
	}

	public static String getContextName() {
		return contextName;
	}

	public static void setContextName(String contextName) {
		SpringContextUtil.contextName = contextName;
	}

	public static String getRealPath() {
		return realPath;
	}

	public static void setRealPath(String realPath) {
		SpringContextUtil.realPath = realPath;
	}

	public static String getServerInfo() {
		return serverInfo;
	}

	public static void setServerInfo(String serverInfo) {
		SpringContextUtil.serverInfo = serverInfo;
	}
}
