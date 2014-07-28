package org.paulchang.weather;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmBroadCastReceiver extends BroadcastReceiver {
	private final int id = 011;

	@Override
	public void onReceive(Context context, Intent intent) {

		System.out.println("broadcast启动了");
		// id = bundle.getInt("id", 1);
		Intent intentTarget = new Intent(context, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				intentTarget, 0);
		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Builder notification = new Notification.Builder(context);
		// 设置提示框的状态
		((Builder) notification)
				.setAutoCancel(true)
				.setTicker("看一下今天的天气，关心一下远方的人")
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("看一下今天的天气，关心一下远方的人")

				.setDefaults(
						Notification.DEFAULT_SOUND
								| Notification.DEFAULT_LIGHTS)
				.setWhen(System.currentTimeMillis())
				.setContentIntent(contentIntent).build();
		nm.notify(id, notification.build());

		Toast.makeText(context, "时间到了！！！！", Toast.LENGTH_LONG).show();
	}

}
