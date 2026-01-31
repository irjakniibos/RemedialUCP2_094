package com.example.myapplication.repositori

import com.example.myapplication.room.Pengarang
import com.example.myapplication.room.PengarangDao
import kotlinx.coroutines.flow.Flow

interface RepositoriPengarang {
    fun getAllPengarangStream(): Flow<List<Pengarang>>
    suspend fun insertPengarang(pengarang: Pengarang)
    fun getPengarangStream(id: Int): Flow<Pengarang?>
    suspend fun deletePengarang(pengarang: Pengarang)
    suspend fun updatePengarang(pengarang: Pengarang)
}

class OfflineRepositoriPengarang(private val pengarangDao: PengarangDao) : RepositoriPengarang {
    override fun getAllPengarangStream(): Flow<List<Pengarang>> = pengarangDao.getAllPengarang()
    override suspend fun insertPengarang(pengarang: Pengarang) = pengarangDao.insert(pengarang)
    override fun getPengarangStream(id: Int): Flow<Pengarang?> = pengarangDao.getPengarang(id)
    override suspend fun deletePengarang(pengarang: Pengarang) = pengarangDao.delete(pengarang)
    override suspend fun updatePengarang(pengarang: Pengarang) = pengarangDao.update(pengarang)
}