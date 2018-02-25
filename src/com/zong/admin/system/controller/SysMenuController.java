package com.zong.admin.system.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zong.admin.system.bean.SysMenu;
import com.zong.admin.system.bean.SysUser;
import com.zong.admin.system.service.SysMenuService;
import com.zong.core.bean.PageData;
import com.zong.core.bean.Result;
import com.zong.core.controller.BaseController;
import com.zong.core.exception.ServiceException;
import com.zong.core.util.ZConst;

/**
 * @desc sysMenu控制层 系统菜单表
 * @author zong
 * @date 2017年03月19日
 */
@Controller
@RequestMapping(value = "/system/menu")
public class SysMenuController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(SysMenuController.class);

	@Autowired
	private SysMenuService sysMenuService;

	/**
	 * 查询sysMenu列表
	 */
	@RequestMapping(value = "/list")
	public String list(Model model) {
		return "/system/sysMenu_list";
	}

	/**
	 * 新增sysMenu页面
	 */
	@RequestMapping(value = "/toAdd")
	public String toAdd(Model model) {
		return "/system/sysMenu_form";
	}

	/**
	 * 新增sysMenu
	 */
	@ResponseBody
	@RequestMapping(value = "/add")
	public Result add(SysMenu sysMenu) {
		Result result = Result.success();
		try {
			sysMenuService.addSysMenu(sysMenu);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	/**
	 * 修改sysMenu页面
	 */
	@RequestMapping(value = "/toEdit")
	public String toEdit(SysMenu sysMenu) {
		return "/system/sysMenu_form";
	}

	/**
	 * 修改sysMenu
	 */
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Result edit(SysMenu sysMenu) {
		Result result = Result.success();
		try {
			sysMenuService.editSysMenu(sysMenu);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	/**
	 * 删除sysMenu
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Result delete(SysMenu sysMenu) {
		Result result = Result.success();
		try {
			sysMenuService.deleteSysMenu(sysMenu);
		} catch (ServiceException e) {
			LOGGER.warn(e.getMessage());
			result.error(e);
		}catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	/**
	 * 查询sysMenu详情data
	 */
	@ResponseBody
	@RequestMapping(value = "/data")
	public Result data(SysMenu sysMenu) {
		Result result = Result.success();
		SysMenu data = sysMenuService.loadSysMenu(sysMenu);
		if (data != null && "1".equals(data.getPid())) {
			data.setPname("菜单根节点");
		}
		result.put("data", data);
		return result;
	}

	/**
	 * 查询sysMenu列表datas，包括下拉框树，菜单授权页面使用
	 */
	@ResponseBody
	@RequestMapping(value = "/datas")
	public List<Result> datas() {
		PageData pd = getPageData();
		List<Result> list = toEasyTree(sysMenuService.packageMenu(sysMenuService.findSysMenu(pd)));
		// 编辑菜单的页面下拉框选择上级，才用到菜单根节点pid=1
		if ("1".equals(pd.get("pid"))) {
			List<Result> rows = new ArrayList<Result>();
			Result root = new Result("id", "1").put("text", "菜单根节点");
			// 判断有无下级
			root.put("state", list.isEmpty() ? "open" : "closed");
			root.put("children", list);
			rows.add(root);
			return rows;
		}
		return list;
	}

	/**
	 * 转换成easyui树形菜单格式
	 */
	private List<Result> toEasyTree(List<SysMenu> list) {
		List<Result> rows = new ArrayList<Result>();
		PageData pd = new PageData();
		for (SysMenu menu : list) {
			Result node = toEasyNode(menu, pd);
			List<Result> children = toEasyTree(menu.getChildMenus());
			// 转下级的，递归调用
			node.put("children", children);
			// 根据又无子节点，更新节点开启状态
			node.put("state", children.isEmpty() ? "open" : "closed");
			rows.add(node);
		}
		return rows;
	}

	private Result toEasyNode(SysMenu menu, PageData pd) {
		Result result = new Result("id", menu.getId()).put("pid", menu.getPid()).put("text", menu.getName())
				.put("iconCls", menu.getIcon());
		result.put("name", menu.getName()).put("icon", menu.getIcon()).put("url", menu.getUrl())
				.put("sort", menu.getSort()).put("status", menu.getStatus()).put("type", menu.getType());
		// 判断有无下级
		result.put("checked", menu.isChecked() && menu.getChildMenus().isEmpty());
		return result;
	}

	/**
	 * 首页左边导航树使用，从用户登录缓存取出
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/userMenus")
	public List<Result> userMenus() {
		SysUser user = (SysUser)getSession().getAttribute(ZConst.SESSION_USER);
		List<SysMenu> list = sysMenuService.packageMenu(sysMenuService.findSysMenu(new PageData("userId", user.getId())));
		return toEasyTree(list);
	}
}