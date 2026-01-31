package com.example.myapplication.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BukuPengarangDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bukuPengarang: BukuPengarang)

    @Query("SELECT pengarangId FROM tblBukuPengarang WHERE bukuId = :bukuId")
    fun getPengarangByBuku(bukuId: Int): Flow<List<Int>>

    @Query("DELETE FROM tblBukuPengarang WHERE bukuId = :bukuId")
    suspend fun deleteByBukuId(bukuId: Int)
}