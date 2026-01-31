package com.example.myapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repositori.RepositoriBuku
import com.example.myapplication.repositori.RepositoriBukuPengarang
import com.example.myapplication.repositori.RepositoriKategori
import com.example.myapplication.repositori.RepositoriPengarang
import com.example.myapplication.room.Buku
import com.example.myapplication.room.BukuPengarang
import com.example.myapplication.room.Kategori
import com.example.myapplication.room.Pengarang
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class BukuEntryViewModel(
    private val repositoriBuku: RepositoriBuku,
    repositoriKategori: RepositoriKategori,
    repositoriPengarang: RepositoriPengarang,
    private val repositoriBukuPengarang: RepositoriBukuPengarang
) : ViewModel() {

    var uiStateBuku by mutableStateOf(UIStateBuku())

    val listKategori: StateFlow<List<Kategori>> = repositoriKategori.getAllKategoriStream()
        .filterNotNull()
        .map { it.toList() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = listOf()
        )

    val listPengarang: StateFlow<List<Pengarang>> = repositoriPengarang.getAllPengarangStream()
        .filterNotNull()
        .map { it.toList() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = listOf()
        )

    private fun validasiInput(uiState: DetailBuku = uiStateBuku.detailBuku): Boolean {
        return with(uiState) {
            judul.isNotBlank() &&
                    tahunTerbit > 0 &&
                    kategoriId > 0 &&
                    pengarangId > 0 &&
                    status.isNotBlank()
        }
    }

    fun updateUiState(detailBuku: DetailBuku) {
        uiStateBuku = UIStateBuku(
            detailBuku = detailBuku,
            isEntryValid = validasiInput(detailBuku)
        )
    }

    suspend fun saveBuku() {
        if (validasiInput()) {
            val buku = uiStateBuku.detailBuku.toBuku()
            repositoriBuku.insertBuku(buku)

            val lastBuku = repositoriBuku.getAllBukuStream()
            lastBuku.collect { listBuku ->
                if (listBuku.isNotEmpty()) {
                    val bukuId = listBuku.last().id
                    repositoriBukuPengarang.insertBukuPengarang(
                        BukuPengarang(bukuId, uiStateBuku.detailBuku.pengarangId)
                    )
                }
            }
        }
    }
}

data class UIStateBuku(
    val detailBuku: DetailBuku = DetailBuku(),
    val isEntryValid: Boolean = false
)

data class DetailBuku(
    val id: Int = 0,
    val judul: String = "",
    val tahunTerbit: Int = 0,
    val kategoriId: Int = 0,
    val pengarangId: Int = 0,
    val status: String = "tersedia",
    val isDeleted: Boolean = false
)

fun DetailBuku.toBuku(): Buku = Buku(
    id = id,
    judul = judul,
    tahunTerbit = tahunTerbit,
    kategoriId = kategoriId,
    status = status,
    isDeleted = isDeleted
)

fun Buku.toDetailBuku(pengarangId: Int = 0): DetailBuku = DetailBuku(
    id = id,
    judul = judul,
    tahunTerbit = tahunTerbit,
    kategoriId = kategoriId,
    pengarangId = pengarangId,
    status = status,
    isDeleted = isDeleted
)