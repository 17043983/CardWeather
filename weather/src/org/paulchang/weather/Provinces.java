package org.paulchang.weather;

import org.paulchang.database.mydatabase;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

import com.testin.commplatform.TestinAgent;

public class Provinces extends ListActivity {

	mydatabase dbhelper;
	Cursor cursor;
	String province[];
	ActionBar actionbar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		actionbar = getActionBar();
		actionbar.setHomeButtonEnabled(true);
		actionbar.setDisplayHomeAsUpEnabled(true);

		MyThread myhtread = new MyThread();
		myhtread.start();

		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				Bundle data = new Bundle();
				data.putInt("province", position + 1);
				Intent intent = new Intent(Provinces.this, City.class);
				intent.putExtras(data);
				startActivityForResult(intent, 2);
				// cursor.close();
			}
		});

	}

	public void provincename() {
		dbhelper = new mydatabase(this, "weather.db", 1);
		String sql = "select distinct province_name as _id from weathers";
		cursor = dbhelper.getReadableDatabase().rawQuery(sql, null);
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2, cursor,
				new String[] { "_id" }, new int[] { android.R.id.text1 },
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		Message msg = new Message();
		msg.what = 1;
		msg.obj = adapter;
		handler.sendMessage(msg);
	}

	class MyThread extends Thread {

		@Override
		public void run() {
			Looper.prepare();
			provincename();
		}

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				SimpleCursorAdapter adapter = (SimpleCursorAdapter) msg.obj;
				getListView().setAdapter(adapter);
				break;

			default:
				break;
			}
		};

	};

	/*
	 * public boolean onCreateOptionsMenu(Menu menu) {
	 * 
	 * // Inflate the menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.auto, menu); return true; }
	 */

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		switch (item.getItemId()) {
		case R.id.auto:
			System.out.println("������Զ���λ");
			break;
		case android.R.id.home:
			System.out.println("��Ҫ����MainActivity");
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	public void onActivityResult(int resultCode, int re, Intent data) {

		if (resultCode == 2 && data != null) {
			String province = data.getStringExtra("area_name");
			System.out.println("province" + province);
			setResult(0, data);
			this.finish();
		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (dbhelper != null) {
			dbhelper.close();
			System.out.println("�ҹ���û��");
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
