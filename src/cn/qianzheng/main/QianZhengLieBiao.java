package cn.qianzheng.main;

import java.util.ArrayList;
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
import cn.model.QianXiangmodel;
import cn.tripg.R;

public class QianZhengLieBiao extends Activity implements OnItemClickListener{
	
	private ListView listView;
	private List<HashMap<String, Object>> listData;
	private static QianZhengLieBiao instance;
	private final int UPDATE_LIST_VIEW = 1;
	private List<QianXiangmodel> list;
	public String idString;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qianzliebiao);
		Exit.getInstance().addActivity(this);
		ImageView imageViewback = (ImageView)findViewById(R.id.title_qianzhengl_back);
		imageViewback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				finish();
			}
		});
		instance = this;
		listData = new ArrayList<HashMap<String, Object>>();
		listView =	(ListView) findViewById(R.id.listViewqianzl1);
		idString = getIntent().getStringExtra("id");
		
		new Thread(){
			public void run(){
				httpCountry();
				
				if (list.size() >0) {
					getDongTaiList();
				}
			}
		}.start();
		
		
		
	
	}
	
	private void httpCountry(){
		
		String urlString = "";
		if (idString.equals("0")) {
			urlString = "http://www.tripg.cn/phone_api/trave/index.php/visa/get_Visas?";
		}else {
			urlString = "http://www.tripg.cn/phone_api/trave/index.php/visa/get_Visas?cid="+idString;
		}
		Log.e("urlString----", ""+urlString);
		
		Tools tools = new Tools();
		String resultString = tools.getURL(urlString);
		Log.e("签证列表接口在此获取 返回值", "" + resultString);
		
		try {
			list = new ArrayList<QianXiangmodel>();
			
			JSONArray jsonArray = new JSONArray(resultString);
			
			for (int i = 0; i < jsonArray.length(); i++) {
				QianXiangmodel qx = new QianXiangmodel();
				JSONObject jsObject = jsonArray.getJSONObject(i);
				qx.productidString = (String)jsObject.getString("ProductId").toString();
				qx.countryNaString = (String)jsObject.getString("CountryName").toString();
				qx.sellPriString = (String)jsObject.getString("SellPrice").toString();
				qx.typeNaString= (String)jsObject.getString("TypeName").toString();
				qx.imageUrlString = "http://210.72.225.98:8080/"+(String)jsObject.getString("Flag").toString();
				
				list.add(qx);				
				
			}
			
			
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("Exception", "---"+e);
		}
		
		
	}
	
	
	public void getDongTaiList() {

		for (QianXiangmodel xml : list) {
			listData.add(getHashMap(xml));

		}
		// 通知刷新界面
		sendHandlerMessage(UPDATE_LIST_VIEW, null);
	}
	
	private HashMap<String, Object> getHashMap(QianXiangmodel qianzhengmodel) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		try {

			hashMap.put("ProductId", qianzhengmodel.productidString);
			hashMap.put("CountryName", qianzhengmodel.countryNaString);
			hashMap.put("SellPrice", qianzhengmodel.typeNaString);
			hashMap.put("TypeName", qianzhengmodel.sellPriString);
			hashMap.put("Flag", qianzhengmodel.imageUrlString);
			


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

				QianItemAdp cell = new QianItemAdp(instance,
						R.layout.qianliebiaoitem, listData);
				listView.setAdapter(cell);
				listView.setOnItemClickListener(instance);

				
				
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
		
		Log.e("position----", ""+position);
		QianXiangmodel q = list.get(position);
		Log.e("QianXiangmodel----", ""+q.productidString);
		
		Intent intent = new Intent(QianZhengLieBiao.this, QianXiangMain.class);
		intent.putExtra("id", q.productidString);
		startActivity(intent);
		
	}

}
