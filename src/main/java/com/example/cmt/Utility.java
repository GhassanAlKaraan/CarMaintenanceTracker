package com.example.cmt;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Utility {

    static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    static CollectionReference getCollectionReferenceForRecords(){
        return FirebaseFirestore.getInstance().collection("records").document("user1").collection("my_records");
    }



}
