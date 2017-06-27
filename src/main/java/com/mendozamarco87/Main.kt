package com.mendozamarco87

import com.mendozamarco87.core.Generator
import com.mendozamarco87.databases.SqLiteDataBase
import com.mendozamarco87.planguages.Java

/**
 * Created by mendoza on 25/06/2017.
 */


fun main(args: Array<String>) {
    val pathDataBase = ""
    val pathGenerateFiles = ""
    val javaPackage = ""

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
