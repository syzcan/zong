package com.zong.coder.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zong.core.bean.Result;
import com.zong.core.controller.BaseController;
import com.zong.core.util.FileUtils;

@Controller
@RequestMapping("/coder")
public class BtlController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(BtlController.class);

	public static String BTL_PATH = "WEB-INF/jsp/coder/btl";

	@RequestMapping(value = "/templates")
	public String templates(Model model) {
		List<String> ftls = new ArrayList<String>();
		for (File f : FileUtils.listFile(getRealPath() + BTL_PATH,"btl")) {
			ftls.add(f.getName().substring(0, f.getName().lastIndexOf(".")));
		}
		model.addAttribute("templates", ftls);
		return "/coder/template";
	}

	@ResponseBody
	@RequestMapping(value = "/templates/{name}", produces = { "application/json;charset=UTF-8" })
	public Result template(@PathVariable("name") String name) {
		Result result = Result.success();
		try {
			result.put("content", FileUtils.readTxt(getRealPath() + BTL_PATH + "/" + name + ".btl"));
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/templates/{name}/edit")
	public Result editTemplate(@PathVariable("name") String name, String content) {
		Result result = Result.success();
		try {
			FileUtils.writeTxt(getRealPath() + BTL_PATH + "/" + name + ".btl", content);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}
}
