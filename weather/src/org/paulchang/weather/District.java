package org.paulchang.weather;

import org.paulchang.database.mydatabase;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import com.testin.commplatform.TestinAgent;

public class District extends ListActivity {

	String city_id, area_id, city_name;
	mydatabase db;
	Cursor c, c1, c2;
	int area;
	String weather_id, area_name, c1city, c2city;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		city_id = intent.getExtras().getString("city_id");
		city_name = intent.getExtras().getString("city_name");
		district();

		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				area = position + 1;
				if (area < 10) {
					area_id = city_id + "0" + area;
					System.out.println("area_id001:" + area_id);

				} else {
					area_id = city_id + area;
					System.out.println("area_id002:" + area_id);

				}
				System.out.println("area_id:" + area_id);
				weather();

				TwoLineListItem a = (TwoLineListItem) view;
				TextView tv1 = (TextView) a.findViewById(android.R.id.text1);
				Toast.makeText(District.this, tv1.getText().toString(),
						Toast.LENGTH_LONG).show();

				boolean bool = distinct(area_name);
				insert(db.getWritableDatabase(), area_name);
				/*
				 * if (c1city.equals(c2city)) { Intent i = new
				 * Intent(District.this, MainActivity.class); startActivity(i);
				 * } else {
				 */

				Intent i = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("area_name", area_name);
				bundle.putString("city", city_name);
				bundle.putBoolean("boolean", bool);
				System.out.println("District" + bool);
				i.putExtras(bundle);
				setResult(0, i);
				finish();
				// }

			}
		});
	}

	public void district() {
		db = new mydatabase(this, "weather.db", 1);
		String sql = "select distinct area_name as _id from weathers where city_id=?";
		c = db.getReadableDatabase().rawQuery(sql, new String[] { city_id });
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2, c, new String[] { "_id" },
				new int[] { android.R.id.text1 },
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		int count = c.getCount();
		System.out.println("区县数目" + count);
		getListView().setAdapter(adapter);
	}

	public void weather() {
		String sql = "select weather_id as _id,area_name from weathers where area_id=?";
		c1 = db.getReadableDatabase().rawQuery(sql, new String[] { area_id });
		while (c1.moveToNext()) {
			weather_id = c1.getString(c1.getColumnIndex("_id"));
			area_name = c1.getString(c1.getColumnIndex("area_name"));
			c1city = c1.getString(1);
			System.out.println("c1city" + c1city);
			System.out.println(weather_id);
			System.out.println(area_name);
		}
	}

	public void insert(SQLiteDatabase sqldb, String district) {

		if (distinct(district)) {
		} else {
			sqldb.execSQL("insert into city values(null,?,?)", new String[] {
					district, city_name });
			Toast.makeText(this, "城市添加成功", Toast.LENGTH_LONG).show();
		}
	}

	public boolean distinct(String district) {
		String sql = "select city_name as _id from city where city_name=?";
		c2 = db.getWritableDatabase().rawQuery(sql, new String[] { district });
		if (c2.moveToFirst()) {
			Toast.makeText(this, "城市已存在", Toast.LENGTH_LONG).show();
			c2city = c2.getString(0);
			System.out.println("c2city" + c2city);
			return true;
		} else {
			return false;
		}
	}

	public void onResume() {
		super.onResume();
		TestinAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		TestinAgent.onPause(this);
	}
}
