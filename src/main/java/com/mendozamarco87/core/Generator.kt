package com.mendozamarco87.core

import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by mendoza on 26/06/2017.
 */
class Generator(val path: String) {
    var programingLan: IProgramingLan? = null
    var database: IDataBase? = null
    var databaseToProgramingLan: IDatabaseToProgramingLan? = null

    fun from(iDataBase: IDataBase): Generator {
        this.database = iDataBase
        return this
    }

    fun to(iProgramingLan: IProgramingLan): Generator {
        this.programingLan = iProgramingLan
        return this
    }

    fun with(databaseToProgramingLan: IDatabaseToProgramingLan): Generator {
        this.databaseToProgramingLan = databaseToProgramingLan
        return this
    }

    fun generate() {
        val path = Paths.get(this.path)
        if (!Files.exists(path))
            Files.createDirectories(path)

        this.database!!.getTables().forEach {
            val content = this.programingLan!!.createScript(it)
            this.programingLan!!.onSuccessCreateScript(it, content, this.path)
//            val file = Paths.get("${this.path}/${it.name}.java")
//            Files.write(file, content.toByteArray(StandardCharsets.UTF_8), StandardOpenOption.CREATE)
        }
        this.programingLan!!.onCompleteGenerate(this.path)
    }

    fun generate(tables: Array<String>) {
        val path = Paths.get(this.path)
        if (!Files.exists(path))
            Files.createDirectories(path)

        tables.forEach {
            val table = Table(name = it, columns = this.database!!.getColumns(it))
            val content = this.programingLan!!.createScript(table)
            this.programingLan!!.onSuccessCreateScript(table, content, this.path)
        }
        this.programingLan!!.onCompleteGenerate(this.path)
    }
}