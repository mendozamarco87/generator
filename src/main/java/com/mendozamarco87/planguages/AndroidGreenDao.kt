package com.mendozamarco87.planguages

import com.mendozamarco87.core.IProgramingLan
import com.mendozamarco87.core.ITable
import com.mendozamarco87.replace
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

/**
 * Created by marco.mendoza on 22/08/2017.
 */

class AndroidGreenDao(val javaPackage: String) : IProgramingLan {

    var template: String = ""
    var content: String = ""

    init {
        val pathTemplates = "${System.getProperty("user.dir")}/src/main/java/com/mendozamarco87/templates/androidGreenDao.mm"
        val encoded = Files.readAllBytes(Paths.get(pathTemplates))
        template = String(encoded, StandardCharsets.UTF_8)
    }

    override fun createScript(iTable: ITable): String {
        var content: String = ""
        val table = iTable.name
        content += createLine("Entity $table = esquema.addEntity(\"D$table\");")
        content += createLine("$table.setTableName(\"$table\");")
        iTable.columns.forEach {
            var text = "$table.add${getTypeJava(it.type)}Property(\"${it.name}\").columnName(\"${it.name}\")${if (it.primaryKey) ".primaryKey()" else ""}"
            if (it.foreignKey.isEmpty()) {
                content += createLine("$text;")
            } else {
                val property = "$table${it.name}"
                text = "Property $property = $text.getProperty();"
                content += createLine(text)
                content += createLine("$table.addToOne(${it.foreignKey}, $property);")
            }
        }
        return content
    }

    override fun onSuccessCreateScript(table: ITable, script: String, path: String) {
        content = content + "\n" + script
    }

    override fun onCompleteGenerate(path: String) {
        content = template.replace(hashMapOf(
                "%package" to this.javaPackage,
                "%content" to content))
        val file = Paths.get("$path/EsquemaGreenDao.java")
        Files.write(file, content.toByteArray(StandardCharsets.UTF_8), StandardOpenOption.CREATE)
    }

    fun createLine(line: String): String {
        return "\n\t\t" + line
    }

    fun getTypeJava(type: String): String {
        when (type) {
            "bigint" -> return "Long"
            "varchar" -> return "String"
            "nvarchar" -> return "String"
            "datetime" -> return "Date"
            "image" -> return "ByteArray"
            "int" -> return "Int"
            "bit" -> return "Boolean"
            "decimal" -> return "Double"
        }
        return "Default"
    }

}
