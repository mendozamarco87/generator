package com.mendozamarco87.planguages

import com.mendozamarco87.core.IProgramingLan
import com.mendozamarco87.core.ITable
import com.mendozamarco87.replace
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

/**
 * Created by mendoza on 09/07/2018.
 */
class CSHtmlViewNew(val namespace: String) : IProgramingLan {

    var template: String = ""

    init {
        val pathTemplates = "${System.getProperty("user.dir")}/src/main/java/com/mendozamarco87/templates/CSHtmlViewNew.mm"
        val encoded = Files.readAllBytes(Paths.get(pathTemplates))
        template = String(encoded, StandardCharsets.UTF_8)
    }

    override fun createScript(table: ITable): String {
        var script = template
        var inputs = ""
        table.columns.forEach {
            inputs += if (it.name != "Id"){
                """
                <div class="form-group row">
                    <label class="col-sm-2 col-12 col-form-label">${it.name}</label>
                    <div class="col-md-6 col-sm-8 col-12">
                        <input asp-for="${it.name}" class="form-control" />
                    </div>
                </div>
                """
            } else {
                """
                <input asp-for="Id" type="hidden" />
                """
            }
        }
        script = script.replace(hashMapOf(
                "%Namespace" to this.namespace,
                "%ClassName" to table.name,
                "%Inputs" to inputs))
        return script
    }

    override fun onSuccessCreateScript(table: ITable, script: String, path: String) {
        val file = Paths.get("$path/New.cshtml")
        Files.write(file, script.toByteArray(StandardCharsets.UTF_8), StandardOpenOption.CREATE)
    }

    override fun onCompleteGenerate(path: String) {

    }
}