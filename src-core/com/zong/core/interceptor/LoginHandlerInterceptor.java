package com.zong.core.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.zong.admin.system.bean.SysMenu;
import com.zong.admin.system.bean.SysUser;
import com.zong.core.util.ZConst;

/**
 * @desc 类名称：登录过滤，权限验证
 * @author zong
 * @date 2016-6-8 上午11:10:15
 */
public class LoginHandlerInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String path = request.getServletPath();
		// 不拦截登录、注销等请求
		if (path.matches(ZConst.NO_INTERCEPTOR_PATH)) {
			return true;
		} else {
			SysUser user = (SysUser) request.getSession().getAttribute(ZConst.SESSION_USER);
			if (user != null) {
				/*List<SysMenu> menus = (List<SysMenu>) request.getSession().getAttribute(ZConst.SESSION_MENUS);
				path = path.substring(1, path.length());
				boolean flag = hasPower(menus, path);
				if (!flag) {
					response.sendRedirect(request.getContextPath() + ZConst.LOGIN);
				}
				return flag;*/
				return true;
			} else {
				// 登陆过滤
				response.sendRedirect(request.getContextPath() + ZConst.LOGIN);
				return false;
			}
		}
	}

	// 访问权限校验，需要具体配置到每一个请求链接
	private boolean hasPower(List<SysMenu> menus, String path) {
		for (SysMenu sysMenu : menus) {
			if (sysMenu.getUrl().indexOf(path) > -1) {
				return true;
			}
		}
		return false;
	}

}
