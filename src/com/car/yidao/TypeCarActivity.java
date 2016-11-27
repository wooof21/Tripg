package com.car.yidao;

import httpdelegate.CarHttpConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import tools.des.DesCodeUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.JsonReader;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import city_info_list.CatTypeInfo;
import cn.internet.Exit;
import cn.tripg.R;
import cn.tripg.activity.flight.RequestCode;
import cn.tripg.widgit.ProgressDialogTripg;

import com.example.yidaocardemo.CarActivity;

public class TypeCarActivity extends Activity implements CarList,
		OnItemClickListener {

	public ListView carlistListView;
	private ProgressDialog dialog;
	private List<HashMap<String, Object>> listData;
	private List<HashMap<String, Object>> carData;
	private ArrayList<String> firstJData;
	private static TypeCarActivity instance;
	private final int UPDATE_LIST_VIEW = 1;
	private final int UPDATE_CAR_VIEW = 2;
	private HashMap<String, Object> httpHashMap;
	public String typeString;
	public String cityString;
	private String cityName;
	public String desString;

	private JSONObject job;
	private JSONObject carJob;
	private String jsonData;
	private HashMap<String, String> cityMap, cityList;
	private HashMap<String, Object> carMap;
	private String cs_rec, city_rec;
	private String UrlType, carLvl, carName, maxLoad, outFee, fee, carImg, hr,
			km, carLvlId, carId, DepCityId, DepCity, ProductId,
			ProductBaseFeeId, PriductTypeDesc, ProductTypeId, UseStatus,
			CarModelId;
	private String firstUrl = "http://mapi.tripglobal.cn/RentCarApi.aspx?action=GetRentCarCity&IsPaging=False&token=OmYTkL2dkPHrmFg0igOUAFfcsgjLbwSJ1HkPVmNnPBY@";
	public ProgressDialog progressDialog;
	private CarActivity ca;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return false;
		}

		return true;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.type_car_xml);
		Exit.getInstance().addActivity(this);
		Intent intent = getIntent();
		typeString = intent.getExtras().getString("type");
		cityString = intent.getExtras().getString("city");
		cityName = intent.getExtras().getString("cityname");
		System.out.println("传值字段显示内容" + cityString + typeString + cityName);

		instance = this;
		carlistListView = (ListView) findViewById(R.id.listView1);
		httpHashMap = new HashMap<String, Object>();
		listData = new ArrayList<HashMap<String, Object>>();
		carData = new ArrayList<HashMap<String, Object>>();
		firstJData = new ArrayList<String>();
		ImageView backImageView = (ImageView) findViewById(R.id.title_back);

		new Thread() {
			public void run() {
				cityMap = getCityMap();

				String cs = (String) cityMap.get(cityName);
				System.out.println("Key ---> " + cityName + "Value ---> "
						+ (String) cityMap.get(cityName));
				System.out.println("1.cityString ---> " + cs);

				try {
					desString = (String) DesCodeUtils.encode("11119688",
							"GetShowProductList|2471CB5496F2A8C8");
				} catch (Exception e) {

					e.printStackTrace();
				}

				String urlString = "http://mapi.tripglobal.cn/RentCarApi.aspx?action=GetShowProductList&CityId="
						+ cs
						+ "&ProductTypeId="
						+ typeString
						+ "&token="
						+ desString;
				System.out.println(urlString);
				getCarMap(urlString);

				sendHandlerMessage(UPDATE_CAR_VIEW, null);
				// HandlerThread handlerThread = new
				// HandlerThread("handlerThread");
				// handlerThread.start();
				// MyHandler handler = new MyHandler(handlerThread.getLooper());
				// Message msg = handler.obtainMessage();
				//
				// Bundle b = new Bundle();
				// b.putString("cityString", cs);
				// msg.setData(b);
				// msg.sendToTarget();
			}
		}.start();

		// new Thread(){
		// public void run(){
		// String urlString
		// ="http://mapi.tripglobal.cn/RentCarApi.aspx?action=GetShowProductList&CityId="+cs_rec+"&ProductTypeId="+typeString+"&token="+desString;
		// System.out.println(urlString);
		// getCarMap(urlString);
		// }
		// }.start();

		// HashMap<String, Object> carMap1 = new HashMap<String, Object>();
		// carMap1 = carMap;
		// System.out.println("3.CarMap ---> " + carMap1);
		// carData.add(carMap1);
		System.out.println("carData ---> " + carData);

		// progressDialog = ProgressDialogTripg.show(this, null, null);

		backImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Intent intent = new Intent(TypeCarActivity.this,
				// CarActivity.class);
				// startActivity(intent);
				// startActivityForResult(intent, RequestCode.TO_SELECT_ORDER);
				finish();
			}
		});

	}// onCreate()

	/*************************************************************************************************/
	class MyHandler extends Handler {

		public MyHandler() {
			super();
		}

		public MyHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			// 获取bundle对象的值
			Bundle b = msg.getData();
			cs_rec = b.getString("cityString");
			System.out.println("3.cityString ---> " + cs_rec);

		}

	}

	/*****************************************************************************************************/
	protected HashMap<String, Object> getCarMap(String url) {

		String carDataS = getURL(url);
		System.out.println("1.Json Car Data ---> " + carDataS.toString());
		System.out.println("开始解析Car Data");
		try {
			carJob = new JSONObject(carDataS);
			JSONArray carArray = carJob.getJSONArray("Result");
			for (int i = 0; i < carArray.length(); i++) {
				JSONObject job1 = (JSONObject) carArray.get(i);
				JSONArray product = job1.getJSONArray("Products");
				for (int j = 0; j < product.length(); j++) {
					carMap = new HashMap<String, Object>();
					JSONObject job2 = (JSONObject) product.opt(j);
					km = job2.getString("IncludesKilometers");
					hr = job2.getString("IncludesHours");
					ProductId = job2.getString("Id");
					UseStatus = job2.getString("UseStatus");
					carMap.put("ProductId", ProductId);
					carMap.put("UseStatus", UseStatus);
					carMap.put("IncludesKilometers", km);
					carMap.put("IncludesHours", hr);
					System.out.println("ProductId ---> " + ProductId);
					System.out.println("UseStatus ---> " + UseStatus);
					System.out.println("IncludesKilometers ---> " + km);
					System.out.println("IncludesHours ---> " + hr);
					JSONObject job5 = job2.getJSONObject("City");
					DepCityId = job5.getString("Id");
					DepCity = job5.getString("CityName");
					carMap.put("DepCityId", DepCityId);
					carMap.put("DepCity", DepCity);
					System.out.println("DepCityId ---> " + DepCityId);
					System.out.println("DepCity ---> " + DepCity);
					JSONObject job7 = job2.getJSONObject("ProductType");
					ProductTypeId = job7.getString("Id");
					PriductTypeDesc = job7.getString("ProductTypeName");
					carMap.put("ProductTypeId", ProductTypeId);
					carMap.put("PriductTypeDesc", PriductTypeDesc);
					System.out.println("ProductTypeId ---> " + ProductTypeId);
					System.out.println("PriductTypeDesc ---> "
							+ PriductTypeDesc);
					JSONObject job3 = job2.getJSONObject("CarModel");
					CarModelId = job3.getString("Id");
					carName = job3.getString("CarBrandName");
					carId = job3.getString("CarBrandId");
					carLvl = job3.getString("CarLevelName");
					carLvlId = job3.getString("CarLevelId");
					maxLoad = job3.getString("MaxPeopleNumber");
					carImg = job3.getString("CarModelImage");
					carMap.put("CarModelId", CarModelId);
					carMap.put("CarBrandName", carName);
					carMap.put("CarLevelName", carLvl);
					carMap.put("MaxPeopleNumber", maxLoad);
					carMap.put("CarModelImage", carImg);
					carMap.put("CarBrandId", carId);
					carMap.put("CarLevelId", carLvlId);
					System.out.println("CarBrandId ---> " + carId);
					System.out.println("CarModelId ---> " + CarModelId);
					System.out.println("CarLevelId ---> " + carLvlId);
					System.out.println("CarBrandName ---> " + carName);
					System.out.println("CarLevelName ---> " + carLvl);
					System.out.println("MaxPeopleNumber ---> " + maxLoad);
					System.out.println("CarModelImage ---> " + carImg);
					JSONArray proFee = job2.getJSONArray("ProductBaseFees");
					for (int k = 0; k < proFee.length(); k++) {
						JSONObject job4 = (JSONObject) proFee.opt(k);
						ProductBaseFeeId = job4.getString("Id");
						carMap.put("ProductBaseFeeId", ProductBaseFeeId);
						System.out.println("ProductBaseFeeId ---> "
								+ ProductBaseFeeId);
						fee = job4.getString("Fee");
						outFee = job4.getString("TimeOutFee");
						carMap.put("Fee", fee);
						carMap.put("TimeOutFee", outFee);
						System.out.println("Fee ---> " + fee);
						System.out.println("TimeOutFee ---> " + outFee);
						// if(UseStatus.equals("0")){
						carMap.put("URLtype", "ziyou");
						carData.add(carMap);
						System.out
								.println("1.CarMap ---> " + carMap.toString());
						System.out.println("1.carData ---> " + carData);
						// }else{
						// break;
						// }

					}
				}

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return carMap;

	}

	/*********************************************************************************************************/
	protected HashMap<String, String> getCityMap() {
		HashMap<String, String> cityMap = new HashMap<String, String>();
		jsonData = getURL(firstUrl);
		System.out.println("2.jsonData ---> " + jsonData.toString());
		System.out.println("开始解析City Data");
		try {
			job = new JSONObject(jsonData);
			JSONArray jarray = job.getJSONArray("Result");
			for (int i = 0; i < jarray.length(); i++) {
				JSONObject job2 = (JSONObject) jarray.opt(i);
				cityMap.put(job2.getString("CityName"), job2.getString("Id"));
				firstJData.add(job2.getString("Id"));
				firstJData.add(job2.getString("CityName"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < firstJData.size(); i++) {
			System.out.println("firstJData ---> " + firstJData.get(i));

		}
		return cityMap;
	}

	/*********************************************************************************************************/
	private String getURL(String urlStr) {
		HttpURLConnection httpcon = null;
		InputStream in = null;
		String data = "";
		StringBuilder builder = new StringBuilder();

		try {
			URL url = new URL(urlStr);
			httpcon = (HttpURLConnection) url.openConnection();
			httpcon.setDoInput(true);
			httpcon.setDoOutput(true);
			httpcon.setUseCaches(false);
			httpcon.setRequestMethod("GET");
			in = httpcon.getInputStream();
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader bufferReader = new BufferedReader(isr);
			String input = "";
			while ((input = bufferReader.readLine()) != null) {
				data = input + data;
			}
			// readJson(data);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (httpcon != null) {
				httpcon.disconnect();
			}
		}

		return data;

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case RequestCode.TO_SELECT_ORDER:
			Log.e("返回上一页", "order----type");
			break;

		default:
			break;
		}

	}

	/**
	 * @param 获取解析数据
	 * */
	@SuppressWarnings("unchecked")
	@Override
	public void getCarList(HashMap<String, Object> hashMap) {

		// dialog.dismiss();
		progressDialog.dismiss();

		httpHashMap = (HashMap<String, Object>) hashMap.get("hashtop");
		String hString = (String) httpHashMap.get("code");

		if (hString.equals("200")) {
			ArrayList<CatTypeInfo> list = (ArrayList<CatTypeInfo>) hashMap
					.get("array");
			for (CatTypeInfo jsonObject : list) {
				listData.add(getHashMap(jsonObject));
			}

			// 通知刷新界面
			sendHandlerMessage(UPDATE_LIST_VIEW, null);
		} else {

			Toast.makeText(this, "网络异常", Toast.LENGTH_LONG).show();
			finish();
		}

	}

	private void sendHandlerMessage(int what, Object obj) {
		Log.e("sendHandlerMessage------------------------", "");
		Message msg = new Message();
		msg.what = what;
		handler.sendMessage(msg);
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		/**
		 * @param handleMessage
		 *            刷新listView的数据
		 * 
		 * */
		public void handleMessage(Message msg) {
			Log.e("handleMessage---------------------", "");
			switch (msg.what) {
			case UPDATE_LIST_VIEW:
				CarCellView cell = new CarCellView(instance,
						R.layout.type_car_xml, listData);
				carlistListView.setAdapter(cell);
				carlistListView.setOnItemClickListener(instance);

				break;
			case UPDATE_CAR_VIEW:
				// showDialog();
				// progressDialog.dismiss();
				CarCellView carcell = new CarCellView(instance,
						R.layout.type_car_xml, carData);
				carlistListView.setAdapter(carcell);
				carlistListView.setOnItemClickListener(instance);
				break;

			default:
				break;
			}
		};
	};

	/**
	 * @param getHashMap
	 *            此方法将解析的数据转化成listView 可用的数据hashMap类型
	 * 
	 * */
	private HashMap<String, Object> getHashMap(CatTypeInfo catTypeInfo) {

		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		JSONObject jsonObject = catTypeInfo.getCarType();

		try {
			hashMap.put("car_type_id", jsonObject.getString("car_type_id"));
			hashMap.put("name", jsonObject.getString("name"));
			hashMap.put("brand", jsonObject.getString("brand"));
			hashMap.put("person_number", jsonObject.getString("person_number"));
			hashMap.put("pic", jsonObject.getString("pic"));
			hashMap.put("min_response_time",
					jsonObject.getString("min_response_time"));
			hashMap.put("fee", jsonObject.getString("fee"));
			hashMap.put("time_length", jsonObject.getString("time_length"));
			hashMap.put("distance", jsonObject.getString("distance"));
			hashMap.put("fee_per_hour", jsonObject.getString("fee_per_hour"));
			hashMap.put("fee_per_kilometer",
					jsonObject.getString("fee_per_kilometer"));

			System.out.println("carType" + jsonObject.getString("name"));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return hashMap;

	}

	// arg0=父View， arg1=点击的Item的View，arg2=position（第几个item）， arg3=id(当前项的View的)
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		HashMap<String, Object> hashMap = listData.get(arg2);
		System.out.println("name*****" + (String) hashMap.get("name"));

	}

}
