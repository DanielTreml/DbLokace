package com.example.dblokace;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBLSluzba extends IntentService{

	private Lokace lokace;
	public SharedPreferences nastaveni;
	private DbHelper dbh;
	
	public DBLSluzba() {
		super("DBLSluzba");
	}

	//pozor! bìží døíve než onStartCommand()
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d("Sluzba", "onHandleIntent()");
		while(true){
			//if(prubeh){
	    		Log.i("Sluzba", "cyklus");
				try{
					String[] poloha = lokace.SoucasnaPoloha();
					addToDatabase(poloha);
					Log.i("Sluzba", "onHandleIntent()-"+poloha[0]+"-"+poloha[1]);
					Thread.sleep(1*1000*60);
				}
				catch(Exception e){
					Log.e("Sluzba","onHandleIntent()-Error");
				}			
			//}
		}
	}


	  private void addToDatabase(String[] poloha) {
		    SQLiteDatabase db = dbh.getWritableDatabase();
		    ContentValues values = new ContentValues();
		    values.put(DbHelper.TIME, System.currentTimeMillis());
		    values.put(DbHelper.BATTERY, "xxx");
		    values.put(DbHelper.LONGITUDE, poloha[0]);
		    values.put(DbHelper.LATITUDE, poloha[1]);
		    db.insert(DbHelper.TABLE, null, values);
		    Log.i("Databaze","vložení dat:"+poloha[0]+"-"+poloha[1]);
		  }
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) { 
		Log.d("Sluzba", "onStartCommand()");
		super.onStartCommand(intent, startId, startId);
		nastaveni = getSharedPreferences("com.example.dblokace", MODE_PRIVATE);
		dbh = new DbHelper(this);
		
		lokace = new Lokace(this);
		String[] s={"12.154","125.0524"};
		//addToDatabase(s);
		
	    //Intent inten = new Intent(this, Mapa.class);
	    //PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
	    //lokace.setGeofence(new LatLng(0, 0), 50, 1000*60*10, pIntent);
		//adapter = new DBAdapter();
		//adapter.init(this);
		/*
		prubeh=true;
		
		if(nastaveni.getBoolean("CheckHodnota", false)){
			Log.i("Sluzba", "onStartCommand()-return start_sticky");
			return Service.START_STICKY; //nezapne se znovu nejspíše
		}
		else {
			Log.i("Sluzba", "onStartCommand()-return start_not_sticky");
			return Service.START_NOT_STICKY;
		}
		*/
		return Service.START_STICKY;
	}
}
