package cn.vip.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.loonggg.listview.LvAdapter;
import net.loonggg.listview.LvAdapterCar;
import net.loonggg.listview.MyListView;
import net.loonggg.listview.MyListView.OnRefreshListener;
import cn.internet.Exit;
import cn.tripg.R;
import cn.tripg.activity.flight.ResultCode;
import cn.tripg.interfaces.TripgMessage;
import cn.tripg.interfaces.impl.VipHotelOrderInterfaces;
import cn.tripg.interfaces.impl.YHttpOrderInterfaces;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class VipYcarOrdermain extends Activity{

	private List<HashMap<String, Object>> listData;
	public String useridString;
	private final int UPDATE_LIST_VIEW = 1;
	private List<String> list;
	private MyListView lv;
	private LvAdapterCar adapter;
	public int j;
	public VipYcarOrdermain vipYcarOrdermain;
	public YHttpOrderInterfaces yHttpOrderInterfaces;
	private ImageView noOrder;
	private FrameLayout carLayout;
	
	
	
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
		setContentView(R.layout.vipyongcheordermain);
		Exit.getInstance().addActivity(this);
		noOrder = (ImageView)findViewById(R.id.car_wudindan);
		carLayout = (FrameLayout)findViewById(R.id.car_framelayout);
		ImageView backImageView = (ImageView) findViewById(R.id.title_back);
		backImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = getIntent();
				setResult(ResultCode.FAILURE, intent);
				finish();
			}
		});
		
		vipYcarOrdermain = this;
		j = 2;
		lv = (MyListView) findViewById(R.id.lv);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Log.e("mylistview  is here  ", "" + arg2);
				HashMap<String, Object> hashMap = listData.get(arg2-1);

				Intent intent = new Intent(VipYcarOrdermain.this,
						VipYcarNextOrderm.class);
				Bundle bundle = new Bundle();
				bundle.putString("memberid", useridString);
				bundle.putString("orderid", hashMap.get("OrderId").toString());
				intent.putExtras(bundle);
				startActivity(intent);

			}
		});
		
		isUserLogin();
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
						httpYiOrderNextGet();

						Log.e("测试下拉刷新", "--------111111-------");
					}
				}.execute(null, null, null);
			}
		});
		
		if(getInternet() == true){
			httpYiOrderGet();
		}else{
			Toast.makeText(vipYcarOrdermain, "网络链接已断开", Toast.LENGTH_LONG).show();
		}
		
		
	}
	
	private boolean isUserLogin() {
		SharedPreferences sharedPre = VipYcarOrdermain.this.getSharedPreferences(
				"config", Context.MODE_PRIVATE);

		useridString = sharedPre.getString("Result", "");
		String password = sharedPre.getString("password", "");
		Log.e("Result", "A" + useridString);
		Log.e("password", "B" + password);
		if ("".equals(useridString) || "".equals(password)) {
			return false;
		} else {
			return true;
		}
	}
	
	public void httpYiOrderNextGet(){
		
		String urlString ="http://mapi.tripglobal.cn/MemApi.aspx?action=RentCarOrderList&memberId="+useridString+"&pageIndex="+j+"&pageSize=20&token=IEEW9lg9hHa1qMC2LrAgHuluilAAX_q0";
		Log.e("Yong车", ""+urlString);
		yHttpOrderInterfaces = new YHttpOrderInterfaces(VipYcarOrdermain.this, handler);
		yHttpOrderInterfaces.getModelFromGET(urlString, TripgMessage.HANGBAN,"0");//
		j++;
	}
	
	
	public void httpYiOrderGet(){
			
		String urlString ="http://mapi.tripglobal.cn/MemApi.aspx?action=RentCarOrderList&memberId="+useridString+"&pageIndex=1&pageSize=20&token=IEEW9lg9hHa1qMC2LrAgHuluilAAX_q0";
		Log.e("Yong车", ""+urlString);
		yHttpOrderInterfaces = new YHttpOrderInterfaces(VipYcarOrdermain.this, handler);
		yHttpOrderInterfaces.getModelFromGET(urlString, TripgMessage.HANGBAN,"0");
	
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
				handMessageDefault(yHttpOrderInterfaces,
						VipYcarOrdermain.this, msg);
				break;
			case UPDATE_LIST_VIEW:
				Log.e("订单详情页 接口调用解析之后返回调用 函数", "****");
				lv.setVisibility(View.VISIBLE);
				noOrder.setVisibility(View.INVISIBLE);
				adapter = new LvAdapterCar(listData, vipYcarOrdermain, 2);
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
				YHttpOrderInterfaces yHttpOrderInterfaces,
				VipYcarOrdermain vipYcarOrdermain, Message msg) {

			if (yHttpOrderInterfaces == null)
				return;
			if (yHttpOrderInterfaces.progressDialog != null)
				yHttpOrderInterfaces.progressDialog.dismiss();
			if (msg.obj == null) {
				Toast.makeText(vipYcarOrdermain, "网络链接超时", Toast.LENGTH_SHORT)
						.show();
			} else {				
					List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) msg.obj;
					Log.e("list----长度 ", "" + list.size());
					for (int i = 0; i < list.size(); i++) {
						HashMap<String, Object> hashMap = new HashMap<String, Object>();
						hashMap = list.get(i);
						listData.add(hashMap);
					}
					if (listData.size() == 0) {
						sendHandlerMessage(2, null);
					} else{
						sendHandlerMessage(UPDATE_LIST_VIEW, null);
					}
					
				

				Log.e("listData----长度 ", "" + listData.size());
				// 通知刷新界面
				

			}

		}

	};

	
	
	
	
	
}
