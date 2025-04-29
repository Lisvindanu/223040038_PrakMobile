package com.virtualrealm.Kuliah
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.virtualrealm.Kuliah.data.AppDatabase
import com.virtualrealm.Kuliah.ui.theme.KuliahTheme
import com.virtualrealm.Kuliah.ui.theme.Pink40
import com.virtualrealm.Kuliah.ui.theme.Purple80
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Read dark mode preference without using isSystemInDarkTheme()
        val preferences = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        // Use system night mode setting as default instead of isSystemInDarkTheme()
        val systemNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        val systemDarkMode = systemNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES
        val useDarkTheme = preferences.getBoolean("dark_mode", systemDarkMode)

        setContent {
            KuliahTheme(darkTheme = useDarkTheme) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FormLogin(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun FormLogin(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = AppDatabase.getDatabase(context)
    val userDao = database.userDao()

    val username = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login",
            style = TextStyle(fontSize = 24.sp),
            modifier = Modifier.padding(16.dp)
        )

        Text(text = "Username", modifier = Modifier.padding(4.dp).fillMaxWidth())
        TextField(
            value = username.value,
            onValueChange = { username.value = it },
            modifier = Modifier.padding(4.dp).fillMaxWidth()
        )

        Text(text = "Password", modifier = Modifier.padding(4.dp).fillMaxWidth())
        TextField(
            value = password.value,
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = { password.value = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.padding(4.dp).fillMaxWidth()
        )

        val loginButtonColors = ButtonDefaults.buttonColors(
            containerColor = Purple80,
            contentColor = Pink40
        )

        val resetButtonColors = ButtonDefaults.buttonColors(
            containerColor = Pink40,
            contentColor = Purple80
        )

        Spacer(modifier = Modifier.weight(1f).width(0.dp))

        Row(modifier = Modifier.padding(4.dp).fillMaxWidth()) {
            Button(
                modifier = Modifier.weight(5f),
                onClick = {
                    scope.launch {
                        val user = userDao.login(username.value.text, password.value.text)
                        if (user != null) {
                            Toast.makeText(
                                context,
                                "Login Sukses: ${user.nama} (${user.username})",
                                Toast.LENGTH_LONG
                            ).show()
                            val intent = Intent(context, HomeActivity::class.java).apply {
                                putExtra("USERNAME", user.username)
                                putExtra("NAMA", user.nama)
                                putExtra("EMAIL", user.email)
                                putExtra("ALAMAT", user.alamat)
                                putExtra("NOMOR_TELEPON", user.nomorTelepon)
                            }
                            context.startActivity(intent)
                        } else {
                            val userExists = userDao.getUserByUsername(username.value.text)
                            if (userExists != null) {
                                Toast.makeText(context, "Password Salah", Toast.LENGTH_LONG).show()
                            }else {
                                Toast.makeText(context, "Username tidak ditemukan", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                },
                colors = loginButtonColors
            ) {
                Text(
                    text = "Login",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier.padding(8.dp)
                )
            }

            Button(
                modifier = Modifier.weight(5f),
                onClick = {
                    username.value = TextFieldValue("")
                    password.value = TextFieldValue("")
                },
                colors = resetButtonColors
            ) {
                Text(
                    text = "Reset",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {
            val intent = Intent(context, RegisterActivity::class.java)
            context.startActivity(intent)
        }) {
            Text(
                text = "Belum punya akun? Daftar disini",
                textAlign = TextAlign.Center,
                color = Purple80
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    KuliahTheme {
        FormLogin()
    }
}