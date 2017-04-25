package com.apps.vithursan.inboxme;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MoreFragment extends Fragment {

    public MoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        if (container != null) {
            container.removeAllViews();
        }
        final String[] optionItems = {"Map"};

        ListView listView =(ListView)view.findViewById(R.id.moreNavListView);
        ListAdapter listViewAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                optionItems
        );
        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        long x = parent.getItemIdAtPosition(position);

                        if(x == 0){
                            startActivity(new Intent(getActivity(), MapsActivity.class));
                        }
                    }
                }
        );

        Button btnLogout = (Button)view.findViewById(R.id.btnLogOut);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginHandler.getInstance(getContext()).logout();
                startActivity(new Intent(getActivity(), InitialActivity.class));
            }
        });
        return view;
    }


}
