package com.example.dblokace;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;


public class MainActivity extends Activity {

	public SharedPreferences nastaveni;
	private DbHelper dbh;
	private TextView tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		Log.d("Aktivita", "onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newlayout);

		nastaveni = getSharedPreferences("com.example.dblokace", MODE_PRIVATE);		

		Switch switchSluzba = ((Switch)findViewById(R.id.switchSluzba));
	switchSluzba.setChecked(BeziSluzba());
		switchSluzba.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(BeziSluzba()==false){
					SpustSluzbu();
				}else{
					//vypnout sluzbu
				}
			}
		});	
		
		Button mapa = ((Button)findViewById(R.id.buttonMapa));
		mapa.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MapaStart();
				//startActivity(new Intent(this, Mapa.class));
			}
		});
		
		tv = ((TextView)findViewById(R.id.textView1));
		
		dbh = new DbHelper(this);
	    Cursor cursor = getEvents();
	    showEvents(cursor);		
	    
		String TIME = "time";
		String BATTERY = "battery";
		String LONGITUDE = "longitude";
		String LATITUDE = "latitude";
		String sql = "create table " + "location" + "( " + BaseColumns._ID
				+ " integer primary key autoincrement, " + TIME + " integer, "
				+ BATTERY + " text, " + LONGITUDE + " text, " + LATITUDE + " text);";
		Log.i("Databáze","vytvoreni db: "+sql);
	} 

	private void MapaStart(){
	    Intent intent = new Intent(this, Mapa.class);
	    startActivity(intent);
	}
	
	private Cursor getEvents() {
	    SQLiteDatabase db = dbh.getReadableDatabase();
	    Cursor cursor = db.query(DbHelper.TABLE, null, null, null, null,
	        null, null);
	    
	    startManagingCursor(cursor);
	    return cursor;
	  }

	  private void showEvents(Cursor cursor) {
	    StringBuilder ret = new StringBuilder("Databáze:\n\n");
	    while (cursor.moveToNext()) {
	      long id = cursor.getLong(0);
	      long time = cursor.getLong(1);
	      String battery = cursor.getString(2);
	      String longitude = cursor.getString(3);
	      String latitude = cursor.getString(4);
	      ret.append(id + ": " + time + ": " + battery + ":" + longitude + ":" + latitude + "\n");
	      Log.i("Databáze","výpis");
	    }
	    Log.i("Databáze","výpis2");
	    tv.setText(ret);
	  }


	public void SpustSluzbu(){
		Log.d("Aktivita", "SpustSluzbu()");		
		Intent intent = new Intent(this, DBLSluzba.class); 
		startService(intent); 		
	}

//pozor sluzba beží pouze pokud onHandleIntent- while(true)
	private boolean BeziSluzba() {
		Log.d("Aktivita", "BeziSluzba()");
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			//Log.i("Aktivita", "BeziSluzba()"+service.service.getClassName()); výpis služeb
			if (DBLSluzba.class.getName().equals(service.service.getClassName())) {
				Log.i("Aktivita", "BeziSluzba()-return true");
				return true;
			}
		}
		Log.i("Aktivita", "BeziSluzba()-return false");
		return false;
	}
}
