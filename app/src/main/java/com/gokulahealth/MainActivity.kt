package com.gokulahealth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gokulahealth.ui.navigation.Screen
import com.gokulahealth.ui.screens.analytics.AnalyticsScreen
import com.gokulahealth.ui.screens.cattle.AddCowScreen
import com.gokulahealth.ui.screens.cattle.CattleListScreen
import com.gokulahealth.ui.screens.cattle.CowDetailsScreen
import com.gokulahealth.ui.screens.heatcycle.HeatCycleScreen
import com.gokulahealth.ui.screens.home.HomeScreen
import com.gokulahealth.ui.screens.milk.MilkDiaryScreen
import com.gokulahealth.ui.screens.settings.SettingsScreen
import com.gokulahealth.ui.screens.splash.SplashScreen
import com.gokulahealth.ui.screens.vaccination.VaccinationScreen
import com.gokulahealth.ui.theme.GokulaHealthTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GokulaHealthTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onNavigateToHome = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToAddCow = { navController.navigate(Screen.AddCow.route) },
                onNavigateToMilkDiary = { navController.navigate(Screen.MilkDiary.route) },
                onNavigateToVaccination = { navController.navigate(Screen.Vaccination.route) },
                onNavigateToHeatCycle = { navController.navigate(Screen.HeatCycle.route) },
                onNavigateToAnalytics = { navController.navigate(Screen.Analytics.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onViewCattle = { navController.navigate(Screen.CattleList.route) }
            )
        }
        
        composable(Screen.AddCow.route) {
            AddCowScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.CattleList.route) {
            CattleListScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAddCow = { navController.navigate(Screen.AddCow.route) },
                onNavigateToDetails = { cowId -> 
                    navController.navigate(Screen.CowDetails.createRoute(cowId))
                }
            )
        }

        composable(
            route = Screen.CowDetails.route,
            arguments = listOf(navArgument("cowId") { type = NavType.LongType })
        ) {
            CowDetailsScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.MilkDiary.route) {
            MilkDiaryScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.Vaccination.route) {
            VaccinationScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.HeatCycle.route) {
            HeatCycleScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.Analytics.route) {
            AnalyticsScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.Settings.route) {
            SettingsScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
