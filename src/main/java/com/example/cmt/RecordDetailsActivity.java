package com.example.cmt;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class RecordDetailsActivity extends AppCompatActivity {

    EditText record_name, record_description;
    Spinner service_type_spinner;
    TextView record_title;
    String name1, desc1, serviceType1, docId1;
    boolean isEditMode = false;
    Button deleteRecordBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_details);
        service_type_spinner = findViewById(R.id.service_type_spinner);
        record_name = findViewById(R.id.record_name);
        record_description = findViewById(R.id.record_description);
        record_title = findViewById(R.id.record_title);
        deleteRecordBtn = findViewById(R.id.deleteRecordBtn);

            //Spinner connection to FireStore
            FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
            CollectionReference subjectsRef = rootRef.collection("Service Type");
            List<String> subjects = new ArrayList<>();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, subjects);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            service_type_spinner.setAdapter(adapter);
            subjectsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String subject = document.getString("name"); // a field inside the document called "name"
                            subjects.add(subject);
                        }
                        if (!isEditMode) {adapter.notifyDataSetChanged();}
                        else{adapter.notifyDataSetChanged();
                            int spinnerPosition = adapter.getPosition(serviceType1);
                            service_type_spinner.setSelection(spinnerPosition);}
                    }
                }
            });
            //End of Spinner connection code.



        //receive data: for Record Edit

        name1 = getIntent().getStringExtra("name");
        desc1 = getIntent().getStringExtra("description");
        serviceType1 = getIntent().getStringExtra("serviceType");
        docId1 = getIntent().getStringExtra("docId");

        if (docId1 != null && !docId1.isEmpty()) {
            isEditMode = true;
        }
        record_name.setText(name1);
        record_description.setText(desc1);


        if (isEditMode) {
            record_title.setText("Edit your Record");
            deleteRecordBtn.setVisibility(View.VISIBLE);
        }


    }

    public void delete_record(View v){
        deleteRecordFromFirebase();
    }

    public void deleteRecordFromFirebase() {
        DocumentReference documentReference = Utility.getCollectionReferenceForRecords().document(docId1);


        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //record is added
                    Utility.showToast(RecordDetailsActivity.this, "Record Deleted");
                    finish();
                } else {
                    Utility.showToast(RecordDetailsActivity.this, "Failed to delete record");
                    finish();
                }
            }
        });
    }

    public void save_record(View v) {
        //
        String r_name = record_name.getText().toString();
        String r_description = record_description.getText().toString();

        //Only name is required
        if (r_name == null || r_name.isEmpty()) {
            record_title.setError("Name is required");
            return;
        }

        CMTRecord cmtRecord = new CMTRecord();
        cmtRecord.setName(r_name);
        cmtRecord.setDescription(r_description);
        cmtRecord.setServiceType(service_type_spinner.getSelectedItem().toString());


        saveRecordToFirebase(cmtRecord);
    }

    public void saveRecordToFirebase(CMTRecord cmtRecord) {

        DocumentReference documentReference;
        if (isEditMode) {
            //update the record
            documentReference = Utility.getCollectionReferenceForRecords().document(docId1);
        } else {
            //create new record
            documentReference = Utility.getCollectionReferenceForRecords().document();
        }

        documentReference.set(cmtRecord).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //record is added
                    Utility.showToast(RecordDetailsActivity.this, "Record Added Successfully");
                    finish();
                } else {
                    Utility.showToast(RecordDetailsActivity.this, "Failed to add record");
                    finish();
                }
            }
        });
    }

    public void cancel_record(View v) {
        //
        finish();
    }

}
