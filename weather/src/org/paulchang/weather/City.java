package org.paulchang.weather;

import org.paulchang.database.mydatabase;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
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

public class City extends ListActivity {

	mydatabase db;
	int province;
	Cursor c;
	String province_id, city_id;
	int city;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		province = intent.getExtras().getInt("province");
		if (province < 10) {
			province_id = "0" + province;
		} else
			province_id = province + "";
		System.out.println("province_id:" + province_id);
		city();

		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				city = position + 1;
				TwoLineListItem a = (TwoLineListItem) view;
				TextView tv1 = (TextView) a.findViewById(android.R.id.text1);
				Toast.makeText(City.this, tv1.getText().toString(),
						Toast.LENGTH_LONG).show();
				System.out.println(getListView().getItemAtPosition(position)
						.toString());
				if (city < 10) {
					city_id = province_id + "0" + city;
				} else {
					city_id = province_id + city;
				}
				System.out.println("city_id:" + city_id);
				Bundle bundle = new Bundle();
				bundle.putString("city_id", city_id);
				bundle.putString("city_name", tv1.getText().toString());
				Intent i = new Intent(City.this, District.class);
				i.putExtras(bundle);
				startActivityForResult(i, 3);
			}
		});

	}

	public void city() {
		db = new mydatabase(this, "weather.db", 1);
		String sql = "select distinct city_name as _id from weathers where province_id=?";
		c = db.getReadableDatabase()
				.rawQuery(sql, new String[] { province_id });
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2, c, new String[] { "_id" },
				new int[] { android.R.id.text1 },
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		int count = c.getCount();
		System.out.println("城市数目" + count);
		getListView().setAdapter(adapter);
	}

	public void onActivityResult(int resultCode, int re, Intent data) {

		if (resultCode == 3 && data != null) {
			String city = data.getStringExtra("area_name");
			System.out.println("city" + city);
			setResult(0, data);
			this.finish();
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
