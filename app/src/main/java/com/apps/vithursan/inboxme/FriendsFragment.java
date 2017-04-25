package com.apps.vithursan.inboxme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment{
    private List<FriendsData> listFriends;
    private RecyclerView recyclerView;
    protected LinearLayoutManager linearLayoutManager;
    private RecyclerView.Adapter adapter;
    private RequestQueue requestQueue;
    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_friends, container, false);

        Toolbar myChatToolbar = (Toolbar)view.findViewById(R.id.my_chat_toolbar);
        myChatToolbar.inflateMenu(R.menu.app_bar);
        myChatToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_add_user:
                        startActivity(new Intent(getActivity(), SearchActivity.class));
                }
                return true;
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        listFriends = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(getContext());

        getData();

        adapter = new FriendsAdapter(listFriends, getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void getData() {

        final int id = LoginHandler.getInstance(getContext()).getUserID();
        requestQueue.add(getDataFromServer(id));
    }

    private JsonArrayRequest getDataFromServer(int id) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(FriendsHandler.DATA_URL + String.valueOf(id),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Calling method parseData to parse the json response
                        parseData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Error
                    }
                });
        return jsonArrayRequest;
    }


    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
        //Post object is being initialised.
        FriendsData friendsData = new FriendsData();
        //Json object is created
        JSONObject json;
        try {
            //Getting json object from the array.
            json = array.getJSONObject(i);

            //Adding each json data to the post object.
            friendsData.setUsername(json.getString(FriendsHandler.TAG_USERNAME));
            friendsData.setFirstname(json.getString(FriendsHandler.TAG_FIRSTNAME));
            friendsData.setSecondname(json.getString(FriendsHandler.TAG_SECONDNAME));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        listFriends.add(friendsData);
    }
        //Notifying the adapter.
        adapter.notifyDataSetChanged();
    }
}

