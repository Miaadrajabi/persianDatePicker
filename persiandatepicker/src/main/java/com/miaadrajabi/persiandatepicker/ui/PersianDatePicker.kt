package com.miaadrajabi.persiandatepicker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material.ripple.rememberRipple
import com.miaadrajabi.persiandatepicker.ui.theme.PersianFonts
import com.miaadrajabi.persiandatepicker.utils.PersianCalendar
import com.miaadrajabi.persiandatepicker.utils.PersianDate
import java.util.Date

enum class SelectionMode {
    SINGLE,
    MULTIPLE,
    RANGE
}

enum class PresentationStyle {
    DIALOG,
    BOTTOM_SHEET
}

enum class DialogGravity {
    TOP,
    CENTER,
    BOTTOM
}

sealed class SelectionResult {
    data class Single(val date: PersianDate) : SelectionResult()
    data class Multiple(val dates: List<PersianDate>) : SelectionResult()
    data class Range(val range: PersianDateRange) : SelectionResult()
}

data class PersianDatePickerStyle(
    // Core colors
    val containerBackgroundColor: Color = Color(0xFFF5F5F5),
    val scrimColor: Color = Color(0x52000000),
    val cardContainerColor: Color = Color.White,
    val primaryColor: Color = Color(0xFF2196F3),
    val titleTextColor: Color = Color(0xFF333333),
    val dialogTitleTextColor: Color? = null,
    val bottomSheetTitleTextColor: Color? = null,
    val bodyTextColor: Color = Color(0xFF333333),
    val subtitleTextColor: Color = Color(0xFF666666),
    // Month/year center text colors
    val monthTitleTextColor: Color = Color(0xFF333333),
    val yearCenterTextColor: Color = Color(0xFF666666),
    val weekdayColor: Color = Color(0xFF2196F3),
    val todayBorderColor: Color = Color(0x802196F3),
    val inRangeColor: Color = Color(0x4D2196F3), // 30% alpha
    val selectedDayColor: Color = Color(0xFF2196F3),
    // Day text colors
    val dayDefaultTextColor: Color = Color(0xFF333333),
    val dayTodayTextColor: Color = Color(0xFF2196F3),
    // Nav and close
    val navButtonBackground: Color = Color(0x1A2196F3),
    val navButtonIconColor: Color = Color(0xFF2196F3),
    val navPrevIconColor: Color? = null,
    val navNextIconColor: Color? = null,
    val closeButtonBackground: Color = Color(0xFFF5F5F5),
    val closeButtonIconColor: Color = Color(0xFF666666),
    // Buttons
    val confirmButtonBackground: Color = Color(0xFF2196F3),
    val confirmButtonTextColor: Color = Color.White,
    val confirmButtonDisabledBackground: Color = Color(0xFFBDBDBD),
    val confirmButtonDisabledTextColor: Color = Color(0xFF757575),
    val cancelButtonTextColor: Color = Color(0xFF666666),
    // Today button
    val todayButtonBackground: Color = Color(0xFFE0E0E0),
    val todayButtonTextColor: Color = Color(0xFF333333),
    val todayButtonCornerRadius: Dp = 12.dp,
    // Ripples
    val dayRippleColor: Color = Color(0x402196F3),
    val buttonRippleColor: Color = Color(0x402196F3),
    val containerRippleColor: Color = Color(0x14000000),
    val cardRippleColor: Color = Color(0x14000000),
    // Sizes
    val cardCornerRadius: Dp = 20.dp,
    val confirmButtonCornerRadius: Dp = 12.dp,
    val dayCellSize: Dp = 40.dp,
    val inRangeCornerRadius: Dp = 6.dp,
    val navButtonSize: Dp = 36.dp,
    val closeButtonSize: Dp = 32.dp,
    val buttonHeight: Dp = 48.dp,
    val gridHeight: Dp = 240.dp,
    // Typography
    val titleFontSize: TextUnit = 20.sp,
    val monthFontSize: TextUnit = 18.sp,
    val yearFontSize: TextUnit = 14.sp,
    val weekdayFontSize: TextUnit = 14.sp,
    val dayFontSize: TextUnit = 14.sp,
    val buttonTextSize: TextUnit = 16.sp,
    // Selection summary footer
    val selectionFooterBackgroundColor: Color = Color(0xFF2196F3).copy(alpha = 0.1f),
    val selectionFooterTextColor: Color = Color(0xFF333333),
    val selectionFooterCornerRadius: Dp = 12.dp,
    // Year picker styles
    val yearPickerBackgroundColor: Color = Color.Transparent,
    val yearPickerTextColor: Color = Color(0xFF333333),
    val yearPickerSelectedTextColor: Color = Color.White,
    val yearPickerItemBackgroundColor: Color = Color(0xFFE0E0E0),
    val yearPickerSelectedBackgroundColor: Color = Color(0xFF2196F3),
    val yearPickerCornerRadius: Dp = 12.dp,
    val yearPickerItemHeight: Dp = 48.dp,
    val yearPickerColumns: Int = 4,
    val yearPickerRangeYears: Int = 20,
)

data class PersianDatePickerConfig(
    val selectionMode: SelectionMode,
    val presentationStyle: PresentationStyle,
    val dialogGravity: DialogGravity,
    val width: Dp?,
    val height: Dp?,
    val widthFraction: Float?,
    val heightFraction: Float?,
    val dialogMarginHorizontal: Dp,
    val dialogMarginVertical: Dp,
    val contentPaddingHorizontal: Dp,
    val contentPaddingVertical: Dp,
    val dismissOnBackPress: Boolean,
    val dismissOnClickOutside: Boolean,
    val initialDateRange: PersianDateRange?,
    val initialSelectedDates: Set<PersianDate>,
    val style: PersianDatePickerStyle,
    val onContainerClick: (() -> Unit)?,
    val onCardClick: (() -> Unit)?,
    val showTodayButton: Boolean,
    val todayButtonText: String,
    val onTodayClick: ((PersianDate) -> Unit)?,
    val singleTitleText: String?,
    val multipleTitleText: String?,
    val rangeTitleText: String?,
    val showSelectionSummaryFooter: Boolean,
    val enableYearPicker: Boolean,
)

class PersianDatePickerBuilder {
    private var selectionMode: SelectionMode = SelectionMode.RANGE
    private var presentationStyle: PresentationStyle = PresentationStyle.DIALOG
    private var dialogGravity: DialogGravity = DialogGravity.CENTER
    private var width: Dp? = null
    private var height: Dp? = null
    private var widthFraction: Float? = null
    private var heightFraction: Float? = null
    private var dialogMarginHorizontal: Dp = 16.dp
    private var dialogMarginVertical: Dp = 16.dp
    private var contentPaddingHorizontal: Dp = 24.dp
    private var contentPaddingVertical: Dp = 24.dp
    private var dismissOnBackPress: Boolean = true
    private var dismissOnClickOutside: Boolean = true
    private var initialDateRange: PersianDateRange? = null
    private var initialSelectedDates: Set<PersianDate> = emptySet()
    private var style: PersianDatePickerStyle = PersianDatePickerStyle()
    private var onContainerClick: (() -> Unit)? = null
    private var onCardClick: (() -> Unit)? = null
    private var showTodayButton: Boolean = false
    private var todayButtonText: String = "امروز"
    private var onTodayClick: ((PersianDate) -> Unit)? = null
    private var singleTitleText: String? = null
    private var multipleTitleText: String? = null
    private var rangeTitleText: String? = null
    private var showSelectionSummaryFooter: Boolean = true
    private var enableYearPicker: Boolean = true

    fun selectionMode(mode: SelectionMode) = apply { this.selectionMode = mode }
    fun presentation(style: PresentationStyle) = apply { this.presentationStyle = style }
    fun gravity(gravity: DialogGravity) = apply { this.dialogGravity = gravity }
    fun size(width: Dp?, height: Dp?) = apply { this.width = width; this.height = height }
    fun sizeFraction(widthFraction: Float?, heightFraction: Float?) = apply {
        this.widthFraction = widthFraction; this.heightFraction = heightFraction
    }
    fun dialogMargin(horizontal: Dp, vertical: Dp) = apply {
        this.dialogMarginHorizontal = horizontal; this.dialogMarginVertical = vertical
    }
    fun contentPadding(horizontal: Dp, vertical: Dp) = apply {
        this.contentPaddingHorizontal = horizontal; this.contentPaddingVertical = vertical
    }
    fun dismissOnBackPress(enabled: Boolean) = apply { this.dismissOnBackPress = enabled }
    fun dismissOnClickOutside(enabled: Boolean) = apply { this.dismissOnClickOutside = enabled }
    fun initialRange(range: PersianDateRange?) = apply { this.initialDateRange = range }
    fun initialSelectedDates(dates: Set<PersianDate>) = apply { this.initialSelectedDates = dates }
    fun style(style: PersianDatePickerStyle) = apply { this.style = style }
    fun onContainerClick(listener: (() -> Unit)?) = apply { this.onContainerClick = listener }
    fun onCardClick(listener: (() -> Unit)?) = apply { this.onCardClick = listener }
    fun showTodayButton(show: Boolean) = apply { this.showTodayButton = show }
    fun todayButtonText(text: String) = apply { this.todayButtonText = text }
    fun onTodayClick(listener: ((PersianDate) -> Unit)?) = apply { this.onTodayClick = listener }
    fun singleTitle(text: String?) = apply { this.singleTitleText = text }
    fun multipleTitle(text: String?) = apply { this.multipleTitleText = text }
    fun rangeTitle(text: String?) = apply { this.rangeTitleText = text }
    fun showSelectionSummaryFooter(show: Boolean) = apply { this.showSelectionSummaryFooter = show }
    fun enableYearPicker(enable: Boolean) = apply { this.enableYearPicker = enable }

    fun build(): PersianDatePickerConfig {
        return PersianDatePickerConfig(
            selectionMode = selectionMode,
            presentationStyle = presentationStyle,
            dialogGravity = dialogGravity,
            width = width,
            height = height,
            widthFraction = widthFraction,
            heightFraction = heightFraction,
            dialogMarginHorizontal = dialogMarginHorizontal,
            dialogMarginVertical = dialogMarginVertical,
            contentPaddingHorizontal = contentPaddingHorizontal,
            contentPaddingVertical = contentPaddingVertical,
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside,
            initialDateRange = initialDateRange,
            initialSelectedDates = initialSelectedDates,
            style = style,
            onContainerClick = onContainerClick,
            onCardClick = onCardClick,
            showTodayButton = showTodayButton,
            todayButtonText = todayButtonText,
            onTodayClick = onTodayClick,
            singleTitleText = singleTitleText,
            multipleTitleText = multipleTitleText,
            rangeTitleText = rangeTitleText,
            showSelectionSummaryFooter = showSelectionSummaryFooter,
            enableYearPicker = enableYearPicker,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersianDatePicker(
    isVisible: Boolean,
    config: PersianDatePickerConfig,
    onDismiss: () -> Unit,
    onResult: (SelectionResult) -> Unit,
) {
    if (!isVisible) return

    when (config.presentationStyle) {
        PresentationStyle.DIALOG -> {
            Dialog(
                onDismissRequest = onDismiss,
                properties = DialogProperties(
                    dismissOnBackPress = config.dismissOnBackPress,
                    dismissOnClickOutside = config.dismissOnClickOutside,
                    usePlatformDefaultWidth = false
                )
            ) {
                val alignment = when (config.dialogGravity) {
                    DialogGravity.TOP -> Alignment.TopCenter
                    DialogGravity.CENTER -> Alignment.Center
                    DialogGravity.BOTTOM -> Alignment.BottomCenter
                }

                val containerClickableModifier = if (config.onContainerClick != null) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(color = config.style.containerRippleColor)
                    ) { config.onContainerClick?.invoke() }
                } else Modifier

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(config.style.containerBackgroundColor)
                        .padding(horizontal = config.dialogMarginHorizontal, vertical = config.dialogMarginVertical)
                        .then(containerClickableModifier),
                    contentAlignment = alignment
                ) {
                    PersianDatePickerCard(
                        config = config,
                        onDismiss = onDismiss,
                        onResult = onResult
                    )
                }
            }
        }

        PresentationStyle.BOTTOM_SHEET -> {
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ModalBottomSheet(
                onDismissRequest = onDismiss,
                sheetState = sheetState,
                containerColor = config.style.containerBackgroundColor,
                contentColor = config.style.bodyTextColor,
                scrimColor = config.style.scrimColor,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                dragHandle = {
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .background(color = Color(0xFFCCCCCC), shape = RoundedCornerShape(2.dp))
                    )
                }
            ) {
                val containerClickableModifier = if (config.onContainerClick != null) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(color = config.style.containerRippleColor)
                    ) { config.onContainerClick?.invoke() }
                } else Modifier

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = config.dialogMarginHorizontal, vertical = config.dialogMarginVertical)
                        .then(containerClickableModifier)
                ) {
                    PersianDatePickerCard(
                        config = config,
                        onDismiss = onDismiss,
                        onResult = onResult
                    )
                }
            }
        }
    }
}

@Composable
private fun PersianDatePickerCard(
    config: PersianDatePickerConfig,
    onDismiss: () -> Unit,
    onResult: (SelectionResult) -> Unit,
) {
    var currentPersianCalendar by remember { mutableStateOf(PersianCalendar()) }
    var dateRange by remember { mutableStateOf(config.initialDateRange ?: PersianDateRange()) }
    var selectedDates by remember { mutableStateOf(config.initialSelectedDates.toSet()) }
    var singleDate by remember { mutableStateOf(config.initialSelectedDates.firstOrNull()) }
    val today = PersianDate.toPersianDate(Date())
    var showYearPicker by remember { mutableStateOf(false) }

    var cardModifier = Modifier
        .then(
            when {
                config.widthFraction != null -> Modifier.fillMaxWidth(config.widthFraction)
                config.width != null -> Modifier.width(config.width)
                else -> Modifier.fillMaxWidth()
            }
        )
        .then(
            when {
                config.heightFraction != null -> Modifier.fillMaxHeight(config.heightFraction)
                config.height != null -> Modifier.height(config.height)
                else -> Modifier
            }
        )

    val cardClickableModifier = if (config.onCardClick != null) {
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(color = config.style.cardRippleColor)
        ) { config.onCardClick?.invoke() }
    } else Modifier

    val isBottomSheet = config.presentationStyle == PresentationStyle.BOTTOM_SHEET
    val cardColors = if (isBottomSheet) {
        CardDefaults.cardColors(containerColor = Color.Transparent)
    } else {
        CardDefaults.cardColors(containerColor = config.style.cardContainerColor)
    }
    val cardElevation = if (isBottomSheet) {
        CardDefaults.cardElevation(defaultElevation = 0.dp)
    } else {
        CardDefaults.cardElevation(defaultElevation = 8.dp)
    }
    val cardShape = if (isBottomSheet) {
        RoundedCornerShape(0.dp)
    } else {
        RoundedCornerShape(config.style.cardCornerRadius)
    }

    Card(
        modifier = cardModifier.then(cardClickableModifier),
        colors = cardColors,
        elevation = cardElevation,
        shape = cardShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = config.contentPaddingHorizontal, vertical = config.contentPaddingVertical)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val defaultTitleSingle = "انتخاب تاریخ"
                val defaultTitleMultiple = "انتخاب چند تاریخ"
                val defaultTitleRange = "انتخاب بازه تاریخ"
                val headerText = when (config.selectionMode) {
                    SelectionMode.SINGLE -> config.singleTitleText ?: defaultTitleSingle
                    SelectionMode.MULTIPLE -> config.multipleTitleText ?: defaultTitleMultiple
                    SelectionMode.RANGE -> config.rangeTitleText ?: defaultTitleRange
                }
                val titleColor = when (config.presentationStyle) {
                    PresentationStyle.DIALOG -> config.style.dialogTitleTextColor ?: config.style.titleTextColor
                    PresentationStyle.BOTTOM_SHEET -> config.style.bottomSheetTitleTextColor ?: config.style.titleTextColor
                }
                Text(
                    text = headerText,
                    fontSize = config.style.titleFontSize,
                    fontWeight = FontWeight.Bold,
                    color = titleColor,
                    fontFamily = PersianFonts.Bold,
                    style = TextStyle(textDirection = TextDirection.ContentOrRtl)
                )

                Box(
                    modifier = Modifier
                        .size(config.style.closeButtonSize)
                        .background(color = config.style.closeButtonBackground, shape = CircleShape)
                        .clip(CircleShape)
                        .clickable {
                            if (showYearPicker) {
                                showYearPicker = false
                            } else {
                                onDismiss()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    val closeIcon = if (showYearPicker) "‹" else "×"
                    Text(
                        text = closeIcon,
                        fontSize = config.style.titleFontSize,
                        color = config.style.closeButtonIconColor,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(textDirection = TextDirection.ContentOrRtl)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Month navigation / Year picker
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (showYearPicker) Arrangement.Center else Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!showYearPicker) {
                    Box(
                        modifier = Modifier
                            .size(config.style.navButtonSize)
                            .background(color = config.style.navButtonBackground, shape = CircleShape)
                            .clip(CircleShape)
                            .clickable {
                                currentPersianCalendar.addMonths(-1)
                                currentPersianCalendar = PersianCalendar(
                                    currentPersianCalendar.getYear(),
                                    currentPersianCalendar.getMonth(),
                                    1
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        val prevColor = config.style.navPrevIconColor ?: config.style.navButtonIconColor
                        Text(text = "‹", fontSize = config.style.monthFontSize, color = prevColor, fontWeight = FontWeight.Bold)
                    }

                    val persianDate = currentPersianCalendar.getPersianDate()
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = persianDate.getMonthName(),
                            fontSize = config.style.monthFontSize,
                            fontWeight = FontWeight.Bold,
                            color = config.style.monthTitleTextColor,
                            fontFamily = PersianFonts.Bold,
                            style = TextStyle(textDirection = TextDirection.ContentOrRtl)
                        )
                        Text(
                            text = PersianDate.toPersianNumber(persianDate.year),
                            fontSize = config.style.yearFontSize,
                            fontWeight = FontWeight.Medium,
                            color = config.style.yearCenterTextColor,
                            fontFamily = PersianFonts.Regular,
                            style = TextStyle(textDirection = TextDirection.ContentOrRtl),
                            modifier = Modifier
                                .clip(RoundedCornerShape(config.style.yearPickerCornerRadius))
                                .clickable(enabled = config.enableYearPicker) { showYearPicker = true }
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(config.style.navButtonSize)
                            .background(color = config.style.navButtonBackground, shape = CircleShape)
                            .clip(CircleShape)
                            .clickable {
                                currentPersianCalendar.addMonths(1)
                                currentPersianCalendar = PersianCalendar(
                                    currentPersianCalendar.getYear(),
                                    currentPersianCalendar.getMonth(),
                                    1
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        val nextColor = config.style.navNextIconColor ?: config.style.navButtonIconColor
                        Text(text = "›", fontSize = config.style.monthFontSize, color = nextColor, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (showYearPicker && config.enableYearPicker) {
                Spacer(modifier = Modifier.height(12.dp))
                YearPickerGrid(
                    currentYear = currentPersianCalendar.getYear(),
                    columns = config.style.yearPickerColumns,
                    rangeYears = config.style.yearPickerRangeYears,
                    itemHeight = config.style.yearPickerItemHeight,
                    background = config.style.yearPickerBackgroundColor,
                    itemBackground = config.style.yearPickerItemBackgroundColor,
                    selectedBackground = config.style.yearPickerSelectedBackgroundColor,
                    textColor = config.style.yearPickerTextColor,
                    selectedTextColor = config.style.yearPickerSelectedTextColor,
                    corner = config.style.yearPickerCornerRadius,
                    onYearSelected = { year ->
                        currentPersianCalendar = PersianCalendar(year, currentPersianCalendar.getMonth(), 1)
                        showYearPicker = false
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Week headers (hide when year picker is visible)
            if (!showYearPicker) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    PersianDate.PERSIAN_WEEKDAYS_SHORT.forEach { day ->
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            Text(
                                text = day,
                                fontSize = config.style.weekdayFontSize,
                                fontWeight = FontWeight.Medium,
                                color = config.style.weekdayColor,
                                fontFamily = PersianFonts.Regular,
                                textAlign = TextAlign.Center,
                                style = TextStyle(textDirection = TextDirection.ContentOrRtl)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!showYearPicker) {
                val firstDayOfWeek = currentPersianCalendar.getFirstDayOfWeekInMonth()
                val daysInMonth = currentPersianCalendar.getDaysInCurrentMonth()

                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    modifier = Modifier.height(config.style.gridHeight),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(firstDayOfWeek) {
                        Spacer(modifier = Modifier.size(36.dp))
                    }

                    items(daysInMonth) { dayIndex ->
                        val date = PersianDate(
                            currentPersianCalendar.getYear(),
                            currentPersianCalendar.getMonth(),
                            dayIndex + 1
                        )

                        val isDateBetween = { d: PersianDate, start: PersianDate?, end: PersianDate? ->
                            start != null && end != null && d.isAfter(start) && d.isBefore(end)
                        }

                        val isSelected = when (config.selectionMode) {
                            SelectionMode.SINGLE -> singleDate?.isSameDay(date) == true
                            SelectionMode.MULTIPLE -> selectedDates.any { it.isSameDay(date) }
                            SelectionMode.RANGE -> (dateRange.startDate?.isSameDay(date) == true) ||
                                (dateRange.endDate?.isSameDay(date) == true) ||
                                isDateBetween(date, dateRange.startDate, dateRange.endDate)
                        }

                        val isRangeStart = config.selectionMode == SelectionMode.RANGE && dateRange.startDate?.isSameDay(date) == true
                        val isRangeEnd = config.selectionMode == SelectionMode.RANGE && dateRange.endDate?.isSameDay(date) == true
                        val isInRange = config.selectionMode == SelectionMode.RANGE && isDateBetween(date, dateRange.startDate, dateRange.endDate)

                        PersianDayCell(
                            date = date,
                            isToday = date.isSameDay(today),
                            isSelected = isSelected,
                            isRangeStart = isRangeStart,
                            isRangeEnd = isRangeEnd,
                            isInRange = isInRange,
                            onClick = {
                                when (config.selectionMode) {
                                    SelectionMode.SINGLE -> {
                                        singleDate = date
                                    }
                                    SelectionMode.MULTIPLE -> {
                                        selectedDates = if (selectedDates.any { it.isSameDay(date) }) {
                                            selectedDates.filter { !it.isSameDay(date) }.toSet()
                                        } else {
                                            selectedDates + date
                                        }
                                    }
                                    SelectionMode.RANGE -> {
                                        dateRange = when {
                                            dateRange.startDate == null -> PersianDateRange(startDate = date)
                                            dateRange.endDate == null -> {
                                                if (date.isBefore(dateRange.startDate!!)) {
                                                    PersianDateRange(startDate = date, endDate = dateRange.startDate)
                                                } else {
                                                    PersianDateRange(startDate = dateRange.startDate, endDate = date)
                                                }
                                            }
                                            else -> PersianDateRange(startDate = date)
                                        }
                                    }
                                }
                            },
                            style = config.style
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Selection summary footer (hide when year picker is visible)
            if (config.showSelectionSummaryFooter && !showYearPicker) {
                val footerText = when (config.selectionMode) {
                    SelectionMode.SINGLE -> singleDate?.getFormattedDate() ?: ""
                    SelectionMode.MULTIPLE -> if (selectedDates.isNotEmpty()) {
                        selectedDates
                            .sortedWith(compareBy({ it.getYear() }, { it.getMonth() }, { it.getDay() }))
                            .joinToString(separator = "، ") { it.getShortFormattedDate() }
                    } else ""
                    SelectionMode.RANGE -> when {
                        dateRange.startDate != null && dateRange.endDate != null ->
                            "از: ${dateRange.startDate!!.getFormattedDate()}\nتا: ${dateRange.endDate!!.getFormattedDate()}"
                        dateRange.startDate != null -> "از: ${dateRange.startDate!!.getFormattedDate()}"
                        else -> ""
                    }
                }
                if (footerText.isNotEmpty()) {
                    SelectionFooter(
                        text = footerText,
                        background = config.style.selectionFooterBackgroundColor,
                        textColor = config.style.selectionFooterTextColor,
                        corner = config.style.selectionFooterCornerRadius
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(config.style.buttonHeight),
                    colors = ButtonDefaults.textButtonColors(contentColor = config.style.cancelButtonTextColor)
                ) {
                    Text(
                        text = "لغو",
                        fontSize = config.style.buttonTextSize,
                        fontWeight = FontWeight.Medium,
                        fontFamily = PersianFonts.Regular,
                        style = TextStyle(textDirection = TextDirection.ContentOrRtl)
                    )
                }

                if (config.showTodayButton) {
                    Button(
                        onClick = {
                            val todayLocal = PersianDate.toPersianDate(Date())
                            // Default behavior per mode
                            when (config.selectionMode) {
                                SelectionMode.SINGLE -> {
                                    singleDate = todayLocal
                                }
                                SelectionMode.MULTIPLE -> {
                                    selectedDates = if (selectedDates.any { it.isSameDay(todayLocal) }) {
                                        selectedDates.filter { !it.isSameDay(todayLocal) }.toSet()
                                    } else {
                                        selectedDates + todayLocal
                                    }
                                }
                                SelectionMode.RANGE -> {
                                    dateRange = when {
                                        dateRange.startDate == null -> PersianDateRange(startDate = todayLocal)
                                        dateRange.endDate == null -> {
                                            if (todayLocal.isBefore(dateRange.startDate!!)) {
                                                PersianDateRange(startDate = todayLocal, endDate = dateRange.startDate)
                                            } else {
                                                PersianDateRange(startDate = dateRange.startDate, endDate = todayLocal)
                                            }
                                        }
                                        else -> PersianDateRange(startDate = todayLocal)
                                    }
                                }
                            }
                            // Navigate calendar to today
                            currentPersianCalendar = PersianCalendar(
                                todayLocal.getYear(), todayLocal.getMonth(), 1
                            )
                            // External listener
                            config.onTodayClick?.invoke(todayLocal)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(config.style.buttonHeight),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = config.style.todayButtonBackground,
                            contentColor = config.style.todayButtonTextColor
                        ),
                        shape = RoundedCornerShape(config.style.todayButtonCornerRadius)
                    ) {
                        Text(
                            text = config.todayButtonText,
                            fontSize = config.style.buttonTextSize,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = PersianFonts.Regular,
                            color = config.style.todayButtonTextColor,
                            style = TextStyle(textDirection = TextDirection.ContentOrRtl)
                        )
                    }
                }

                val confirmEnabled = when (config.selectionMode) {
                    SelectionMode.SINGLE -> singleDate != null
                    SelectionMode.MULTIPLE -> selectedDates.isNotEmpty()
                    SelectionMode.RANGE -> dateRange.startDate != null && dateRange.endDate != null
                }

                Button(
                    onClick = {
                        when (config.selectionMode) {
                            SelectionMode.SINGLE -> singleDate?.let { onResult(SelectionResult.Single(it)) }
                            SelectionMode.MULTIPLE -> onResult(SelectionResult.Multiple(selectedDates.toList()))
                            SelectionMode.RANGE -> onResult(SelectionResult.Range(dateRange))
                        }
                        onDismiss()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(config.style.buttonHeight),
                    enabled = confirmEnabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = config.style.confirmButtonBackground,
                        disabledContainerColor = config.style.confirmButtonDisabledBackground,
                        contentColor = config.style.confirmButtonTextColor,
                        disabledContentColor = config.style.confirmButtonDisabledTextColor
                    ),
                    shape = RoundedCornerShape(config.style.confirmButtonCornerRadius)
                ) {
                    Text(
                        text = "تأیید",
                        fontSize = config.style.buttonTextSize,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = PersianFonts.Regular,
                        color = if (confirmEnabled) config.style.confirmButtonTextColor else config.style.confirmButtonDisabledTextColor,
                        style = TextStyle(textDirection = TextDirection.ContentOrRtl)
                    )
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2196F3).copy(alpha = 0.1f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                fontFamily = PersianFonts.Bold,
                textAlign = TextAlign.Center,
                style = TextStyle(textDirection = TextDirection.ContentOrRtl)
            )
        }
    }
}

@Composable
private fun SelectionFooter(
    text: String,
    background: Color,
    textColor: Color,
    corner: Dp,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = background),
        shape = RoundedCornerShape(corner)
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                fontFamily = PersianFonts.Bold,
                textAlign = TextAlign.Center,
                style = TextStyle(textDirection = TextDirection.ContentOrRtl)
            )
        }
    }
}

@Composable
private fun YearPickerGrid(
    currentYear: Int,
    columns: Int,
    rangeYears: Int,
    itemHeight: Dp,
    background: Color,
    itemBackground: Color,
    selectedBackground: Color,
    textColor: Color,
    selectedTextColor: Color,
    corner: Dp,
    onYearSelected: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(background)
            .padding(4.dp)
    ) {
        val startYear = currentYear - rangeYears
        val endYear = currentYear + rangeYears
        val years = (startYear..endYear).toList()
        val rows = (years.size + columns - 1) / columns

        for (row in 0 until rows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                for (col in 0 until columns) {
                    val index = row * columns + col
                    if (index < years.size) {
                        val y = years[index]
                        val isSelected = y == currentYear
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(itemHeight)
                                .clip(RoundedCornerShape(corner))
                                .background(if (isSelected) selectedBackground else itemBackground)
                                .clickable { onYearSelected(y) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = PersianDate.toPersianNumber(y),
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) selectedTextColor else textColor,
                                fontFamily = PersianFonts.Regular
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Composable
private fun PersianDayCell(
    date: PersianDate,
    isToday: Boolean,
    isSelected: Boolean,
    isRangeStart: Boolean,
    isRangeEnd: Boolean,
    isInRange: Boolean,
    onClick: () -> Unit,
    style: PersianDatePickerStyle,
) {
    val selectedColor = style.selectedDayColor
    val todayBg = style.primaryColor.copy(alpha = 0.1f)

    Box(
        modifier = Modifier
            .size(style.dayCellSize)
            .background(
                color = when {
                    isRangeStart || isRangeEnd -> selectedColor
                    isInRange -> style.inRangeColor
                    isSelected -> selectedColor
                    isToday -> todayBg
                    else -> Color.Transparent
                },
                shape = when {
                    isRangeStart || isRangeEnd -> CircleShape
                    isInRange -> RoundedCornerShape(style.inRangeCornerRadius)
                    else -> CircleShape
                }
            )
            .run {
                if (isToday && !isRangeStart && !isRangeEnd && !isSelected) {
                    border(width = 1.dp, color = style.todayBorderColor, shape = CircleShape)
                } else this
            }
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = style.dayRippleColor)
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = PersianDate.toPersianNumber(date.day),
            fontSize = style.dayFontSize,
            fontWeight = if (isSelected || isRangeStart || isRangeEnd) FontWeight.Bold else FontWeight.Normal,
            color = when {
                isSelected || isRangeStart || isRangeEnd -> Color.White
                isToday -> style.dayTodayTextColor
                else -> style.dayDefaultTextColor
            },
            fontFamily = PersianFonts.Regular,
            style = TextStyle(textDirection = TextDirection.ContentOrRtl)
        )
    }
}


