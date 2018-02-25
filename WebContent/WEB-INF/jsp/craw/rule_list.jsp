<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<title>月光边境</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/WEB-INF/jsp/common/style_easyui.jsp"%>
<body>
<!-- 工具栏 -->
<div id="toolbar" class="easyui-toolbar">
    <a class="easyui-linkbutton btn-green" data-options="iconCls:'fa fa-plus'" id="add">新增</a>
    <a class="easyui-linkbutton btn-blue" data-options="iconCls:'fa fa-edit'" id="edit">编辑</a>
    <a class="easyui-linkbutton btn-red" data-options="iconCls:'fa fa-remove'" id="del">删除</a>
</div>
<!-- 数据网格 -->
<table id="dg" data-options="toolbar:'#toolbar'"></table>
<!-- 表单弹出层 -->
<div style="display: none">
	<div id="dialog" data-options="buttons:'#btns'"></div>
	<div id="btns">
		<a class="easyui-linkbutton btn-green" data-options="iconCls:'fa fa-save'" onclick="$('#form').submit()">保存</a>
    	<a class="easyui-linkbutton btn-red" data-options="iconCls:'fa fa-close'" onclick="$('#dialog').dialog('close')">关闭</a>
	</div>
</div>
<script>
        // 数据列表
        $("#dg").zdatagrid({
            url: '${ctx}/craw/rule/datas.json',
            columns: [[
                {field: 'id', title: '主键', hidden: true},
                {field: 'name', title: '名称'},
                {field: 'craw_store', title: '存储表'},
                {field: 'craw_url', title: '样本地址'},
                {field: 'craw_item', title: '条目规则'},
                {field: 'craw_next', title: '下一页规则'}
            ]],
            pagination:false
        });
        // 新增数据
        $('#add').zbutton({
			operate: 'add',
			title: '新增', 
			width: 900,
			height: 500,
			href: '${ctx}/craw/rule/toAdd',
			posturl: '${ctx}/craw/rule/add.json'
		});
        // 编辑选中数据
        $('#edit').zbutton({
			operate: 'edit',
			title: '编辑',
			width: 900,
			height: 500,
			href: '${ctx}/craw/rule/toEdit', 
			posturl: '${ctx}/craw/rule/edit.json', 
			dataurl: '${ctx}/craw/rule/data.json',
			pk: 'id',
			onLoadData: function(data){
				var columns = [
			 		{field:'rule_ext_name',title:'字段名英文',width:100,editor:'text'},
			 		{field:'rule_ext_css',title:'cssQuery',width:200,editor:'text'},
			 		{field:'rule_ext_type',title:'内容类型',width:65,editor:{
			 			type:'combobox',
			 			options:{
			 				valueField: 'v',
			 				textField: 'v',
			 				data:[{v:'text'},{v:'html'},{v:'attr'}]
			 			}
			 		}},
			 		{field:'rule_ext_reg',title:'过滤正则',width:120,editor:'text'},
			 		{field:'rule_ext_attr',title:'属性',width:90,editor:{
			 			type:'combobox',
			 			options:{
			 				valueField: 'v',
			 				textField: 'v',
			 				data:[{v:'href'},{v:'src'},{v:'title'},{v:'value'},{v:'alt'},{v:'data-src'},{v:'content'}]
			 			}
			 		}},
			 		{field:'rule_ext_mode',title:'模式',width:70,editor:{
			 			type:'combobox',
			 			options:{
			 				valueField: 'v',
			 				textField: 'v',
			 				data:[{v:'string'},{v:'array'}]
			 			}
			 		}},
			 		{field:'rule_ext_desc',title:'字段描述',width:100,editor:'text'},
				];
				$('#dg_list_ext').datagrid({
					columns:[columns],
					data: data.list_ext,
					onLoadSuccess: function(){
						$.each(data.list_ext,function(i){
							$('#dg_list_ext').datagrid('beginEdit', i);
						});
						$('#dg_list_ext').datagrid('resize'); 
					}
				});
				$('#dg_content_ext').datagrid({
					columns:[columns],
					data: data.content_ext,
					onLoadSuccess: function(){
						$.each(data.content_ext,function(i){
							$('#dg_content_ext').datagrid('beginEdit', i);
						});
						$('#dg_content_ext').datagrid('resize'); 
					}
				});
			},
			onSubmit: function(){
				$.each($('#dg_list_ext').datagrid('getRows'),function(i){
					$('#dg_list_ext').datagrid('endEdit', i);
				});
				$.each($('#dg_content_ext').datagrid('getRows'),function(i){
					$('#dg_content_ext').datagrid('endEdit', i);
				});
				var list_ext = JSON.stringify($('#dg_list_ext').datagrid('getRows'));
				var content_ext = JSON.stringify($('#dg_content_ext').datagrid('getRows'));
				$('#list_ext').val(list_ext);
				$('#content_ext').val(content_ext);
				return true;
			}
		});
        // 删除选中数据
        $('#del').zbutton({
            operate: 'del',
            posturl: '${ctx}/craw/rule/delete.json',
            pk: 'id'
        });
</script>
</body>
</html>