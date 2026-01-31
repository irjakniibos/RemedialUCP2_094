package com.example.myapplication.view.route

import com.example.myapplication.R

object DestinasiEntryBuku : DestinasiNavigasi {
    override val route = "entry_buku"
    override val titleRes = R.string.entry_buku
    const val kategoriIdArg = "kategoriId"
    val routeWithArgs = "$route/{$kategoriIdArg}"
}