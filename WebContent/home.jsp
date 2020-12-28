<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- begin content -->
<div id="content" class="content">
	<!-- begin breadcrumb -->
	<ol class="breadcrumb pull-right">
		<li class="breadcrumb-item active">Home</li>
	</ol>
	<!-- end breadcrumb -->
	<!-- begin page-header -->
	<h1 class="page-header">ยินดีต้อนรับ</h1>
	<!-- end page-header -->
	<div class="row">
		<div class="col-md-4 offset-md-0">
			<div class="card">
				<div class="card-body">
					<h4 class="card-title">Quotation</h4>
					<h6 class="text-muted card-subtitle mb-2"><span id="quotation_count">-</span> Total</h6>
					<a class="card-link" href="quotation">Manage</a>
				</div>
			</div>
		</div>
		<div class="col-md-4">
			<div class="card">
				<div class="card-body">
					<h4 class="card-title">Work Order</h4>
					<h6 class="text-muted card-subtitle mb-2"><span id="workorder_count">-</span> Total</h6>
					<a class="card-link" href="workorder">Manage</a>
				</div>
			</div>
		</div>
		<div class="col-md-4">
			<div class="card">
				<div class="card-body">
					<h4 class="card-title">Invoice</h4>
					<h6 class="text-muted card-subtitle mb-2"><span id="invoice_count">-</span> Total</h6>
					<a class="card-link" href="invoice" disabled>Manage</a>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- end content -->

<script type="text/javascript">
	var quotationCount = document.getElementById('quotation_count');
	var workorderCount = document.getElementById('workorder_count');
	var invoiceCount = document.getElementById('invoice_count');
	
	sessionStorage.setItem('page', 'home');
	getCount();
	
	function getCount() {
		fetch("/report-build/apis/utils?get=count", {
			method: 'GET',
			mode: 'same-origin',
			credentials: 'same-origin',
			headers: {
				'Content-Type': 'application/json'
			},
			redirect: 'follow',
			referrerPolicy: 'no-referrer'
		})
		.then(response => response.text())
		.then(result => {
			var data = JSON.parse(result);
			console.log(data);
			quotationCount.innerText = data[0].quotation;
			workorderCount.innerText = data[0].workorder;
		})
		.catch(error => console.log('error', error));
	}
</script>