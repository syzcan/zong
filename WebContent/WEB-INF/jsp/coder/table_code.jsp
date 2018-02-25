<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<title>月光边境</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/WEB-INF/jsp/common/style_easyui.jsp"%>
<link rel="stylesheet" href="${ctx }/plugins/SyntaxHighlighter/shCoreDefault.css">
<script type="text/javascript" src="${ctx }/plugins/SyntaxHighlighter/shCore.js"></script>
<style>
a.active{background: #0ae;color: #fff}
</style>
<body>
	<div id="bar" class="easyui-toolbar datagrid-toolbar">
		<div style="margin-bottom: 10px">
		包名：<input id="packageName" class="easyui-textbox" value="${table.packageName }">
		对象名：<input id="objectName" class="easyui-textbox" value="${table.objectName }">
		类名：<input id="className" class="easyui-textbox" value="${table.className }">
		</div>
		<div style="position: relative;">
		<c:forEach items="${templates }" var="template">
		<a class="easyui-linkbutton btn-blue" data-name="${template }" data-type="${template.indexOf('Xml')>-1?'xml':(template.indexOf('jsp')>-1?'html':'java') }">${template }</a>
		</c:forEach>
		<div style="position: absolute;right: 30px;bottom: -50px;z-index: 666">
			<a class="easyui-linkbutton btn-yellow" title="下载全部代码" data-options="iconCls:'fa fa-cloud-download'"></a>
			<a class="easyui-linkbutton btn-green" title="复制当前代码" data-options="iconCls:'fa fa-copy'"></a>
		</div>
		</div>
	</div>
	<div id="code_wrap" style="position: fixed;width: 100%;overflow: auto;">
		<div id="preview">
		</div>
	</div>
</body>
<script>
$(function(){
	$('.easyui-linkbutton[data-name]').click(function(){
		$(this).addClass('active').siblings().removeClass('active');
		var pre = '<pre class="brush:'+$(this).data('type')+';toolbar:false;quick-code:false">';
		$.ajax({
			url:'${ctx}/coder/tables/${param.tableName}/code/'+$(this).data('name')+'?dbname=${param.dbname}',
			type:'post',
			dataType:'text',
			data:{
				objectName:$.trim($('#objectName').textbox('getValue')),
				className:$.trim($('#className').textbox('getValue')),
				packageName:$.trim($('#packageName').textbox('getValue'))
			},
			success:function(data){
				//特殊字符转义
				data = data.replace(/</g,"&lt;").replace(/>/g,"&gt;");
				$('#preview').html(pre+data+'</pre>');
				//代码高亮
				SyntaxHighlighter.highlight();
			}
		});
	});
	$('.fa-cloud-download').click(function(){
		location.href='${ctx}/coder/tables/${param.tableName}/downCode?dbname=${param.dbname}&objectName='+$.trim($('#objectName').val())+'&className='+$.trim($('#className').val())+'&packageName='+$.trim($('#packageName').val());
	});
	$('.fa-copy').click(function(){
		selectText();
	});
	//选中代码div并复制到粘贴板
	function selectText() {
	    var text = $('.container')[0];
	    if (document.body.createTextRange) {
	        var range = document.body.createTextRange();
	        range.moveToElementText(text);
	        range.select();
	    } else if (window.getSelection) {
	        var selection = window.getSelection();
	        var range = document.createRange();
	        range.selectNodeContents(text);
	        selection.removeAllRanges();
	        selection.addRange(range);
	    } else {
	        alert("none");
	    }
	    document.execCommand("Copy"); //执行浏览器复制命令
	}
	$('.easyui-linkbutton[data-name]').first().click();
	// 失去焦点事件
	$('.textbox input').blur(function(){
		$('a.active').click();
	});
	autoHeight();
});
$(window).resize(function() {
	autoHeight();
});
function autoHeight(){
	$('#code_wrap').css('top',$('#bar').height()+11);
	$('#code_wrap').height(parent.$('#z-tabs').height()-40-$('#bar').height());
}
</script>
</html>