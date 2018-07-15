package com.mendozamarco87

import com.mendozamarco87.core.Generator
import com.mendozamarco87.databases.PostgreSQLDataBase
import com.mendozamarco87.databases.SqLiteDataBase
import com.mendozamarco87.databases.SqlServerDataBase
import com.mendozamarco87.planguages.*

/**
 * Created by mendoza on 25/06/2017.
 */


fun main(args: Array<String>) {
//    generateFromSqlServerToAndroidGreenDao()
//    generateFromSqlServerToCSharpSqliteEntityFramework()
//    generateFromSqlServerToCSharpMapper()
    generateFromPostgresqlToCSharp()
}

fun generateFromPostgresqlToCSharp() {
    val pathProject = "C:/Users/mendo/Documents/Workspace/NetCore/bill-system/Qubit.BillSystem"
    val namespace = "Qubit.BillSystem"
    val table = "Activity"
    val tables = arrayOf(table)
    var generator = Generator("$pathProject/$namespace.BackOffice/Controllers")
    generator.apply {
        from(PostgreSQLDataBase("localhost:5432","postgres", "root", "dbBillSystem"))
        to(CSharpController(namespace + ".BackOffice.Controllers"))
        generate(tables)
    }
    generator = Generator("$pathProject/$namespace.BackOffice/Views/$table")
    generator.apply {
        from(PostgreSQLDataBase("localhost:5432","postgres", "root", "dbBillSystem"))
        to(CSHtmlViewIndex(namespace + ".Entity"))
        generate(tables)
    }
    generator = Generator("$pathProject/$namespace.BackOffice/Views/$table")
    generator.apply {
        from(PostgreSQLDataBase("localhost:5432","postgres", "root", "dbBillSystem"))
        to(CSHtmlViewNew(namespace + ".Entity"))
        generate(tables)
    }
    generator = Generator("$pathProject/$namespace.BusinessLogic")
    generator.apply {
        from(PostgreSQLDataBase("localhost:5432","postgres", "root", "dbBillSystem"))
        to(CSharpBLogic(namespace + ".BusinessLogic"))
        generate(tables)
    }
    generator = Generator("$pathProject/$namespace.Entity")
    generator.apply {
        from(PostgreSQLDataBase("localhost:5432","postgres", "root", "dbBillSystem"))
        to(CSharpEntity(namespace + ".Entity"))
        generate(tables)
    }
}

fun generateFromSqlServerToCSharpSqliteEntityFramework() {
    val tables = arrayOf("CategoriaActividad", "EstadoActividad", "EstadoOportunidad", "ExtensionCI", "Ciudad", "MotivoNoCierre",
            "Producto", "Fuente", "TipoClasificacion", "Clasificacion", "Etapa", "EtapaEmbudo", "Oportunidad", "Actividad", "OportunidadEtapa",
            "ActividadHistorico", "OportunidadClasificacion", "Parametro")
    val db = SqlServerDataBase("PCI7R\\SQL2014", "dsoft2017", "123456", "BackOfficeNalVida")
//    val db = SqlServerDataBase("DIANA-PC", "sa", "12345678", "BackOfficeNalVida")

    var script = ""
    tables.forEach {
        val tableName = it
        val columns = db.getColumns(tableName)
        var content = """
List<Data.$tableName> dLista$tableName= dbData.$tableName.ToList();
foreach (var item in dLista$tableName)
{
    DataSqlite.$tableName model = new DataSqlite.$tableName();
"""
        columns.forEach {
            var isNull = ""
            if (it.isNull)
                isNull = ".HasValue ?"


            if (it.type == "datetime") {
                content += "model.${it.name} = "
                content += if (isNull.isEmpty()) "DateTimeToLong(item.${it.name})" else "item.${it.name}$isNull DateTimeToLong(item.${it.name}.Value) : null"
            } else if (it.type == "bit") {
                content += "model.${it.name} = item.${it.name}"
                content += if (isNull.isEmpty()) "? 1 : 0" else "$isNull (item.${it.name}.Value? 1 : 0) : 0"
            } else {
                content += "model.${it.name} = item.${it.name}"
            }

            content += ";\n"
        }
        println(content)
        println("db.$tableName.Add(model);")
        println("}")
//        println("db.SaveChanges();")
    }
}

fun generateFromSqlServerToCSharpSqliteConnection() {
    val tables = arrayOf("CategoriaActividad", "EstadoActividad", "EstadoOportunidad", "ExtensionCI", "Ciudad", "MotivoNoCierre",
            "Producto", "Fuente", "TipoClasificacion", "Clasificacion", "Etapa", "EtapaEmbudo", "Oportunidad", "Actividad", "OportunidadEtapa")
    val db = SqlServerDataBase("PCI7R\\SQL2014", "dsoft2017", "123456", "BackOfficeNalVida")
//    val db = SqlServerDataBase("DIANA-PC", "sa", "12345678", "BackOfficeNalVida")

    var script = ""
    tables.forEach {
        val tableName = it
        val columns = db.getColumns(tableName)
        var content = """
List<Data.$tableName> dLista$tableName= db.$tableName.ToList();
foreach (var item in dLista$tableName)
{
    cmd.CommandText = "Insert into $tableName Values("""
        var parameters = ""
        columns.forEach {
            content += "@${it.name},"
            parameters += "cmd.Parameters.Add(new SQLiteParameter(\"${it.name}\", item.${it.name}));\n"
        }
        println(content.substring(0, content.length - 1) + ")\";")
        println(parameters)
        println("cmd.ExecuteNonQuery();")
        println("}")
    }
}

fun generateFromSqlServerToAndroidGreenDao() {
    val pathGenerateFiles = "D:/marco.mendoza/workspace-kotlin/generator/src/main/java/com/mendozamarco87"
    val javaPackage = "com.mendozamarco87"
    val tables = arrayOf("Parametro")
    val generator = Generator(pathGenerateFiles)
    generator.apply {
        from(SqlServerDataBase("PCI7R\\SQL2014", "dsoft2017", "123456", "BackOfficeNalVida"))
        to(AndroidGreenDao(javaPackage))
        generate(tables)
    }
}

fun generateFromSqlServerToCSharpMapper(){
    val pathGenerateFiles = "D:/marco.mendoza/workspace-kotlin/generator/src/main/java/com/mendozamarco87"
    val namespace = "Logica"
    val tables = arrayOf("InsumoCompetencia")
    val generator = Generator(pathGenerateFiles)
    generator.apply {
        from(SqlServerDataBase("172.21.15.103", "usr_desa", "des@rr0ll02013", "dbDatecPricing"))
        to(CSharp(namespace))
        generate(tables)
    }
}

fun generateFromSqLiteToJava() {
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
