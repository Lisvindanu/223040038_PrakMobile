package com.virtualrealm.Kuliah

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.virtualrealm.Kuliah.data.AppDatabase
import com.virtualrealm.Kuliah.data.User
import com.virtualrealm.Kuliah.ui.theme.KuliahTheme
import com.virtualrealm.Kuliah.ui.theme.Pink40
import com.virtualrealm.Kuliah.ui.theme.Purple80
import kotlinx.coroutines.launch

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KuliahTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FormRegistration(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun FormRegistration(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = AppDatabase.getDatabase(context)
    val userDao = database.userDao()

    val nama = remember { mutableStateOf(TextFieldValue("")) }
    val username = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf(TextFieldValue("")) }
    val nomorTelepon = remember { mutableStateOf(TextFieldValue("")) }
    val email = remember { mutableStateOf(TextFieldValue("")) }
    val alamat = remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Registrasi Akun",
            style = TextStyle(fontSize = 24.sp),
            modifier = Modifier.padding(16.dp)
        )

        Text(text = "Nama", modifier = Modifier.padding(4.dp).fillMaxWidth())
        TextField(
            value = nama.value,
            onValueChange = { nama.value = it },
            modifier = Modifier.padding(4.dp).fillMaxWidth()
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

        Text(text = "Nomor Telepon", modifier = Modifier.padding(4.dp).fillMaxWidth())
        TextField(
            value = nomorTelepon.value,
            onValueChange = { nomorTelepon.value = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.padding(4.dp).fillMaxWidth()
        )

        Text(text = "Email", modifier = Modifier.padding(4.dp).fillMaxWidth())
        TextField(
            value = email.value,
            onValueChange = { email.value = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.padding(4.dp).fillMaxWidth()
        )

        Text(text = "Alamat Rumah", modifier = Modifier.padding(4.dp).fillMaxWidth())
        TextField(
            value = alamat.value,
            onValueChange = { alamat.value = it },
            modifier = Modifier.padding(4.dp).fillMaxWidth()
        )

        val saveButtonColors = ButtonDefaults.buttonColors(
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
                    if (
                        nama.value.text.isNotEmpty() &&
                        username.value.text.isNotEmpty() &&
                        password.value.text.isNotEmpty() &&
                        nomorTelepon.value.text.isNotEmpty() &&
                        email.value.text.isNotEmpty() &&
                        alamat.value.text.isNotEmpty()
                    ) {
                        scope.launch {
                            val user = User(
                                username = username.value.text,
                                password = password.value.text,
                                nama = nama.value.text,
                                nomorTelepon = nomorTelepon.value.text,
                                email = email.value.text,
                                alamat = alamat.value.text
                            )
                            userDao.insertUser(user)
                            Toast.makeText(
                                context,
                                "Halo, ${nama.value.text}. Registrasi berhasil!",
                                Toast.LENGTH_LONG
                            ).show()

                            // Return to login screen
                            (context as? ComponentActivity)?.finish()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Semua inputan harus diisi",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                colors = saveButtonColors
            ) {
                Text(
                    text = "Simpan",
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
                    nama.value = TextFieldValue("")
                    username.value = TextFieldValue("")
                    password.value = TextFieldValue("")
                    nomorTelepon.value = TextFieldValue("")
                    email.value = TextFieldValue("")
                    alamat.value = TextFieldValue("")
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
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationPreview() {
    KuliahTheme {
        FormRegistration()
    }
}