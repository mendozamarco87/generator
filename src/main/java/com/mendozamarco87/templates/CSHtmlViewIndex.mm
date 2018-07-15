@model List<%Namespace.E%ClassName>
@{
    ViewData["title"] = "%ClassName";
}
@section Styles {
    <link href="~/lib/datatables/dataTables.min.css" rel="stylesheet" type="text/css">
}
@section Scripts {
    <script src="~/lib/datatables/dataTables.min.js"></script>
    <script>

        function onDocReady() {
            LoadTable();
        }

        var %ClassNameDataTable;
        function LoadTable() {
            %ClassNameDataTable = renderDataTable(
                "#%ClassNameDataTable",
                function () {
                    return { state: $('input[name=state]:checked').val() };
                }
            );

            $(".dataTables_filter").hide();
            $("#search").keyup(function () {
                var text = $(this).val();
                %ClassNameDataTable.search(text).draw();
            });
        }

        function ReloadTable() {
            if (%ClassNameDataTable)
                %ClassNameDataTable.ajax.reload();
        }

        function onChangeStateSuccess() {
            ReloadTable();
        }
    </script>
}
<div class="row">
    <div class="col-12">
        <div class="card-box">
            <h4 class="m-t-0 header-title">Lista %ClassName</h4>
            <div class="row">
                <div class="col-lg-6 col-md-6 col-sm-8 col-xs-12">
                    <div class="radio form-check-inline">
                        <input type="radio" id="state0" value="0" name="state" onclick="ReloadTable()">
                        <label for="state0"> Todos </label>
                    </div>
                    <div class="radio form-check-inline">
                        <input type="radio" id="state1" value="1" name="state" checked="" onclick="ReloadTable()">
                        <label for="state1"> Habilitados </label>
                    </div>
                    <div class="radio form-check-inline">
                        <input type="radio" id="state2" value="2" name="state" onclick="ReloadTable()">
                        <label for="state2"> Deshabilitados </label>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-6 col-md-6 col-sm-8 col-xs-12" style="margin-bottom:10px;">
                    @*<label for="search" class="col-form-label">Buttons</label>*@
                    <div class="input-group">
                        <input id="search" type="text" class="form-control" placeholder="Buscar.." autocomplete="off" />
                        <div class="input-group-append">
                            <button class="btn btn-dark waves-effect waves-light" type="button">
                                <i class="fa fa-search"></i>
                            </button>
                        </div>
                    </div>
                </div>
                <div class="col-lg-6 col-md-6 col-sm-4 col-xs-12">
                    <a class="btn btn-outline-dark waves-effect pull-right" href="@Url.Action("New","%ClassName")">
                        <span>Nuevo %ClassName</span>
                        <i class="fa fa-plus m-l-5"></i>
                    </a>
                </div>
            </div>
            <div class="table-responsive">
                <table id="%ClassNameDataTable" data-action="@Url.Action("GetAll","%ClassName")"
                       class="table table-sm table-striped table-bordered" cellspacing="0" style="width:100%">
                    <thead class="thead-dark">
                        <tr>
                            %TableHeaders
                            <th data-bind="Id" data-render="renderEdit" data-extra="@Url.Action("Edit","%ClassName")"
                                data-class="text-center" style="width:60px">Editar</th>
                            <th data-bind="State" data-render="renderState" data-extra="@Url.Action("ChangeState","%ClassName")"
                                data-class="text-center" style="width:60px">Estado</th>

                        </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
</div>