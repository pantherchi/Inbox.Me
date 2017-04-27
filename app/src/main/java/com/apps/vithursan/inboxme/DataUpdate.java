package com.apps.vithursan.inboxme;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class DataUpdate extends AppCompatActivity {
    protected RadioGroup radioGroup;
    protected RadioButton radioButton;
    protected DatePicker datePicker;
    private ProgressDialog progressDialog;

    //Location of the php script for updating user credentials.
//    private static final String PHP_URL = "http://192.168.1.7/inboxme/userAllUpdate.php";
//    private static final String PHP_URL = "https://inboxme.000webhostapp.com/inboxme/userAllUpdate.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_update);


        final String[] optionItems = {"Firstname", "Secondname", "Email", "Password", "Gender", "Date of birth"};

        ListView listView =(ListView) findViewById(R.id.lvUpdate);
        ListAdapter listViewAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,optionItems);
        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        long x = parent.getItemIdAtPosition(position);
                        if(x == 0){
                            //updating the firstname
                            //initialising the respective dialog xml layout.
                            View nameView = getLayoutInflater().inflate(R.layout.reset_name_dialog, null);
                            //Getting the string of the clicked item so that it can be used to assign the alert title.
                            String item = String.valueOf(parent.getItemAtPosition(position));
                            //Data is passed into the method which creates the alertDialog
                            //item---position---view
                            updateName(item, "1", nameView);

                        }else if (x == 1){
                            //updating the secondname
                            //initialising the respective dialog xml layout.
                            View nameView = getLayoutInflater().inflate(R.layout.reset_name_dialog, null);
                            //Getting the string of the clicked item so that it can be used to assign the alert title.
                            String item = String.valueOf(parent.getItemAtPosition(position));
                            //Data is passed into the method which creates the alertDialog
                            updateName(item, "2", nameView);

                        }else if (x == 2){
                            //updating the secondname
                            //initialising the respective dialog xml layout.
                            View lnView = getLayoutInflater().inflate(R.layout.reset_login_dialog, null);
                            //Getting the string of the clicked item so that it can be used to assign the alert title.
                            String item = String.valueOf(parent.getItemAtPosition(position));
                            //Data is passed into the method which creates the alertDialog

                            final EditText etNewEmail = (EditText)lnView.findViewById(R.id.etValue);
                            final EditText etConEmail = (EditText)lnView.findViewById(R.id.etConfirmValue);
                            final EditText etPassword = (EditText)lnView.findViewById(R.id.etPassword);

                            etNewEmail.setHint("New Email");
                            etConEmail.setHint("Confirm Email");
                            etPassword.setHint("Old Password");

                            updateLogin(item, "3", lnView);

                        }else if (x == 3){
                            View lnView = getLayoutInflater().inflate(R.layout.reset_login_dialog, null);
                            String item = String.valueOf(parent.getItemAtPosition(position));

                            final EditText etNewPass = (EditText)lnView.findViewById(R.id.etValue);
                            final EditText etConPass = (EditText)lnView.findViewById(R.id.etConfirmValue);
                            final EditText etOldPass = (EditText)lnView.findViewById(R.id.etPassword);

                            //Setting the input type through java so that the same dialog can be used again for other purpose
                            //Here the edit texts are set to the input methods to password.
                            etNewPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            etConPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

                            //Setting each EditText hint.
                            etNewPass.setHint("New Password");
                            etConPass.setHint("Confirm Password");
                            etOldPass.setHint("Old Password");

                            updateLogin(item, "4", lnView);


                        }
                        else if (x == 4){
                            View genView = getLayoutInflater().inflate(R.layout.reset_gender_dialog, null);
                            String item = String.valueOf(parent.getItemAtPosition(position));
                            updateGender(item, "5", genView);

                        }else{
                            View dobView = getLayoutInflater().inflate(R.layout.reset_dob_dialog, null);
                            String item = String.valueOf(parent.getItemAtPosition(position));
                            updateDOB(item, "6", dobView);
                        }
                    }
                }
        );
        progressDialog = new ProgressDialog(this);
    }

    private void updateName(final String title, final String pos, final View rootView) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final TextView tvDialogTitle = (TextView) rootView.findViewById(R.id.tvDialogTitle);
        tvDialogTitle.setText("Update " + title);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final EditText etName = (EditText)rootView.findViewById(R.id.etValue);
                final EditText etPassword = (EditText)rootView.findViewById(R.id.etPassword);

                String value = etName.getText().toString();
                String password = etPassword.getText().toString();

                updateDatabase(value, password, pos);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setView(rootView);
        builder.show();
    }

    private void updateLogin(final String title, final String pos, final View rootView) {
        final String x = pos;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final TextView tvDialogTitle = (TextView) rootView.findViewById(R.id.tvDialogTitle);
        tvDialogTitle.setText("Update " + title);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final EditText etValue = (EditText)rootView.findViewById(R.id.etValue);
                final EditText etConfirmValue = (EditText)rootView.findViewById(R.id.etConfirmValue);
                final EditText etPassword = (EditText)rootView.findViewById(R.id.etPassword);

                String value = etValue.getText().toString();
                String confirmValue = etConfirmValue.getText().toString();
                String password = etPassword.getText().toString();

                if (!value.isEmpty() && !confirmValue.isEmpty() && !password.isEmpty()){
                    if (!value.equals(confirmValue)){
                        Toast.makeText(getApplicationContext(), "Credentials do not match...", Toast.LENGTH_SHORT).show();
                    }else{
                        updateDatabase(confirmValue, password, x);
                    }

                }else {
                    Toast.makeText(getApplication(), "Enter all credentials...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setView(rootView);
        builder.show();
    }

    private void updateGender(final String title, final String pos, final View rootView) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final TextView tvDialogTitle = (TextView) rootView.findViewById(R.id.tvDialogTitle);
        tvDialogTitle.setText("Update " + title);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Getting string of chosen gender.
                radioGroup = (RadioGroup) rootView.findViewById(R.id.Gender);
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) rootView.findViewById(selectedId);
                final String gender = radioButton.getText().toString().trim();

                final EditText etPassword = (EditText)rootView.findViewById(R.id.etPassword);
                String password = etPassword.getText().toString();

                updateDatabase(gender, password, pos);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setView(rootView);
        builder.show();
    }

    private void updateDOB(final String title, final String pos, final View rootView) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final TextView tvDialogTitle = (TextView) rootView.findViewById(R.id.tvDialogTitle);
        tvDialogTitle.setText("Update " + title);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                datePicker = (DatePicker) rootView.findViewById(R.id.datePicker);

                //Getting string of the date inputted by user in the date picker.
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();
                final String dob  = year+"-"+ month+"-"+ day;

                final EditText etPassword = (EditText)rootView.findViewById(R.id.etPassword);
                String password = etPassword.getText().toString();

                updateDatabase(dob, password, pos);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setView(rootView);
        builder.show();
    }

    private void updateDatabase(final String value, final String password, final String pos) {
        final String id = String.valueOf(LoginHandler.getInstance(getApplicationContext()).getUserID());
        progressDialog.setMessage("Updating...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Scripts.O_DATA_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")){
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                finish();
                                LoginHandler.getInstance(getApplicationContext()).logout();
                                startActivity(new Intent(getApplicationContext(), InitialActivity.class));
                            }else{
                                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("oldPass", password);
                params.put("operation", pos);
                params.put("value", value);
                return params;
            }
        };
        SingletonRequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}