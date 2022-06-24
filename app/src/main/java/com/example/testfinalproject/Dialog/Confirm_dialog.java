package com.example.testfinalproject.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.testfinalproject.DoInBackGroundCaller;
import com.example.testfinalproject.R;


public class Confirm_dialog extends Dialog {
    private TextView txtAlertTitle;
    private TextView txtAlertBody;
    private Button btnAlertPositive;
    private Button btnAlertNegative;
    private RelativeLayout iv_x;

    public Confirm_dialog(@NonNull Context context) {
        super(context);
    }

    DoInBackGroundCaller callbackPositiveHandler;
    DoInBackGroundCaller callbackNegativeHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_dilog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        setCancelable(false);


        txtAlertTitle = (TextView) findViewById(R.id.txtAlertTitle);
        txtAlertBody = (TextView) findViewById(R.id.txtAlertBody);
        btnAlertPositive = (Button) findViewById(R.id.btnAlertPositive);
        btnAlertNegative = (Button) findViewById(R.id.btnAlertNegative);

        iv_x = (RelativeLayout) findViewById(R.id.iv_x);

        iv_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnAlertPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (callbackPositiveHandler != null) {
                    callbackPositiveHandler.doTask();
                }


            }
        });


        btnAlertNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (callbackNegativeHandler != null) {
                    callbackNegativeHandler.doTask();
                }


            }
        });

    }

    public void show(String title, String message, String buttonText) {
        show();
        txtAlertTitle.setText(title);
        txtAlertBody.setText(message);
        btnAlertPositive.setText(buttonText);


    }

    public void show(String title, String message, String buttonPositiveText,
                     String buttonNegativeText, DoInBackGroundCaller doInBackGroundCallerPositive, DoInBackGroundCaller doInBackGroundCallerNegative) {
        show();
        txtAlertTitle.setText(title);
        txtAlertBody.setText(message);
        btnAlertPositive.setText(buttonPositiveText);
        btnAlertNegative.setText(buttonNegativeText);


        this.callbackPositiveHandler = doInBackGroundCallerPositive;
        this.callbackNegativeHandler = doInBackGroundCallerNegative;

    }
}
