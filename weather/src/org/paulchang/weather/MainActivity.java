package org.paulchang.weather;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import org.paulchang.database.mydatabase;
import org.paulchang.weather.list.OnDismissCallback;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.jensdriller.libs.undobar.UndoBar;
import com.jensdriller.libs.undobar.UndoBar.Listener;
import com.testin.commplatform.TestinAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends ActionBarActivity implements Callback {

	private View tvView;
	private TextView mtv;
	private LocationClient locationclient;
	Cursor c;
	String city, weather_id, area_name, c_city, pm;
	Location app;
	Handler handler;
	LinkedList<HashMap<String, Object>> listdata;
	HashMap<String, Object> map, map1, mapall;
	list list;
	String[] results;
	private CardAdapter adapter;
	Cursor cursor;
	mydatabase db;
	boolean boolcity;
	AlarmManager am;
	Calendar calendar;
	SharedPreferences share;
	SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);
		TestinAgent.onError(this);
		TestinAgent.postBaseData(this);
		ShareSDK.initSDK(this);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		tvView = getLayoutInflater().inflate(R.layout.textview, null);
		mtv = (TextView) tvView.findViewById(R.id.tv);

		am = (AlarmManager) getSystemService(ALARM_SERVICE);
		calendar = Calendar.getInstance();

		locationclient = ((Location) getApplication()).locationclient;
		adapter = new CardAdapter(this);

		// mtv = (TextView) findViewById(R.id.tv);
		share = getSharedPreferences("alarm", MODE_PRIVATE);
		editor = share.edit();
		mtv.setText(share.getString("time", null));
		list = (list) findViewById(R.id.list);
		((Location) getApplication()).mtv = mtv;

		list.addHeaderView(tvView);
		handler = new FirstHandler();

		ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo net = manager.getActiveNetworkInfo();
		if (net == null || !net.isAvailable()) {
			Toast.makeText(this, "没有网络连接", Toast.LENGTH_SHORT).show();
			System.out.println("没网，你还说啥");
		} else {
			async sync = new async();
			sync.execute();
			Toast.makeText(this, "网络连接正常", Toast.LENGTH_SHORT).show();
			System.out.println("网络ok");

		}

		listdata = new LinkedList<HashMap<String, Object>>();
		list.setAdapter(adapter);

		list.setOnDismissCallback(new OnDismissCallback() {

			public void onDismiss(final int dismissPosition) {
				System.out.println("到底促发这个方法没");
				final String city_name = listdata.get(dismissPosition - 1)
						.get("city").toString();
				System.out.println("啥城市：" + city_name);
				listdata.remove(dismissPosition - 1);
				adapter.notifyDataSetChanged();
				db = new mydatabase(MainActivity.this, "weather.db", 1);
				String area = "select city as_id from city where city_name=?";
				Cursor cursor = db.getReadableDatabase().rawQuery(area,
						new String[] { city_name });
				while (cursor.moveToNext()) {
					city = cursor.getString(0);
					System.out.println("undo-->" + city);
				}
				new UndoBar.Builder(MainActivity.this)
						.setMessage("你删了" + city_name)
						.setListener(new Listener() {

							@Override
							public void onUndo(Parcelable token) {
								// TODO Auto-generated method stub

								String[] results = HttpJson.Http(city_name);
								map = new HashMap<String, Object>();
								map = HttpJson.Map(results);
								String pm = HttpJson.HttpPm(city);
								map1 = new HashMap<String, Object>();
								map1 = HttpJson.PMMap(pm);
								mapall = new HashMap<String, Object>();
								mapall.putAll(map);
								mapall.putAll(map1);
								listdata.add(dismissPosition - 1, mapall);
								adapter.notifyDataSetChanged();
							}

							@Override
							public void onHide() {
								// TODO Auto-generated method stub
								delete(city_name);
							}
						}).show();
			}
		});

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(MainActivity.this,
						(CharSequence) listdata.get((int) id).get("city"),
						Toast.LENGTH_SHORT).show();
				System.out.println("我点了" + listdata.get((int) id).get("city"));

			}
		});

		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) { // TODO Auto-generated method stub
				OnekeyShare oks = new OnekeyShare();
				oks.setNotification(R.drawable.ic_launcher,
						getString(R.string.app_name));
				oks.setTitle(getString(R.string.share));
				oks.setTitleUrl("http://sharesdk.cn");
				oks.setSite(getString(R.string.app_name));
				oks.setSiteUrl("http://sharesdk.cn");
				oks.setUrl("http://sharesdk.cn");
				oks.setComment(getString(R.string.share));
				oks.setText("大家好，我是张大璞大爷写的卡片天气，当前城市："
						+ listdata.get((int) id).get("city") + "\n" + "实时温度："
						+ listdata.get((int) id).get("temp") + "\n" + "天气："
						+ listdata.get((int) id).get("todaywea") + "\n风力："
						+ listdata.get((int) id).get("wind"));
				oks.show(MainActivity.this);
				return false;
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_new:
			mydatabase.AssetsToDatabase(this);
			Intent intent = new Intent(MainActivity.this, Provinces.class);
			startActivityForResult(intent, 1);
			mtv.setText("唯美食与爱不可辜负");
			break;
		case R.id.alarm:
			Calendar calendar = Calendar.getInstance();
			new TimePickerDialog(MainActivity.this, 0, // 绑定监听器
					new TimePickerDialog.OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker tp, int hourOfDay,
								int minute) {
							// 指定启动AlarmActivity组件
							Intent intent = new Intent(MainActivity.this,
									AlarmBroadCastReceiver.class);
							// 创建PendingIntent对象
							PendingIntent pi = PendingIntent.getBroadcast(
									MainActivity.this, 0, intent, 0);
							Calendar c = Calendar.getInstance();
							// 根据用户选择时间来设置Calendar对象
							c.set(Calendar.HOUR_OF_DAY, hourOfDay);
							c.set(Calendar.MINUTE, minute);
							if (System.currentTimeMillis() > c
									.getTimeInMillis()) {
								Toast.makeText(MainActivity.this, "明天叫你哟",
										Toast.LENGTH_SHORT).show();
								c.set(Calendar.DAY_OF_YEAR,
										c.get(Calendar.DAY_OF_YEAR) + 1);
							}
							// 设置AlarmManager将在Calendar对应的时间启动指定组件
							am.set(AlarmManager.RTC_WAKEUP,
									c.getTimeInMillis(), pi);
							Date data = new Date(c.getTimeInMillis());
							SimpleDateFormat format = new SimpleDateFormat(
									"HH:mm");
							mtv.setText("提醒时间：" + format.format(data));
							editor.putString("time", format.format(data));
							editor.commit();
							// 显示闹铃设置成功的提示信息
							Toast.makeText(MainActivity.this, "闹铃设置成功啦",
									Toast.LENGTH_SHORT).show();
						}
					}, calendar.get(Calendar.HOUR_OF_DAY),
					calendar.get(Calendar.MINUTE), true).show();
			break;
		case R.id.email:
			/*
			 * Intent i = new Intent(MainActivity.this, FbHome.class);
			 * sendBroadcast(i);
			 */
			FeedbackAgent agent = new FeedbackAgent(this);
			agent.startFeedbackActivity();
			System.out.println("点击了y");
			break;
		case R.id.auto:
			System.out.println("点击了setting");
			setLocationOption();
			locationclient.start();
			locationclient.requestLocation();
			MyThread thread = new MyThread();
			thread.start();

			break;
		}

		return super.onOptionsItemSelected(item);
	}

	public void onActivityResult(int resultCode, int re, Intent data) {
		String weather_id, area_name = null;
		if (resultCode == 1 && re == 0) {
			if (data == null) {
				System.out.println("为毛传不过来呢？");
			} else {
				Bundle bundle = data.getExtras();
				area_name = bundle.getString("area_name");
				city = bundle.getString("city");
				System.out.println("area_name--->" + area_name + "\n"
						+ "city---->" + city);
				boolean bool = bundle.getBoolean("boolean");
				System.out.println(bool);
				results = HttpJson.Http(area_name);
				map = new HashMap<String, Object>();
				map = HttpJson.Map(results);
				pm = HttpJson.HttpPm(city);
				map1 = new HashMap<String, Object>();
				map1 = HttpJson.PMMap(pm);
				mapall = new HashMap<String, Object>();
				mapall.putAll(map);
				mapall.putAll(map1);

				if (bool) {
				} else {
					listdata.add(mapall);
					adapter.notifyDataSetChanged();
				}
			}
		}
	}

	class MyThread extends Thread {

		@Override
		public void run() {
			Looper.prepare();

			try {
				sleep(2500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			city = ((Location) getApplication()).mylocaitonlistener.getCity();

			insert(city);
			Message msg = handler.obtainMessage();
			msg.what = 2;
			handler.sendMessage(msg);
			System.out.println("getcity11" + city);

		}
	}

	public void insert(String city1) {
		db = new mydatabase(this, "weather.db", 1);
		if (distinct(city1)) {
			System.out.println("true:啥都不执行" + boolcity);
		} else {
			db.getWritableDatabase().execSQL(
					"insert into city values(null,?,?)",
					new String[] { city1, city1 });
			System.out.println("false：添加城市" + boolcity);
			Toast.makeText(this, "城市添加成功", Toast.LENGTH_LONG).show();
		}
		System.out.println("第二步");
	}

	public boolean distinct(String city1) {
		System.out.println("第一步");
		db = new mydatabase(this, "weather.db", 1);
		String sql = "select city_name as _id from city where city_name=?";
		Cursor c2 = db.getWritableDatabase().rawQuery(sql,
				new String[] { city1 });
		if (c2.moveToFirst()) {
			Toast.makeText(this, "城市已存在", Toast.LENGTH_LONG).show();
			String c2city = c2.getString(0);
			System.out.println("枣庄：" + c2city);
			boolcity = true;
			return true;
		} else {
			System.out.println("不存在枣庄");
			boolcity = false;
			return false;
		}
	}

	class async extends AsyncTask<Void, Void, Void> {

		protected Void doInBackground(Void... params) {
			mydatabase.AssetsToDatabase(MainActivity.this);
			mydatabase dbhelper = new mydatabase(MainActivity.this,
					"weather.db", 1);
			cursor = dbhelper.getWritableDatabase().rawQuery(
					"select * from city", null);
			while (cursor.moveToNext()) {
				String citynames = cursor.getString(1);
				String city = cursor.getString(2);
				System.out.println("cursor" + citynames);
				results = HttpJson.Http(citynames);
				map = new HashMap<String, Object>();
				map = HttpJson.Map(results);
				pm = HttpJson.HttpPm(city);
				map1 = new HashMap<String, Object>();
				map1 = HttpJson.PMMap(pm);
				mapall = new HashMap<String, Object>();
				mapall.putAll(map);
				mapall.putAll(map1);
				listdata.add(mapall);
			}
			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			System.out.println("data" + listdata.size() + "\n" + "cursor"
					+ cursor.getCount());
			if (listdata.size() == cursor.getCount()) {
				list.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
		}

	}

	public void delete(String district) {
		db = new mydatabase(this, "weather.db", 1);
		db.getReadableDatabase().execSQL("delete from city where city_name=?",
				new String[] { district });
		Toast.makeText(this, "城市删除成功", Toast.LENGTH_SHORT).show();
	}

	class FirstHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 2) {
				results = HttpJson.Http(city);
				map = new HashMap<String, Object>();
				pm = HttpJson.HttpPm(city);
				map = HttpJson.Map(results);
				pm = HttpJson.HttpPm(city);
				map1 = new HashMap<String, Object>();
				map1 = HttpJson.PMMap(pm);
				mapall = new HashMap<String, Object>();
				mapall.putAll(map);
				mapall.putAll(map1);

				System.out.println("第三步");

				if (boolcity) {

				} else {
					adapter.notifyDataSetChanged();
					listdata.addFirst(mapall);

				}
			}
		}

	}

	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setIsNeedAddress(true);
		option.setLocationMode(LocationMode.Hight_Accuracy);
		locationclient.setLocOption(option);
	}

	class CardAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public CardAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listdata.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater
						.inflate(R.layout.weather, parent, false);
				holder = new ViewHolder();

				holder.layout_bg = (LinearLayout) convertView
						.findViewById(R.id.layout_bg);

				holder.pmbg = (LinearLayout) convertView
						.findViewById(R.id.PMBG);

				holder.cityTV = (TextView) convertView
						.findViewById(R.id.cityTV);
				holder.todayIV = (ImageView) convertView
						.findViewById(R.id.todayIV);
				holder.tempTV = (TextView) convertView
						.findViewById(R.id.tempTV);
				holder.tempTodayTV = (TextView) convertView
						.findViewById(R.id.tempTodayTV);
				holder.todayweaTV = (TextView) convertView
						.findViewById(R.id.todayweaTV);
				holder.windTV = (TextView) convertView
						.findViewById(R.id.windTV);

				holder.PMTV = (TextView) convertView.findViewById(R.id.PMTV);
				holder.PM2TV = (TextView) convertView.findViewById(R.id.PM2TV);
				holder.PM3TV = (TextView) convertView.findViewById(R.id.PM3TV);

				holder.tomrrowTV = (TextView) convertView
						.findViewById(R.id.tomrrowTV);
				holder.tomrrowweaIV = (ImageView) convertView
						.findViewById(R.id.tomrrowweaIV);
				holder.tomrrowweaTV = (TextView) convertView
						.findViewById(R.id.tomrrowweaTV);
				holder.tomrrowtempTV = (TextView) convertView
						.findViewById(R.id.tomrrowtempTV);

				holder.secondTV = (TextView) convertView
						.findViewById(R.id.secondTV);
				holder.secondweaIV = (ImageView) convertView
						.findViewById(R.id.secondweaIV);
				holder.secondweaTV = (TextView) convertView
						.findViewById(R.id.secondweaTV);
				holder.secondtempTV = (TextView) convertView
						.findViewById(R.id.secondtempTV);

				holder.thirdTV = (TextView) convertView
						.findViewById(R.id.thirdTV);
				holder.thirdweaIV = (ImageView) convertView
						.findViewById(R.id.thirdweaIV);
				holder.thirdweaTV = (TextView) convertView
						.findViewById(R.id.thirdweaTV);
				holder.thirdtempTV = (TextView) convertView
						.findViewById(R.id.thirdtempTV);

				convertView.setTag(holder);

			}

			else {
				holder = (ViewHolder) convertView.getTag();
			}

			// holder.layout_bg.setBackgroundResource(GetPic
			// .GetBgID((String) listdata.get(position).get("todaywea")));

			holder.cityTV.setText((String) listdata.get(position).get("city"));
			holder.tempTV.setText((String) listdata.get(position).get("temp"));
			holder.todayIV.setImageResource(GetPic.GetID((String) listdata.get(
					position).get("todaywea")));
			holder.todayweaTV.setText((String) listdata.get(position).get(
					"todaywea"));
			holder.tempTodayTV.setText((String) listdata.get(position).get(
					"todaytemp"));
			holder.windTV.setText((String) listdata.get(position).get("wind"));

			holder.PMTV.setText("PM2.5:");
			holder.PM2TV.setText((String) listdata.get(position).get("pm"));

			// holder.pmbg.setBackgroundColor(getResources().getColor(
			// HttpJson.setColor(Integer.parseInt((String) listdata.get(
			// position).get("pm")))));

			holder.PM3TV.setText(HttpJson.level(Integer
					.parseInt((String) listdata.get(position).get("pm"))));
			// holder.PM3TV.setTextColor(getResources().getColor(
			// HttpJson.setColor(Integer.parseInt((String) listdata.get(
			// position).get("pm")))));

			holder.tomrrowTV.setText("明天");
			holder.tomrrowweaTV.setText((String) listdata.get(position).get(
					"tomrrowwea"));
			holder.tomrrowweaIV.setImageResource(GetPic.GetID((String) listdata
					.get(position).get("tomrrowwea")));
			holder.tomrrowtempTV.setText((String) listdata.get(position).get(
					"tomrrowtemp"));

			holder.secondtempTV.setText((String) listdata.get(position).get(
					"secondtemp"));
			holder.secondTV.setText((String) listdata.get(position).get(
					"tomrrow1_date"));
			holder.secondweaIV.setImageResource(GetPic.GetID((String) listdata
					.get(position).get("secondwea")));
			holder.secondweaTV.setText((String) listdata.get(position).get(
					"secondwea"));

			holder.thirdtempTV.setText((String) listdata.get(position).get(
					"thirdtemp"));
			holder.thirdTV.setText((String) listdata.get(position).get(
					"tomrrow2_date"));
			holder.thirdweaIV.setImageResource(GetPic.GetID((String) listdata
					.get(position).get("thirdwea")));
			holder.thirdweaTV.setText((String) listdata.get(position).get(
					"thirdwea"));

			return convertView;
		}

	}

	static class ViewHolder {
		LinearLayout layout_bg, pmbg;
		TextView cityTV, tempTV, tempTodayTV, todayweaTV, windTV, PMTV, PM2TV,
				PM3TV, tomrrowTV, tomrrowtempTV, tomrrowweaTV, secondTV,
				secondtempTV, secondweaTV, thirdTV, thirdweaTV, thirdtempTV;
		ImageView todayIV, tomrrowweaIV, secondweaIV, thirdweaIV;
	}

	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
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
