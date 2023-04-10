package com.example.cmt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.UUID;

public class SetReminderActivity extends AppCompatActivity {

    TextView time_tv, date_tv;
    String title;
    Button add_reminder, cancel_reminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_reminder);
        createNotificationChannel();
        date_tv = findViewById(R.id.date_tv);
        time_tv = findViewById(R.id.time_tv);
        add_reminder = findViewById(R.id.add_reminder);
        cancel_reminder = findViewById(R.id.cancel_reminder);
        title = getIntent().getStringExtra("message0");
        date_tv.setText(title);
    }

    public void cancel(View v) {
        finish();
    }

    public void add(View v){
        //send the record name
        Intent intent = new Intent(SetReminderActivity.this, ReminderBroadcast.class);
        intent.putExtra("title", title); // set the message to display in the notification


        int hour, minute, year, month, day;
        String myDate = date_tv.getText().toString();
        String myTime = time_tv.getText().toString();
        if(myDate == null || myDate.isEmpty()){
            Toast.makeText(this, "Choose date first", Toast.LENGTH_SHORT).show();
            return;
        }else{
            String[] dateArray = myDate.split("/");

            // Extract the year, month, and day as integers
            year = Integer.parseInt(dateArray[0]);
            month = Integer.parseInt(dateArray[1]);
            day = Integer.parseInt(dateArray[2]);
        }
        if(myTime == null || myTime.isEmpty()){
            Toast.makeText(this, "Choose time first", Toast.LENGTH_SHORT).show();
            return;
        }else{
            String[] timeArray = myTime.split(":");

            // Extract the year, month, and day as integers
            hour = Integer.parseInt(timeArray[0]);
            minute = Integer.parseInt(timeArray[1]);
        }
        //calculate the difference
        Calendar currentTime = Calendar.getInstance();
//        long currentTimeInMillis = currentTime.getTimeInMillis();

        Calendar newTime = Calendar.getInstance();
        newTime.set(year, month-1, day, hour, minute);

        long newTimeInMillis = newTime.getTimeInMillis();

//        long timeDifferenceInMillis = newTimeInMillis - currentTimeInMillis;


        //DEBUGGING
//        System.out.println(year);
//        System.out.println(month);
//        System.out.println(day);
//        System.out.println(hour);
//        System.out.println(minute);
//        System.out.println("New time "+ newTimeInMillis);
//        System.out.println("Current time "+ currentTimeInMillis);
//        System.out.println("difference is "+timeDifferenceInMillis);
//

        //toast
        Toast.makeText(this, "Reminder Added", Toast.LENGTH_SHORT).show();

        //Set up the alarm
        int requestCode = UUID.randomUUID().hashCode();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(SetReminderActivity.this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //USED FOR DEBUGGING:
        //long time = System.currentTimeMillis();
        //long newTimeInMillis = 1000 * 5; // 5seconds

        alarmManager.set(AlarmManager.RTC_WAKEUP,
                newTimeInMillis,
                pendingIntent);
        add_reminder.setEnabled(false);
        add_reminder.setText("Added");
        cancel_reminder.setText("Go Back");
        //finish();
    }

    public void set_date(View v) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        openDatePicker(year, month, day);
    }

    public void set_time(View v) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);
        openTimePicker(hour, min);
    }


    private void openDatePicker(int year, int month, int day) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //Showing the picked value in the textView
                date_tv.setText(String.valueOf(year) + "/" + String.valueOf(month+1) + "/" + String.valueOf(day));
            }
        }, year, month, day);

        Calendar calendar = Calendar.getInstance();
        long currentTimeInMillis = calendar.getTimeInMillis();
        datePickerDialog.getDatePicker().setMinDate(currentTimeInMillis);
        datePickerDialog.show();
    }

    private void openTimePicker(int cHour, int cMin) {

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Calendar selectedTime = Calendar.getInstance();
                selectedTime.set(Calendar.HOUR_OF_DAY, hour);
                selectedTime.set(Calendar.MINUTE, minute);

                // Get the current time
                Calendar currentTime = Calendar.getInstance();

                // Check if the selected time is in the past
                //Showing the picked value in the textView
                if (selectedTime.getTimeInMillis()<currentTime.getTimeInMillis()) {
                    // Display a message to the user
                    Toast.makeText(SetReminderActivity.this, "Please select a time in the future.", Toast.LENGTH_SHORT).show();
                } else {
                    time_tv.setText(hour + ":" + minute);
                }
            }
        }, cHour, cMin, false);

        timePickerDialog.show();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Car Tracker";
            String description = "Channel for Car Tracker";
            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            // Create the NotificationChannel with the specified parameters
            NotificationChannel channel = new NotificationChannel("cartracker", name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);

            // Register the channel with the system
            notificationManager.createNotificationChannel(channel);
        }
    }
}