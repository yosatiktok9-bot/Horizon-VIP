package io.horizon.vip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import io.horizon.vip.ui.nav.HorizonNavHost
import io.horizon.vip.ui.theme.HorizonTheme
import io.horizon.vip.ui.theme.HorizonColors

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HorizonTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = HorizonColors.bgVoid
                ) {
                    HorizonNavHost()
                }
            }
        }
    }
}
