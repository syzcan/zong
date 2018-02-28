package com.zong.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zong.core.bean.PageData;
import com.zong.core.bean.Result;

/**
 * @desc jsoup爬虫解析网页
 * @author zong
 * @date 2017年4月3日
 */
public class JsoupUtil {
	// 模拟浏览器访问
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31";
	// 规则表名
	public static final String CRAW_RULE_TABLE = "craw_rule";
	// 存储表，实际存储拼接作为表前缀
	public static final String CRAW_STORE_TABLE = "craw_store";
	// 抓取地址参数
	public static final String CRAW_URL = "craw_url";
	// 列表条目规则参数
	public static final String CRAW_ITEM = "craw_item";
	// 列表下一页规则参数，作为参数名返回下一页地址
	public static final String CRAW_NEXT = "craw_next";
	public static final String CRAW_NEXT_REG = "craw_next_reg";
	/**
	 * 扩展规则参数，post请求参数title=
	 * a;attr[reg];href;array,其中RULE_EXT_NAME=title,参数值按;分割对应分别下面各项
	 */
	public static final String RULE_EXT_NAME = "rule_ext_name";
	public static final String RULE_EXT_DESC = "rule_ext_desc";
	public static final String RULE_EXT_CSS = "rule_ext_css";
	// text/html/attr/url【分页方式】
	public static final String RULE_EXT_TYPE = "rule_ext_type";
	// href/src/title
	public static final String RULE_EXT_ATTR = "rule_ext_attr";
	// string/array，默认string返回字符串，array返回数组
	public static final String RULE_EXT_MODE = "rule_ext_mode";
	// 内容正则表达式过滤提取
	public static final String RULE_EXT_REG = "rule_ext_reg";
	// 基本存储表结构字段
	public static final String STORE_TABLE_COL_ID = "id";
	public static final String STORE_TABLE_COL_TITLE = "title";
	public static final String STORE_TABLE_COL_URL = "url";
	public static final String STORE_TABLE_COL_CONTENT = "content";
	public static final String STORE_TABLE_COL_STATUS = "status";
	public static final String STORE_TABLE_COL_CREATE_TIME = "create_time";
	public static final String STORE_TABLE_COL_UPDATE_TIME = "update_time";
	// 启用Js解释器 :1启动
	public static final String JS_ENABLED = "js_enabled";

	/**
	 * 解析列表页面
	 *
	 * @param request
	 */
	public static Result parseList(PageData request) throws Exception {
		Result result = new Result();
		String js_enable = (String) request.remove(JS_ENABLED);
		String craw_url = request.getString(CRAW_URL);
		String craw_item = request.getString(CRAW_ITEM);
		List<PageData> extFields = extFields(request);
		Document document = null;
		if ("1".equals(js_enable)) {
			document = Jsoup.parse(HtmlunitUtil.getHtml(craw_url));
		} else {
			document = Jsoup.connect(craw_url).timeout(60 * 1000).userAgent(USER_AGENT).get();
		}
		Elements elements = document.select(craw_item);
		List<Result> list = new ArrayList<Result>();
		for (Element element : elements) {
			Result data = parseExt(craw_url, element, extFields);
			list.add(data);
		}
		result.put("data", list);
		String craw_next = request.getString(CRAW_NEXT);
		if (craw_next != null && !craw_next.equals("")) {
			PageData ext = extField(CRAW_NEXT, craw_next);
			String next_url = "";
			if ("url".equals(ext.getString(RULE_EXT_TYPE))) {
				Pattern pattern = Pattern.compile(ext.getString(RULE_EXT_REG));
				Matcher matcher = pattern.matcher(craw_url);
				if (matcher.find()) {
					String pageContent = matcher.group(0);
					int nextPage = Integer.parseInt(matcher.group(1)) + 1;
					next_url = craw_url.replace(pageContent, pageContent.replace(matcher.group(1), nextPage + ""));
				}
			} else if ("attr".equals(ext.getString(RULE_EXT_TYPE))) {
				next_url = parseElementAttr(document.select("body").first(), ext, craw_url).toString();
			}
			result.put(CRAW_NEXT, next_url);
		}
		return result;
	}

	/**
	 * 解析详细
	 *
	 * @param request
	 */
	public static Result parseDetail(PageData request) throws Exception {
		String js_enabled = (String) request.remove(JS_ENABLED);
		String craw_url = request.getString(CRAW_URL);
		Document document = null;
		if ("1".equals(js_enabled)) {
			document = Jsoup.parse(HtmlunitUtil.getHtml(craw_url));
		} else {
			document = Jsoup.connect(craw_url).timeout(60 * 1000).userAgent(USER_AGENT).get();
		}
		Result data = parseExt(craw_url, document.select("body").first(), extFields(request));
		return data;
	}

	/**
	 * 获取当前请求规则扩展字段
	 *
	 * @param request
	 */
	private static List<PageData> extFields(PageData request) {
		List<PageData> fields = new ArrayList<PageData>();
		for (Object key : request.keySet()) {
			if (key.equals(CRAW_URL) || key.equals(CRAW_STORE_TABLE) || key.equals(CRAW_RULE_TABLE)
					|| key.equals(CRAW_ITEM) || key.equals(CRAW_NEXT)) {
				continue;
			}
			fields.add(extField(key.toString(), request.getString(key)));
		}
		return fields;
	}

	private static PageData extField(String key, String value) {
		String[] vals = value.split(";");
		PageData ext = new PageData(RULE_EXT_NAME, key);
		ext.put(RULE_EXT_CSS, vals[0]);
		// 正则表达式分离 RULE_EXT_TYPE[RULE_EXT_REG]
		Pattern pattern = Pattern.compile("\\[(.*)\\]");
		Matcher matcher = pattern.matcher(vals[1]);
		if (matcher.find()) {
			ext.put(RULE_EXT_TYPE, vals[1].replace(matcher.group(0), ""));
			ext.put(RULE_EXT_REG, matcher.group(1));
		} else {
			ext.put(RULE_EXT_TYPE, vals[1]);
		}
		if (vals.length > 2) {
			ext.put(RULE_EXT_ATTR, vals[2]);
		}
		if (vals.length > 3) {
			ext.put(RULE_EXT_MODE, vals[3]);
		}
		return ext;
	}

	/**
	 * 解析扩展字段
	 *
	 * @param craw_url
	 * @param element
	 * @param extFields
	 */
	public static Result parseExt(String craw_url, Element element, List<PageData> extFields) {
		Result data = new Result();
		for (PageData ext : extFields) {
			if ("text".equals(ext.getString(RULE_EXT_TYPE))) {
				data.put(ext.getString(RULE_EXT_NAME), parseElementText(element, ext));
			} else if ("html".equals(ext.getString(RULE_EXT_TYPE))) {
				data.put(ext.getString(RULE_EXT_NAME), parseElementHtml(element, ext));
			} else if ("attr".equals(ext.getString(RULE_EXT_TYPE))) {
				data.put(ext.getString(RULE_EXT_NAME), parseElementAttr(element, ext, craw_url));
			}
		}
		return data;
	}

	/**
	 * src和href等链接判断是否加http和项目路径
	 *
	 * @param craw_url
	 * @param link
	 */
	private static String dealLink(String craw_url, String link) {
		String protocal = craw_url.split("://")[0];
		String domain = craw_url.split("://")[0] + "://" + craw_url.split("://")[1].split("/")[0];
		if (link.startsWith("//")) {
			link = protocal + ":" + link;
		} else if (!link.startsWith("http") && !"".equals(link)) {
			if (link.startsWith("/")) {
				link = domain + link;
			} else if (link.startsWith("?")) {
				link = craw_url.split("\\?")[0] + link;
			} else {
				link = domain + "/" + link;
			}
		}
		return link;
	}

	private static Object parseElement(Element element, String cssQuery) {
		if (cssQuery == null || "".equals(cssQuery)) {
			return null;
		}
		// 由于jsoup的:eq(n)用法跟jquery不一样，这里做处理实现和jquery一样的效果
		if (cssQuery.indexOf(":eq") > -1) {
			return patternEq(element, cssQuery);
		} else {
			Elements es = element.select(cssQuery);
			return es;
		}
	}

	/**
	 * 处理cssQuery查询筛选:eq，实现跟jquery一样用法
	 * 
	 * @param element
	 * @param cssQuery
	 * @return
	 */
	private static Object patternEq(Element element, String cssQuery) {
		String[] csses = cssQuery.split(":eq\\((\\d+)\\)");
		List<String> eqs = new ArrayList<String>();
		Pattern pattern = Pattern.compile(":eq\\((\\d+)\\)");
		Matcher matcher = pattern.matcher(cssQuery);
		while (matcher.find()) {
			eqs.add(matcher.group(0));
		}
		Object target = element;
		int i = 0;
		while (i < csses.length) {
			Element tmpE = null;
			if (target instanceof Element) {
				tmpE = (Element) target;
			} else if (target instanceof Elements) {
				tmpE = ((Elements) target).first();
			}
			Elements es = tmpE.select(csses[i]);
			if (es.isEmpty()) {
				return null;
			}
			if (eqs.size() > i) {
				String eq = eqs.get(i);
				int index = Integer.parseInt(eq.replace(":eq(", "").replace(")", ""));
				if (index < es.size()) {
					target = es.get(index);
				} else {
					return null;
				}
			} else {
				target = es.first();
			}
			i++;
		}
		return target;
	}

	/**
	 * 解析当前标签元素text
	 * 
	 * @param element
	 * @param ext 扩展字段封装
	 */
	public static Object parseElementText(Element element, PageData ext) {
		Object object = parseElement(element, ext.getString(RULE_EXT_CSS));
		if (object != null) {
			if (object instanceof Elements) {
				if ("array".equals(ext.getString(RULE_EXT_MODE))) {
					List<String> list = new ArrayList<String>();
					for (Element e : (Elements) object) {
						String text = dealExtReg(e.text(), ext);
						list.add(text);
					}
					return list;
				} else {
					if (!((Elements) object).isEmpty()) {
						return dealExtReg(((Elements) object).get(0).text(), ext);
					}
				}
			} else {
				if ("array".equals(ext.getString(RULE_EXT_MODE))) {
					List<String> list = new ArrayList<String>();
					list.add(dealExtReg(((Element) object).text(), ext));
					return list;
				} else {
					return dealExtReg(((Element) object).text(), ext);
				}
			}
		}
		return "";
	}

	/**
	 * 解析当前标签元素html
	 * 
	 * @param element
	 * @param ext 扩展字段封装
	 */
	public static Object parseElementHtml(Element element, PageData ext) {
		Object object = parseElement(element, ext.getString(RULE_EXT_CSS));
		if (object != null) {
			if (object instanceof Elements) {
				if ("array".equals(ext.getString(RULE_EXT_MODE))) {
					List<String> list = new ArrayList<String>();
					for (Element e : (Elements) object) {
						list.add(dealExtReg(e.html(), ext));
					}
					return list;
				} else {
					if (!((Elements) object).isEmpty()) {
						return dealExtReg(((Elements) object).get(0).html(), ext);
					}
				}
			} else {
				if ("array".equals(ext.getString(RULE_EXT_MODE))) {
					List<String> list = new ArrayList<String>();
					list.add(dealExtReg(((Element) object).html(), ext));
					return list;
				} else {
					return dealExtReg(((Element) object).html(), ext);
				}
			}
		}
		return "";
	}

	/**
	 * 解析当前标签元素attr
	 * 
	 * @param element
	 * @param ext
	 * @param craw_url 当前爬取地址
	 * @return
	 */
	public static Object parseElementAttr(Element element, PageData ext, String craw_url) {
		String attr = ext.getString(RULE_EXT_ATTR);
		Object object = parseElement(element, ext.getString(RULE_EXT_CSS));
		if (object != null) {
			if (object instanceof Elements) {
				if ("array".equals(ext.getString(RULE_EXT_MODE))) {
					List<String> list = new ArrayList<String>();
					for (Element e : (Elements) object) {
						list.add(dealAttr(e.attr(attr), ext, craw_url));
					}
					return list;
				} else {
					if (!((Elements) object).isEmpty()) {
						return dealAttr(((Elements) object).get(0).attr(attr), ext, craw_url);
					}
				}
			} else {
				if ("array".equals(ext.getString(RULE_EXT_MODE))) {
					List<String> list = new ArrayList<String>();
					list.add(dealAttr(((Element) object).attr(attr), ext, craw_url));
					return list;
				} else {
					return dealAttr(((Element) object).attr(attr), ext, craw_url);
				}
			}
		}
		return "";
	}

	private static String dealAttr(String val, PageData ext, String craw_url) {
		// src和href等链接判断是否加http和项目路径
		if ("src".equals(ext.getString(RULE_EXT_ATTR)) || "href".equals(ext.getString(RULE_EXT_ATTR))
				|| "data-src".equals(ext.getString(RULE_EXT_ATTR))
				|| "data-href".equals(ext.getString(RULE_EXT_ATTR))) {
			val = dealLink(craw_url, val);
		}
		return dealExtReg(val, ext);
	}

	/**
	 * 字段提取内容正则表达式进一步过滤，返回正则表达式最后一个匹配组
	 * 
	 * @param val
	 * @param ext 扩展字段封装
	 * @return
	 */
	private static String dealExtReg(String val, PageData ext) {
		String ext_reg = ext.getString(RULE_EXT_REG);
		if (ext_reg != null && !"".equals(ext_reg)) {
			Pattern pattern = Pattern.compile(ext_reg);
			Matcher matcher = pattern.matcher(val);
			if (matcher.find()) {
				int gc = matcher.groupCount();
				for (int i = 0; i <= gc; i++) {
					if (matcher.group(i) != null) {
						val = matcher.group(i);
					}
				}
			}
		}
		return val;
	}

	/**
	 * 构造基本存储的表结构的基本字段mysql
	 */
	public static String baseTableColumns() {
		StringBuffer sb = new StringBuffer();
		sb.append("id int(11) NOT NULL PRIMARY KEY auto_increment COMMENT 'mysql自增主键',");
		sb.append("title varchar(255) COMMENT '标题',");
		sb.append("url varchar(255) UNIQUE KEY COMMENT '详情地址，如果有详情页',");
		sb.append("content mediumtext COMMENT '详情内容',");
		sb.append("status int(11) NOT NULL default 1 COMMENT '状态：1未解析 2已解析 3解析失败',");
		sb.append("create_time datetime COMMENT '创建时间',");
		sb.append("update_time datetime COMMENT '修改时间',");
		return sb.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String createTableSql(PageData ruleData) {
		List<Map> list_ext = (List<Map>) ruleData.get("list_ext");
		List<Map> content_ext = (List<Map>) ruleData.get("content_ext");
		List<Map> columns = new ArrayList();
		columns.addAll(list_ext);
		columns.addAll(content_ext);
		String sql = "create table " + storeTable(ruleData.getString("craw_store")) + "(" + baseTableColumns();
		for (Map data : columns) {
			String column = data.get(RULE_EXT_NAME).toString();
			String desc = data.get(RULE_EXT_DESC).toString();
			if (!column.equals(JsoupUtil.CRAW_URL) && !column.equals(JsoupUtil.CRAW_RULE_TABLE)
					&& !column.equals(JsoupUtil.CRAW_STORE_TABLE) && !column.equals(JsoupUtil.CRAW_ITEM)
					&& !column.equals(JsoupUtil.CRAW_NEXT) && !column.equals(STORE_TABLE_COL_ID)
					&& !column.equals(STORE_TABLE_COL_TITLE) && !column.equals(STORE_TABLE_COL_URL)
					&& !column.equals(STORE_TABLE_COL_CONTENT) && !column.equals(STORE_TABLE_COL_STATUS)
					&& !column.equals(STORE_TABLE_COL_CREATE_TIME) && !column.equals(STORE_TABLE_COL_UPDATE_TIME)) {
				sql += column + " varchar(500) COMMENT '" + desc + "',";
			}
		}
		sql = sql.substring(0, sql.lastIndexOf(",")) + ") COMMENT='" + ruleData.getString("name") + "';";
		return sql;
	}

	/**
	 * 构造存储表名称，CRAW_STORE_TABLE作为表前缀
	 * 
	 * @param tableName
	 */
	public static String storeTable(String tableName) {
		return CRAW_STORE_TABLE + "_" + tableName;
	}

	public static List<Result> queueDatas = new ArrayList<Result>();

	/**
	 * 获取当前craw_store的队列
	 * 
	 * @param craw_store
	 */
	public static Queue crawQueue(String craw_store) {
		Queue queue = null;
		for (Result data : queueDatas) {
			if (craw_store.equals(data.getString("craw_store"))) {
				queue = (Queue) data.get("queue");
				break;
			}
		}
		if (queue == null) {
			queue = new Queue();
			queueDatas.add(new Result("craw_store", craw_store).put("queue", queue));
		}
		return queue;
	}

}