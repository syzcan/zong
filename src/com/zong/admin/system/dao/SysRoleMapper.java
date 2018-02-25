package com.zong.admin.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zong.admin.system.bean.SysRole;
import com.zong.core.bean.Page;
import com.zong.core.bean.PageData;
import com.zong.core.bean.Result;

public interface SysRoleMapper {

	/**
	 * 根据对象主键查询
	 * @param sysRole
	 */
	SysRole load(SysRole sysRole);
	
	/**
	 * 根据条件分页查询
	 * 
	 * @param page
	 * @return
	 */
	List<SysRole> findSysRolePage(Page page);
	
	/**
	 * 根据条件查询全部
	 * 
	 * @param pageData
	 * @return
	 */
	List<SysRole> findSysRole(PageData pageData);
	
	/**
	 * 根据对象主键删除
	 * @param sysRole
	 */
	void delete(SysRole sysRole);
	
	/**
	 * 插入对象全部属性的字段
	 * @param sysRole
	 */
	void insert(SysRole sysRole);
	
	/**
	 * 根据主键更新对象不为空属性的字段
	 * @param sysRole
	 */
	void update(SysRole sysRole);

	List<Result> findRoleBox(@Param("userId")String userId);
	
}