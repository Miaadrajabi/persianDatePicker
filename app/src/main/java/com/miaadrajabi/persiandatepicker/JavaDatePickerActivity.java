package com.miaadrajabi.persiandatepicker;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Activity;
import android.widget.ScrollView;

import androidx.core.content.ContextCompat;

import com.miaadrajabi.persiandatepicker.utils.PersianCalendarJava;
import com.miaadrajabi.persiandatepicker.utils.PersianDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JavaDatePickerActivity extends Activity {
    
    private TextView tvTestResults;
    private LinearLayout layoutSelectedRange;
    private TextView tvSelectedRange;
    private PersianDate startDate;
    private PersianDate endDate;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_date_picker);
        
        initViews();
        setupClickListeners();
//        runLeapYearTest(); 
    }
    
    private void runLeapYearTest() {
        StringBuilder testResults = new StringBuilder();
        testResults.append("=== الگوریتم بر اساس طول دقیق سال شمسی ===\n");
        testResults.append("طول سال: ۳۶۵.۲۴۲۱۸۷۵ روز\n");
        testResults.append("چرخه: ۱۲۸ سال - ۳۱ سال کبیسه\n\n");
        
        // Leap years in 128-year cycle
        testResults.append("=== چرخه ۱۲۸ ساله ===\n");
        testResults.append("سال‌های کبیسه در هر چرخه:\n");
        testResults.append("۱، ۵، ۹، ۱۳، ۱۷، ۲۱، ۲۶، ۳۰، ۳۴، ۳۸، ۴۲، ۴۶، ۵۰، ۵۴، ۵۹، ۶۳\n");
        testResults.append("۶۷، ۷۱، ۷۵، ۷۹، ۸۳، ۸۷، ۹۲، ۹۶، ۱۰۰، ۱۰۴، ۱۰۸، ۱۱۲، ۱۱۶، ۱۲۰، ۱۲۵\n\n");
        
        // Recent years
        testResults.append("=== سال‌های اخیر ===\n");
        int[] recentYears = {1395, 1396, 1397, 1398, 1399, 1400, 1401, 1402, 1403, 1404, 1405, 1406, 1407, 1408};
        
        for (int year : recentYears) {
            boolean isLeap = PersianDate.isLeapYear(year);
            testResults.append(String.format("سال %s: %s\n", 
                PersianDate.toPersianNumber(year),
                isLeap ? "کبیسه ✓" : "عادی"
            ));
        }
        
        // Today's date
        testResults.append("\n=== تاریخ امروز ===\n");
        PersianDate today = PersianDate.toPersianDate(new Date());
        testResults.append("تاریخ: ").append(today.getFormattedDate()).append("\n");
        testResults.append("عددی: ").append(today.getNumericFormattedDate()).append("\n");
        testResults.append("سال ").append(PersianDate.toPersianNumber(today.getYear()))
                    .append(": ").append(PersianDate.isLeapYear(today.getYear()) ? "کبیسه" : "عادی").append("\n");
        
        // Position in 128-year cycle
        int cyclePosition = today.getYear() % 128;
        testResults.append("موقعیت در چرخه: ").append(PersianDate.toPersianNumber(cyclePosition)).append("\n\n");
        
        // Conversion accuracy tests
        testResults.append("=== تست دقت تبدیل ===\n");
        String[] testDates = {"1400/01/01", "1403/12/29", "1404/12/30", "1405/01/01"};
        
        for (String dateStr : testDates) {
            String[] parts = dateStr.split("/");
            int jy = Integer.parseInt(parts[0]);
            int jm = Integer.parseInt(parts[1]);
            int jd = Integer.parseInt(parts[2]);
            
            PersianDate persianDate = new PersianDate(jy, jm, jd);
            Date gregorianDate = PersianDate.fromPersianDate(persianDate);
            PersianDate backToJalali = PersianDate.toPersianDate(gregorianDate);
            
            boolean isAccurate = backToJalali.isSameDay(persianDate);
            
            testResults.append(String.format("%s: %s %s\n",
                PersianDate.toPersianNumber(dateStr),
                backToJalali.getFormattedDate(),
                isAccurate ? "✓" : "✗"
            ));
        }
        
        if (tvTestResults != null) {
            tvTestResults.setText(testResults.toString());
        }
    }
    
    private void initViews() {
        tvTestResults = findViewById(R.id.tv_test_results);
        layoutSelectedRange = findViewById(R.id.layout_selected_range);
        tvSelectedRange = findViewById(R.id.tv_selected_range);
        
        Button btnBack = findViewById(R.id.btn_back);
        Button btnShowDatePicker = findViewById(R.id.btn_show_date_picker);
        
        btnBack.setOnClickListener(v -> finish());
        btnShowDatePicker.setOnClickListener(v -> showDatePickerDialog());
    }
    
    private void setupClickListeners() {
        // Already handled in initViews()
    }
    
    private void updateSelectedRangeDisplay() {
        if (startDate != null || endDate != null) {
            layoutSelectedRange.setVisibility(View.VISIBLE);
            
            String rangeText;
            if (startDate != null && endDate != null) {
                rangeText = "بازه انتخاب شده:\n\n" +
                           "از: " + startDate.getFormattedDate() + "\n" +
                           "تا: " + endDate.getFormattedDate() + "\n\n" +
                           "فرمت عددی:\n" +
                           "از: " + startDate.getNumericFormattedDate() + "\n" +
                           "تا: " + endDate.getNumericFormattedDate();
            } else if (startDate != null) {
                rangeText = "تاریخ شروع:\n" + startDate.getFormattedDate() + "\n" + 
                           startDate.getNumericFormattedDate();
            } else {
                rangeText = "تاریخ انتخاب کنید";
            }
            
            tvSelectedRange.setText(rangeText);
        } else {
            layoutSelectedRange.setVisibility(View.GONE);
        }
    }
    
    private void showDatePickerDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_persian_date_picker);
        
        // Set dialog size and corner radius
        Window window = dialog.getWindow();
        if (window != null) {
            // Set corner radius for dialog window
            window.setBackgroundDrawableResource(R.drawable.dialog_background);
            
            android.view.WindowManager.LayoutParams params = window.getAttributes();
            params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.95);
            params.height = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
        
        // Initialize dialog components
        PersianDatePickerDialog datePickerDialog = new PersianDatePickerDialog(dialog, this);
        datePickerDialog.setOnDateRangeSelectedListener(new PersianDatePickerDialog.OnDateRangeSelectedListener() {
            @Override
            public void onDateRangeSelected(PersianDate start, PersianDate end) {
                startDate = start;
                endDate = end;
                updateSelectedRangeDisplay();
                dialog.dismiss();
            }
        });
        
        dialog.show();
    }
    
    /**
     * Persian Date Picker Dialog Implementation for Android Java
     * پیاده‌سازی دیالوگ انتخاب تاریخ فارسی برای جاوای اندروید
     */
    public static class PersianDatePickerDialog {
        private Dialog dialog;
        private JavaDatePickerActivity activity;
        private PersianCalendarJava currentCalendar;
        private PersianDate startDate;
        private PersianDate endDate;
        private PersianDate today;
        private OnDateRangeSelectedListener listener;
        private boolean showYearPicker = false;
        
        // UI Components
        private TextView tvHeader;
        private Button btnClose;
        private LinearLayout layoutMonthNavigation;
        private Button btnPrevMonth;
        private Button btnNextMonth;
        private TextView tvMonthName;
        private TextView tvYear;
        private LinearLayout layoutYearPicker;
        private ScrollView scrollYearPicker;
        private LinearLayout layoutYearGrid;
        private LinearLayout layoutWeekdaysHeader;
        private GridLayout gridCalendar;
        private LinearLayout cardSelectedRange;
        private TextView tvSelectedRange;
        private Button btnToday;
        private Button btnCancel;
        private Button btnConfirm;
        
        public interface OnDateRangeSelectedListener {
            void onDateRangeSelected(PersianDate start, PersianDate end);
        }
        
        public PersianDatePickerDialog(Dialog dialog, JavaDatePickerActivity activity) {
            this.dialog = dialog;
            this.activity = activity;
            
            // Get device current date
            java.util.Calendar deviceCalendar = java.util.Calendar.getInstance();
            System.out.println("=== Device Date Debug ===");
            System.out.println("Device Date: " + deviceCalendar.getTime());
            System.out.println("Device Year: " + deviceCalendar.get(java.util.Calendar.YEAR));
            System.out.println("Device Month: " + (deviceCalendar.get(java.util.Calendar.MONTH) + 1) + " (0-based)");
            System.out.println("Device Day: " + deviceCalendar.get(java.util.Calendar.DAY_OF_MONTH));
            
            // Convert device date to Jalali
            this.today = PersianDate.toPersianDate(deviceCalendar.getTime());
            
            System.out.println("=== Persian Date Debug ===");
            System.out.println("Persian Today: " + today.getFormattedDate());
            System.out.println("Persian Year: " + today.getYear() + ", Month: " + today.getMonth() + ", Day: " + today.getDay());
            
            // Initialize currentCalendar with today
            this.currentCalendar = new PersianCalendarJava(today.getYear(), today.getMonth(), today.getDay());
            
            // No default selection - user will select dates
            this.startDate = null;
            this.endDate = null;
            
            System.out.println("Current Calendar: " + currentCalendar.getPersianDate().getFormattedDate());
            System.out.println("Current Calendar Year: " + currentCalendar.getYear() + ", Month: " + currentCalendar.getMonth());
            System.out.println("No default selected date - user must select both start and end dates");
            
            // Algorithm tests
            System.out.println("=== Algorithm Test ===");
            
            // Test July 16, 2025
            java.util.Calendar testCal = java.util.Calendar.getInstance();
            testCal.set(2025, java.util.Calendar.JULY, 16);
            PersianDate testDate = PersianDate.toPersianDate(testCal.getTime());
            System.out.println("July 16, 2025 -> " + testDate.getFormattedDate());
            System.out.println("Expected: ۲۵ تیر ۱۴۰۴");
            
            // Test June 16, 2025
            testCal.set(2025, java.util.Calendar.JUNE, 16);
            PersianDate testDate2 = PersianDate.toPersianDate(testCal.getTime());
            System.out.println("June 16, 2025 -> " + testDate2.getFormattedDate());
            System.out.println("Expected: ۲۶ خرداد ۱۴۰۴");
            
            // Test today's date
            java.util.Calendar todayCal = java.util.Calendar.getInstance();
            PersianDate todayTest = PersianDate.toPersianDate(todayCal.getTime());
            System.out.println("Today -> " + todayTest.getFormattedDate());
            
            initViews();
            updateDisplay();
        }
        
        private void initViews() {
            // Header
            tvHeader = dialog.findViewById(R.id.tv_header);
            btnClose = dialog.findViewById(R.id.btn_close);
            
            // Month Navigation
            layoutMonthNavigation = dialog.findViewById(R.id.layout_month_navigation);
            btnPrevMonth = dialog.findViewById(R.id.btn_prev_month);
            btnNextMonth = dialog.findViewById(R.id.btn_next_month);
            tvMonthName = dialog.findViewById(R.id.tv_month_name);
            tvYear = dialog.findViewById(R.id.tv_year);
            
            // Year Picker
            layoutYearPicker = dialog.findViewById(R.id.layout_year_picker);
            scrollYearPicker = dialog.findViewById(R.id.scroll_year_picker);
            layoutYearGrid = dialog.findViewById(R.id.layout_year_grid);
            
            // Calendar
            layoutWeekdaysHeader = dialog.findViewById(R.id.layout_weekdays_header);
            gridCalendar = dialog.findViewById(R.id.grid_calendar);
            
            // Selected Range
            cardSelectedRange = dialog.findViewById(R.id.card_selected_range);
            tvSelectedRange = dialog.findViewById(R.id.tv_selected_range);
            
            // Action Buttons
            btnToday = dialog.findViewById(R.id.btn_today);
            btnCancel = dialog.findViewById(R.id.btn_cancel);
            btnConfirm = dialog.findViewById(R.id.btn_confirm);
            
            setupClickListeners();
            setupWeekdaysHeader();
        }
        
        private void setupClickListeners() {
            btnClose.setOnClickListener(v -> dialog.dismiss());
            
            btnPrevMonth.setOnClickListener(v -> navigateMonth(-1));
            btnNextMonth.setOnClickListener(v -> navigateMonth(1));
            
            // Year click to show year picker
            tvYear.setOnClickListener(v -> toggleYearPicker());
            
            btnToday.setOnClickListener(v -> goToToday());
            btnCancel.setOnClickListener(v -> dialog.dismiss());
            btnConfirm.setOnClickListener(v -> {
                if (startDate != null && endDate != null) {
                    showTimePickerDialog();
                }
            });
        }
        
        private void setupWeekdaysHeader() {
            layoutWeekdaysHeader.removeAllViews();
            
            if (layoutWeekdaysHeader.getOrientation() != LinearLayout.HORIZONTAL) {
                layoutWeekdaysHeader.setOrientation(LinearLayout.HORIZONTAL);
            }
            
            // Compact for all screen sizes
            int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
            int containerPadding = (int) (55 * activity.getResources().getDisplayMetrics().density);
            int availableWidth = screenWidth - containerPadding;
            int cellWidth = availableWidth / 7;
            int cellHeight = (int) (18 * activity.getResources().getDisplayMetrics().density);
            
            String[] weekdays = {"ش", "ی", "د", "س", "چ", "پ", "ج"};
            
            int dayCount = 0;
            for (String weekday : weekdays) {
                TextView tvWeekday = new TextView(activity);
                tvWeekday.setText(weekday);
                tvWeekday.setTextSize(10);
                tvWeekday.setTextColor(Color.parseColor("#666666"));
                tvWeekday.setGravity(Gravity.CENTER);
                tvWeekday.setTextDirection(View.TEXT_DIRECTION_RTL);
                tvWeekday.setTypeface(null, android.graphics.Typeface.BOLD);
                
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    cellWidth, cellHeight
                );
                params.setMargins(1, 0, 1, 0);
                tvWeekday.setLayoutParams(params);
                
                layoutWeekdaysHeader.addView(tvWeekday);
                dayCount++;
            }
            System.out.println("Weekday header count: " + dayCount);
        }
        
        private void navigateMonth(int direction) {
            currentCalendar.addMonths(direction);
            // Reset to first day of month
            currentCalendar = new PersianCalendarJava(
                currentCalendar.getYear(),
                currentCalendar.getMonth(),
                1
            );
            showYearPicker = false;
            updateDisplay();
        }
        
        private void toggleYearPicker() {
            showYearPicker = !showYearPicker;
            if (showYearPicker) {
                setupYearPicker();
            }
            updateDisplay();
        }
        
        private void setupYearPicker() {
            layoutYearGrid.removeAllViews();
            
            int currentYear = currentCalendar.getYear();
            int startYear = currentYear - 20;
            int endYear = currentYear + 20;
            
            int columnsPerRow = 4;
            LinearLayout currentRow = null;
            
            for (int year = startYear; year <= endYear; year++) {
                if ((year - startYear) % columnsPerRow == 0) {
                    currentRow = new LinearLayout(activity);
                    currentRow.setOrientation(LinearLayout.HORIZONTAL);
                    currentRow.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    ));
                    layoutYearGrid.addView(currentRow);
                }
                
                Button btnYear = new Button(activity);
                btnYear.setText(PersianDate.toPersianNumber(year));
                btnYear.setTextDirection(View.TEXT_DIRECTION_RTL);
                btnYear.setTextSize(14);
                
                if (year == currentYear) {
                    btnYear.setBackground(ContextCompat.getDrawable(activity, R.drawable.year_selector_background));
                    btnYear.setSelected(true);
                    btnYear.setTextColor(ContextCompat.getColor(activity, android.R.color.white));
                    btnYear.setTypeface(null, android.graphics.Typeface.BOLD);
                } else {
                    btnYear.setBackground(ContextCompat.getDrawable(activity, R.drawable.year_selector_background));
                    btnYear.setSelected(false);
                    btnYear.setTextColor(Color.parseColor("#333333"));
                }
                
                int finalYear = year;
                btnYear.setOnClickListener(v -> selectYear(finalYear));
                
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0, (int) (48 * activity.getResources().getDisplayMetrics().density), 1.0f
                );
                params.setMargins(6, 6, 6, 6);
                btnYear.setLayoutParams(params);
                
                if (currentRow != null) {
                    currentRow.addView(btnYear);
                }
            }
        }
        
        private void selectYear(int year) {
            currentCalendar = new PersianCalendarJava(year, currentCalendar.getMonth(), 1);
            showYearPicker = false;
            updateDisplay();
        }
        
        private void goToToday() {
            // Today button logic
            if (startDate == null) {
                // If no date selected, just navigate to today's month/year
                currentCalendar = new PersianCalendarJava(today.getYear(), today.getMonth(), today.getDay());
                showYearPicker = false;
                updateDisplay();
                return;
            } else if (endDate == null) {
                // If start exists and end is null, set today as end
                if (today.isBefore(startDate)) {
                    // If today is before start, swap
                    endDate = startDate;
                    startDate = today;
                } else {
                    // If today is after start, set as end
                    endDate = today;
                }
            } else {
                // If both dates are selected, do nothing
                return;
            }
            
            // Navigate to today's month/year
            currentCalendar = new PersianCalendarJava(today.getYear(), today.getMonth(), today.getDay());
            showYearPicker = false;
            updateDisplay();
        }
        
        private void updateDisplay() {
            // Update header
            tvHeader.setText("انتخاب بازه تاریخ");
            tvHeader.setTextDirection(View.TEXT_DIRECTION_RTL);
            
            if (showYearPicker) {
                // Show year picker
                layoutMonthNavigation.setVisibility(View.GONE);
                layoutWeekdaysHeader.setVisibility(View.GONE);
                gridCalendar.setVisibility(View.GONE);
                layoutYearPicker.setVisibility(View.VISIBLE);
            } else {
                // Show normal calendar
                layoutMonthNavigation.setVisibility(View.VISIBLE);
                layoutWeekdaysHeader.setVisibility(View.VISIBLE);
                gridCalendar.setVisibility(View.VISIBLE);
                layoutYearPicker.setVisibility(View.GONE);
                
                // Update month navigation
                PersianDate currentDate = currentCalendar.getPersianDate();
                System.out.println("=== Display Update Debug ===");
                System.out.println("Current Date: " + currentDate.getFormattedDate());
                System.out.println("Current Date Year: " + currentDate.getYear() + ", Month: " + currentDate.getMonth() + ", Day: " + currentDate.getDay());
                System.out.println("Month Name: " + currentDate.getMonthName());
                
                tvMonthName.setText(currentDate.getMonthName());
                tvMonthName.setTextDirection(View.TEXT_DIRECTION_RTL);
                
                tvYear.setText(PersianDate.toPersianNumber(currentDate.getYear()));
                tvYear.setTextDirection(View.TEXT_DIRECTION_RTL);
                tvYear.setTextColor(ContextCompat.getColor(activity, android.R.color.holo_blue_dark));
                
                // Update calendar grid
                updateCalendarGrid();
            }
            
            // Update selected range display
            updateSelectedRangeDisplay();
            
            // Update confirm button state - require both start and end dates
            btnConfirm.setEnabled(startDate != null && endDate != null);
        }
        
        private void updateCalendarGrid() {
            gridCalendar.removeAllViews();
            
            int currentYear = currentCalendar.getYear();
            int currentMonth = currentCalendar.getMonth();
            int daysInCurrentMonth = currentCalendar.getDaysInCurrentMonth();
            
            // Compute weekday for first day of month
            PersianDate firstDayOfMonth = new PersianDate(currentYear, currentMonth, 1);
            int firstDayOfWeek = firstDayOfMonth.getDayOfWeek();
            
            // Debug info
            System.out.println("=== Calendar Debug ===");
            System.out.println("Year: " + currentYear + ", Month: " + currentMonth + " (" + firstDayOfMonth.getMonthName() + ")");
            System.out.println("Days in month: " + daysInCurrentMonth);
            System.out.println("First day of month: " + firstDayOfMonth.getFormattedDate());
            System.out.println("First day of week: " + firstDayOfWeek + " (" + firstDayOfMonth.getDayOfWeekName() + ")");
            
            // Debug info
            System.out.println("=== Calendar Debug ===");
            System.out.println("Year: " + currentYear + ", Month: " + currentMonth + " (" + firstDayOfMonth.getMonthName() + ")");
            System.out.println("Days in month: " + daysInCurrentMonth);
            System.out.println("First day of month: " + firstDayOfMonth.getFormattedDate());
            System.out.println("First day of week: " + firstDayOfWeek + " (" + firstDayOfMonth.getDayOfWeekName() + ")");
            
            // Calculate required grid cells
            int totalCells = firstDayOfWeek + daysInCurrentMonth;
            int requiredRows = (int) Math.ceil((double) totalCells / 7);
            
            System.out.println("Total cells needed: " + totalCells);
            System.out.println("Required rows: " + requiredRows);
            
            // Configure GridLayout
            gridCalendar.setColumnCount(7);
            gridCalendar.setRowCount(requiredRows);
            
            // Compute cell sizes
            int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
            int containerPadding = (int) (60 * activity.getResources().getDisplayMetrics().density);
            int availableWidth = screenWidth - containerPadding;
            int cellWidth = availableWidth / 7;
            int cellHeight = (int) (32 * activity.getResources().getDisplayMetrics().density);
            
            int currentMonthDay = 1;
            
            // Build calendar grid
            for (int row = 0; row < requiredRows; row++) {
                for (int col = 0; col < 7; col++) {
                    int cellIndex = row * 7 + col;
                    Button btnDay;
                    PersianDate date;
                    boolean isCurrentMonth = false;
                    boolean isEmpty = false;
                    
                    System.out.println("Cell [" + row + "," + col + "] - Index: " + cellIndex + " - FirstDayOfWeek: " + firstDayOfWeek);
                    
                    if (cellIndex < firstDayOfWeek) {
                        // Empty cells before month starts
                        isEmpty = true;
                        date = new PersianDate(currentYear, currentMonth, 1);
                        System.out.println("  -> Empty cell (before month starts)");
                    } else if (currentMonthDay <= daysInCurrentMonth) {
                        // Current month days
                        date = new PersianDate(currentYear, currentMonth, currentMonthDay);
                        isCurrentMonth = true;
                        System.out.println("  -> Month day: " + currentMonthDay);
                        currentMonthDay++;
                    } else {
                        // Empty cells after month ends
                        isEmpty = true;
                        date = new PersianDate(currentYear, currentMonth, 1);
                        System.out.println("  -> Empty cell (after month ends)");
                    }
                    
                    if (isEmpty) {
                        btnDay = createEmptyDayButton(cellWidth, cellHeight);
                    } else {
                        btnDay = createDayButton(date, isCurrentMonth, cellWidth, cellHeight);
                    }
                    
                    gridCalendar.addView(btnDay);
                }
            }
            
            System.out.println("Calendar grid created successfully");
        }
        
        private Button createEmptyDayButton(int cellWidth, int cellHeight) {
            Button btnDay = new Button(activity);
            btnDay.setText("");
            btnDay.setEnabled(false);
            
            // Set transparent background for empty cells
            btnDay.setBackgroundColor(Color.TRANSPARENT);
            
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = cellWidth;
            params.height = cellHeight;
            params.setMargins(1, 1, 1, 1);
            btnDay.setLayoutParams(params);
            
            return btnDay;
        }
        
        private Button createDayButton(PersianDate date, boolean isCurrentMonth, int cellWidth, int cellHeight) {
            Button btnDay = new Button(activity);
            btnDay.setText(PersianDate.toPersianNumber(date.getDay()));
            btnDay.setTextDirection(View.TEXT_DIRECTION_RTL);
            
            // Determine button appearance
                    boolean isToday = date.isSameDay(today);
            boolean isRangeStart = startDate != null && startDate.isSameDay(date);
            boolean isRangeEnd = endDate != null && endDate.isSameDay(date);
                    boolean isInRange = startDate != null && endDate != null && 
                                      date.isAfter(startDate) && date.isBefore(endDate);
                    
            // Set background and text colors - clean professional styling
                    if (isRangeStart || isRangeEnd) {
                btnDay.setBackground(ContextCompat.getDrawable(activity, R.drawable.day_background_selected));
                btnDay.setTextColor(ContextCompat.getColor(activity, android.R.color.white));
                btnDay.setTypeface(null, android.graphics.Typeface.BOLD);
                    } else if (isInRange) {
                btnDay.setBackground(ContextCompat.getDrawable(activity, R.drawable.day_background_in_range));
                btnDay.setTextColor(ContextCompat.getColor(activity, android.R.color.black));
                    } else if (isToday) {
                btnDay.setBackground(ContextCompat.getDrawable(activity, R.drawable.day_background_today));
                btnDay.setTextColor(Color.parseColor("#FF9800")); // Orange color for today
                btnDay.setTypeface(null, android.graphics.Typeface.BOLD);
            } else {
                btnDay.setBackground(ContextCompat.getDrawable(activity, R.drawable.day_background_normal));
                if (isCurrentMonth) {
                    btnDay.setTextColor(Color.parseColor("#333333")); // Dark gray for current month days
                    } else {
                    btnDay.setTextColor(Color.parseColor("#BBBBBB")); // Light gray for other month days
                }
            }
            
            // Set click listener
            btnDay.setOnClickListener(v -> selectDate(date));
            
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = cellWidth;
            params.height = cellHeight;
            params.setMargins(1, 1, 1, 1);
            btnDay.setLayoutParams(params);
            
            return btnDay;
        }
        
        private void selectDate(PersianDate date) {
            // Date selection logic: user selects a start and an end date
            if (startDate == null) {
                // If no start date, set selected date as start
                startDate = date;
                endDate = null; // clear end so user can select it
            } else if (endDate == null) {
                // If start exists and end is null, set selected date as end
                if (date.isSameDay(startDate)) {
                    // If same as start, ignore
                    return;
                } else if (date.isBefore(startDate)) {
                    // If selected date is before start, swap
                    endDate = startDate;
                    startDate = date;
                } else {
                    // If selected date is after start, set as end
                    endDate = date;
                }
            } else {
                // If both selected, restart selection from this date
                startDate = date;
                endDate = null; // clear end so the user can select again
            }
            
            updateDisplay();
        }
        
        private void updateSelectedRangeDisplay() {
            if (startDate != null || endDate != null) {
                cardSelectedRange.setVisibility(View.VISIBLE);
                
                String rangeText;
                if (startDate != null && endDate != null) {
                    rangeText =
                               "از :  " + startDate.getFormattedDate() +  "  " +
                               "تا :  " + endDate.getFormattedDate() +   "  "  /*+
                               "فرمت عددی:\n" +
                               "از: " + startDate.getNumericFormattedDate() + "\n" +
                               "تا: " + endDate.getNumericFormattedDate()*/;
                } else if (startDate != null) {
                    rangeText = "از: " + startDate.getFormattedDate() + "\n" /*+
                               startDate.getNumericFormattedDate()*/;
                } else {
                    rangeText = "تاریخ انتخاب کنید";
                }
                
                tvSelectedRange.setText(rangeText);
                tvSelectedRange.setTextDirection(View.TEXT_DIRECTION_RTL);
                tvSelectedRange.setGravity(Gravity.CENTER);
            } else {
                cardSelectedRange.setVisibility(View.GONE);
            }
        }
        
        private void showTimePickerDialog() {
            Dialog timeDialog = new Dialog(activity);
            timeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            timeDialog.setContentView(R.layout.dialog_time_picker);
            
            // Set dialog size and corner radius
            Window window = timeDialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawableResource(R.drawable.dialog_background);
                
                android.view.WindowManager.LayoutParams params = window.getAttributes();
                params.width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
                params.height = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(params);
            }
            
            // Initialize time picker
            PersianTimePickerDialog timePickerDialog = new PersianTimePickerDialog(timeDialog, activity, startDate, endDate);
            timePickerDialog.setOnTimeRangeSelectedListener(new PersianTimePickerDialog.OnTimeRangeSelectedListener() {
                @Override
                public void onTimeRangeSelected(PersianDate startDate, PersianDate endDate, int startHour, int startMinute, int endHour, int endMinute) {
                    // Close both dialogs
                    timeDialog.dismiss();
                    dialog.dismiss();
                    
                    // Call the original listener with time information
                    if (listener != null) {
                        // For now, just call the original listener
                        // You can modify the interface to include time if needed
                        listener.onDateRangeSelected(startDate, endDate);
                    }
                    
                    // Show final result
                    showFinalResult(startDate, endDate, startHour, startMinute, endHour, endMinute);
                }
            });
            
            timeDialog.show();
        }
        
        private void showFinalResult(PersianDate startDate, PersianDate endDate, int startHour, int startMinute, int endHour, int endMinute) {
            String result = String.format("تاریخ و زمان انتخاب شده:\n\nاز: %s ساعت %02d:%02d\nتا: %s ساعت %02d:%02d",
                startDate.getFormattedDate(), startHour, startMinute,
                endDate.getFormattedDate(), endHour, endMinute);
            
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
            builder.setTitle("نتیجه نهایی")
                   .setMessage(result)
                   .setPositiveButton("تایید", (dialog, which) -> dialog.dismiss())
                   .show();
        }
        
        public void setOnDateRangeSelectedListener(OnDateRangeSelectedListener listener) {
            this.listener = listener;
        }
    }
} 