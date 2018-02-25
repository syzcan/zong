package com.zong.craw.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spreada.utils.chinese.ZHConverter;
import com.zong.core.bean.DataMap;
import com.zong.core.bean.Page;
import com.zong.core.bean.Result;
import com.zong.core.dao.CommonMapper;
import com.zong.core.exception.ServiceException;
import com.zong.core.util.JsoupUtil;
import com.zong.zdb.bean.ColumnField;
import com.zong.zdb.bean.Table;
import com.zong.zdb.service.JdbcCodeService;

/**
 * @desc 通用业务层
 * @author zong
 * @date 2017年3月24日
 */
@Service
public class CrawService {
	private static final ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
	@Autowired
	private CommonMapper commonMapper;
	@Autowired
	private JdbcCodeService codeService;

	@Transactional(rollbackFor = Exception.class)
	public void add(String table, DataMap pd) throws ServiceException {
		for (Object key : pd.keySet()) {
			Object value = pd.get(key);
			if (value != null && !(value instanceof Date)) {
				if (!(value instanceof String)) {
					pd.put(key, value.toString());
				}
				pd.put(key, converter.convert(pd.getString(key)));
			}
		}
		commonMapper.insert(table, pd);
	}

	@Transactional(rollbackFor = Exception.class)
	public void delete(String table, DataMap pd) throws ServiceException {
		commonMapper.delete(table, pd);
	}

	@Transactional(rollbackFor = Exception.class)
	public void edit(String table, DataMap pd, DataMap idPd) throws ServiceException {
		for (Object key : pd.keySet()) {
			Object value = pd.get(key);
			if (value != null && !(value instanceof Date)) {
				if (!(value instanceof String)) {
					pd.put(key, value.toString());
				}
				pd.put(key, converter.convert(pd.getString(key)));
			}
		}
		commonMapper.update(table, pd, idPd);
	}

	public Result load(String table, DataMap pd) {
		List<Result> list = commonMapper.find(table, pd);
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	public List<Result> find(String table, DataMap pd) {
		return commonMapper.find(table, pd);
	}

	public List<Result> findPage(Page page) {
		return commonMapper.findPage(page);
	}

	public void createTable(String createTableSql) {
		commonMapper.executeSql(createTableSql);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void alterTable(DataMap ruleData) {
		String tableName = JsoupUtil.storeTable(ruleData.getString(JsoupUtil.CRAW_STORE_TABLE));
		Table table = codeService.currentTable(tableName);
		List<ColumnField> list = table.getNormalColumns();
		List<Map> list_ext = (List<Map>) ruleData.get("list_ext");
		List<Map> content_ext = (List<Map>) ruleData.get("content_ext");
		List<Map> columns = new ArrayList();
		columns.addAll(list_ext);
		columns.addAll(content_ext);
		for (Map data : columns) {
			String column = data.get(JsoupUtil.RULE_EXT_NAME).toString();
			if (!column.equals(JsoupUtil.STORE_TABLE_COL_ID) && !column.equals(JsoupUtil.STORE_TABLE_COL_TITLE)
					&& !column.equals(JsoupUtil.STORE_TABLE_COL_URL)
					&& !column.equals(JsoupUtil.STORE_TABLE_COL_CONTENT)
					&& !column.equals(JsoupUtil.STORE_TABLE_COL_CREATE_TIME)
					&& !column.equals(JsoupUtil.STORE_TABLE_COL_UPDATE_TIME)) {
				String desc = data.get(JsoupUtil.RULE_EXT_DESC).toString();
				boolean flag = true;
				for (ColumnField col : list) {
					if (col.getColumn().equals(column)) {
						flag = false;
						if (!desc.equals(col.getRemark())) {
							commonMapper.executeSql("alter table " + tableName + " modify " + column
									+ " varchar(500) COMMENT '" + desc + "';");
						}
					}
				}
				if (flag) {
					commonMapper.executeSql(
							"alter table " + tableName + " add " + column + " varchar(500) COMMENT '" + desc + "';");
				}
			}
		}
		if (!table.getComment().equals(ruleData.getString("name"))) {
			commonMapper.executeSql("alter table " + tableName + " comment '" + ruleData.getString("name") + "';");
		}
	}

	public Table showTable(String tableName) throws Exception {
		return codeService.currentTable(tableName);
	}

	public List<Table> showTables() throws Exception {
		return codeService.currentTables();
	}

	public List<ColumnField> showTableColumns(String tableName) throws Exception {
		return codeService.currentTableColumns(tableName);
	}

}
