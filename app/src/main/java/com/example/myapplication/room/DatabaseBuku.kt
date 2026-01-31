package com.example.myapplication.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Kategori::class, Buku::class, Pengarang::class, BukuPengarang::class],
    version = 1,
    exportSchema = false
)
abstract class DatabaseBuku : RoomDatabase() {
    abstract fun kategoriDao(): KategoriDao
    abstract fun bukuDao(): BukuDao
    abstract fun pengarangDao(): PengarangDao
    abstract fun bukuPengarangDao(): BukuPengarangDao

    companion object {
        @Volatile
        private var Instance: DatabaseBuku? = null

        fun getDatabase(context: Context): DatabaseBuku {
            return (Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    DatabaseBuku::class.java,
                    "buku_database"
                ).build().also { Instance = it }
            })
        }
    }
}