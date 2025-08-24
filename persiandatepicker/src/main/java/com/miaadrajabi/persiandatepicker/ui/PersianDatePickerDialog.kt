package com.miaadrajabi.persiandatepicker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.miaadrajabi.persiandatepicker.ui.theme.PersianFonts
import com.miaadrajabi.persiandatepicker.utils.PersianCalendar
import com.miaadrajabi.persiandatepicker.utils.PersianDate
import java.util.Date

@Composable
fun PersianDatePickerDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onDateRangeSelected: (PersianDateRange?) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isVisible) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )
        ) {
            PersianDatePickerDialogContent(
                onDismiss = onDismiss,
                onDateRangeSelected = onDateRangeSelected,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun PersianDatePickerDialogContent(
    onDismiss: () -> Unit,
    onDateRangeSelected: (PersianDateRange?) -> Unit,
    modifier: Modifier = Modifier,
) {
    // State management
    var currentPersianCalendar by remember { mutableStateOf(PersianCalendar()) }
    var dateRange by remember { mutableStateOf(PersianDateRange()) }
    val today = PersianDate.toPersianDate(Date())
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Header
            PersianDatePickerDialogHeader(
                onCloseClick = onDismiss
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Month Navigation
            PersianDatePickerMonthNavigation(
                currentPersianCalendar = currentPersianCalendar,
                onPreviousMonth = { 
                    currentPersianCalendar.addMonths(-1)
                    currentPersianCalendar = PersianCalendar(
                        currentPersianCalendar.getYear(),
                        currentPersianCalendar.getMonth(),
                        1
                    )
                },
                onNextMonth = { 
                    currentPersianCalendar.addMonths(1)
                    currentPersianCalendar = PersianCalendar(
                        currentPersianCalendar.getYear(),
                        currentPersianCalendar.getMonth(),
                        1
                    )
                }
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Calendar Grid
            PersianDatePickerCalendarGrid(
                currentPersianCalendar = currentPersianCalendar,
                dateRange = dateRange,
                today = today,
                onDateSelected = { date ->
                    dateRange = when {
                        dateRange.startDate == null -> PersianDateRange(startDate = date)
                        dateRange.endDate == null -> {
                            if (date.isBefore(dateRange.startDate!!)) {
                                PersianDateRange(
                                    startDate = date,
                                    endDate = dateRange.startDate
                                )
                            } else {
                                PersianDateRange(
                                    startDate = dateRange.startDate,
                                    endDate = date
                                )
                            }
                        }
                        else -> PersianDateRange(startDate = date)
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Selected Range Display
            if (dateRange.startDate != null || dateRange.endDate != null) {
                PersianDatePickerSelectedRange(dateRange = dateRange)
                Spacer(modifier = Modifier.height(20.dp))
            }
            
            // Action Buttons
            PersianDatePickerDialogActions(
                onCancel = onDismiss,
                onConfirm = {
                    onDateRangeSelected(dateRange)
                    onDismiss()
                },
                isConfirmEnabled = dateRange.startDate != null
            )
        }
    }
}

@Composable
private fun PersianDatePickerDialogHeader(
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "انتخاب بازه تاریخ",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333),
            fontFamily = PersianFonts.Bold,
            style = TextStyle(
                textDirection = TextDirection.ContentOrRtl
            )
        )
        
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = Color(0xFFF5F5F5),
                    shape = CircleShape
                )
                .clip(CircleShape)
                .clickable { onCloseClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "×",
                fontSize = 20.sp,
                color = Color(0xFF666666),
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    textDirection = TextDirection.ContentOrRtl
                )
            )
        }
    }
}

@Composable
private fun PersianDatePickerMonthNavigation(
    currentPersianCalendar: PersianCalendar,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Previous Month Button
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    color = Color(0xFF2196F3).copy(alpha = 0.1f),
                    shape = CircleShape
                )
                .clip(CircleShape)
                .clickable { onPreviousMonth() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "‹",
                fontSize = 18.sp,
                color = Color(0xFF2196F3),
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    textDirection = TextDirection.ContentOrRtl
                )
            )
        }
        
        // Month and Year
        val persianDate = currentPersianCalendar.getPersianDate()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = persianDate.getMonthName(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                fontFamily = PersianFonts.Bold,
                style = TextStyle(
                    textDirection = TextDirection.ContentOrRtl
                )
            )
            Text(
                text = PersianDate.toPersianNumber(persianDate.year),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF666666),
                fontFamily = PersianFonts.Regular,
                style = TextStyle(
                    textDirection = TextDirection.ContentOrRtl
                )
            )
        }
        
        // Next Month Button
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    color = Color(0xFF2196F3).copy(alpha = 0.1f),
                    shape = CircleShape
                )
                .clip(CircleShape)
                .clickable { onNextMonth() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "›",
                fontSize = 18.sp,
                color = Color(0xFF2196F3),
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    textDirection = TextDirection.ContentOrRtl
                )
            )
        }
    }
}

@Composable
private fun PersianDatePickerCalendarGrid(
    currentPersianCalendar: PersianCalendar,
    dateRange: PersianDateRange,
    today: PersianDate,
    onDateSelected: (PersianDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Days of week header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PersianDate.PERSIAN_WEEKDAYS_SHORT.forEach { day ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2196F3),
                        fontFamily = PersianFonts.Regular,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            textDirection = TextDirection.ContentOrRtl
                        )
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Calendar dates
        val firstDayOfWeek = currentPersianCalendar.getFirstDayOfWeekInMonth()
        val daysInMonth = currentPersianCalendar.getDaysInCurrentMonth()
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.height(240.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Empty cells for days before the first day of month
            items(firstDayOfWeek) {
                Spacer(modifier = Modifier.size(36.dp))
            }
            
            // Days of the month
            items(daysInMonth) { dayIndex ->
                val date = PersianDate(
                    currentPersianCalendar.getYear(),
                    currentPersianCalendar.getMonth(),
                    dayIndex + 1
                )
                
                val isDateBetween = { date: PersianDate, start: PersianDate?, end: PersianDate? ->
                    start != null && end != null && date.isAfter(start) && date.isBefore(end)
                }
                
                PersianDatePickerCalendarDay(
                    date = date,
                    isToday = date.isSameDay(today),
                    isRangeStart = dateRange.startDate?.isSameDay(date) == true,
                    isRangeEnd = dateRange.endDate?.isSameDay(date) == true,
                    isInRange = isDateBetween(date, dateRange.startDate, dateRange.endDate),
                    onDateSelected = { onDateSelected(date) }
                )
            }
        }
    }
}

@Composable
private fun PersianDatePickerCalendarDay(
    date: PersianDate,
    isToday: Boolean,
    isRangeStart: Boolean,
    isRangeEnd: Boolean,
    isInRange: Boolean,
    onDateSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedColor = Color(0xFF2196F3)
    val backgroundColor = when {
        isRangeStart || isRangeEnd -> selectedColor
        isInRange -> selectedColor.copy(alpha = 0.3f)
        isToday -> selectedColor.copy(alpha = 0.1f)
        else -> Color.Transparent
    }
    
    val textColor = when {
        isRangeStart || isRangeEnd -> Color.White
        isToday -> selectedColor
        else -> Color(0xFF333333)
    }
    
    Box(
        modifier = modifier
            .size(36.dp)
            .background(
                color = backgroundColor,
                shape = when {
                    isRangeStart || isRangeEnd -> CircleShape
                    isInRange -> RoundedCornerShape(4.dp)
                    else -> CircleShape
                }
            )
            .run {
                if (isToday && !isRangeStart && !isRangeEnd) {
                    border(
                        width = 1.dp,
                        color = selectedColor,
                        shape = CircleShape
                    )
                } else this
            }
            .clip(CircleShape)
            .clickable { onDateSelected() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = PersianDate.toPersianNumber(date.day),
            fontSize = 14.sp,
            fontWeight = if (isRangeStart || isRangeEnd) FontWeight.Bold else FontWeight.Normal,
            color = textColor,
            fontFamily = PersianFonts.Regular,
            style = TextStyle(
                textDirection = TextDirection.ContentOrRtl
            )
        )
    }
}

@Composable
private fun PersianDatePickerSelectedRange(
    dateRange: PersianDateRange,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2196F3).copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "بازه انتخاب شده",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2196F3),
                fontFamily = PersianFonts.Regular,
                style = TextStyle(
                    textDirection = TextDirection.ContentOrRtl
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            val rangeText = when {
                dateRange.startDate != null && dateRange.endDate != null -> {
                    "از: ${dateRange.startDate.getFormattedDate()}\n" +
                    "تا: ${dateRange.endDate.getFormattedDate()}\n\n" +
                    "فرمت عددی:\n" +
                    "از: ${dateRange.startDate.getNumericFormattedDate()}\n" +
                    "تا: ${dateRange.endDate.getNumericFormattedDate()}"
                }
                dateRange.startDate != null -> {
                    "از: ${dateRange.startDate.getFormattedDate()}\n${dateRange.startDate.getNumericFormattedDate()}"
                }
                else -> "تاریخ انتخاب کنید"
            }
            
            Text(
                text = rangeText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                fontFamily = PersianFonts.Bold,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    textDirection = TextDirection.ContentOrRtl
                )
            )
        }
    }
}

@Composable
private fun PersianDatePickerDialogActions(
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    isConfirmEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Cancel Button
        TextButton(
            onClick = onCancel,
            modifier = Modifier.weight(1f),
            colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                contentColor = Color(0xFF666666)
            )
        ) {
            Text(
                text = "لغو",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = PersianFonts.Regular,
                style = TextStyle(
                    textDirection = TextDirection.ContentOrRtl
                )
            )
        }
        
        // Confirm Button
        Button(
            onClick = onConfirm,
            modifier = Modifier.weight(1f),
            enabled = isConfirmEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3),
                disabledContainerColor = Color(0xFFBDBDBD),
                contentColor = Color.White,
                disabledContentColor = Color(0xFF757575)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "تأیید",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = PersianFonts.Regular,
                color = if (isConfirmEnabled) Color.White else Color(0xFF757575),
                style = TextStyle(
                    textDirection = TextDirection.ContentOrRtl
                )
            )
        }
    }
} 