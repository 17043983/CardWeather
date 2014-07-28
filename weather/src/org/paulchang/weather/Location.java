package org.paulchang.weather;

import android.app.Application;
import android.database.Cursor;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;

public class Location extends Application {

	public LocationClient locationclient = null;
	public MyLocationListener mylocaitonlistener = new MyLocationListener();
	public String mdata;
	public TextView mtv;
	String city = "beijing";
	String city1, weather_id;
	Cursor c;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		locationclient = new LocationClient(this);
		locationclient.setAccessKey("MmMs6p5DGcQfBeK3wCGIidcX");
		locationclient.registerLocationListener(mylocaitonlistener);

	}

	public class MyLocationListener implements BDLocationListener {

		private String city;

		// private String city;

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			if (location == null)
				return;
			StringBuffer sb = new StringBuffer(256);
			/*
			 * sb.append("time : "); sb.append(location.getTime());
			 * sb.append("\nerror code : "); sb.append(location.getLocType());
			 * sb.append("\nlatitude : "); sb.append(location.getLatitude());
			 * sb.append("\nlontitude : "); sb.append(location.getLongitude());
			 * sb.append("\nradius : "); sb.append(location.getRadius());
			 */

			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append(location.getCity());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\n省");
				sb.append(location.getProvince());
				sb.append("\n市");
				sb.append(location.getCity());
				int end = location.getCity().length() - 1;
				city = location.getCity();
				city = city.substring(0, end);
				sb.append("\n县");
				sb.append(location.getDistrict());
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			}

			sb.append("\nsdk version : ");
			sb.append(locationclient.getVersion());
			sb.append("\nisCellChangeFlag : ");
			sb.append(location.isCellChangeFlag());

			logMsg(city);
			setCity(city);
			// logMsg(sb.toString());
			System.out.println("sbsb" + sb.toString());

			// Log.i(TAG, sb.toString());

		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub

		}

		public void logMsg(String str) {
			try {
				mdata = str;
				mtv.setText("自动定位到:" + mdata);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public String getCity() {
			return city;
		}

		public void setCity(String s) {
			this.city = s;
		}
	}

}
