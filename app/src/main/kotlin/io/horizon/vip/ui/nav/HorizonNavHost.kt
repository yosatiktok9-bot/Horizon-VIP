package io.horizon.vip.ui.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.horizon.vip.ui.history.HistoryScreen
import io.horizon.vip.ui.home.HomeScreen
import io.horizon.vip.ui.scan.ScanConfigScreen
import io.horizon.vip.ui.settings.SettingsScreen
import io.horizon.vip.ui.theme.HorizonColors

private data class Tab(val route: String, val label: String, val icon: ImageVector)

private val tabs = listOf(
    Tab("home", "HOME", Icons.Filled.Home),
    Tab("scan", "SCAN", Icons.Filled.Search),
    Tab("history", "LOGS", Icons.Filled.History),
    Tab("settings", "CFG", Icons.Filled.Settings)
)

@Composable
fun HorizonNavHost() {
    val nav = rememberNavController()
    val back by nav.currentBackStackEntryAsState()
    val current = back?.destination?.route

    Scaffold(
        containerColor = HorizonColors.bgVoid,
        bottomBar = {
            NavigationBar(containerColor = HorizonColors.bgSurface) {
                tabs.forEach { tab ->
                    val selected = current == tab.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            nav.navigate(tab.route) {
                                popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(tab.icon, contentDescription = tab.label, tint = if (selected) HorizonColors.neonMint else HorizonColors.textMid)
                        },
                        label = {
                            Text(tab.label, fontFamily = FontFamily.Monospace, fontSize = 10.sp,
                                color = if (selected) HorizonColors.neonMint else HorizonColors.textMid)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = HorizonColors.bgElevated
                        )
                    )
                }
            }
        }
    ) { pad ->
        NavHost(
            navController = nav,
            startDestination = "home",
            modifier = Modifier.padding(pad).background(HorizonColors.bgVoid)
        ) {
            composable("home") { HomeScreen(onStartScan = { nav.navigate("scan") }) }
            composable("scan") { ScanConfigScreen() }
            composable("history") { HistoryScreen() }
            composable("settings") { SettingsScreen() }
        }
    }
}
