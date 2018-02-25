package com.zong.admin.passport.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zong.admin.system.bean.SysUser;
import com.zong.admin.system.dao.SysUserMapper;
import com.zong.core.bean.PageData;
import com.zong.core.exception.ServiceException;
import com.zong.core.util.MD5Util;
import com.zong.core.util.RandomCode;

/**
 * @desc 用户登录和注册实现类
 * @author zong
 * @date 2016年04月23日
 */
@Service
public class PassportService {
	@Autowired
	private SysUserMapper userMapper;

	/**
	 * 用户登录
	 * 
	 * @param user
	 */
	public SysUser login(SysUser user) throws Exception {
		if (user.getUsername() == null || user.getUsername().equals("")) {
			throw new ServiceException("请输入用户名");
		}
		if (user.getPassword() == null || user.getPassword().equals("")) {
			throw new ServiceException("请输入密码");
		}
		List<SysUser> list = userMapper.findSysUser(new PageData("username", user.getUsername()));
		if (list.isEmpty()) {
			throw new ServiceException("该用户不存在");
		}
		SysUser u = list.get(0);
		if (!MD5Util.MD5(user.getPassword()).equals(u.getPassword())) {
			throw new ServiceException("密码错误");
		}
		// 如果记住登录，更新auth_key并返回写入cookie
		if ("1".equals(user.getRememberMe())) {
			u.setAuthKey(UUID.randomUUID().toString());
		}
		// 登录成功，记录日志
		SysUser logUser = new SysUser();
		logUser.setId(u.getId());
		logUser.setLastLogin(new Date());
		logUser.setIp(user.getIp());
		logUser.setAuthKey(u.getAuthKey());
		userMapper.update(logUser);
		return u;
	}

	public SysUser loginByAuthKey(SysUser user) {
		List<SysUser> list = userMapper.findSysUser(new PageData("authKey", user.getAuthKey()));
		if (!list.isEmpty()) {
			SysUser u = list.get(0);
			// 登录成功，记录日志
			SysUser logUser = new SysUser();
			logUser.setId(u.getId());
			logUser.setLastLogin(new Date());
			logUser.setIp(user.getIp());
			userMapper.update(logUser);
			return u;
		}
		return null;
	}

	/**
	 * 生成用户名
	 * 
	 * @return
	 */
	public String makeUsername() {
		String username = RandomCode.makeUserName();
		while (true) {
			if (userMapper.findSysUser(new PageData("username", username)).isEmpty()) {
				break;
			}
			username = RandomCode.makeUserName();
		}
		return username;
	}

	public void updateAuthKey(SysUser user) {
		user = userMapper.load(user);
		if (user != null) {
			user.setAuthKey(UUID.randomUUID().toString());
			userMapper.update(user);
		}
	}

	public void updatePassword(SysUser sysUser) {
		userMapper.update(sysUser);
	}

	public SysUser userinfo(SysUser user) {
		return userMapper.load(user);
	}
	
	public void saveInfo(SysUser user) {
		user.setUpdateTime(new Date());
		userMapper.update(user);
	}
}
