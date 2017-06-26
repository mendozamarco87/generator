package com.mendozamarco87

import com.mendozamarco87.databases.SqLiteDataBase
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption


/**
 * Created by mendoza on 25/06/2017.
 */

fun main(args: Array<String>) {
    val db = SqLiteDataBase()
    val tables = db.getTables()

    val pathTemplates = "${System.getProperty("user.dir")}/src/main/java/com/mendozamarco87/templates/java.mm"
    val pathGenerateFiles = "D:/marco.mendoza/generados"
    val encoded = Files.readAllBytes(Paths.get(pathTemplates))
    val template = String(encoded, StandardCharsets.UTF_8)
    val javaPackage = "com.mendozamarco87.generado"

    val pathString = pathGenerateFiles + "/" + javaPackage.replace('.', '/')
    val path = Paths.get(pathString)
    if (!Files.exists(path))
        Files.createDirectories(path)

    tables.forEach {

        val file = Paths.get("$pathString/${it.name}.java")
        var content = template
        content = content.replace(hashMapOf(
                "%package" to javaPackage,
                "%class" to it.name,
                "%vars" to createVars()))
        Files.write(file, content.toByteArray(StandardCharsets.UTF_8), StandardOpenOption.CREATE)

    }
}

fun String.replace(map: HashMap<String, String>): String {
    var result = this
    map.forEach { old, new ->
        result = result.replace(old, new)
    }
    return result
}

fun createVars():String {
    return ""
}