<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
</head>
<script type="text/javascript">

    $(function () {

        //给"编辑"按钮添加单击事件
        $("#edit").click(function () {
            //收集参数
            //获取列表中被选中的checkbox
            var chkedIds = $("#tBody input[type='checkbox']:checked");
            if (chkedIds.size() == 0) {
                alert("请选择要修改的字典类型");
                return;
            }
            if (chkedIds.size() > 1) {
                alert("每次只能修改一条字典类型");
                return;
            }
            var code = chkedIds[0].value;
            //发送请求
            window.location.href = "settings/dictionary/type/toEditDictionaryType.do/" + code;
        });

        $("#chckAll").click(function () {
            //如果"全选"按钮是选中状态，则列表中所有checkbox都选中
            /*if(this.checked==true){
                $("#tBody input[type='checkbox']").prop("checked",true);
            }else{
                $("#tBody input[type='checkbox']").prop("checked",false);
            }*/

            $("#tBody input[type='checkbox']").prop("checked", this.checked);
        });

        $("#tBody").on("click", "input[type='checkbox']", function () {
            //如果列表中的所有checkbox都选中，则"全选"按钮也选中
            if ($("#tBody input[type='checkbox']").size() == $("#tBody input[type='checkbox']:checked").size()) {
                $("#chckAll").prop("checked", true);
            } else {//如果列表中的所有checkbox至少有一个没选中，则"全选"按钮也取消
                $("#chckAll").prop("checked", false);
            }
        });

        $("#delete").click(function () {
            var chekkedCodes = $("#tBody input[type='checkbox']:checked");
            if (chekkedCodes.size() == 0) {
                alert("请选择要删除的字典类型");
                return;
            }

            if (window.confirm("确定删除吗？")) {
                var codes = '';
                $.each(chekkedCodes, function () {//id=xxxx&id=xxx&.....&id=xxx&
                    codes += "code=" + this.value + "&";
                });
                codes = codes.substr(0, codes.length - 1);//id=xxxx&id=xxx&.....&id=xxx id='xxx' or id=
                alert(codes);
                //发送请求
                $.ajax({
                    url: 'settings/dictionary/deleteDictionaryType.do',
                    data:codes,
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == "1") {
                            //刷新市场活动列表,显示第一页数据,保持每页显示条数不变
                            alert("删除成功!")
                            window.location.href="settings/dictionary/index.do";
                        } else {
                            //提示信息
                            alert(data.message);
                        }
                    }
                });
            }
        })
    });
</script>
<body>

<div>
    <div style="position: relative; left: 30px; top: -10px;">
        <div class="page-header">
            <h3>字典类型列表</h3>
        </div>
    </div>
</div>
<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;left: 30px;">
    <div class="btn-group" style="position: relative; top: 18%;">
        <button type="button" class="btn btn-primary" onclick="window.location.href='settings/dictionary/type/toCreateDictionaryType.do'"><span
                class="glyphicon glyphicon-plus"></span> 创建
        </button>
        <button type="button" class="btn btn-default" id="edit"><span
                class="glyphicon glyphicon-edit" ></span> 编辑
        </button>
        <button type="button" class="btn btn-danger" id="delete"><span class="glyphicon glyphicon-minus"></span> 删除</button>
    </div>
</div>
<div style="position: relative; left: 30px; top: 20px;">
    <table class="table table-hover">
        <thead>
        <tr style="color: #B3B3B3;">
            <td><input type="checkbox" id="chckAll"/></td>
            <td>序号</td>
            <td>编码</td>
            <td>名称</td>
            <td>描述</td>
        </tr>
        </thead>
        <tbody id="tBody">
        <%--				<tr class="active">--%>
        <%--					<td><input type="checkbox" /></td>--%>
        <%--					<td>1</td>--%>
        <%--					<td>sex</td>--%>
        <%--					<td>性别</td>--%>
        <%--					<td>性别包括男和女</td>--%>
        <%--				</tr>--%>
        <%--                        htmlStr += "<tr class=\"active\">";
                    htmlStr += "<td><input type=\"checkbox\" value=\"" + obj.id + "\"/></td>";
                    htmlStr += "<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/activity/detailActivity.do/" + obj.id + "'\">" + obj.name + "</a></td>";
                    htmlStr += "<td>" + obj.owner + "</td>";
                    htmlStr += "<td>" + obj.startDate + "</td>";
                    htmlStr += "<td>" + obj.endDate + "</td>";
                    htmlStr += "</tr>";
    <c:forEach items="${remarkList}" var="remark">
		<div id="div_${remark.id}" class="remarkDiv" style="height: 60px;">
			<img title="${remark.createBy}" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>${remark.noteContent}</h5>
				<font color="gray">市场活动</font> <font color="gray">-</font> <b>${activity.name}</b> <small style="color: gray;"> ${remark.editFlag=='1'?remark.editTime:remark.createTime} 由${remark.editFlag=='1'?remark.editBy:remark.createBy}${remark.editFlag=='1'?'修改':'创建'}</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" name="editA" remarkId="${remark.id}" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" name="deleteA" remarkId="${remark.id}" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>
	</c:forEach>
	--%>
        <c:forEach items="${requestScope.typeList}" var="type" varStatus="status">
            <tr>
                <td><input type="checkbox" value="${type.code}" id="code_val"/></td>
                <td>${status.index + 1}</td>
                <td>${type.code}</td>
                <td>${type.name}</td>
                <td>${type.description}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

</body>
</html>