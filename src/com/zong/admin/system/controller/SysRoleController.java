package com.zong.admin.system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zong.admin.system.bean.SysRole;
import com.zong.admin.system.service.SysRoleService;
import com.zong.core.bean.PageData;
import com.zong.core.bean.Result;
import com.zong.core.controller.BaseController;

/**
 * @desc sysRole控制层 系统角色表
 * @author zong
 * @date 2017年03月19日
 */
@Controller
@RequestMapping(value = "/system/role")
public class SysRoleController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(SysRoleController.class);

	@Autowired
	private SysRoleService sysRoleService;

	/**
	 * 查询sysRole列表
	 */
	@RequestMapping(value = "/list")
	public String list(Model model) {
		return "/system/sysRole_list";
	}
	
	/**
	 * 新增sysRole页面
	 */
	@RequestMapping(value = "/toAdd")
	public String toAdd(Model model) {
		return "/system/sysRole_form";
	}

	/**
	 * 修改sysRole页面
	 */
	@RequestMapping(value = "/toEdit")
	public String toEdit(SysRole sysRole) {
		return "/system/sysRole_form";
	}
	
	/**
	 * 新增sysRole
	 */
	@ResponseBody
	@RequestMapping(value = "/add")
	public Result add(SysRole sysRole) {
		Result result = Result.success();
		try {
			sysRoleService.addSysRole(sysRole);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	/**
	 * 修改sysRole
	 */
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Result edit(SysRole sysRole) {
		Result result = Result.success();
		try {
			sysRoleService.editSysRole(sysRole);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}
	
	/**
	 * 修改sysRole权限
	 */
	@ResponseBody
	@RequestMapping(value = "/saveRoleMenu")
	public Result saveRoleMenu(SysRole sysRole) {
		Result result = Result.success();
		try {
			sysRoleService.saveRoleMenu(sysRole);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}
	
	/**
	 * 查询sysRole详情data
	 */
	@ResponseBody
	@RequestMapping(value = "/data")
	public Result data(SysRole sysRole) {
		Result result = Result.success();
		SysRole data = sysRoleService.loadSysRole(sysRole);
		result.put("data", data);
		return result;
	}

	/**
	 * 删除sysRole
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Result delete(SysRole sysRole) {
		Result result = Result.success();
		try {
			sysRoleService.deleteSysRole(sysRole);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}
	
	/**
	 * 查询sysRole列表datas
	 */
	@ResponseBody
	@RequestMapping(value = "/datas")
	public Result datas() {
		Result result = Result.success();
		result.put("rows", sysRoleService.findSysRole(new PageData()));
		return result;
	}	
}
