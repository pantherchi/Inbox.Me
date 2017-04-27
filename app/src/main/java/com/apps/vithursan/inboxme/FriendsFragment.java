package com.apps.vithursan.inboxme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment{
    private List<Friends> listFriends;
    private RecyclerView recyclerView;
    protected LinearLayoutManager linearLayoutManager;
    private RecyclerView.Adapter adapter;
    private SwipeRefreshLayout mySwipeRefreshLayout;

    //Location of php scripts.
//    final String DATA_URL = "http://192.168.1.7/inboxme/getFriends.php?id=";
//    final String DATA_URL = "https://inboxme.000webhostapp.com/inboxme/getFriends.php?id=";

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
                        startActivity(new Intent(getActivity(), SearchFriendsActivity.class));
                }
                return true;
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        listFriends = new ArrayList<>();

        getData();

        adapter = new FriendsAdapter(listFriends, getContext());
        recyclerView.setAdapter(adapter);

        mySwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Toast.makeText(getContext(), "Refreshing...", Toast.LENGTH_SHORT).show();
                        onSwipeRefresh();
                        mySwipeRefreshLayout.setRefreshing(false);

                    }
                }
        );

        return view;
    }
    private void onSwipeRefresh() {
        FriendsFragment fragment = new FriendsFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.currentContainer, fragment);
        fragmentTransaction.detach(fragment).attach(fragment).commit();
    }

    private void getData() {
        final int id = LoginHandler.getInstance(getContext()).getUserID();
        SingletonRequestHandler.getInstance(getContext()).addToRequestQueue(getDataFromServer(id));
    }

    private JsonArrayRequest getDataFromServer(int id) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Scripts.O_GET_FRIENDS + String.valueOf(id),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Calling method parseData to parse each JSON object data into the Friends object/data model from JSON Array response.
                        parseData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Volley Error
                    }
                });
        return jsonArrayRequest;
    }

    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            //Initialising Friends object.
            Friends friends = new Friends();
            //Json object is created
            JSONObject json;
            try {
                //Getting JSON object from the array.
                json = array.getJSONObject(i);

                //Adding each JSON object data to the Friends object.
                friends.setUsername(json.getString("username"));
                friends.setFirstname(json.getString("firstname"));
                friends.setSecondname(json.getString("secondname"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            listFriends.add(friends);
        }
//      Notifying the adapter.
        adapter.notifyDataSetChanged();
    }
}

