package cn.youlun.main;

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
import cn.model.YouMainModel;
import cn.tripg.R;
import cn.youlun.main.PullToRefreshView.OnFooterRefreshListener;
import cn.youlun.main.PullToRefreshView.OnHeaderRefreshListener;

public class YouLunLieBiaoMain extends Activity implements OnItemClickListener,
		OnFooterRefreshListener, OnHeaderRefreshListener {

	private ListView listView;
	private List<HashMap<String, Object>> listData;
	private static YouLunLieBiaoMain instance;
	private final int UPDATE_LIST_VIEW = 1;
	private HashMap<String, Object> hashMap;
	private List<YouMainModel> ylModel;
	PullToRefreshView mPullToRefreshView;
	private int page = 1;
	private YouItemAdp cell;
	private String lastUpdated;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.youlunliebiao);
		Exit.getInstance().addActivity(this);
		ImageView imageViewback = (ImageView) findViewById(R.id.title_youlunliel_back);
		imageViewback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				finish();
			}
		});

		instance = this;
		listData = new ArrayList<HashMap<String, Object>>();
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
		listView = (ListView) findViewById(R.id.listViewlvyoul1);


		mPullToRefreshView.setOnFooterRefreshListener(this);

		mPullToRefreshView.setOnHeaderRefreshListener(this);
		new Thread(){
			public void run(){
				youlunHttp("1");
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
				youlunHttp("2");
				cell.notifyDataSetChanged();
				Log.e("加载更多功能启动---", "---" + page);
			}
		}, 1000);
	}

	private void youlunHttp(String type) {

		String urlString = "http://www.tripg.cn/phone_api/trave/index.php/cruise/index?times=&route&counts=&cruises&PageSize=15&Page="
				+ page;
		Log.e("urlString--", urlString);
		Tools tools = new Tools();
		String resultString = tools.getURL(urlString);
		Log.e("游轮列表接口在此获取 返回值", "" + resultString);

		try {

			ylModel = new ArrayList<YouMainModel>();
			JSONObject jsonObject = new JSONObject(resultString);
			String codeString = jsonObject.getString("Code");
			if (codeString.equals("1")) {
				JSONObject resjsonObject = jsonObject.getJSONObject("Result");
				JSONArray listjArray = resjsonObject.getJSONArray("list");

				for (int i = 0; i < listjArray.length(); i++) {
					YouMainModel yMainModel = new YouMainModel();
					JSONObject yJsonObject = listjArray.getJSONObject(i);
					yMainModel.mTidString = (String) yJsonObject.getString(
							"tid").toString();
					yMainModel.mR_fangString = (String) yJsonObject.getString(
							"r_fang1").toString();
					yMainModel.mR_listpicString = "http://www.tripg.cn/cruise2013/"
							+ (String) yJsonObject.getString("r_listpic")
									.toString();
					yMainModel.mR_TitleString = (String) yJsonObject.getString(
							"r_title").toString();
					yMainModel.mR_CompanyString = (String) yJsonObject
							.getString("r_company").toString();
					yMainModel.mR_GocityString = (String) yJsonObject
							.getString("r_gocity").toString();
					yMainModel.mR_GotimeString = (String) yJsonObject
							.getString("r_gotime").toString();
					yMainModel.mR_AllcityString = (String) yJsonObject
							.getString("r_allcity").toString();
					yMainModel.mC_TitleString = (String) yJsonObject.getString(
							"c_title").toString();

					ylModel.add(yMainModel);
				}

				if (page != 1) {
					mPullToRefreshView.onFooterRefreshComplete();
					mPullToRefreshView.onHeaderRefreshComplete("最新更新: "+lastUpdated);
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

		for (YouMainModel xml : ylModel) {
			listData.add(getHashMap(xml));

		}
		if(type == 1){
			sendHandlerMessage(UPDATE_LIST_VIEW, null);
		}else{
			sendHandlerMessage(0, null);
		}
		
	}

	private HashMap<String, Object> getHashMap(YouMainModel youMainModel) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		try {

			hashMap.put("tid", youMainModel.mTidString);
			hashMap.put("r_fang1", youMainModel.mR_fangString);
			hashMap.put("r_listpic", youMainModel.mR_listpicString);
			hashMap.put("r_title", youMainModel.mR_TitleString);
			hashMap.put("r_company", youMainModel.mR_CompanyString);
			hashMap.put("r_gocity", youMainModel.mR_GocityString);
			hashMap.put("r_gotime", youMainModel.mR_GotimeString);
			hashMap.put("r_allcity", youMainModel.mR_AllcityString);
			hashMap.put("c_title", youMainModel.mC_TitleString);

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

				cell = new YouItemAdp(instance,
						R.layout.youlunadpitem, listData);
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

		String idString = (String) ylModel.get(position).mTidString;
		Intent intent = new Intent(YouLunLieBiaoMain.this,
				YouLunXiangMain.class);
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
				youlunHttp("2");

				Log.e("加载更多功能启动---", "---" + page);
			}
		}, 1000);
	}

}
