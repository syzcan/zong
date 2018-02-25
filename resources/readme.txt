zbase项目所用框架和技术：

一、后端技术框架
1.spring4.1.9
2.springMVC
3.mybatis3.3
4.数据库支持mysql、oracle
5.数据库连接池druid-1.0.18.jar
6.日至统一使用slf4j-api.jar接口声明，这里用log4j-1.2.17.jar实现
7.json序列化工具jackson2.7.3
8.模板工具freemarker-2.3.8.jar
9.新一代模板工具beetl-2.2.5.jar【语法易懂性能更优】
10.网页抓取和解析jsoup-1.9.1.jar【可方便构造http请求】
11.excel生成工具poi-3.14.jar

二、前端框架和插件
1.pintuer1.2 作为基础样式
	源码修改：pintuer.js第150行表单提示使用 layer.tips($checktext, this,{tipsMore: true});
		   308行表单ajax校验;	
2.layer-v3.0.3 Web弹层组件
3.layDate v1.1 日期控件
	源码修改：laydate.css第10行样式加上 -webkit-box-sizing:content-box;-moz-box-sizing:content-box;【解决在pintuer下变形问题】
4.umeditor1.2.2 百度富文本编辑器UEditor Mini版本【包含模板引擎etpl】
	源码修改：umeditor.css第一行增加以下样式【解决pintuer.css样式box-sizing:border-box造成图片拖动缩放一直变小问题】
	.edui-scale{-webkit-box-sizing: content-box;-moz-box-sizing: content-box;box-sizing: content-box;}
5.SyntaxHighlighter 代码高亮插件核心文件，从UEditor中抽取出来自己修改的
6.uploadify3.2.1上传控件flash版本
7.webuploader0.1.5百度上传控件完全版包含flash和html5版本
8.echarts.min.js百度纯 Javascript 的图表库
9.jquery.masonry.min.js基于jquery的瀑布流插件
10.perfect-scrollbar v0.6.16滚动条插件
11.ztree v3.5.28

三、自定义工具
1.前端工具类zutil.js【依赖jquery、layer】

四、jar包依赖关系
1.jedis-2.6.1.jar【commons-pool2-2.4.2.jar】
2.ehcache-core-2.6.11.jar【slf4j-api-1.7.7.jar、slf4j-log4j12-1.7.12.jar】
3.jackson-dataformat-xml-2.7.3.jar【jackson-module-jaxb-annotations-2.7.3.jar、stax2-api-3.1.4.jar】
加上该包springmvc默认返回xml数据，要返回Json数据可指定Accept:application/json或后缀http://xx.json

