package com.example.myapplication.repositori

import com.example.myapplication.room.Kategori
import com.example.myapplication.room.KategoriDao
import kotlinx.coroutines.flow.Flow

interface RepositoriKategori {
    fun getAllKategoriStream(): Flow<List<Kategori>>
    suspend fun insertKategori(kategori: Kategori)
    fun getKategoriStream(id: Int): Flow<Kategori?>
    suspend fun deleteKategori(kategori: Kategori)
    suspend fun updateKategori(kategori: Kategori)
}

class OfflineRepositoriKategori(private val kategoriDao: KategoriDao) : RepositoriKategori {
    override fun getAllKategoriStream(): Flow<List<Kategori>> = kategoriDao.getAllKategori()
    override suspend fun insertKategori(kategori: Kategori) = kategoriDao.insert(kategori)
    override fun getKategoriStream(id: Int): Flow<Kategori?> = kategoriDao.getKategori(id)
    override suspend fun deleteKategori(kategori: Kategori) = kategoriDao.delete(kategori)
    override suspend fun updateKategori(kategori: Kategori) = kategoriDao.update(kategori)
}