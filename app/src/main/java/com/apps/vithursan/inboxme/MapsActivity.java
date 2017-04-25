package com.apps.vithursan.inboxme;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Geocoder geocoder;
    List<Address> addressList;
    EditText location;
    final String DATA_URL = "http://192.168.1.7/inboxme/getMarker.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        dataQry();
    }

    private void dataQry(){
        JsonArrayRequest request = new JsonArrayRequest(DATA_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        String  title = null, username = null, postcode = null;
                        for(int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject object = jsonArray.getJSONObject(i);
                                title = object.getString("title");
                                username = object.getString("username");
                                postcode = object.getString("post_code");
                            } catch (Exception e) {

                            }
                            try {
                                makeMarker(title,postcode,username);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        });
        SingletonRequestHandler.getInstance(this).addToRequestQueue(request);//singleton class
    }

    private void makeMarker(String Title, String location, String snippet) throws IOException {
        geocoder = new Geocoder(this);
        addressList = geocoder.getFromLocationName(location,1);
        Address address = addressList.get(0);
        LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng).title(Title).snippet(snippet));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
    }
    public void onSearch(View view) throws IOException {
        location = (EditText)findViewById(R.id.tvLocation);
        String val = location.getText().toString();

        geocoder = new Geocoder(this);
        addressList = geocoder.getFromLocationName(val,1);
        Address address = addressList.get(0);
        String locality = address.getLocality();
        LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng).title(locality));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

    }
}
