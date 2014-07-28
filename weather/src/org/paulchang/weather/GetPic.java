package org.paulchang.weather;



public class GetPic {
	public static String GetWea(String weather) {
		if (weather.indexOf("转") != -1) {
			weather = weather.split("转")[1];
		} else
			weather = weather;
		return weather;

	}

	public static int GetID(String weather) {
		int PicID = R.drawable.weather_snow_shower;
		if (weather.indexOf("晴") != -1) {
			PicID = R.drawable.weather_sunny;
		} else if (weather.indexOf("多云") != -1) {
			PicID = R.drawable.weather_mostly_cloudy;
		} else if (weather.indexOf("阴") != -1) {
			PicID = R.drawable.weather_cloudy;
		} else if (weather.indexOf("阵雨") != -1) {
			PicID = R.drawable.weather_shower;
		} else if (weather.indexOf("小雨") != -1) {
			PicID = R.drawable.weather_drizzle;
		} else if (weather.indexOf("小雨转中雨") != -1) {
			PicID = R.drawable.weather_drizzle;
		} else if (weather.indexOf("中雨") != -1) {
			PicID = R.drawable.weather_drizzle;
		} else if (weather.indexOf("中雨转大雨") != -1) {
			PicID = R.drawable.weather_heavy_rain;
		} else if (weather.indexOf("大雨") != -1) {
			PicID = R.drawable.weather_heavy_rain;
		} else if (weather.indexOf("暴雨") != -1) {
			PicID = R.drawable.weather_heavy_rain;
		} else if (weather.indexOf("冻雨") != -1) {
			PicID = R.drawable.weather_sleet;
		} else if (weather.indexOf("雷阵雨") != -1) {
			PicID = R.drawable.weather_thunderstorms;
		} else if (weather.indexOf("雷阵雨伴有冰雹") != -1) {
			PicID = R.drawable.weather_sleet;
		} else if (weather.indexOf("雨夹雪") != -1) {
			PicID = R.drawable.weather_sleet;
		} else if (weather.indexOf("阵雪") != -1) {
			PicID = R.drawable.weather_snow_shower;
		} else if (weather.indexOf("小雪") != -1) {
			PicID = R.drawable.weather_little_snow;
		} else if (weather.indexOf("中雪") != -1) {
			PicID = R.drawable.weather_little_snow;
		} else if (weather.indexOf("大雪") != -1) {
			PicID = R.drawable.weather_heavy_snow;
		} else if (weather.indexOf("中雪转大雪") != -1) {
			PicID = R.drawable.weather_heavy_snow;
		} else if (weather.indexOf("暴雪") != -1) {
			PicID = R.drawable.weather_heavy_snow;
		} else if (weather.indexOf("浮尘") != -1) {
			PicID = R.drawable.weather_tornado;
		} else if (weather.indexOf("扬沙") != -1) {
			PicID = R.drawable.weather_tornado;
		} else if (weather.indexOf("霾") != -1) {
			PicID = R.drawable.weather_fog;
		} else if (weather.indexOf("沙尘暴") != -1) {
			PicID = R.drawable.weather_sandstorm;
		} else if (weather.indexOf("雾") != -1) {
			PicID = R.drawable.weather_fog;
		}
		return PicID;
	}

	public static int GetBgID(String weather) {
		int PicBgID = R.drawable.weather_bg_sunny;
		if (weather.indexOf("晴") != -1 || weather.indexOf("多云") != -1
				|| weather.indexOf("阴") != -1) {
			PicBgID = R.drawable.weather_bg_sunny;
		} else if (weather.indexOf("阵雨") != -1) {
			PicBgID = R.drawable.weather_bg_rain;
		} else if (weather.indexOf("小雨") != -1) {
			PicBgID = R.drawable.weather_bg_rain;
		} else if (weather.indexOf("小雨转中雨") != -1) {
			PicBgID = R.drawable.weather_bg_rain;
		} else if (weather.indexOf("中雨") != -1) {
			PicBgID = R.drawable.weather_bg_rain;
		} else if (weather.indexOf("中雨转大雨") != -1) {
			PicBgID = R.drawable.weather_bg_rain;
		} else if (weather.indexOf("大雨") != -1) {
			PicBgID = R.drawable.weather_bg_rain;
		} else if (weather.indexOf("暴雨") != -1) {
			PicBgID = R.drawable.weather_bg_rain;
		} else if (weather.indexOf("冻雨") != -1) {
			PicBgID = R.drawable.weather_bg_rain;
		} else if (weather.indexOf("雷阵雨") != -1) {
			PicBgID = R.drawable.weather_bg_rain;
		} else if (weather.indexOf("雷阵雨伴有冰雹") != -1) {
			PicBgID = R.drawable.weather_bg_rain;
		} else if (weather.indexOf("雨夹雪") != -1) {
			PicBgID = R.drawable.weather_bg_snow;
		} else if (weather.indexOf("阵雪") != -1) {
			PicBgID = R.drawable.weather_bg_snow;
		} else if (weather.indexOf("小雪") != -1) {
			PicBgID = R.drawable.weather_bg_snow;
		} else if (weather.indexOf("中雪") != -1) {
			PicBgID = R.drawable.weather_bg_snow;
		} else if (weather.indexOf("大雪") != -1) {
			PicBgID = R.drawable.weather_bg_snow;
		} else if (weather.indexOf("中雪转大雪") != -1) {
			PicBgID = R.drawable.weather_bg_snow;
		} else if (weather.indexOf("暴雪") != -1) {
			PicBgID = R.drawable.weather_bg_snow;
		} else if (weather.indexOf("浮尘") != -1 || weather.indexOf("扬沙") != -1
				|| weather.indexOf("霾") != -1 || weather.indexOf("沙尘暴") != -1
				|| weather.indexOf("雾") != -1) {
			PicBgID = R.drawable.weather_bg_fog;

		}
		return PicBgID;

	}
}
