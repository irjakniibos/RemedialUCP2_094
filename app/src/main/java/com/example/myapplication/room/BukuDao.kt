package com.example.myapplication.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BukuDao {
    @Query("SELECT * FROM tblBuku WHERE isDeleted = 0 ORDER BY judul ASC")
    fun getAllBuku(): Flow<List<Buku>>

    @Query("SELECT * FROM tblBuku WHERE kategoriId = :kategoriId AND isDeleted = 0 ORDER BY judul ASC")
    fun getBukuByKategori(kategoriId: Int): Flow<List<Buku>>

    @Transaction
    @Query("SELECT * FROM tblBuku WHERE isDeleted = 0 ORDER BY judul ASC")
    fun getAllBukuWithDetails(): Flow<List<BukuWithDetails>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(buku: Buku)

    @Query("SELECT * FROM tblBuku WHERE id = :id")
    fun getBuku(id: Int): Flow<Buku>

    @Transaction
    @Query("SELECT * FROM tblBuku WHERE id = :id")
    fun getBukuWithDetails(id: Int): Flow<BukuWithDetails>

    @Delete
    suspend fun delete(buku: Buku)

    @Update
    suspend fun update(buku: Buku)

    @Query("UPDATE tblBuku SET isDeleted = 1 WHERE id = :id")
    suspend fun softDelete(id: Int)
}