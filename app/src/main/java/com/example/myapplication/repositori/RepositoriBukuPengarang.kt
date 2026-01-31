package com.example.myapplication.repositori

import com.example.myapplication.room.BukuPengarang
import com.example.myapplication.room.BukuPengarangDao
import kotlinx.coroutines.flow.Flow

interface RepositoriBukuPengarang {
    suspend fun insertBukuPengarang(bukuPengarang: BukuPengarang)
    fun getPengarangByBukuStream(bukuId: Int): Flow<List<Int>>
    suspend fun deleteBukuPengarang(bukuId: Int)
}

class OfflineRepositoriBukuPengarang(
    private val bukuPengarangDao: BukuPengarangDao
) : RepositoriBukuPengarang {
    override suspend fun insertBukuPengarang(bukuPengarang: BukuPengarang) =
        bukuPengarangDao.insert(bukuPengarang)
    override fun getPengarangByBukuStream(bukuId: Int): Flow<List<Int>> =
        bukuPengarangDao.getPengarangByBuku(bukuId)
    override suspend fun deleteBukuPengarang(bukuId: Int) =
        bukuPengarangDao.deleteByBukuId(bukuId)
}