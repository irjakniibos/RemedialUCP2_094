package com.example.myapplication.view.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.room.Kategori
import com.example.myapplication.viewmodel.DetailKategori
import com.example.myapplication.viewmodel.KategoriViewModel
import com.example.myapplication.viewmodel.provider.PenyediaViewModel
import com.example.myapplication.viewmodel.toDetailKategori
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanKategori(
    navigateToListBuku: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: KategoriViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val uiState by viewModel.kategoriUiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var isEdit by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            BukuTopAppBar(
                title = stringResource(R.string.kategori),
                canNavigateBack = false
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isEdit = false
                    viewModel.updateDialogState(DetailKategori())
                    showDialog = true
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(
                    start = dimensionResource(id = R.dimen.padding_large),
                    end = dimensionResource(id = R.dimen.padding_large),
                    bottom = 80.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.entry_kategori)
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (uiState.listKategori.isEmpty()) {
                Text(
                    text = stringResource(R.string.deskripsi_no_kategori),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(modifier = Modifier) {
                    items(items = uiState.listKategori, key = { it.id }) { kategori ->
                        DataKategori(
                            kategori = kategori,
                            onKategoriClick = { navigateToListBuku(it.id) },
                            onEditClick = {
                                isEdit = true
                                viewModel.updateDialogState(it.toDetailKategori())
                                showDialog = true
                            },
                            modifier = Modifier
                                .padding(dimensionResource(id = R.dimen.padding_small))
                        )
                    }
                }
            }
        }

        if (showDialog) {
            DialogKategori(
                detailKategori = viewModel.dialogKategoriState.detailKategori,
                listKategori = uiState.listKategori,
                isEdit = isEdit,
                onValueChange = viewModel::updateDialogState,
                onDismiss = { showDialog = false },
                onConfirm = {
                    coroutineScope.launch {
                        if (isEdit) {
                            viewModel.updateKategori()
                        } else {
                            viewModel.saveKategori()
                        }
                        showDialog = false
                    }
                },
                isEntryValid = viewModel.dialogKategoriState.isEntryValid
            )
        }
    }
}

@Composable
fun DataKategori(
    kategori: Kategori,
    onKategoriClick: (Kategori) -> Unit,
    onEditClick: (Kategori) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onKategoriClick(kategori) }
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = kategori.namaKategori,
                style = MaterialTheme.typography.titleLarge,
            )
            TextButton(onClick = { onEditClick(kategori) }) {
                Text("Edit")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogKategori(
    detailKategori: DetailKategori,
    listKategori: List<Kategori>,
    isEdit: Boolean,
    onValueChange: (DetailKategori) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    isEntryValid: Boolean
) {
    var expandedParent by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEdit) "Edit Kategori" else "Tambah Kategori") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = detailKategori.namaKategori,
                    onValueChange = { onValueChange(detailKategori.copy(namaKategori = it)) },
                    label = { Text(stringResource(R.string.nama_kategori)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                ExposedDropdownMenuBox(
                    expanded = expandedParent,
                    onExpandedChange = { expandedParent = !expandedParent }
                ) {
                    OutlinedTextField(
                        value = listKategori.find { it.id == detailKategori.parentId }?.namaKategori ?: "Tanpa Parent",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Parent Kategori") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedParent) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedParent,
                        onDismissRequest = { expandedParent = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Tanpa Parent") },
                            onClick = {
                                onValueChange(detailKategori.copy(parentId = null))
                                expandedParent = false
                            }
                        )
                        listKategori.filter { it.id != detailKategori.id }.forEach { kategori ->
                            DropdownMenuItem(
                                text = { Text(kategori.namaKategori) },
                                onClick = {
                                    onValueChange(detailKategori.copy(parentId = kategori.id))
                                    expandedParent = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = isEntryValid
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}