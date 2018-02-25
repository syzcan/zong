<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/WEB-INF/jsp/common/style_pintuer.jsp"%>
<div id="list_div">
	<table class="table text-default table-condensed">
		<thead>
			<tr>
				<th width="50">序号</th>
				<th width="300">title</th>
				<th width="300"><span class="badge bg-red">总数:${total }</span></th>
				<th width="80"><span class="badge bg-red" id="msg"></span></th>
			</tr>
		</thead>
		<tbody id="dataList">
			<c:forEach items="${stores }" var="store" varStatus="vs">
				<tr>
					<td>${vs.count }</td>
					<td>${fn:length(store.title)>30?fn:substring(store.title,0,30):store.title }</td>
					<td><a target="_blank" href="${store.url }">${fn:length(store.url)>45?fn:substring(store.url,0,45):store.url }</a></td>
					<td><span class="badge bg-green" data-url="${store.url }">等待</span></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<script type="text/javascript">
function startDown(){
	//没有可解析的跳到下一页
	if($('#dataList span.bg-green').size()==0){
		//$('.pagination li.active').next().children().click();
		location.href=location.href;
		return;
	}
	$('#msg').html('等待:'+$('#dataList span.bg-green').size());
	var $cur = $('#dataList span.bg-green').first().removeClass('bg-green').addClass('bg-yellow').text('正在解析..');
	$.get('${ctx}/craw/rule/data.json?id=${param.rule_id}').done(function(data){
		var rule = {};
		rule.js_enabled = '${param.js_enabled}';
		rule.craw_url = $cur.attr('data-url');
		rule.craw_store = '${param.craw_store}';
		$.each(data.rule.content_ext, function(i, n) {
			rule[n.rule_ext_name] = n.rule_ext_css + ";"
					+ n.rule_ext_type + "["+ n.rule_ext_reg + "];" + n.rule_ext_attr + ";" + n.rule_ext_mode;
		});
		var downer = $.ajax({
			url:'${ctx}/craw/crawDetail.json',
			type:'post',
			data:rule,
			dataType:'json',
			timeout : 10000, //超时时间设置，单位毫秒
			success:function(data){
				if(data.statusCode==200){
					$('#dataList span.bg-yellow').removeClass('bg-yellow').addClass('bg-blue').text('已解析');
				}else{
					$('#dataList span.bg-yellow').removeClass('bg-yellow').addClass('bg-red').text('解析失败！');
				}
				startDown();
			},
			complete : function(XMLHttpRequest,status){ //请求完成后最终执行参数
				if(status=='timeout'){//
					downer.abort();
					$('#dataList span.bg-yellow').removeClass('bg-yellow').addClass('bg-red').text('超时！');
					startDown();
				}
			}
		});
	});
	
}

if ('${fn:length(stores)}' != '0') {
	startDown();
	layer.load(1,{shade: 0});
}else{
	layer.alert('没有需要解析的数据！',{shade: 0})
}
</script>