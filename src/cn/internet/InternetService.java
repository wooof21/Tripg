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

		//����������ӷ���
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		State state = connManager.getNetworkInfo(
		ConnectivityManager.TYPE_WIFI).getState(); // ��ȡ��������״̬
		boolean wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.isConnectedOrConnecting();
		boolean internet = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.isConnectedOrConnecting();
		if (wifi==true) { // �ж��Ƿ�����ʹ��WIFI����
		success = true;
		}

		state = connManager.getNetworkInfo(
		ConnectivityManager.TYPE_MOBILE).getState(); // ��ȡ��������״̬
		if (internet==true) { // �ж��Ƿ�����ʹ��GPRS����
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