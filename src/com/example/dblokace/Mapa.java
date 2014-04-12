package com.example.dblokace;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.Geofence.Builder;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.*;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class Mapa extends Activity {

	DbHelper dbh;
	GoogleMap map;
	Geofence gf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dbh = new DbHelper(this);
		Cursor cursor = getEvents();
		
		map = ((MapFragment) getFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		// You can customize the marker image using images bundled with
		// your app, or dynamically generated bitmaps. 
		NastavMapu(cursor);	
		
		map.setOnMapLongClickListener(new OnMapLongClickListener() {
			
			@Override
			public void onMapLongClick(LatLng point) {
				/*
				gf = new Geofence.Builder()
				.setRequestId("1")
				.setCircularRegion(point.latitude, point.longitude, 50)
				.setExpirationDuration(1000*60*10)
				.build();*/
				
				map.addCircle(new CircleOptions()
				.center(point)
				.radius(50));
			}
			
		});
	}

	protected void NastavMapu(Cursor cursor){
		while (cursor.moveToNext()) {
			
				long id = cursor.getLong(0);
				long time = cursor.getLong(1);
				String battery = cursor.getString(2);
				String longitude = cursor.getString(3);				
				String latitude = cursor.getString(4);
				double Dlongitude = 0;
				double Dlatitude = 0;
				try{
					Dlongitude = Double.valueOf(longitude);
					Dlatitude = Double.valueOf(latitude);
				}
				catch(NumberFormatException e){					
				}
				
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, dd.MMM.yyyy");

		        Date resultdate = new Date(time);
		        String cas = ((sdf.format(resultdate)).toString());
				
				//if(Dlatitude!=0){
				PridejMarker(new LatLng(Dlatitude, Dlongitude), cas);	  
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(
					new LatLng(Dlatitude, Dlongitude), 16)); 					
				//}		
		}
	}

	private Cursor getEvents() {
		SQLiteDatabase db = dbh.getReadableDatabase();
		Cursor cursor = db.query(DbHelper.TABLE, null, null, null, null,
				null, null);

		startManagingCursor(cursor);
		return cursor;
	}

	protected void PridejMarker(LatLng pozice, String cas){
		map.addMarker(new MarkerOptions()
		.icon(BitmapDescriptorFactory.defaultMarker())
		.title(cas)
		.snippet(pozice.toString())
		.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
		.position(pozice));
	}
}