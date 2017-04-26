package com.apps.vithursan.inboxme;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {
    private Context context;
    List<Friends>  listFriends;

    public FriendsAdapter(List<Friends>  listFriends, Context context){
        super();
        this.listFriends = listFriends;
        this.context = context;
    }

    @Override
    public FriendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_layout, parent, false);
        FriendsAdapter.ViewHolder viewHolder = new FriendsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FriendsAdapter.ViewHolder holder, int position) {
        Friends friends =  listFriends.get(position);

        //Showing data on the views
        holder.tvFriendFirstname.setText(friends.getFirstname());
        holder.tvFriendSecondname.setText(friends.getSecondname());
        holder.tvUsername.setText("@"+friends.getUsername());
    }

    @Override
    public int getItemCount() {
        return listFriends.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvFriendFirstname, tvFriendSecondname, tvUsername;

        public ViewHolder(View itemView) {
            super(itemView);
            tvFriendFirstname = (TextView) itemView.findViewById(R.id.tvFriendFirstname);
            tvFriendSecondname = (TextView) itemView.findViewById(R.id.tvFriendSecondname);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
        }
    }
}
