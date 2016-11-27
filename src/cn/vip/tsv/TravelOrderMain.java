package cn.vip.tsv;

import java.util.ArrayList;
import java.util.HashMap;

import net.loonggg.listview.MyListView;
import net.loonggg.listview.MyListView.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.internet.Exit;
import cn.internet.Tools;
import cn.tripg.R;
import cn.tripg.widgit.ProgressDialogTripg;
import cn.youlun.main.PullToRefreshView;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TravelOrderMain extends Activity{

	private ImageView back;
	private TextView title;
	private ImageView noOrder;
	private MyListView lv;
	
	private Tools tools;
	private String tUrl = "http://www.tripg.cn/phone_api/trave/index.php/Travel/order_list?title=&PageSize=10&Page=";
	private int page = 1;
	private int maxPage;
	private ProgressDialog progressDialog;
	private ArrayList<HashMap<String, String>> list;
	private HashMap<String, String> hashMap;
	private TSVListViewAdapter tsvAdapter;
	private TravelTask tt;
	private String userName;
	
	
	private int getMaxPage() {
		return maxPage;
	}
	private void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}
	private void prepareView(){
		back = (ImageView)findViewById(R.id.tsv_main_title_back);
		title = (TextView)findViewById(R.id.tsv_main_title_text);
		noOrder = (ImageView)findViewById(R.id.tsv_wudindan);
		lv = (MyListView)findViewById(R.id.tsv_my_lv);
		
		tools = new Tools();
		list = new ArrayList<HashMap<String,String>>();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tsv_order_main);
		Exit.getInstance().addActivity(this);
		prepareView();
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		userName = tools.getUserName(getApplicationContext());
		title.setText("旅游订单");
		tt = new TravelTask();
		tt.execute(page);
		
		
		
	}

	class TravelTask extends AsyncTask<Integer, Void, String> implements OnItemClickListener{

		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			progressDialog = ProgressDialogTripg.show(TravelOrderMain.this, null, null);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();		
			if(result.equals("1")){
				tsvAdapter = new TSVListViewAdapter(TravelOrderMain.this, list, "T");
				lv.setAdapter(tsvAdapter);
				lv.setOnItemClickListener(this);
				lv.setonRefreshListener(new OnRefreshListener() {
					
					@Override
					public void onRefresh() {
						// TODO Auto-generated method stub
						page++;
						if(page > getMaxPage()){
							Toast.makeText(TravelOrderMain.this, "已到达最后一页", Toast.LENGTH_LONG).show();
							lv.onRefreshComplete();
						}else{
							tt = new TravelTask();
							tt.execute(page);
							lv.onRefreshComplete();
						}
					}
				});
			}
		}

		@Override
		protected String doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			String toast = "";
			String code = "";
			String url = tUrl + params[0] + "&username=" + userName;
			Log.e("url ---> ", url);
			String data = tools.getURL(url);
			System.out.println("data ---> " + data);
			
			try {
				JSONObject job = new JSONObject(data);
				code = job.getString("Code");
				toast = job.getString("Message");
				if(code.equals("1")){
					JSONObject job1 = job.getJSONObject("Result");
					setMaxPage(job1.getInt("AllPage"));
					JSONArray jArray = job1.getJSONArray("list");
					if(jArray.length() == 0){
						Message msg = new Message();
						msg.what = -1;
						handler.sendMessage(msg);						
					}else{
						for(int i=0,j=jArray.length();i<j;i++){
							JSONObject job2 = jArray.optJSONObject(i);
							hashMap = new HashMap<String, String>();
							Log.e("job2 go_date", job2.getString("go_date"));
							hashMap.put("go_date", job2.getString("go_date"));	
							hashMap.put("title", job2.getString("title"));
							hashMap.put("count", job2.getString("personnum"));
							hashMap.put("id", job2.getString("id"));
							hashMap.put("total", job2.getString("total"));//
							
							list.add(hashMap);
						}						
					}							
				}else{
					Message msg = new Message();
					msg.what = 0;
					msg.obj = toast;
					handler.sendMessage(msg);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return code;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Log.e("position clicked ", ""+position);
			Log.e("id", list.get(position-1).get("id"));
			Intent intent = new Intent(TravelOrderMain.this, TSVOrderDetail.class);
			intent.putExtra("id", list.get(position-1).get("id"));
			intent.putExtra("type", "T");
			startActivity(intent);
		}
		
	}
	

	@SuppressLint("HandlerLeak") 
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				noOrder.setVisibility(View.VISIBLE);
				String text = (String) msg.obj;
				Toast.makeText(TravelOrderMain.this, text, Toast.LENGTH_LONG).show();
				break;
			case -1:
				noOrder.setVisibility(View.VISIBLE);
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
