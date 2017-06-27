package com.mendozamarco87.databases

import com.mendozamarco87.core.*
import java.sql.Connection
import java.sql.DriverManager
import java.util.*
import java.util.regex.Pattern

/**
 * Created by marco.mendoza on 26/06/2017.
 */
class SqLiteDataBase(path: String) : IDataBase {
    private val DB_URL = path

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
                    type = getType(res.getString("type")),
                    length = getLength(res.getString("type")),
                    precision = -1,
                    isNull = res.getBoolean("notnull"),
                    primaryKey = res.getBoolean("pk")
            )
            columns.add(column)
        }
        return columns
    }

    private fun getLength(text: String): Int {
        val m = Pattern.compile("\\(([^)]+)\\)").matcher(text)
        return if (m.find()) m.group(1).toInt() else -1
    }

    private fun getType(text: String) : String {
        return text.substringBefore("(")
    }
}