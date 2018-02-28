package com.zong.core.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zong.admin.passport.service.PassportService;
import com.zong.admin.system.bean.SysUser;
import com.zong.admin.system.service.SysMenuService;
import com.zong.core.bean.PageData;
import com.zong.core.util.SpringContextUtil;
import com.zong.core.util.ZConst;

public class LoginFilter implements Filter {
	private static final Logger LOGGER = LoggerFactory.getLogger(TokenFilter.class);
	private PassportService passportService = SpringContextUtil.getBean(PassportService.class);
	private SysMenuService menuService = SpringContextUtil.getBean(SysMenuService.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String url = req.getRequestURI();
		// 静态资源不过滤
		for (String suffix : ZConst.STATIC_RESOURCES) {
			if (url.endsWith("." + suffix)) {
				chain.doFilter(request, response);
				return;
			}
		}
		if (req.getSession().getAttribute(ZConst.SESSION_USER) == null) {
			String authKey = "";
			Cookie[] cookies = req.getCookies();
			if (cookies != null && cookies.length > 0) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals("auth_key")) {
						authKey = cookie.getValue();
						break;
					}
				}
				if (!authKey.equals("")) {
					SysUser user = new SysUser();
					user.setAuthKey(authKey);
					user.setIp(getIp(req));
					user = passportService.loginByAuthKey(user);
					if (user != null) {
						// 登录成功，存到session
						req.getSession().setAttribute(ZConst.SESSION_USER, user);
						// 用户菜单权限
						req.getSession().setAttribute(ZConst.SESSION_MENUS,
								menuService.packageMenu(menuService.findSysMenu(new PageData("userId", user.getId()))));
					}
				}
			}
		}
		chain.doFilter(request, response);
	}

	public String getIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		LOGGER.info("LoginFilter过滤器初始化");
	}

	@Override
	public void destroy() {
		LOGGER.info("LoginFilter过滤器关闭");
	}

}
