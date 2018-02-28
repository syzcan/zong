<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<title>月光边境</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/WEB-INF/jsp/common/style_easyui.jsp"%>
<body class="easyui-layout" data-options="fit:true">
<div data-options="region:'west',split:true,border:false,width:'10%'">
	<table id="dg1" data-options="toolbar:'#toolbar1'"></table>
	<div id="toolbar1" class="easyui-toolbar" style="height: 27px;padding: 2px 0 0 5px">
		<a class="easyui-menubutton" data-options="menu:'#plug',iconCls:'fa fa-plug',plain:true">连接</a>
	</div>
	<div id="plug" style="width:80px;">
		<div data-options="iconCls:'fa fa-refresh text-yellow'" id="reconnect">打开或重连</div>
		<div data-options="iconCls:'fa fa-plus text-green'" id="add">新建</div>
		<div data-options="iconCls:'fa fa-edit text-blue'" id="edit">编辑</div>
		<div data-options="iconCls:'fa fa-remove text-red'" id="del">删除</div>
	</div>
</div>
<div data-options="region:'center',split:true,border:false,width:'40%'">
	<table id="dg2" data-options="toolbar:'#toolbar2'"></table>
	<div id="toolbar2" class="easyui-toolbar">
	</div>
</div>
<div data-options="region:'east',split:true,border:false,width:'50%'">
	<table id="dg3" data-options="toolbar:'#toolbar3'"></table>
	<div id="toolbar3" class="easyui-toolbar">
	</div>
</div>
<!-- 表单弹出层 -->
<div style="display: none">
	<div id="dialog" data-options="buttons:'#btns'">
		<form id="form" method="post">
			<table class="table-form">
				<tr>
					<td align="right">dbname：</td>
					<td>
						<input id="dbname" name="dbname" class="easyui-textbox" data-options="prompt:'连接唯一标识名称',required:true,width:250" />
					</td>
				</tr>
				<tr>
					<td align="right">driverClassName：</td>
					<td>
						<select class="easyui-combobox" name="jdbc.driverClassName" data-options="required:true,editable:false,width:250">
				        	<option value="oracle.jdbc.driver.OracleDriver">oracle.jdbc.driver.OracleDriver</option>
				        	<option value="com.mysql.jdbc.Driver">com.mysql.jdbc.Driver</option>
				        </select>
					</td>
				</tr>
				<tr>
					<td align="right">jdbcurl：</td>
					<td>
						<input name="jdbc.url" class="easyui-textbox" data-options="required:true,width:250" />
					</td>
				</tr>
				<tr>
					<td align="right">username：</td>
					<td>
						<input name="jdbc.username" class="easyui-textbox" data-options="required:true,width:250" />
					</td>
				</tr>
				<tr>
					<td align="right">password：</td>
					<td>
						<input name="jdbc.password" class="easyui-textbox" data-options="required:true,width:250" />
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div id="btns">
		<a class="easyui-linkbutton btn-green" data-options="iconCls:'fa fa-save'" onclick="$('#form').submit()">保存</a>
    	<a class="easyui-linkbutton btn-red" data-options="iconCls:'fa fa-close'" onclick="$('#dialog').dialog('close')">关闭</a>
	</div>
</div>
<script>
    	// 数据列表
        $("#dg1").zdatagrid({
            url: '${ctx}/coder/dbs.json',
            columns: [[
                {field: 'dbname', title: '连接名', formatter: function(value){
                	return '<span class="fa fa-database"></span>&nbsp;' + value;
                }}
            ]],
            onLoadSuccess: function(){
            	$("#dg1").datagrid('selectRow',0);
            },
            onSelect : function(i, row){
            	showDb(row.dbname);
            },
            pagination : false
        }); 
        function showDb(dbname){
        	$('#toolbar2').html('所有表格&nbsp;'+ dbname);
	        $("#dg2").zdatagrid({
	            url: '${ctx}/coder/tables.json?dbname='+dbname,
	            columns: [[
	                {field: 'tableName', title: '表名', formatter: function(value){
	                	return '<span class="fa fa-table"></span>&nbsp;' + value;
	                }},
	                {field: 'comment', title: '备注', width: 120},
	                {field: 'totalResult', title: '行'},
	                {field: 'code', title: '操作', width: 65, formatter: function(value, row){
	                	return '<a class="easyui-linkbutton btn-blue" title="查看数据" data-options="iconCls:\'fa fa-eye\'" data-table="'+row.tableName+'"></a>'
	                	+ '&nbsp;<a class="easyui-linkbutton btn-green" title="查看代码" data-options="iconCls:\'fa fa-code\'" data-table="'+row.tableName+'"></a>';
	                }}
	            ]],
	            onLoadSuccess: function(){
	            	$.parser.parse('td[field="code"]');
	            	$('td[field="code"] .easyui-linkbutton').click(function(){
	            		if($(this).find('.fa-code').length>0){
	            			parent.addTab({
		        				title : '代码-'+$(this).data('table'),
		        				url : '${ctx}/coder/tables/code?dbname='+dbname+'&tableName='+$(this).data('table'),
		        				iconCls : 'fa fa-code'
		        			});
	            		}else{
		            		parent.addTab({
		        				title : '数据-'+$(this).data('table'),
		        				url : '${ctx}/coder/tables/data?dbname='+dbname+'&tableName='+$(this).data('table'),
		        				iconCls : 'fa fa-eye'
		        			});
	            		}
	            	});
	            	$("#dg2").datagrid('selectRow',0);
	            },
	            onSelect : function(i, row){
	            	showTable(dbname,row.tableName);
	            },
	            pagination : false
	        });
        }
    	
    	function showTable(dbname,tableName){
    		$('#toolbar3').html('表详情&nbsp;'+ tableName);
    		$("#dg3").zdatagrid({
                url: '${ctx}/coder/tables/'+ tableName +'.json?dbname='+dbname,
                columns: [[
                    {field: 'field', title: '字段', formatter: function(value, row){
                    	return '<span class="'+(row.key=='PRI'?'text-red':'')+'">'+value+'</span>';
                    }},
                    /*{field: 'jdbcType', title: 'jdbcType', formatter: function(value){
                    	return '<span class="text-blue">'+value+'</span>';
                    }},
                    {field: 'javaType', title: 'java类型', formatter: function(value){
                    	return '<span class="text-green">'+value+'</span>';
                    }},*/
                    {field: 'columnType', title: '类型'},
                    {field: 'dataLength', title: '长度', formatter: function(value, row){
                    	return row.jdbcType!='DECIMAL' && row.jdbcType!='INTEGER'?value:(row.dataPrecision + ',' + row.dataScale);
                    }},
                    {field: 'canNull', title: '必填', formatter: function(value){
                    	return value=='NO'?'<span style="font-weight:bold;color:red">√</span>':'';
                    }},
                    {field: 'key', title: '主键', formatter: function(value){
                    	return '<span style="font-weight:bold;color:red">'+(value=='PRI'?value:'')+'</span>';
                    }},
                    {field: 'defaultValue', title: '默认值'},
                    {field: 'remark', title: '备注'}
                ]],
                pagination : false
            });
    	}
    	
    	$(function(){
    		$('#reconnect').click(function(){
    			$.get('${ctx}/coder/dbs/reconnect.json',function(result){
    				if (result.retCode == 200) {
    					$.messager.show({
    						title : '提示',
    						msg : '操作成功',
    						timeout : 2000,
    						showType : 'slide'
    					});
    				} else {
    					$.messager.alert('提示', result.retMsg, 'warning');
    				}
    			});
    		});
    		$('#add').zbutton({
    			title: '新建连接',
    			operate: 'add',
    			posturl: '${ctx}/coder/dbs/add.json',
    			datagrid: '#dg1',
    			onOpenHead: function(row){
    				$('#dbname').next().find('input').removeAttr('readonly');
    				$('#form').find(':input[textboxname]').textbox('setValue','');
    			},
    			onSubmit: function(){
    				var flag = true;
    				var dbname = $('#form input[textboxname="dbname"]').textbox('getValue');
    				$.each($('#dg1').datagrid('getRows'), function(i, row){
    					if(row.dbname == dbname){
    						$.messager.alert('提示', dbname+' 已存在', 'warning');
	    					flag = false;
	    					return false;
    					}
    				});
    				return flag;
    			}
    		});
    		$('#edit').zbutton({
    			title: '编辑连接',
    			operate: 'edit',
    			posturl: '${ctx}/coder/dbs/edit.json',
    			datagrid: '#dg1',
    			selectRowCheck: function(row){
    				if(row.dbname == 'current'){
    					$.messager.alert('提示', '当前数据库不可操作', 'warning');
    					return false;
    				}
    				return true;
    			},
    			onOpenHead: function(row){
    				$('#dbname').next().find('input').attr('readonly','true');
    				$('#form').find(':input[textboxname]').textbox('setValue','');
    				for(key in row){
	    				$('#form').find(':input[textboxname="'+key+'"]').textbox('setValue',row[key]);
    				}
    			}
    		});
    		$('#del').zbutton({
    			operate: 'del',
    			posturl: '${ctx}/coder/dbs/del.json',
    			datagrid: '#dg1',
    			selectRowCheck: function(rows){
    				var flag = true;
    				$.each(rows, function(i, row){
	    				if(row.dbname == 'current'){
	    					$.messager.alert('提示', '当前数据库不可操作', 'warning');
	    					flag = false;
	    					return false;
	    				}
    				});
    				return flag;
    			},
    			pk:'dbname'
    		});
    	});
    	
</script>
</body>
</html>