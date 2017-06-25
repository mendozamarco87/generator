package com.mendozamarco87

import com.mendozamarco87.databases.SqlServerDataBase

/**
 * Created by mendoza on 25/06/2017.
 */

fun main(args : Array<String>) {
    val db = SqlServerDataBase()
    print(db.getTables())
}