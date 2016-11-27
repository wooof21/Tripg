package cn.vip.tsv;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.apache.http.cookie.SM;
import org.json.JSONException;
import org.json.JSONObject;

import cn.internet.Exit;
import cn.internet.Tools;
import cn.tripg.R;
import cn.tripg.widgit.ProgressDialogTripg;
import cn.tripg.xlistview.MarqueeTV;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TSVOrderDetail extends Activity{

	private ImageView back;
	private TextView title;
	private TextView orderStatus;
	private TextView change1;
	private MarqueeTV changeText1;
	private TextView change2;
	private TextView changeText2;
	private TextView timeTitle;
	private TextView time;
	private TextView name;
	private TextView phone;
	private TextView totalTitle;
	private TextView total;
	private LinearLayout gone1;
	private LinearLayout gone2;
	private LinearLayout bg;
	
	private Tools tools;
	private ProgressDialog progressDialog;
	private String tUrl = "http://www.tripg.cn/phone_api/trave/index.php/Travel/order_info?id=";
	private String sUrl = "http://www.tripg.cn/phone_api/trave/index.php/cruise/order_info?id=";
	private String vUrl = "http://www.tripg.cn/phone_api/trave/index.php/visa/order_info?id=";
	private String id;
	private String type;
	
	private void prepareView(){
		back = (ImageView)findViewById(R.id.tsv_detail_title_back);
		title = (TextView)findViewById(R.id.tsv_detail_title_text);
		orderStatus = (TextView)findViewById(R.id.tsv_detail_order_status);
		gone1 = (LinearLayout)findViewById(R.id.tsv_detail_change_ll1);
		change1 = (TextView)findViewById(R.id.tsv_detail_change_title);
		changeText1 = (MarqueeTV)findViewById(R.id.tsv_detail_change_text);
		gone2 = (LinearLayout)findViewById(R.id.tsv_detail_change_ll2);
		change2 = (TextView)findViewById(R.id.tsv_detail_change_title2);
		changeText2 = (TextView)findViewById(R.id.tsv_detail_change_text2);
		timeTitle = (TextView)findViewById(R.id.tsv_detail_time_title);
		time = (TextView)findViewById(R.id.tsv_detail_time_text);
		name = (TextView)findViewById(R.id.tsv_detail_name);
		phone = (TextView)findViewById(R.id.tsv_detail_phone);
		totalTitle = (TextView)findViewById(R.id.tsv_detail_change3);
		total = (TextView)findViewById(R.id.tsv_detail_change_text3);
		bg = (LinearLayout)findViewById(R.id.tsv_order_detail_bg);
		tools = new Tools();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tsv_order_detail);
		Exit.getInstance().addActivity(this);
		prepareView();
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		id = getIntent().getExtras().getString("id");
		type = getIntent().getExtras().getString("type");
		if(type.equalsIgnoreCase("T")){
			bg.setBackgroundResource(R.drawable.order_bg_travel);
			TravelOrderDetailTask tod = new TravelOrderDetailTask();
			tod.execute(id);
		}else if(type.equalsIgnoreCase("S")){
			bg.setBackgroundResource(R.drawable.order_bg_ship);
			ShipOrderDetailTask sod = new ShipOrderDetailTask();
			sod.execute(id);
		}else if(type.equalsIgnoreCase("V")){
			bg.setBackgroundResource(R.drawable.order_bg_visa);
			VisaOrderDetailTask vod = new VisaOrderDetailTask();
			vod.execute(id);
		}
		
	}

	class TravelOrderDetailTask extends AsyncTask<String, Void, Void>{

		  
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = ProgressDialogTripg.show(TSVOrderDetail.this, null, null);
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = tUrl + params[0];
			Log.e("tUrl", url);
			String data = tools.getURL(url);
			System.out.println("data ---> " + data);
			
			try {
				JSONObject job = new JSONObject(data);
				String code = job.getString("Code");
				if(code.equals("1")){
					JSONObject job1 = job.getJSONObject("Result");
					HashMap<String, String> hashMap = new HashMap<String, String>();
					hashMap.put("title", job1.getString("title"));
					hashMap.put("go_date", job1.getString("go_date"));
					hashMap.put("count", job1.getString("personnum"));
					hashMap.put("total", job1.getString("total"));
					hashMap.put("name", job1.getString("personname"));
					hashMap.put("phone", job1.getString("phone"));
					
					Message msg = new Message();
					msg.what = 1;
					msg.obj = hashMap;
					handler.sendMessage(msg);
				}else{
					Message msg = new Message();
					msg.what = -1;
					msg.obj = job.getString("Message");
					handler.sendMessage(msg);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		
	}
	
	class ShipOrderDetailTask extends AsyncTask<String, Void, Void>{

		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = ProgressDialogTripg.show(TSVOrderDetail.this, null, null);
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = sUrl + params[0];
			Log.e("sUrl", url);
			String data = tools.getURL(url);
			System.out.println("data ---> " + data);
			
			try {
				JSONObject job = new JSONObject(data);
				String code = job.getString("Code");
				if(code.equals("1")){
					JSONObject job1 = job.getJSONObject("Result");
					HashMap<String, String> hashMap = new HashMap<String, String>();
					hashMap.put("name", job1.getString("realname"));
					hashMap.put("phone", job1.getString("telphone"));
					hashMap.put("datetime", job1.getString("datetime"));
					hashMap.put("cangwei", job1.getString("cangwei"));
					
					Message msg = new Message();
					msg.what = 2;
					msg.obj = hashMap;
					handler.sendMessage(msg);
				}else{
					Message msg = new Message();
					msg.what = -1;
					msg.obj = job.getString("Message");
					handler.sendMessage(msg);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}
	
	class VisaOrderDetailTask extends AsyncTask<String, Void, Void>{

		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = ProgressDialogTripg.show(TSVOrderDetail.this, null, null);
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = vUrl + params[0];
			Log.e("vUrl", url);
			String data = tools.getURL(url);
			System.out.println("data ---> " + data);
			
			try {
				JSONObject job = new JSONObject(data);
				String code = job.getString("Code");
				if(code.equals("1")){
					JSONObject job1 = job.getJSONObject("Result");
					HashMap<String, String> hashMap = new HashMap<String, String>();
					hashMap.put("orderStatus", job1.getString("OrderStatus"));
					hashMap.put("outDate", job1.getString("OutDate"));
					hashMap.put("price", job1.getString("Price"));
					hashMap.put("country", job1.getString("CountryName"));
					hashMap.put("typeNmae", job1.getString("TypeName"));
					hashMap.put("name", job1.getString("ContactName"));
					hashMap.put("phone", job1.getString("ContactMobile"));
					
					Message msg = new Message();
					msg.what = 3;
					msg.obj = hashMap;
					handler.sendMessage(msg);
					
				}else{
					Message msg = new Message();
					msg.what = -1;
					msg.obj = job.getString("Message");
					handler.sendMessage(msg);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		
	}
	
	@SuppressLint("HandlerLeak") 
	private Handler handler = new Handler(){

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case -1:
				String text = (String) msg.obj;
				Toast.makeText(TSVOrderDetail.this, text, Toast.LENGTH_LONG).show();
				break;
			case 1:
				HashMap<String, String> hashMap = (HashMap<String, String>) msg.obj;
				title.setText("旅游订单详情");
				change1.setText("产品名称:");
				changeText1.setText(hashMap.get("title"));
				change2.setText("订单人数:");
				changeText2.setText(hashMap.get("count"));
				timeTitle.setText("出发日期:");
				time.setText(hashMap.get("go_date"));
				name.setText(hashMap.get("name"));
				phone.setText(hashMap.get("phone"));
				total.setText("￥"+hashMap.get("total"));
				break;
			case 2:
				HashMap<String, String> sMap = (HashMap<String, String>) msg.obj;
				title.setText("游轮订单详情");
				timeTitle.setText("出航时间:");
				String dt = sMap.get("datetime");
				long date = Long.parseLong(dt) * 1000L;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date d = new Date(date);
				time.setText(sdf.format(d));
				Log.e("dt", sdf.format(d));
				name.setText(sMap.get("name"));
				phone.setText(sMap.get("phone"));
				gone1.setVisibility(View.GONE);
				gone2.setVisibility(View.GONE);
				totalTitle.setText("仓        位:");
				total.setText(sMap.get("cangwei"));
				break;
			case 3:
				HashMap<String, String> vMap = (HashMap<String, String>) msg.obj;
				title.setText("签证订单详情");
				if(vMap.get("orderStatus").equals("1")){
					orderStatus.setText("新订单");
				}
				changeText1.setText(vMap.get("country"));
				changeText2.setText(vMap.get("typeNmae"));
				name.setText(vMap.get("name"));
				phone.setText(vMap.get("phone"));
				Log.e("price", vMap.get("price"));
				total.setText("￥"+vMap.get("price"));
				String od = vMap.get("outDate");
				long ms = Long.parseLong(od) * 1000L;
				SimpleDateFormat _sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date t = new Date(ms);
				time.setText(_sdf.format(t));
				break;
			default:
				break;
			}
		}
	
		
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}

	
}
