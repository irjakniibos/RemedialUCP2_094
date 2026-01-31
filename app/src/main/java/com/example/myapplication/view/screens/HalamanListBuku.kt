package com.example.myapplication.view.screens

import ListBukuViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.room.Buku
import com.example.myapplication.view.route.DestinasiListBuku
import com.example.myapplication.viewmodel.provider.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListBukuScreen(
    navigateBack: () -> Unit,
    navigateToEntryBuku: (Int) -> Unit,
    navigateToDetailBuku: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListBukuViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val uiState by viewModel.listBukuUiState.collectAsState()

    Scaffold(
        topBar = {
            BukuTopAppBar(
                title = stringResource(DestinasiListBuku.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.kategoriId?.let { kategoriId ->
                        navigateToEntryBuku(kategoriId)
                    }
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.entry_buku)
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (uiState.listBuku.isEmpty()) {
                Text(
                    text = stringResource(R.string.deskripsi_no_buku),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(modifier = Modifier) {
                    items(items = uiState.listBuku, key = { it.id }) { buku ->
                        DataBuku(
                            buku = buku,
                            onBukuClick = { navigateToDetailBuku(it.id) },
                            modifier = Modifier
                                .padding(dimensionResource(id = R.dimen.padding_small))
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DataBuku(
    buku: Buku,
    onBukuClick: (Buku) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onBukuClick(buku) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = buku.judul,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = buku.tahunTerbit.toString(),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Text(
                text = "Status: ${buku.status}",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}