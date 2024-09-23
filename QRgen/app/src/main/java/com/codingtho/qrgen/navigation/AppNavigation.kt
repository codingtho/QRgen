package com.codingtho.qrgen.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.codingtho.qrgen.ui.screen.Screen
import com.codingtho.qrgen.ui.screen.view.GenerateScreen
import com.codingtho.qrgen.ui.screen.view.SavedScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.GenerateScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.GenerateScreen.route) {
                GenerateScreen()
            }

            composable(Screen.SavedScreen.route) {
                SavedScreen()
            }
        }
    }
}

@Composable
private fun TopBar(navController: NavController) {
    Column {
        TitleBar()
        TabBar(navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TitleBar() {
    TopAppBar(
        title = {
            Text(
                text = "QRgen",
                fontSize = MaterialTheme.typography.displaySmall.fontSize,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TabBar(navController: NavController) {
    val actualScreen = navController.currentBackStackEntryAsState().value?.destination?.route
    val tabs = listOf(
        Screen.GenerateScreen,
        Screen.SavedScreen
    )

    PrimaryTabRow(
        selectedTabIndex = if (actualScreen == null) 0
        else tabs.indexOfFirst { it.route == actualScreen }
    ) {
        tabs.forEach {
            TabItem(navController, actualScreen, it)
        }
    }
}

@Composable
private fun TabItem(navController: NavController, actualScreen: String?, it: Screen) {
    Tab(
        selected = actualScreen == it.route,
        onClick = {
            navController.popBackStack()
            navController.navigate(it.route)
        },
        modifier = Modifier.height(48.dp)
    ) {
        Text(
            text = it.title,
            fontSize = MaterialTheme.typography.labelLarge.fontSize,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}
