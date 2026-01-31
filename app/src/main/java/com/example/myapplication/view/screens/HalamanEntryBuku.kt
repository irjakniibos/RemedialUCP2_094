package com.example.myapplication.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.view.route.DestinasiEntryBuku
import com.example.myapplication.viewmodel.BukuEntryViewModel
import com.example.myapplication.viewmodel.DetailBuku
import com.example.myapplication.viewmodel.UIStateBuku
import com.example.myapplication.viewmodel.provider.PenyediaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryBukuScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BukuEntryViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val listKategori by viewModel.listKategori.collectAsState()
    val listPengarang by viewModel.listPengarang.collectAsState()

    Scaffold(
        topBar = {
            BukuTopAppBar(
                title = stringResource(DestinasiEntryBuku.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        EntryBukuBody(
            uiStateBuku = viewModel.uiStateBuku,
            listKategori = listKategori.map { it.id to it.namaKategori },
            listPengarang = listPengarang.map { it.id to it.namaPengarang },
            onBukuValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveBuku()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryBukuBody(
    uiStateBuku: UIStateBuku,
    listKategori: List<Pair<Int, String>>,
    listPengarang: List<Pair<Int, String>>,
    onBukuValueChange: (DetailBuku) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        FormInputBuku(
            detailBuku = uiStateBuku.detailBuku,
            listKategori = listKategori,
            listPengarang = listPengarang,
            onValueChange = onBukuValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled = uiStateBuku.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.btn_submit))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInputBuku(
    detailBuku: DetailBuku,
    listKategori: List<Pair<Int, String>>,
    listPengarang: List<Pair<Int, String>>,
    modifier: Modifier = Modifier,
    onValueChange: (DetailBuku) -> Unit = {},
    enabled: Boolean = true
) {
    var expandedKategori by remember { mutableStateOf(false) }
    var expandedPengarang by remember { mutableStateOf(false) }
    var expandedStatus by remember { mutableStateOf(false) }

    val statusOptions = listOf("tersedia", "dipinjam")

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        OutlinedTextField(
            value = detailBuku.judul,
            onValueChange = { onValueChange(detailBuku.copy(judul = it)) },
            label = { Text(stringResource(R.string.judul_buku)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = if (detailBuku.tahunTerbit == 0) "" else detailBuku.tahunTerbit.toString(),
            onValueChange = { onValueChange(detailBuku.copy(tahunTerbit = it.toIntOrNull() ?: 0)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(text = stringResource(R.string.tahun_terbit)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        ExposedDropdownMenuBox(
            expanded = expandedKategori,
            onExpandedChange = { expandedKategori = !expandedKategori }
        ) {
            OutlinedTextField(
                value = listKategori.find { it.first == detailBuku.kategoriId }?.second ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.kategori)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedKategori) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedKategori,
                onDismissRequest = { expandedKategori = false }
            ) {
                listKategori.forEach { (id, nama) ->
                    DropdownMenuItem(
                        text = { Text(nama) },
                        onClick = {
                            onValueChange(detailBuku.copy(kategoriId = id))
                            expandedKategori = false
                        }
                    )
                }
            }
        }

        ExposedDropdownMenuBox(
            expanded = expandedPengarang,
            onExpandedChange = { expandedPengarang = !expandedPengarang }
        ) {
            OutlinedTextField(
                value = listPengarang.find { it.first == detailBuku.pengarangId }?.second ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.pengarang)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPengarang) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedPengarang,
                onDismissRequest = { expandedPengarang = false }
            ) {
                listPengarang.forEach { (id, nama) ->
                    DropdownMenuItem(
                        text = { Text(nama) },
                        onClick = {
                            onValueChange(detailBuku.copy(pengarangId = id))
                            expandedPengarang = false
                        }
                    )
                }
            }
        }

        ExposedDropdownMenuBox(
            expanded = expandedStatus,
            onExpandedChange = { expandedStatus = !expandedStatus }
        ) {
            OutlinedTextField(
                value = detailBuku.status,
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.status)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStatus) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedStatus,
                onDismissRequest = { expandedStatus = false }
            ) {
                statusOptions.forEach { status ->
                    DropdownMenuItem(
                        text = { Text(status) },
                        onClick = {
                            onValueChange(detailBuku.copy(status = status))
                            expandedStatus = false
                        }
                    )
                }
            }
        }

        if (enabled) {
            Text(
                text = stringResource(R.string.required_field),
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}