package com.dating.home.presentation.home.bottom_navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import aura.feature.home.presentation.generated.resources.Res
import aura.feature.home.presentation.generated.resources.bottom_nav_dates
import aura.feature.home.presentation.generated.resources.bottom_nav_radar
import aura.feature.home.presentation.generated.resources.bottom_nav_feed
import aura.feature.home.presentation.generated.resources.bottom_nav_matches
import aura.feature.home.presentation.generated.resources.bottom_nav_messages
import aura.feature.home.presentation.generated.resources.bottom_nav_profile
import org.jetbrains.compose.resources.stringResource

@Composable
fun BottomNavigationBar(
    selectedSection: BottomNavSection,
    onSectionSelected: (BottomNavSection) -> Unit,
    sections: List<BottomNavSection> = BottomNavSection.entries.toList(),
    badges: Map<BottomNavSection, Int> = emptyMap(),
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            shadowElevation = 12.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                sections.forEach { section ->
                    val selected = selectedSection == section
                    val badgeCount = badges[section] ?: 0
                    val contentColor = if (selected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(22.dp))
                            .clickable { onSectionSelected(section) }
                            .background(
                                if (selected) MaterialTheme.colorScheme.primaryContainer
                                else Color.Transparent
                            )
                            .padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        BadgedBox(
                            badge = {
                                if (badgeCount > 0) {
                                    Badge(containerColor = MaterialTheme.colorScheme.error) {
                                        Text(
                                            text = if (badgeCount > 99) "99+" else "$badgeCount",
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp)
                                        )
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (selected) section.selectedIcon else section.unselectedIcon,
                                contentDescription = bottomNavLabelText(section),
                                tint = contentColor,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Text(
                            text = bottomNavLabelText(section),
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, lineHeight = 12.sp),
                            color = contentColor,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun bottomNavLabelText(section: BottomNavSection): String {
    return when (section.labelKey) {
        BottomNavLabel.FEED -> stringResource(Res.string.bottom_nav_feed)
        BottomNavLabel.MATCHES -> stringResource(Res.string.bottom_nav_matches)
        BottomNavLabel.MESSAGES -> stringResource(Res.string.bottom_nav_messages)
        BottomNavLabel.DATES -> stringResource(Res.string.bottom_nav_dates)
        BottomNavLabel.PROFILE -> stringResource(Res.string.bottom_nav_profile)
        BottomNavLabel.RADAR -> stringResource(Res.string.bottom_nav_radar)
    }
}
