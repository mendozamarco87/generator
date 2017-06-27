package com.mendozamarco87.planguages

import com.mendozamarco87.core.IProgramingLan
import com.mendozamarco87.core.ITable
import com.mendozamarco87.replace
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by mendoza on 26/06/2017.
 */
class Java(val javaPackage: String) : IProgramingLan {

    var template: String = ""

    init {
        val pathTemplates = "${System.getProperty("user.dir")}/src/main/java/com/mendozamarco87/templates/java.mm"
        val encoded = Files.readAllBytes(Paths.get(pathTemplates))
        template = String(encoded, StandardCharsets.UTF_8)
    }

    override fun createScript(table: ITable):String {
        var content = template
        content = content.replace(hashMapOf(
                "%package" to this.javaPackage,
                "%class" to table.name,
                "%vars" to createVars()))
        return content
    }

    private fun createVars(): String {
        return ""
    }
}