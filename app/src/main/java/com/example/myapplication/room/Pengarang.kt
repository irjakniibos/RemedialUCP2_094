package com.example.myapplication.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tblPengarang")
data class Pengarang(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val namaPengarang: String
)