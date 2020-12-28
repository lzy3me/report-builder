var paperTop = '';
var paperList = '';
var paperDef = new Object;
var paperData = new Object;
var quotationList = new Object;
var customerList = new Object;


//
// ─── UTILS ──────────────────────────────────────────────────────────────────────
//


function onDupicate(isAdded) {
    if (isAdded) {
        $('#dupicate').show();
    } else {
        $('#dupicate').hide();
    }
}


//
// ─── INITIAL FUNCTION ON LOADED WEBSITE ─────────────────────────────────────────
//

    
setTimeout(function () {
    initNewQuotation();
}, 1000);

function initNewQuotation() {
    $('form').find("input[type=text], input[type=number], textarea").val("");
    generateQuotaId();
    onDupicate(false);
}


//
// ─── QUOTATION MANAGEMENT ───────────────────────────────────────────────────────
//

    
function generateQuotaId() {
    $.ajax({
        url: '/report-build/apis/quotation',
        data: {
            quota: 'getmaxid'
        },
        type: 'GET'
    }).done(function (data, textStatus, xhr) {
        document.form.quotationId.value = data[0].id;
        getPaperList();
    });
}

function onEditQuotation() {
    if (quotationList !== null) {
        $.ajax({
            url: '/report-build/apis/quotation',
            data: {
                quota: 'list',
                isapprove: ''
            },
            type: 'GET'
        }).done(function (data, status, xhr) {
            quotationList = data;
            listQuotation();
        });
    } else {
        listQuotation();
    }
}

function listQuotation() {
    var obj = '';
    $.each(quotationList, function (index, val) {
        obj += '<option value="'+val.quotationId+'">รหัสใบเสนอราคา '+val.quotationId+' -- ชื่อผลิตภัณฑ์ '+val.productObject.productName+'</option>'
    });
    $('#type').text("ใบเสนอราคา");
    $('#customer_id').html(obj);
    $('#btn-footer-set').html('<button type="button" onclick="selectQuotationForEdit($(\'#customer_id\').val())" class="btn btn-primary" data-dismiss="modal">เลือก</button>'
    +'<button type="button" class="btn btn-secondary" data-dismiss="modal">ยกเลิก</button>');
    $('#listCustomer').modal();
}

function selectQuotationForEdit(quotationId) {
    $('form').find("input[type=text], input[type=number], textarea").val("");
    $.ajax({
        url: '/report-build/apis/quotation',
        data: {
            quota: 'getquota',
            id: quotationId
        },
        type: 'GET'
    }).done(function (data, status, xhr) {
        $.each(data, function (index, val) {
            $('#quotationId').val(val.quotationId);
            $('#saleNo').val(val.saleId);
            // $('#dispatch').val(val.dispatchDate);

            $('#customerId').val(val.customerObject.customerId);
            $('#customerName').val(val.customerObject.customerOrgName);
            $('#customerAddr').val(val.customerObject.customerAddr);
            $('#customerContact').val(val.customerObject.customerContact);

            $('#productId').val(val.productObject.productId);
            $('#productName').val(val.productObject.productName);
            $('#productQuantity').val(val.productObject.productQuantity);
            $('#productTpage').val(val.productObject.productNOofPart);
            $('#productConstat').prop('checked', val.productObject.productConstat==='1'?true:false);
            $('#productUnitSet').prop('checked', val.productObject.productUnitSet==='1'?true:false);
            $('#productOthers').val(val.productObject.productOther);
            $('#productHeight').val(val.productObject.productHeight);
            $('#productWidth').val(val.productObject.productWidth);
            $('#productStart').val(val.productObject.productNOstart);
            $('#productEnd').val(val.productObject.productNOend);
            
            $('#priceDetail').val(val.priceDetail);
            $('#quotationAdd').val(val.additionalDetail);
            $('#approve').val(val.approve);

            setPartTable(val.productObject.productNOofPart);
            getProductSpecForEdit(val.productObject.productId);
        });
        onDupicate(true);
    });  
}


//
// ─── PAPER PART MANAGEMENT ──────────────────────────────────────────────────────
//

    
function getPaperList() {
    if (paperList === '') {
        $.ajax({
            url: '/report-build/apis/all',
            data: {
                p: 'paperlist'
            },
            type: 'GET'
        }).done(function (data, textStatus, xhr) {
            $.each(data, function (index, val) {
                paperList += '<option value="'+val.paperId+'">'+val.paperType+' สี'+val.paperColor+'</option>';
                paperData[val.paperId] = val.paperWeight;
            });
            getDefaultPaper();
        });	
    }
}

function getDefaultPaper() {
    paperTop = '';
    paperBottom = '';
    
    $.ajax({
        url: '/report-build/apis/all',
        data: {
            p: 'paperdef'
        },
        type: 'GET'
    }).done(function (data, textStatus, xhr) {
        $.each(data, function (index, val) {
            if (index == 0) {
                paperTop += '<tr>';
                paperTop += '<td>1</td>';
                paperTop += '<td>'+val.paperType+' '+val.paperColor+'<input type="hidden" name="psType0" id="psType0" value="'+val.paperId+'"></td>';
                paperTop += '<td><input type="text" class="readonly-data form-control" name="psSubstance0" id="psSubstance0" value="'+val.paperWeight+'" readonly></td>';
                //paperTop += '<td><input type="text" class="readonly-data form-control" name="psPrice0" value="'+val.paperPrice+'" readonly></td>'
                paperTop += '<td><input type="text" class="form-control" name="psInkOnFace0" id="psInkOnFace0" value=""></td>';
                paperTop += '<td><input type="text" class="form-control" name="psInkBackFace0" id="psInkBackFace0" value=""></td>';
                paperTop += '<td><input type="checkbox" class="form-control" name="psHorz0" id="psHorz0" value="true"></td>';
                paperTop += '<td><input type="checkbox" class="form-control" name="psVert0" id="psVert0" value="true"></td>';
                paperTop += '<td><input type="text" class="form-control" name="psRemark0" id="psRemark0" value=""></td>';
                paperTop += '</td>';
            } else {
                paperDef = val;
            }
        });
        getSaleList();
        setPartTable($('#productTpage').val());
    });
}

function onSelectPaperThenGetPaperWeight(docVal, docRenderId) {
    var val = docVal.value;
    if (typeof(paperData[val]) !== 'undefined') {
        $(docRenderId).val(paperData[val]);
    }
}


//
// ─── CUSTOMER MANAGEMENT ────────────────────────────────────────────────────────
//

    
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
    $('#btn-footer-set').html('<a href="/report-build/customer" class="btn btn-primary">เพิ่มลูกค้าใหม่ <i class="fas fa-external-link-alt"></i></a>'
    +'<button type="button" onclick="onSelectCustomer($(\'#customer_id\').val())" class="btn btn-primary" data-dismiss="modal">เลือก</button>'
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
            document.form.customerId.value = val.customerId;
            document.form.customerName.value = val.customerOrgName;
            document.form.customerAddr.value = val.customerAddr;
            document.form.customerContact.value = val.customerContact;
        });
    });
}


//
// ─── PRODUCT SPECIFICATION MANAGEMENT ───────────────────────────────────────────
//

    
function getProductSpecForEdit(productId) {
    var table = '';
    $.ajax({
        url: '/report-build/apis/product',
        data: {
            p: 'getps',
            productId: productId
        },
        type: 'GET'
    }).done(function (data, textStatus, xhr) {
        console.table(data);
        $.each(data, function (index, val) {
            $('#psType'+index).val(val.paperId);
            $('#psSubstance'+index).val(paperData[val.paperId]);
            $('#psInkOnFace'+index).val(val.psInkOnFace);
            $('#psInkBackFace'+index).val(val.psInkBackFace);
            $('#psHorz'+index).prop('checked', val.psHorzPerf==='1'?true:false);
            $('#psVert'+index).prop('checked', val.psVertPerf==='1'?true:false);
            $('#psRemark'+index).val(val.psRemark);
        });
    });
}

function setPartTable(docVal) {
    var i = docVal;
    var obj = '';
    for (var loop=1; loop<i; loop++) {
        if (loop < i-1) {				
            obj += '<tr>'
            obj += '<td>'+(loop+1)+'</td>'
            obj += '<td><select class="form-control" name="psType'+loop+'" id="psType'+loop+'"  '
            obj += 'onclick="onSelectPaperThenGetPaperWeight(this, psSubstance'+loop+')" data-style="btn-white">'
            obj += paperList +'</select></td>'
            obj += '<td><input type="text" class="readonly-data form-control" name="psSubstance'+loop+'" id="psSubstance'+loop+'" readonly></td>'
            //obj += '<td><input type="text" class="readonly-data form-control" name="psPrice'+loop+'" readonly></td>'
            obj += '<td><input type="text" class="form-control" name="psInkOnFace'+loop+'" id="psInkOnFace'+loop+'"></td>'
            obj += '<td><input type="text" class="form-control" name="psInkBackFace'+loop+'" id="psInkBackFace'+loop+'"></td>'
            obj += '<td><input type="checkbox" class="form-control" name="psHorz'+loop+'" id="psHorz'+loop+'" value="true"></td>'
            obj += '<td><input type="checkbox" class="form-control" name="psVert'+loop+'" id="psVert'+loop+'" value="true"></td>'
            obj += '<td><input type="text" class="form-control" name="psRemark'+loop+'" id="psRemark'+loop+'"></td>'
            obj += '</td>'
        } else {
            obj += '<tr>'
            obj += '<td>'+(loop+1)+'</td>'
            obj += '<td>'+paperDef.paperType+' '+paperDef.paperColor+'<input type="hidden" name="psType'+loop+'" value="'+paperDef.paperId+'"></td></td>'
            obj += '<td><input type="text" class="readonly-data form-control" name="psSubstance'+loop+'" value="'+paperDef.paperWeight+'" readonly></td>'
            //obj += '<td><input type="text" class="readonly-data form-control" name="psPrice'+loop+'" value="'+paperDef.paperPrice+'" readonly></td>'
            obj += '<td><input type="text" class="form-control" name="psInkOnFace'+loop+'" id="psInkOnFace'+loop+'"></td>'
            obj += '<td><input type="text" class="form-control" name="psInkBackFace'+loop+'" id="psInkBackFace'+loop+'"></td>'
            obj += '<td><input type="checkbox" class="form-control" name="psHorz'+loop+'" id="psHorz'+loop+'" value="true"></td>'
            obj += '<td><input type="checkbox" class="form-control" name="psVert'+loop+'" id="psVert'+loop+'" value="true"></td>'
            obj += '<td><input type="text" class="form-control" name="psRemark'+loop+'" id="psRemark'+loop+'"></td>'
            obj += '</td>'
        }
    }
    $('#worklist').html(paperTop+obj);
    $('.selectpicker').selectpicker('render');
}


//
// ─── SALE SECTION - GET SALE DATA LIST FROM DATABASE ────────────────────────────
//

    
function getSaleList() {
    $.ajax({
        url: '/report-build/apis/all',
        data: {
            p: 'salelist'
        },
        type: 'GET'
    }).done(function (data, textStatus, xhr) {
        var obj = '';
        $.each(data, function (index, val) {
            obj += '<option value="'+val.saleId+'">['+val.saleNo+'] '+val.saleFirstName+'</option>';
        });
        $('#saleNo').html(obj);
    });
}


//
// ─── SUBMIT FORM TO CHECK IF DATA EXIST OR NEW DATA ─────────────────────────────
//


function onSubmit() {
    var formData = $("#form").serializeArray();
    
    var obj = new Object
    obj['quota'] = 'addorupdate'
    
    for (var i in formData) {
        obj[formData[i].name] = formData[i].value;
    }
    console.table(obj);
    
    $.ajax({
        url: '/report-build/apis/quotation',
        data: obj,
        type: 'GET'
    }).done(function (data, textStatus, xhr) {
        var state = xhr.getResponseHeader('state');
        if (state === 'OKAY') {
            swal("Completed!", "ใบเสนอราคาที่ "+$('#quotationId').val()+" เพิ่มเข้าฐานข้อมูลเรียบร้อยแล้ว", "success");
            onDupicate(true);
        } else if (state === 'UPDATED') {
            swal("Completed!", "ใบเสนอราคาที่ "+$('#quotationId').val()+" ปรับปรุงข้อมูลเรียบร้อยแล้ว", "success");
            onDupicate(true);
        } else if (state === 'AREADY') {
            swal("Failed!", "มีชื่ออยู่ในข้อมูลลูกค้าแล้ว", "error");
        } else {
            swal("Failed!", state, "error");
        }
    })
    
}

function onSubmitDuplicate() {
    swal({
        title: "คุณต้องการทำซ้ำใบเสนอราคานี้หรือไม่",
        icon: "info",
        dangerMode: true,
        buttons: true
    }).then(
        function (isDuplicate) {
            if (isDuplicate) {
                $.ajax({
                    url: '/report-build/apis/quotation',
                    data: {
                        quota: 'getmaxid'
                    },
                    type: 'GET'
                }).done(function (data, textStatus, xhr) {
                    document.form.quotationId.value = data[0].id;

                    var formData = $("#form").serializeArray();
    
                    var obj = new Object
                    obj['quota'] = 'addorupdate'
                    
                    for (var i in formData) {
                        obj[formData[i].name] = formData[i].value;
                    }

                    $.ajax({
                        url: '/report-build/apis/quotation',
                        data: obj,
                        type: 'GET'
                    }).done(function (data, textStatus, xhr) {
                        var state = xhr.getResponseHeader('state');
                        if (state === 'OKAY') {
                            swal('Completed!', 'บันทึกซ้ำเรียบร้อยแล้ว', 'success');
                            onDupicate(true);
                        } else {
                            swal("Failed!", state, "error");
                        }
                    });
                })
            }
        }
    );
}

// ────────────────────────────────────────────────────────────────────────────────
