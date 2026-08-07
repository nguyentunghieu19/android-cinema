package com.cinema.android.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cinema.android.ui.screen.home.HomeScreen
import com.cinema.android.ui.screen.profile.ProfileScreen

@Composable
fun MainScreen(
    onMovieClick: (Int) -> Unit,
    onLoggedOut: () -> Unit,
    onViewBookingHistory: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .padding(12.dp)
                    .clip(RoundedCornerShape(24.dp)),
                containerColor = Color(0xFF1E1E1E), // Dark background for the bar
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 0) Icons.Default.Home else Icons.Outlined.Home,
                            contentDescription = "Trang chủ"
                        )
                    },
                    label = {
                        Text(
                            "Trang chủ",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 10.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFFFFC107), // Gold for selected
                        selectedTextColor = Color(0xFFFFC107),
                        indicatorColor = Color(0xFF333333),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 1) Icons.Default.Person else Icons.Outlined.Person,
                            contentDescription = "Tài khoản"
                        )
                    },
                    label = {
                        Text(
                            "Tài khoản",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 10.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF03DAC5), // Teal for selected
                        selectedTextColor = Color(0xFF03DAC5),
                        indicatorColor = Color(0xFF333333),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
            }
        },
        containerColor = Color(0xFF0F0F0F) // Overall dark background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> HomeScreen(onMovieClick = onMovieClick)
                1 -> ProfileScreen(
                    onLoggedOut = onLoggedOut,
                    onViewBookingHistory = onViewBookingHistory
                )
            }
        }
    }
}
