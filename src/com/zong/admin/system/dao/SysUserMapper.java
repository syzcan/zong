package com.zong.admin.system.dao;

import java.util.List;

import com.zong.admin.system.bean.SysUser;
import com.zong.core.bean.Page;
import com.zong.core.bean.PageData;

public interface SysUserMapper {

	/**
	 * 根据对象主键查询
	 * @param sysUser
	 */
	SysUser load(SysUser sysUser);
	
	/**
	 * 根据条件分页查询
	 * 
	 * @param page
	 * @return
	 */
	List<SysUser> findSysUserPage(Page page);
	
	/**
	 * 根据条件查询全部
	 * 
	 * @param pageData
	 * @return
	 */
	List<SysUser> findSysUser(PageData pageData);
	
	/**
	 * 根据对象主键删除
	 * @param sysUser
	 */
	void delete(SysUser sysUser);
	
	/**
	 * 插入对象全部属性的字段
	 * @param sysUser
	 */
	void insert(SysUser sysUser);
	
	/**
	 * 根据主键更新对象不为空属性的字段
	 * @param sysUser
	 */
	void update(SysUser sysUser);
	
}