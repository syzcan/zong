package com.zong.craw.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zong.admin.common.controller.CommonController;
import com.zong.core.bean.Page;
import com.zong.core.bean.PageData;
import com.zong.core.bean.Result;
import com.zong.core.controller.BaseController;
import com.zong.core.dao.CommonMapper;
import com.zong.core.util.JsoupUtil;
import com.zong.craw.service.CrawService;
import com.zong.zdb.bean.ColumnField;

@Controller
@RequestMapping("/craw")
public class CrawController extends BaseController {
	private Logger LOGGER = LoggerFactory.getLogger(CommonController.class);
	@Autowired
	private CrawService crawService;
	@Autowired
	private CommonMapper commonMapper;

	/**
	 * 解析任何页面
	 * 
	 * @param craw_url 抓取地址【必须】
	 * @param exts 非craw_*关键参数的其他扩展规则
	 */
	@ResponseBody
	@RequestMapping("/data")
	public Result data() {
		Result result = Result.success();
		try {
			PageData pd = super.getPageData();
			Result data = JsoupUtil.parseDetail(pd);
			result.put("data", data);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	/**
	 * 解析列表页面
	 * 
	 * @param craw_url 抓取地址【必须】
	 * @param craw_item 条目规则【必须】
	 * @param craw_next 下一页规则
	 * @param craw_store 存储表名，只有指定才保存到数据库
	 * @param exts 非craw_*关键参数的其他扩展规则
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/crawList")
	public Result crawList() {
		Result result = Result.success();
		try {
			PageData pd = super.getPageData();
			Result data = JsoupUtil.parseList(pd);
			saveData(pd, (List<Result>) data.get("data"));
			result.put("craw", data);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
			if (e.toString().indexOf("Status=404") > -1) {
				result.put(Result.RET_MSG, "系统错误：Status=404");
			}
		}
		return result;
	}

	private void saveData(PageData pd, List<Result> data) throws Exception {
		String craw_store = pd.getString(JsoupUtil.CRAW_STORE_TABLE);
		if (craw_store != null && !"".equals(craw_store)) {
			craw_store = JsoupUtil.storeTable(craw_store);
			for (Result pageData : data) {
				try {
					pageData.put(JsoupUtil.STORE_TABLE_COL_CREATE_TIME, new Date());
					crawService.add(craw_store, pageData);
					LOGGER.info("抓取插入 {} : {}", craw_store, pageData.get("url"));
				} catch (Exception e) {
					LOGGER.warn(e.toString());
				}
			}
		}
	}

	@RequestMapping(value = "/toCrawList")
	public String toCrawList(Model model) {
		List<Result> rules = crawService.find(JsoupUtil.CRAW_RULE_TABLE, new PageData());
		model.addAttribute("rules", rules);
		return "/craw/craw_list";
	}

	@RequestMapping(value = "/toCrawTab")
	public String toCrawTab() {
		return "/craw/craw_tab";
	}

	/**
	 * 详情抓取使用队列，把待抓取数据存到队列，可实现多线程
	 * 
	 * @param craw_store
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/toCrawDetail")
	public String toCrawDetail(Model model) {
		return "/craw/craw_detail";
	}

	@ResponseBody
	@RequestMapping(value = "/crawQueue")
	public Result crawQueue(String craw_store) {
		Result result = Result.success();
		List<Result> stores = new ArrayList<Result>();
		// 先查询队列是否还有数据，没有从数据库取
		if (JsoupUtil.crawQueue(craw_store).isEmpty()) {
			Page page = new Page();
			page.setTable(JsoupUtil.storeTable(craw_store));
			// 一次全部取出来，只查询关键字段
			page.setShowCount(10000000);
			String[] columns = { "id", "title", "url", "status" };
			// status=1待抓取的数据
			page.getPd().put("status", 1).put("columns", columns);
			List<Result> list = crawService.findPage(page);
			// 存入队列
			for (Result data : list) {
				JsoupUtil.crawQueue(craw_store).add(data);
			}
		}
		// 一次取15条出来
		for (int i = 1; i <= 15; i++) {
			if (!JsoupUtil.crawQueue(craw_store).isEmpty()) {
				stores.add(JsoupUtil.crawQueue(craw_store).remove());
			}
		}
		result.put("rows", stores).put("total", JsoupUtil.crawQueue(craw_store).size());
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/crawDetail")
	public Result crawDetail() {
		Result result = Result.success();
		PageData request = super.getPageData();
		String craw_store = JsoupUtil.storeTable(request.getString(JsoupUtil.CRAW_STORE_TABLE));
		try {
			Result data = JsoupUtil.parseDetail(request);
			crawService.edit(craw_store, data.put("status", 2).put(JsoupUtil.STORE_TABLE_COL_UPDATE_TIME, new Date()),
					new PageData("url", request.getString(JsoupUtil.CRAW_URL)));
			LOGGER.info("爬取并保存 {}", request.getString(JsoupUtil.CRAW_URL));
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
			commonMapper.update(craw_store, new PageData("status", 3),
					new PageData("url", request.getString(JsoupUtil.CRAW_URL)));
		}
		return result;
	}

	@RequestMapping(value = "/store/list")
	public String stores(Model model) {
		return "/craw/store_list";
	}

	@ResponseBody
	@RequestMapping(value = "/store/datas")
	public Result datas() {
		Result result = Result.success();
		try {
			List<Result> rules = crawService.find(JsoupUtil.CRAW_RULE_TABLE, new PageData());
			Page page = super.getPage();
			String keyword = (String) page.getPd().remove("keyword");
			String craw_store = (String) page.getPd().remove(JsoupUtil.CRAW_STORE_TABLE);
			if (keyword != null) {
				page.getPd().put("like", new PageData("title", keyword));
			}
			if (craw_store == null) {
				if (!rules.isEmpty()) {
					String table = rules.get(0).getString(JsoupUtil.CRAW_STORE_TABLE);
					craw_store = JsoupUtil.storeTable(table);
					getPageData().put("craw_store", table);
				} else {
					craw_store = JsoupUtil.CRAW_STORE_TABLE;
				}
			} else {
				craw_store = JsoupUtil.storeTable(craw_store);
			}
			page.setTable(craw_store);
			List<ColumnField> columnFields = crawService.showTableColumns(craw_store);
			List<String> list = new ArrayList<String>();
			for (ColumnField columnField : columnFields) {
				if (!columnField.getColumn().equals(JsoupUtil.STORE_TABLE_COL_CONTENT)) {
					list.add(columnField.getColumn());
				}
			}
			String[] columns = list.toArray(new String[list.size()]);
			page.getPd().put("columns", columns);
			List<Result> stores = crawService.findPage(page);
			result.put("rows", stores).put("total", page.getTotalResult());
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/store/ruleBox")
	public List<Result> ruleBox() {
		List<Result> list = crawService.find(JsoupUtil.CRAW_RULE_TABLE, new PageData());
		list.get(0).put("selected", 1);
		return list;
	}

	@RequestMapping(value = "/store/view")
	public String store(String rule_id, String craw_store, String id, Model model) {
		try {
			String tableName = JsoupUtil.storeTable(craw_store);
			Result store = crawService.load(tableName, new PageData("id", id));
			model.addAttribute("table", crawService.showTable(tableName));
			model.addAttribute("store", store);
			Result rule = crawService.load(JsoupUtil.CRAW_RULE_TABLE, new PageData("id", rule_id));
			if (rule != null) {
				rule.put("list_ext", rule.readJsonValues("list_ext"));
				rule.put("content_ext", rule.readJsonValues("content_ext"));
				model.addAttribute("rule", rule);
			}
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
		}
		return "/craw/store_view";
	}
}
