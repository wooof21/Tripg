package cn.dongtai.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import cn.internet.Exit;
import cn.model.XmlCityModel;
import cn.tripg.R;
import cn.tripg.activity.flight.RequestCode;
import cn.tripg.activity.flight.ResultCode;

public class DongTaiTableview extends Activity implements OnItemClickListener {

	public List<XmlCityModel> xmlModels;
	public ListView listView;
	private final int UPDATE_LIST_VIEW = 1;
	private static DongTaiTableview instance;
	private List<HashMap<String, Object>> listData;
	public ImageView imageViewtitle;

	@SuppressWarnings("unchecked")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dongtableview_xml);
		Exit.getInstance().addActivity(this);
		instance = this;
		@SuppressWarnings("unused")
		Intent intent = getIntent();
		imageViewtitle = (ImageView) findViewById(R.id.title_back);
		imageViewtitle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				Intent intent2 = new Intent(instance, DongTaiMain.class);
//				startActivity(intent2);
				finish();
			}
		});
		xmlModels = (List<XmlCityModel>) getIntent().getSerializableExtra(
				"xmllist");
		Log.e("xml", xmlModels.get(0).fNoString);

		listData = new ArrayList<HashMap<String, Object>>();
		listView = (ListView) findViewById(R.id.listViewd1);

		getDongTaiList();

	}

	public void getDongTaiList() {

		for (XmlCityModel xml : xmlModels) {
			listData.add(getHashMap(xml));

		}
		// 通知刷新界面
		sendHandlerMessage(UPDATE_LIST_VIEW, null);
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

				DongCellView cell = new DongCellView(instance,
						R.layout.dongtableview_xml, listData);
				listView.setAdapter(cell);
				listView.setOnItemClickListener(instance);

				break;

			default:

				break;
			}
		};
	};

	private HashMap<String, Object> getHashMap(XmlCityModel xmlCityModel) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		try {

			hashMap.put("FilghtNo", xmlCityModel.fNoString);
			hashMap.put("FlightCompany", xmlCityModel.fCompanyString);
			hashMap.put("FlightDep", xmlCityModel.fDepString);
			hashMap.put("FlightArr", xmlCityModel.fArrString);
			hashMap.put("FlightDepAirport", xmlCityModel.fDepAirportString);
			hashMap.put("FlightArrAirport", xmlCityModel.fArrAirportString);
			hashMap.put("FlightDeptimePlan", xmlCityModel.fDeptimePlanString);
			hashMap.put("FlightArrtimePlan", xmlCityModel.fArrtimePlanString);
			hashMap.put("FlightDeptime", xmlCityModel.fDeptimeString);
			hashMap.put("FlightArrtime", xmlCityModel.fArrtimeString);
			hashMap.put("FlightState", xmlCityModel.fStateString);
			hashMap.put("FlightTerminal", xmlCityModel.fTerminalString);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return hashMap;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		HashMap<String, Object> hashMap = listData.get(arg2);

		Intent intent = new Intent(instance, DongXiangqing.class);
		Bundle bundle1 = new Bundle();
		bundle1.putString("FilghtNo", (String) hashMap.get("FilghtNo"));
		bundle1.putString("FlightCompany",
				(String) hashMap.get("FlightCompany"));
		bundle1.putString("FlightDep", (String) hashMap.get("FlightDep"));
		bundle1.putString("FlightArr", (String) hashMap.get("FlightArr"));
		bundle1.putString("FlightDepAirport",
				(String) hashMap.get("FlightDepAirport"));
		bundle1.putString("FlightArrAirport",
				(String) hashMap.get("FlightArrAirport"));
		bundle1.putString("FlightDeptimePlan",
				(String) hashMap.get("FlightDeptimePlan"));
		bundle1.putString("FlightArrtimePlan",
				(String) hashMap.get("FlightArrtimePlan"));
		bundle1.putString("FlightDeptime",
				(String) hashMap.get("FlightDeptime"));
		bundle1.putString("FlightArrtime",
				(String) hashMap.get("FlightArrtime"));
		bundle1.putString("FlightState", (String) hashMap.get("FlightState"));
		bundle1.putString("FlightTerminal",
				(String) hashMap.get("FlightTerminal"));
		intent.putExtras(bundle1);
		startActivityForResult(intent, RequestCode.TO_SELECT_DONGXIANG);

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case RequestCode.TO_SELECT_DONGXIANG:
			if (resultCode == ResultCode.SUCCESS) {
				Log.e("返回tableView", "success");

			}

			break;

		}
	}

}
