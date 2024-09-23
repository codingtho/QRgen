package com.codingtho.qrgen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.codingtho.qrgen.navigation.AppNavigation
import com.codingtho.qrgen.ui.theme.QRgenTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QRgenTheme {
                AppNavigation()
            }
        }
    }
}
