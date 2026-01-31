package com.example.myapplication.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repositori.RepositoriBuku
import com.example.myapplication.room.BukuWithDetails
import com.example.myapplication.view.route.DestinasiDetailBuku
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class BukuDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoriBuku: RepositoriBuku
) : ViewModel() {

    private val bukuId: Int = checkNotNull(savedStateHandle[DestinasiDetailBuku.itemIdArg])

    val uiDetailState: StateFlow<DetailBukuUiState> =
        repositoriBuku.getBukuWithDetailsStream(bukuId)
            .filterNotNull()
            .map {
                DetailBukuUiState(bukuWithDetails = it)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = DetailBukuUiState()
            )

    suspend fun softDeleteBuku() {
        repositoriBuku.softDeleteBuku(bukuId)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class DetailBukuUiState(
    val bukuWithDetails: BukuWithDetails? = null
)