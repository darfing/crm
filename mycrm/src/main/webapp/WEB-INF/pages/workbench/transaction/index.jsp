<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
	+ request.getContextPath() + "/";
%>
<html>
<head>
	<base href="<%=baseUrl%>">
<meta charset="UTF-8">

	<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
	<link rel="stylesheet" type="text/css"
		  href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">
	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">

	<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
	<script type="text/javascript"
			src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.min.js"></script>

<script type="text/javascript">

	$(function () {
		$(".mydate").datetimepicker({
			language: 'en', //语言
			format: 'yyyy-mm-dd',//日期的格式
			minView: 'month', //可以选择的最小视图
			initialDate: new Date(),//初始化显示的日期
			autoClose: true,//设置选择完日期或者时间之后，是否自动关闭日历
			todayBtn: true,//设置是否显示"今天"按钮,默认是false
			clearBtn: true,//设置是否显示"清空"按钮，默认是false

		});
		queryTransactionForPageAndByCondition(1, 10);
		//给"查询"按钮添加单击事件
		$("#queryBtn").click(function () {
			//查询所有符合条件数据的第一页以及所有符合条件数据的总条数;
			queryTransactionForPageAndByCondition(1, $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
		});
		//给"全选"按钮添加单击事件
		$("#chckAll").click(function () {
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
		//给删除按钮添加单击事件
		$("#deleteBtn").click(function () {
			//收集参数
			//获取列表中所有被选中的checkbox
			var chekkedIds = $("#tBody input[type='checkbox']:checked");
			if (chekkedIds.size() == 0) {
				alert("请选择要删除的客户");
				return;
			}

			if (window.confirm("确定删除吗？")) {
				var ids = '';
				$.each(chekkedIds, function () {//id=xxxx&id=xxx&.....&id=xxx&
					ids += "id=" + this.value + "&";
				});
				ids = ids.substr(0, ids.length - 1);//id=xxxx&id=xxx&.....&id=xxx id='xxx' or id=
				//发送请求
				$.ajax({
					url: 'workbench/contacts/deleteContacts.do',
					data: ids,
					type: 'post',
					dataType: 'json',
					success: function (data) {
						if (data.code == "1") {
							//刷新市场活动列表,显示第一页数据,保持每页显示条数不变
							queryTransactionForPageAndByCondition(1, $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
							alert("删除成功");
						} else {
							//提示信息
							alert(data.message);
						}
					}
				});
			}
		});
		function queryTransactionForPageAndByCondition(pageNo, pageSize) {
			//收集参数
			var owner = $.trim($("#owner").val());
			var name = $("#name").val();
			var customerId = $.trim($("#customerName").val());
			var stage = $("#stage").val();
			var type = $("#transactionType").val();
			var source = $.trim($("#source").val());
			var contactsId = $.trim($("#contactsName").val());
			//发送请求
			$.ajax({
				url: 'workbench/transaction/queryTransactionForPageAndByCondition.do',
				data: {
					name: name,
					owner: owner,
					customerId: customerId,
					source: source,
					stage: stage,
					type: type,
					contactsId: contactsId,
					pageNo: pageNo,
					pageSize: pageSize,
				},
				type: 'post',
				dataType: 'json',
				success: function (data) {
					//显示总条数
					$("#totalRowsB").text(data.total);
					//显示市场活动的列表
					//遍历clueList，拼接所有行数据
					var htmlStr = "";
					$.each(data.list, function (index, obj) {
						htmlStr += "<tr class=\"active\">";
						htmlStr += "<td><input type=\"checkbox\" value=\"" + obj.id + "\"/></td>";
						htmlStr += "<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/clue/detail.do/" + obj.id + "'\">" + obj.name + "</a></td>";
						htmlStr += "<td>" + obj.customerId + "</td>";
						htmlStr += "<td>" + obj.stage + "</td>";
						htmlStr += "<td>" + obj.type + "</td>";
						htmlStr += "<td>" + obj.owner + "</td>";
						htmlStr += "<td>" + obj.source + "</td>";
						htmlStr += "<td>" + obj.contactsId + "</td>";
						htmlStr += "</tr>";
					});
					$("#tBody").html(htmlStr);

					//取消"全选"按钮
					$("#chckAll").prop("checked", false);


					//计算总页数
					var totalPages = 1;
					if (data.total % pageSize == 0) {
						totalPages = data.total / pageSize;
					} else {
						totalPages = parseInt(data.total / pageSize) + 1;
					}

					//对容器调用bs_pagination工具函数，显示翻页信息
					$("#demo_pag1").bs_pagination({
						currentPage: pageNo,//当前页号,相当于pageNo
						rowsPerPage: pageSize,//每页显示条数,相当于pageSize
						totalRows: data.total,//总条数
						totalPages: totalPages,  //总页数,必填参数.
						visiblePageLinks: 5,//最多可以显示的卡片数
						showGoToPage: true,//是否显示"跳转到"部分,默认true--显示
						showRowsPerPage: true,//是否显示"每页显示条数"部分。默认true--显示
						showRowsInfo: true,//是否显示记录的信息，默认true--显示

						//用户每次切换页号，都自动触发本函数;
						//每次返回切换页号之后的pageNo和pageSize
						onChangePage: function (event, pageObj) { // returns page_num and rows_per_page after a link has clicked
							queryTransactionForPageAndByCondition(pageObj.currentPage, pageObj.rowsPerPage);
						}
					});
				}
			});
		}
	})
</script>
</head>
<body>

	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>交易列表</h3>
			</div>
		</div>
	</div>
	
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
	
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <%--<input class="form-control" type="text" id="owner">--%>
						<select class="form-control" id="owner">
							<option></option>
							<c:forEach items="${users}" var="user">
								<option value="${user.id}">${user.name}</option>
							</c:forEach>
						</select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">客户名称</div>
				      <%--<input class="form-control" type="text" id="customerName">--%>
						<select class="form-control" id="customerName">
							<option></option>
							<c:forEach items="${customers}" var="customer">
								<option value="${customer.id}">${customer.name}</option>
							</c:forEach>
						</select>
				    </div>
				  </div>
				  
				  <br>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">阶段</div>
					  <select class="form-control" id="stage">
					  	<option></option>
					  	<c:forEach items="${stageList}" var="stage">
							<option value="${stage.id}">${stage.value}</option>
						</c:forEach>
					  </select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">类型</div>
					  <select class="form-control" id="transactionType">
					  	<option></option>
						<c:forEach items="${transactionTypeList}" var="type">
							<option value="${type.id}">${type.value}</option>
						</c:forEach>
					  </select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">来源</div>
				      <select class="form-control" id="source">
						  <option></option>
						  <c:forEach items="${sourceList}" var="sl">
							  <option value="${sl.id}">${sl.value}</option>
						  </c:forEach>
						</select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">联系人名称</div>
				      <%--<input class="form-control" type="text" id="contactsName">--%>
						<select class="form-control" id="contactsName">
							<option></option>
							<c:forEach items="${contacts}" var="contact">
								<option value="${contact.id}">${contact.fullname}</option>
							</c:forEach>
						</select>
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 10px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" onclick="window.location.href='workbench/transaction/toSave.do';"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" onclick="window.location.href='workbench/transaction/toEdit.do';"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="chckAll"/></td>
							<td>名称</td>
							<td>客户名称</td>
							<td>阶段</td>
							<td>类型</td>
							<td>所有者</td>
							<td>来源</td>
							<td>联系人名称</td>
						</tr>
					</thead>
					<tbody id="tBody">
						<%--<tr>
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">动力节点-交易01</a></td>
							<td>动力节点</td>
							<td>谈判/复审</td>
							<td>新业务</td>
							<td>zhangsan</td>
							<td>广告</td>
							<td>李四</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">动力节点-交易01</a></td>
                            <td>动力节点</td>
                            <td>谈判/复审</td>
                            <td>新业务</td>
                            <td>zhangsan</td>
                            <td>广告</td>
                            <td>李四</td>
                        </tr>--%>
					</tbody>
				</table>
				<div id="demo_pag1"></div>
			</div>
			
			<%--<div style="height: 50px; position: relative;top: 20px;">
				<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b>50</b>条记录</button>
				</div>
				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">
					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>
					<div class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							10
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">20</a></li>
							<li><a href="#">30</a></li>
						</ul>
					</div>
					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
				</div>
				<div style="position: relative;top: -88px; left: 285px;">
					<nav>
						<ul class="pagination">
							<li class="disabled"><a href="#">首页</a></li>
							<li class="disabled"><a href="#">上一页</a></li>
							<li class="active"><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li><a href="#">下一页</a></li>
							<li class="disabled"><a href="#">末页</a></li>
						</ul>
					</nav>
				</div>
			</div>--%>
			
		</div>
		
	</div>
</body>
</html>