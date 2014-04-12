package com.example.dblokace;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/** Helper to the database, manages versions and creation */
public class DbHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "locationDB.db";
	private static final int DATABASE_VERSION = 14;

	// Table name
	public static final String TABLE = "location";

	// Columns
	public static final String TIME = "time";
	public static final String BATTERY = "battery";
	public static final String LONGITUDE = "longitude";
	public static final String LATITUDE = "latitude";

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table " + TABLE + "( " + BaseColumns._ID
				+ " integer primary key autoincrement, " + TIME + " integer, "
				+ BATTERY + " text, " + LONGITUDE + " text, " + LATITUDE + " text);";
		Log.i("Databáze","vytvoreni db: "+sql);
		db.execSQL(sql);
		Log.i("Databáze","vytvoreni db: "+sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("Databáze","onUpgrade()-old: "+oldVersion+"new: "+newVersion);
		
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
		
		/*
		if (oldVersion >= newVersion){
			Log.i("Databáze","onUpgrade()-if0");
			return;
		}
			

		String sql = null;
		if (oldVersion == 1){
			sql = "alter table " + TABLE + " add note text;";
			Log.i("Databáze","onUpgrade()-if1");
		}
		if (oldVersion == 2){
			sql = "";
			Log.i("Databáze","onUpgrade()-if2");
		}

		if (sql != null){
			Log.i("Databáze","onUpgrade-sql: "+sql);
			db.execSQL(sql);			
		}
		*/
	}

}
