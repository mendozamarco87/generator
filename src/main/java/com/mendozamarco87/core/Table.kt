package com.mendozamarco87.core

/**
 * Created by mendoza on 25/06/2017.
 */
data class Table(override var name: String, override var columns: ArrayList<IColumn>) : ITable {

}