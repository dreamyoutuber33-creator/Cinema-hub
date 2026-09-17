package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.QualityPreference
import com.example.ui.theme.AmoledBlack
import com.example.ui.theme.CinemaCrimson
import com.example.ui.theme.CinemaEmerald
import com.example.ui.theme.CinemaGold
import com.example.ui.theme.CinemaIndigo
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.settings.collectAsState()
    val cacheSize by viewModel.cacheSizeMb.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var apiKeyInput by remember(settings.tmdbApiKey) { mutableStateOf(settings.tmdbApiKey) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AmoledBlack)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("settings_scroll"),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Screen Header
            item {
                Text(
                    text = "Settings & Preferences",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.5).sp
                )
            }

            // User Profile / Guest Badge
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceCard)
                        .border(1.dp, SurfaceBorder.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(CinemaGold.copy(alpha = 0.2f))
                            .border(1.dp, CinemaGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = CinemaGold,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "CinemaHub Patron",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Standard Guest Access • All Free Content Unlocked",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // Section: Video Playback
            item {
                SettingsSection(
                    title = "Playback Preferences",
                    icon = Icons.Default.PlayCircle
                ) {
                    SettingsSwitchRow(
                        title = "Auto-play on Launch",
                        subtitle = "Immediately begin streaming when opening a title",
                        checked = settings.autoPlay,
                        onCheckedChange = { chk -> viewModel.updateSettings { it.copy(autoPlay = chk) } }
                    )

                    SettingsSwitchRow(
                        title = "Resume Playback",
                        subtitle = "Remember last watched position across sessions",
                        checked = settings.resumePlayback,
                        onCheckedChange = { chk -> viewModel.updateSettings { it.copy(resumePlayback = chk) } }
                    )

                    SettingsSwitchRow(
                        title = "Keep Screen Awake",
                        subtitle = "Prevent device display from sleeping during playback",
                        checked = settings.keepScreenAwake,
                        onCheckedChange = { chk -> viewModel.updateSettings { it.copy(keepScreenAwake = chk) } }
                    )

                    SettingsSwitchRow(
                        title = "Picture-in-Picture (PiP)",
                        subtitle = "Enable floating mini-player when leaving app",
                        checked = settings.pipEnabled,
                        onCheckedChange = { chk -> viewModel.updateSettings { it.copy(pipEnabled = chk) } }
                    )

                    // Default Quality Picker
                    Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)) {
                        Text(
                            text = "Default Quality (Wi-Fi): ${settings.wifiQuality.label}",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf(QualityPreference.AUTO, QualityPreference.HIGHEST, QualityPreference.HD).forEach { q ->
                                val isSel = settings.wifiQuality == q
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isSel) CinemaGold else SurfaceDark)
                                        .clickable { viewModel.updateSettings { it.copy(wifiQuality = q) } }
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = q.name,
                                        color = if (isSel) AmoledBlack else TextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Section: Subtitles & Audio
            item {
                SettingsSection(
                    title = "Subtitles & Audio",
                    icon = Icons.Default.Subtitles
                ) {
                    SettingsSwitchRow(
                        title = "Subtitles Enabled",
                        subtitle = "Display closed captions by default",
                        checked = settings.subtitlesEnabled,
                        onCheckedChange = { chk -> viewModel.updateSettings { it.copy(subtitlesEnabled = chk) } }
                    )

                    SettingsSwitchRow(
                        title = "Audio Normalization",
                        subtitle = "Balance explosive scenes and quiet dialogues automatically",
                        checked = settings.audioNormalization,
                        onCheckedChange = { chk -> viewModel.updateSettings { it.copy(audioNormalization = chk) } }
                    )

                    // Subtitle Size
                    Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)) {
                        Text(
                            text = "Subtitle Text Size: ${settings.subtitleTextSize}",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Small", "Medium", "Large").forEach { size ->
                                val isSel = settings.subtitleTextSize == size
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isSel) CinemaGold else SurfaceDark)
                                        .clickable { viewModel.updateSettings { it.copy(subtitleTextSize = size) } }
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = size,
                                        color = if (isSel) AmoledBlack else TextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Section: Gesture Controls
            item {
                SettingsSection(
                    title = "Player Gestures",
                    icon = Icons.Default.TouchApp
                ) {
                    SettingsSwitchRow(
                        title = "Swipe & Tap Gestures",
                        subtitle = "Left swipe brightness, right swipe volume, double-tap seek",
                        checked = settings.gesturesEnabled,
                        onCheckedChange = { chk -> viewModel.updateSettings { it.copy(gesturesEnabled = chk) } }
                    )

                    // Seek Duration
                    Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)) {
                        Text(
                            text = "Double Tap Seek Duration: ${settings.doubleTapSeekSeconds} seconds",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf(5, 10, 15, 30).forEach { sec ->
                                val isSel = settings.doubleTapSeekSeconds == sec
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isSel) CinemaGold else SurfaceDark)
                                        .clickable { viewModel.updateSettings { it.copy(doubleTapSeekSeconds = sec) } }
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "${sec}s",
                                        color = if (isSel) AmoledBlack else TextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Section: Appearance & Accent Color
            item {
                SettingsSection(
                    title = "Appearance & Theme",
                    icon = Icons.Default.ColorLens
                ) {
                    SettingsSwitchRow(
                        title = "Pure AMOLED Black",
                        subtitle = "True pitch black background for OLED screens & battery saving",
                        checked = settings.isAmoledDark,
                        onCheckedChange = { chk -> viewModel.updateSettings { it.copy(isAmoledDark = chk) } }
                    )

                    // Accent Color Row
                    Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)) {
                        Text(
                            text = "Accent Color: ${settings.accentColor}",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            val accents = listOf(
                                "Gold" to CinemaGold,
                                "Crimson" to CinemaCrimson,
                                "Indigo" to CinemaIndigo,
                                "Emerald" to CinemaEmerald
                            )
                            accents.forEach { (name, color) ->
                                val isSel = settings.accentColor == name
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .border(
                                            width = if (isSel) 3.dp else 1.dp,
                                            color = if (isSel) TextPrimary else SurfaceBorder,
                                            shape = CircleShape
                                        )
                                        .clickable { viewModel.updateSettings { it.copy(accentColor = name) } }
                                )
                            }
                        }
                    }
                }
            }

            // Section: Data, Network & Cache
            item {
                SettingsSection(
                    title = "Data & Storage Management",
                    icon = Icons.Default.CleaningServices
                ) {
                    SettingsSwitchRow(
                        title = "Data Saver Mode",
                        subtitle = "Stream in 480p/360p to minimize mobile data usage",
                        checked = settings.dataSaverMode,
                        onCheckedChange = { chk -> viewModel.updateSettings { it.copy(dataSaverMode = chk) } }
                    )

                    SettingsSwitchRow(
                        title = "Stream Over Wi-Fi Only",
                        subtitle = "Prevent streaming when on cellular data network",
                        checked = settings.wifiOnlyStreaming,
                        onCheckedChange = { chk -> viewModel.updateSettings { it.copy(wifiOnlyStreaming = chk) } }
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Estimated Cache Size", color = TextPrimary, fontSize = 13.sp)
                            Text(text = cacheSize, color = CinemaGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                viewModel.clearAllCache()
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("All temporary image and media caches cleared.")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SurfaceDark,
                                contentColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(8.dp),
                            border = ButtonDefaults.outlinedButtonBorder
                        ) {
                            Text("Clear Cache", fontSize = 12.sp)
                        }
                    }
                }
            }

            // Section: TMDB API Key Configuration
            item {
                SettingsSection(
                    title = "Movie Metadata API Key",
                    icon = Icons.Default.Key
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Official TMDB API Key (Optional)",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Supply an optional official TMDB API key to search and discover the entire global catalog. We never hardcode or share keys.",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = apiKeyInput,
                            onValueChange = { apiKeyInput = it },
                            placeholder = { Text("Enter your TMDB v3 API Key...", color = TextTertiary, fontSize = 12.sp) },
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CinemaGold,
                                unfocusedBorderColor = SurfaceBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedContainerColor = SurfaceDark,
                                unfocusedContainerColor = SurfaceDark
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("settings_api_key_input")
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                viewModel.updateSettings { it.copy(tmdbApiKey = apiKeyInput.trim()) }
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("API Key preferences updated.")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CinemaGold,
                                contentColor = AmoledBlack
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Save Key", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }

            // Section: Legal & Public Domain Compliance Statement
            item {
                SettingsSection(
                    title = "Legal Compliance & About",
                    icon = Icons.Default.Info
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "CinemaHub v1.0.0",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "CinemaHub is an open cinema and legal discovery player. CinemaHub strictly does NOT provide pirated movies, illegal torrents, or unauthorized streams.",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "All curated movies are provided under Creative Commons Attribution licenses (Blender Studio open movies) or reside in the verified Public Domain (Night of the Living Dead, Charade, His Girl Friday, The General).",
                            color = TextTertiary,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
private fun SettingsSection(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceCard)
            .border(1.dp, SurfaceBorder.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CinemaGold,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        HorizontalDivider(color = SurfaceBorder.copy(alpha = 0.4f))

        content()
    }
}

@Composable
private fun SettingsSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text(text = subtitle, color = TextSecondary, fontSize = 11.sp)
        }

        Spacer(modifier = Modifier.width(12.dp))

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = AmoledBlack,
                checkedTrackColor = CinemaGold,
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = SurfaceDark
            )
        )
    }
}
