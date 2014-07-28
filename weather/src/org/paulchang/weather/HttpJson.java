package org.paulchang.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpJson {
	public static String[] Http(String city_name) {
		String city = null, temp = null, today = null, wind = null, time1 = null, today_wea = null, tomrrow0 = null, tomrrow0_wea = null, tomrrow1 = null, tomrrow1_wea = null, tomrrow2 = null, tomrrow2_wea = null, tomrrow1_date = null, tomrrow2_date = null, tomrrow1_date1 = null, tomrrow2_date1 = null;
		String city1 = "http://api.map.baidu.com/telematics/v3/weather?location="
				+ city_name + "&output=json&ak=HwnbhWlQObCsvgKABtx5ddRG";
		System.out.println(city1);
		HttpClient http = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(city1);
		HttpResponse httpresponse;
		try {
			httpresponse = http.execute(httpget);
			HttpEntity entity = httpresponse.getEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					entity.getContent()));
			String result = "", line;
			while ((line = br.readLine()) != null) {

				result = result + line;

			}
			http.getConnectionManager().shutdown();
			// System.out.println(result);

			JSONObject json = new JSONObject(result);
			city = json.getJSONArray("results").getJSONObject(0)
					.getString("currentCity");
			temp = json.getJSONArray("results").getJSONObject(0)
					.getJSONArray("weather_data").getJSONObject(0)
					.getString("date");
			today = json.getJSONArray("results").getJSONObject(0)
					.getJSONArray("weather_data").getJSONObject(0)
					.getString("temperature");
			today_wea = json.getJSONArray("results").getJSONObject(0)
					.getJSONArray("weather_data").getJSONObject(0)
					.getString("weather");
			wind = json.getJSONArray("results").getJSONObject(0)
					.getJSONArray("weather_data").getJSONObject(0)
					.getString("wind");
			tomrrow0 = json.getJSONArray("results").getJSONObject(0)
					.getJSONArray("weather_data").getJSONObject(1)
					.getString("temperature");
			tomrrow0_wea = json.getJSONArray("results").getJSONObject(0)
					.getJSONArray("weather_data").getJSONObject(1)
					.getString("weather");
			tomrrow1_date = json.getJSONArray("results").getJSONObject(0)
					.getJSONArray("weather_data").getJSONObject(2)
					.getString("date");
			tomrrow1 = json.getJSONArray("results").getJSONObject(0)
					.getJSONArray("weather_data").getJSONObject(2)
					.getString("temperature");
			tomrrow1_wea = json.getJSONArray("results").getJSONObject(0)
					.getJSONArray("weather_data").getJSONObject(2)
					.getString("weather");
			tomrrow2_date = json.getJSONArray("results").getJSONObject(0)
					.getJSONArray("weather_data").getJSONObject(3)
					.getString("date");
			tomrrow2 = json.getJSONArray("results").getJSONObject(0)
					.getJSONArray("weather_data").getJSONObject(3)
					.getString("temperature");
			tomrrow2_wea = json.getJSONArray("results").getJSONObject(0)
					.getJSONArray("weather_data").getJSONObject(3)
					.getString("weather");

			temp = temp.split("：")[1];
			time1 = temp.substring(0, temp.length() - 1);

			/*
			 * tomrrow1_date = tomrrow1_date.split("(")[1]; tomrrow1_date1 =
			 * tomrrow1_date.substring(0, tomrrow1_date.length() - 1);
			 * tomrrow2_date = tomrrow2_date.split("(")[1]; tomrrow2_date1 =
			 * tomrrow2_date.substring(0, tomrrow1_date.length() - 1);
			 */

			// String time2 = time1.split(")")[0];

			today_wea = GetPic.GetWea(today_wea);
			tomrrow0_wea = GetPic.GetWea(tomrrow0_wea);
			tomrrow1_wea = GetPic.GetWea(tomrrow1_wea);
			tomrrow2_wea = GetPic.GetWea(tomrrow2_wea);

			System.out.println(time1 + "\n" + tomrrow0 + "\n" + tomrrow1 + "\n"
					+ tomrrow2 + "\n");

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String[] { city, time1, today, today_wea, wind, tomrrow0,
				tomrrow0_wea, tomrrow1, tomrrow1_wea, tomrrow2, tomrrow2_wea,
				tomrrow1_date, tomrrow2_date };
	}

	public static String HttpPm(String city) {
		String pm2 = null, error = null;
		String pm = "http://web.juhe.cn:8080/environment/air/pm?city=" + city
				+ "&key=33f149edde0a728db8432523c7d90950";
		System.out.println("请求PM2.5" + pm);
		HttpClient http = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(pm);
		try {
			HttpResponse httprespnse = http.execute(httpget);
			HttpEntity entity = httprespnse.getEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					entity.getContent()));
			String result = "", line;
			while ((line = br.readLine()) != null) {

				result = result + line;
				System.out.println(result);
			}

			JSONObject json = new JSONObject(result);

			error = json.getString("error_code");
			if (error.equals("10012")) {
				pm2 = "-1";
			} else if (error.equals("203303")) {
				pm2 = "-2";
			} else if (error.equals("203302")) {
				pm2 = "-3";
			}

			else {
				pm2 = json.getJSONArray("result").getJSONObject(0)
						.getString("PM2.5");
				System.out.println("pm2.5" + pm2);

			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return pm2;

	}

	public static int setColor(int pm2) {
		int colorID = R.color.you;
		if (pm2 == -1) {
			colorID = R.color.you;
		} else if (pm2 < 50) {
			colorID = R.color.you;
		} else if (pm2 < 100) {
			colorID = R.color.liang;
		} else if (pm2 < 150) {
			colorID = R.color.qing;
		} else if (pm2 < 200) {
			colorID = R.color.zhong;
		} else {
			colorID = R.color.heavy;
		}
		return colorID;
	}

	public static int level(int pm2) {
		int pmID = R.string.no;
		if (pm2 == -1) {
			pmID = R.string.beiju1;
		} else if (pm2 == -2) {
			pmID = R.string.beiju2;
		} else if (pm2 == -3) {
			pmID = R.string.beiju3;
		} else if (pm2 < 50) {
			pmID = R.string.you;
		} else if (pm2 < 100) {
			pmID = R.string.liang;
		} else if (pm2 < 150) {
			pmID = R.string.qing;
		} else if (pm2 < 200) {
			pmID = R.string.zhong;
		} else {
			pmID = R.string.heavy;
		}
		return pmID;

	}

	public static HashMap<String, Object> PMMap(String result) {
		HashMap<String, Object> pmmap = new HashMap<String, Object>();
		pmmap.put("pm", result);
		return pmmap;
	}

	public static HashMap<String, Object> Map(String[] results) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("city", results[0]);
		map.put("temp", results[1]);
		map.put("todaytemp", results[2]);

		map.put("todaywea", results[3]);

		map.put("wind", results[4]);

		map.put("tomrrowtemp", results[5]);

		map.put("tomrrowwea", results[6]);

		map.put("secondtemp", results[7]);

		map.put("secondwea", results[8]);

		map.put("thirdtemp", results[9]);

		map.put("thirdwea", results[10]);

		map.put("tomrrow1_date", results[11]);

		map.put("tomrrow2_date", results[12]);

		return map;
	}
}
