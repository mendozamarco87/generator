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

class CSharpEntity(val namespace: String) : IProgramingLan {

    var template: String = ""

    init{
        template = """using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace %Namespace
{
    public partial class E%ClassName
    {
        public E%ClassName()
        {

        }
        %Properties

        public bool Enabled { get; set; }

    }
}
            """
    }

    override fun createScript(table: ITable): String {
        val tableName:String = table.name
        var script = ""
        table.columns.forEach {
            val text = if (!it.foreignKey.isEmpty() && !it.primaryKey) {
                "public E${it.foreignKey} E${it.foreignKey} { get; set; }"
            } else {
                "public ${getTypeCSharp(it.type)}${if (it.isNull) "?" else ""} ${it.name} { get; set; }"
            }
            script += "\n\t\t" + text
        }
        script = template.replace(hashMapOf(
                "%Namespace" to this.namespace,
                "%ClassName" to table.name,
                "%Properties" to script))
        return script
    }

    override fun onSuccessCreateScript(table: ITable, script: String, path: String) {

        val file = Paths.get("$path/E${table.name}.cs")
        Files.write(file, script.toByteArray(StandardCharsets.UTF_8), StandardOpenOption.CREATE)
    }

    override fun onCompleteGenerate(path: String) {

    }

    fun getTypeCSharp(type: String): String {
        when (type) {
            "bigint" -> return "long"
            "text" -> return "string"
            "varchar" -> return "string"
            "nvarchar" -> return "string"
            "datetime" -> return "DateTime"
            "image" -> return "ByteArray"
            "integer" -> return "int"
            "bit" -> return "bool"
            "decimal" -> return "decimal"
            "smallint" -> return "short"
            "timestamp without time zone" -> return "DateTime"
        }
        return "Default"
    }
}
