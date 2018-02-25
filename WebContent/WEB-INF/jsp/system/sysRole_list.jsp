<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<title>月光边境</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/WEB-INF/jsp/common/style_easyui.jsp"%>
<body class="easyui-layout" data-options="fit:true">
<div data-options="region:'west',split:true,border:false,width:'50%'">
	<!-- 工具栏 -->
	<div id="toolbar" class="easyui-toolbar">
        <a class="easyui-linkbutton btn-green" data-options="iconCls:'fa fa-plus'" id="add">新增</a>
        <a class="easyui-linkbutton btn-blue" data-options="iconCls:'fa fa-edit'" id="edit">编辑</a>
        <a class="easyui-linkbutton btn-red" data-options="iconCls:'fa fa-remove'" id="del">删除</a>
        <a class="easyui-linkbutton btn-yellow" data-options="iconCls:'fa fa-save'" id="save">保存菜单</a>
	</div>
	<!-- 数据网格 -->
	<table id="dg" data-options="toolbar:'#toolbar'"></table>
</div>
<div data-options="region:'center',border:false">
	<ul id="menu_tree" style="padding: 10px"></ul>
</div>
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
            url: '${ctx}/system/role/datas.json',
            columns: [[
                {field: 'id', title: '主键', hidden: true},
                {field: 'name', title: '名称'},
                {field: 'remark', title: '备注'},
                {field: 'createTime', title: '创建时间'}
            ]],
            pagination : false,
            onSelect: function(i,row){
            	loadMenu(row.id);
            }
        });
        // 新增数据
        $('#add').zbutton({
            operate: 'add',
            width: 400,
            height: 200,
            href: '${ctx}/system/role/toAdd',
            posturl: '${ctx}/system/role/add.json'
        });
        // 编辑选中数据
        $('#edit').zbutton({
            operate: 'edit',
            width: 400,
            height: 200,
            href: '${ctx}/system/role/toEdit',
            posturl: '${ctx}/system/role/edit.json',
            dataurl: '${ctx}/system/role/data.json'
        });
        // 删除选中数据
        $('#del').zbutton({
            operate: 'del',
            posturl: '${ctx}/system/role/delete.json'
        });
        // 查询数据
        $('#search').zbutton({
            operate: 'search'
        });
        
		function loadMenu(roleId) {
			$("#menu_tree").tree({
				id : 'menu_tree',
				idField : 'id',
				treeField : 'text',
				checkbox : true,
				url : '${ctx }/system/menu/datas.json?roleId=' + roleId,
				onLoadSuccess : function() {
					$("#menu_tree").tree('expandAll');
				}
			});
		}
		
		$('#save').click(function() {
			var row = $("#dg").datagrid('getSelected');
			if (!row) {
				$.messager.alert('提示', '请选择角色', 'warning');
				return;
			}
			var menus = $("#menu_tree").tree('getChecked',
					[ 'checked', 'indeterminate' ]);
			if (menus.length == 0) {
				$.messager.alert('提示', '请选择菜单', 'warning');
				return;
			}
			var data = {
				id : row.id
			};
			$.each(menus, function(i, n) {
				data['menus[' + i + '].id'] = n.id;
			});
			$.ajax({
				url : '${ctx}/system/role/saveRoleMenu.json',
				type : 'post',
				dataType : 'json',
				data : data,
				success : function(data) {
					if (data.statusCode == 200) {
						$.messager.show({
							title : data.title,
							msg : data.message,
							timeout : 2000,
							showType : 'slide'
						});
						$("#dg").datagrid('reload');
					} else {
						$.messager.alert(data.title, data.message, 'error');
					}
				}
			});
		});
</script>
</body>
</html>