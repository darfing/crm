<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript">

		$(function () {

			//给"编辑"按钮添加单击事件
			$("#edit").click(function () {
				//收集参数
				//获取列表中被选中的checkbox
				var chkedIds = $("#tBody input[type='checkbox']:checked");
				if (chkedIds.size() == 0) {
					alert("请选择要修改的字典值");
					return;
				}
				if (chkedIds.size() > 1) {
					alert("每次只能修改一条字典值");
					return;
				}
				var id = chkedIds[0].value;
				//发送请求
				window.location.href = "settings/dictionary/value/toEditDictValue.do/" + id;
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
				var chekkedIds = $("#tBody input[type='checkbox']:checked");
				if (chekkedIds.size() == 0) {
					alert("请选择要删除的字典值");
					return;
				}

				if (window.confirm("确定删除吗？")) {
					var ids = '';
					$.each(chekkedIds, function () {//id=xxxx&id=xxx&.....&id=xxx&
						ids += "id=" + this.value + "&";
					});
					ids = ids.substr(0, ids.length - 1);//id=xxxx&id=xxx&.....&id=xxx id='xxx' or id=
					alert(ids);
					//发送请求
					$.ajax({
						url: 'settings/dictionary/deleteDicValue.do',
						data:ids,
						type: 'post',
						dataType: 'json',
						success: function (data) {
							if (data.code == "1") {
								//刷新市场活动列表,显示第一页数据,保持每页显示条数不变
								alert("删除成功!")
								window.location.href="settings/dictionary/value/index.do";
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
</head>
<body>

	<div>
		<div style="position: relative; left: 30px; top: -10px;">
			<div class="page-header">
				<h3>字典值列表</h3>
			</div>
		</div>
	</div>
	<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;left: 30px;">
		<div class="btn-group" style="position: relative; top: 18%;">
		  <button type="button" class="btn btn-primary" onclick="window.location.href='settings/dictionary/value/toCreateDicValue.do'"><span class="glyphicon glyphicon-plus"></span> 创建</button>
		  <button type="button" class="btn btn-default" id="edit"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
		  <button type="button" class="btn btn-danger" id="delete"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>
	<div style="position: relative; left: 30px; top: 20px;">
		<table class="table table-hover">
			<thead>
				<tr style="color: #B3B3B3;">
					<td><input type="checkbox" id="chckAll"/></td>
					<td>序号</td>
					<td>字典值</td>
					<td>文本</td>
					<td>排序号</td>
					<td>字典类型编码</td>
				</tr>
			</thead>
			<tbody id="tBody">
				<c:forEach items="${requestScope.valueList}" var="value" varStatus="status">
					<tr class="active">
						<td><input type="checkbox" value="${value.id}" id="idVal"/></td>
						<td>${status.index + 1}</td>
						<td>${value.value}</td>
						<td>${value.text}</td>
						<td>${value.orderNo}</td>
						<td>${value.typeCode}</td>
					</tr>
				</c:forEach>
				<%--<tr class="active">
					<td><input type="checkbox" /></td>
					<td>1</td>
					<td>m</td>
					<td>男</td>
					<td>1</td>
					<td>sex</td>
				</tr>
				<tr>
					<td><input type="checkbox" /></td>
					<td>2</td>
					<td>f</td>
					<td>女</td>
					<td>2</td>
					<td>sex</td>
				</tr>
				<tr class="active">
					<td><input type="checkbox" /></td>
					<td>3</td>
					<td>1</td>
					<td>一级部门</td>
					<td>1</td>
					<td>orgType</td>
				</tr>
				<tr>
					<td><input type="checkbox" /></td>
					<td>4</td>
					<td>2</td>
					<td>二级部门</td>
					<td>2</td>
					<td>orgType</td>
				</tr>
				<tr class="active">
					<td><input type="checkbox" /></td>
					<td>5</td>
					<td>3</td>
					<td>三级部门</td>
					<td>3</td>
					<td>orgType</td>
				</tr>--%>
			</tbody>
		</table>
	</div>
	
</body>
</html>