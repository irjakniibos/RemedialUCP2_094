package com.example.myapplication.view.route


import com.example.myapplication.R

object DestinasiListBuku : DestinasiNavigasi {
    override val route = "list_buku"
    override val titleRes = R.string.list_buku
    const val kategoriIdArg = "kategoriId"
    val routeWithArgs = "$route/{$kategoriIdArg}"
}