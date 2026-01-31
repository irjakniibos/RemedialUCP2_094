package com.example.myapplication.room

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class BukuWithDetails(
    @Embedded val buku: Buku,
    @Relation(
        parentColumn = "kategoriId",
        entityColumn = "id"
    )
    val kategori: Kategori,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = BukuPengarang::class,
            parentColumn = "bukuId",
            entityColumn = "pengarangId"
        )
    )
    val pengarangList: List<Pengarang>
)