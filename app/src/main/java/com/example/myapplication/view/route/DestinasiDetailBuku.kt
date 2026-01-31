package com.example.myapplication.view.route

import com.example.myapplication.R

object DestinasiDetailBuku : DestinasiNavigasi {
    override val route = "detail_buku"
    override val titleRes = R.string.detail_buku
    const val itemIdArg = "idBuku"
    val routeWithArgs = "$route/{$itemIdArg}"
}