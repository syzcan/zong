package com.zong.admin.system.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @desc sysUserRole实体类
 * @author zong
 * @date 2017年03月19日
 */
@JsonInclude(Include.NON_NULL)
public class SysUserRole implements Serializable {
	private String id;
	private String userId;//用户Id
	private String roleId;//角色id
	
	public SysUserRole(){
	}
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getUserId(){
		return userId;
	}
	
	public void setUserId(String userId){
		this.userId = userId;
	}
	
	public String getRoleId(){
		return roleId;
	}
	
	public void setRoleId(String roleId){
		this.roleId = roleId;
	}
	
}