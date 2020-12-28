<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- begin content -->
<div id="content" class="content">

	<!-- begin modal -->
	<div class="modal fade" id="listCustomer" tabindex="-1" role="dialog">
  		<div class="modal-dialog modal-dialog-centered" role="document">
    		<div class="modal-content">
      			<div class="modal-body">
      				<div class="customer form-group row m-b-15">
		                <label class="col-form-label col-md-2" id="type"></label>
		                <div class="col-md-10">
		        			<select class="form-control" id="customer_id">
		        				<option disabled="disabled">ไม่มีข้อมูล</option>
		        			</select>
		        		</div>
		        	</div>
      			</div>
      			<div class="modal-footer" id="btn-footer-set">
      				
      			</div>
   			</div>
  		</div>
	</div>
	<!-- end modal -->
	
	<div class="row">
		<div class="d-none d-md-block col-md-3"></div>
		<div class="col-md-6">
			<!-- begin breadcrumb -->
			<ol class="breadcrumb pull-right">
				<li class="breadcrumb-item"><a href="/report-build">Home</a></li>
				<li class="breadcrumb-item active">Customer</li>
			</ol>
			<!-- end breadcrumb -->
			<!-- begin page-header -->
			<h1 class="page-header">ลูกค้า</h1>
			<!-- end page-header -->
			<!-- begin panel -->
			<div class="panel panel-inverse" data-sortable-id="form-stuff-1">
				<!-- begin panel-heading -->
				<div class="panel-heading">
					<div class="btn-group btn-group-toggle" data-toggle="buttons">
						<input class="btn btn-primary btn-xs active" type="button" onclick="initNewCustomer()" value="เพิ่มใหม่" /> 
						<input class="btn btn-secondary btn-xs" type="button" onclick="getCustomerId()" value="แก้ไข..." /> 
					</div>
					<h4 class="panel-title pull-right">ลูกค้า</h4>
				</div>
				<!-- end panel-heading -->
				<!-- begin panel-body -->
				<div class="panel-body">
					<form name="formCus" id="formCus">
				        <fieldset>
				        	<div class="customer form-group row m-b-15">
				                <label class="col-form-label col-md-2">รหัส</label>
				                <div class="col-md-2">
				                    <input name="customerId" id="customerId" type="text" class="readonly-data form-control m-b-5" placeholder="ID" readonly/>
				                </div>
				            </div>
				            <div class="customer form-group row m-b-15">
				                <label class="col-form-label col-md-2">ชื่อ</label>
				                <div class="col-md-10">
				                    <input name="customerName" id="customerName" type="text" class="form-control m-b-5" placeholder="Name" />
				                </div>
				            </div>
				            <div class="customer form-group row m-b-15">
				                <label class="col-form-label col-md-2">ที่อยู่</label>
				                <div class="col-md-10">
				                    <textarea name="customerAddr" id="customerAddr" class="form-control" rows="2" placeholder="Address" maxlength="255"></textarea>
				                </div>
				            </div>
				            <div class="customer form-group row m-b-15">
				                <label class="col-form-label col-md-2">การติดต่อ</label>
				                <div class="col-md-10">
				                    <textarea name="customerContact" id="customerContact" class="form-control" rows="2" placeholder="Contact" maxlength="255"></textarea>
				                </div>
				            </div>
				            <button class="btn btn-primary" type="button" onclick="onSubmit()">บันทึก</button>
				            <button class="btn btn-danger" id="delete" style="display: none;" type="button" onclick="onDropThis($('#customerId').val())">ลบ</button>
				        </fieldset>
					</form>
				</div>
			</div>
		</div>
	</div>
	<div class="d-none d-md-block col-md-3"></div>
</div>
<!-- end content -->
<script defer>
	var customerList = new Object;
	
	setTimeout(function () {
	    initNewCustomer();
	}, 1000);
	
	function initNewCustomer() {
	    $('form').find("input[type=text], textarea").val("");
	    onRemove(false);
	    generateCustomerId();
	}
	
	function generateCustomerId() {
		$.ajax({
	        url: '/report-build/apis/customer',
	        data: {
	            cus: 'getmaxid'
	        },
	        type: 'GET'
	    }).done(function (data, textStatus, xhr) {
	        $('#customerId').val(data[0].maxid);
	    });
	}
	
	function getCustomerId() {
	    if (customerList !== null) {
	        $.ajax({
	            url: '/report-build/apis/customer',
	            data: {
	                cus: 'getcustomer'
	            },
	            type: 'GET'
	        }).done(function (data, textStatus, xhr){
	            customerList = data;
	            listCustomer();
	        });
	    } else {
	        listCustomer();	
	    }
	}
	
	function listCustomer() {
	    var obj = '';
	    $.each(customerList, function (index, val) {
	        obj += '<option value="'+val.customerId+'">'+val.customerId+' -- '+val.customerOrgName+'</option>'
	    });
	    $('#type').text("ลูกค้า");
	    $('#btn-footer-set').html('<button type="button" onclick="onSelectCustomer($(\'#customer_id\').val())" class="btn btn-primary" data-dismiss="modal">เลือก</button>'
	    +'<button type="button" class="btn btn-secondary" data-dismiss="modal">ยกเลิก</button>');
	    $('#customer_id').html(obj);
	    $('#listCustomer').modal();
	}
	
	function onSelectCustomer(cusId) {
	    $.ajax({
	        url: '/report-build/apis/customer',
	        data : {
	            cus: 'getcustomer',
	            customerId: cusId
	        },
	        type: 'GET'
	    }).done(function (data, textStatus, xhr) {
	        $.each(data, function (index, val) {
	            $('#customerId').val(val.customerId);
	            $('#customerName').val(val.customerOrgName);
	            $('#customerAddr').val(val.customerAddr);
	            $('#customerContact').val(val.customerContact);
	        });
	        onRemove(true);
	    });
	}
	
	function onRemove(isAdded) {
		if (isAdded) {
			$('#delete').show();
		} else {
			$('#delete').hide();
		}
	}
	
	function onDropThis(thisId) {
		swal({
		    title: "ต้องการลบข้อมูลนี้หรือไม่?",
		    text: "ข้อมูลจะถูกลบออกไปจากระบบ",
		    icon: "warning",
		    dangerMode: true,
		    buttons: true
		 }).then(
		    function (remove) {
		        if (remove) {
					$.ajax({
						url: '/report-build/apis/customer',
						data: {
							cus: 'drop',
							customerId: thisId
						},
						type: 'GET'
					}).done(function (data, textStatus, xhr) {
						var state = xhr.getResponseHeader('state');
			            if (state === 'OKAY') {
			                swal("Completed!", "ลบออกจากฐานข้อมูลเรียบร้อยแล้ว", "success");
			                onRemove(false);
			                initNewCustomer();
			            } else {
			                swal("Failed!", state, "error");
			            }
					});
		        }
		    }
        );
	}

	function onSubmit() {
		var formData = $("#formCus").serializeArray();
		
		var obj = new Object
		obj['cus'] = 'addorupdate'
		
		for (var i in formData) {
			obj[formData[i].name] = formData[i].value;
		}
		console.table(obj);
		
		$.ajax({
			url: '/report-build/apis/customer',
			data: obj,
			type: 'GET'
		}).done(function (data, textStatus, xhr) {
			var state = xhr.getResponseHeader('state');
            if (state === 'OKAY') {
                swal("Completed!", "เพิ่มเข้าฐานข้อมูลเรียบร้อยแล้ว", "success");
                onRemove(true);
            } else if (state === 'UPDATED') {
                swal("Completed!", "ปรับปรุงเรียบร้อยแล้ว", "success");
                onRemove(true);
            } else {
                swal("Failed!", state, "error");
            }
		})
		
	}
</script>