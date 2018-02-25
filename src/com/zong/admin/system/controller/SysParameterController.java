package com.zong.admin.system.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zong.admin.system.bean.SysParameter;
import com.zong.admin.system.service.SysParameterService;
import com.zong.core.bean.Page;
import com.zong.core.bean.PageData;
import com.zong.core.bean.Result;
import com.zong.core.controller.BaseController;
import com.zong.core.dao.CommonMapper;
import com.zong.core.exception.ServiceException;
import com.zong.core.util.ZConst;

/**
 * @desc sysParameter控制层 系统参数表
 * @author zong
 * @date 2017年03月19日
 */
@Controller
@RequestMapping(value = "/system/parameter")
public class SysParameterController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(SysParameterController.class);

	@Autowired
	private SysParameterService sysParameterService;
	@Autowired
	private CommonMapper commonMapper;

	/**
	 * 查询sysParameter列表
	 */
	@RequestMapping(value = "/list")
	public String list(Model model) {
		return "/system/sysParameter_list";
	}

	/**
	 * 新增sysParameter页面
	 */
	@RequestMapping(value = "/toAdd")
	public String toAdd(Model model) {
		return "/system/sysParameter_form";
	}

	/**
	 * 新增sysParameter
	 */
	@ResponseBody
	@RequestMapping(value = "/add")
	public Result add(SysParameter sysParameter) {
		Result result = Result.success();
		try {
			sysParameterService.addSysParameter(sysParameter);
		} catch (ServiceException e) {
			LOGGER.warn(e.getMessage());
			result.error(e);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	/**
	 * 修改sysParameter页面
	 */
	@RequestMapping(value = "/toEdit")
	public String toEdit(Model model) {
		model.addAttribute("edit", true);
		return "/system/sysParameter_form";
	}

	/**
	 * 修改sysParameter
	 */
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Result edit(SysParameter sysParameter) {
		Result result = Result.success();
		try {
			sysParameterService.editSysParameter(sysParameter);
			// 更新application缓存参数
			PageData zparam = (PageData) getApplication().getAttribute(ZConst.ZPARAM);
			zparam.put(sysParameter.getParamKey(),
					new Result("name", sysParameter.getName()).put("param_key", sysParameter.getParamKey())
							.put("param_value", sysParameter.getParamValue()).put("remark", sysParameter.getRemark()));
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	/**
	 * 删除sysParameter
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Result delete(SysParameter sysParameter) {
		Result result = Result.success();
		try {
			sysParameterService.deleteSysParameter(sysParameter);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	/**
	 * 查询sysParameter详情data
	 */
	@ResponseBody
	@RequestMapping(value = "/data")
	public Result data(SysParameter sysParameter) {
		Result result = Result.success();
		SysParameter data = sysParameterService.loadSysParameter(sysParameter);
		result.put("data", data);
		return result;
	}

	/**
	 * 查询sysParameter列表datas
	 */
	@ResponseBody
	@RequestMapping(value = "/datas")
	public Result datas() {
		Result result = Result.success();
		Page page = super.getPage();
		List<SysParameter> sysParameters = sysParameterService.findSysParameterPage(page);
		result.put("rows", sysParameters).put("total", page.getTotalResult());
		return result;
	}

	/**
	 * 查询sysParameter是否可用key
	 */
	@ResponseBody
	@RequestMapping(value = "/check")
	public boolean check(String paramKey) {
		return commonMapper.find("sys_parameter", new PageData("param_key", paramKey)).isEmpty();
	}
}
