<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- begin content -->
<div id="content" class="content">
	<!-- begin breadcrumb -->
	<ol class="breadcrumb pull-right">
		<li class="breadcrumb-item"><a href="/report-build">Home</a></li>
		<li class="breadcrumb-item active">Work Order</li>
	</ol>
	<!-- end breadcrumb -->
	
	<!-- begin page-header -->
	<h1 class="page-header">ใบสั่งงาน</h1>
	<!-- end page-header -->
	
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
	
	<!-- begin panel -->
	<div class="panel panel-inverse" data-sortable-id="form-stuff-1">
		<!-- begin panel-heading -->
		<div class="panel-heading">
			<div class="btn-group btn-group-toggle" data-toggle="buttons">
				<input class="btn btn-primary btn-xs active" type="button" onclick="initNewWorkorder()" value="เพิ่มใหม่" /> 
				<input class="btn btn-secondary btn-xs" type="button" onclick="onEditWorkorder()" value="แก้ไข..." /> 
			</div>
			<h4 class="panel-title pull-right">ใบสั่งงาน</h4>
		</div>
		<!-- end panel-heading -->
		<!-- begin panel-body -->
		<div class="panel-body">
			<form name="form" id="form">
<%--
	if (reqPara.equals("quota")) {
--%>
				<fieldset>
					<input type="hidden" name="quotationId"/>
					<!-- begin table-responsive -->
					<div class="table-responsive">
						<table class="table table-striped m-b-0">
							<thead>
								<tr style="text-align: center">
									<th>WORKORDER NO<br>เลขที่</th>
									<th>QUOTATION REF<br>อ้างอิงจาก</th>
									<th>CUST ID<br>รหัสลูกค้า</th>
									<th>REP CODE<br>รหัสพนักงานขาย</th>
									<th>DISPATCH<br>กำหนดส่ง</th>
								</tr>
							</thead>
							<tbody style="text-align: center">
								<tr>
									<td><input name="workorderId" id="workorderId" type="number" class="readonly-data form-control m-b-5" placeholder="WORKORDER NO" readonly/></td>
									<td><input name="quotationId" id="quotationId" onclick="onSelectQuotation()" type="number" class="clickable form-control m-b-5" placeholder="QUO REF" readonly/></td>
				                    <td><input name="customerId" id="customerId" onclick="getCustomerId()" type="number" class="clickable form-control m-b-5" placeholder="CUST ID" readonly/></td>
				                    <td><select name="saleNo" id="saleNo" class="form-control m-b-5">
				                    <option disabled>ไม่มีข้อมูล</option>
				                    </select></td>
				                    <td><input name="dispatch" id="dispatch" type="date" pattern="DD/MM/YYYY" class="form-control m-b-5" placeholder="DISPATCH" /></td>
								</tr>
							</tbody>
						</table>
					</div>
				</fieldset>
				<div class="row">
					<div class="col-lg-6">
						<fieldset>
				            <legend class="m-b-15">ลูกค้า</legend>
				            <div class="customer form-group row m-b-15">
				                <label class="col-form-label col-md-2">ชื่อ</label>
				                <div class="col-md-10">
				                    <input name="customerName" id="customerName" type="text" class="readonly-data form-control m-b-5" placeholder="Name" readonly/>
				                </div>
				            </div>
				            <div class="customer form-group row m-b-15">
				                <label class="col-form-label col-md-2">ที่อยู่</label>
				                <div class="col-md-10">
				                    <textarea name="customerAddr" id="customerAddr" class="readonly-data form-control" rows="2" placeholder="Address" maxlength="255" readonly></textarea>
				                </div>
				            </div>
				            <div class="customer form-group row m-b-15">
				                <label class="col-form-label col-md-2">การติดต่อ</label>
				                <div class="col-md-10">
				                    <textarea name="customerContact" id="customerContact" class="readonly-data form-control" rows="2" placeholder="Contact" maxlength="255" readonly></textarea>
				                </div>
				            </div>
				        </fieldset>
					</div>
					<div class="col-lg-6">
						<fieldset>
							<legend class="m-b-15">รายละเอียดผลิตภัณฑ์</legend>
				            <div class="form-group row m-b-15">
				                <label class="col-md-2 col-form-label">ชื่อผลิตภัณฑ์</label>
				                <div class="col-md-10">
				                    <input name="productName" id="productName" type="text" class="form-control" placeholder="Product Name" value="" />
				                    <input name="productId" id="productId" type="hidden">
				                </div>
				            </div>
				            <!-- begin table-responsive -->
				            <div class="table-responsive">
				                <table class="table table-striped m-b-0">
				                    <thead>
				                        <tr>
				                            <th>จำนวน</th>
				                            <th>จำนวนแผ่น</th>
				                            <th>ฟอร์มต่อเนื่อง</th>
				                            <th>ฟอร์มชุด</th>
				                            <th>อื่นๆ</th>
				                        </tr>
				                    </thead>
				                    <tbody>
				                        <tr>
				                            <td><input name="productQuantity" id="productQuantity" type="number" class="form-control" placeholder="Quantity" value="" /></td>
				                            <td><input name="productTpage" id="productTpage" type="number" max="9" maxlength="1" onkeyup="setPartTable($(this).val())" class="form-control" placeholder="NO. of Part" value="1" /></td>
				                            <td><input name="productConstat" id="productConstat" type="checkbox" class="form-control" value="true"></td>
				                            <td><input name="productUnitSet" id="productUnitSet" type="checkbox" class="form-control" value="true"></td>
				                            <td><input name="productOthers" id="productOthers" type="text" class="form-control" placeholder="OTHERS" /></td>
				                        </tr>
				                    </tbody>
				                </table>
				            </div>
				            <!-- end table-responsive -->
				            <!-- begin table-responsive -->
				            <div class="table-responsive">
				                <table class="table table-striped m-b-0">
				                    <thead>
				                        <tr>
				                            <th>ขนาดความลึก</th>
				                            <th>ขนาดความกว้าง</th>
				                            <th>เลขที่จาก </th>
				                            <th>ถึงเลขที่ </th>
				                        </tr>
				                    </thead>
				                    <tbody>
				                        <tr>
				                            <td><input name="productHeight" id="productHeight" type="number" class="form-control" placeholder="FORM DEPTH" /></td>
				                            <td><input name="productWidth" id="productWidth" type="number" class="form-control" placeholder="FORM WIDTH" /></td>
				                            <td><input name="productStart" id="productStart" type="number" class="form-control" placeholder="NUMBERED FROM" /></td>
				                            <td><input name="productEnd" id="productEnd" type="number" class="form-control" placeholder="NUMBERED TO" /></td>
				                        </tr>
				                    </tbody>
				                </table>
				            </div>
				            <!-- end table-responsive -->
						</fieldset>
					</div>
				</div>
				<fieldset>
					<legend class="m-b-15">รายละเอียดงาน</legend>
					<!-- begin table-responsive -->
					<div class="table-responsive">
						<table class="table table-striped m-b-0">
							<thead>
								<tr style="text-align: center">
									<th>PART NO.<br>แผ่นที่</th>
									<th>TYPE OF PAPER<br>ประเภทกระดาษ</th>
									<th>SUBTANCE<br>น้ำหนัก</th>
									<th>INK ON FACE<br>สีด้านหน้า</th>
									<th>INK BACK FACE<br>สีด้านหลัง</th>
									<th>HORZ. PERF'S<br>ปรุขวาง</th>
									<th>VERT. PERF'S<br>ปรุตรง</th>
									<th>หมายเหตุ</th>
								</tr>
							</thead>
							<tbody id="worklist">
								<tr>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td>ไม่มีข้อมูล</td>
									<td></td>
									<td></td>
									<td></td>
								</tr>
							</tbody>
						</table>
					</div>
					<!-- end table-responsive -->
				</fieldset>
				<br>
				<fieldset>
					<legend class="m-b-15">การเข้าเล่ม</legend>
					<!-- begin table-responsive -->
					<div class="table-responsive">
						<table class="table table-striped m-b-0">
							<thead>
								<tr style="text-align: center">
									<th>SPROCKET HOLE<br>เจาะรูข้าง</th>
									<th>CRIMPING<br>การยืด</th>
									<th>GLUE<br>ทากาว</th>
									<th>FORM FOLDING<br>ขนาดพับ</th>
									<th>PACKING<br>หีบห่อ</th>
								</tr>
							</thead>
							<tbody style="text-align: center">
								<tr>
									<td>
										<div class="form-check form-check-inline">
											<input class="form-check-input" type="radio" name="haveSprocetHole" id="haveSprocetHole" value="true" />
											<label class="form-check-label" for="haveSprocetHole">มี </label>
										</div>
										<div class="form-check form-check-inline">
											<input class="form-check-input" type="radio" name="haveSprocetHole" id="notHaveSprocetHole" value=false />
											<label class="form-check-label" for="nothaveSprocetHole">ไม่มี </label>
										</div>
									</td>
									<td>
										<div class="form-check form-check-inline">
											<input class="form-check-input" type="radio" name="haveCrimping" id="haveCrim" value="true" />
											<label class="form-check-label" for="haveCrim">มี </label>
										</div>
										<div class="form-check form-check-inline">
											<input class="form-check-input" type="radio" name="haveCrimping" id="notHaveCrim" value=false />
											<label class="form-check-label" for="notHaveCrim">ไม่มี </label>
										</div>
									</td>
									<td>
										<div class="form-check form-check-inline">
											<input class="form-check-input" type="radio" name="haveGlue" id="haveGlue" value="true" />
											<label class="form-check-label" for="haveGlue">มี </label>
										</div>
										<div class="form-check form-check-inline">
											<input class="form-check-input" type="radio" name="haveGlue" id="notHaveGlue" value=false />
											<label class="form-check-label" for="notHaveGlue">ไม่มี </label>
										</div>
									</td>
									<td>
										<input type="text" name="formFolding" id="formFolding" class="form-control" placeholder="Form Folding" />
									</td>
									<td width="30%">
										<div class="form-group row m-b-15">
											<div class="col-md-6">
												<input class="form-control" type="text" name="packingSet" id="packingSet" placeholder="Packing Set" />										
											</div>
											<div class="col-md-6">
												<input class="form-control" type="text" name="packingBox" id="packingBox" placeholder="Packing Box" />										
											</div>
										</div>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<!-- end table-responsive -->
					<br>
					<div class="form-group row m-b-15">
						<label class="col-form-label col-md-2">ข้อมูลเพิ่มเติม<br>Additional Detail</label>
						<div class="col-md-10">
							<textarea name="workAddition" id="workAddition" class="form-control" rows="2" placeholder="Additional Detail" maxlength="255"></textarea>
						</div>
					</div>
				</fieldset>
				<div class="customer form-group row m-b-15">
					<div class="col-md-4"></div>
					<div class="col-md-4">
						<button type="button" class="btn btn-success btn-block" onclick="onSubmit()">บันทึก</button>
						<button type="button" id="dupicate" class="btn btn-warning btn-block" style="display: none" onclick="onSubmitDuplicate()">บันทึกในรหัสใหม่</button>
					</div>
					<div class="col-md-4"></div>
				</div>
			</form>
		</div>
		<!-- end panel-body -->
	</div>
	<!-- end panel -->
</div>
<!-- end content -->
<script defer src="js/apps/workorder.js"></script>