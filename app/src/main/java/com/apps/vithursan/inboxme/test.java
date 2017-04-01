package com.apps.vithursan.inboxme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class test extends AppCompatActivity {
    protected TextView tvFirstname, tvSecondname, tvEmail, tvGender, tvDOB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        tvFirstname = (TextView)findViewById(R.id.tvFirstname);
        tvSecondname = (TextView)findViewById(R.id.tvSecondname);
        tvEmail = (TextView)findViewById(R.id.tvEmail);
        tvGender = (TextView)findViewById(R.id.tvGender);
        tvDOB = (TextView)findViewById(R.id.tvDOB);

        tvFirstname.setText(LoginHandler.getInstance(this).getFirstname());
        tvSecondname.setText(LoginHandler.getInstance(this).getSecondname());
        tvEmail.setText(LoginHandler.getInstance(this).getEmail());
        tvGender.setText(LoginHandler.getInstance(this).getGender());
        tvDOB.setText(LoginHandler.getInstance(this).getDOB());
    }
}
