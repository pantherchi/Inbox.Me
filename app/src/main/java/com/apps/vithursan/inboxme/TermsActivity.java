package com.apps.vithursan.inboxme;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class TermsActivity extends AppCompatActivity {
    private TextView toolbarTitle, tvTermsText;
    private Typeface Nunito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        /*
        //this code is for the pop up window of this activity that I wanted to have however the whole display inflate looks better
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width * 0.9),(int)(height * 0.9));

        */

        //Top tool bar is declared and set to return back
        Toolbar loginToolbar = (Toolbar) findViewById(R.id.loginToolbar);
        setSupportActionBar(loginToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");


        //Setting typeface to desired components.
        //Here type face is set to a variable.
        Nunito = Typeface.createFromAsset(getAssets(), "fonts/Nunito/Nunito-Regular.ttf");

        //The text inside tool bar is targeted and applied with the typeface above.
        toolbarTitle =(TextView)findViewById(R.id.toolbarTitle);
        toolbarTitle.setTypeface(Nunito);

        //The text inside the
        tvTermsText =(TextView)findViewById(R.id.tvTermsText);
        tvTermsText.setTypeface(Nunito);
    }
}
