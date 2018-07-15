using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Qubit.BillSystem.BackOffice.Utils;

namespace %Namespace
{
    public class %ClassNameController : BaseController
    {
        BusinessLogic.B%ClassName B%ClassName = new BusinessLogic.B%ClassName();

        public IActionResult Index()
        {
            return View();
        }

        public IActionResult GetAll(int State)
        {
            try
            {
                var list = this.B%ClassName.GetAll(State);
                return Json(list);
            }
            catch (Exception ex)
            {
                return JavaScriptResult.ShowError(ex.Message);
            }
        }

        [HttpGet]
        public IActionResult New()
        {
            var model = new Entity.E%ClassName();
            model.Id = 0;
            model.Enabled = true;
            return View(model);
        }

        [HttpPost]
        public IActionResult New(Entity.E%ClassName E%ClassName)
        {
            try
            {
                if (!ModelState.IsValid)
                    throw new Exception("Error verifique los datos ingresados.");
                E%ClassName.State = E%ClassName.Enabled ? Entity.Const.State.Enabled : Entity.Const.State.Disabled;
                E%ClassName.CreationDate = DateTime.Now;
                E%ClassName.CreationUser = Utils.Session.getUserId();
                E%ClassName.ModificationDate = DateTime.Now;
                E%ClassName.ModificationUser = Utils.Session.getUserId();
                this.B%ClassName.Save(E%ClassName);

                return JavaScriptResult.Redirect(Url.Action("Index"));
            }
            catch (Exception ex)
            {
                ModelState.AddModelError("", ex.Message);
                return JavaScriptResult.ShowError(GetModelErrors());
            }
        }

        [HttpGet]
        public IActionResult Edit(long? Id)
        {
            if (Id.HasValue)
            {
                var model = this.B%ClassName.GetById(Id.Value);
                model.Enabled = model.State == 1;
                return View("New", model);
            }
            else
            {
                return View("Index");
            }
        }

        [HttpPost]
        public IActionResult ChangeState(long? Id, int State)
        {
            try
            {
                if (Id.HasValue)
                {
                    this.B%ClassName.ChangeState(Id.Value, State);
                    return JavaScriptResult.ShowDialog("Se cambio el estado correctamente.");
                }
                else
                {
                    return JavaScriptResult.Redirect(Url.Action("Index"));
                }
            }
            catch (Exception ex)
            {
                return JavaScriptResult.ShowDialogError(ex.Message);
            }
        }
    }
}