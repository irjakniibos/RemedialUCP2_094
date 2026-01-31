package com.example.myapplication.view.screens

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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.myapplication.room.Pengarang
import com.example.myapplication.viewmodel.DetailPengarang
import com.example.myapplication.viewmodel.PengarangViewModel
import com.example.myapplication.viewmodel.provider.PenyediaViewModel
import com.example.myapplication.viewmodel.toDetailPengarang
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanPengarang(
    modifier: Modifier = Modifier,
    viewModel: PengarangViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val uiState by viewModel.pengarangUiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var isEdit by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            BukuTopAppBar(
                title = stringResource(R.string.pengarang),
                canNavigateBack = false
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isEdit = false
                    viewModel.updateDialogState(DetailPengarang())
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
                    contentDescription = stringResource(R.string.entry_pengarang)
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (uiState.listPengarang.isEmpty()) {
                Text(
                    text = stringResource(R.string.deskripsi_no_pengarang),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(modifier = Modifier) {
                    items(items = uiState.listPengarang, key = { it.id }) { pengarang ->
                        DataPengarang(
                            pengarang = pengarang,
                            onEditClick = {
                                isEdit = true
                                viewModel.updateDialogState(it.toDetailPengarang())
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
            DialogPengarang(
                detailPengarang = viewModel.dialogPengarangState.detailPengarang,
                isEdit = isEdit,
                onValueChange = viewModel::updateDialogState,
                onDismiss = { showDialog = false },
                onConfirm = {
                    coroutineScope.launch {
                        if (isEdit) {
                            viewModel.updatePengarang()
                        } else {
                            viewModel.savePengarang()
                        }
                        showDialog = false
                    }
                },
                isEntryValid = viewModel.dialogPengarangState.isEntryValid
            )
        }
    }
}

@Composable
fun DataPengarang(
    pengarang: Pengarang,
    onEditClick: (Pengarang) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = pengarang.namaPengarang,
                style = MaterialTheme.typography.titleLarge,
            )
            TextButton(onClick = { onEditClick(pengarang) }) {
                Text("Edit")
            }
        }
    }
}

@Composable
fun DialogPengarang(
    detailPengarang: DetailPengarang,
    isEdit: Boolean,
    onValueChange: (DetailPengarang) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    isEntryValid: Boolean
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEdit) "Edit Pengarang" else "Tambah Pengarang") },
        text = {
            OutlinedTextField(
                value = detailPengarang.namaPengarang,
                onValueChange = { onValueChange(detailPengarang.copy(namaPengarang = it)) },
                label = { Text(stringResource(R.string.nama_pengarang)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
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