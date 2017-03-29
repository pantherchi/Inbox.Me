package com.apps.vithursan.inboxme;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FeedFragment extends Fragment {


    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Comfortaa/Comfortaa-Bold.ttf");
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setTypeface(font);

        return view;
    }

}
