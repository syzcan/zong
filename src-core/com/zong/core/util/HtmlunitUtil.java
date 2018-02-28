package com.zong.core.util;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @desc htmlunit抓取网页可以模拟js事件，获得动态js页面
 * @author zong
 * @date 2017年4月11日
 */
public class HtmlunitUtil {
	public static WebClient wc = new WebClient(BrowserVersion.CHROME);

	public static String getHtml(String url) throws Exception {
		wc.getOptions().setUseInsecureSSL(true);
		wc.getOptions().setJavaScriptEnabled(true); // 启用JS解释器，默认为true
		wc.getOptions().setCssEnabled(false); // 禁用css支持
		wc.getOptions().setThrowExceptionOnScriptError(false); // js运行错误时，是否抛出异常
		wc.getOptions().setTimeout(30 * 1000); // 如果为0，则无限期等待
		wc.getOptions().setDoNotTrackEnabled(false);
		HtmlPage page = wc.getPage(url);
		String pageXml = page.asXml();
		return pageXml;
	}
}
