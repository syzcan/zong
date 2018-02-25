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
	<div style="float: left;">
	    <a class="easyui-linkbutton btn-green" data-options="iconCls:'fa fa-plus'" onclick="toCrawDetail('')">解析详情</a>
	    <a class="easyui-linkbutton btn-blue" data-options="iconCls:'fa fa-plus'" onclick="toCrawDetail('1')">解析详情JS</a>
	    <a class="easyui-linkbutton btn-cyan" data-options="iconCls:'fa fa-cloud-download'" onclick="crawDetail('')">同步</a>
	    <a class="easyui-linkbutton btn-yellow" data-options="iconCls:'fa fa-cloud-download'" onclick="crawDetail('1')">同步JS</a>
    </div>
    <form style="float: left;margin-left: 20px">
    	<select class="easyui-combobox" name="status" data-options="
    	onChange:function(){
    		$('#search').click();
    	}">
        	<option value="">状态</option>
        	<option value="1">未解析</option>
        	<option value="2">已解析</option>
        	<option value="3">解析失败</option>
        </select>
        <input id="ruleBox" class="easyui-combobox" name="craw_store"
    	data-options="valueField:'craw_store',textField:'name',
    	url:'${ctx}/craw/store/ruleBox.json',
    	onChange:function(row){
    		$('#search').click();
    	}
    	">
        <input type="text" name="keyword" class="easyui-textbox" data-options="prompt:'关键字',width:120">
        <a class="easyui-linkbutton btn-cyan" data-options="iconCls:'fa fa-search'" id="search">查询</a>
    </form>
    <div style="clear: both;"></div>
</div>
<!-- 数据网格 -->
<table id="dg" data-options="toolbar:'#toolbar'"></table>
<script>
        // 数据列表
        $("#dg").zdatagrid({
            url: '${ctx}/craw/store/datas.json',
            columns: [[
                {field: 'id', title: '主键', hidden: true},
                {field: 'title', title: '标题', formatter: function(value, row){
                	var img = '';
                	for(key in row){
                		if(/.*(gif|png|jpg)$/.test(row[key])){
                			img = '<img width="120" src="'+row[key]+'">';
                			break;
                		}
                	}
                	var html = '';
                	if(value){
                		var i=0;
	                	while(i<value.length){
	                		html += value.substring(i,i+10) +'<br>';
	                		i = i+10;
	                	}
                	}
                	return img + '<a onclick="toView(\''+ value +'\',\''+ row.id +'\')">'+html+'</a>';
                }},
                {field: 'url', title: '地址'},
                {field: 'status', title: '状态', formatter: function(value){
                	return value==1?'<span class="badge bg-yellow">未解析</span>':
               		(value==2?'<span class="badge bg-green">已解析</span>':'<span class="badge bg-red">解析失败</span>');
                }}
            ]],
            rownumbers :false,
            onLoadSuccess: function(){
        		$('.datagrid-btable').viewer();
            }
        });
     	// 查询数据
        $('#search').zbutton({
            operate: 'search'
        });
        function openTab(title,url){
        	parent.addTab({title:title,url:url});
        }
        function toCrawDetail(js_enabled){
        	var craw_store = $('#ruleBox').combobox('getValue');
        	var rule_id = '';
        	$.each($('#ruleBox').combobox('getData'),function(i,n){
        		if(n.craw_store == craw_store){
        			rule_id = n.id;
        			return false;
        		}
        	});
        	var index = layer.load(0);
        	openTab('解析详情','${ctx}/craw/toCrawTab?rule_id='+rule_id+'&craw_store='+craw_store+'&js_enabled='+js_enabled);
        	layer.close(index);
        }
        function toView(title,id){
        	var craw_store = $('#ruleBox').combobox('getValue');
        	var rule_id = '';
        	$.each($('#ruleBox').combobox('getData'),function(i,n){
        		if(n.craw_store == craw_store){
        			rule_id = n.id;
        			return false;
        		}
        	});
        	//location.href = '${ctx}/craw/store/view?rule_id='+rule_id+'&craw_store='+craw_store+'&id='+id;
        	openTab(title.length>6?title.substr(0,6):title,'${ctx}/craw/store/view?rule_id='+rule_id+'&craw_store='+craw_store+'&id='+id);
        }
        function crawDetail(js_enabled){
        	var rows = $('#dg').datagrid('getSelections');
    		if (rows.length == 0) {
    			$.messager.alert('提示', '请选择要操作的数据', 'warning');
    			return;
    		}
        	var craw_url = rows[0].url;
        	var craw_store = $('#ruleBox').combobox('getValue');
        	var rule_id = '';
        	$.each($('#ruleBox').combobox('getData'),function(i,n){
        		if(n.craw_store == craw_store){
        			rule_id = n.id;
        			return false;
        		}
        	});
        	layer.load(1);
        	$.get('${ctx}/craw/rule/data.json?id='+rule_id).done(function(data){
        		var rule = {};
        		rule.js_enabled = js_enabled;
        		rule.craw_url = craw_url;
        		rule.craw_store = craw_store;
        		$.each(data.rule.content_ext, function(i, n) {
        			rule[n.rule_ext_name] = n.rule_ext_css + ";"
        					+ n.rule_ext_type + "["+ n.rule_ext_reg + "];" + n.rule_ext_attr + ";" + n.rule_ext_mode;
        		});
        		$.ajax({
        			url:'${ctx}/craw/crawDetail.json',
        			type:'post',
        			data:rule,
        			dataType:'json',
        			success:function(data){
        				layer.closeAll();
        				if(data.statusCode==200){
        					layer.msg('操作成功');
        				}else{
        					layer.msg(data.message);
        				}
        			}
        		});
        	});
        }
</script>
</body>
</html>