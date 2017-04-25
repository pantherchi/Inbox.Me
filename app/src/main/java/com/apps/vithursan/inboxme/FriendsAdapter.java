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
    List<FriendsData>  listFriends;

    public FriendsAdapter(List<FriendsData>  listFriends, Context context){
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
        FriendsData friendsData =  listFriends.get(position);

        //Showing data on the views
        holder.tvFriendFirstname.setText(friendsData.getFirstname());
        holder.tvFriendSecondname.setText(friendsData.getSecondname());
        holder.tvUsername.setText("@"+friendsData.getUsername());
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
