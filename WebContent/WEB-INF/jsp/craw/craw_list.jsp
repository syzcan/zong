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
</style>
<body class="easyui-layout" data-options="fit:true">
	<!-- 工具栏 -->
	<div id="toolbar" class="easyui-toolbar datagrid-toolbar">
		<form id="form">
			<select id="list-rule" class="easyui-combobox"
				data-options="required:true,editable:false,
			    onSelect:function(row){
			    	current_rule = null;
			    	$(this).val(row.value);
			    	$('#craw_url').textbox('setValue',$(this).find('option:selected').data('url'));
			    }">
				<c:forEach items="${rules }" var="rule">
					<option data-url="${rule.craw_url }" value="${rule.id }">${rule.name }</option>
				</c:forEach>
			</select> 
			<select id="js_enabled" class="easyui-combobox"
				data-options="editable:false">
				<option value="">禁用JS解释器</option>
				<option value="1">启用JS解释器</option>
			</select> 
			<input
				data-options="prompt:'爬取列表地址',required:true,validType:'url',width:400"
				class="easyui-textbox" id="craw_url" /> 
			<a class="easyui-linkbutton btn-green" data-options="iconCls:'fa fa-send'" onclick="craw()">解析</a>
		</form>
	</div>
	<!-- 数据网格 -->
	<table id="dg" data-options="toolbar:'#toolbar'"></table>
	<script>
		var current_rule = null;
		function craw() {
			if (!$('#form').form('validate')) {
				return;
			}
			layer.load(1);
			if (current_rule == null) {
				$.ajax({
					url : '${ctx}/craw/rule/data.json?id=' + $('#list-rule').combobox('getValue'),
					dataType : 'json',
					async : false,
					success : function(result) {
						current_rule = result.data;
					}
				});
			}
			var rule = {
				js_enabled : $('#js_enabled').combobox('getValue'),
				craw_url : $('#craw_url').textbox('getValue'),
				craw_item : current_rule.craw_item,
				craw_next : current_rule.craw_next
			};
			//rule.craw_store = current_rule.craw_store;
			$.each(current_rule.list_ext.concat(current_rule.content_ext), function(i, n) {
				rule[n.rule_ext_name] = n.rule_ext_css + ";" + n.rule_ext_type + "[" + n.rule_ext_reg + "];" + n.rule_ext_attr + ";"
						+ n.rule_ext_mode;
			});
			$.post('${ctx}/craw/crawList.json', rule, function(result) {
				if (result.retCode == 200) {
					$('#dg').zdatagrid({
						columns : [ [ {
							field : 'title',
							title : 'title'
						}, {
							field : 'url',
							title : 'url'
						} ] ],
						data : result.craw.data,
						pagination : false
					});
					if (result.craw.craw_next != null) {
						$('#craw_url').data('next', result.craw.craw_next);
					}
				}
				layer.close(layer.index);
				if ($('#craw_url').data('next') == '') {
					$.messager.alert('提示', '没有下一页', 'warning');
					return;
				}
				$('#craw_url').textbox('setValue', $('#craw_url').data('next'));
				// 更新下一页地址，继续爬取
				craw();
			});
		}
	</script>
</body>
</html>