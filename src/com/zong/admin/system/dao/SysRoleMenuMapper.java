package com.zong.admin.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zong.admin.system.bean.SysRoleMenu;
import com.zong.core.bean.PageData;

public interface SysRoleMenuMapper {
	/**
	 * 根据条件查询全部
	 * 
	 * @param pageData
	 * @return
	 */
	List<SysRoleMenu> findSysRoleMenu(PageData pageData);

	/**
	 * 插入对象全部属性的字段
	 * 
	 * @param sysRoleMenu
	 */
	void insert(SysRoleMenu sysRoleMenu);

	void deleteByRole(@Param("roleId") String roleId);

	void deleteByMenu(@Param("menuId") String menuId);

}