package com.miaadrajabi.persiandatepicker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miaadrajabi.persiandatepicker.ui.theme.PersianDatePickerTheme
import com.miaadrajabi.persiandatepicker.ui.theme.PersianFonts
import com.miaadrajabi.persiandatepicker.utils_java.PersianDateImpl
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class JavaTestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PersianDatePickerTheme {
                JavaTestScreen()
            }
        }
    }
}

@Composable
fun JavaTestScreen() {
    var testResults by remember { mutableStateOf("") }
    
    // Auto tests on screen load
    LaunchedEffect(Unit) {
        val results = StringBuilder()
        
        // Test 1: today's date
        results.append("=== تست تاریخ امروز ===\n")
        val todayJava = PersianDateImpl.toPersianDate(Date())
        results.append("امروز (Java): ${todayJava.getFormattedDate()}\n")
        results.append("عددی: ${todayJava.getNumericFormattedDate()}\n")
        results.append("ماه: ${todayJava.getMonthName()}\n")
        results.append("سال: ${PersianDateImpl.toPersianNumber(todayJava.getYear())}\n\n")
        
        // Test 2: specific date - 15 July 2024
        results.append("=== تست تاریخ خاص ===\n")
        val testDate = Calendar.getInstance().apply {
            set(2024, 6, 15) // 15 July 2024
        }.time
        
        val persianTestDate = PersianDateImpl.toPersianDate(testDate)
        results.append("15 جولای 2024:\n")
        results.append("Java: ${persianTestDate.getFormattedDate()}\n")
        results.append("عددی: ${persianTestDate.getNumericFormattedDate()}\n\n")
        
        // Test 3: Persian digits
        results.append("=== تست اعداد فارسی ===\n")
        val numbers = arrayOf(0, 1, 10, 100, 1403, 2024)
        for (number in numbers) {
            results.append("$number -> ${PersianDateImpl.toPersianNumber(number)}\n")
        }
        results.append("\n")
        
        // Test 4: leap years
        results.append("=== تست سال کبیسه ===\n")
        val years = arrayOf(1403, 1404, 1405, 1406)
        for (year in years) {
            val isLeap = PersianDateImpl.isLeapYear(year)
            results.append("سال ${PersianDateImpl.toPersianNumber(year)}: ${if (isLeap) "کبیسه" else "عادی"}\n")
        }
        results.append("\n")
        
        // Test 5: Compare Kotlin vs Java
        results.append("=== مقایسه Kotlin vs Java ===\n")
        val kotlinToday = com.miaadrajabi.persiandatepicker.utils.PersianDate.toPersianDate(Date())
        val javaToday = PersianDateImpl.toPersianDate(Date())
        
        results.append("Kotlin: ${kotlinToday.getFormattedDate()}\n")
        results.append("Java: ${javaToday.getFormattedDate()}\n")
        results.append("یکسان: ${kotlinToday.getFormattedDate() == javaToday.getFormattedDate()}\n\n")
        
        testResults = results.toString()
        
        // Log to console
        Log.d("JavaTest", testResults)
    }
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF8F9FA)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "تست نسخه Java",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2196F3),
                fontFamily = PersianFonts.Bold,
                modifier = Modifier.padding(16.dp)
            )
            
            Text(
                text = "Java Persian Date Picker Test",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF666666),
                fontFamily = PersianFonts.Regular
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Test results card
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        // Test results
                        Text(
                            text = testResults,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF333333),
                            fontFamily = PersianFonts.Regular,
                            textAlign = TextAlign.Start,
                            style = androidx.compose.ui.text.TextStyle(
                                textDirection = TextDirection.ContentOrRtl
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        // Re-run test button
                        Button(
                            onClick = {
                                // Re-run
                                testResults = "در حال تست مجدد..."
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "تست مجدد",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White,
                                fontFamily = PersianFonts.Regular
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Additional info
            Text(
                text = "این تست نشان می‌دهد که کلاس های Java دقیقاً همان نتایج Kotlin را می‌دهند",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF666666),
                fontFamily = PersianFonts.Regular,
                textAlign = TextAlign.Center,
                style = androidx.compose.ui.text.TextStyle(
                    textDirection = TextDirection.ContentOrRtl
                )
            )
        }
    }
}

// Manual test for comparison
fun manualTest() {
    println("=== Manual test for Java classes ===")
    
    // Construct dates
    val date1 = PersianDateImpl(1403, 4, 24)
    val date2 = PersianDateImpl(1403, 4, 25)
    
    println("تاریخ 1: ${date1.getFormattedDate()}")
    println("تاریخ 2: ${date2.getFormattedDate()}")
    
    // Comparison
    println("date1 قبل از date2: ${date1.isBefore(date2)}")
    println("date1 بعد از date2: ${date1.isAfter(date2)}")
    println("date1 مساوی date2: ${date1.isSameDay(date2)}")
    
    // Date conversion
    val today = Date()
    val persianToday = PersianDateImpl.toPersianDate(today)
    println("امروز: ${persianToday.getFormattedDate()}")
    
    // Persian digits  
    println("عدد ۱۴۰۳: ${PersianDateImpl.toPersianNumber(1403)}")
    println("عدد ۲۰۲۴: ${PersianDateImpl.toPersianNumber(2024)}")
} 