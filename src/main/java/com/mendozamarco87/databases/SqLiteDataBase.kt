package com.mendozamarco87.databases

import com.mendozamarco87.core.*
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

/**
 * Created by marco.mendoza on 26/06/2017.
 */
class SqLiteDataBase: IDataBase {
    val DB_URL = "D:/marco.mendoza/downloads/bdPuntoAtencion.sqlite"

    fun getConnection(): Connection {
        Class.forName("org.sqlite.JDBC")
        val dbURL = "jdbc:sqlite:$DB_URL"
        val conn = DriverManager.getConnection(dbURL) ?: throw Exception("Could not connect")
        return conn
    }

    override fun getTables(): ArrayList<ITable> {
        val conn = getConnection()
        val query = conn.createStatement()
        val res = query.executeQuery("""SELECT name as 'TABLE_NAME' FROM sqlite_master WHERE type='table'""")
        val tables = ArrayList<ITable>()
        while (res.next()) {
            val tableName = res.getString("TABLE_NAME")
            tables.add(Table(
                    name = tableName,
                    columns = getColumns(tableName)))
        }

        return tables
    }

    override fun getColumns(tableName: String): ArrayList<IColumn> {
        val conn = getConnection()
        val query = conn.createStatement()
        val res = query.executeQuery("""PRAGMA table_info('$tableName')""")
        val columns = ArrayList<IColumn>()
        while (res.next()) {
            var column = Column(
                    name = res.getString("name"),
                    type = res.getString("type"),
                    length = -1,
                    precision = -1,
                    isNull = res.getBoolean("notnull"),
                    primaryKey = res.getBoolean("pk")
            )
            columns.add(column)
        }
        return columns
    }
}