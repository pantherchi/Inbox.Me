package com.apps.vithursan.inboxme;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

//    private static final String PHP_URL = "http://192.168.1.7/inboxme/setLikes.php" ;
//    private static final String PHP_URL = "https://inboxme.000webhostapp.com/inboxme/setLikes.php" ;

    private Context context;

    //List to store all posts
    List<Post> posts;

    //Constructor of this class
    public PostAdapter(List<Post> posts, Context context){
        super();
        //Getting all posts
        this.posts = posts;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Getting the particular item from the list
        Post post =  posts.get(position);

        //Showing data on the views
        holder.username.setText(post.getUsername());
        holder.date.setText(post.getTimestamp());
        holder.postTitle.setText(post.getTitle());
        holder.postContent.setText(post.getContent());
        holder.postLikes.setText(post.getLikes());
        holder.clickListeners(position);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //Views
        public TextView username;
        public TextView date;
        public TextView postTitle;
        public TextView postContent;
        public TextView postLikes;
        public ImageButton imageButton;

        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.username);
            date = (TextView) itemView.findViewById(R.id.date);
            postTitle = (TextView) itemView.findViewById(R.id.postTitle);
            postContent = (TextView) itemView.findViewById(R.id.postContent);
            postLikes = (TextView) itemView.findViewById(R.id.postLikes);
            imageButton = (ImageButton) itemView.findViewById(R.id.imageButton);
        }

        public void clickListeners(final int position) {
            Post post = posts.get(position);
            final String post_id = post.getId();
            final String user_id = String.valueOf(LoginHandler.getInstance(context).getUserID());
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageButton.setBackgroundColor(0);
                    Toast.makeText(context, post_id, Toast.LENGTH_SHORT).show();
                    setLikes(post_id, user_id);
                }
            });
        }

        private void setLikes(final String post_id, final String user_id) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Scripts.O_SET_LIKES,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("post_id", post_id);
                    params.put("user_id", user_id);
                    return params;
                }
            };
            SingletonRequestHandler.getInstance(context).addToRequestQueue(stringRequest);
        }
    }
}
