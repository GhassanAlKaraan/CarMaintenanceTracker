package com.example.cmt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    EditText username, vehicleName;
    Button getStarted;
    ProgressBar progressBar;
    SharedPreferences sp;

    String user_name, vehicle_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        vehicleName = findViewById(R.id.vehiclename);
        getStarted = findViewById(R.id.getstarted);
        progressBar = findViewById(R.id.progress_bar);
        sp = getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        getStarted.setOnClickListener(v -> getStarted());

    }

    public void getStarted() {
        user_name = username.getText().toString();
        vehicle_name = vehicleName.getText().toString();
        //Save the Data locally in the Apps shared preferences
        //I didnt save it in database, because there's no authentication needed.
        SharedPreferences sp = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString("user_name", user_name);
        editor.putString("vehicle_name", vehicle_name);
        editor.apply();


        if(vehicle_name.isEmpty() || user_name.isEmpty()){
            Toast.makeText(this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            Toast.makeText(this, "Welcome " +user_name, Toast.LENGTH_SHORT).show();
        }

        //Loading animation
        changeInProgress(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                changeInProgress(false);
                //send the userId (document ID to the next Activity)
                startActivity(new Intent(MainActivity.this, RecordActivity.class));
                finish();            }
        }, 800);

    }

    //Loading animation
    private void changeInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            getStarted.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            getStarted.setVisibility(View.VISIBLE);
        }
    }

}