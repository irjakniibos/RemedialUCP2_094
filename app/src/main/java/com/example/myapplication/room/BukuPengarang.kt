package com.example.myapplication.room

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "tblBukuPengarang",
    primaryKeys = ["bukuId", "pengarangId"],
    foreignKeys = [
        ForeignKey(
            entity = Buku::class,
            parentColumns = ["id"],
            childColumns = ["bukuId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Pengarang::class,
            parentColumns = ["id"],
            childColumns = ["pengarangId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BukuPengarang(
    val bukuId: Int,
    val pengarangId: Int
)