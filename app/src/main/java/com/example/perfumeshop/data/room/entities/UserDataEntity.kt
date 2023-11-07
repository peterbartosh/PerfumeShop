package com.example.perfumeshop.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_data_table")
data class UserDataEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "first_name")
    var firstName: String? = null,

    @ColumnInfo(name = "second_name")
    var secondName: String? = null,

    @ColumnInfo(name = "phone_number")
    var phoneNumber : String? = null,

    @ColumnInfo(name = "street")
    var street : String? = null,

    @ColumnInfo(name = "home")
    var home : String? = null,

    @ColumnInfo(name = "flat")
    var flat : String? = null,

    @ColumnInfo(name = "sex")
    var sex: String? = null,
)
