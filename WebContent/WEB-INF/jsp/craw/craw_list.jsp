<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/style_pintuer.jsp"%>
<div class="padding-big">
	<div class="margin-bottom">
		<select id="list-rule" class="input input-auto">
			<option value="">--爬取规则--</option>
			<c:forEach items="${rules }" var="rule">
				<option data-url="${rule.craw_url }" value="${rule.id }">${rule.name }</option>
			</c:forEach>
		</select> 
		<select id="js_enabled" class="input input-auto">
			<option value="">禁用JS解释器</option>
			<option value="1">启用JS解释器</option>
		</select> 
		<input type="text" placeholder="爬取列表地址" class="input input-auto"
			name="craw_url" data-next="" size="50" />
		<button class="button bg-green" type="button" onclick="craw()">解析</button>
		<button class="button bg-blue" type="button" onclick="next()">下一页</button>
	</div>
	<table class="table text-default table-condensed">
		<thead>
			<tr>
				<th width="50">序号</th>
				<th width="300">title</th>
				<th>url</th>
			</tr>
		</thead>
		<tbody>

		</tbody>
	</table>
</div>
<script>
	function craw() {
		var craw_url = $.trim($('input[name="craw_url"]').val());
		if (craw_url == '') {
			layer.msg('请填写抓取地址');
			return;
		}
		if ($('#list-rule').val() == '') {
			layer.msg('请选择爬取规则');
			return;
		}
		layer.load(1,{shade: 0});
		$.get('${ctx}/craw/rule/data.json?id=' + $('select').val()).done(
				function(data) {
					var rule = {};
					rule.js_enabled = $('#js_enabled').val();
					rule.craw_url = craw_url;
					rule.craw_item = data.rule.craw_item;
					rule.craw_next = data.rule.craw_next;
					rule.craw_store = data.rule.craw_store;
					$.each(data.rule.list_ext, function(i, n) {
						rule[n.rule_ext_name] = n.rule_ext_css + ";"
								+ n.rule_ext_type + "["+ n.rule_ext_reg + "];" + n.rule_ext_attr + ";" + n.rule_ext_mode;
					});
					$.each(data.rule.content_ext, function(i, n) {
						rule[n.rule_ext_name] = n.rule_ext_css + ";"
								+ n.rule_ext_type + "["+ n.rule_ext_reg + "];" + n.rule_ext_attr + ";" + n.rule_ext_mode;
					});
					$.post('${ctx}/craw/crawList.json', rule).done(
							function(data) {
								$('tbody').html('');
								if(data.statusCode==200){
									$.each(data.craw.data, function(i, n) {
										$('tbody').append(
												'<tr><td>'+(i+1)+'</td><td>' + n.title + '</td><td><a target="_blank" href="'
														+ n.url + '">'+n.url+'</a></td></tr>');
									});
									if (data.craw.craw_next != null) {
										$('input[name="craw_url"]').attr('data-next', data.craw.craw_next);
									}
								}
								if($('tbody').html()==''){
									$('tbody').html('<tr><td>没有数据</td></tr>');
								}
								layer.close(layer.index);
								next();
							});
				});
	}
	function next() {
		if ($('tbody tr').size() == 0) {
			layer.msg('请先解析');
			return;
		}
		if ($('tbody').html().indexOf('没有数据') > -1) {
			layer.msg('没有数据');
			return;
		}
		if ($('input[name="craw_url"]').attr('data-next') == '') {
			layer.msg('没有下一页');
			return;
		}
		$('input[name="craw_url"]').val($('input[name="craw_url"]').attr('data-next'));
		craw();
	}
	$('#list-rule').change(function(){
		if($.trim($('input[name="craw_url"]').val())==''){
			$('input[name="craw_url"]').val($(this).find('option:selected').attr('data-url'));
			$('input[name="craw_url"]').attr('data-next','');
		}
	});
</script>
