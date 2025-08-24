package com.miaadrajabi.persiandatepicker;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Activity;
import androidx.core.content.ContextCompat;
import com.miaadrajabi.persiandatepicker.utils.PersianDate;
import java.util.Calendar;
import java.util.Locale;

class PersianTimePickerDialog extends Activity {
    
    private Dialog dialog;
    private Activity activity;
    private PersianDate startDate;
    private PersianDate endDate;
    private int startHour = -1;
    private int startMinute = -1;
    private int endHour = -1;
    private int endMinute = -1;
    private OnTimeRangeSelectedListener listener;
    
    // UI Components
    private TextView tvTimeHeader;
    private Button btnTimeClose;
    private LinearLayout cardSelectedDateRange;
    private TextView tvSelectedDateRange;
    private Button btnStartTime;
    private Button btnEndTime;
    private LinearLayout cardSelectedTimeRange;
    private TextView tvSelectedTimeRange;
    private Button btnTimeCancel;
    private Button btnTimeConfirm;
    
    public interface OnTimeRangeSelectedListener {
        void onTimeRangeSelected(PersianDate startDate, PersianDate endDate, int startHour, int startMinute, int endHour, int endMinute);
    }
    
    PersianTimePickerDialog(Dialog dialog, Activity activity, PersianDate startDate, PersianDate endDate) {
        this.dialog = dialog;
        this.activity = activity;
        this.startDate = startDate;
        this.endDate = endDate;
        
        initViews();
        setupClickListeners();
        updateDisplay();
    }
    
    private void initViews() {
        tvTimeHeader = dialog.findViewById(R.id.tv_time_header);
        btnTimeClose = dialog.findViewById(R.id.btn_time_close);
        cardSelectedDateRange = dialog.findViewById(R.id.card_selected_date_range);
        tvSelectedDateRange = dialog.findViewById(R.id.tv_selected_date_range);
        btnStartTime = dialog.findViewById(R.id.btn_start_time);
        btnEndTime = dialog.findViewById(R.id.btn_end_time);
        cardSelectedTimeRange = dialog.findViewById(R.id.card_selected_time_range);
        tvSelectedTimeRange = dialog.findViewById(R.id.tv_selected_time_range);
        btnTimeCancel = dialog.findViewById(R.id.btn_time_cancel);
        btnTimeConfirm = dialog.findViewById(R.id.btn_time_confirm);
    }
    
    private void setupClickListeners() {
        btnTimeClose.setOnClickListener(v -> dialog.dismiss());
        
        btnStartTime.setOnClickListener(v -> showTimePicker(true));
        btnEndTime.setOnClickListener(v -> showTimePicker(false));
        
        btnTimeCancel.setOnClickListener(v -> dialog.dismiss());
        btnTimeConfirm.setOnClickListener(v -> {
            if (listener != null && startHour != -1 && endHour != -1) {
                listener.onTimeRangeSelected(startDate, endDate, startHour, startMinute, endHour, endMinute);
            }
            dialog.dismiss();
        });
    }
    
    private void showTimePicker(boolean isStartTime) {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        
        TimePickerDialog timePickerDialog = new TimePickerDialog(
            activity,
            (view, hourOfDay, minute) -> {
                if (isStartTime) {
                    startHour = hourOfDay;
                    startMinute = minute;
                    updateStartTimeButton();
                } else {
                    endHour = hourOfDay;
                    endMinute = minute;
                    updateEndTimeButton();
                }
                updateTimeRangeDisplay();
                updateConfirmButton();
            },
            currentHour,
            currentMinute,
            true // 24-hour format
        );
        
        timePickerDialog.show();
    }
    
    private void updateStartTimeButton() {
        if (startHour != -1) {
            String timeText = String.format(Locale.getDefault(), "%02d:%02d", startHour, startMinute);
            btnStartTime.setText(timeText);
            btnStartTime.setBackground(ContextCompat.getDrawable(activity, R.drawable.day_background_selected));
            btnStartTime.setTextColor(ContextCompat.getColor(activity, android.R.color.white));
        }
    }
    
    private void updateEndTimeButton() {
        if (endHour != -1) {
            String timeText = String.format(Locale.getDefault(), "%02d:%02d", endHour, endMinute);
            btnEndTime.setText(timeText);
            btnEndTime.setBackground(ContextCompat.getDrawable(activity, R.drawable.day_background_selected));
            btnEndTime.setTextColor(ContextCompat.getColor(activity, android.R.color.white));
        }
    }
    
    private void updateTimeRangeDisplay() {
        if (startHour != -1 && endHour != -1) {
            cardSelectedTimeRange.setVisibility(View.VISIBLE);
            String timeRangeText = String.format(Locale.getDefault(), 
                "از ساعت %02d:%02d تا ساعت %02d:%02d", 
                startHour, startMinute, endHour, endMinute);
            tvSelectedTimeRange.setText(timeRangeText);
        } else {
            cardSelectedTimeRange.setVisibility(View.GONE);
        }
    }
    
    private void updateConfirmButton() {
        btnTimeConfirm.setEnabled(startHour != -1 && endHour != -1);
    }
    
    private void updateDisplay() {
        // Show selected date range
        if (startDate != null && endDate != null) {
            cardSelectedDateRange.setVisibility(View.VISIBLE);
            String dateRangeText = "از: " + startDate.getFormattedDate() + " تا: " + endDate.getFormattedDate();
            tvSelectedDateRange.setText(dateRangeText);
        } else {
            cardSelectedDateRange.setVisibility(View.GONE);
        }
        
        updateTimeRangeDisplay();
        updateConfirmButton();
    }
    
    public void setOnTimeRangeSelectedListener(OnTimeRangeSelectedListener listener) {
        this.listener = listener;
    }
} 