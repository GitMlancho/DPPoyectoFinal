getData();
let chartLine = null;
let chartBar = null;
function getData() {
    let url = "/app-web-sales/main/obtain";
    fetch(url).then(response => {
        if (!response.ok) {
            throw new Error('Error al consumir el controlador');
        }
        return response.json();
    }).then(data => {
        if (data.code === "200") {            
            createChart(data, null, null);
        } else {
            showToast("error", data.message);
        }
    }).catch(error => {
        showToast("error", error);
    });
}

function createChart(data, dateIni, dateEnd) {
    if (chartLine) {
        chartLine.destroy();
    }
    if (chartBar) {
        chartBar.destroy();
    }
    const lineCtx = document.getElementById("myChartLine");
    let labelsSale = [];
    let datasetSale = [];

    for (var i = 0; i < data.sales.length; i++) {
        labelsSale[i] = data.sales[i].monthName;
        datasetSale[i] = data.sales[i].total;
    }
    if (!dateIni || !dateEnd) {
        const today = new Date();
        dateEnd = today.toLocaleDateString();

        today.setMonth(today.getMonth() - 12);
        dateIni = today.toLocaleDateString();
    }

    chartLine = new Chart(lineCtx, {
        type: 'line',
        data: {
            labels: labelsSale,
            datasets: [{
                    label: 'Ventas desde ' + dateIni + ' hasta ' + dateEnd,
                    backgroundColor: 'rgb(255, 99, 132)',
                    borderColor: 'rgb(255, 99, 132)',
                    data: datasetSale
                }]
        },
        options: {}
    });
    
    let labelsProduct = [];
    let datasetProduct = [];
    
    for (var i = 0; i < data.products.length; i++) {
        labelsProduct[i] = data.products[i].nameProduct;
        datasetProduct[i] = data.products[i].quanty;
    }
    
    console.log(labelsProduct);
    const ctx = document.getElementById('myChartBar');
    chartBar = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labelsProduct,
            datasets: [{
                    label: 'Los 10 productos mas vendidos',
                    data: datasetProduct,
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.2)',
                        'rgba(54, 162, 235, 0.2)',
                        'rgba(255, 206, 86, 0.2)',
                        'rgba(75, 192, 192, 0.2)',
                        'rgba(153, 102, 255, 0.2)',
                        'rgba(255, 159, 64, 0.2)'
                    ],
                    borderColor: [
                        'rgba(255, 99, 132, 1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(75, 192, 192, 1)',
                        'rgba(153, 102, 255, 1)',
                        'rgba(255, 159, 64, 1)'
                    ],
                    borderWidth: 1
                }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });

$("#btnFilter").click(function (){
    let dateIni = $("#dateStart").val();
    let dateEnd = $("#dateEnd").val();
    
    if (!dateIni || !dateEnd) {
        showToast("error","Debe ingresar fechas validas");
        return;
    }
    
    let dataJson=JSON.stringify({dateIni: dateIni, dateEnd: dateEnd});
    
    let url = "/app-web-sales/main/obtain";
    fetch(url, {
        method: 'POST',
        body: dataJson,
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
            createChart(data,dateIni , dateEnd);
        } else {
            showToast("error", data.message);
        }
    }).catch(error => {
        showToast("error", error);
    });
    
});

}