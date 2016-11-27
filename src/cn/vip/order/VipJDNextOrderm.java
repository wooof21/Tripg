package cn.vip.order;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.internet.Exit;
import cn.tripg.R;
import cn.tripg.activity.flight.ResultCode;
import cn.tripg.interfaces.TripgMessage;
import cn.tripg.interfaces.impl.VipHotelXOrderInterfaces;

public class VipJDNextOrderm extends Activity{

	public String memberString;
	public String orderidString;
	private final int UPDATE_LIST_VIEW = 1;
	public VipHotelXOrderInterfaces vipHotelInterfaces;
	private List<HashMap<String, Object>> listData;
	public HashMap<String, Object> osHashMap;
	
	public TextView textView1;
	public TextView textView2;
	public TextView textView3;
	public TextView textView4;
	public TextView textView5;
	public TextView textView6;
	public TextView textView7;
	public TextView textView8;
	public TextView textView9;
	public TextView textView10;
	
	
	/****************************************************************************/
	public boolean getInternet() {
		ConnectivityManager con = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.isConnectedOrConnecting();
		boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.isConnectedOrConnecting();
		if (wifi || internet) {
			return true;
		} else {
			return false;
		}
	}
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vipjdiannextorder);
		Exit.getInstance().addActivity(this);
		listData = new ArrayList<HashMap<String, Object>>();
		Intent intent = getIntent();
		memberString = intent.getExtras().getString("menberid");
		orderidString = intent.getExtras().getString("orderid");
		Log.e("memberString ", ""+memberString+"----------"+orderidString);
		
		
		osHashMap = new HashMap<String, Object>();
		osHashMap.put("1", "已预订");
		osHashMap.put("2", "办理中");
		osHashMap.put("3", "处理完成");
		osHashMap.put("4", "处理失败");
		osHashMap.put("5", "申请退");
		osHashMap.put("6", "申请退处理中");
		osHashMap.put("7", "申请退处理完成");
		osHashMap.put("8", "申请退处理失败");
		osHashMap.put("9", "申请改");
		osHashMap.put("10", "申请改处理中");
		osHashMap.put("11", "申请改处理完成");
		osHashMap.put("12", "申请改处理失败");
		osHashMap.put("13", "订单取消");
		
		ImageView backImageView = (ImageView) findViewById(R.id.title_back);
		backImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = getIntent();
				setResult(ResultCode.FAILURE, intent);
				finish();
			}
		});
		
		textView1 = (TextView)findViewById(R.id.textViewhxiang2);
		textView2 = (TextView)findViewById(R.id.textViewhxiang4);
		textView3 = (TextView)findViewById(R.id.textViewhxiang6);
		textView4 = (TextView)findViewById(R.id.textViewhxiang8);
		textView5 = (TextView)findViewById(R.id.textViewhxiang9);
		textView6 = (TextView)findViewById(R.id.textViewhxiang10);
		textView7 = (TextView)findViewById(R.id.textViewhxiang12);
		textView8 = (TextView)findViewById(R.id.textViewhxiang14);
		textView9 = (TextView)findViewById(R.id.textViewhxiang16);
		textView10 = (TextView)findViewById(R.id.textViewhxiang18);
		
		if(getInternet() == true){
			httpjdianxiang();
		}else{
			Toast.makeText(VipJDNextOrderm.this, "网络链接已断开", Toast.LENGTH_LONG).show();
		}
		
	
	}
	
	
	public void httpjdianxiang(){
		
		String urlString = "http://mapi.tripglobal.cn/MemApi.aspx?action=GetHotelOrder&memberId="+memberString+"&OrderId="+orderidString+"&token=IEEW9lg9hHa1qMC2LrAgHuluilAAX_q0";
		Log.e("urlString", ""+urlString);
		
		vipHotelInterfaces = new VipHotelXOrderInterfaces(VipJDNextOrderm.this, handler);
		vipHotelInterfaces.getModelFromGET(urlString, TripgMessage.HANGBAN,"0");
	}
	
	private void sendHandlerMessage(int what, Object obj) {

		Message msg = new Message();
		msg.what = what;
		handler.sendMessage(msg);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TripgMessage.HANGBAN:
				handMessageDefault(vipHotelInterfaces,
						VipJDNextOrderm.this, msg);
				break;
			case UPDATE_LIST_VIEW:
				Log.e("订单详情页 接口调用解析之后返回调用 函数", "****");
				if (listData.size() != 0) {
					
					HashMap<String, Object> hashMap = listData.get(0);
					String[] dt = hashMap.get("AddTime").toString().split(" ");
					String[] _dt = dt[0].split("/");
					String _dt_ = _dt[2] + "-" + _dt[0] + "-" + _dt[1] + " " + dt[1];
					textView1.setText(osHashMap.get(hashMap.get("OrderStatus").toString()).toString());

					textView2.setText(hashMap.get("OrderId").toString());
					textView3.setText(_dt_);
					String priceString = (String)hashMap.get("TotalPrice").toString();
					System.out.println("priceString ---> " + priceString);
					double b = Double.parseDouble(priceString);
					int p = (int) b;
					System.out.println("p ------> " + p);
					//int p = Integer.valueOf(priceString);
					textView4.setText(p+"元");
					textView5.setText(hashMap.get("HotelName").toString());
					textView6.setText(hashMap.get("RoomType").toString());
					textView7.setText(hashMap.get("CheckInDate").toString());
					textView8.setText(hashMap.get("CheckOutDate").toString());
					textView9.setText(hashMap.get("Name").toString());
					textView10.setText(hashMap.get("Mobile").toString());
				}
				
				
				break;

			default:

				break;
			}

		}

		@SuppressWarnings("unchecked")
		private void handMessageDefault(
				VipHotelXOrderInterfaces vipHttpXiangFaces,
				VipJDNextOrderm vipJDNextOrderm, Message msg) {

			if (vipHttpXiangFaces == null)
				return;
			if (vipHttpXiangFaces.progressDialog != null)
				vipHttpXiangFaces.progressDialog.dismiss();
			if (msg.obj == null) {
				Toast.makeText(vipJDNextOrderm, "网络链接超时", Toast.LENGTH_SHORT)
						.show();
			} else {
				 listData = (List<HashMap<String, Object>>) msg.obj;
				 Log.e("listData----长度 ", "" + listData.size());

			
			}

				// 通知刷新界面
				sendHandlerMessage(UPDATE_LIST_VIEW, null);

			}

	};
	
}
