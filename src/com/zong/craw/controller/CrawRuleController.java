package com.zong.craw.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zong.core.bean.PageData;
import com.zong.core.bean.Result;
import com.zong.core.controller.BaseController;
import com.zong.core.util.JsoupUtil;
import com.zong.craw.service.CrawService;
import com.zong.zdb.bean.Table;

@Controller
@RequestMapping("/craw")
public class CrawRuleController extends BaseController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CrawRuleController.class);
	@Autowired
	private CrawService crawService;

	@RequestMapping(value = "/rule/list")
	public String list(Model model) {
		List<Result> rules = crawService.find(JsoupUtil.CRAW_RULE_TABLE, new PageData());
		model.addAttribute("rules", rules);
		return "/craw/rule_list";
	}

	@RequestMapping(value = "/rule/toAdd")
	public String toAdd(Model model) {
		return "/craw/rule_form";
	}

	@RequestMapping(value = "/rule/toEdit")
	public String toEdit(String id, Model model) {
		List<Result> rules = crawService.find(JsoupUtil.CRAW_RULE_TABLE, new PageData("id", id));
		if (!rules.isEmpty()) {
			Result data = rules.get(0);
			try {
				data.put("list_ext", data.readJsonValues("list_ext"));
				data.put("content_ext", data.readJsonValues("content_ext"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			model.addAttribute("rule", data);
		}
		model.addAttribute("formType", "edit");
		return "/craw/rule_form";
	}

	@ResponseBody
	@RequestMapping(value = "/rule/add")
	public Result add() {
		Result result = Result.success();
		try {
			PageData data = getPageData();
			data.put("list_ext", data.readJsonValues("list_ext"));
			data.put("content_ext", data.readJsonValues("content_ext"));
			// 保存表结构
			saveStoreTable(data);
			data.put("list_ext", data.getStringJson("list_ext"));
			data.put("content_ext", data.getStringJson("content_ext"));
			data.remove("id");
			crawService.add(JsoupUtil.CRAW_RULE_TABLE, data);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	private void saveStoreTable(PageData ruleData) throws Exception {
		String craw_store = JsoupUtil.storeTable(ruleData.getString(JsoupUtil.CRAW_STORE_TABLE));
		Table table = crawService.showTable(craw_store);
		if (table == null) {
			crawService.createTable(JsoupUtil.createTableSql(ruleData));
		} else {
			crawService.alterTable(ruleData);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/rule/edit")
	public Result edit(String id) {
		Result result = Result.success();
		try {
			PageData data = getPageData();
			data.put("list_ext", data.readJsonValues("list_ext"));
			data.put("content_ext", data.readJsonValues("content_ext"));
			// 保存表结构
			saveStoreTable(data);
			data.put("list_ext", data.getStringJson("list_ext"));
			data.put("content_ext", data.getStringJson("content_ext"));
			crawService.edit(JsoupUtil.CRAW_RULE_TABLE, data, new PageData("id", id));
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/rule/delete")
	public Result delete(String id) {
		Result result = Result.success();
		try {
			crawService.delete(JsoupUtil.CRAW_RULE_TABLE, new PageData("id", id));
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/rule/data")
	public Result data(String id) {
		Result result = Result.success();
		try {
			List<Result> rules = crawService.find(JsoupUtil.CRAW_RULE_TABLE, new PageData("id", id));
			if (!rules.isEmpty()) {
				Result data = rules.get(0);
				data.put("list_ext", data.readJsonValues("list_ext"));
				data.put("content_ext", data.readJsonValues("content_ext"));
				result.put("data", data);
			}
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/rule/datas")
	public Result datas(String id) {
		Result result = Result.success();
		try {
			List<Result> rules = crawService.find(JsoupUtil.CRAW_RULE_TABLE, new PageData());
			result.put("rows", rules);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	/**
	 * 查询是否可用craw_store
	 */
	@ResponseBody
	@RequestMapping(value = "/rule/check")
	public boolean check(String craw_store) {
		return crawService.find(JsoupUtil.CRAW_RULE_TABLE, new PageData("craw_store", craw_store)).isEmpty();
	}
}
