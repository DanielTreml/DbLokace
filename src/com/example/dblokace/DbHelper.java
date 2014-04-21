package com.example.dblokace;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/** Helper to the database, manages versions and creation */
public class DbHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "locationDatabase.db";
	private static final int DATABASE_VERSION = 32;

	// Tables names
	public static final String TABLE = "location";
	public static final String TABLE2 = "settings";

	// Columns
	public static final String TIME = "time";
	public static final String BATTERY = "battery";
	public static final String LONGITUDE = "longitude";
	public static final String LATITUDE = "latitude";
	public static final String SPEED = "speed";
	public static final String PROVIDER = "provider";
	public static final String DISTANCE = "distance";
	

	public static final String MINTIME = "mintime";
	public static final String MINDISTANCE = "mindistance";

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table " + TABLE + " ( " + BaseColumns._ID
				+ " integer primary key autoincrement, " + TIME + " integer, "
				+ BATTERY + " integer, " + SPEED + " real, " + PROVIDER + " integer, " + DISTANCE + " real, " + LONGITUDE + " real, " + LATITUDE + " real);";
		//Log.i("Databáze","vytvoreni db: "+sql);
		db.execSQL(sql);
		
		String sql2 = "create table " + TABLE2 + " ( " + BaseColumns._ID
				+ " integer primary key autoincrement, " + MINTIME + " real, " + MINDISTANCE + " real);";
		Log.i("Databáze","vytvoreni db: "+sql2);
		db.execSQL(sql2);
		
		//Log.i("Databáze","vytvoreni db: "+sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("Databáze","onUpgrade()-old: "+oldVersion+"new: "+newVersion);
		
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE2);
        onCreate(db);
		//db.close();
	}

}
