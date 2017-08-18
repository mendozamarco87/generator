package com.mendozamarco87.databases

import com.mendozamarco87.core.*
import com.microsoft.sqlserver.jdbc.SQLServerDriver
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

/**
 * Created by mendoza on 25/06/2017.
 */
class SqlServerDataBase(server: String, user: String, password: String, database: String): IDataBase {

    private val SERVER = server
    private val USER = user
    private val PASSWORD = password
    private val DATABASE = database

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
                """select
						c.COLUMN_NAME 'Column Name',
                        c.DATA_TYPE 'Data type',
                        c.CHARACTER_MAXIMUM_LENGTH 'Max Length',
                        c.NUMERIC_PRECISION 'precision',
                        c.NUMERIC_SCALE 'scale' ,
                        c.IS_NULLABLE 'is_nullable',
                        (CHARINDEX('PK',U.CONSTRAINT_NAME))  as 'primary Key',
						t.name  as 'foreign Key'

                    from [$DATABASE].INFORMATION_SCHEMA.Columns C
                    LEFT OUTER JOIN [$DATABASE].INFORMATION_SCHEMA.KEY_COLUMN_USAGE U
                    ON C.COLUMN_NAME = U.COLUMN_NAME and c.TABLE_NAME = u.TABLE_NAME
					LEFT OUTER JOIN [$DATABASE].sys.foreign_keys k on k.name = u.CONSTRAINT_NAME
                    LEFT OUTER JOIN [$DATABASE].sys.tables t on t.object_id = k.referenced_object_id
                    WHERE C.TABLE_NAME='$tableName'""")
        val columns = ArrayList<IColumn>()

        while (res.next()) {
            var text = res.getString("foreign Key")
            val column = Column(
                    name = res.getString("Column Name"),
                    type = res.getString("Data type"),
                    length = res.getInt("Max Length"),
                    precision = res.getInt("precision"),
                    isNull = res.getBoolean("is_nullable"),
                    primaryKey = res.getBoolean("primary Key"),
                    foreignKey = res.getString("foreign Key") ?: ""
            )
            columns.add(column)
        }
        return columns
    }
}
