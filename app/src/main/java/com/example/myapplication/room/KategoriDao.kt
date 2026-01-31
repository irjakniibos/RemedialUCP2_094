package com.example.myapplication.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface KategoriDao {
    @Query("SELECT * FROM tblKategori ORDER BY namaKategori ASC")
    fun getAllKategori(): Flow<List<Kategori>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(kategori: Kategori)

    @Query("SELECT * FROM tblKategori WHERE id = :id")
    fun getKategori(id: Int): Flow<Kategori>

    @Delete
    suspend fun delete(kategori: Kategori)

    @Update
    suspend fun update(kategori: Kategori)
}