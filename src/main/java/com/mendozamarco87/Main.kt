package com.mendozamarco87

import com.mendozamarco87.core.Generator
import com.mendozamarco87.databases.SqLiteDataBase
import com.mendozamarco87.databases.SqlServerDataBase
import com.mendozamarco87.planguages.Java
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by mendoza on 25/06/2017.
 */


fun main(args: Array<String>) {
    val pathGenerateFiles = "D:/marco.mendoza/generados/com/mendozamarco87/test"
    val javaPackage = "com.mendozamarco87.test"
    val path = Paths.get(pathGenerateFiles)
    if (!Files.exists(path))
        Files.createDirectories(path)

//    val database = SqlServerDataBase("PCI7R\\SQL2014", "dsoft2017", "123456", "BackOfficeNalVida")
    val database = SqlServerDataBase("DIANA-PC", "sa", "12345678", "BackOfficeNalVida")

    val tables = arrayOf("CategoriaActividad", "EstadoOportunidad", "ExtensionCI", "Ciudad",
            "Producto", "Fuente", "Oportunidad", "Actividad", "OportunidadEtapa")

    tables.forEach {
        val table = it
        val column = database.getColumns(table)
        println("Entity $table = esquema.addEntity(\"D$table\");")
        println("$table.setTableName(\"$table\");")
        column.forEach {
            var text = "$table.add${getTypeJava(it.type)}Property(\"${it.name}\").columnName(\"${it.name}\")${if (it.primaryKey) ".primaryKey()" else ""}"
            if (it.foreignKey.isEmpty()) {
                println("$text;")
            } else {
                val property = "$table${it.name}"
                text = "Property $property = $text.getProperty();"
                println(text)
                println("$table.addToOne(${it.foreignKey}, $property);")
            }
        }
        println()
    }
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
    }
    return "Default"
}


fun generateFromSqLite() {
    val pathDataBase = "D:/marco.mendoza/generados/PuntosAtencion.sqlite"
    val pathGenerateFiles = "D:/marco.mendoza/generados/com/mendozamarco87/test"
    val javaPackage = "com.mendozamarco87.test"

    val pathJsonSqliteToJava = ""
    val generator = Generator(pathGenerateFiles)
    generator.apply {
        from(SqLiteDataBase(pathDataBase))
        to(Java(javaPackage))
        generate()
    }
}

fun String.replace(map: HashMap<String, String>): String {
    var result = this
    map.forEach { old, new ->
        result = result.replace(old, new)
    }
    return result
}
