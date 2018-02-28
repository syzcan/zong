package com.zong.core.lisenter;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.zong.core.bean.PageData;
import com.zong.core.bean.Result;
import com.zong.core.dao.CommonMapper;
import com.zong.core.util.ZConst;

/**
 * @desc tomcat启动时执行初始化数据
 * @author zong
 * @date 2016年11月27日 下午10:35:33
 */
public class ConfigListener implements ServletContextListener {

	public static final Logger LOGGER = LoggerFactory.getLogger(ConfigListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		LOGGER.info("======ConfigListener-销毁=====");
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		LOGGER.info("======ConfigListener-初始化=====");
		ServletContext application = event.getServletContext();
		ZConst.WEB_APP_CONTEXT = WebApplicationContextUtils.getWebApplicationContext(application);
		// 加载系统参数到缓存
		//SysMenuService menuService = ZConst.WEB_APP_CONTEXT.getBean(SysMenuService.class);
		//application.setAttribute("contextMenus", menuService.packageMenu(menuService.findSysMenu(new PageData())));
		PageData zparam = new PageData();
		CommonMapper commonMapper = ZConst.WEB_APP_CONTEXT.getBean(CommonMapper.class);
		List<Result> datas = commonMapper.find("sys_parameter",new PageData());
		for (Result data : datas) {
			zparam.put(data.getString("param_key"), data);
		}
		//缓存到ServletContext
		application.setAttribute(ZConst.ZPARAM, zparam);
		/**
		 * 缓存到ehcache或redis
		 */
		
	}

}
