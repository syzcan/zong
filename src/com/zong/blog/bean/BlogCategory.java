package com.zong.blog.bean;


import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @desc category实体类
 * @author zong
 * @date 2018年02月18日
 */
@JsonInclude(Include.NON_NULL)
public class BlogCategory implements Serializable {
	private String id;
	private String name;
	private String description;
	
	public BlogCategory(){
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
	
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
}