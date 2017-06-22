package com.example.umarkhan.slumeducation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Umar Khan on 6/7/2017.
 */

public class Tracker implements LocationListener  {


    private final Context mContext;
    private LocationManager locationManager;
    private Location mLocation;

double Latitude,Longitude;
    boolean checkGPS,checkNEtwork;



    public Tracker(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public void onLocationChanged(Location location) {

            Latitude = location.getLatitude();
            Longitude = location.getLongitude();

    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    public boolean getLocation() {
        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);


        checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);


        if (checkGPS) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30, 0, this);

           if (locationManager != null) {
               mLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (mLocation != null) {
                    Latitude = mLocation.getLatitude();
                   Longitude = mLocation.getLongitude();

                }
                else {Toast.makeText(mContext,"Not Getting Location Go in Open Air",Toast.LENGTH_SHORT).show();
                    checkGPS=false;
                    }
                }

           else {Toast.makeText(mContext,"LOcation manager null",Toast.LENGTH_LONG).show();
           checkGPS =false;}
       }
       else {
           showSettingsAlert();
       }

        return checkGPS;
    }





    public void showSettingsAlert() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);


        alertDialog.setTitle("GPS Not Enabled");

        alertDialog.setMessage("Do you wants to turn On GPS");


        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);

            }
        });


        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        alertDialog.show();
    }




    public double getLongitude()
    {
        if(mLocation!=null){return Longitude;}
        else {return 0;}
    }

    public double getLatitude()
    {
        if(mLocation!=null)
        {return  Latitude;}
else {return 0;}
    }


public void NetworkLocation()
{
    checkNEtwork=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
   if(checkNEtwork)
   {Toast.makeText(mContext,"NETWORK",Toast.LENGTH_SHORT).show();}
       locationManager.requestLocationUpdates(
               LocationManager.NETWORK_PROVIDER,
               0,
               0, this);

       if (locationManager != null) {
           mLocation = locationManager
                   .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

       }

       if (mLocation != null) {
           Latitude = mLocation.getLatitude();
           Longitude = mLocation.getLongitude();
       }
       else {Toast.makeText(mContext,"Netwok Locaation null",Toast.LENGTH_LONG).show();}


}



    public void stopUsingGPS() {
        if (locationManager != null) {

            locationManager.removeUpdates(Tracker.this);
        }
    }

}
