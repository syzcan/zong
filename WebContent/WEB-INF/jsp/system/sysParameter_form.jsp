<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" />
<form id="form" method="post">
    <input type="hidden" name="id" />
    <table class="table-form">
        <tr>
            <td align="right">参数名称：</td>
            <td>
                <input name="name" class="easyui-textbox" data-options="required:true" />
            </td>
        </tr>
        <c:if test="${edit }">
        <tr>
            <td align="right">英文键，唯一：</td>
            <td data-field="paramKey">
            </td>
        </tr>
        </c:if>
        <c:if test="${!edit }">
        <tr>
            <td align="right">英文键，唯一：</td>
            <td>
                <input name="paramKey" class="easyui-textbox" data-options="required:true,validType:{check:['${ctx }/system/parameter/check','paramKey','已存在']}" />
            </td>
        </tr>
        </c:if>
        <tr>
            <td align="right">参数值：</td>
            <td>
                <input name="paramValue" class="zuploadbox" data-options="required:true,width:240" />
            </td>
        </tr>
        <tr>
            <td align="right">备注：</td>
            <td>
                <textarea name="remark" class="easyui-textbox" data-options="multiline:true" style="width:300px;height:60px;"></textarea>
            </td>
        </tr>
    </table>
</form>