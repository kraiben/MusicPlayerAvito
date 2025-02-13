package com.gab.musicplayeravito.ui.screens.general

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.gab.musicplayeravito.ui.navigation.NavigationItem
import com.gab.musicplayeravito.ui.navigation.NavigationState
import com.gab.musicplayeravito.utils.topBorder

@Composable
fun NavScaffold(
    navigationState: NavigationState,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.topBorder(MaterialTheme.colorScheme.onTertiary, height = 1F),
                containerColor = MaterialTheme.colorScheme.background
            ) {
                val navBackStackEntry by navigationState
                    .navHostController.currentBackStackEntryAsState()
                val items = listOf(
                    NavigationItem.Search,
                    NavigationItem.Downloaded
                )
                items.forEach { item ->
                    val selected = navBackStackEntry?.destination?.hierarchy?.any {
                        it.route == item.screen.route
                    } ?: false
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (!selected) {
                                navigationState.navigateTo(item.screen.route)
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.background,
                            unselectedIconColor = MaterialTheme.colorScheme.secondary,
                            selectedIconColor = MaterialTheme.colorScheme.primary
                        ),
                        icon = {
                            Icon(
                                ImageVector.vectorResource(item.iconResId),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                            )
                        },
                        label = {
                            Text(
                                color = MaterialTheme.colorScheme.primary,
                                text = stringResource(id = item.titleResId)
                            )
                        }

                    )
                }
            }
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}