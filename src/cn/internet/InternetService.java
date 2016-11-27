package cn.internet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Handler;
import android.os.Message;

public class InternetService extends BroadcastReceiver{

	private Handler rHandler;
	
	public InternetService(Handler rHandler) {
		super();
		this.rHandler = rHandler;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		boolean success = false;

		//获得网络连接服务
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		State state = connManager.getNetworkInfo(
		ConnectivityManager.TYPE_WIFI).getState(); // 获取网络连接状态
		boolean wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.isConnectedOrConnecting();
		boolean internet = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.isConnectedOrConnecting();
		if (wifi==true) { // 判断是否正在使用WIFI网络
		success = true;
		}

		state = connManager.getNetworkInfo(
		ConnectivityManager.TYPE_MOBILE).getState(); // 获取网络连接状态
		if (internet==true) { // 判断是否正在使用GPRS网络
		success = true;
		}

		if (success==true) {
			Message msg = new Message();
			msg.what = 2;
			rHandler.sendMessage(msg);
		}else{
			Message msg = new Message();
			msg.what = 1;
			rHandler.sendMessage(msg);
		}
		
		
	}


}