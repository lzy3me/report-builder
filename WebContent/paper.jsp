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
				<li class="breadcrumb-item active">Paper</li>
			</ol>
			<!-- end breadcrumb -->
			<!-- begin page-header -->
			<h1 class="page-header">แบบกระดาษ</h1>
			<!-- end page-header -->
			<!-- begin panel -->
			<div class="panel panel-inverse" data-sortable-id="form-stuff-1">
				<!-- begin panel-heading -->
				<div class="panel-heading">
					<div class="btn-group btn-group-toggle" data-toggle="buttons">
						<input class="btn btn-primary btn-xs active" type="button" onclick="initNewPaper()" value="เพิ่มใหม่" /> 
						<input class="btn btn-secondary btn-xs" type="button" onclick="onEditPaper()" value="แก้ไข..." /> 
					</div>
					<h4 class="panel-title pull-right">แบบกระดาษ</h4>
				</div>
				<!-- end panel-heading -->
				<!-- begin panel-body -->
				<div class="panel-body">
					<form name="formPaper" id="formPaper">
				        <fieldset>
				            <div class="customer form-group row m-b-15">
				            	<label class="col-form-label col-md-1">รหัส</label>
				                <div class="col-md-3">
				                    <input name="paperId" id="paperId" type="text" class="readonly-data form-control m-b-5" placeholder="Paper ID" readonly/>
				                </div>
				                <label class="col-form-label col-md-1">ประเภทกระดาษ</label>
				                <div class="col-md-3">
				                    <input name="paperType" id="paperType" type="text" class="form-control m-b-5" placeholder="Paper Type" />
				                </div>
				                <label class="col-form-label col-md-1">น้ำหนัก</label>
				                <div class="col-md-3">
				                    <input name="paperWeight" id="paperWeight" type="number" class="form-control m-b-5" placeholder="Weight" />
				                </div>
				            </div>
				            <div class="customer form-group row m-b-15">
				                <label class="col-form-label col-md-1">สี</label>
				                <div class="col-md-3">
				                    <input name="paperColor" id="paperColor" type="text" class="form-control m-b-5" placeholder="Color" />
				                </div>
				           		<div class="col-md-4">				           		
				           			<div class="form-check form-check-inline">
										<input type="checkbox" value="" onclick="chkThis()" id="isDefault" class="form-check-input" />
										<label class="form-check-label" for="isDefault">ตั้งเป็นกระดาษเริ่มต้น</label>
									</div>
				           		</div>
				           		<label for="paperDefault" class="col-form-label col-md-1">ตำแหน่ง</label>
				           		<div class="col-md-3">
				           			<select name="paperDefault" id="paperDefault" class="form-control" disabled="disabled">
				           				<option value="null" selected>----</option>
				           				<option value="top">หัวกระดาษ</option>
				           				<option value="bottom">ท้ายกระดาษ</option>
				           			</select>
				           		</div>
				           	</div>
				            <button class="btn btn-primary" type="button" onclick="onSubmit()">บันทึก</button>
				            <button class="btn btn-danger" id="delete" style="display: none;" type="button" onclick="onDropThis($('#paperId').val())">ลบ</button>
				        </fieldset>
					</form>
				</div>
			</div>
		</div>
		<div class="d-none d-md-block col-md-3"></div>
	</div>
</div>
<!-- end content -->
<script defer>
	var paperList = new Object;

	setTimeout(function () {
	    initNewPaper();
	}, 1000);
	
	function initNewPaper() {
	    $('form').find("input[type=text], input[type=number], textarea").val("");
	    onRemove(false);
	    generatePaperId();
	}

	function generatePaperId() {
		$.ajax({
	        url: '/report-build/apis/all',
	        data: {
	            p: 'getpapermaxid'
	        },
	        type: 'GET'
	    }).done(function (data, textStatus, xhr) {
	        $('#paperId').val(data[0].paperId);
	    });
	}
	
	function onEditPaper() {
		if (paperList !== null) {
		    $.ajax({
		        url: '/report-build/apis/all',
		        data: {
		            p: 'paperlist'
		        },
		        type: 'GET'
		    }).done(function (data, textStatus, xhr) {
		        paperList = data;
		        onSelectPaperList();
		    });			
		} else {
			onSelectPaperList();
		}
	}
	
	function onSelectPaperList() {
		var obj = '';
	    $.each(paperList, function (index, val) {
	        obj += '<option value="'+val.paperId+'">'+val.paperId+' -- '+val.paperType+' สี'+val.paperColor+'</option>'
	    });
	    $('#type').text("กระดาษ");
	    $('#btn-footer-set').html('<button type="button" onclick="getPaperData($(\'#customer_id\').val())" class="btn btn-primary" data-dismiss="modal">เลือก</button>'
	    +'<button type="button" class="btn btn-secondary" data-dismiss="modal">ยกเลิก</button>');
	    $('#customer_id').html(obj);
	    $('#listCustomer').modal();
	}
	
	function getPaperData(id) {
		$.ajax({
	        url: '/report-build/apis/all',
	        data : {
	            p: 'paper',
	            id: id
	        },
	        type: 'GET'
	    }).done(function (data, textStatus, xhr) {
	    	$.each(data, function (index, val) {	    		
	    		$('#paperId').val(val.paperId);
	    		$('#paperType').val(val.paperType);
	    		$('#paperWeight').val(val.paperWeight);
	    		$('#paperColor').val(val.paperColor);
	    		if (typeof(val.paperDefault) !== 'undefined') {
	    			$('#isDefault').prop('checked', true);
	    			$('#paperDefault').val(val.paperDefault);
	    			chkThis();
	    		} else {
	    			$('#isDefault').prop('checked', false);
	    			$('#paperDefault').val('null');
	    			chkThis();
	    		}
	    	});
	    	onRemove(true);
	    });
	}

	function onSubmit() {
		var formData = $("#formPaper").serializeArray();
		
		var obj = new Object
		obj['p'] = 'addupdatepaper'
		
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
	
	function chkThis() {
		if ($('#isDefault').is(':checked')) {
			console.log('yes')
			$('#paperDefault').prop('disabled', false);
		} else {
			console.log('no')
			$('#paperDefault').prop('disabled', true);
		}
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
							p: 'droppaper',
							id: thisId
						},
						type: 'GET'
					}).done(function (data, textStatus, xhr) {
						var state = xhr.getResponseHeader('state');
			            if (state === 'OKAY') {
			                swal("Completed!", "ลบออกจากฐานข้อมูลเรียบร้อยแล้ว", "success");
			                onRemove(false);
			                initNewPaper()
			            } else {
			                swal("Failed!", state, "error");
			            }
					});
		        }
		    }
	    );
	}
</script>