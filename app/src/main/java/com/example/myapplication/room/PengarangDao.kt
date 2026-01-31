package com.example.myapplication.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PengarangDao {
    @Query("SELECT * FROM tblPengarang ORDER BY namaPengarang ASC")
    fun getAllPengarang(): Flow<List<Pengarang>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(pengarang: Pengarang)

    @Query("SELECT * FROM tblPengarang WHERE id = :id")
    fun getPengarang(id: Int): Flow<Pengarang>

    @Delete
    suspend fun delete(pengarang: Pengarang)

    @Update
    suspend fun update(pengarang: Pengarang)
}