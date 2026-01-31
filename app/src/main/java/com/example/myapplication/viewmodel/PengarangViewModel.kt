package com.example.myapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repositori.RepositoriPengarang
import com.example.myapplication.room.Pengarang
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PengarangViewModel(private val repositoriPengarang: RepositoriPengarang) : ViewModel() {
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val pengarangUiState: StateFlow<PengarangUiState> =
        repositoriPengarang.getAllPengarangStream()
            .filterNotNull()
            .map { PengarangUiState(listPengarang = it.toList()) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = PengarangUiState()
            )

    var dialogPengarangState by mutableStateOf(DialogPengarangState())

    fun updateDialogState(detailPengarang: DetailPengarang) {
        dialogPengarangState = DialogPengarangState(
            detailPengarang = detailPengarang,
            isEntryValid = validasiInput(detailPengarang)
        )
    }

    private fun validasiInput(uiState: DetailPengarang = dialogPengarangState.detailPengarang): Boolean {
        return with(uiState) {
            namaPengarang.isNotBlank()
        }
    }

    suspend fun savePengarang() {
        if (validasiInput()) {
            repositoriPengarang.insertPengarang(dialogPengarangState.detailPengarang.toPengarang())
        }
    }

    suspend fun updatePengarang() {
        if (validasiInput(dialogPengarangState.detailPengarang)) {
            repositoriPengarang.updatePengarang(dialogPengarangState.detailPengarang.toPengarang())
        }
    }

    suspend fun deletePengarang(pengarang: Pengarang) {
        repositoriPengarang.deletePengarang(pengarang)
    }

    data class PengarangUiState(
        val listPengarang: List<Pengarang> = listOf()
    )
}

data class DialogPengarangState(
    val detailPengarang: DetailPengarang = DetailPengarang(),
    val isEntryValid: Boolean = false
)

data class DetailPengarang(
    val id: Int = 0,
    val namaPengarang: String = ""
)

fun DetailPengarang.toPengarang(): Pengarang = Pengarang(
    id = id,
    namaPengarang = namaPengarang
)

fun Pengarang.toDetailPengarang(): DetailPengarang = DetailPengarang(
    id = id,
    namaPengarang = namaPengarang
)