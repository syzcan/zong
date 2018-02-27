<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<title>月光边境</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/WEB-INF/jsp/common/style_easyui.jsp"%>
<style>
.validatebox-readonly {
	background: none;
}
.tabs-panels .panel-body{overflow: hidden;}
</style>
</head>
<body class="easyui-layout" data-options="fit:true">
	<div id="tt" class="easyui-tabs" data-options="tools:'#tab-tools',border:false,width:'100%',height:'100%'">
	</div>
	<div id="tab-tools">
		<a class="easyui-linkbutton" data-options="plain:true,iconCls:'fa fa-plus text-green'" onclick="addPanel()"></a>
	</div>
	<script type="text/javascript">
		var index = 0;
		function addPanel(){
			index++;
			$('#tt').tabs('add',{
				title: '线程'+index,
				content: parent.createFrame('${ctx }/craw/toCrawDetail?rule_id=${param.rule_id }&craw_store=${param.craw_store}&js_enabled=${param.js_enabled}'),
				closable:index>1
			});
		}
		$(function(){
			addPanel();
		});
	</script>
</body>
</html>