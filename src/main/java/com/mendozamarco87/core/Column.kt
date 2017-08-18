package com.mendozamarco87.core

/**
 * Created by mendoza on 25/06/2017.
 */
data class Column(
        override var name: String,
        override var type: String,
        override var length: Int,
        override var precision: Int,
        override var isNull: Boolean,
        override var primaryKey: Boolean,
        override var foreignKey: String) : IColumn {

}