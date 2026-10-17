package com.example.userinputprak5

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.userinputprak5.ui.theme.UserInputPrak5Theme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormBiodataScreen() {
    val context = LocalContext.current

    // State variabel untuk menampung input user
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    // State khusus untuk Dropdown Experience
    var experience by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val experienceOptions = listOf("0-1 Tahun", "1-3 Tahun", "3-5 Tahun", "5+ Tahun")

    // State khusus untuk DatePicker (Kalender)
    var tglLahir by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    // Konfigurasi agar kalender tidak bisa memilih tanggal di masa depan
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                // Hanya izinkan tanggal yang lebih kecil atau sama dengan hari ini
                return utcTimeMillis <= System.currentTimeMillis()
            }
        }
    )

    // State Checkbox
    var isAndroid by remember { mutableStateOf(false) }
    var isUiUx by remember { mutableStateOf(false) }
    var isWeb by remember { mutableStateOf(false) }

    // Logika Validasi Email (Error jika sudah diketik tapi tidak diakhiri @gmail.com)
    val isEmailError = email.isNotEmpty() && !email.endsWith("@gmail.com")

    // Validasi tombol Proceed:
    // 1. Semua form teks wajib terisi
    // 2. Email tidak boleh error (@gmail.com)
    // 3. Minimal salah satu skill (Checkbox) harus dipilih
    val isFormValid = firstName.isNotEmpty() &&
            lastName.isNotEmpty() &&
            email.isNotEmpty() &&
            !isEmailError &&
            tglLahir.isNotEmpty() &&
            experience.isNotEmpty() &&
            (isAndroid || isUiUx || isWeb) // Syarat tambahan untuk Checkbox

    // Dialog Kalender
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    datePickerState.selectedDateMillis?.let { millis ->
                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        tglLahir = formatter.format(Date(millis))
                    }
                }) {
                    Text(stringResource(id = R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(id = R.string.batal))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 1. Gambar Background di bagian atas
            Image(
                painter = painterResource(id = R.drawable.bg_header),
                contentDescription = stringResource(id = R.string.bg_desc),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.35f)
                    .align(Alignment.TopCenter)
            )

            // 2. Card Form dengan ukuran dari dimens.xml
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f)
                    .align(Alignment.BottomCenter)
                    .clip(
                        RoundedCornerShape(
                            topStart = dimensionResource(id = R.dimen.corner_radius_card),
                            topEnd = dimensionResource(id = R.dimen.corner_radius_card)
                        )
                    )
                    .background(colorResource(id = R.color.white_bg))
                    .padding(dimensionResource(id = R.dimen.padding_card))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.spacing_column))
                ) {
                    // Judul Form
                    Text(
                        text = stringResource(id = R.string.form_title),
                        fontSize = dimensionResource(id = R.dimen.text_size_title).value.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.purple_title)
                    )

                    // Subjudul Form

                    // Row Nama Depan & Belakang
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.spacing_row))
                    ) {
                        OutlinedTextField(
                            value = firstName,
                            onValueChange = { firstName = it },
                            label = { Text(stringResource(id = R.string.nama_depan)) },
                            shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_input)),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = lastName,
                            onValueChange = { lastName = it },
                            label = { Text(stringResource(id = R.string.nama_belakang)) },
                            shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_input)),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    // Email dengan Validasi Error
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(stringResource(id = R.string.email)) },
                        isError = isEmailError,
                        supportingText = {
                            if (isEmailError) {
                                Text(
                                    text = stringResource(id = R.string.error_email),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_input)),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    // Row Tgl Lahir & Dropdown Experience
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.spacing_row))
                    ) {
                        // Tgl Lahir
                        OutlinedTextField(
                            value = tglLahir,
                            onValueChange = { },
                            readOnly = true,
                            label = { Text(stringResource(id = R.string.tgl_lahir)) },
                            trailingIcon = {
                                IconButton(onClick = { showDatePicker = true }) {
                                    Icon(Icons.Default.DateRange, contentDescription = null)
                                }
                            },
                            shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_input)),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { showDatePicker = true },
                            singleLine = true
                        )

                        // Dropdown Experience
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = experience,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text(stringResource(id = R.string.experience)) },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                },
                                shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_input)),
                                modifier = Modifier.menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                experienceOptions.forEach { selectionOption ->
                                    DropdownMenuItem(
                                        text = { Text(selectionOption) },
                                        onClick = {
                                            experience = selectionOption
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Teks Skill
                    Text(
                        text = stringResource(id = R.string.pilih_skill),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Checkbox Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = isAndroid, onCheckedChange = { isAndroid = it })
                            Text(
                                text = stringResource(id = R.string.skill_android),
                                fontSize = dimensionResource(id = R.dimen.text_size_checkbox).value.sp
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = isUiUx, onCheckedChange = { isUiUx = it })
                            Text(
                                text = stringResource(id = R.string.skill_uiux),
                                fontSize = dimensionResource(id = R.dimen.text_size_checkbox).value.sp
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = isWeb, onCheckedChange = { isWeb = it })
                            Text(
                                text = stringResource(id = R.string.skill_web),
                                fontSize = dimensionResource(id = R.dimen.text_size_checkbox).value.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacing_medium)))

                    // Tombol Proceed
                    val successMessage = stringResource(id = R.string.submit_success)
                    Button(
                        onClick = {
                            Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
                        },
                        enabled = isFormValid,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dimensionResource(id = R.dimen.height_button)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.purple_title),
                            disabledContainerColor = colorResource(id = R.color.grey_button)
                        ),
                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_button))
                    ) {
                        Text(
                            text = stringResource(id = R.string.btn_proceed),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewFormBiodata() {
    UserInputPrak5Theme {
        FormBiodataScreen()
    }
}