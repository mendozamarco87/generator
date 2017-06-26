package com.mendozamarco87.databases

import com.mendozamarco87.core.*
import com.microsoft.sqlserver.jdbc.SQLServerDriver
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

/**
 * Created by mendoza on 25/06/2017.
 */
class SqlServerDataBase: IDataBase {

    val SERVER = ""
    val USER = ""
    val PASSWORD = ""
    val DATABASE = ""

    fun getConnection(): Connection {
        DriverManager.registerDriver(SQLServerDriver())
        val dbURL = "jdbc:sqlserver://$SERVER;user=$USER;password=$PASSWORD"
        val conn = DriverManager.getConnection(dbURL) ?: throw Exception("Could not connect")
        return conn
    }

    override fun getTables(): ArrayList<ITable> {
        val conn = getConnection()
        val query = conn.createStatement()
        val res = query.executeQuery("""SELECT TABLE_NAME
                                    FROM $DATABASE.INFORMATION_SCHEMA.TABLES
                                    WHERE TABLE_TYPE = 'BASE TABLE';""")
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
        val res = query.executeQuery(
                """SELECT
                        c.name 'Column Name',
                        t.Name 'Data type',
                        c.max_length 'Max Length',
                        c.precision ,
                        c.scale ,
                        c.is_nullable,
                        ISNULL(i.is_primary_key, 0) 'Primary Key'
                    FROM
                        sys.columns c
                    INNER JOIN
                        sys.types t ON c.user_type_id = t.user_type_id
                    LEFT OUTER JOIN
                        sys.index_columns ic ON ic.object_id = c.object_id AND ic.column_id = c.column_id
                    LEFT OUTER JOIN
                        sys.indexes i ON ic.object_id = i.object_id AND ic.index_id = i.index_id
                    WHERE
                        c.object_id = OBJECT_ID('$tableName')
                    """)
        val columns = ArrayList<IColumn>()
        while (res.next()) {
            var column = Column(
                    name = res.getString("Column Name"),
                    type = res.getString("Data type"),
                    length = res.getInt("Max Length"),
                    precision = res.getInt("precision"),
                    isNull = res.getBoolean("is_nullable"),
                    primaryKey = res.getBoolean("Primary Key")
            )
            columns.add(column)
        }
        return columns
    }
}
