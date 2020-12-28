<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	
	String url_index = request.isSecure()?"https://":"http://"
					 + request.getServerName() 
					 + ":" + request.getServerPort()
					 + "/" + request.getContextPath();
	String tpage = config.getInitParameter("page") + ".jsp";

	if (tpage.equals("null.jsp")) {
		tpage = "home.jsp";
	}
	System.out.println("Page ======>>>> "+tpage);
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />
	<meta content="" name="description" />
	<meta content="" name="author" />
	<title>Report Builder</title> 

	<link href="./css/bootstrap.min.css" rel="stylesheet" />
	<link href="./css/bootstrap-select.min.css" rel="stylesheet" />

</head>
<style>
	@font-face {
		font-family: 'Open Sans';
		src: url('./fonts/OpenSans-Regular.ttf');
	}

	@font-face {
		font-family: 'Prompt';
		src: url('./fonts/Prompt-Regular.ttf');
	}

	body {
		font-family: 'Open Sans', 'Prompt', sans-serif;
	}
	.sidebar .nav {
		font-size: 1.2em;
	}
</style>
<body>
	<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
		<a href="/report-build" class="navbar-brand"><span class="navbar-logo"></span> Report Builder</a>
		<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#top-navbar" aria-controls="top-navbar" aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>

		<div class="collapse navbar-collapse pull-left" id="top-navbar">
			<ul class="navbar-nav mr-auto" style="font-size: 1.3em; font-weight: 500">
				<li class="nav-item active">
					<a class="nav-link" href="/report-build">
						<i class="fa fa-home fa-fw"></i> หน้าแรก
					</a>
				</li>
				<li class="nav-item dropdown">
					<a class="nav-link" href="javascript:;" class="dropdown-toggle" id="navbarDropdown" role="button" data-toggle="dropdown">
						<i class="fa fa-plus fa-fw"></i> เพิ่ม/แก้ไข <b class="caret"></b>
					</a>
					<div class="dropdown-menu" aria-labelledby="navbarDropdown" style="font-size: 1em">
						<a class="dropdown-item" href="quotation">ใบเสนอราคา</a>
						<a class="dropdown-item" href="workorder">ใบสั่งงาน</a>
						<div class="dropdown-divider"></div>
						<a class="dropdown-item" href="customer">ลูกค้า</a>
						<a class="dropdown-item" href="sale">พนักงานขาย</a>
						<div class="dropdown-divider"></div>
						<a class="dropdown-item" href="paper">กระดาษ</a>
					</div>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="javascript:;" onclick="callReportModal()">
						<i class="fa fa-copy fa-fw"></i> ออกรายงาน
					</a>
				</li>
			</ul>
		</div>
		<!-- end navbar-collapse -->
	</nav>
	
	<main class="container-fluid">
		<jsp:include flush="true" page="<%=tpage %>"></jsp:include>
	</main>

	<!-- #modal-dialog -->
	<div class="modal" id="report">
		<div class="modal-dialog modal-dialog-centered">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title">ออกใบรายงาน</h4>
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				</div>
				<div class="modal-body">
					<form action="/report-build/apis/report" target="_blank" method="GET" name="report" id="report">					
						<div class="form-group row m-b-15">
							<label class="col-form-label col-md-3" for="name">เลือกโปรแกรม</label>
							<div class="col-md-9">
								<select name="name" id="name" class="form-control form-control-lg">
									<option selected></option>
									<option value="Quotation">FL1001 - ใบเสนอราคา</option>
									<option value="Workorder">FL1002 - ใบสั่งงาน</option>
									<option value="Invoice" disabled>FL1003 - ใบเสร็จรับเงิน</option>
								</select>
							</div>
						</div>
						<div class="form-group row m-b-15">
							<label class="col-form-label col-md-3" for="id">รหัส</label>
							<div id="programGen" class="col-md-9">
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<a href="javascript:;" onclick="onCallReport()" class="btn btn-success">เลือก</a>
					<a href="javascript:;" class="btn btn-white" data-dismiss="modal">ยกเลิก</a>
				</div>
			</div>
		</div>
	</div>
	<!-- #modal-dialog end -->
	
	<script src="./js/jquery-3.3.1.min.js"></script>
	<script src="./js/bootstrap.min.js"></script>
	<script src="./js/bootstrap-select.min.js"></script>
	<script src="./js/sweetalert.min.js"></script>
	<script>
		$('#name').change(function () {
			var str = $('#name').val();
			if (str === "Quotation") {
				$.ajax({
					url: '/report-build/apis/quotation',
					data: {
						quota: 'list'
					},
					type: 'GET'
				}).done(function (data, status, xhr) {
					var obj = '';
					var selHeader = '<select class="form-control form-control-lg" name="id">';
					var selFooter = '</select>';
					$.each(data, function (index, val) {
						obj += '<option value="'+val.quotationId+'">NO.'+val.quotationId+' -- Product: '+val.productObject.productName+'</option>';
					});
					$('#programGen').html(selHeader+obj+selFooter);
				});
			} else if (str === "Workorder") {
				$.ajax({
					url: '/report-build/apis/workorder',
					data: {
						order: 'list'
					},
					type: 'GET'
				}).done(function (data, status, xhr) {
					var obj = '';
					var selHeader = '<select class="form-control form-control-lg" name="id">';
					var selFooter = '</select>';
					$.each(data, function (index, val) {
						obj += '<option value="'+val.workorderId+'">รหัส['+val.workorderId+'] วันที่ออก['+val.workDate+'] ชื่อผลิตภัณฑ์['+val.product.productName+']</option>';
					});
					$('#programGen').html(selHeader+obj+selFooter);
				});
			}
		})
		
		function onCallReport() {
			document.report.submit();
		}
		
		function callReportModal() {
			$('#report').modal();
		}
	</script>
</body>
</html>