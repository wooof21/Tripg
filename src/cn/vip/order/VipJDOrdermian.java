package cn.vip.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.loonggg.listview.LvAdapter;
import net.loonggg.listview.MyListView;
import net.loonggg.listview.MyListView.OnRefreshListener;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import cn.internet.Exit;
import cn.internet.Tools;
import cn.tripg.R;
import cn.tripg.activity.flight.ResultCode;
import cn.tripg.activity.newhotels.HotelOrder;
import cn.tripg.activity.newhotels.HotelOrderAddTimeDownComparator;
import cn.tripg.interfaces.TripgMessage;
import cn.tripg.interfaces.impl.VipHotelOrderInterfaces;
import cn.tripg.interfaces.impl.VipHttpXiangFaces;
import cn.tripg.widgit.ProgressDialogTripg;
import cn.vip.next.main.VipNOrderXiang;
import cn.vip.next.main.VipNextOrdermain;
import cn.vip.next.main.VipXiangCell;

public class VipJDOrdermian extends Activity {
	private List<String> list;
	private MyListView lv;
	private ImageView noOrder;
	private LvAdapter adapter;
	public String cellString;
	public VipHotelOrderInterfaces vipHotelOrderInterfaces;
	private final int UPDATE_LIST_VIEW = 1;
	public VipJDOrdermian vipJDOrdermian;
	private List<HashMap<String, Object>> listData;
	public String username;
	public int j;
	private ProgressDialog progressDialog;
	private ArrayList<HotelOrder> ho;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vipjiudainordermain);
		Exit.getInstance().addActivity(this);
		noOrder = (ImageView)findViewById(R.id.hotel_wudindan);
		vipJDOrdermian = this;
		j = 2;
		lv = (MyListView) findViewById(R.id.lv);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Log.e("mylistview  is here  ", "" + arg2);
				HotelOrder hashMap = ho.get(arg2-1);

				Intent intent = new Intent(VipJDOrdermian.this,
						VipJDNextOrderm.class);
				Bundle bundle = new Bundle();
				bundle.putString("menberid", username);
				bundle.putString("orderid", hashMap.getOrderId());
				intent.putExtras(bundle);
				startActivity(intent);

			}
		});

		listData = new ArrayList<HashMap<String, Object>>();
		lv.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}

						Log.e("测试下拉刷新", "-------222222------");

						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						adapter.notifyDataSetChanged();
						lv.onRefreshComplete();
						httpUpDataget();

						Log.e("测试下拉刷新", "--------111111-------");
					}
				}.execute(null, null, null);
			}
		});

		ImageView backImageView = (ImageView) findViewById(R.id.title_back);
		backImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = getIntent();
				setResult(ResultCode.FAILURE, intent);
				finish();
			}
		});
		isUserLogin();
		httpHotelgetjson();

	}

	private boolean isUserLogin() {
		SharedPreferences sharedPre = VipJDOrdermian.this.getSharedPreferences(
				"config", Context.MODE_PRIVATE);

		username = sharedPre.getString("Result", "");
		String password = sharedPre.getString("password", "");
		Log.e("Result", "A" + username);
		Log.e("password", "B" + password);
		if ("".equals(username) || "".equals(password)) {
			return false;
		} else {
			return true;
		}
	}

	public void httpUpDataget() {
		String urlString = "http://mapi.tripglobal.cn/MemApi.aspx?action=HotelOrderList&memberId="
				+ username
				+ "&pageIndex="
				+ j
				+ "&pageSize=20&token=IEEW9lg9hHa1qMC2LrAgHuluilAAX_q0";
		Log.e("url--------------", "" + urlString);
		vipHotelOrderInterfaces = new VipHotelOrderInterfaces(
				VipJDOrdermian.this, handler);
		vipHotelOrderInterfaces
				.getModelFromGET(urlString, TripgMessage.HANGBAN,"0");
		j++;
	}

	public void httpHotelgetjson() {

//		vipHotelOrderInterfaces = new VipHotelOrderInterfaces(
//				VipJDOrdermian.this, handler);
//		vipHotelOrderInterfaces
//				.getModelFromGET(urlString, TripgMessage.HANGBAN,"0");
		new AsyncTask<Void, Void, String>(){

			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				progressDialog = ProgressDialogTripg.show(vipJDOrdermian, null, null);
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				progressDialog.dismiss();
				if(!(result.equals(""))){
					Toast.makeText(vipJDOrdermian, result, Toast.LENGTH_LONG).show();
				}
			}

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				Tools tools = new Tools();
				String msg = "";
				String urlString = "http://mapi.tripglobal.cn/MemApi.aspx?action=HotelOrderList&memberId="
						+ username
						+ "&pageIndex=1&pageSize=20&token=IEEW9lg9hHa1qMC2LrAgHuluilAAX_q0";
				Log.e("url--------------", "" + urlString);
				String data = tools.getURL(urlString);
				Log.e("data", data);
				
				ho = new ArrayList<HotelOrder>();
				try {
					JSONObject job = new JSONObject(data);
					String code = job.getString("Code");
					if(code.equals("1")){
						JSONObject job1 = job.getJSONObject("Result");
						JSONArray jArray = job1.getJSONArray("PageList");
						for(int i=0;i<jArray.length();i++){
							HotelOrder hOrder = new HotelOrder();
							JSONObject job2 = jArray.optJSONObject(i);
							hOrder.setAddTime(job2.getString("AddTime").toString());
							hOrder.setCheckInDate(job2.getString("CheckInDate").toString());
							hOrder.setHotelName(job2.getString("HotelName").toString());
							hOrder.setOrderId(job2.getString("OrderId").toString());
							hOrder.setOrderStatus(job2.getString("OrderStatus").toString());
							hOrder.setTotalPrice(job2.getString("TotalPrice").toString());
							ho.add(hOrder);
						}
						if(!(ho.isEmpty())){
							sendHandlerMessage(UPDATE_LIST_VIEW, null);
						}else{
							sendHandlerMessage(2, null);
						}
					}else{
						msg = "获取酒店列表失败";
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return msg;
			}
			
		}.execute();
		
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
				handMessageDefault(vipHotelOrderInterfaces,
						VipJDOrdermian.this, msg);
				break;
			case UPDATE_LIST_VIEW:
				Log.e("订单详情页 接口调用解析之后返回调用 函数", "****");
				lv.setVisibility(View.VISIBLE);
				noOrder.setVisibility(View.INVISIBLE);
				HotelOrderAddTimeDownComparator hod = new HotelOrderAddTimeDownComparator();
				Collections.sort(ho, hod);
				for(int i=0;i<ho.size();i++){
					Log.e("AddTime down ", ho.get(i).getAddTime());
				}
				adapter = new LvAdapter(ho, vipJDOrdermian, 1);
				lv.setAdapter(adapter);

				break;
			case 2:
				lv.setVisibility(View.INVISIBLE);
				noOrder.setVisibility(View.VISIBLE);
				break;
			default:

				break;
			}

		}

		@SuppressWarnings("unchecked")
		private void handMessageDefault(
				VipHotelOrderInterfaces vipHttpXiangFaces,
				VipJDOrdermian vipJDOrdermian, Message msg) {

			if (vipHttpXiangFaces == null)
				return;
			if (vipHttpXiangFaces.progressDialog != null)
				vipHttpXiangFaces.progressDialog.dismiss();
			if (msg.obj == null) {
				Toast.makeText(vipJDOrdermian, "网络链接超时", Toast.LENGTH_SHORT)
						.show();
			}
			
//			else {
//				// listData = (List<HashMap<String, Object>>) msg.obj;
//				// List<HashMap<String, Object>> list = (List<HashMap<String,
//				// Object>>) msg.obj;
//				
//					
//					List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) msg.obj;
//					Log.e("list----长度 ", "" + list.size());
//					for (int i = 0; i < list.size(); i++) {
//						HashMap<String, Object> hashMap = new HashMap<String, Object>();
//						hashMap = list.get(i);
//						listData.add(hashMap);
//					}
//
//					if (listData.size() == 0) {
//						sendHandlerMessage(2, null);
//					} else{
//						sendHandlerMessage(UPDATE_LIST_VIEW, null);
//					}
//
//				Log.e("listData----长度 ", "" + listData.size());
//				// 通知刷新界面
//				
//
//			}

		}

	};

}
