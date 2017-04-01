package com.apps.vithursan.inboxme;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FeedFragment extends Fragment {
    TextView tvFirstname;
    TextView tvSecondname;
    TextView tvEmail;
    TextView tvGender;
    TextView tvDOB;


    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        tvFirstname = (TextView) view.findViewById(R.id.tvFirstname);
        tvSecondname = (TextView) view.findViewById(R.id.tvSecondname);
        tvEmail = (TextView)view.findViewById(R.id.tvEmail);
        tvGender = (TextView)view.findViewById(R.id.tvGender);
        tvDOB = (TextView)view.findViewById(R.id.tvDOB);


        fetch(tvFirstname, tvSecondname, tvEmail, tvGender, tvDOB);



        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Comfortaa/Comfortaa-Bold.ttf");
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setTypeface(font);
        return view;

    }
    public void fetch(TextView tvFirstname ,TextView tvSecondname, TextView tvEmail, TextView tvGender, TextView tvDOB){
        tvFirstname.setText(LoginHandler.getInstance(getActivity()).getFirstname());
        tvSecondname.setText(LoginHandler.getInstance(getActivity()).getSecondname());
        tvEmail.setText(LoginHandler.getInstance(getActivity()).getEmail());
        tvGender.setText(LoginHandler.getInstance(getActivity()).getGender());
        tvDOB.setText(LoginHandler.getInstance(getActivity()).getDOB());
    }
}
