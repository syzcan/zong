<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<title>月光边境</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/WEB-INF/jsp/common/style_easyui.jsp"%>
<link rel="stylesheet" href="${ctx }/plugins/SyntaxHighlighter/shCoreDefault.css">
<script type="text/javascript" src="${ctx }/plugins/SyntaxHighlighter/shCore.js"></script>
<script type="text/javascript" src="${ctx }/plugins/jquery/jquery.format.js"></script>
<script type="text/javascript" src="${ctx }/plugins/jquery/json-format.js"></script>
<style>.validatebox-readonly{background: none;}</style>
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
		<select class="easyui-combobox" data-options="width:60,editable:false" id="test_type">
        	<option value="list">列表</option>
        	<option value="content">内容</option>
        </select>
		<input class="easyui-textbox easyui-validatebox" data-options="prompt:'列表或详情页地址',width:350,validType:'url'" id="test_text" />
		<a class="easyui-linkbutton btn-blue" data-options="iconCls:'fa fa-send'" id="test_bt">测试</a>
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
	 		{field:'rule_ext_name',title:'字段名英文',width:100,editor:{type:'textbox', options:{required:true,validType:'rule_ext_name'}}},
	 		{field:'rule_ext_css',title:'cssQuery',width:180,editor:'textbox', options:{required:true}},
	 		{field:'rule_ext_type',title:'内容类型',width:65,editor:{
	 			type:'combobox',
	 			options:{
	 				valueField: 'v',
	 				textField: 'v',
	 				data:[{v:'text'},{v:'html'},{v:'attr'}],
	 				editable: false,
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
	 		{field:'rule_ext_reg',title:'过滤正则',width:120,editor:'textbox'},
	 		{field:'rule_ext_mode',title:'模式',width:70,editor:{
	 			type:'combobox',
	 			options:{
	 				valueField: 'v',
	 				textField: 'v',
	 				data:[{v:'string'},{v:'array'}],
	 				editable: false
	 			}
	 		}},
	 		{field:'rule_ext_desc',title:'字段描述',width:100,editor:'textbox'},
	 		{field:'operate',title:'<span class="fa fa-plus-circle text-green"></span>',width:20,formatter:function(v,row){
	 			if(row.rule_ext_name=='title' || row.rule_ext_name == 'url' || row.rule_ext_name == 'content'){
	 				return;
	 			}
	 			return '<span class="fa fa-minus-circle text-red"></span>';
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
						// 新增自动添加必须的title和url
						$(this).datagrid('appendRow',{rule_ext_name: 'title', rule_ext_type: 'text', rule_ext_desc: '标题', rule_ext_mode: 'string'}); 
						$(this).datagrid('appendRow',{rule_ext_name: 'url', rule_ext_type: 'text', rule_ext_desc: '详情地址', rule_ext_mode: 'string'}); 
						editRow($(this).datagrid('getRows')[0], $(this));
						editRow($(this).datagrid('getRows')[1], $(this));
					},
					onClickRow: function(index,row){
						editRow(row, $(this));
					}
				});
				$('#dg_content_ext').datagrid({
					columns:[columns],
					data: [],
					onLoadSuccess: function(){
						// 新增自动添加必须的content
						$(this).datagrid('appendRow',{rule_ext_name: 'content', rule_ext_type: 'text', rule_ext_desc: '详情内容', rule_ext_mode: 'string'}); 
						editRow($(this).datagrid('getRows')[0], $(this));
					},
					onClickRow: function(index,row){
						editRow(row, $(this));
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
					onClickRow: function(index,row){
						editRow(row, $(this), 'edit');
					}
				});
				$('#dg_content_ext').datagrid({
					columns:[columns],
					data: data.content_ext,
					onClickRow: function(index,row){
						editRow(row, $(this), 'edit');
					}
				});
				$('#test_text').textbox('setValue','');
			},
			onSubmit: function(){
				// 校验表单
				if(!$('#form').form('validate')){
					return false;
				}
				serializeExt();
				return true;
			}
		});
        function editRow(row, $obj, type){
        	var col = $obj.datagrid('getColumnOption', 'rule_ext_name');
			col.editor1 = col.editor;
			if(type || row.rule_ext_name == 'title' || row.rule_ext_name == 'url' || row.rule_ext_name == 'content'){
				col.editor = null;
			}
			$obj.datagrid('beginEdit', $obj.datagrid('getRowIndex',row));
			col.editor = col.editor1;
        }
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
        // 添加规则项
        $(document).on('click','td[data-dg] .fa-plus-circle',function(){
        	var dg = $(this).parents('td[data-dg]').data('dg');
        	$(dg).datagrid('appendRow',{rule_ext_type: 'text', rule_ext_mode: 'string'});
        	$(dg).datagrid('beginEdit',$(dg).datagrid('getRows').length - 1);
        	$(dg).datagrid('resize');
		});
        // 删除规则项
        $(document).on('click','td[data-dg] .fa-minus-circle',function(event){
        	var dg = $(this).parents('td[data-dg]').data('dg');
        	var i = $(this).parents('tr.datagrid-row').attr('datagrid-row-index');
        	$(dg).datagrid('deleteRow',i);
        	$(dg).datagrid('resize');
        	event.stopPropagation();
		});
        // 删除选中数据
        $('#del').zbutton({
            operate: 'del',
            posturl: '${ctx}/craw/rule/delete.json',
            pk: 'id'
        });
        // 校验名称重复
        $.extend($.fn.validatebox.defaults.rules, {
            rule_ext_name: {
        		validator: function(value){
        			function countName(dg){
        				var count = 0;
            			$.each($(dg).datagrid('getRows'),function(i, row){
            				var ed = $(dg).datagrid('getEditor', {index:i,field:'rule_ext_name'});
            				if(ed){
            					if($(ed.target).textbox('getValue') == value){
                					count++;
                				}
            				}else{
            					if(row.rule_ext_name == value){
                					count++;
                				}
            				}
            			});
            			return count;
        			}
        			return countName('#dg_list_ext')+countName('#dg_content_ext')<=1;
        		},
        		message: '已存在'
            }
        });
        // 测试规则
        $('#test_bt').click(function(){
        	var url = $('#test_text').textbox('getValue');
        	if(url==''){
        		$('#test_text').textbox('textbox').focus();
        		return;
        	}
        	if(!$('#test_text').validatebox('isValid')){
        		$('#test_text').textbox('textbox').focus();
        		return;
        	}
        	if(!$('#form').form('validate')){
        		return;
			}
        	serializeExt();
        	layer.load(1);
        	var data = {craw_url:url,craw_item:$('#craw_item').textbox('getValue'),craw_next:$('#craw_next').textbox('getValue')};
        	if($('#test_type').combobox('getValue')=='list'){
	        	$.each($('#dg_list_ext').datagrid('getRows'),function(i, row){
	        		data[row.rule_ext_name] = row.rule_ext_css + ";"
					+ row.rule_ext_type + "["+ row.rule_ext_reg + "];" + row.rule_ext_attr + ";" + row.rule_ext_mode;
	        	});
	        	$.post('${ctx}/craw/crawList.json',data,function(result){
	        		code('列表信息:'+url,result);
	        	});
        	}else{
	        	$.each($('#dg_content_ext').datagrid('getRows'),function(i, row){
	        		data[row.rule_ext_name] = row.rule_ext_css + ";"
					+ row.rule_ext_type + "["+ row.rule_ext_reg + "];" + row.rule_ext_attr + ";" + row.rule_ext_mode;
	        	});
	        	$.post('${ctx}/craw/data.json',data,function(result){
	        		code('内容信息:'+url,result);
	        	});
        	}
        	function code(title,result){
        		code = $.format(result, {method : 'json'});
    			$('#preview').html('<pre class="brush:java;toolbar:false;quick-code:false">'+code+'</pre>');
    			SyntaxHighlighter.highlight();
        		$('#dialog_result').dialog({title:title,width:'100%',height:'100%',modal:true});
        		layer.closeAll();
        	}
        });
</script>
<div id="dialog_result">
	<div id="preview"></div>
</div>
</body>
</html>