package com.mendozamarco87.core

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

/**
 * Created by mendoza on 26/06/2017.
 */
class Generator(val path: String) {
    var programingLan: IProgramingLan? = null
    var database: IDataBase? = null

    fun from(iDataBase: IDataBase): Generator {
        this.database = iDataBase
        return this
    }

    fun to(iProgramingLan: IProgramingLan): Generator {
        this.programingLan = iProgramingLan
        return this
    }

    fun generate() {
        this.database ?: throw NullPointerException("IDatabase can not be null...")
        this.programingLan ?: throw NullPointerException("IProgramingLan can not be null...")

        val path = Paths.get(this.path)
        if (!Files.exists(path))
            Files.createDirectories(path)

        this.database!!.getTables().forEach {
            val content = this.programingLan!!.createScript(it)
            val file = Paths.get("${this.path}/${it.name}.java")
            Files.write(file, content.toByteArray(StandardCharsets.UTF_8), StandardOpenOption.CREATE)
        }
    }
}