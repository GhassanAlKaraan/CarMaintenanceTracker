package com.example.cmt;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class RecordAdapter extends FirestoreRecyclerAdapter<CMTRecord, RecordAdapter.RecordViewHolder> {
    Context context;
    public RecordAdapter(@NonNull FirestoreRecyclerOptions<CMTRecord> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull RecordViewHolder holder, int position, @NonNull CMTRecord cmtRecord) {
        holder.name.setText(cmtRecord.name);
        holder.description.setText(cmtRecord.description);
        holder.serviceType.setText(cmtRecord.serviceType);

        //take me to RecordDetailsActivity to edit the record
        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context, RecordDetailsActivity.class);
            intent.putExtra("name", cmtRecord.name);
            intent.putExtra("description", cmtRecord.description);
            intent.putExtra("serviceType", cmtRecord.serviceType);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId", docId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_record_item, parent, false);
        return new RecordViewHolder(view);
    }

    class RecordViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView description;
        TextView serviceType;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.rec_name);
            description = itemView.findViewById(R.id.rec_desc);
            serviceType = itemView.findViewById(R.id.rec_st);
        }
    }
}
