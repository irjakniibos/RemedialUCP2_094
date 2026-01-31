package com.example.myapplication.view.route

import com.example.myapplication.R

object DestinasiEditBuku : DestinasiNavigasi {
    override val route = "edit_buku"
    override val titleRes = R.string.edit_buku
    const val itemIdArg = "idBuku"
    val routeWithArgs = "$route/{$itemIdArg}"
}