package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repositori.RepositoriBuku
import com.example.myapplication.room.BukuWithDetails
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SemuaBukuViewModel(private val repositoriBuku: RepositoriBuku) : ViewModel() {
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val semuaBukuUiState: StateFlow<SemuaBukuUiState> =
        repositoriBuku.getAllBukuWithDetailsStream()
            .filterNotNull()
            .map { SemuaBukuUiState(listBuku = it.toList()) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = SemuaBukuUiState()
            )

    data class SemuaBukuUiState(
        val listBuku: List<BukuWithDetails> = listOf()
    )
}