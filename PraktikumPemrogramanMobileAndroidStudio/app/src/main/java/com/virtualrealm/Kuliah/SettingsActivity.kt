package com.virtualrealm.Kuliah

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
// Icons import
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.virtualrealm.Kuliah.ui.theme.KuliahTheme

class SettingsActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Read the saved preferences
            val context = LocalContext.current
            val preferences = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
            val savedDarkMode = preferences.getBoolean("dark_mode", isSystemInDarkTheme())
            val savedNotifications = preferences.getBoolean("notifications_enabled", true)

            // Use the saved dark mode preference
            KuliahTheme(darkTheme = savedDarkMode) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Pengaturan") },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali")
                                }
                            }
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    SettingsScreen(
                        initialDarkMode = savedDarkMode,
                        initialNotifications = savedNotifications,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(
    initialDarkMode: Boolean = false,
    initialNotifications: Boolean = true,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isDarkTheme by remember { mutableStateOf(initialDarkMode) }
    var isNotificationEnabled by remember { mutableStateOf(initialNotifications) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    // Function to save preferences
    fun savePreferences() {
        val preferences = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        preferences.edit()
            .putBoolean("dark_mode", isDarkTheme)
            .putBoolean("notifications_enabled", isNotificationEnabled)
            .apply()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Pengaturan Aplikasi",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Dark mode setting
        SettingsSwitchItem(
            title = "Mode Gelap",
            description = "Mengaktifkan tampilan gelap untuk aplikasi",
            checked = isDarkTheme,
            onCheckedChange = {
                isDarkTheme = it
                savePreferences()

                // Show restart suggestion toast
                Toast.makeText(
                    context,
                    "Silakan restart aplikasi untuk menerapkan tema baru",
                    Toast.LENGTH_LONG
                ).show()
            }
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        // Notifications setting
        SettingsSwitchItem(
            title = "Notifikasi",
            description = "Aktifkan untuk menerima notifikasi terbaru",
            checked = isNotificationEnabled,
            onCheckedChange = {
                isNotificationEnabled = it
                savePreferences()

                val message = if (it) "Notifikasi diaktifkan" else "Notifikasi dinonaktifkan"
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        // Language setting
        SettingsButtonItem(
            title = "Bahasa",
            description = "Pilih bahasa yang digunakan dalam aplikasi",
            icon = Icons.Default.Settings,
            onClick = { showLanguageDialog = true }
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        // About setting
        SettingsButtonItem(
            title = "Tentang Aplikasi",
            description = "Informasi tentang aplikasi dan versi",
            icon = Icons.Default.Info,
            onClick = { showAboutDialog = true }
        )

        // Language dialog
        if (showLanguageDialog) {
            LanguageSelectionDialog(
                onDismiss = { showLanguageDialog = false },
                onLanguageSelected = { language ->
                    // Save selected language
                    val preferences = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
                    preferences.edit().putString("language", language).apply()

                    Toast.makeText(context, "Bahasa diubah ke $language", Toast.LENGTH_SHORT).show()
                    showLanguageDialog = false
                }
            )
        }

        // About dialog
        if (showAboutDialog) {
            AboutDialog(onDismiss = { showAboutDialog = false })
        }
    }
}

@Composable
fun SettingsSwitchItem(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun SettingsButtonItem(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(end = 16.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun LanguageSelectionDialog(
    onDismiss: () -> Unit,
    onLanguageSelected: (String) -> Unit
) {
    val languages = listOf("Bahasa Indonesia", "English", "日本語", "한국어")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Pilih Bahasa",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                languages.forEach { language ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = { onLanguageSelected(language) })
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = language,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    if (language != languages.last()) {
                        HorizontalDivider()
                    }
                }

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 8.dp)
                ) {
                    Text("Batal")
                }
            }
        }
    }
}

@Composable
fun AboutDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Tentang Aplikasi",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Kuliah App",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Versi 1.0.0",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "© 2025 Virtual Realm.\nSemua hak cipta dilindungi.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Tutup")
                }
            }
        }
    }
}