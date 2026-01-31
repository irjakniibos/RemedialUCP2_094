package com.example.myapplication.view.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val screen: @Composable () -> Unit
)

@Composable
fun HomeScreenWithBottomNav(navController: NavHostController) {
    var selectedTab by rememberSaveable { mutableStateOf(0) }

    val bottomNavItems = listOf(
        BottomNavItem(
            title = "Kategori",
            icon = Icons.Default.Category,
            screen = { HalamanKategori(navigateToListBuku = { kategoriId ->
                navController.navigate("list_buku/$kategoriId")
            }) }
        ),
        BottomNavItem(
            title = "Semua Buku",
            icon = Icons.Default.Book,
            screen = { HalamanSemuaBuku(navigateToDetailBuku = { bukuId ->
                navController.navigate("detail_buku/$bukuId")
            }) }
        ),
        BottomNavItem(
            title = "Pengarang",
            icon = Icons.Default.Person,
            screen = { HalamanPengarang() }
        )
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        bottomNavItems[selectedTab].screen()
    }
}