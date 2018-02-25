<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<form id="form" method="post">
    <input type="hidden" name="id" />
    <table class="table-form">
        <tr>
            <td align="right">名称：</td>
            <td>
                <input name="name" class="easyui-textbox" data-options="required:true" />
            </td>
        </tr>
        <tr>
            <td align="right">备注：</td>
            <td>
                <input name="remark" class="easyui-textbox" data-options="" />
            </td>
        </tr>
    </table>
</form>