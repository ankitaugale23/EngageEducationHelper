package com.example.educationhelper.FirebaseAdapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.educationhelper.CurrentUser;
import com.example.educationhelper.Faculty.FacultyChannelComponents;
import com.example.educationhelper.FirebaseDataClass.UploadChat;
import com.example.educationhelper.FirebaseDataClass.UploadStudentRequest;
import com.example.educationhelper.R;
import com.example.educationhelper.Student.StudentForum;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListChatAdapter extends BaseAdapter {
    private Activity mContext;
    ArrayList<UploadChat> requestListMain, tempArray;
    DatabaseReference reference;

    public ListChatAdapter(Activity mContext, ArrayList<UploadChat> employeeListMain){
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
        View listItemView = inflater.inflate(R.layout.list_chat, null, false);

        TextView tvName1 = listItemView.findViewById(R.id.row_name1);
        TextView tvName2 = listItemView.findViewById(R.id.row_name2);

        TextView tvMessage1 = listItemView.findViewById(R.id.row_message1);
        TextView tvMessage2 = listItemView.findViewById(R.id.row_message2);

        LinearLayout linearLayout1 = listItemView.findViewById(R.id.row_linear1);
        LinearLayout linearLayout2 = listItemView.findViewById(R.id.row_linear2);

        tvName1.setText(requestListMain.get(position).getName());
        tvName2.setText(requestListMain.get(position).getName());

        tvMessage1.setText(requestListMain.get(position).getMessage());
        tvMessage2.setText(requestListMain.get(position).getMessage());

        String currentUserId = new CurrentUser(mContext).getUsername(); //Fetch current user info

        //if chat is sent by current user then show the send_chat UI
        if(currentUserId.equals(requestListMain.get(position).getUserid())){
            linearLayout1.setVisibility(View.INVISIBLE);
            linearLayout2.setVisibility(View.VISIBLE);
        }
        else {
            //if chat is not sent by current user then show receive_chat UI
            linearLayout1.setVisibility(View.VISIBLE);
            linearLayout2.setVisibility(View.INVISIBLE);
        }
        return listItemView;
    }


}
