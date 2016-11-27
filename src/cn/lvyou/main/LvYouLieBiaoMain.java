package cn.lvyou.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import cn.internet.Exit;
import cn.internet.Tools;
import cn.model.LvYouMmodel;
import cn.tripg.R;
import cn.youlun.main.PullToRefreshView;
import cn.youlun.main.PullToRefreshView.OnFooterRefreshListener;
import cn.youlun.main.PullToRefreshView.OnHeaderRefreshListener;

public class LvYouLieBiaoMain extends Activity implements OnItemClickListener,OnFooterRefreshListener, OnHeaderRefreshListener{

	private ListView listView;
	private List<HashMap<String, Object>> listData;
	private static LvYouLieBiaoMain instance;
	private final int UPDATE_LIST_VIEW = 1;
	private HashMap<String, Object> hashMap;
	private List<LvYouMmodel> ylModel;
	PullToRefreshView mPullToRefreshView;
	private int page = 1;
	private LvYouAdpitem cell;
	private String lastUpdated;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lvyouliebiao);
		Exit.getInstance().addActivity(this);
		ImageView imageViewback = (ImageView)findViewById(R.id.title_lvyoul_back);
		imageViewback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				finish();
			}
		});
	
		instance = this;
		listData = new ArrayList<HashMap<String, Object>>();
		mPullToRefreshView = (PullToRefreshView)findViewById(R.id.main_pull_refresh_view);
		listView =	(ListView) findViewById(R.id.listViewlvyoul1);
		
		mPullToRefreshView.setOnFooterRefreshListener(this);

		mPullToRefreshView.setOnHeaderRefreshListener(this);
		
		
		
		new Thread(){
			public void run(){
				LvyouHttp("1");
			}
			
		}.start();
		
		
		
	}
	
	public void onFooterRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				Date curDate = new Date(System.currentTimeMillis());
				lastUpdated = sdf.format(curDate);
				Log.e("lastUpdated", lastUpdated);
				page++;
				LvyouHttp("2");
				cell.notifyDataSetChanged();
				Log.e("加载更多功能启动---", "---");
			}
		}, 1000);
	}
	
	
	
	private void LvyouHttp(String type) {

		String urlString = "http://www.tripg.cn/phone_api/trave/index.php/Travel?title=&PageSize=15&Page="+page;
		Log.e("urlString--", urlString);
		Tools tools = new Tools();
		String resultString = tools.getURL(urlString);
		Log.e("旅游视图接口在此获取 返回值", "" + resultString);

		try {

			ylModel = new ArrayList<LvYouMmodel>();
			JSONObject jsonObject = new JSONObject(resultString);
			String codeString = jsonObject.getString("Code");
			if (codeString.equals("1")) {
				JSONObject resjsonObject = jsonObject.getJSONObject("Result");
				JSONArray listjArray = resjsonObject.getJSONArray("list");

				for (int i = 0; i < listjArray.length(); i++) {
					
					LvYouMmodel yMainModel = new LvYouMmodel();
					JSONObject yJsonObject = listjArray.getJSONObject(i);
					yMainModel.lmidString = (String) yJsonObject.getString("id").toString();
					yMainModel.lmtitString = (String) yJsonObject.getString("title").toString();
					yMainModel.lmpriString = (String) yJsonObject.getString("price").toString();
					yMainModel.lmupString = "http://www.tripg.cn/"+ (String)yJsonObject.getString("upload").toString();
					yMainModel.lmteseString = (String) yJsonObject.getString("tese").toString();

					ylModel.add(yMainModel);
				}
				
				if (page != 1) {
					mPullToRefreshView.onFooterRefreshComplete();
					mPullToRefreshView.onHeaderRefreshComplete("最近更新: "+lastUpdated);
				}
				if (ylModel.size() > 0) {
					// 通知刷新界面
					if(type.equals("1")){
						getDongTaiList(1);
					}else{
						getDongTaiList(2);
					}
					
				}

			} else {
				Toast.makeText(getApplicationContext(), "获取数据失败",
						Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			// TODO: handle exception
			Log.e("Exception", "---" + e);

		}

	}
	
	
	
	public void getDongTaiList(int type) {

		for (LvYouMmodel xml : ylModel) {
			listData.add(getHashMap(xml));

		}
		if(type == 1){
			sendHandlerMessage(UPDATE_LIST_VIEW, null);
		}else{
			sendHandlerMessage(0, null);
		}
		
		// 通知刷新界面
		
	}
	
	private HashMap<String, Object> getHashMap(LvYouMmodel youMainModel) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		try {

			hashMap.put("tid", youMainModel.lmidString);
			hashMap.put("title", youMainModel.lmtitString);
			hashMap.put("price", youMainModel.lmpriString);
			hashMap.put("upload", youMainModel.lmupString);
			hashMap.put("tese", youMainModel.lmteseString);
		
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		return hashMap;
	}
	
	
	private void sendHandlerMessage(int what, Object obj) {

		Message msg = new Message();
		msg.what = what;
		handler.sendMessage(msg);

	}


	Handler handler = new Handler() {
		/**
		 * @param handleMessage
		 *            刷新listView的数据
		 * 
		 * */
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_LIST_VIEW:

				cell = new LvYouAdpitem(instance,
						R.layout.lvyouadpitem, listData);
				listView.setAdapter(cell);
				listView.setOnItemClickListener(instance);

				
				
				break;
			case 0:
				cell.notifyDataSetChanged();
				break;

			default:

				break;
			}
		};
	};


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
		String idString = (String)ylModel.get(position).lmidString;
		Intent intent = new Intent(LvYouLieBiaoMain.this, LvYouXiangMain.class);
		intent.putExtra("tid", idString);
		startActivity(intent);
		
		
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		mPullToRefreshView.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				Date curDate = new Date(System.currentTimeMillis());
				lastUpdated = sdf.format(curDate);
				Log.e("lastUpdated", lastUpdated);
				page++;
				cell.notifyDataSetChanged();
				LvyouHttp("1");
				Log.e("加载更多功能启动---", "---");
			}
		}, 1000);
	}

	
	
	
	
	
	
	
	
	
	
	
}
