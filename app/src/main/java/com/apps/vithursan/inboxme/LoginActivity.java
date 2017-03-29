package com.apps.vithursan.inboxme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    protected TextView loginTitle, toolbarTitle;
    protected Typeface ComfortaaB, Nunito, NunitoB;
    protected EditText tfEmail, tfPassword;
    protected Button btnLogIn;
    private ProgressDialog progressDialog;

    final String PHP_URL = "http://192.168.1.7/inboxme/fln.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Toolbar - this can be used by the user to return back
        Toolbar loginToolbar = (Toolbar)findViewById(R.id.loginToolbar);
        setSupportActionBar(loginToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        ComfortaaB = Typeface.createFromAsset(getAssets(), "fonts/Comfortaa/Comfortaa-Bold.ttf");
        Nunito = Typeface.createFromAsset(getAssets(), "fonts/Nunito/Nunito-Regular.ttf");
        NunitoB = Typeface.createFromAsset(getAssets(), "fonts/Nunito/Nunito-Bold.ttf");

        toolbarTitle =(TextView)findViewById(R.id.toolbarTitle);
        toolbarTitle.setTypeface(Nunito);

        loginTitle = (TextView) findViewById(R.id.loginTitle);
        loginTitle.setTypeface(ComfortaaB);

        tfEmail = (EditText)findViewById(R.id.tfEmail);
        tfEmail.setTypeface(Nunito);

        tfPassword = (EditText)findViewById(R.id.tfPassword);
        tfPassword.setTypeface(Nunito);

        btnLogIn = (Button) findViewById(R.id.btnLogIn);
        btnLogIn.setTypeface(ComfortaaB);

        progressDialog = new ProgressDialog(this);

        btnLogIn.setOnClickListener(this);


    }
    private void userLogin(){
        final String email = tfEmail.getText().toString().trim();
        final String password = tfPassword.getText().toString().trim();

        progressDialog.setMessage("Logging In...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PHP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean("error")){
                        LoginHandler.getInstance(getApplicationContext()).userLogin(jsonObject.getInt("id"), jsonObject.getString("firstname"), jsonObject.getString("email"));
                        Toast.makeText(getApplicationContext(),"Successfully logged in",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), BottomActivity.class));
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),jsonObject.toString(),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogIn)
            userLogin();
    }
}
