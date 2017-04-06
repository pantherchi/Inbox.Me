package com.apps.vithursan.inboxme;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class ComposeFriendsFragment extends Fragment {
    private Toolbar myChatToolbar;


    public ComposeFriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_compose_friends, container, false);
        Toolbar myChatToolbar = (Toolbar)view.findViewById(R.id.my_chat_toolbar);
        myChatToolbar.inflateMenu(R.menu.cancel_menu);
        myChatToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_cancel:
                        ChatFragment fragment = new ChatFragment();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.currentContainer, fragment);
                        fragmentTransaction.commit();
                }
                return true;
            }
        });
        return view;
    }

}
