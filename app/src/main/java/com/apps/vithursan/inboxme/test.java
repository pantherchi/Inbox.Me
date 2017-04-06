package com.apps.vithursan.inboxme;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class test extends AppCompatActivity {
    protected TextView tvFirstname, tvSecondname, tvEmail, tvGender, tvDOB, tvID;
    private Button btnClearSP, btnChangePass;
    final String PHP_URL = "http://192.168.1.7/inboxme/userUpdate.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        tvID = (TextView)findViewById(R.id.tvId);
        tvFirstname = (TextView)findViewById(R.id.tvFirstname);
        tvSecondname = (TextView)findViewById(R.id.tvSecondname);
        tvEmail = (TextView)findViewById(R.id.tvEmail);
        tvGender = (TextView)findViewById(R.id.tvGender);
        tvDOB = (TextView)findViewById(R.id.tvDOB);
        btnClearSP = (Button)findViewById(R.id.btnClearSP);
        btnChangePass = (Button)findViewById(R.id.btnChangePass);

        String val = String.valueOf(LoginHandler.getInstance(this).getUserID());
        tvID.setText(val);
        tvFirstname.setText(LoginHandler.getInstance(this).getFirstname());
        tvSecondname.setText(LoginHandler.getInstance(this).getSecondname());
        tvEmail.setText(LoginHandler.getInstance(this).getEmail());
        tvGender.setText(LoginHandler.getInstance(this).getGender());
        tvDOB.setText(LoginHandler.getInstance(this).getDOB());

        btnClearSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                LoginHandler.getInstance(getApplicationContext()).logout();
                startActivity(new Intent(getApplicationContext(), InitialActivity.class));
            }
        });
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View adView = getLayoutInflater().inflate(R.layout.reset_password_dialog, null);

        final EditText etNewPass = (EditText)adView.findViewById(R.id.etNewPass);
        final EditText etConPass = (EditText)adView.findViewById(R.id.etConPass);
        final EditText etOldPass = (EditText)adView.findViewById(R.id.etOldPass);
//        final Button btnUpdate = (Button)adView.findViewById(R.id.btnUpdate);
//        final Button btnCancel = (Button)adView.findViewById(R.id.btnCancel);
//        btnUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!etNewPass.getText().toString().isEmpty() &&
//                        !etConPass.getText().toString().isEmpty() &&
//                        !etOldPass.getText().toString().isEmpty()){
//                    if (etNewPass != etConPass){
//                        Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
//                    }else{
//                        String newPass = etNewPass.getText().toString();
//                        sendData(newPass);
//                    }
//
//                }else {
//                    Toast.makeText(getApplicationContext(), "Enter all credentials", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                builder.setCancelable(true);
//            }
//        });

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newPass = etNewPass.getText().toString();
                String conPass = etConPass.getText().toString();
                String oldPass = etOldPass.getText().toString();
                if (!newPass.isEmpty() && !conPass.isEmpty() && !oldPass.isEmpty()){
                    if (!newPass.equals(conPass)){
                        Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }else{
                        sendData(newPass, oldPass);
                    }

                }else {
                    Toast.makeText(getApplication(), "Enter all credentials", Toast.LENGTH_SHORT).show();
                }
//                String newPass = etNewPass.getText().toString();
//                String conPass = etConPass.getText().toString();
//                String oldPAss = etOldPass.getText().toString();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setView(adView);
        builder.show();
    }

    private void sendData(final String newPass, final String oldPass) {
        final String id = String.valueOf(LoginHandler.getInstance(getApplicationContext()).getUserID());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PHP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")){
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                LoginHandler.getInstance(getApplicationContext()).logout();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
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
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("newPass", newPass);
                params.put("oldPass", oldPass);
                return params;
            }
        };
        SingletonRequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}
