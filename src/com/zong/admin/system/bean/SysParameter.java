package com.zong.admin.system.bean;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @desc sysParameter实体类
 * @author zong
 * @date 2017年03月19日
 */
@JsonInclude(Include.NON_NULL)
public class SysParameter implements Serializable {
	private String id;
	private String name;//参数名称
	private String paramKey;//英文键，唯一
	private String paramValue;//参数值
	private String remark;//备注
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date createTime;//创建时间
	
	public SysParameter(){
	}
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getParamKey(){
		return paramKey;
	}
	
	public void setParamKey(String paramKey){
		this.paramKey = paramKey;
	}
	
	public String getParamValue(){
		return paramValue;
	}
	
	public void setParamValue(String paramValue){
		this.paramValue = paramValue;
	}
	
	public String getRemark(){
		return remark;
	}
	
	public void setRemark(String remark){
		this.remark = remark;
	}
	
	public Date getCreateTime(){
		return createTime;
	}
	
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	
}