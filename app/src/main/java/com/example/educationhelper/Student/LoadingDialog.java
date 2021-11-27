package com.example.educationhelper.Student;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.educationhelper.R;

public class LoadingDialog {
    private Activity activity;
    private AlertDialog dialog;
    TextView textView;

    LoadingDialog(Activity myActivity) {
        activity = myActivity;
    }

    void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View content = inflater.inflate(R.layout.custom_dialog, null);
        builder.setView(content);
        builder.setCancelable(false);
        textView = content.findViewById(R.id.textViewCustomDialog);
        dialog = builder.create();
        dialog.show();
    }

    void dismissDialog() {
        dialog.dismiss();
    }

    void setText(String val) {
        textView.setText(val);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    }
}
