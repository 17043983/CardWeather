package org.paulchang.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class mydatabase extends SQLiteOpenHelper {

	public mydatabase(Context context, String name, int version) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}

	static String Database = "/data/data/org.paulchang.weather/databases/weather.db";
	static String Assets_Name = "weather.db";
	static String Save_Path = "/data/data/org.paulchang.weather/databases/";
	final static String Create_Table = "create table city (_id integer primary key autoincrement,city_name,city)";

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("我要建表了");
		db.execSQL(Create_Table);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public static void insert(Context context, mydatabase db,
			String district) {
		db = new mydatabase(context, "weather.db", 1);
		if (distinct(context, db, district)) {
		} else {
			db.getWritableDatabase().execSQL("insert into city values(null,?)",
					new String[] { district });
			Toast.makeText(context, "城市添加成功", Toast.LENGTH_LONG).show();
		}
	}

	public static boolean distinct(Context context, mydatabase db,
			String district) {
		db = new mydatabase(context, "weather.db", 1);
		String sql = "select city_name as _id from city where city_name=?";
		Cursor c2 = db.getWritableDatabase().rawQuery(sql,
				new String[] { district });
		if (c2.moveToFirst()) {
			Toast.makeText(context, "�����Ѵ���", Toast.LENGTH_LONG).show();
			String c2city = c2.getString(0);
			System.out.println("c2city" + c2city);
			return true;
		} else {
			return false;
		}
	}



	public static void AssetsToDatabase(Context c) { // ��assets������ݿ⵽databases�ļ���

		File f = new File(Save_Path);

		if (!f.exists()) {
			f.mkdir();
		}

		if (!new File(Database).exists()) {
			try {
				InputStream in = c.getAssets().open(Assets_Name);
				System.out.println(2 + "");
				FileOutputStream out = new FileOutputStream(Database);
				byte[] b = new byte[1024];
				int length = 0;
				while ((length = in.read(b)) > 0) {
					out.write(b, 0, length);
				}
				out.flush();
				out.close();
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
