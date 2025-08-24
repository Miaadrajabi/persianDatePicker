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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miaadrajabi.persiandatepicker.ui.theme.PersianFonts
import com.miaadrajabi.persiandatepicker.utils.PersianCalendar
import com.miaadrajabi.persiandatepicker.utils.PersianDate
import java.util.Date

enum class PersianDatePickerMode {
    TIME_RANGE,
    MULTIPLE_DAYS
}

data class PersianDateRange(
    val startDate: PersianDate? = null,
    val endDate: PersianDate? = null,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersianDatePickerBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onDone: (PersianDateRange?, List<PersianDate>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    // State management
    var selectedMode by remember { mutableStateOf(PersianDatePickerMode.TIME_RANGE) }
    var currentPersianCalendar by remember { mutableStateOf(PersianCalendar()) }
    var dateRange by remember { mutableStateOf(PersianDateRange()) }
    var selectedDates by remember { mutableStateOf(setOf<PersianDate>()) }
    val today = PersianDate.toPersianDate(Date())

    // Static corner radius - always rounded
    val cornerRadius = 20.dp

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = bottomSheetState,
            containerColor = Color(0xFFF5F5F5),
            contentColor = Color(0xFF333333),
            windowInsets = androidx.compose.foundation.layout.WindowInsets(0),
            shape = RoundedCornerShape(
                topStart = cornerRadius,
                topEnd = cornerRadius,
                bottomStart = 0.dp,
                bottomEnd = 0.dp
            ),
            dragHandle = {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .background(
                            color = Color(0xFFCCCCCC),
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Header
                PersianDatePickerHeader(
                    onCloseClick = onDismiss
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Mode Selection Tabs
                PersianDatePickerModeSelector(
                    selectedMode = selectedMode,
                    onModeSelected = { selectedMode = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Month Navigation
                PersianMonthNavigationHeader(
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

                Spacer(modifier = Modifier.height(16.dp))

                // Calendar Grid
                PersianCalendarGrid(
                    currentPersianCalendar = currentPersianCalendar,
                    selectedMode = selectedMode,
                    dateRange = dateRange,
                    selectedDates = selectedDates,
                    today = today,
                    onDateSelected = { date ->
                        when (selectedMode) {
                            PersianDatePickerMode.TIME_RANGE -> {
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

                            PersianDatePickerMode.MULTIPLE_DAYS -> {
                                selectedDates = if (selectedDates.any { it.isSameDay(date) }) {
                                    selectedDates.filter { !it.isSameDay(date) }.toSet()
                                } else {
                                    selectedDates + date
                                }
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Action Buttons
                PersianDatePickerActionButtons(
                    onCancel = onDismiss,
                    onDone = {
                        when (selectedMode) {
                            PersianDatePickerMode.TIME_RANGE -> onDone(dateRange, emptyList())
                            PersianDatePickerMode.MULTIPLE_DAYS -> onDone(null, selectedDates.toList())
                        }
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun PersianDatePickerHeader(
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        // Centered Title
        Text(
            text = "تاریخ مورد نظر خود را انتخاب کنید",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333),
            fontFamily = PersianFonts.Bold,
            modifier = Modifier.align(Alignment.Center)
        )

        // Close Button aligned to end
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp)
                )
                .clip(RoundedCornerShape(16.dp))
                .clickable { onCloseClick() }
                .align(Alignment.CenterEnd),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "×",
                fontSize = 24.sp,
                color = Color(0xFF666666),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun PersianDatePickerModeSelector(
    selectedMode: PersianDatePickerMode,
    onModeSelected: (PersianDatePickerMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .background(
                    color = Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Time Range Tab
            PersianDatePickerTab(
                text = "بازه زمانی",
                isSelected = selectedMode == PersianDatePickerMode.TIME_RANGE,
                onClick = { onModeSelected(PersianDatePickerMode.TIME_RANGE) },
                modifier = Modifier.width(120.dp)
            )

            // Multiple Days Tab
            PersianDatePickerTab(
                text = "چند روز",
                isSelected = selectedMode == PersianDatePickerMode.MULTIPLE_DAYS,
                onClick = { onModeSelected(PersianDatePickerMode.MULTIPLE_DAYS) },
                modifier = Modifier.width(120.dp)
            )
        }
    }
}

@Composable
private fun PersianDatePickerTab(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .background(
                color = if (isSelected) Color.White else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color(0xFF333333) else Color(0xFF666666),
            fontFamily = PersianFonts.Regular
        )
    }
}

@Composable
private fun PersianMonthNavigationHeader(
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
        PersianIconButton(
            onClick = onPreviousMonth,
            icon = "‹"
        )

        // Month and Year
        val persianDate = currentPersianCalendar.getPersianDate()
        Text(
            text = "${persianDate.getMonthName()} ${PersianDate.toPersianNumber(persianDate.year)}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333),
            fontFamily = PersianFonts.Bold
        )

        // Next Month Button
        PersianIconButton(
            onClick = onNextMonth,
            icon = "›"
        )
    }
}

@Composable
private fun PersianIconButton(
    onClick: () -> Unit,
    icon: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = Color(0xFFCCCCCC),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = icon,
            fontSize = 20.sp,
            color = Color(0xFF333333),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun PersianCalendarGrid(
    currentPersianCalendar: PersianCalendar,
    selectedMode: PersianDatePickerMode,
    dateRange: PersianDateRange,
    selectedDates: Set<PersianDate>,
    today: PersianDate,
    onDateSelected: (PersianDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 26.dp)
    ) {
        // Days of week header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PersianDate.PERSIAN_WEEKDAYS_SHORT.forEach { day ->
                Text(
                    text = day,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF678F33),
                    fontFamily = PersianFonts.Regular,
                    modifier = Modifier.weight(1f)
                )
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
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Empty cells for days before the first day of month
            items(firstDayOfWeek) {
                Spacer(modifier = Modifier.size(40.dp))
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
                
                PersianCalendarDay(
                    date = date,
                    isToday = date.isSameDay(today),
                    isSelected = when (selectedMode) {
                        PersianDatePickerMode.TIME_RANGE -> {
                            (dateRange.startDate?.isSameDay(date) == true) || 
                            (dateRange.endDate?.isSameDay(date) == true) ||
                            isDateBetween(date, dateRange.startDate, dateRange.endDate)
                        }
                        PersianDatePickerMode.MULTIPLE_DAYS -> selectedDates.any { it.isSameDay(date) }
                    },
                    isRangeStart = selectedMode == PersianDatePickerMode.TIME_RANGE && dateRange.startDate?.isSameDay(date) == true,
                    isRangeEnd = selectedMode == PersianDatePickerMode.TIME_RANGE && dateRange.endDate?.isSameDay(date) == true,
                    isInRange = selectedMode == PersianDatePickerMode.TIME_RANGE &&
                            isDateBetween(date, dateRange.startDate, dateRange.endDate),
                    onDateSelected = { onDateSelected(date) }
                )
            }
        }
    }
}

@Composable
private fun PersianCalendarDay(
    date: PersianDate,
    isToday: Boolean,
    isSelected: Boolean,
    isRangeStart: Boolean,
    isRangeEnd: Boolean,
    isInRange: Boolean,
    onDateSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedColor = Color(0xFF2196F3)
    
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onDateSelected() },
        contentAlignment = Alignment.Center
    ) {
        // Range background - continuous highlight extending beyond circle
        if (isInRange || isRangeStart || isRangeEnd) {
            Box(
                modifier = Modifier
                    .width(
                        when {
                            isRangeStart && isRangeEnd -> 32.dp
                            isRangeStart -> 50.dp
                            isRangeEnd -> 50.dp
                            else -> 44.dp
                        }
                    )
                    .height(32.dp)
                    .background(
                        color = selectedColor.copy(alpha = 0.2f),
                        shape = when {
                            isRangeStart && isRangeEnd -> RoundedCornerShape(16.dp)
                            isRangeStart -> RoundedCornerShape(
                                topStart = 16.dp,
                                bottomStart = 16.dp,
                                topEnd = 0.dp,
                                bottomEnd = 0.dp
                            )
                            isRangeEnd -> RoundedCornerShape(
                                topStart = 0.dp,
                                bottomStart = 0.dp,
                                topEnd = 16.dp,
                                bottomEnd = 16.dp
                            )
                            isInRange -> RoundedCornerShape(0.dp)
                            else -> CircleShape
                        }
                    )
            )
        }

        // Oval background for range start/end dates
        if (isRangeStart || isRangeEnd) {
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(28.dp)
                    .background(
                        color = selectedColor,
                        shape = RoundedCornerShape(14.dp)
                    )
            )
        }

        // Individual date circle for selected single dates
        if (isSelected && !isRangeStart && !isRangeEnd && !isInRange) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = selectedColor,
                        shape = CircleShape
                    )
            )
        }

        // Today indicator border
        if (isToday && !isSelected && !isRangeStart && !isRangeEnd) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(
                        width = 1.dp,
                        color = selectedColor.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
            )
        }

        // Date text
        Text(
            text = PersianDate.toPersianNumber(date.day),
            fontSize = 16.sp,
            fontWeight = if (isSelected || isRangeStart || isRangeEnd) FontWeight.Bold else FontWeight.Normal,
            color = when {
                isSelected || isRangeStart || isRangeEnd -> Color.White
                isToday -> selectedColor
                else -> Color(0xFF333333)
            },
            fontFamily = PersianFonts.Regular
        )
    }
}

@Composable
private fun PersianDatePickerActionButtons(
    onCancel: () -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Cancel Button
        TextButton(
            onClick = onCancel,
            modifier = Modifier.padding(end = 14.dp)
        ) {
            Text(
                text = "لغو",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF666666),
                fontFamily = PersianFonts.Regular
            )
        }

        // Done Button
        Button(
            onClick = onDone,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3)
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .width(120.dp)
                .height(48.dp)
        ) {
            Text(
                text = "تأیید",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                fontFamily = PersianFonts.Regular
            )
        }
    }
} 