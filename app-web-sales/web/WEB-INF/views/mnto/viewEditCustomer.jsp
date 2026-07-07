<!-- Modal -->
<div class="modal fade" id="modalAddCustomer" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl">
        <div class="modal-content">
            <form id="customerForm">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="staticBackdropLabel">Crear nuevo cliente</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-sm-4">
                        <label class="form-label">Codigo</label>
                        <input type="text" class="form-control" id="customerCode" readonly="">
                    </div>
                    <div class="col-sm-4">
                        <label class="form-label">Tipo de Documento</label>
                        <select class="form-select" id="typeDoc" name="typeDoc">
                            <option value="1">DNI</option>
                            <option value="4">CE</option>
                            <option value="6">RUC</option>
                            <option value="7">PASAPORTE</option>
                        </select>
                    </div>
                    <div class="col-sm-4">
                        <label class="form-label">N° Documento</label>
                        <input type="text" class="form-control" id="numDoc">
                    </div>
                    <div class="col-sm-4">
                        <label class="form-label">Nombres</label>
                        <input type="text" class="form-control" id="name">
                    </div>
                    <div class="col-sm-4">
                        <label class="form-label">Apellido Paterno</label>
                        <input type="text" class="form-control" id="firstName">
                    </div>
                    <div class="col-sm-4">
                        <label class="form-label">Apellido Materno</label>
                        <input type="text" class="form-control" id="lastName">
                    </div>
                    <div class="col-sm-4">
                        <label class="form-label">Genero</label>
                        <select class="form-select" id="selectGender">
                            <option value="M">Masculino</option>
                            <option value="F">Femenino</option>
                        </select>
                    </div>
                    <div class="col-sm-4">
                        <label class="form-label">Estado Civil</label>
                        <select class="form-select" id="selectCivilState">
                            <option value="S">SOLTERO</option>
                            <option value="C">CASADO</option>
                        </select>
                    </div>
                    <div class="col-sm-4">
                        <label class="form-label">Fecha Nacimiento</label>
                        <input type="date" class="form-control" id="birthDate">
                    </div>
                    <div class="col-sm-4">
                        <label class="form-label">Razon Social</label>
                        <input type="text" class="form-control" id="businessName">
                    </div>
                    <div class="col-sm-4">
                        <label class="form-label">Correo</label>
                        <input type="text" class="form-control" id="email">
                    </div>
                    <div class="col-sm-4">
                        <label class="form-label">Telefono</label>
                        <input type="text" class="form-control" id="numberPhone">
                    </div>
                    <div class="col-sm-4">
                        <label class="form-label">Direccion</label>
                        <input type="text" class="form-control" id="address">
                    </div>
                    <div class="col-sm-4">
                        <label class="form-label">Departamento</label>
                        <select class="form-select" id="departament">
                            <option value="0">Seleccionar</option>
                        </select>
                    </div>
                    <div class="col-sm-4">
                        <label class="form-label">Provincia</label>
                        <select class="form-select" id="province">
                            <option value="0">Seleccionar</option>
                        </select>
                    </div>
                    <div class="col-sm-4">
                        <label class="form-label">Distrito</label>
                        <select class="form-select" id="district">
                            <option value="0">Seleccionar</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" data-bs-dismiss="modal"><i class="bi bi-x-circle p-1"></i>Cancelar</button>
                <button type="button" class="btn btn-primary"id="btnSaveCustomer"><i class="bi bi-save p-1"></i>Guardar Cambios</button>
            </div>
            </form>
        </div>
    </div>
</div>