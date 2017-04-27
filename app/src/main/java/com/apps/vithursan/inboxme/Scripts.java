package com.apps.vithursan.inboxme;

public class Scripts {

    //here is the local root url which contains the php files that helps query the database locally.
//    private static final String LOCAL_ROOT_URL = "http://192.168.1.7/inboxme/";


    //here is the online root url which contains the php files that helps query the database online.
    private static final String ONLINE_ROOT_URL = "https://inboxme.000webhostapp.com/inboxme/";

    /*
    //Local scripts.
    public static final String L_USER_REGISTER =  LOCAL_ROOT_URL+"registerUser.php";
    public static final String L_USER_LOGIN =  LOCAL_ROOT_URL+"userLogin.php";
    public static final String L_DATA_UPDATE = LOCAL_ROOT_URL+"userAllUpdate.php";
    public static final String L_NEW_POST = LOCAL_ROOT_URL+"newPost.php";
    public static final String L_GET_POST = LOCAL_ROOT_URL+"getPost.php?post=";
    public static final String L_GET_FRIENDS = LOCAL_ROOT_URL+"getFriends.php?id=";
    public static final String L_ADD_FRIENDS = LOCAL_ROOT_URL+"addFriends.php";
    public static final String L_USER_FRIENDS = LOCAL_ROOT_URL+"userFriends.php";
    public static final String L_GET_MARKER = LOCAL_ROOT_URL+"getMarker.php";
    public static final String L_SET_LIKES = LOCAL_ROOT_URL+"setLikes.php";
     */

    //Online scripts - these will query the online database.
    public static final String O_USER_REGISTER =  ONLINE_ROOT_URL+"registerUser.php";
    public static final String O_USER_LOGIN =  ONLINE_ROOT_URL+"userLogin.php";
    public static final String O_DATA_UPDATE = ONLINE_ROOT_URL+"userAllUpdate.php";
    public static final String O_NEW_POST = ONLINE_ROOT_URL+"newPost.php";
//    public static final String O_GET_POST = ONLINE_ROOT_URL+"getPost.php?post=";
    public static final String O_USERS_POST = ONLINE_ROOT_URL+"usersPost.php?post=";
    public static final String O_GET_FRIENDS = ONLINE_ROOT_URL+"getFriends.php?id=";
    public static final String O_ADD_FRIENDS = ONLINE_ROOT_URL+"addFriends.php";
    public static final String O_USER_FRIENDS = ONLINE_ROOT_URL+"userFriends.php";
    public static final String O_GET_MARKER = ONLINE_ROOT_URL+"getMarker.php";
    public static final String O_SET_LIKES = ONLINE_ROOT_URL+"setLikes.php";

    /*
    * Files that uses PHP scripts
    * DataUpdate.java - to update user information
    * FeedFragment - to post and get new posts from the database
    * Login Activity - to log the user into the database
    * MapsActivity - to get information about each marker (Title, User & Location)
    * PostAdapter - to set like when the user clicks on the button on the post
    * Register Activity -  to insert information about the user
    * SearchFriendsActivity - to load up the information about their current friends and to allow them to query such as add and remove
    *
    * ALL OF THE FILES ABOVE SHOULD BE SET WITH PROGRESS BAR OR DIALOG IN ORDER TO LET THE USER KNOW THAT THE SCRIPT IN RUNNING AND IT TAKES TIME DUE TO INTERNET CONNECTION
    *
    */


}
