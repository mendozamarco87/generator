package com.mendozamarco87.core

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
}