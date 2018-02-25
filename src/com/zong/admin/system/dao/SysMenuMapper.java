package com.zong.admin.system.dao;

import java.util.List;

import com.zong.admin.system.bean.SysMenu;
import com.zong.core.bean.PageData;

public interface SysMenuMapper {

	/**
	 * 根据对象主键查询
	 * @param sysMenu
	 */
	SysMenu load(SysMenu sysMenu);
	
	/**
	 * 根据条件查询全部
	 * 
	 * @param pageData
	 * @return
	 */
	List<SysMenu> findSysMenu(PageData pageData);
	
	/**
	 * 根据对象主键删除
	 * @param sysMenu
	 */
	void delete(SysMenu sysMenu);
	
	/**
	 * 插入对象全部属性的字段
	 * @param sysMenu
	 */
	void insert(SysMenu sysMenu);
	
	/**
	 * 根据主键更新对象不为空属性的字段
	 * @param sysMenu
	 */
	void update(SysMenu sysMenu);
	
}