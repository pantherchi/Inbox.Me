package com.apps.vithursan.inboxme;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class SearchFriendsActivity extends AppCompatActivity{
    TextView tvSearch, tvItem;
    Button btnSearch, btnAction;
    ProgressDialog progressDialog;

    final String PHP_URL_GET = "http://192.168.1.7/inboxme/addFriends.php";
    final String PHP_URL_QUERY = "http://192.168.1.7/inboxme/userFriends.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar myChatToolbar = (Toolbar) findViewById(R.id.my_chat_toolbar);
        myChatToolbar.inflateMenu(R.menu.cancel_menu);
        myChatToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_cancel:
                        SearchFriendsHandler.getInstance(getApplicationContext()).clearData();
                        finish();
                }
                return true;
            }
        });
        tvSearch = (TextView)findViewById(R.id.tvSearch);
        tvItem = (TextView)findViewById(R.id.tvItem);


        btnSearch = (Button)findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDatabase();
            }
        });

        btnAction = (Button)findViewById(R.id.btnAction);
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryRelations();
            }
        });


        progressDialog = new ProgressDialog(this);

    }

    private void searchDatabase() {
        final String search = tvSearch.getText().toString().trim();
        final String id = String.valueOf(LoginHandler.getInstance(this).getUserID());

        progressDialog.setMessage("Searching...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PHP_URL_GET, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean("error")){
                        SearchFriendsHandler.getInstance(getApplicationContext()).getData(
                                jsonObject.getInt("id"),
                                jsonObject.getString("action"),
                                jsonObject.getString("username"));
                        parseData();
                    }else{
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("searchQuery", search);
                params.put("currentUser", id);
                return params;
            }
        };
        SingletonRequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void parseData() {
        tvItem.setText(SearchFriendsHandler.getInstance(this).getUsername());
        final String action = SearchFriendsHandler.getInstance(this).getKeyAction();

        btnActionText(action);
    }

    private void btnActionText(String action) {
        if (action.equals("1")){
            btnAction.setText("Remove");
        }else {
            btnAction.setText("Add");
        }
    }

    private void queryRelations() {
        final String user_one = String.valueOf(LoginHandler.getInstance(this).getUserID());
        final String user_two = String.valueOf(SearchFriendsHandler.getInstance(this).getUserID());
        final String action = SearchFriendsHandler.getInstance(this).getKeyAction();
//        Toast.makeText(getApplicationContext(), action, Toast.LENGTH_SHORT).show();

        progressDialog.setMessage("Searching...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PHP_URL_QUERY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean("error")){
//                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                        parseData();
                    }else{
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_one", user_one);
                params.put("user_two", user_two);
                params.put("action", action);
                return params;
            }
        };
        SingletonRequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }


}
