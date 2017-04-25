package com.apps.vithursan.inboxme;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@RequiresApi(api = Build.VERSION_CODES.M)
public class FeedFragment extends Fragment implements RecyclerView.OnScrollChangeListener{
    private List<Post> listPost;
    private RecyclerView recyclerView;
    protected LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private RecyclerView.Adapter adapter;

    private int count = 0;

    final String PHP_NEW_POST = "http://192.168.1.7/inboxme/newPost.php";
    public static final String PHP_URL_DATA = "http://192.168.1.7/inboxme/test21.php?post=";

    public FeedFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_feed, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        listPost = new ArrayList<>();

        getData();

        recyclerView.setOnScrollChangeListener(this);

        adapter = new PostAdapter(listPost, getContext());

        //Adding adapter to recycler view
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPost();
            }
        });

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
        FeedFragment fragment = new FeedFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.currentContainer, fragment);
        fragmentTransaction.detach(fragment).attach(fragment).commit();
    }

    private void newPost() {
        final String user_name = String.valueOf(LoginHandler.getInstance(getContext()).getUsername());

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View postView = getActivity().getLayoutInflater().inflate(R.layout.new_post_dialog, null);

        final TextView username = (TextView) postView.findViewById(R.id.layoutUsername);
        username.setText(user_name);

        final EditText title = (EditText)postView.findViewById(R.id.layoutPostTitle);
        final EditText content = (EditText)postView.findViewById(R.id.layoutContent);

        builder.setPositiveButton("Post", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String postTitle = title.getText().toString();
                String postContent = content.getText().toString();

                updateDatabase(postTitle, postContent);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setView(postView);
        builder.show();
    }

    private void updateDatabase(final String title, final String content){
        final String id = String.valueOf(LoginHandler.getInstance(getContext()).getUserID());
        final String user_name = LoginHandler.getInstance(getContext()).getUsername();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PHP_NEW_POST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")){
                                Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                //add the post to the array that generates the card
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", id);
                params.put("username", user_name);
                params.put("post_title", title);
                params.put("post_content", content);
                return params;
            }
        };
        SingletonRequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private JsonArrayRequest getUsersPosts(int requestCount) {
        final String id = String.valueOf(LoginHandler.getInstance(getContext()).getUserID());
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(PHP_URL_DATA + String.valueOf(requestCount) + "& id=" + id ,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Calling method parseData to parse the json response
                        parseData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //End of the feed
                        Toast.makeText(getContext(), "No more posts to show", Toast.LENGTH_SHORT).show();
                    }
                });

        return jsonArrayRequest;
    }

    private void getData() {
        SingletonRequestHandler.getInstance(getContext()).addToRequestQueue(getUsersPosts(count));
        count++;
    }

    //Parsing the json array
    private void parseData(JSONArray array) {
        Log.v("1","IN PARSE DATA");
        for (int i = 0; i < array.length(); i++) {
            Post post = new Post();
            try {
                //Getting json object from the array and passing each json data to the post object.
                JSONObject json = array.getJSONObject(i);
                Log.v("1","SETTING STRINGS");
                post.setId(json.getString(PostHandler.TAG_POST_ID));
                post.setUsername(json.getString(PostHandler.TAG_USERNAME));
                post.setTimestamp(json.getString(PostHandler.TAG_TIME));
                post.setTitle(json.getString(PostHandler.TAG_TITLE));
                post.setContent(json.getString(PostHandler.TAG_CONTENT));
                post.setLikes(json.getString(PostHandler.TAG_LIKES));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            listPost.add(post);
        }
        //Notify the PostAdapter that the data set has changed.
        adapter.notifyDataSetChanged();
    }

    //Checking if all the recycler view posts are pushed
    private boolean lastItem(RecyclerView recyclerView) {
        //Check if there is items in the recycler view adapter.
        if (recyclerView.getAdapter().getItemCount() != 0) {
            //Returns the adapter position of the last fully visible view.
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            //Checks if the last item is not equals to -1.
            //getAdapter - Retrieves the previously set adapter or null if no adapter is set.
            //getItemCount() - Returns the total number of items in the data set held by the adapter.
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION){
//                Log.d("position","NO_POSITION");
//                String c = String.valueOf(lastVisibleItemPosition);
//                Log.d("position",c);
//                String r = String.valueOf(recyclerView.getAdapter().getItemCount());
//                Log.d("position",r);
                if (lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1){
                    Log.e("position","getItemCount()");
                    return true;
                }
            }
                /*
                This is true when:
                    1. Recycler views adapter not equals zero meaning that when the getData() method is called in the onCreate() the array  is inflated with no data.
                    2. LastCompletelyVisibleItemPosition is not equals to -1 and that the previously set adapter count is 1 less than the last position as it starts 0.
                 */
//                return true;
        }
        return false;
    }

    @Override
    public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //When user reaches the end of visible cards the getData() method is called which will load more posts.
        if (lastItem(recyclerView)) {
//            String x = String.valueOf(recyclerView.getAdapter().getItemCount());
//            Log.d("pos",x);
            getData();
        }
    }
}
