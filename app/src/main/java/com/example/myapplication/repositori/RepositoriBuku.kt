package com.example.myapplication.repositori

import com.example.myapplication.room.Buku
import com.example.myapplication.room.BukuDao
import com.example.myapplication.room.BukuWithDetails
import kotlinx.coroutines.flow.Flow

interface RepositoriBuku {
    fun getAllBukuStream(): Flow<List<Buku>>
    fun getBukuByKategoriStream(kategoriId: Int): Flow<List<Buku>>
    fun getAllBukuWithDetailsStream(): Flow<List<BukuWithDetails>>
    suspend fun insertBuku(buku: Buku)
    fun getBukuStream(id: Int): Flow<Buku?>
    fun getBukuWithDetailsStream(id: Int): Flow<BukuWithDetails?>
    suspend fun deleteBuku(buku: Buku)
    suspend fun updateBuku(buku: Buku)
    suspend fun softDeleteBuku(id: Int)
}

class OfflineRepositoriBuku(private val bukuDao: BukuDao) : RepositoriBuku {
    override fun getAllBukuStream(): Flow<List<Buku>> = bukuDao.getAllBuku()
    override fun getBukuByKategoriStream(kategoriId: Int): Flow<List<Buku>> =
        bukuDao.getBukuByKategori(kategoriId)
    override fun getAllBukuWithDetailsStream(): Flow<List<BukuWithDetails>> =
        bukuDao.getAllBukuWithDetails()
    override suspend fun insertBuku(buku: Buku) = bukuDao.insert(buku)
    override fun getBukuStream(id: Int): Flow<Buku?> = bukuDao.getBuku(id)
    override fun getBukuWithDetailsStream(id: Int): Flow<BukuWithDetails?> =
        bukuDao.getBukuWithDetails(id)
    override suspend fun deleteBuku(buku: Buku) = bukuDao.delete(buku)
    override suspend fun updateBuku(buku: Buku) = bukuDao.update(buku)
    override suspend fun softDeleteBuku(id: Int) = bukuDao.softDelete(id)
}