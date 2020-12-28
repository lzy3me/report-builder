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
				<li class="breadcrumb-item active">Sale</li>
			</ol>
			<!-- end breadcrumb -->
			<!-- begin page-header -->
			<h1 class="page-header">พนักงานขาย</h1>
			<!-- end page-header -->
			<!-- begin panel -->
			<div class="panel panel-inverse" data-sortable-id="form-stuff-1">
				<!-- begin panel-heading -->
				<div class="panel-heading">
					<div class="btn-group btn-group-toggle" data-toggle="buttons">
						<input class="btn btn-primary btn-xs active" type="button" onclick="initNewSale()" value="เพิ่มใหม่" /> 
						<input class="btn btn-secondary btn-xs" type="button" onclick="onEditSale()" value="แก้ไข..." /> 
					</div>
					<h4 class="panel-title pull-right">พนักงานขาย</h4>
				</div>
				<!-- end panel-heading -->
				<!-- begin panel-body -->
				<div class="panel-body">
					<form name="formSale" id="formSale">
				        <fieldset>
				            <div class="form-group row m-b-15">
				            	<label class="col-form-label col-md-1">รหัส</label>
				                <div class="col-md-2">
				                    <input name="saleId" id="saleId" type="text" class="readonly-data form-control m-b-5" maxlength="3" placeholder="SALE ID." readonly/>
				                </div>
				                <label class="col-form-label col-md-1">รหัสพนักงาน</label>
				                <div class="col-md-2">
				                    <input name="saleNo" id="saleNo" type="text" class="form-control m-b-5" maxlength="3" placeholder="SALE NO." />
				                </div>
			                </div>
			                <div class="form-group row m-b-15">
				                <label class="col-form-label col-md-1">ชื่อ</label>
				                <div class="col-md-5">
				                    <input name="saleFirstName" id="saleFirstName" class="form-control" placeholder="NAME" />
				                </div>
				                <label class="col-form-label col-md-1">นามสกุล</label>
				                <div class="col-md-5">
				                    <input name="saleLastName" id="saleLastName" class="form-control" placeholder="SURNAME" />
				                </div>
				            </div>
				            <button class="btn btn-primary" type="button" onclick="onSubmit()">บันทึก</button>
				            <button class="btn btn-danger" id="delete" style="display: none;" type="button" onclick="onDropThis($('#saleId').val())">ลบ</button>
				        </fieldset>
					</form>
				</div>
			</div>
		</div>
	</div>
	<div class="d-none d-md-block col-md-3"></div>
</div>
<!-- end content -->
<script>
	var saleList = new Object;
	
	setTimeout(function () {
	    initNewSale();
	}, 1000);
	
	function initNewSale() {
	    $('form').find("input").val("");
	    onRemove(false);
	    generateSaleId();
	}
	
	function generateSaleId() {
		$.ajax({
	        url: '/report-build/apis/all',
	        data: {
	            p: 'getsalemaxid'
	        },
	        type: 'GET'
	    }).done(function (data, textStatus, xhr) {
	        $('#saleId').val(data[0].saleId);
	    });
	}
	
	function onEditSale() {
		if (saleList !== null) {
		    $.ajax({
		        url: '/report-build/apis/all',
		        data: {
		            p: 'salelist'
		        },
		        type: 'GET'
		    }).done(function (data, textStatus, xhr) {
		    	saleList = data;
		        onSelectSaleList();
		    });			
		} else {
			onSelectSaleList();
		}
	}
	
	function onSelectSaleList() {
		var obj = '';
	    $.each(saleList, function (index, val) {
	        obj += '<option value="'+val.saleId+'">[<b>ID:</b>'+val.saleId+'][<b>NO:</b>'+val.saleNo+'] '+val.saleFirstName+' '+val.saleLastName+'</option>'
	    });
	    $('#type').text("พนักงานขาย");
	    $('#btn-footer-set').html('<button type="button" onclick="getSaleData($(\'#customer_id\').val())" class="btn btn-primary" data-dismiss="modal">เลือก</button>'
	    +'<button type="button" class="btn btn-secondary" data-dismiss="modal">ยกเลิก</button>');
	    $('#customer_id').html(obj);
	    $('#listCustomer').modal();
	}
	
	function getSaleData(id) {
		$.ajax({
	        url: '/report-build/apis/all',
	        data : {
	            p: 'sale',
	            id: id
	        },
	        type: 'GET'
	    }).done(function (data, textStatus, xhr) {
	    	$.each(data, function (index, val) {	    		
	    		$('#saleId').val(val.saleId);
	    		$('#saleNo').val(val.saleNo);
	    		$('#saleFirstName').val(val.saleFirstName);
	    		$('#saleLastName').val(val.saleLastName);
	    	});
	    	onRemove(true);
	    });
	}

	function onSubmit() {
		var formData = $("#formSale").serializeArray();
		
		var obj = new Object;
		obj['p'] = 'addupdatesale'
		
		for (var i in formData) {
			obj[formData[i].name] = formData[i].value;
		}
		console.table(obj);
		
		$.ajax({
			url: '/report-build/apis/all',
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
		            url: '/report-build/apis/all',
		            data: {
		                p: 'dropsale',
		                id: thisId
		            },
		            type: 'GET'
		            }).done(function (data, textStatus, xhr) {
		                var state = xhr.getResponseHeader('state');
		                if (state === 'OKAY') {
		                    swal("Completed!", "ลบออกจากฐานข้อมูลเรียบร้อยแล้ว", "success");
		                    onRemove(false);
		                    initNewSale();
		                } else {
		                    swal("Failed!", state, "error");
		                }
		            }); 
		        }
		    }
		);
	}
</script>