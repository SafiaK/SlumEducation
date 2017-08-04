package com.uog.umarkhan.slumeducation;

import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationDetails getValues;
    private Tracker tracker;

    int looper =0;
    String flag;
    ArrayList<LocationDetails> list=new ArrayList<LocationDetails>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        tracker=new Tracker(MapsActivity.this);
        tracker.getLocation();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

       list=(ArrayList<LocationDetails>)getIntent().getSerializableExtra("Key");

        flag=getIntent().getStringExtra("flag");
      // Toast.makeText(getApplicationContext(),getValues.getName(),Toast.LENGTH_LONG).show();


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.zoomTo(7));

        if(tracker.getLatitude()!=0)
        {  if(flag.equals("Admin") )
        {
            //Toast.makeText(this,tracker.getLatitude()+" "+tracker.getLongitude(),Toast.LENGTH_LONG).show();



            ArrayList<LatLng> latLngArrayList = new ArrayList<LatLng>();


            Iterator<LocationDetails> iter = list.iterator();

            while (iter.hasNext()) {

                getValues = iter.next();


    latLngArrayList.add(new LatLng(getValues.getLatitude(), getValues.getLongitude()));
    mMap.addMarker(new MarkerOptions().position(latLngArrayList.get(looper)).title(getValues.getName()));
    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngArrayList.get(looper)));

    looper++;


            }
        }
        else {

            LatLng sydney = new LatLng(tracker.getLatitude(), tracker.getLongitude());
            mMap.addMarker(new MarkerOptions().position(sydney).title("My Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));




        }
        }
        else {finish();}

        // Add a marker  and move the camera
//        LatLng sydney = new LatLng(32.1033836, 74.2087636);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("MyMarker"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));



    }
}
