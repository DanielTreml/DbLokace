package com.example.dblokace;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

	public SharedPreferences nastaveni;
	private DbHelper dbh;
	//private TextView tv;
	private ListView lv;
	private String[] values;

	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		Log.d("Aktivita", "onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newlayout);
		//LocationHelper l = new LocationHelper(this);
		//l.RunHelper();

		dbh = new DbHelper(this);

		startService(new Intent(this, LocationHelper.class));
		//nastaveni = getSharedPreferences("com.example.dblokace", MODE_PRIVATE);		

		Switch switchSluzba = ((Switch)findViewById(R.id.switchSluzba));
		switchSluzba.setChecked(BeziSluzba());
		switchSluzba.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(BeziSluzba()==false){
					SpustSluzbu();
				}else{
					Intent i = new Intent(MainActivity.this, LocationHelper.class);
					MainActivity.this.stopService(i);
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

		//tv = ((TextView)findViewById(R.id.textView1));
		lv = ((ListView)findViewById(R.id.listView1));

		Cursor cursor1 = getEvents(dbh.TABLE);
		Cursor cursor2 = getEvents(dbh.TABLE2);

		values=new String[cursor1.getCount()];

		showEvents(cursor1, cursor2);		
		cursor1.close();
		cursor2.close();

		dbh.close();
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, android.R.id.text1, values);			


		if(values.length>0){
			Log.i("values",""+values.length);
			lv.setAdapter(adapter);
		}
		
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				MapaStartBod(arg3);
				return false;
			}

		});

	} 

	private void MapaStart(){
		Intent intent = new Intent(this, Mapa.class);
		startActivity(intent);
	}
	private void MapaStartBod(long id){
		Intent intent = new Intent(this, Mapa.class);
		//Toast.makeText(getApplicationContext(), "id:"+(id+1), Toast.LENGTH_LONG).show();
		intent.putExtra("id", id+1);
		startActivity(intent);
	}

	private Cursor getEvents(String tableName) {/*
		for (String str : strArray) {
			Cursor cursor = db.rawQuery("select count(*) from " + TABLE_TASKS + " where priority='"
					+ str + "'", null);
			if (cursor != null && cursor.moveToFirst()) {

				int count = cursor.getInt(0);
				priorities[i] = count;
				i++;
				cursor.close();
			}

		}
		db.close();

	 */

		SQLiteDatabase db = dbh.getReadableDatabase();
		Cursor cursor = db.query(tableName, null, null, null, null, null, null);//.rawQuery("", null);
		

		/*
		Cursor cursor = db.query(DbHelper.TABLE, null, null, null, null,
				null, null);
		startManagingCursor(cursor);*/
		return cursor;
	}

	private void showEvents(Cursor cursor1, Cursor cursor2) {
		int x = 0;
		while (cursor1.moveToNext()&&cursor2.moveToNext()) {
			long id = cursor1.getLong(0);
			long time = cursor1.getLong(1);
			int battery = cursor1.getInt(2);
			int provider = cursor1.getInt(4);
			double speed = cursor1.getDouble(3);
			double distance = cursor1.getDouble(5);
			double longitude = cursor1.getDouble(6);
			double latitude = cursor1.getDouble(7);

			double minTime = cursor2.getDouble(1);
			double minDistance = cursor2.getDouble(2);

			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS, dd.MMM.yyyy");
			Date resultdate = new Date(time);
			String cas = ((sdf.format(resultdate)).toString());

			values[x]="Èas: "+cas+"\nLatitude: "+latitude+"\nLongitude: "+longitude+"\nSpeed: "+speed+"\nDistance: "+distance+"\nBattery:"+battery+"%"+"\nProvider: "+provider+"\nMinTime: "+minTime+"\nMinDistance: "+minDistance;	    	  

			x++;
		}

	}


	public void SpustSluzbu(){/*
		Log.d("Aktivita", "SpustSluzbu()");		
		Intent intent = new Intent(this, DBLSluzba.class); 
		startService(intent); 	*/

		//startService(new Intent(this, LocationHelper.class));
	}

	//pozor sluzba beží pouze pokud onHandleIntent- while(true)
	private boolean BeziSluzba() {
		Log.d("Aktivita", "BeziSluzba()");
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			//Log.i("Aktivita", "BeziSluzba()"+service.service.getClassName()); výpis služeb
			if (LocationManager.class.getName().equals(service.service.getClassName())) {
				Log.i("Aktivita", "BeziSluzba()-return true");
				return true;
			}
		}
		Log.i("Aktivita", "BeziSluzba()-return false");
		return false;
	}
}
