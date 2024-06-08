package com.mertg.shoppingapp.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mertg.shoppingapp.ui.theme.Orange

@Composable
fun BottomNavigation(
    items: List<BottomNavigationItem>,
    selected: Int,
    onItemClick: (Int) -> Unit,
    onFabClick: () -> Unit
) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Orange,
        tonalElevation = 10.dp,
    ) {
        val halfIndex = items.size / 2
        items.forEachIndexed { index, item ->
            if (index == halfIndex) {
                Spacer(modifier = Modifier.width(8.dp))
                FloatingActionButton(
                    onClick = onFabClick,
                    containerColor = Color.White,
                    modifier = Modifier
                        .size(56.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Item")
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
            NavigationBarItem(
                selected = index == selected,
                onClick = { onItemClick(index) },
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = if (index == selected)  LocalContentColor.current else Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.text,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (index == selected)  LocalContentColor.current else Color.White
                        )
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = LocalContentColor.current,
                    selectedTextColor = LocalContentColor.current,
                    unselectedIconColor = Color.White,
                    unselectedTextColor = Color.White,
                    indicatorColor = Orange
                ),
                modifier = Modifier.weight(1f)
            )
        }
    }
}


