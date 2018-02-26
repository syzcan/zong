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
        // 规则列表的表单项
        var columns = [
	 		{field:'rule_ext_name',title:'字段名英文',width:100,editor:'text'},
	 		{field:'rule_ext_css',title:'cssQuery',width:180,editor:'text'},
	 		{field:'rule_ext_type',title:'内容类型',width:68,editor:{
	 			type:'combobox',
	 			options:{
	 				valueField: 'v',
	 				textField: 'v',
	 				data:[{v:'text'},{v:'html'},{v:'attr'}],
	 				editable: false
	 			}
	 		}},
	 		{field:'rule_ext_attr',title:'属性attr',width:90,editor:{
	 			type:'combobox',
	 			options:{
	 				valueField: 'v',
	 				textField: 'v',
	 				data:[{v:'href'},{v:'src'},{v:'title'},{v:'value'},{v:'alt'},{v:'data-src'},{v:'content'}]
	 			}
	 		}},
	 		{field:'rule_ext_reg',title:'过滤正则',width:120,editor:'text'},
	 		{field:'rule_ext_mode',title:'模式',width:70,editor:{
	 			type:'combobox',
	 			options:{
	 				valueField: 'v',
	 				textField: 'v',
	 				data:[{v:'string'},{v:'array'}],
	 				editable: false
	 			}
	 		}},
	 		{field:'rule_ext_desc',title:'字段描述',width:100,editor:'text'},
	 		{field:'operate',title:'<span class="fa fa-plus text-green"></span>',width:20,formatter:function(v,row){
	 			if(row.rule_ext_name=='title' || row.rule_ext_name == 'url' || row.rule_ext_name == 'content'){
	 				return;
	 			}
	 			return '<span class="fa fa-minus text-red" data-name="'+row.rule_ext_name+'"></span>';
	 		}},
		];
        // 新增数据
        $('#add').zbutton({
			operate: 'add',
			title: '新增', 
			width: 900,
			height: 500,
			href: '${ctx}/craw/rule/toAdd',
			posturl: '${ctx}/craw/rule/add.json',
			onLoadHead: function(){
				$('#dg_list_ext').datagrid({
					columns:[columns],
					data: [],
					onLoadSuccess: function(){
						$(this).datagrid('appendRow',{rule_ext_name: 'title', rule_ext_type: 'text', rule_ext_desc: '标题', rule_ext_mode: 'string'}); 
						$(this).datagrid('appendRow',{rule_ext_name: 'url', rule_ext_type: 'text', rule_ext_desc: '详情地址', rule_ext_mode: 'string'}); 
						$(this).datagrid('beginEdit', 0);
						$(this).datagrid('beginEdit', 1);
					},
					onDblClickCell: function(index,field,value){
						$(this).datagrid('beginEdit', index);
						var ed = $(this).datagrid('getEditor', {index:index,field:field});
						if(field=='rule_ext_name'&&(value=='title' || value == 'url')){
							$(ed.target).attr('readonly','readonly');
			 			}else{
							$(ed.target).focus();
			 			}
					}
				});
				$('#dg_content_ext').datagrid({
					columns:[columns],
					data: [],
					onLoadSuccess: function(){
						$(this).datagrid('appendRow',{rule_ext_name: 'content', rule_ext_type: 'text', rule_ext_desc: '详情内容', rule_ext_mode: 'string'}); 
						$(this).datagrid('beginEdit', 0);
					},
					onDblClickCell: function(index,field,value){
						$(this).datagrid('beginEdit', index);
						var ed = $(this).datagrid('getEditor', {index:index,field:field});
						if(field=='rule_ext_name'&&value == 'content'){
							$(ed.target).attr('readonly','readonly');
			 			}else{
							$(ed.target).focus();
			 			}
					}
				});
			},
			onSubmit: function(){
				serializeExt();
				return true;
			}
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
				// 加载数据后赋值生成规则表单列表
				$('#dg_list_ext').datagrid({
					columns:[columns],
					data: data.list_ext,
					onDblClickCell: function(index,field,value){
						$(this).datagrid('beginEdit', index);
						var ed = $(this).datagrid('getEditor', {index:index,field:field});
						if(field=='rule_ext_name'&&(value=='title' || value == 'url')){
							$(ed.target).attr('readonly','readonly');
			 			}else{
							$(ed.target).focus();
			 			}
					}
				});
				$('#dg_content_ext').datagrid({
					columns:[columns],
					data: data.content_ext,
					onDblClickCell: function(index,field,value){
						$(this).datagrid('beginEdit', index);
						var ed = $(this).datagrid('getEditor', {index:index,field:field});
						if(field=='rule_ext_name'&&value == 'content'){
							$(ed.target).attr('readonly','readonly');
			 			}else{
							$(ed.target).focus();
			 			}
					}
				});
			},
			onSubmit: function(){
				serializeExt();
				return true;
			}
		});
        // 提交表单前序列化列表和内容规则为字符串，赋值到隐藏域
        function serializeExt(){
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
        }
        function addRule(dg){console.log(dg);
        	$(dg).datagrid('appendRow',{rule_ext_type: 'text', rule_ext_mode: 'string'});
        }
        $(document).on('click','#td_list_ext .fa-plus',function(){
			addRule('#dg_list_ext');
		});
        $(document).on('click','#td_content_ext .fa-plus',function(){
			addRule('#dg_content_ext');
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