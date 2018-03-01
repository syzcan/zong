zone项目所用框架和技术：

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
11.excel生成工具jstl-1.2.jar

二、前端框架和插件
1.EasyUI for jQuery 1.5.4.1	
2.Font Awesome 4.7.0 图标字体库
3.jQuery v1.11.3
4.layer-v3.0.3 Web弹层组件
5.umeditor1.2.2 百度富文本编辑器UEditor Mini版本【包含模板引擎etpl，已删除third-party中的公式和页面公式图标】
6.webuploader0.1.5 百度上传控件完全版包含flash和html5版本
7.SyntaxHighlighter 代码高亮插件核心文件，从UEditor中抽取出来自己修改的
8.Viewer v0.5.0 图片浏览器
9.ckplayer X 视频播放器
10.ffmpeg.exe 读取视频媒体信息，截图功能
11.Bootstrap v3.3.7【单独的展示页面使用】

三、自定义工具
1.easyui.zextend.js
     封装easyui基于datagrid通用的弹出层增删改等操作，通过绑定按钮实现【上传控件依赖webuploder和layer，富文本umeditor】
2.easyui/themes/zone【自定义主题美化】
3.zdb3.0.jar 【自定义数据库客户端，整合代码生成功能】

四、jar包依赖关系
1.jedis-2.6.1.jar【commons-pool2-2.4.2.jar】
2.ehcache-core-2.6.11.jar【slf4j-api-1.7.7.jar、slf4j-log4j12-1.7.12.jar】
3.jackson-dataformat-xml-2.7.3.jar【jackson-module-jaxb-annotations-2.7.3.jar、stax2-api-3.1.4.jar】
加上该包springmvc默认返回xml数据，要返回Json数据可指定Accept:application/json或后缀http://xx.json

