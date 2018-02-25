package com.zong.admin.passport.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zong.admin.passport.service.PassportService;
import com.zong.admin.system.bean.SysUser;
import com.zong.core.bean.Result;
import com.zong.core.controller.BaseController;
import com.zong.core.exception.ServiceException;
import com.zong.core.util.MD5Util;
import com.zong.core.util.ZConst;

@Controller
public class LoginController extends BaseController {

	private final static Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private PassportService passportService;

	/**
	 * 登录
	 */
	@RequestMapping(value = "/toLogin")
	public String toLogin() {
		// 已登录直接到首页
		if (getSession().getAttribute(ZConst.SESSION_USER) != null) {
			return "redirect:/";
		}
		return "/login";
	}

	@ResponseBody
	@RequestMapping(value = "/login")
	public Result login(SysUser user, String passcode, HttpServletResponse response) {
		Result result = Result.success();
		try {
			/*String code = (String) getSession().getAttribute(ZConst.SESSION_SECURITY_CODE);
			if (passcode == null) {
				throw new ServiceException("请输入验证码");
			}
			if (code == null || !code.toLowerCase().equals(passcode.toLowerCase())) {
				throw new ServiceException("验证码错误");
			}*/
			user.setIp(getIp(getRequest()));
			SysUser u = passportService.login(user);
			// 登录成功，存到session
			getSession().setAttribute(ZConst.SESSION_USER, u);
			// redisService.put(Const.SESSION_USER, user.getId(), user);

			Cookie cookie = new Cookie("auth_key", u.getAuthKey());
			cookie.setPath("/");
			// 如果记住登录，更新auth_key并返回写入cookie
			if ("1".equals(user.getRememberMe())) {
				cookie.setMaxAge(60 * 60 * 24 * 7);// 设置为7天
			} else {// 删除cookie
				cookie.setMaxAge(0);
			}
			response.addCookie(cookie);
			// 用户菜单权限
			//getSession().setAttribute(ZConst.SESSION_MENUS,
					//menuService.packageMenu(menuService.findSysMenu(new PageData("userId", u.getId()))));
		} catch (ServiceException e) {
			LOGGER.warn(e.getMessage());
			result.error(e);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	/**
	 * 修改密码
	 */
	@RequestMapping(value = "/password", method = RequestMethod.GET)
	public String password() {
		return "/password";
	}

	@ResponseBody
	@RequestMapping(value = "/savePassword", method = RequestMethod.POST)
	public Result password(SysUser user, HttpServletResponse response) {
		Result result = Result.success();
		try {
			SysUser sysUser = (SysUser) getSession().getAttribute(ZConst.SESSION_USER);
			if (!sysUser.getPassword().equals(MD5Util.MD5(user.getOldPassword()))) {
				throw new ServiceException("旧密码错误");
			}
			user.setId(sysUser.getId());
			user.setPassword(MD5Util.MD5(user.getPassword()));
			passportService.updatePassword(user);
			// 修改密码自动注销
			logout(response);
		} catch (ServiceException e) {
			LOGGER.warn(e.getMessage());
			result.error(e);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	/**
	 * 注销
	 */
	@RequestMapping(value = "/logout")
	public String logout(HttpServletResponse response) {
		SysUser user = (SysUser) getSession().getAttribute(ZConst.SESSION_USER);
		if (user != null) {
			passportService.updateAuthKey(user);
			getSession().removeAttribute(ZConst.SESSION_USER);
			// 删除cookie的auth_key
			Cookie cookie = new Cookie("auth_key", user.getAuthKey());
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		return "redirect:/toLogin";
	}

	/**
	 * 个人信息
	 */
	@RequestMapping(value = "/userInfo")
	public String userinfo(Model model) {
		SysUser user = (SysUser) getSession().getAttribute(ZConst.SESSION_USER);
		model.addAttribute("user", passportService.userinfo(user));
		return "/user_info";
	}
	
	@ResponseBody
	@RequestMapping(value = "/userInfo.json")
	public Result userinfoData(Model model) {
		Result result = Result.success();
		SysUser user = (SysUser) getSession().getAttribute(ZConst.SESSION_USER);
		result.put("data", passportService.userinfo(user));
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/saveInfo")
	public Result saveInfo(SysUser user) {
		Result result = Result.success();
		try {
			SysUser sysUser = (SysUser) getSession().getAttribute(ZConst.SESSION_USER);
			user.setId(sysUser.getId());
			passportService.saveInfo(user);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	/**
	 * 首页
	 */
	@RequestMapping(value = "/")
	public String index() {
		return "/index";
	}

	/**
	 * easyui首页
	 */
	@RequestMapping(value = "/main.html")
	public String main() {
		return "/main";
	}

	@RequestMapping(value = "/music")
	public String music() {
		return "/music";
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
}
