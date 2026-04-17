package com.buildxide.ui.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Home : Screen("home")
    data object Ide : Screen("ide/{projectId}") {
        fun createRoute(projectId: Long) = "ide/$projectId"
    }
    data object Build : Screen("build/{projectId}") {
        fun createRoute(projectId: Long) = "build/$projectId"
    }
    data object Settings : Screen("settings")
}
