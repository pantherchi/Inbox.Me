package com.apps.vithursan.inboxme;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class ProfileFragment extends Fragment {

    TextView tvUsername, tvFirstname, tvSecondname,tvEmail,tvGender,tvDOB;
    Button btnUpdateInfo;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        //Instantiating all the components beforehand in the onCreate method.
        tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        tvFirstname = (TextView) view.findViewById(R.id.tvFirstname);
        tvSecondname = (TextView) view.findViewById(R.id.tvSecondname);
        tvEmail = (TextView)view.findViewById(R.id.tvEmail);
        tvGender = (TextView)view.findViewById(R.id.tvGender);
        tvDOB = (TextView)view.findViewById(R.id.tvDOB);
        btnUpdateInfo = (Button)view.findViewById(R.id.btnUpdateInfo);

        //this method fetches the user data from the private shared preference file and assigns it to the text views
        fetch(tvUsername,tvFirstname, tvSecondname, tvEmail, tvGender, tvDOB);

        btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //starts the DataUpdate activity which can be used to update the users information.
                startActivity(new Intent(getActivity(), DataUpdate.class));
            }
        });
        return view;
    }
    public void fetch(TextView tvUsername, TextView tvFirstname ,TextView tvSecondname, TextView tvEmail, TextView tvGender, TextView tvDOB){
        //On each component the objects getter method is called.
        tvUsername.setText(LoginHandler.getInstance(getActivity()).getUsername());
        tvFirstname.setText(LoginHandler.getInstance(getActivity()).getFirstname());
        tvSecondname.setText(LoginHandler.getInstance(getActivity()).getSecondname());
        tvEmail.setText(LoginHandler.getInstance(getActivity()).getEmail());
        tvGender.setText(LoginHandler.getInstance(getActivity()).getGender());
        tvDOB.setText(LoginHandler.getInstance(getActivity()).getDOB());
    }

}
