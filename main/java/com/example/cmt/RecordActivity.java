package com.example.cmt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

public class RecordActivity extends AppCompatActivity {

    TextView welcome;
    TextView carname;
    FloatingActionButton add_record_btn;
    RecyclerView recyclerView;
    RecordAdapter recordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        //Display Prefs
        welcome = findViewById(R.id.welcome);
        carname = findViewById(R.id.carname);
        SharedPreferences sp = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        welcome.setText("Welcome " + sp.getString("user_name", "DEFAULT VALUE"));
        carname.setText(sp.getString("vehicle_name", "DEFAULT VALUE"));

        //Floating Action Button:
        add_record_btn = findViewById(R.id.add_record_btn);
        add_record_btn.setOnClickListener((v) -> startActivity(new Intent(RecordActivity.this, RecordDetailsActivity.class)));


        //recycler view
        recyclerView = findViewById(R.id.recycler_view);
        setupRecyclerView();
    }

    public void setupRecyclerView(){
        Query query = Utility.getCollectionReferenceForRecords().orderBy("name", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<CMTRecord> options = new FirestoreRecyclerOptions.Builder<CMTRecord>()
                .setQuery(query, CMTRecord.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recordAdapter = new RecordAdapter(options, this);
        recyclerView.setAdapter(recordAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recordAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        recordAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        recordAdapter.notifyDataSetChanged();
    }

    public void changename(View v){
        startActivity(new Intent(RecordActivity.this, MainActivity.class));
    }


}