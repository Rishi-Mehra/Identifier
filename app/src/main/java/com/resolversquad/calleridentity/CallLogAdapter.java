package com.resolversquad.calleridentity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.CallLogViewHolder> {

    private Context context;
    private ArrayList<CallLogModel> callLogModelArrayList;

    public CallLogAdapter(Context context, ArrayList<CallLogModel> callLogModelArrayList) {
        this.context = context;
        this.callLogModelArrayList = callLogModelArrayList;
    }

    @NonNull
    @Override
    public CallLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(context).inflate(R.layout.call_log_item, parent, false);
         return new CallLogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallLogViewHolder holder, int position) {

        final CallLogModel callLogModel = callLogModelArrayList.get(position);

        holder.name.setText(callLogModel.getName());
        holder.time.setText(callLogModel.getTime());
        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callLogModel.getNumber().isEmpty()){
                    Toast.makeText(context, "try again...", Toast.LENGTH_SHORT).show();
                }else {
                    String phoneNumber = "tel:" + callLogModel.getNumber();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse(phoneNumber));
                    context.startActivity(callIntent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return callLogModelArrayList.size();
    }

    public class CallLogViewHolder extends RecyclerView.ViewHolder{

        private ImageView userImage, btnCall;
        private TextView name, number, time;

        public CallLogViewHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.call_log_image);
            btnCall = itemView.findViewById(R.id.call_log_btn_call);
            name = itemView.findViewById(R.id.call_log_name);
            time = itemView.findViewById(R.id.call_log_time);
        }
    }
}
