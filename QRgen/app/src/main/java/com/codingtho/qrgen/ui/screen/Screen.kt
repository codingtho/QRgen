package com.codingtho.qrgen.ui.screen

sealed class Screen(
    val route: String,
    val title: String
) {
    data object GenerateScreen : Screen("generate", "Generate")
    data object SavedScreen : Screen("saved", "Saved")
}
