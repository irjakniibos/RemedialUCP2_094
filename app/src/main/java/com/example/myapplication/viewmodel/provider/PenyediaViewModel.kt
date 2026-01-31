package com.example.myapplication.viewmodel.provider

import ListBukuViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.repositori.AplikasiBuku
import com.example.myapplication.viewmodel.BukuDetailViewModel
import com.example.myapplication.viewmodel.BukuEditViewModel
import com.example.myapplication.viewmodel.BukuEntryViewModel
import com.example.myapplication.viewmodel.KategoriViewModel
import com.example.myapplication.viewmodel.PengarangViewModel
import com.example.myapplication.viewmodel.SemuaBukuViewModel

object PenyediaViewModel {
    val Factory = viewModelFactory {
        initializer {
            KategoriViewModel(
                aplikasiBuku().container.repositoriKategori
            )
        }
        initializer {
            ListBukuViewModel(
                this.createSavedStateHandle(),
                aplikasiBuku().container.repositoriBuku
            )
        }
        initializer {
            BukuEntryViewModel(
                this.createSavedStateHandle(),
                aplikasiBuku().container.repositoriBuku,
                aplikasiBuku().container.repositoriKategori,
                aplikasiBuku().container.repositoriPengarang,
                aplikasiBuku().container.repositoriBukuPengarang
            )
        }
        initializer {
            BukuDetailViewModel(
                this.createSavedStateHandle(),
                aplikasiBuku().container.repositoriBuku
            )
        }
        initializer {
            BukuEditViewModel(
                this.createSavedStateHandle(),
                aplikasiBuku().container.repositoriBuku,
                aplikasiBuku().container.repositoriKategori,
                aplikasiBuku().container.repositoriPengarang,
                aplikasiBuku().container.repositoriBukuPengarang
            )
        }
        initializer {
            PengarangViewModel(
                aplikasiBuku().container.repositoriPengarang
            )
        }
        initializer {
            SemuaBukuViewModel(
                aplikasiBuku().container.repositoriBuku
            )
        }
    }
}

fun CreationExtras.aplikasiBuku(): AplikasiBuku =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AplikasiBuku)