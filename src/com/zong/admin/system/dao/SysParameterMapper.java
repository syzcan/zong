package com.zong.admin.system.dao;

import java.util.List;

import com.zong.admin.system.bean.SysParameter;
import com.zong.core.bean.Page;

public interface SysParameterMapper {

	/**
	 * 根据对象主键查询
	 * @param sysParameter
	 */
	SysParameter load(SysParameter sysParameter);
	
	/**
	 * 根据条件分页查询
	 * 
	 * @param page
	 * @return
	 */
	List<SysParameter> findSysParameterPage(Page page);
	
	/**
	 * 根据对象主键删除
	 * @param sysParameter
	 */
	void delete(SysParameter sysParameter);
	
	/**
	 * 插入对象全部属性的字段
	 * @param sysParameter
	 */
	void insert(SysParameter sysParameter);
	
	/**
	 * 根据主键更新对象不为空属性的字段
	 * @param sysParameter
	 */
	void update(SysParameter sysParameter);
	
}