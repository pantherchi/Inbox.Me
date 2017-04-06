package com.apps.vithursan.inboxme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity{
    protected EditText tfFirstName, tfSecondName, tfEmail, tfCEmail, tfPassword, tfCPassword;
    protected RadioGroup radioGroup;
    protected RadioButton radioButton,btnRadioMale, btnRadioFemale;
    protected Button btnRegister;
    protected DatePicker datePicker;
    protected TextView registerTitle, radioText, tvDOB, toolbarTitle;
    protected Typeface ComfortaaB, Nunito;
    protected Calendar cal;
    private ProgressDialog progressDialog;

    final String PHP_URL = "http://192.168.1.7/inboxme/registerUser.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Toolbar - this can be used by the user to return back
        Toolbar loginToolbar = (Toolbar)findViewById(R.id.loginToolbar);
        setSupportActionBar(loginToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        //Instantiating fonts that I want to use.
        ComfortaaB = Typeface.createFromAsset(getAssets(), "fonts/Comfortaa/Comfortaa-Bold.ttf");
        Nunito = Typeface.createFromAsset(getAssets(), "fonts/Nunito/Nunito-Regular.ttf");
        //Instantiating all the components beforehand in the onCreate method.
        //Text Views
        toolbarTitle =(TextView)findViewById(R.id.toolbarTitle);
        registerTitle = (TextView) findViewById(R.id.registerTitle);
        radioText = (TextView)findViewById(R.id.radioText);
        tvDOB = (TextView)findViewById(R.id.tvDOB);
        //Edit Texts
        tfFirstName = (EditText)findViewById(R.id.tfFirstName);
        tfSecondName = (EditText)findViewById(R.id.tfSecondName);
        tfEmail = (EditText)findViewById(R.id.tfEmail);
        tfCEmail = (EditText)findViewById(R.id.tfCEmail);
        tfPassword = (EditText)findViewById(R.id.tfPassword);
        tfCPassword = (EditText)findViewById(R.id.tfCPassword);
        //Buttons
        btnRadioMale =(RadioButton)findViewById(R.id.btnRadioMale);
        btnRadioFemale =(RadioButton)findViewById(R.id.btnRadioFemale);
        btnRadioMale.setTypeface(Nunito);
        btnRadioFemale.setTypeface(Nunito);
        btnRegister = (Button)findViewById(R.id.btnRegister);//Instantiating button to be used as a summit.

        //For loop to set app the edit text font to Nunito... Saves space.
        EditText[] editTexts = {tfFirstName, tfSecondName, tfEmail, tfCEmail, tfPassword, tfCPassword};
        for (int i = 0; i < editTexts.length; i++) {
            editTexts[i].setTypeface(Nunito);
        }

        //For loop for setting text view's respected fonts... Saves space.
        TextView[] textViews = {toolbarTitle, radioText, tvDOB, registerTitle};
        for (int i = 0; i < textViews.length; i++) {
            if(i <= 2){
                textViews[i].setTypeface(Nunito);
            }else{
                textViews[i].setTypeface(ComfortaaB);
            }
        }
        progressDialog = new ProgressDialog(this);

        ButtonListener();
    }

    public void ButtonListener(){
        //Declaring main components.
        radioGroup = (RadioGroup) findViewById(R.id.Gender);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        //Declaring the edit text components.
        final EditText firstname = (EditText)findViewById(R.id.tfFirstName);
        final EditText secondname = (EditText)findViewById(R.id.tfSecondName);
        final EditText email = (EditText)findViewById(R.id.tfEmail);
        final EditText cEmail = (EditText)findViewById(R.id.tfCEmail);
        final EditText password = (EditText)findViewById(R.id.tfPassword);
        final EditText cPassword = (EditText)findViewById(R.id.tfCPassword);
        //Button on click method.
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Passing in the necessary strings into user register function.
                userRegister(firstname, secondname, email, password, cEmail, cPassword);

            }
        });
    }

    //Function to store user data into the database.
    private void userRegister(EditText fist_name, EditText second_name, EditText mail, EditText pass, EditText confirm_mail, EditText confirm_pass){
        //Getting strings from the user and storing it into a variable.
        final String firstname = fist_name.getText().toString().trim();
        final String secondname = second_name.getText().toString().trim();
        final String email = mail.getText().toString().trim();
        final String password = pass.getText().toString().trim();
        final String cEmail = confirm_mail.getText().toString().trim();
        final String cPassword = confirm_pass.getText().toString().trim();

        //Getting string of chosen gender.
        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);
        final String gender = radioButton.getText().toString().trim();

        //Getting string of the date inputted by user in the date picker.
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();
        final String dob  = year+"-"+ month+"-"+ day;

        //Users date input in the date picker.
        cal = Calendar.getInstance();
        int cYear = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH) + 1;
        int cDay = cal.get(Calendar.DAY_OF_MONTH);

        //Getting the values into the date format.
        Date cDate = new Date(cYear,cMonth,cDay);
        Date bDate = new Date(year,month,day);

        boolean confirmPassword = password.equals(cPassword);
        boolean confirmEmail = email.equals(cEmail);

        //Passing parameters to validate (Basic).
        int value = validate(cDate, bDate, confirmEmail, confirmPassword);

        if (value == 3) {
            //Setting up a progress dialog so that the user can follow the process.
            progressDialog.setMessage("Registering...");
            progressDialog.show();
            //Takes 4 parameter: 1.Method  2.URL  3.Response Listener  4.Error Listener  and Override getParams to pass in my data.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, PHP_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.hide();
                    Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("gender", gender);
                    params.put("dob", dob);
                    params.put("firstname", firstname);
                    params.put("secondname", secondname);
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }
            };
            SingletonRequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        }else {
            if (value == 0){
                Toast.makeText(RegisterActivity.this, "You have to be over 16 years old.", Toast.LENGTH_SHORT).show();
            }else if(value == 1){
                Toast.makeText(RegisterActivity.this, "Please check your email", Toast.LENGTH_SHORT).show();
            }else if(value == 2){
                Toast.makeText(RegisterActivity.this, "Please check your Password", Toast.LENGTH_SHORT).show();
            }
//            Toast.makeText(RegisterActivity.this, "Some error occurred, Please check your inputs.", Toast.LENGTH_SHORT).show();
        }
    }
    //This function will check if the user goes through basic validations.
    private int validate(Date cu, Date bi, boolean confirm_email, boolean confirm_password){
        if ((((cu.getTime()-bi.getTime())/(1000*60*60*24)) < 5844)){
            return 0;
        }else if (!confirm_email){
            return  1;
        }else if (!confirm_password){
            return 2;
        }else {
            return 3;
        }
    }
}