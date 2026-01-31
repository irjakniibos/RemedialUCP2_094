package com.example.myapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repositori.RepositoriKategori
import com.example.myapplication.room.Kategori
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class KategoriViewModel(private val repositoriKategori: RepositoriKategori) : ViewModel() {
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val kategoriUiState: StateFlow<KategoriUiState> =
        repositoriKategori.getAllKategoriStream()
            .filterNotNull()
            .map { KategoriUiState(listKategori = it.toList()) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = KategoriUiState()
            )

    var dialogKategoriState by mutableStateOf(DialogKategoriState())

    fun updateDialogState(detailKategori: DetailKategori) {
        dialogKategoriState = DialogKategoriState(
            detailKategori = detailKategori,
            isEntryValid = validasiInput(detailKategori)
        )
    }

    private fun validasiInput(uiState: DetailKategori = dialogKategoriState.detailKategori): Boolean {
        return with(uiState) {
            namaKategori.isNotBlank()
        }
    }

    suspend fun saveKategori() {
        if (validasiInput()) {
            repositoriKategori.insertKategori(dialogKategoriState.detailKategori.toKategori())
        }
    }

    suspend fun updateKategori() {
        if (validasiInput(dialogKategoriState.detailKategori)) {
            repositoriKategori.updateKategori(dialogKategoriState.detailKategori.toKategori())
        }
    }

    suspend fun deleteKategori(kategori: Kategori) {
        repositoriKategori.deleteKategori(kategori)
    }

    data class KategoriUiState(
        val listKategori: List<Kategori> = listOf()
    )
}

data class DialogKategoriState(
    val detailKategori: DetailKategori = DetailKategori(),
    val isEntryValid: Boolean = false
)

data class DetailKategori(
    val id: Int = 0,
    val namaKategori: String = "",
    val parentId: Int? = null
)

fun DetailKategori.toKategori(): Kategori = Kategori(
    id = id,
    namaKategori = namaKategori,
    parentId = parentId
)

fun Kategori.toDetailKategori(): DetailKategori = DetailKategori(
    id = id,
    namaKategori = namaKategori,
    parentId = parentId
)