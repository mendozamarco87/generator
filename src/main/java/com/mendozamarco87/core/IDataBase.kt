package com.mendozamarco87.core

import java.util.*

/**
 * Created by mendoza on 25/06/2017.
 */

interface IDataBase {
    fun getTables(): ArrayList<ITable>
    fun getColumns(tableName: String): ArrayList<IColumn>
}

interface ITable {
    val name: String
    val columns: ArrayList<IColumn>
}

interface IColumn {
    val name: String
    val type: String
    val length: Int
    val precision: Int
    val isNull: Boolean
    val primaryKey: Boolean
    val foreignKey: String
}

interface IProgramingLan {
    fun createScript(table: ITable):String
    fun onSuccessCreateScript(table: ITable, script: String, path: String)
    fun onCompleteGenerate(path: String)
}

interface IDatabaseToProgramingLan {
    fun get(typeDb: String): String
}