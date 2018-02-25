<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	function jumpPage(obj) {
		$(obj).parents('form').find('input[name="currentPage"]').val($(obj).attr('data-num'));
		$(obj).parents('form').submit();
	}
</script>
<input type="hidden" name="currentPage" value="${page.currentPage}" />
<!-- 分页 -->
<div>
	<ul class="pagination border-main">
		<c:if test="${page.currentPage!=1&&page.totalPage!=0}">
			<li><a href="javascript:;" onclick="jumpPage(this);" data-num="1" title="首页">«</a></li>
		</c:if>
		<c:forEach items="${page.nums }" var="num">
			<li ${num==page.currentPage||page.currentPage==0?' class="active"':'' }><a href="javascript:;" ${num!=page.currentPage&&page.currentPage!=0?'onclick="jumpPage(this);"':'' }
				data-num="${num }">${num }</a></li>
		</c:forEach>
		<c:if test="${page.currentPage!=page.totalPage&&page.totalPage!=0}">
			<li><a href="javascript:;" onclick="jumpPage(this);" data-num="${page.totalPage}" title="末页">»</a></li>
		</c:if>
		<li><a>共<span class="text-yellow">${page.totalPage }</span>页</a></li>
	</ul>
	<div class="button-group">
		<select name="showCount" class="input border-main" onchange="$(this).parents('form').submit()">
			<option value="10"${page.showCount==10?' selected':'' }>10</option>
			<option value="20"${page.showCount==20?' selected':'' }>20</option>
			<option value="40"${page.showCount==40?' selected':'' }>40</option>
			<option value="80"${page.showCount==80?' selected':'' }>80</option>
		</select>
	</div>
</div>
<!-- 分页END -->