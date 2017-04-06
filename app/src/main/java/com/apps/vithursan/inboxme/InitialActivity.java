package com.apps.vithursan.inboxme;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InitialActivity extends AppCompatActivity {
    private Typeface ComfortaaB, ComfortaaL;
    private TextView appName, TnC;
    private Button btnRegister, btnLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        ComfortaaB = Typeface.createFromAsset(getAssets(), "fonts/Comfortaa/Comfortaa-Bold.ttf");
        ComfortaaL = Typeface.createFromAsset(getAssets(), "fonts/Comfortaa/Comfortaa-Light.ttf");

        appName = (TextView)findViewById(R.id.appName);
        appName.setTypeface(ComfortaaB);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setTypeface(ComfortaaB);

        btnLogIn = (Button) findViewById(R.id.btnLogIn);
        btnLogIn.setTypeface(ComfortaaB);

        TnC = (TextView)findViewById(R.id.TnC);
        TnC.setTypeface(ComfortaaL);


    }
    public void onbtnLogInClick(View v){
        Intent i = new Intent(InitialActivity.this, LoginActivity.class);
        startActivity(i);
    }
    public void onbtnRegisterClick(View v){
        Intent i = new Intent(InitialActivity.this, RegisterActivity.class);
        startActivity(i);
    }
public void ontxtTermsClick(View v){
        Intent i = new Intent(InitialActivity.this, TermsActivity.class);
        startActivity(i);
    }
    public void testFragment(View v){
        Intent i = new Intent(InitialActivity.this, test.class);
        startActivity(i);
    }

}
