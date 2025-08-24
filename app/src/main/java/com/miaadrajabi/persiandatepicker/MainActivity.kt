package com.miaadrajabi.persiandatepicker

import android.os.Bundle
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miaadrajabi.persiandatepicker.ui.DialogGravity
import com.miaadrajabi.persiandatepicker.ui.PersianDatePicker
import com.miaadrajabi.persiandatepicker.ui.PersianDatePickerBuilder
import com.miaadrajabi.persiandatepicker.ui.PersianDatePickerStyle
import com.miaadrajabi.persiandatepicker.ui.PresentationStyle
import com.miaadrajabi.persiandatepicker.ui.SelectionMode
import com.miaadrajabi.persiandatepicker.ui.SelectionResult
import com.miaadrajabi.persiandatepicker.ui.theme.PersianDatePickerTheme
import com.miaadrajabi.persiandatepicker.ui.theme.PersianFonts
import com.miaadrajabi.persiandatepicker.utils.PersianDate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PersianDatePickerTheme {
                PersianDatePickerDemo()
            }
        }
    }
}

@Composable
fun PersianDatePickerDemo() {
    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var resultText by remember { mutableStateOf("هیچ تاریخی انتخاب نشده است") }
    
    val today = PersianDate.toPersianDate(Date())
    
    // Conversion accuracy test
    LaunchedEffect(Unit) {
        val testDate = Calendar.getInstance().apply {
            set(2024, 6, 15) // 15 July 2024
        }.time
        
        val convertedPersian = PersianDate.toPersianDate(testDate)
        println("تست تبدیل تاریخ: 15 جولای 2024 = ${convertedPersian.getFormattedDate()}")
        
        val today2024 = Calendar.getInstance().apply {
            set(2024, 6, 15)
        }.time
        val todayPersian = PersianDate.toPersianDate(today2024)
        println("15 جولای 2024 = ${todayPersian.day} ${todayPersian.getMonthName()} ${todayPersian.year}")
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF8F9FA)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "تقویم فارسی",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2196F3),
                fontFamily = PersianFonts.Bold
            )
            
            Text(
                text = "Persian Date Picker",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF666666),
                fontFamily = PersianFonts.Regular
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Current Date Card
            Card(
                modifier = Modifier
                    .width(300.dp)
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "امروز",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF666666),
                        fontFamily = PersianFonts.Regular
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = today.getFormattedDate(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333),
                        fontFamily = PersianFonts.Bold
                    )
                    
                    Text(
                        text = today.getDayOfWeekName(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2196F3),
                        fontFamily = PersianFonts.Regular
                    )
                    
                    // Show Gregorian date as comparison
                    val gregorianFormatter = SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH)
                    Text(
                        text = "میلادی: ${gregorianFormatter.format(Date())}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF999999),
                        fontFamily = PersianFonts.Regular
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Date Picker Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Bottom Sheet Button
                Button(
                    onClick = { showDatePicker = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2196F3)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) {
                    Text(
                        text = "Bottom Sheet",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        fontFamily = PersianFonts.Regular
                    )
                }
                
                // Dialog Button
                Button(
                    onClick = { showDatePickerDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) {
                    Text(
                        text = "دیالوگ",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        fontFamily = PersianFonts.Regular
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Result Card
            Card(
                modifier = Modifier
                    .width(350.dp)
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "نتیجه انتخاب",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF666666),
                        fontFamily = PersianFonts.Regular
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text(
                        text = resultText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF333333),
                        fontFamily = PersianFonts.Regular,
                        textAlign = TextAlign.Center,
                        style = androidx.compose.ui.text.TextStyle(
                            textDirection = TextDirection.ContentOrRtl
                        )
                    )
                }
            }
        }
    }
    
    // Persian Date Picker - Bottom Sheet (MULTIPLE) demo with style and listeners
    PersianDatePicker(
        isVisible = showDatePicker,
        config = PersianDatePickerBuilder()
            .selectionMode(SelectionMode.MULTIPLE)
            .presentation(PresentationStyle.BOTTOM_SHEET)
            .dialogMargin(horizontal = 16.dp, vertical = 8.dp)
            .contentPadding(horizontal = 20.dp, vertical = 16.dp)
            .singleTitle("انتخاب تاریخ")
            .multipleTitle("انتخاب چند تاریخ")
            .rangeTitle("انتخاب بازه زمانی")
            .style(
                PersianDatePickerStyle(
                    primaryColor = Color(0xFFF3F2F2),
                    selectedDayColor = Color(0xFFFF9800),
                    weekdayColor = Color(0xFFF3F2F2),
                    containerBackgroundColor = Color(0xDD212121),
                    scrimColor = Color(0x54404040), // BottomSheet
                    navButtonBackground = Color(0xFF929292),
                    navButtonIconColor = Color(0xFF6750A4),
                    confirmButtonBackground = Color(0xFFFF9800),
                    confirmButtonTextColor = Color.White,
                    cancelButtonTextColor = Color(0xFFF3F2F2),
                    dialogTitleTextColor = Color(0xFFF3F2F2),
                    bottomSheetTitleTextColor = Color(0xFFF3F2F2),
                    navPrevIconColor = Color(0xFFF3F2F2),
                    navNextIconColor = Color(0xFFF3F2F2),
                    monthTitleTextColor = Color(0xFFF3F2F2),
                    yearCenterTextColor = Color(0xFFF3F2F2),
                    dayDefaultTextColor = Color(0xFFF3F2F2),
                    dayTodayTextColor = Color(0xFFF3F2F2),
                    selectionFooterBackgroundColor = Color(0xFFE3F2FD),
                    selectionFooterTextColor = Color(0xFF0D47A1),
                    selectionFooterCornerRadius = 12.dp,
                    dayCellSize = 40.dp,
                    buttonHeight = 48.dp,
                    yearPickerBackgroundColor = Color(0x000E0E0E),
                    yearPickerTextColor = Color(0xFFFFFFFF),
                    yearPickerSelectedTextColor = Color.White,
                    yearPickerItemBackgroundColor = Color(0xDD212121),
                    yearPickerSelectedBackgroundColor = Color(0xFFFF9800),
                    yearPickerCornerRadius = 12.dp,
                    yearPickerItemHeight = 48.dp,
                    yearPickerColumns = 2,
                    yearPickerRangeYears = 6
                )
            )
            .showTodayButton(true)
            .todayButtonText("امروز")
            .onContainerClick { /* کلیک روی پس‌زمینه باتم‌شیت/دیالوگ */ }
            .onCardClick { /* کلیک روی کارت محتوا */ }
            .onTodayClick { today ->
                resultText = "امروز انتخاب شد (Bottom Sheet):\n\n${today.getFormattedDate()}\n${today.getNumericFormattedDate()}"
            }
            .build(),
        onDismiss = { showDatePicker = false },
        onResult = { result ->
            when (result) {
                is SelectionResult.Single -> {
                    resultText = "تاریخ انتخاب شده (Bottom Sheet):\n\n" +
                        "${result.date.getFormattedDate()}\n${result.date.getNumericFormattedDate()}"
                }
                is SelectionResult.Multiple -> {
                    resultText = if (result.dates.isNotEmpty()) {
                        "تاریخ های انتخاب شده (Bottom Sheet):\n\n" +
                            result.dates.joinToString("\n") { d ->
                                "${d.getFormattedDate()}\n${d.getNumericFormattedDate()}\n"
                            }
                    } else {
                        "هیچ تاریخی انتخاب نشده است"
                    }
                }
                is SelectionResult.Range -> {
                    val r = result.range
                    resultText = when {
                        r.startDate != null && r.endDate != null -> {
                            "بازه انتخاب شده (Bottom Sheet):\n\n" +
                                    "از: ${r.startDate!!.getFormattedDate()}\n" +
                                    "تا: ${r.endDate!!.getFormattedDate()}\n\n" +
                                    "فرمت عددی:\n" +
                                    "از: ${r.startDate!!.getNumericFormattedDate()}\n" +
                                    "تا: ${r.endDate!!.getNumericFormattedDate()}"
                        }
                        r.startDate != null -> {
                            "تاریخ شروع:\n${r.startDate!!.getFormattedDate()}\n${r.startDate!!.getNumericFormattedDate()}"
                        }
                        else -> "هیچ تاریخی انتخاب نشده است"
                    }
                }
            }
        }
    )
    
    // Persian Date Picker - Dialog (RANGE) demo with different style and gravity
    PersianDatePicker(
        isVisible = showDatePickerDialog,
        config = PersianDatePickerBuilder()
            .selectionMode(SelectionMode.RANGE)
            .presentation(PresentationStyle.DIALOG)
            .gravity(DialogGravity.CENTER)
            .singleTitle("انتخاب تاریخ")
            .multipleTitle("انتخاب چند تاریخ")
            .rangeTitle("انتخاب بازه زمانی")
            .sizeFraction(widthFraction = 0.95f, heightFraction = null)
            .dialogMargin(horizontal = 16.dp, vertical = 16.dp)
            .contentPadding(horizontal = 24.dp, vertical = 24.dp)
            .style(
                PersianDatePickerStyle(
                    primaryColor = Color(0xFF000000),
                    selectedDayColor = Color(0xFFFF9800),
                    weekdayColor = Color(0xFF000000),
                    containerBackgroundColor = Color(0x0C3D3D3D),
                    cardContainerColor = Color(0xF5FFFFFF),
                    scrimColor = Color(0x00929292), // BottomSheet
                    navButtonBackground = Color(0xFFF3F2F2),
                    navButtonIconColor = Color(0xFF6750A4),
                    confirmButtonBackground = Color(0xFFFF9800),
                    confirmButtonTextColor = Color.White,
                    cancelButtonTextColor = Color(0xFF000000),
                    dialogTitleTextColor = Color(0xFF000000),
                    bottomSheetTitleTextColor = Color(0xFF000000),
                    navPrevIconColor = Color(0xFF000000),
                    navNextIconColor = Color(0xFF000000),
                    monthTitleTextColor = Color(0xFF000000),
                    yearCenterTextColor = Color(0xFF000000),
                    dayDefaultTextColor = Color(0xFF000000),
                    dayTodayTextColor = Color(0xFF000000),
                    selectionFooterBackgroundColor = Color(0xFFE3F2FD),
                    selectionFooterTextColor = Color(0xFF0D47A1),
                    selectionFooterCornerRadius = 12.dp,
                    dayCellSize = 40.dp,
                    buttonHeight = 48.dp,
                )
            )
            .showTodayButton(true)
            .todayButtonText("امروز")
            .onContainerClick { /* کلیک روی پس‌زمینه دیالوگ */ }
            .onCardClick { /* کلیک روی کارت داخل دیالوگ */ }
            .onTodayClick { today ->
                resultText = "امروز انتخاب شد (دیالوگ):\n\n${today.getFormattedDate()}\n${today.getNumericFormattedDate()}"
            }
            .build(),
        onDismiss = { showDatePickerDialog = false },
        onResult = { result ->
            when (result) {
                is SelectionResult.Single -> {
                    resultText = "تاریخ انتخاب شده (دیالوگ):\n\n" +
                        "${result.date.getFormattedDate()}\n${result.date.getNumericFormattedDate()}"
                }
                is SelectionResult.Multiple -> {
                    resultText = if (result.dates.isNotEmpty()) {
                        "تاریخ های انتخاب شده (دیالوگ):\n\n" +
                                result.dates.joinToString("\n") { d ->
                                    "${d.getFormattedDate()}\n${d.getNumericFormattedDate()}\n"
                                }
                    } else {
                        "هیچ تاریخی انتخاب نشده است"
                    }
                }
                is SelectionResult.Range -> {
                    val r = result.range
                    resultText = when {
                        r.startDate != null && r.endDate != null -> {
                            "بازه انتخاب شده (دیالوگ):\n\n" +
                                    "از: ${r.startDate!!.getFormattedDate()}\n" +
                                    "تا: ${r.endDate!!.getFormattedDate()}\n\n" +
                                    "فرمت عددی:\n" +
                                    "از: ${r.startDate!!.getNumericFormattedDate()}\n" +
                                    "تا: ${r.endDate!!.getNumericFormattedDate()}"
                        }
                        r.startDate != null -> {
                            "تاریخ شروع:\n${r.startDate!!.getFormattedDate()}\n${r.startDate!!.getNumericFormattedDate()}"
                        }
                        else -> "هیچ تاریخی انتخاب نشده است"
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PersianDatePickerPreview() {
    PersianDatePickerTheme {
        PersianDatePickerDemo()
    }
}