<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
</head>
<body class="easyui-layout" data-options="fit:true">
	<!-- 数据网格 -->
	<table id="dg" data-options="toolbar:'#toolbar'"></table>
	<script type="text/javascript">
		var rule = {
			js_enabled : '${param.js_enabled}',
			craw_store : '${param.craw_store}'
		};
		$.ajax({
			url : '${ctx}/craw/rule/data.json?id=${param.rule_id}',
			dataType : 'json',
			async : false,
			success : function(result) {
				$.each(result.data.content_ext, function(i, n) {
					rule[n.rule_ext_name] = n.rule_ext_css + ";" + n.rule_ext_type + "[" + n.rule_ext_reg + "];" + n.rule_ext_attr + ";"
							+ n.rule_ext_mode;
				});
			}
		});
		function loadDg() {
			$('#dg').zdatagrid({
				url : '${ctx}/craw/crawQueue?craw_store=${param.craw_store}',
				columns : [ [ {
					field : 'title',
					title : 'title'
				}, {
					field : 'url',
					title : '剩余总数:<span class="badge" id="total">0</span>',
					formatter : function(v, row) {
						return '<a href="'+row.url+'" target="blank">' + row.url + '</span>';
					}
				}, {
					field : 'status',
					title : '等待解析:<span class="badge" id="wait">0</span>',
					formatter : function(v, row) {
						return '<span class="badge bg-green" data-url="'+row.url+'">等待</span>';
					},
					width : 100
				} ] ],
				onLoadSuccess : function() {
					$('#total').html($('#dg').datagrid('getData').total);
					if ($('#dg').datagrid('getRows').length == 0) {
						layer.closeAll();
						layer.alert('没有需要解析的数据！', {
							shade : 0
						});
						return;
					}
					startDown();
				},
				pagination : false
			});
		}
		function startDown() {
			layer.closeAll();
			layer.load(1, {
				shade : 0
			});
			//没有可解析的重新加载
			if ($('span.bg-green').size() == 0) {
				loadDg();
				return;
			}
			$('#wait').html($('span.bg-green').size());
			var $cur = $('span.bg-green').first().removeClass('bg-green').addClass('bg-yellow').text('正在解析..');
			rule.craw_url = $cur.attr('data-url');
			var downer = $.ajax({
				url : '${ctx}/craw/crawDetail.json',
				type : 'post',
				data : rule,
				dataType : 'json',
				timeout : 10000, //超时时间设置，单位毫秒
				success : function(data) {
					if (data.statusCode == 200) {
						$('span.bg-yellow').removeClass('bg-yellow').addClass('bg-blue').text('已解析');
					} else {
						$('span.bg-yellow').removeClass('bg-yellow').addClass('bg-red').text('解析失败！');
					}
					startDown();
				},
				complete : function(XMLHttpRequest, status) { //请求完成后最终执行参数
					if (status == 'timeout') {//
						downer.abort();
						$('span.bg-yellow').removeClass('bg-yellow').addClass('bg-red').text('超时！');
						startDown();
					}
				}
			});

		}
		loadDg();
	</script>
</body>
</html>