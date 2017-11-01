package com.mendozamarco87.planguages

import com.mendozamarco87.core.IProgramingLan
import com.mendozamarco87.core.ITable
import com.mendozamarco87.replace
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

/**
 * Created by marco.mendoza on 20/10/2017.
 */

class CSharp(val namespace: String) : IProgramingLan {

    var template: String = ""

    init{
        template = """using System;
using System.Collections.Generic;
using System.Data.Entity.Validation;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace $namespace
{
    public class L%class
    {
        %content
    }
}
            """
    }

    override fun createScript(table: ITable): String {
        val tableName:String = table.name
        var content = "public Entidades.E$tableName map(Data.$tableName data) {"
        content += createLine("if (data == null) return null;")
        content += createLine("var entidad = new Entidades.E$tableName();")
        table.columns.forEach {
            var text = "entidad.${it.name} = data.${it.name};"
            content += createLine(text)
        }
        content += createLine("return entidad;")
        content += "\n\t\t}\n\t\t"

        content += "\n\t\tpublic List<Entidades.E$tableName> mapList(List<Data.$tableName> datalist) {"
        content += createLine("if (datalist == null || !datalist.Any()) return new List<Entidades.E$tableName>();")
        content += createLine("List<Entidades.E$tableName> entidadList = new List<Entidades.E$tableName>();")
        content += createLine("foreach (var data in datalist) {")
        content += createLine("\tentidadList.Add(map(data));")
        content += createLine("}")
        content += createLine("return entidadList;")
        content += "\n\t\t}"

        return content
    }

    override fun onSuccessCreateScript(table: ITable, script: String, path: String) {
        var content = template.replace(hashMapOf(
                "%class" to table.name,
                "%content" to script))
        val file = Paths.get("$path/L${table.name}.cs")
        Files.write(file, content.toByteArray(StandardCharsets.UTF_8), StandardOpenOption.CREATE)
    }

    override fun onCompleteGenerate(path: String) {

    }

    fun createLine(line: String): String {
        return "\n\t\t\t" + line
    }
}
