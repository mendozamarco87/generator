@model %Namespace.E%ClassName
@{
    ViewData["title"] = "%ClassName";
}

@section Scripts {
<script>
    function onDocReady() {
        %ClassNameForm();
    }

    function %ClassNameForm() {
        ajaxForm(
            formId = "#%ClassNameForm",
            showLoading = true,
            onSuccess = function (data, textStatus, jqXHR) {

            }
        )
    }
</script>
}
<div class="row">
    <div class="col-12">
        <div class="card-box">
            <h4 class="m-t-0 header-title">Nuevo %ClassName</h4>
            <div class="row">
                <div class="col-12">
                    <div id="DivErrors" asp-validation-summary="All" class="text-danger"></div>
                </div>
            </div>
            <form id="%ClassNameForm" asp-action="New" asp-controller="%ClassName" method="post">
                %Inputs
                <div class="form-group row">
                    <div class="col-12">
                        <div class="checkbox checkbox-primary">
                            <input type="checkbox" asp-for="Enabled">
                            <label for="Enabled">
                                Habilitar
                            </label>
                        </div>
                    </div>
                </div>

                <div class="form-group row m-t-20">
                    <div class="col-12">
                        <button class="btn btn-secondary waves-effect waves-light" onclick="redirect('@Url.Action("Index", "%ClassName")')" type="button">
                            Cancelar
                        </button>
                        <button class="btn btn-primary waves-effect" type="submit" style="width:140px">
                            Guardar
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>