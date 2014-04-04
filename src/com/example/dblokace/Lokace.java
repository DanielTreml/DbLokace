package com.example.dblokace;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class Lokace {
    
    private final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 100; // in Meters
    private final long MINIMUM_TIME_BETWEEN_UPDATES = 10000; // in Milliseconds
    private Context ctx;
    
    private LocationManager locationManager;
    

    public Lokace(Context ctx){
		Log.d("Lokace", "Lokace()");
    	this.ctx=ctx;
    	NastavLManager();
    }
    
    private void NastavLManager(){
		Log.d("Lokace", "NastavManager()");
    	locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
    	locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 
                MINIMUM_TIME_BETWEEN_UPDATES, 
                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                new MyLocationListener()
        );
    }
    
    public String[] SoucasnaPoloha() {
		Log.d("Lokace", "SoucasnaPoloha()");
		String[]pole= new String[2];
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
    		Log.i("Lokace", "není null");
            String message = String.format(
                    "\n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude()
            );
			Log.i("Lokace", "SoucasnaPoloha()-return"+message);
			
			pole[0]=Double.toString(location.getLongitude());
			pole[1]=Double.toString(location.getLatitude());
            return pole;
        }
		Log.i("Lokace", "SoucasnaPoloha()-return xxx");
		pole[0]="";
		pole[1]="";
        return pole;

    }   

    private class MyLocationListener implements LocationListener {

        @Override
		public void onLocationChanged(Location location) {
        }

        @Override
		public void onStatusChanged(String s, int i, Bundle b) {
        }

        @Override
		public void onProviderDisabled(String s) {
        }

        @Override
		public void onProviderEnabled(String s) {
        }

    }
    
}