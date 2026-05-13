package com.gokulahealth.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object CattleList : Screen("cattle_list")
    object AddCow : Screen("add_cow")
    object CowDetails : Screen("cow_details/{cowId}") {
        fun createRoute(cowId: Long) = "cow_details/$cowId"
    }
    object MilkDiary : Screen("milk_diary")
    object Vaccination : Screen("vaccination")
    object HeatCycle : Screen("heat_cycle")
    object Analytics : Screen("analytics")
    object Settings : Screen("settings")
}
