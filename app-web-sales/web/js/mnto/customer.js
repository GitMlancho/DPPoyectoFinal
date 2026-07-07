
$(document).on('click','#btnSaveCustomer',function (){    
    saveCustomer();
});

function saveCustomer(){
    var formData={};
    $("#customerForm").find("input,select").each(function (index,element){
        var fieldName=$(element).attr("id");
        var fieldValue=$(element).val();
        formData[fieldName]=fieldValue;
    });
    console.log(JSON.stringify(formData));
    var url="/app-web-sales/mnto/customer/save";
    
    fetch(url,{
        method: 'POST',
        body: JSON.stringify(formData),
        Headers: {
            'Content-Type': 'application/json'
        }
    }).then(response=>{
        if(!response.ok){
            throw new Error('Error al consumir el controlador');
        }
        return response.json();
    }).then(data=>{
        if(data.code==="200"){
            showToast("success", data.message);
        }else{
            showToast("error", data.message);
        }
    }).catch(error=>{
        showToast("error", error);
    });
    
}