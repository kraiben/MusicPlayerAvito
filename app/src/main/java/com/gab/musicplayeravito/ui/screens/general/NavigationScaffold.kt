package com.gab.musicplayeravito.ui.screens.general

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.gab.musicplayeravito.R
import com.gab.musicplayeravito.ui.navigation.NavigationItem
import com.gab.musicplayeravito.ui.navigation.NavigationState
import com.gab.musicplayeravito.utils.topBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavScaffold(
    navigationState: NavigationState,
    content: @Composable (Modifier) -> Unit,
    onSearchClickListener: (String) -> Unit = {},
    currentTrackState: CurrentTrackState,
    onNextClickListener: () -> Unit = {},
    onPreviousClickListener: () -> Unit = {},
    onStopClickListener: () -> Unit = {},
    onStartClickListener: () -> Unit = {},
) {
    Scaffold(

        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        var textField: String by remember { mutableStateOf("") }
                        TextField(
                            value = textField,
                            maxLines = 1,
                            textStyle = TextStyle(fontSize = 16.sp),
                            modifier = Modifier.weight(1f),
                            onValueChange = { textField = it },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedTextColor = Color.Black,
                                disabledTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            )
                        )
                        Icon(
                            ImageVector.vectorResource(R.drawable.magnifying_glass),
                            contentDescription = null,
                            modifier = Modifier
                                .size(28.dp)
                                .clickable {
                                    onSearchClickListener(textField)
                                }
                        )
                    }
                }
            )
        },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            content(Modifier.weight(1f))
            MusicPlayerMini(
                currentTrackState,
                onClick = {
                    navigationState.navigateToPlayer()
                },
                onNextClickListener = onNextClickListener,
                onPreviousClickListener = onPreviousClickListener,
                onStopClickListener = onStopClickListener,
                onStartClickListener = onStartClickListener,
                )
        }
    }
}