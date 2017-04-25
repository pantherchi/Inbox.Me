package com.apps.vithursan.inboxme;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private Context context;

    //List to store all posts
    List<FriendsData> listFriends;

    //Constructor of this class
    public SearchAdapter(List<FriendsData>  listFriends, Context context){
        super();
        //Getting items searched
        this.listFriends = listFriends;
        this.context = context;
    }

    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_layout, parent, false);
        SearchAdapter.ViewHolder viewHolder = new SearchAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SearchAdapter.ViewHolder holder, int position) {
        //Getting the particular item from the list
        FriendsData FriendsData =  listFriends.get(position);

        //Showing data on the views
        holder.tvUsername.setText(SearchHandler.getInstance(context).getUsername());
    }

    @Override
    public int getItemCount() {
        return listFriends.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //Views
        public TextView tvUsername;

        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
        }
    }
}