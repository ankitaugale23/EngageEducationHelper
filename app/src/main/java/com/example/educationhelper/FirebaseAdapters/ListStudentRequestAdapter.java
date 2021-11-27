package com.example.educationhelper.FirebaseAdapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.educationhelper.Faculty.FacultyChannelComponents;
import com.example.educationhelper.Faculty.RequestFromStudent;
import com.example.educationhelper.FirebaseDataClass.UploadStudentRequest;
import com.example.educationhelper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ListStudentRequestAdapter extends BaseAdapter implements Filterable{
    private Activity mContext;
    ArrayList<UploadStudentRequest> requestListMain, tempArray;
    DatabaseReference reference;
    CustomFilter cs;

    public ListStudentRequestAdapter(Activity mContext, ArrayList<UploadStudentRequest> employeeListMain){
        this.mContext = mContext;
        this.requestListMain = employeeListMain;
        this.tempArray = employeeListMain;
    }


    @Override
    public int getCount() {
        return requestListMain.size();
    }

    @Override
    public Object getItem(int position) {
        return requestListMain.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        View listItemView = inflater.inflate(R.layout.list_student_request, null, false);

        TextView tvName = listItemView.findViewById(R.id.row_studentName);
        Button mAccept = listItemView.findViewById(R.id.row_accept);
        Button mDelete = listItemView.findViewById(R.id.row_delete);

        String channelCode = requestListMain.get(position).getChannelCode();
        tvName.setText(requestListMain.get(position).getStudentName());

        mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = FirebaseDatabase.getInstance().getReference("credentials");
                UploadStudentRequest upload = new UploadStudentRequest(
                        requestListMain.get(position).getStudentName(),
                        requestListMain.get(position).getChannelName(),
                        requestListMain.get(position).getChannelCode(),
                        requestListMain.get(position).getFacultyPhone(),
                        requestListMain.get(position).getStudentPhone(),
                        "accepted"
                );

                reference.child(requestListMain.get(position).getFacultyPhone())
                        .child("channel").child(requestListMain.get(position).getChannelCode())
                        .child("members").child(requestListMain.get(position).getStudentPhone()).setValue(upload).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        reference.child(requestListMain.get(position).getStudentPhone()).child("channel")
                                .child(requestListMain.get(position).getChannelCode()).setValue(upload).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                reference.child(requestListMain.get(position).getFacultyPhone())
                                        .child("channel").child(requestListMain.get(position).getChannelCode())
                                        .child("request").child(requestListMain.get(position).getStudentPhone()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent i = new Intent(mContext, FacultyChannelComponents.class);
                                        i.putExtra("channelCode", channelCode);
                                        mContext.startActivity(i);
                                        mContext.finish();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = FirebaseDatabase.getInstance().getReference("credentials");

                reference.child(requestListMain.get(position).getStudentPhone()).child("channel")
                        .child(requestListMain.get(position).getChannelCode()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        reference.child(requestListMain.get(position).getFacultyPhone())
                                .child("channel").child(requestListMain.get(position).getChannelCode())
                                .child("request").child(requestListMain.get(position).getStudentPhone()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent i = new Intent(mContext, FacultyChannelComponents.class);
                                i.putExtra("channelCode", channelCode);
                                mContext.startActivity(i);
                                mContext.finish();
                            }
                        });
                    }
                });
            }
        });

        return listItemView;
    }


    @Override
    public Filter getFilter() {
        if(cs == null){
            cs = new CustomFilter();
        }
        return cs;
    }

    class CustomFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if(constraint!=null && constraint.length()>0) {
                constraint = constraint.toString().toUpperCase();
                ArrayList<UploadStudentRequest> filters = new ArrayList<>();
                for (int i = 0; i < tempArray.size(); i++) {
                    if (
                            tempArray.get(i).getStudentName().toUpperCase().contains(constraint)
                    )
                    {
                        UploadStudentRequest uploadEmployee = new UploadStudentRequest(
                                tempArray.get(i).getStudentName(),
                                tempArray.get(i).getChannelName(),
                                tempArray.get(i).getFacultyPhone(),
                                tempArray.get(i).getStatus(),
                                tempArray.get(i).getChannelCode(),
                                tempArray.get(i).getStudentPhone());
                        filters.add(uploadEmployee);
                    }
                }
                results.count = filters.size();
                results.values = filters;
            }
            else {
                results.count = tempArray.size();
                results.values = tempArray;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            requestListMain = (ArrayList<UploadStudentRequest>)filterResults.values;
            notifyDataSetChanged();
        }
    }
    ////////////////////////////////////////////////
}
