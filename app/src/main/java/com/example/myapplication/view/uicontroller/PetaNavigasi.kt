package com.example.myapplication.view.uicontroller

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.view.route.DestinasiDetailBuku
import com.example.myapplication.view.route.DestinasiEditBuku
import com.example.myapplication.view.route.DestinasiEntryBuku
import com.example.myapplication.view.route.DestinasiKategori
import com.example.myapplication.view.route.DestinasiListBuku
import com.example.myapplication.view.screens.DetailBukuScreen
import com.example.myapplication.view.screens.EditBukuScreen
import com.example.myapplication.view.screens.EntryBukuScreen
import com.example.myapplication.view.screens.HomeScreenWithBottomNav
import com.example.myapplication.view.screens.ListBukuScreen

@Composable
fun BukuApp(navController: NavHostController = rememberNavController(), modifier: Modifier) {
    HostNavigasi(navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HostNavigasi(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = DestinasiKategori.route,
        modifier = modifier
    ) {
        composable(DestinasiKategori.route) {
            HomeScreenWithBottomNav(navController = navController)
        }
        composable(
            route = DestinasiListBuku.routeWithArgs,
            arguments = listOf(navArgument(DestinasiListBuku.kategoriIdArg) {
                type = NavType.IntType
            })
        ) {
            ListBukuScreen(
                navigateBack = { navController.popBackStack() },
                navigateToEntryBuku = { kategoriId ->
                    navController.navigate("${DestinasiEntryBuku.route}/$kategoriId")
                },
                navigateToDetailBuku = { bukuId ->
                    navController.navigate("${DestinasiDetailBuku.route}/$bukuId")
                }
            )
        }
        composable(
            route = DestinasiEntryBuku.routeWithArgs,
            arguments = listOf(navArgument(DestinasiEntryBuku.kategoriIdArg) {
                type = NavType.IntType
            })
        ) {
            EntryBukuScreen(navigateBack = { navController.popBackStack() })
        }
        composable(
            route = DestinasiDetailBuku.routeWithArgs,
            arguments = listOf(navArgument(DestinasiDetailBuku.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            DetailBukuScreen(
                navigateToEditItem = { bukuId ->
                    navController.navigate("${DestinasiEditBuku.route}/$bukuId")
                },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = DestinasiEditBuku.routeWithArgs,
            arguments = listOf(navArgument(DestinasiEditBuku.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            EditBukuScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}