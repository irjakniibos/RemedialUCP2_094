package com.example.myapplication.repositori

import android.app.Application
import android.content.Context
import com.example.myapplication.room.DatabaseBuku

interface ContainerApp {
    val repositoriKategori: RepositoriKategori
    val repositoriBuku: RepositoriBuku
    val repositoriPengarang: RepositoriPengarang
    val repositoriBukuPengarang: RepositoriBukuPengarang
}

class ContainerDataApp(private val context: Context) : ContainerApp {
    override val repositoriKategori: RepositoriKategori by lazy {
        OfflineRepositoriKategori(DatabaseBuku.getDatabase(context).kategoriDao())
    }
    override val repositoriBuku: RepositoriBuku by lazy {
        OfflineRepositoriBuku(DatabaseBuku.getDatabase(context).bukuDao())
    }
    override val repositoriPengarang: RepositoriPengarang by lazy {
        OfflineRepositoriPengarang(DatabaseBuku.getDatabase(context).pengarangDao())
    }
    override val repositoriBukuPengarang: RepositoriBukuPengarang by lazy {
        OfflineRepositoriBukuPengarang(DatabaseBuku.getDatabase(context).bukuPengarangDao())
    }
}

class AplikasiBuku : Application() {
    lateinit var container: ContainerApp

    override fun onCreate() {
        super.onCreate()
        container = ContainerDataApp(this)
    }
}