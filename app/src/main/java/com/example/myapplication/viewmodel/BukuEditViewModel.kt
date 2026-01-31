package com.example.myapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repositori.RepositoriBuku
import com.example.myapplication.repositori.RepositoriBukuPengarang
import com.example.myapplication.repositori.RepositoriKategori
import com.example.myapplication.repositori.RepositoriPengarang
import com.example.myapplication.room.BukuPengarang
import com.example.myapplication.room.Kategori
import com.example.myapplication.room.Pengarang
import com.example.myapplication.view.route.DestinasiEditBuku
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BukuEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoriBuku: RepositoriBuku,
    repositoriKategori: RepositoriKategori,
    repositoriPengarang: RepositoriPengarang,
    private val repositoriBukuPengarang: RepositoriBukuPengarang
) : ViewModel() {

    var uiStateBuku by mutableStateOf(UIStateBuku())
        private set

    private val bukuId: Int = checkNotNull(savedStateHandle[DestinasiEditBuku.itemIdArg])

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

    init {
        viewModelScope.launch {
            val buku = repositoriBuku.getBukuStream(bukuId)
                .filterNotNull()
                .first()

            val pengarangIds = repositoriBukuPengarang.getPengarangByBukuStream(bukuId)
                .filterNotNull()
                .first()

            val pengarangId = if (pengarangIds.isNotEmpty()) pengarangIds.first() else 0

            uiStateBuku = UIStateBuku(
                detailBuku = buku.toDetailBuku(pengarangId),
                isEntryValid = true
            )
        }
    }

    fun updateUiState(detailBuku: DetailBuku) {
        uiStateBuku = UIStateBuku(
            detailBuku = detailBuku,
            isEntryValid = validasiInput(detailBuku)
        )
    }

    private fun validasiInput(uiState: DetailBuku = uiStateBuku.detailBuku): Boolean {
        return with(uiState) {
            judul.isNotBlank() &&
                    tahunTerbit > 0 &&
                    kategoriId > 0 &&
                    pengarangId > 0 &&
                    status.isNotBlank()
        }
    }

    suspend fun updateBuku() {
        if (validasiInput(uiStateBuku.detailBuku)) {
            repositoriBuku.updateBuku(uiStateBuku.detailBuku.toBuku())

            repositoriBukuPengarang.deleteBukuPengarang(bukuId)
            repositoriBukuPengarang.insertBukuPengarang(
                BukuPengarang(bukuId, uiStateBuku.detailBuku.pengarangId)
            )
        }
    }
}