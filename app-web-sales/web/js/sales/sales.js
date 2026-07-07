$("#linkSales").click(function () {
    $("#title").html('<i class="bi bi-cart4"></i> Modulo de Ventas');
    $("#contenido").empty();
    $("#contenido").load("/app-web-sales/sale/", function () {
        getSale();
        constrolNumberDocument(8);
    });
});

function getSale() {
    var url = "/app-web-sales/sale/obtain";
    fetch(url).then(response => {
        if (!response.ok) {
            throw new Error('Error al consumir el controlador');
        }
        return response.json();
    }).then(data => {
        if (data.code === "200") {
            showSale(data.sale);
            showCustomer(data.sale.customer);
        } else {
            showToast("error", data.message);
        }
    }).catch(error => {
        showToast("error", error);
    });
}

function showCustomer(customer) {
    if (customer && customer.customerId > 0) {
        $("#typedoc opcion[value='" + customer.typedoc + ']').prop('selected', true);
        $("#numdoc").val(customer.numdoc);
        $("#customerName").val(customer.name + ' ' + customer.firstName + ' ' + customer.lastName);
        if (customer.typedoc === "6") {
            $("#customerName").val(customer.bussinesName);
        }
    } else {
        $("#numdoc").val('');
        $("#customerName").val('');
        $("#numdoc").focus();
    }
}
$(document).on('click', '#btnSearchProduct', function () {
    var productCode = $("#productCode").val();
    var url = "/app-web-sales/mnto/product/obtain/code/?code=" + productCode;

    fetch(url).then(response => {
        if (!response.ok) {
            throw new Error('Error al consumir el controlador');
        }
        return response.json();
    }).then(data => {
        console.log("data", data);
        if (data.code === "200") {
            $("#productId").val(data.product.productId);
            $("#productName").val(data.product.name);
            $("#productDescription").val(data.product.description);
            $("#productPrice").val(data.product.price);
            $("#productStock").val(data.product.stock);
        } else {
            clearProductForm();
            showToast("error", data.message);
        }
    }).catch(error => {
        showToast("error", error);
    });
});

function clearProductForm() {
    $("#productId").val('');
    $("#productCode").val('');
    $("#productName").val('');
    $("#productDescription").val('');
    $("#productPrice").val('');
    $("#productStock").val('');
    $("#productCode").focus();
}

$(document).on('click', '#btnSearchCustomer', function () {
    var url = "/app-web-sales/sale/obtain/customer";
    var formData = {};
    formData["typedoc"] = $("#typedoc").val();
    formData["numdoc"] = $("#numdoc").val();

    fetch(url, {
        method: 'POST',
        body: JSON.stringify(formData),
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if (!response.ok) {
            throw new Error('Error al consumir el controlador');
        }
        return response.json();
    }).then(data => {
        if (data.code === "200") {
            if (data.sale.customer.customerId === 0) {
                $("#customerName").val('');
                $("#numdoc").focus();
                Swal.fire({
                    title: "Mensaje",
                    text: "Cliente no existe, deseas registrar?",
                    icon: "warning",
                    showCancelButton: true,
                    confirmButtonColor: "#3085d6",
                    cancelButtonColor: "#d33",
                    confirmButtonText: "SI",
                    cancelButtonText: "NO"
                }).then((result) => {
                    if (result.isConfirmed) {
                        $("#contentModalCustomer").empty();
                        $("#contentModalCustomer").load("/app-web-sales/mnto/customer/edit", function () {
                            $("#modalAddCustomer").modal("show");
                        });
                    }
                });
            } else {
                showCustomer(data.sale.customer);
            }
            showSale(data.sale);
        } else {

        }
    }).catch(error => {
        console.log("Error:", error);
    });
});

$(document).on('click', '#btnAddProductoToCart', function () {

    var url = "/app-web-sales/sale/add";

    var dataSaleDetailProd = {};
    dataSaleDetailProd["productId"] = $("#productId").val();
    dataSaleDetailProd["code"] = $("#productCode").val();
    dataSaleDetailProd["name"] = $("#productName").val();
    dataSaleDetailProd["description"] = $("#productDescription").val();
    dataSaleDetailProd["price"] = $("#productPrice").val();
    dataSaleDetailProd["stock"] = $("#productStock").val();


    var dataSaleDetail = {};
    dataSaleDetail["product"] = dataSaleDetailProd;
    dataSaleDetail["price"] = $("#productPrice").val();
    dataSaleDetail["quanty"] = $("#productQuanty").val();

    fetch(url, {
        method: 'POST',
        body: JSON.stringify(dataSaleDetail),
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if (!response.ok) {
            throw new Error('Error al consumir el controlador');
        }
        return response.json();
    }).then(data => {
        console.log("data", data);
        if (data.code === "200") {
            showToast("success", data.message);
            showSale(data.sale);
            clearProductForm();
        } else {
            showToast("error", data.message);
        }
    }).catch(error => {
        showToast("error", error);
    });
});
function showSale(sale) {
    $('#tableCart tbody').empty();
    if (sale && Array.isArray(sale.details) && sale.details.length > 0) {
        $.each(sale.details, function (index, value) {
            var row = $('<tr>');
            row.append($('<td class="text-center">').text(index + 1));
            row.append($('<td class="text-center" id="detailsProductCode">').text(value.product.code));
            row.append($('<td>').text(value.product.name));
            row.append($('<td class="text-center">').text(value.price));
            row.append($('<td class="text-center">').append($('<input type="number" id="detalleProductoQuantyId" class="form-control text-center" style="width:80px">').attr('min', '1').val(value.quanty)));
            row.append($('<td class="text-center">').text(value.total));
            row.append($('<td class="text-center">')
                    .append('<a class="btn btn-danger btn-sm ms-1" onclick="deleteProductDetails(' + value.product.productId + ')"><i class="bi bi-trash"></i></a>'));
            $("#tableCart tbody").append(row);
        });
    }
    if (sale) {
        $("#montoNeto").text(sale.amount_net.toFixed(2));
        $("#montoIgv").text(sale.amount_iva.toFixed(2));
        $("#montoTotal").text(sale.amount_total.toFixed(2));
        showCustomer(sale.customer);
    }
}

$(document).on('click', '#detalleProductoQuantyId', function () {

    var url = "/app-web-sales/sale/update";

    var $row = $(this).closest('tr');

    var dataSaleDetailProd = {};
    dataSaleDetailProd["productId"] = 0;
    dataSaleDetailProd["code"] = $row.find("#detailsProductCode").text();

    var dataSaleDetail = {};
    dataSaleDetail["product"] = dataSaleDetailProd;
    dataSaleDetail["quanty"] = $(this).val();

    fetch(url, {
        method: 'POST',
        body: JSON.stringify(dataSaleDetail),
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if (!response.ok) {
            throw new Error('Error al consumir el controlador');
        }
        return response.json();
    }).then(data => {
        console.log("data", data);
        if (data.code === "200") {
            showToast("success", data.message);
            showSale(data.sale);
        } else {
            showToast("error", data.message);
            showSale(data.sale);
        }
    }).catch(error => {
        showToast("error", error);
    });
});

function deleteProductDetails(productId) {
    console.log(productId);
    Swal.fire({
        title: "Estas seguro de eliminar?",
        text: "Recuerda! no podras recuperar.",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "SI",
        cancelButtonText: "NO"
    }).then((result) => {
        if (result.isConfirmed) {
            confirmDeleteProductDetails(productId);
        }
    });
}

function confirmDeleteProductDetails(productId) {
    var url = "/app-web-sales/sale/delete/?id=" + productId;

    fetch(url).then(response => {
        if (!response.ok) {
            throw new Error('Error al consumir el controlador');
        }
        return response.json();
    }).then(data => {
        if (data.code === "200") {
            showToast("success", data.message);
            showSale(data.sale);
        }
    }).catch(error => {
        showToast("error", error);
    });
}

$(document).on('click', '#btnCancelarSale', function () {

    var url = "/app-web-sales/sale/cancel";


    fetch(url).then(response => {
        if (!response.ok) {
            throw new Error('Error al consumir el controlador');
        }
        return response.json();
    }).then(data => {
        if (data.code === "200") {
            showToast("success", data.message);           
            showSale(data.sale);
        } else {
            showToast("warning", data.message);
        }
    }).catch(error => {
        showToast("error", error);
    });
});

$(document).on('click', '#btnGenerarSale', function () {

    var url = "/app-web-sales/sale/save";


    fetch(url).then(response => {
        if (!response.ok) {
            throw new Error('Error al consumir el controlador');
        }
        return response.json();
    }).then(data => {
        if (data.code === "200") {
            showToast("success", data.message);
            window.open("/app-web-sales/download?filename=" + encodeURIComponent(data.sale.filename), '_blank');
            showSale(data.sale);
        } else {
            showToast("warning", data.message);
        }
    }).catch(error => {
        showToast("error", error);
    });
});


$(document).on('change', '#typedoc', function () {
    $("#numdoc").val('');
    $("#customerName").val('');
    let tipo = $(this).val();
    let maxlen = 8;
    if (tipo === '1') {
        maxlen = 8;
    } else if (tipo === '4') {
        maxlen = 9;
    } else if (tipo === '6') {
        maxlen = 11;
    } else if (tipo === '7') {
        maxlen = 12;
    }
    $("#numdoc").attr('maxlength', maxlen);
    $("#numdoc").focus();
    constrolNumberDocument(maxlen);
});
function constrolNumberDocument(maxlen) {
    $("#numdoc").off('input').on('input', function () {
        this.value = this.value.replace(/\D/g, '');
        if (this.value.length > maxlen) {
            this.value = this.value.slice(0, maxlen);
        }
    });
}