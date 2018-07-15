package com.mendozamarco87.databases

import com.mendozamarco87.core.*
import com.microsoft.sqlserver.jdbc.SQLServerDriver
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

/**
 * Created by mendoza on 05/07/2018.
 */

class PostgreSQLDataBase(server: String, user: String, password: String, database: String) : IDataBase {

    private val SERVER = server
    private val USER = user
    private val PASSWORD = password
    private val DATABASE = database

    fun getConnection(): Connection {
        DriverManager.registerDriver(SQLServerDriver())
        val dbURL = "jdbc:postgresql://$SERVER/$DATABASE?user=$USER&password=$PASSWORD"
        val conn = DriverManager.getConnection(dbURL) ?: throw Exception("Could not connect")
        return conn
    }

    override fun getTables(): ArrayList<ITable> {
        val conn = getConnection()
        val query = conn.createStatement()
        val res = query.executeQuery("""
SELECT *
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_TYPE = 'BASE TABLE' and table_schema = 'public'

""")
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
                """
SELECT c.column_name,
c.data_type,
c.character_maximum_length,
c.numeric_precision,
c.numeric_scale,
c.is_nullable,
k.constraint_name,
cu.table_name,
t.constraint_type,
t.constraint_type = 'PRIMARY KEY' as is_primary_key,
c.ordinal_position
FROM information_schema.columns c left join information_schema.key_column_usage k on c.column_name = k.column_name and c.table_name = k.table_name
left join information_schema.constraint_column_usage cu on cu.constraint_name = k.constraint_name
left join information_schema.table_constraints t on t.constraint_name = cu.constraint_name
WHERE c.table_schema = 'public'
  AND c.table_name   = '$tableName'
group by c.column_name, c.data_type,
c.character_maximum_length, c.numeric_precision,
c.numeric_scale, c.is_nullable, k.constraint_name, cu.table_name, t.constraint_type, c.ordinal_position
order by c.ordinal_position
                    """)
        val columns = ArrayList<IColumn>()

        while (res.next()) {
            val column = Column(
                    name = res.getString("column_name"),
                    type = res.getString("data_type"),
                    length = res.getInt("character_maximum_length"),
                    precision = res.getInt("numeric_precision"),
                    isNull = res.getBoolean("is_nullable"),
                    primaryKey = res.getBoolean("is_primary_key"),
                    foreignKey = res.getString("table_name") ?: ""
            )
            columns.add(column)
        }
        return columns
    }

}
