package com.zong.admin.system.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @desc sysRoleMenu实体类
 * @author zong
 * @date 2017年03月19日
 */
@JsonInclude(Include.NON_NULL)
public class SysRoleMenu implements Serializable {
	private String id;
	private String roleId;//角色id
	private String menuId;//菜单Id
	
	public SysRoleMenu(){
	}
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getRoleId(){
		return roleId;
	}
	
	public void setRoleId(String roleId){
		this.roleId = roleId;
	}
	
	public String getMenuId(){
		return menuId;
	}
	
	public void setMenuId(String menuId){
		this.menuId = menuId;
	}
	
}