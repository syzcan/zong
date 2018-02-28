<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<title>月光边境</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/WEB-INF/jsp/common/style_easyui.jsp"%>
<link rel="stylesheet" href="${ctx }/plugins/viewer/viewer.min.css" />
<script type="text/javascript" src="${ctx}/plugins/viewer/viewer.min.js"></script>
<body>
<!-- 工具栏 -->
<div id="toolbar" class="easyui-toolbar">
	<a class="easyui-linkbutton btn-green zuploadbt" data-options="iconCls:'fa fa-cloud-upload',
	uploadType:'picture',
	multiple:true,
	success: function(result){
		if(result.retCode == 200){
			// 上传成功保存，然后刷新列表
			$.post(ctx+'/media/photoItem/add.json',{name:result.fileName,url:result.filePath,pid:'${param.id }'},function(result){
				$('#dg').datagrid('reload');
			})
		}
	}
	">上传</a>
	<a class="easyui-linkbutton btn-blue" data-options="iconCls:'fa fa-save'" id="save">保存</a>
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
            url: '${ctx}/media/photoItem/datas.json?pid=${param.id }',
            columns: [[
                {field: 'id', title: '主键', checkbox: true},
                {field: 'url', title: '图片', width: 200, formatter: function(value, row){
                	return '<img style="max-width:200px" src="${ctx}/'+ value +'" alt="'+row.name+'">';
                }},
                {field: 'name', title: '名称', editor:'text'},
                {field: 'remark', title: '备注', editor:'text'},
                {field: 'createTime', title: '创建时间'}
            ]],
            rownumbers : false,
            singleSelect : false,
            onDblClickRow: function(index, row){
            	$('#dg').datagrid('beginEdit', index);
            },
            onLoadSuccess: function(){
            	//相册调用
        		/* layer.photos({
        			photos : '.datagrid-btable'
        		}); */
        		$('.datagrid-btable').viewer();
            }
        });
		// 删除选中数据
		$('#del').zbutton({
			operate: 'del',
			posturl: '${ctx}/media/photoItem/delete.json',
			pk: 'id'
		});
		$('#save').click(function(){
			$('#dg').datagrid('acceptChanges');
			var data = {};
			$.each($('#dg').datagrid('getRows'), function(i, n){
				data['items[' + i + '].id'] = n.id;
				if(n.name){
					data['items[' + i + '].name'] = n.name;
				}
				if(n.remark){
					data['items[' + i + '].remark'] = n.remark;
				}
			});
			$.post(ctx + '/media/photoItem/edit.json', data, function(result){
				if (result.retCode == 200) {
					$.messager.show({
						title : '提示',
						msg : '操作成功',
						timeout : 2000,
						showType : 'slide'
					});
					$('#dg').datagrid('reload');
				} else {
					$.messager.alert('提示', result.retMsg, 'warning');
				}
			});
		});

</script>
</body>
</html>