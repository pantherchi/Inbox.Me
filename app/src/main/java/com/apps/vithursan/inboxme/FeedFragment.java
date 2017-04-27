package com.apps.vithursan.inboxme;

import android.app.ProgressDialog;
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


//This is set because in order to have a scroll listener the minimum of marshmallow android version is needed.
@RequiresApi(api = Build.VERSION_CODES.M)
public class FeedFragment extends Fragment implements RecyclerView.OnScrollChangeListener{
    private List<Post> listPost;
    private RecyclerView recyclerView;
    protected LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private RecyclerView.Adapter adapter;
    private ProgressDialog progressDialog;

    //Counter that will be assigned to the post in the php script
    private int count = 0;

    //Location of the php script for adding a new post.
//    final String PHP_NEW_POST = "http://192.168.1.7/inboxme/newPost.php";
//    final String PHP_NEW_POST = "https://inboxme.000webhostapp.com/inboxme/newPost.php";
//    public static final String PHP_URL_DATA = "http://192.168.1.7/inboxme/usersPost.php?post=";
    //Location of the php script for getting the post from the database post table.
//    public static final String PHP_URL_DATA = "http://192.168.1.7/inboxme/getPost.php?post=";
//    public static final String PHP_URL_DATA = "https://inboxme.000webhostapp.com/inboxme/getPost.php?post=";

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_feed, container, false);

        //initialising the recycler view in the feed_fragment.xml
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);

        //layout of the recycler view is set to linear so that it is in a linear fashion just like ordinary posts.
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        //new array list is initialised in the view so that it can be parsed with json object values
        listPost = new ArrayList<>();

        //This method is the one which triggers the request and adds one to the count ($post in php) so that the next data can be fetched.
        getData();

        //Setting up the scroll change listener
        recyclerView.setOnScrollChangeListener(this);

        //constructing a new adapter with the json parsed list item and the context of this fragment
        adapter = new PostAdapter(listPost, getContext());

        //Adding adapter to recycler view
        recyclerView.setAdapter(adapter);

        //Floating button that triggers the new post method and inserts the user input to the posts table in the database
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPost();
            }
        });
        //The user can reload the feed by swiping the feed cards downwards
        //Initialising
        mySwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefresh);
        //setting the listener
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        //a toast is prompted
                        Toast.makeText(getContext(), "Refreshing...", Toast.LENGTH_SHORT).show();
                        //a method is triggered
                        onSwipeRefresh();
                        //and the mySwipeRefreshLayout is set to hide once its done
                        mySwipeRefreshLayout.setRefreshing(false);

                    }
                }
        );

        progressDialog = new ProgressDialog(getContext());
        return view;
    }

    //Method which will be triggered onRefresh() from the SwipeRefreshLayout
    //this method is just detaching and attaching the fragment so that the onCreateView() can be ran again.
    private void onSwipeRefresh() {
        FeedFragment fragment = new FeedFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.currentContainer, fragment);
        fragmentTransaction.detach(fragment).attach(fragment).commit();
    }

    //Method that triggers an alert dialog.
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

        progressDialog.setMessage("Posting...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Scripts.O_NEW_POST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
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
                progressDialog.hide();
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
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Scripts.O_USERS_POST + String.valueOf(requestCount) + "& id=" + id ,
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
        //this method calls the
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
                post.setId(json.getString("id"));
                post.setUsername(json.getString("username"));
                post.setTimestamp(json.getString("timestamp"));
                post.setTitle(json.getString("title"));
                post.setContent(json.getString("content"));
                post.setLikes(json.getString("likes"));

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
