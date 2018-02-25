package com.zong.coder.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zong.coder.util.ZipUtil;
import com.zong.core.bean.Result;
import com.zong.core.controller.BaseController;
import com.zong.core.util.BeetlUtil;
import com.zong.core.util.FileUploadDownload;
import com.zong.core.util.FileUtils;
import com.zong.zdb.bean.Table;
import com.zong.zdb.service.JdbcCodeService;
import com.zong.zdb.service.TemplateRoot;
import com.zong.zdb.util.CreateCodeUtil;
import com.zong.zdb.util.Page;
import com.zong.zdb.util.PageData;
import com.zong.zdb.util.ZDBConfig;

@Controller
@RequestMapping("/coder")
public class DatabaseController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(DatabaseController.class);
	@Autowired
	private JdbcCodeService codeService;

	private static ObjectMapper objectMapper = new ObjectMapper();
	private static String CURRENT = "current";

	@RequestMapping(value = "/tables/list")
	public String tables(Model model) {
		return "/coder/table_list";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping(value = "/dbs")
	public Result dbs() {
		Result result = Result.success();
		try {
			String zdbPath = getRealPath() + BtlController.BTL_PATH + "/zdb.json";
			String json = FileUtils.readTxt(zdbPath);
			Map config = objectMapper.readValue(json, Map.class);
			List<Map> rows = (List<Map>) config.get("dbs");
			boolean flag = true;
			for (Map map : rows) {
				if (map.get("dbname").equals(CURRENT)) {
					flag = false;
					break;
				}
			}
			if (flag) {
				rows.add(0, new Result("dbname", CURRENT));
			}
			result.put("rows", rows);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping(value = "/dbs/add")
	public Result add() {
		Result result = Result.success();
		try {
			String zdbPath = getRealPath() + BtlController.BTL_PATH + "/zdb.json";
			Map config = objectMapper.readValue(FileUtils.readTxt(zdbPath), Map.class);
			List rows = (List) config.get("dbs");
			rows.add(getPageData());
			FileUtils.writeTxt(zdbPath, objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(config));
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping(value = "/dbs/edit")
	public Result edit() {
		Result result = Result.success();
		try {
			com.zong.core.bean.PageData pd = getPageData();
			String zdbPath = getRealPath() + BtlController.BTL_PATH + "/zdb.json";
			Map config = objectMapper.readValue(FileUtils.readTxt(zdbPath), Map.class);
			List<Map> rows = (List<Map>) config.get("dbs");
			for (Map map : rows) {
				if (map.get("dbname").equals(pd.get("dbname"))) {
					map.put(ZDBConfig.JDBC_DRIVER, pd.get(ZDBConfig.JDBC_DRIVER));
					map.put(ZDBConfig.JDBC_URL, pd.get(ZDBConfig.JDBC_URL));
					map.put(ZDBConfig.JDBC_USERNAME, pd.get(ZDBConfig.JDBC_USERNAME));
					map.put(ZDBConfig.JDBC_PASSWORD, pd.get(ZDBConfig.JDBC_PASSWORD));
					break;
				}
			}
			FileUtils.writeTxt(zdbPath, objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(config));
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping(value = "/dbs/del")
	public Result del() {
		Result result = Result.success();
		try {
			com.zong.core.bean.PageData pd = getPageData();
			String zdbPath = getRealPath() + BtlController.BTL_PATH + "/zdb.json";
			Map config = objectMapper.readValue(FileUtils.readTxt(zdbPath), Map.class);
			List<Map> rows = (List<Map>) config.get("dbs");
			for (Map map : rows) {
				if (map.get("dbname").equals(pd.get("dbname"))) {
					rows.remove(map);
					break;
				}
			}
			FileUtils.writeTxt(zdbPath, objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(config));
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/dbs/reconnect")
	public Result reconnect() {
		Result result = Result.success();
		try {
			String zdbPath = getRealPath() + BtlController.BTL_PATH + "/zdb.json";
			String json = FileUtils.readTxt(zdbPath);
			ZDBConfig.readConfig(json);
			// ZDBConfig.reconnect();
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result.error(e);
		}
		return result;
	}

	@RequestMapping(value = "/tables/data")
	public String data() {
		return "/coder/table_data";
	}

	@RequestMapping(value = "/tables/code")
	public String code(String dbname, String tableName, Model model) {
		Table table = dbname == null || dbname.equals(CURRENT) ? codeService.currentTable(tableName)
				: codeService.showTable(dbname, tableName);
		List<String> ftls = new ArrayList<String>();
		for (File f : FileUtils.listFile(getRealPath() + BtlController.BTL_PATH, "btl")) {
			ftls.add(f.getName().substring(0, f.getName().lastIndexOf(".")));
		}
		model.addAttribute("templates", ftls);
		model.addAttribute("table", TemplateRoot.createTemplateRoot(table));
		return "/coder/table_code";
	}

	@ResponseBody
	@RequestMapping(value = "/tables.json")
	public Result tables(String dbname) {
		Result result = Result.success();
		if (dbname == null || dbname.equals(CURRENT)) {
			result.put("rows", codeService.currentTables());
		} else {
			result.put("rows", codeService.showTables(dbname));
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/tables/{tableName}.json")
	public Result table(String dbname, @PathVariable("tableName") String tableName) {
		Result result = Result.success();
		if (dbname == null || dbname.equals(CURRENT)) {
			result.put("rows", codeService.currentTableColumns(tableName));
		} else {
			result.put("rows", codeService.showTableColumns(dbname, tableName));
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/tables/{tableName}/datas")
	public Result datas(String dbname, @PathVariable("tableName") String tableName, int rows, int page, String sort,
			String order) {
		Result result = Result.success();
		Page p = new Page();
		p.setTable(tableName);
		p.setShowCount(rows);
		p.setCurrentPage(page);
		p.getPd().put("orderColumn", sort).put("orderType", order);
		List<PageData> datas = new ArrayList<PageData>();
		if (dbname == null || dbname.equals(CURRENT)) {
			datas = codeService.getCurrentDBDao().showTableDatas(p);
		} else {
			datas = codeService.showTableData(dbname, p);
		}
		result.put("rows", datas).put("total", p.getTotalResult());
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/tables/{tableName}/sqldatas")
	public Result sqldatas(String dbname, @PathVariable("tableName") String tableName, String sql) {
		Result result = Result.success();
		if (sql != null && !sql.equals("")) {
			List<PageData> datas = new ArrayList<PageData>();
			if (dbname == null || dbname.equals(CURRENT)) {
				datas = codeService.getCurrentDBDao().showSqlDatas(sql);
			} else {
				datas = codeService.showSqlData(dbname, sql);
			}
			result.put("rows", datas);
		}
		return result;
	}

	/**
	 * 下载代码code.zip
	 */
	@RequestMapping(value = "/tables/{tableName}/downCode")
	public void downCode(String dbname, @PathVariable("tableName") String tableName, String objectName,
			String className, String packageName, HttpServletResponse response) {
		try {
			Table table = dbname == null || dbname.equals(CURRENT) ? codeService.currentTable(tableName)
					: codeService.showTable(dbname, tableName);
			// 生成代码
			String filePath = CreateCodeUtil.downCode(table, objectName, className, packageName,
					getRealPath() + "WEB-INF/jsp/coder/down_code", getRealPath() + "WEB-INF/jsp/coder/btl/");
			// 压缩
			String zipPath = filePath + "/code.zip";
			new File(zipPath).delete();
			ZipUtil.zip(filePath, zipPath);
			// 下载
			FileUploadDownload.fileDownload(response, zipPath, "code_" + tableName + ".zip");
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
		}
	}

	/**
	 * 根据beetl模板生成当前表的文件字符串返回
	 *
	 */
	@ResponseBody
	@RequestMapping(value = "/tables/{tableName}/code/{type}", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
	public String beetlCode(String dbname, @PathVariable("tableName") String tableName,
			@PathVariable("type") String type, String objectName, String className, String packageName) {
		String result = "";
		try {
			String btlPath = getRealPath() + "WEB-INF/jsp/coder/btl/" + type + ".btl";
			Table table = dbname == null || dbname.equals(CURRENT) ? codeService.currentTable(tableName)
					: codeService.showTable(dbname, tableName);
			result = BeetlUtil.printString(btlPath,
					TemplateRoot.createTemplateRoot(table, objectName, className, packageName));
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			result = e.toString();
		}
		return result;
	}
}
