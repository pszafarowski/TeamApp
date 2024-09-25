package com.example.TeamApp

import BottomNavBar
import ChatScreen
import MessageScreen
import UserViewModel
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.TeamApp.auth.ForgotPasswordScreen
import com.example.TeamApp.auth.RegisterScreen
import com.example.TeamApp.data.User
import com.example.TeamApp.event.CreateEventScreen
import com.example.TeamApp.event.CreateEventViewModel
import com.example.TeamApp.event.DetailsScreen
//import com.example.TeamApp.event.MapScreen
import com.example.TeamApp.event.ViewModelProvider
import com.example.TeamApp.profile.ProfileScreen
import com.example.TeamApp.searchThrough.SearchScreen
import com.example.TeamApp.searchThrough.FiltersScreen
import com.example.TeamApp.settings.SendMessageScreen
import com.example.TeamApp.settings.SettingsScreen
import com.example.TeamApp.settings.SettingsScreenv2
import com.example.TeamApp.settings.TermsOfUse
import com.example.TeamApp.settings.YourEventsScreen
import com.example.TeamApp.ui.LoadingScreen
import com.example.TeamApp.utils.SystemUiUtils
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

class MainAppActivity : AppCompatActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Log.d("MainAppActivity", "onCreate called")
            SystemUiUtils.configureSystemUi(this)

            val gradientColors = listOf(
                Color(0xFFE8E8E8),
                Color(0xFF007BFF)
            )
            val navController = rememberAnimatedNavController()
            var isBottomBarVisible by remember { mutableStateOf(true) }
            var isLoading by remember { mutableStateOf(true) }
            var showMainContent by remember { mutableStateOf(false) }
            var isRefreshing by remember { mutableStateOf(false) }
            val userViewModel: UserViewModel = viewModel()
            val viewModel: CreateEventViewModel = ViewModelProvider.createEventViewModel
            LaunchedEffect(navController) {
//                navController.addOnDestinationChangedListener { _, destination, _ ->
//                    isBottomBarVisible = when (destination.route) {
//                        "details/{activityId}" -> false
//                        else -> true
//                    }
//                }
                isBottomBarVisible = true
            }
            LaunchedEffect(Unit){
                Log.d("LaunchedEffect", "LaunchedEffect called")
                val firebaseUser = FirebaseAuth.getInstance().currentUser
                firebaseUser?.email?.let { email ->
                    userViewModel.fetchUserFromFirestore(email)
                }
                Log.d("LaunchedEffect", "${userViewModel.user}")
                viewModel.fetchEvents()
                delay(500)
                isLoading = false
                delay(400)
                showMainContent = true

            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.linearGradient(colors = gradientColors))
            ) {
//                AnimatedVisibility(
//                    visible = isLoading
//                ) {
//                    LoadingScreen()
//                }
                AnimatedVisibility(
                    visible = isLoading,
                    enter = fadeIn(animationSpec = tween(400)) + scaleIn(initialScale = 1.0f, animationSpec = tween(400)),
                    exit = fadeOut(animationSpec = tween(400)) + scaleOut(targetScale = 0.5f, animationSpec = tween(400))
                ) {
                    LoadingScreen()
                }

                AnimatedVisibility(
                    visible = showMainContent,
                    enter = fadeIn(animationSpec = tween(900)),
                    exit = fadeOut(animationSpec = tween(900))
                ) {
                    Scaffold(
                        modifier = Modifier.navigationBarsPadding(),
                        bottomBar = {
                            if (isBottomBarVisible) {
                                BottomNavBar(navController)
                            }
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Brush.linearGradient(colors = gradientColors))
                                .padding(innerPadding)
                        ) {
                            AnimatedNavHost(
                                navController = navController,
                                startDestination = "createEvent"
                            ) {
                                composable(
                                    route = "createEvent",
                                    enterTransition = { fadeIn(animationSpec = tween(300)) },
                                    exitTransition = { fadeOut(animationSpec = tween(300)) },
                                    popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                                    popExitTransition = { fadeOut(animationSpec = tween(300)) }
                                ) { CreateEventScreen(navController,userViewModel) }

                                composable(
                                    route = "search",
                                    enterTransition = { fadeIn(animationSpec = tween(300)) },
                                    exitTransition = { fadeOut(animationSpec = tween(300)) },
                                    popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                                    popExitTransition = { fadeOut(animationSpec = tween(300)) }
                                ) { SearchScreen(navController) { isScrollingDown ->
                                    isBottomBarVisible = !isScrollingDown
                                } }

                                composable(
                                    route = "profile",
                                    enterTransition = { fadeIn(animationSpec = tween(300)) },
                                    exitTransition = { fadeOut(animationSpec = tween(300)) },
                                    popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                                    popExitTransition = { fadeOut(animationSpec = tween(300)) }
                                ) { ProfileScreen(navController) }

                                composable(
                                    route = "settings",
                                    enterTransition = { fadeIn(animationSpec = tween(300)) },
                                    exitTransition = { fadeOut(animationSpec = tween(300)) },
                                    popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                                    popExitTransition = { fadeOut(animationSpec = tween(300)) }
                                ) { SettingsScreenv2(navController) }

                                composable(
                                    route = "filterScreen",
                                    enterTransition = { fadeIn(animationSpec = tween(300)) },
                                    exitTransition = { fadeOut(animationSpec = tween(300)) },
                                    popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                                    popExitTransition = { fadeOut(animationSpec = tween(300)) }
                                ) { FiltersScreen(navController) }

                                composable(
                                    route = "details/{activityId}",
                                    arguments = listOf(navArgument("activityId") { type = NavType.StringType }),
                                    enterTransition = {
                                        slideInHorizontally(
                                            initialOffsetX = { fullWidth -> fullWidth },
                                            animationSpec = tween(300)
                                        ) + fadeIn(animationSpec = tween(300))
                                    },
                                    exitTransition = {
                                        slideOutHorizontally(
                                            targetOffsetX = { fullWidth -> -fullWidth },
                                            animationSpec = tween(300)
                                        ) + fadeOut(animationSpec = tween(300))
                                    },
                                    popEnterTransition = {
                                        slideInHorizontally(
                                            initialOffsetX = { fullWidth -> -fullWidth },
                                            animationSpec = tween(300)
                                        ) + fadeIn(animationSpec = tween(300))
                                    },
                                    popExitTransition = {
                                        slideOutHorizontally(
                                            targetOffsetX = { fullWidth -> fullWidth },
                                            animationSpec = tween(300)
                                        ) + fadeOut(animationSpec = tween(300))
                                    }
                                ) { backStackEntry ->
                                    val activityId = backStackEntry.arguments?.getString("activityId") ?: return@composable
                                    DetailsScreen(navController, activityId, userViewModel)
                                }

                                composable(
                                    route = "yourEvents",
                                    enterTransition = { fadeIn(animationSpec = tween(300)) },
                                    exitTransition = { fadeOut(animationSpec = tween(300)) },
                                    popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                                    popExitTransition = { fadeOut(animationSpec = tween(300)) }
                                ) { YourEventsScreen(navController) }

                                composable(
                                    route = "termsOfUse",
                                    enterTransition = { fadeIn(animationSpec = tween(300)) },
                                    exitTransition = { fadeOut(animationSpec = tween(300)) },
                                    popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                                    popExitTransition = { fadeOut(animationSpec = tween(300)) }
                                ) { TermsOfUse() }

                                composable(
                                    route = "sendUsMessage",
                                    enterTransition = { fadeIn(animationSpec = tween(300)) },
                                    exitTransition = { fadeOut(animationSpec = tween(300)) },
                                    popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                                    popExitTransition = { fadeOut(animationSpec = tween(300)) }
                                ) { SendMessageScreen() }

                                composable(
                                    route = "ForgotPassword",
                                    arguments = listOf(navArgument("activityId") { type = NavType.StringType }),
                                    enterTransition = { fadeIn(animationSpec = tween(300)) },
                                    exitTransition = { fadeOut(animationSpec = tween(300)) },
                                    popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                                    popExitTransition = { fadeOut(animationSpec = tween(300)) }
                                ) { ForgotPasswordScreen(navController) }
                                composable(
                                    route = "chat/{activityId}",
                                    enterTransition = { fadeIn(animationSpec = tween(300)) },
                                    exitTransition = { fadeOut(animationSpec = tween(300)) },
                                    popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                                    popExitTransition = { fadeOut(animationSpec = tween(300)) }
                                ) {
                                    backStackEntry ->
                                    val activityId = backStackEntry.arguments?.getString("activityId") ?: return@composable
                                    ChatScreen(navController, activityId, userViewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
