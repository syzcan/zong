package com.zong.admin.system.dao;

import org.apache.ibatis.annotations.Param;

import com.zong.admin.system.bean.SysUserRole;

public interface SysUserRoleMapper {
	/**
	 * 插入对象全部属性的字段
	 * 
	 * @param sysUserRole
	 */
	void insert(SysUserRole sysUserRole);

	void deleteByUser(@Param("userId") String userId);

	void deleteByRole(@Param("roleId") String roleId);
}