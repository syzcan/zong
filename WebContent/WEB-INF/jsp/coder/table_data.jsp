<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<title>月光边境</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/WEB-INF/jsp/common/style_easyui.jsp"%>
<style type="text/css">
.datagrid-view .datagrid-editable-input {
	border: 0
}

.datagrid-cell {
	padding: 0
}

.datagrid-row-selected .datagrid-cell-rownumber {
	font-weight: bold;
}

.datagrid-row-selected .data-field {
	background: #EBF2FC
}
</style>
<body>
	<!-- 数据网格 -->
	<table id="dg"></table>
	<script>
		$.ajax({
			url : '${ctx}/coder/tables/${param.tableName}.json?dbname=${param.dbname}',
			dataType : 'json',
			asnyc : false,
			success : function(data) {
				var columns = [];
				$.each(data.rows, function(i, n) {
					columns.push({
						field : n.column,
						title : n.column,
						editor : 'text',
						sortable : true,
						width : 120,
						formatter : function(value) {
							if (value) {
								value = html_encode(value);
							} else {
								value = '';
							}
							value = '<input type="text" class="data-field" value="' + value + '" style="width: 100%; height: 26px;border:0">';
							return value;
						}
					});
				});
				// 数据列表
				$("#dg").zdatagrid({
					url : '${ctx}/coder/tables/${param.tableName}/datas.json?dbname=${param.dbname}&',
					columns : [ columns ],
					fitColumns : false,
					pageList:[10,20,30,40,50,100]
				});
			}
		});
		// 转义特殊字符，不然输入框显示错误
		function html_encode(str) {
			var s = "";
			if (str.length == 0)
				return "";
			s = str.replace(/&/g, "&gt;");
			s = s.replace(/</g, "&lt;");
			s = s.replace(/>/g, "&gt;");
			s = s.replace(/ /g, "&nbsp;");
			s = s.replace(/\'/g, "&#39;");
			s = s.replace(/\"/g, "&quot;");
			s = s.replace(/\n/g, "<br>");
			return s;
		}
	</script>
</body>
</html>