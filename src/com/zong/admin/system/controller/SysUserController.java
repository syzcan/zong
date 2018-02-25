package com.zong.admin.system.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zong.admin.passport.service.PassportService;
import com.zong.admin.system.bean.SysRole;
import com.zong.admin.system.bean.SysUser;
import com.zong.admin.system.dao.SysRoleMapper;
import com.zong.admin.system.service.SysRoleService;
import com.zong.admin.system.service.SysUserService;
import com.zong.core.bean.Page;
import com.zong.core.bean.PageData;
import com.zong.core.bean.Result;
import com.zong.core.controller.BaseController;
import com.zong.core.util.ExportExcel;
import com.zong.core.util.MD5Util;

import jxl.write.Label;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.coobird.thumbnailator.Thumbnails;

/**
 * @desc sysUser控制层 系统用户表
 * @author zong
 * @date 2017年03月19日
 */
@Controller
@RequestMapping(value = "/system/user")
public class SysUserController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(SysUserController.class);

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private PassportService passportService;
	@Autowired
	private SysRoleService roleService;
	@Autowired
	private SysRoleMapper roleMapper;

	/**
	 * 查询sysUser列表
	 */
	@RequestMapping(value = "/list")
	public String list(Model model) {
		return "/system/sysUser_list";
	}
	
	/**
	 * 新增sysUser页面
	 */
	@RequestMapping(value = "/toAdd")
	public String toAdd(Model model) {
		model.addAttribute("roles", roleService.findSysRole(new PageData()));
		return "/system/sysUser_form";
	}

	/**
	 * 修改sysUser页面
	 */
	@RequestMapping(value = "/toEdit")
	public String toEdit(SysUser sysUser, Model model) {
		//全部角色
		List<SysRole> roles = roleService.findSysRole(new PageData());
		//当前角色，选中
		List<SysRole> myRoles = roleService.findSysRole(new PageData("userId",sysUser.getId()));
		for (SysRole sysRole : myRoles) {
			for (SysRole role : roles) {
				if(role.getId().equals(sysRole.getId())){
					role.setChecked(true);
					break;
				}
			}
		}
		model.addAttribute("roles", roles);
		return "/system/sysUser_form";
	}
	
	/**
	 * 新增sysUser
	 */
	@ResponseBody
	@RequestMapping(value = "/add")
	public Result add(SysUser sysUser) {
		Result result = Result.success();
		try {
			//随机用户名，初始密码为MD5加密123456
			sysUser.setUsername(passportService.makeUsername());
			sysUser.setPassword(MD5Util.MD5("123456"));
			sysUserService.addSysUser(sysUser);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}
	
	/**
	 * 修改sysUser
	 */
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Result edit(SysUser sysUser) {
		Result result = Result.success();
		try {
			sysUserService.editSysUser(sysUser);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	/**
	 * 删除sysUser
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Result delete(SysUser sysUser) {
		Result result = Result.success();
		try {
			sysUserService.deleteSysUser(sysUser);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	/**
	 * 查询sysUser详情data
	 */
	@ResponseBody
	@RequestMapping(value = "/data")
	public Result data(SysUser sysUser) {
		Result result = Result.success();
		SysUser data = sysUserService.loadSysUser(sysUser);
		result.put("data", data);
		return result;
	}

	/**
	 * 查询sysUser列表datas
	 */
	@ResponseBody
	@RequestMapping(value = "/datas")
	public Result datas() {
		Result result = Result.success();
		Page page = super.getPage();
		List<SysUser> sysUsers = sysUserService.findSysUserPage(page);
		result.put("rows", sysUsers).put("total", page.getTotalResult());
		return result;
	}	
	
	@ResponseBody
	@RequestMapping(value = "/roleBox")
	public List<Result> roleBox(String userId) {
		return roleMapper.findRoleBox(userId);
	}	
	
	/**
	 * 导出excel数据
	 * 
	 * @param response
	 */
	@RequestMapping(value = "/exportExcel")
	public void exportExcel(String id, HttpServletResponse response) {
		try {
			String[] ids = id.split(",");
			WritableWorkbook workbook = ExportExcel.getWorkbook("用户信息.xls", response);
			WritableSheet ws = workbook.createSheet("资料", 0);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			for (int i = 0; ids != null && i < ids.length; i++) {
				SysUser user = new SysUser();
				user.setId(ids[i]);
				user = sysUserService.loadSysUser(user);
				File file = new File(getRealPath() + user.getAvatar());
				if (!file.exists() || !file.isFile()) {
					file = new File(getRealPath() + "static/images/avatar.jpg");
				}
				// 图片先用Thumbnails压缩，再getImageData转为png格式
				// 前两位是起始格，后两位是图片占多少个格，并非是位置
				WritableImage image = new WritableImage(0, i * 9, 2, 8, ExportExcel.getImageData(Thumbnails.of(ImageIO.read(file)).size(200, 200).asBufferedImage()));
				ws.addImage(image);
				ws.addCell(new Label(2, i * 9, "账号："));
				ws.addCell(new Label(3, i * 9, user.getUsername()));
				ws.addCell(new Label(2, i * 9 + 1, "昵称："));
				ws.addCell(new Label(3, i * 9 + 1, user.getNickname()));
				ws.addCell(new Label(2, i * 9 + 2, "性别："));
				ws.addCell(new Label(3, i * 9 + 2, user.getGender()));
				ws.addCell(new Label(2, i * 9 + 3, "生日："));
				ws.addCell(new Label(3, i * 9 + 3, user.getBirthday()==null?"":dateFormat.format(user.getBirthday())));
				ws.addCell(new Label(2, i * 9 + 4, "上次登录："));
				ws.addCell(new Label(3, i * 9 + 4, user.getLastLogin()==null?"":dateFormat.format(user.getLastLogin())));
				ws.addCell(new Label(2, i * 9 + 5, "创建时间："));
				ws.addCell(new Label(3, i * 9 + 5, user.getCreateTime()==null?"":dateFormat.format(user.getCreateTime())));
				ws.addCell(new Label(2, i * 9 + 6, "简介："));
				ws.addCell(new Label(3, i * 9 + 6, user.getRemark()));
			}
			ExportExcel.writeWorkbook(workbook);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
		}
	}
}
