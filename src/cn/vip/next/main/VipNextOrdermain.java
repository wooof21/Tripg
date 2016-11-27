package cn.vip.next.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.jar.Attributes.Name;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import cn.internet.Exit;
import cn.tripg.R;
import cn.tripg.activity.flight.ResultCode;
import cn.tripg.activity.hotel.HotelCell;
import cn.tripg.interfaces.TripgMessage;
import cn.tripg.interfaces.impl.VipOrderHttpIntfaces;
import cn.vip.main.VipMainConterllor;
import cn.vip.main.VipOrderCell;

public class VipNextOrdermain extends Activity implements OnItemClickListener{

	public ListView listView;
	private List<HashMap<String, Object>> listData;
	
	public VipOrderHttpIntfaces vipOrderHttpIntfaces;
	public VipNextOrdermain vipNextOrdermain;
	private final int UPDATE_LIST_VIEW = 1;
	public String username;
	private FrameLayout fLayout;
	private ImageView noOrder;
	
	
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
		setContentView(R.layout.vipnextorder_main);
		Exit.getInstance().addActivity(this);
		fLayout = (FrameLayout)findViewById(R.id.flight_framelayout);
		noOrder = (ImageView)findViewById(R.id.flight_wudindan);
		noOrder.setVisibility(View.INVISIBLE);
		vipNextOrdermain = this;
		ImageView backImageView = (ImageView)findViewById(R.id.title_back);
		backImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = getIntent();
				setResult(ResultCode.FAILURE, intent);
				finish();
			}
		});
		listData = new ArrayList<HashMap<String,Object>>();
		listView = (ListView)findViewById(R.id.listViewviporder1);
		isUserLogin();
		
		if(getInternet() == true){
			httporderintfaces();
		}else{
			Toast.makeText(VipNextOrdermain.this, "网络链接已断开", Toast.LENGTH_LONG).show();
		}
		
		
		
		
	}
	
	private boolean isUserLogin(){
        SharedPreferences sharedPre = VipNextOrdermain.this.getSharedPreferences(
        		"config", Context.MODE_PRIVATE);

        username = sharedPre.getString("username", "");
        String password = sharedPre.getString("password", "");
        Log.e("username", "A" + username);
        Log.e("password", "B" + password);
        if("".equals(username) || "".equals(password)){
        	return false;
        }else{
        	return true;
        }
    }
	
	public void httporderintfaces(){
		
		//String url = "http://210.72.225.98:8090/index.php/OrderApi/list_show?username="+username;
		String url = "http://www.tripg.cn/phone_api/shouji/index.php/OrderApi/list_show?username="+username;
		Log.e("httphangbanhao----url", url);
		//调用封装的http方法 并且绑定回调函数
		vipOrderHttpIntfaces = new VipOrderHttpIntfaces(vipNextOrdermain, handler);
		vipOrderHttpIntfaces.getModelFromGET(url,TripgMessage.HANGBAN,"0");
		
		
	}
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Log.e("listview  item", "****item"+arg2);
	
			HashMap<String, Object> hashMap = listData.get(arg2);
//			Log.e("listview  item  hashMap", "****hashMap "+hashMap.get("order_id"));
//			Log.e("listview  item  hashMap", "****hashMap "+hashMap.get("order_safe"));
			Intent intent = new Intent(vipNextOrdermain,VipNOrderXiang.class);
			Bundle bundle = new Bundle();
			bundle.putString("order_id", (String)hashMap.get("order_id"));
			if(hashMap.get("order_safe").toString().equals("null")){
				bundle.putString("order_safe", "0");
			}else{
				bundle.putString("order_safe", hashMap.get("order_safe").toString());
			}
			intent.putExtras(bundle);
			startActivity(intent);
			
	}
	
	private void sendHandlerMessage(int what, Object obj) {
		
		Message msg = new Message();
		msg.what = what;
		handler.sendMessage(msg);
	}
	
	private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
        	switch(msg.what){
        	case TripgMessage.HANGBAN:
        		handMessageDefault(vipOrderHttpIntfaces,VipNextOrdermain.this, msg);
        		break;
        	case UPDATE_LIST_VIEW:
				
				VipOrderCell cell = new VipOrderCell(vipNextOrdermain,
						R.layout.vipnextorder_main, listData);			
				listView.setAdapter(cell);
				listView.setOnItemClickListener(vipNextOrdermain);
				
				break;
        	case 2:
        		listView.setVisibility(View.INVISIBLE);
        		noOrder.setVisibility(View.VISIBLE);
        		break;
			default:
				
				break;
        	}
        	
        	
        }

		
		@SuppressWarnings("unchecked")
		private void handMessageDefault(VipOrderHttpIntfaces bf,
				VipNextOrdermain vipNextOrdermain, Message msg) {
			
			if(bf == null)
	    		return;
	    	if(bf.progressDialog != null)
				bf.progressDialog.dismiss();
	    	if(msg.obj == null){
	    		Toast.makeText(vipNextOrdermain, "网络链接超时",
	    				Toast.LENGTH_SHORT).show();
	    	}else{
	    		listData = (List<HashMap<String, Object>>) msg.obj;
	    	
	    		for (int i = 0; i < listData.size(); i++) {
	    			Log.e("order_id", ""+listData.get(i).get("order_id"));
	    			Log.e("order_safe", ""+listData.get(i).get("order_safe"));
				}
	    		
	    		Log.e("listdata  ---", ""+listData.size());
	    		//通知刷新界面
	    		if(listData.size() == 0){
	    			sendHandlerMessage(2, null);
	    		}else{
					sendHandlerMessage(UPDATE_LIST_VIEW, null);
	    		}
	    		
	    		
	    		
	    	}
			
		}

    };
    
    
	
	
}
