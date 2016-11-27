package cn.tripg.activity.hotel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import model.hotel.HOTELS;
import model.hotel.Hotel;
import model.hotel.Room;
import android.annotation.SuppressLint;
import android.app.Activity;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.internet.Exit;
import cn.tripg.R;
import cn.tripg.activity.flight.RequestCode;
import cn.tripg.activity.flight.ResultCode;
import cn.tripg.activity.newhotels.HotelPriceDownComparator;
import cn.tripg.activity.newhotels.HotelPriceUpComparator;
import cn.tripg.activity.newhotels.HotelStarDownComparator;
import cn.tripg.activity.newhotels.HotelStarUpComparator;
import cn.tripg.interfaces.BaseInterface;
import cn.tripg.interfaces.TripgMessage;
import cn.tripg.interfaces.impl.TestInterface;
import cn.tripg.view.hotel.HotelFilterAdapter;
import cn.tripg.xlistview.XFilterListView;
import cn.tripg.xlistview.XFilterListView.IFilterXListViewListener;
import cn.tripg.xlistview.XListView;
import cn.tripg.xlistview.XListView.IXListViewListener;

public class HotelQueryResultActivity extends Activity {
	private TestInterface test, text1;
	
	//
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TripgMessage.DEFAULT:
				handMessageDefault(test, HotelQueryResultActivity.this, msg);
				break;
			}
		}
	};

	private void handMessageDefault(BaseInterface bf, Context context,
			Message msg) {
		if (bf == null)
			return;
		if (bf.progressDialog != null)
			bf.progressDialog.dismiss();
		if (msg.obj == null) {
			Toast.makeText(context, "解析数据失败", Toast.LENGTH_SHORT).show();
		} else {
			// TO do
			HOTELS load = (HOTELS) msg.obj;
			if ("1".equals(load.getCode())) {
				hotels.addAll(load.getResult().getHotels());

			}
			onLoad();
			// 有时候我们需要修改已经生成的列表，添加或者修改数据，notifyDataSetChanged()可以在修改适配器绑定的数组后，不用重新刷新Activity，通知Activity更新ListView。
			hqAdapter.notifyDataSetChanged();
			Log.e("fresh or more model", load.Code);
		}

	}

	// xlistview
	private XListView mListView;
	private XFilterListView mFilterListView;
	private HashMap<String, String> urlMap;
	private HashMap<String, String> mapParamsQuery;
	private HotelQueryAdapter hqAdapter;
	private Handler mHandler;
	private int start = 0;
	private static int refreshCnt = 0;
	private HOTELS model;
	private HOTELS filterModel;
	private ArrayList<Hotel> hotels;
	private ArrayList<Hotel> commercialHotels;
	private ArrayList<Hotel> districtHotels;
	public ImageView imagebackView;
	public String liveTimeString;
	public String leaveTimeString;
	private ImageView sxImageView;
	private ImageView jgImageView;
	private ImageView xjImageView;
	private int priceFlag = 0;
	private int starFlag = 0;
	private Builder areaDialog;
	private String cityId;
	private ListView filterListView;
	private LinearLayout areaLayout;
	private Button commercialText;
	private Button districtText;
	private int commercialType = 2;
	private int districtType = 1;
	private String jsonData;
	private ArrayList<String> nameList;
	private HashMap<String, String> locationHashMap;
	private HOTELS modelQuery;
	private final int UPDATE_COMMERCIAL_LIST_VIEW = 0;
	private final int UPDATE_DISTRICT_LIST_VIEW = 1;
	private ImageView filter_cancle;
	private static HotelQueryResultActivity instance;
	private String locationId;
	private String cityUrl = "http://mapi.tripglobal.cn/Hotel.aspx?action=GetStaticCityLocations&CityId=";
	private String hotelUrl = "http://mapi.tripglobal.cn/Hotel.aspx?action=GetDynamicHotelList&checkInDate=";

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚刚");
	}
	private void onFilterLoad() {
		mFilterListView.stopFilterRefresh();
		mFilterListView.stopFilterLoadMore();
		mFilterListView.setRefreshTime("刚刚");
	}

	@SuppressWarnings("unchecked")
	private boolean getPassedModel() {
		Intent intent = getIntent();
		if (intent != null) {
			model = (HOTELS) intent.getSerializableExtra("model");
			System.out.println("In getPassedModel() ---> model ---> "
					+ model.toString());
			if (model == null)
				return false;
			else {
				urlMap = (HashMap<String, String>) intent
						.getSerializableExtra("urlMap");
				
				mapParamsQuery = (HashMap<String, String>) intent
						.getSerializableExtra("urlMap");
				
				System.out.println("In getPassedModel() ---> urlMap ---> "
						+ urlMap.toString());
				if (urlMap == null) {
					return false;
				} else {
					return true;
				}
			}
		}
		return false;
	}
//	@SuppressWarnings("unchecked")
//	private boolean getPassedFilterModel() {
//		Intent intent = getIntent();
//		if (intent != null) {
//			modelQuery = (HOTELS) intent.getSerializableExtra("model");
//			System.out.println("In getPassedModel() ---> modelQuery ---> "
//					+ modelQuery.toString());
//			if (modelQuery == null)
//				return false;
//			else {
//				mapParamsQuery = (HashMap<String, String>) intent
//						.getSerializableExtra("urlMap");
//				System.out.println("In getPassedModel() ---> mapParamsQuery ---> "
//						+ mapParamsQuery.toString());
//				if (mapParamsQuery == null) {
//					return false;
//				} else {
//					return true;
//				}
//			}
//		}
//		return false;
//	}

//	private void areaChooseDialog() {
//		areaDialog = new AlertDialog.Builder(this);
//		;
//		areaDialog.setTitle("请选择区域");
//		String[] area = new String[] { "商业区", "行政区" };
//		ArrayList<String> arrList = new ArrayList<String>();
//		for (int i = 0; i < area.length; i++) {
//			if (!area[i].equals("null")) {
//				arrList.add(area[i]);
//			}
//		}
//
//		areaDialog.setSingleChoiceItems(arrList.toArray(new String[0]), 0,
//				new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						dialog.dismiss();
//						dialog.toString();
//
//						switch (which) {
//						case 0:
//
//							break;
//						case 1:
//
//							break;
//						default:
//							break;
//
//						}
//					}
//				}).show();
//	}

	
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
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hotel_query);
		Exit.getInstance().addActivity(this);
		mFilterListView = (XFilterListView) findViewById(R.id.xFilterListView);
		
		boolean result = getPassedModel();
		//boolean filterResult = getPassedFilterModel();
		System.out.println("In HotelQueryResultActivity ---> getPassedModel() called ---> result ---> " + result);
		Log.e("HotelQueryResultActivity",
				"HotelQueryResultActivity-----------------");

		if (result == false) {
			finish();
			Toast.makeText(HotelQueryResultActivity.this, "获取数据失败",
					Toast.LENGTH_SHORT).show();
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		}
//		if (filterResult == false) {
//			finish();
//			Toast.makeText(HotelQueryResultActivity.this, "获取数据失败",
//					Toast.LENGTH_SHORT).show();
//			overridePendingTransition(android.R.anim.fade_in,
//					android.R.anim.fade_out);
//		}

		
		
		
		
		filter_cancle = (ImageView)findViewById(R.id.hotel_area_filter_cancle);
		filter_cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				areaLayout.setVisibility(View.INVISIBLE);				
			}
		});
		
		imagebackView = (ImageView) findViewById(R.id.title_back);
		imagebackView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				Intent intent = getIntent();
				setResult(ResultCode.FAILURE, intent);
				finish();

			}
		});
		areaLayout = (LinearLayout)findViewById(R.id.hotel_area_layout);
		commercialText = (Button)findViewById(R.id.hotel_area_commercial_textview);
		commercialText.setOnClickListener(new ButtonOnClickListener());
		districtText = (Button)findViewById(R.id.hotel_area_district_textview);
		districtText.setOnClickListener(new ButtonOnClickListener());
		filterListView = (ListView)findViewById(R.id.hotel_filter_listview);
		sxImageView = (ImageView)findViewById(R.id.hotel_shaixuan);
		sxImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(getInternet() == true){
					sxImageView.setImageResource(R.drawable.shaixuan1);
					filterListView.setVisibility(View.INVISIBLE);
					areaLayout.setVisibility(View.VISIBLE);
				}else{
					Toast.makeText(HotelQueryResultActivity.this, "网络链接已断开", Toast.LENGTH_LONG).show();
				}
				
				
			}
		});
		xjImageView = (ImageView)findViewById(R.id.hotel_xingji);
		xjImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(starFlag == 0){
					xjImageView.setImageResource(R.drawable.xingji_up);
					HotelStarUpComparator hsu = new HotelStarUpComparator();
					Collections.sort(hotels, hsu);
					for(int i=0;i<hotels.size();i++){
						System.out.println("1. hotels star up ---> " + hotels.get(i).StarCode);
					}
					hqAdapter = new HotelQueryAdapter(HotelQueryResultActivity.this, hotels);
					mListView.setAdapter(hqAdapter);
					mListView.setVisibility(View.VISIBLE);
					mFilterListView.setVisibility(View.INVISIBLE);
					starFlag = 1;
				}else{
					xjImageView.setImageResource(R.drawable.xingji_down);
					HotelStarDownComparator hsd = new HotelStarDownComparator();
					Collections.sort(hotels, hsd);
					for(int i=0;i<hotels.size();i++){
						System.out.println("1. hotels star down ---> " + hotels.get(i).StarCode);
					}
					hqAdapter = new HotelQueryAdapter(HotelQueryResultActivity.this, hotels);
					mListView.setAdapter(hqAdapter);
					mListView.setVisibility(View.VISIBLE);
					mFilterListView.setVisibility(View.INVISIBLE);
					starFlag = 0;
				}
			}
		});
		
		jgImageView = (ImageView)findViewById(R.id.hotel_jiage);
		jgImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(priceFlag == 0){
					jgImageView.setImageResource(R.drawable.jiage_up);
					HotelPriceUpComparator hp = new HotelPriceUpComparator();
					Collections.sort(hotels, hp);	
					for(int i=0;i<hotels.size();i++){
						System.out.println("1. hotels up ---> " + hotels.get(i).LowestPrice);
					}
					hqAdapter = new HotelQueryAdapter(HotelQueryResultActivity.this, hotels);
					mListView.setAdapter(hqAdapter);
					mListView.setVisibility(View.VISIBLE);
					mFilterListView.setVisibility(View.INVISIBLE);
					priceFlag = 1;
				}else{
					jgImageView.setImageResource(R.drawable.jiage_down);
					HotelPriceDownComparator hd = new HotelPriceDownComparator();
					Collections.sort(hotels, hd);
					for(int i=0;i<hotels.size();i++){
						System.out.println("2. hotels down ---> " + hotels.get(i).LowestPrice);
					}
					hqAdapter = new HotelQueryAdapter(HotelQueryResultActivity.this, hotels);
					mListView.setAdapter(hqAdapter);
					mListView.setVisibility(View.VISIBLE);
					mFilterListView.setVisibility(View.INVISIBLE);
					priceFlag = 0;
				}
				

			}
		});
		
		
		Intent intent = getIntent();
		leaveTimeString = (String) intent.getExtras().getString("leaveTime");
		liveTimeString = (String) intent.getExtras().getString("liveTime");
		cityId = intent.getExtras().getString("cityId");
		System.out.println("in hotelqueryresultactivity cityId ----------> " + cityId);
//		km = (int) intent.getExtras().getInt("nearby") * 1000;
//		ownlat = (double) intent.getExtras().getDouble("longitude");
//		ownlng = (double) intent.getExtras().getDouble("latitude");
//		System.out.println("km ---> " + km);

		mListView = (XListView) findViewById(R.id.xListView);
		mListView.setPullLoadEnable(true);
		hotels = model
				.getResult().getHotels();
		//newhotels = modelQuery.getResult().getHotels(); 
		commercialHotels = new ArrayList<Hotel>();
		districtHotels = new ArrayList<Hotel>();
//		float[] results=new float[1];  
//		if(km == 0 || ownlat == 0 || ownlng == 0){
		

			
//		}else{
//			hotels = model.getResult().getHotels();
//			System.out.println("hotels size ---> " + hotels.size());
//			for(int i=0; i<hotels.size(); i++){
//				if(hotels.get(i).Lat == null || hotels.get(i).Lon == null){
//					hotels.remove(i);
//				}
//				else{
//					double k = Double.parseDouble(hotels.get(i).Lat);
//					double l = Double.parseDouble(hotels.get(i).Lon);
//					System.out.println("ownlat ---> " + ownlat);
//					System.out.println("ownlng ---> " + ownlng);
//					System.out.println("k ---> " + k);
//					System.out.println("l ---> " + l);
//					Location.distanceBetween(ownlat, ownlng, l, k, results);
//					if((int)results[0] > km){
//						System.out.println("i ---> " + i);
//						System.out.println("hotels.get(i) ---> " + hotels.get(i).HotelName);
//						hotels.remove(hotels.get(i));
//					}
//					System.out.println("results[0] ---> " + results[0]);
//				}
//
//
//			}
//			for(int j=0;j<hotels.size();j++){
//				System.out.println("final hotels ---> " + hotels.get(j).HotelName);
//				hqAdapter = new HotelQueryAdapter(HotelQueryResultActivity.this, hotels);
//			}
//		}
		hqAdapter = new HotelQueryAdapter(HotelQueryResultActivity.this, hotels);
		mListView.setAdapter(hqAdapter);
		mListView.setVisibility(View.VISIBLE);
		mFilterListView.setVisibility(View.INVISIBLE);
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(new HotelIXListViewListener());
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// model.getResult().getHotels().get(arg2);
				Log.e("item num is  ", "" + arg2);
				arg2 = arg2 - 1;
				Log.e("item num is arg  ", "" + arg2);
				Log.e("listview", ""
						+ model.getResult().getHotels().get(arg2).Phone);
				ArrayList<Room> aryList = model.getResult().getHotels()
						.get(arg2).Rooms;
				Log.e("listview2", ""
						+ model.getResult().getHotels().get(arg2).Rooms);
				Log.e("listview3", "" + aryList.get(0).RoomName);
				Log.e("listview3" , "" + aryList.get(0).Area);
				Intent intent = new Intent(HotelQueryResultActivity.this,
						HotelOrderActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("cityId", cityId);
				bundle.putString("HotelName", model.getResult().getHotels()
						.get(arg2).HotelName);
				bundle.putString("HotelAddress", model.getResult().getHotels()
						.get(arg2).HotelAddress);
				bundle.putString("HotelId",
						model.getResult().getHotels().get(arg2).HotelId);
				bundle.putString("HotelInvStatusCode", model.getResult()
						.getHotels().get(arg2).HotelInvStatusCode);
				bundle.putString("HotelSource", model.getResult().getHotels()
						.get(arg2).HotelSource);
				bundle.putString("StarCode",
						model.getResult().getHotels().get(arg2).StarCode);
				bundle.putString("Lat",
						model.getResult().getHotels().get(arg2).Lat);
				bundle.putString("Lon",
						model.getResult().getHotels().get(arg2).Lon);
				bundle.putString("LowestPrice", model.getResult().getHotels()
						.get(arg2).LowestPrice);
				bundle.putString("Phone",
						model.getResult().getHotels().get(arg2).Phone);
				bundle.putString("OutdoorSceneImage", model.getResult()
						.getHotels().get(arg2).OutdoorSceneImage);
				bundle.putString("leaveTime", leaveTimeString);
				bundle.putString("liveTime", liveTimeString);
				intent.putExtras(bundle);
				intent.putExtra("rooms", (Serializable) aryList);

				startActivityForResult(intent, RequestCode.TO_SELECT_ORDER);

			}
		});
		mHandler = new Handler();

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case RequestCode.TO_SELECT_ORDER:
			Log.e("返回上一页", "hotelorder----query");
			break;

		default:
			break;
		}

	}

	/*********************************************************************************************************/
	private void sendHandlerMessage(int what, Object obj) {
		Log.e("sendHandlerMessage------------------------", "");
		Message msg = new Message();
		msg.what = what;
		msg.obj = obj;
		mmhandler.sendMessage(msg);
	}

	@SuppressLint("HandlerLeak")
	Handler mmhandler = new Handler() {
		/**
		 * @param handleMessage
		 *            刷新listView的数据
		 * 
		 * */
		public void handleMessage(Message msg) {
			Log.e("handleMessage---------------------", "");
			switch (msg.what) {
			case UPDATE_COMMERCIAL_LIST_VIEW:
				msg.obj = nameList;
				areaLayout.setVisibility(View.INVISIBLE);
				filterListView.setVisibility(View.VISIBLE);
				System.out.println("In handleMessage ---> " + nameList);
				HotelFilterAdapter hfac = new HotelFilterAdapter(HotelQueryResultActivity.this, R.layout.activity_hotel_query, nameList);
				filterListView.setAdapter(hfac);
				filterListView.setOnItemClickListener(new CommercialItemClickListener());
				break;
			case UPDATE_DISTRICT_LIST_VIEW:
				msg.obj = nameList;
				areaLayout.setVisibility(View.INVISIBLE);
				filterListView.setVisibility(View.VISIBLE);
				System.out.println("In handleMessage ---> " + nameList);
				HotelFilterAdapter hfad = new HotelFilterAdapter(HotelQueryResultActivity.this, R.layout.activity_hotel_query, nameList);
				filterListView.setAdapter(hfad);
				filterListView.setOnItemClickListener(new DistrictItemClickListener());
				break;
			default:
				break;
			}
		};
	};
	/***************************************************************************************************/
	private Handler handlerQuery = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TripgMessage.COM:
				handMessageDefaultQueryCom(test, HotelQueryResultActivity.this, msg);
				break;
			case TripgMessage.DIS:
				handMessageDefaultQueryDis(test, HotelQueryResultActivity.this, msg);
				break;
			}
		}
	};

	private void handMessageDefaultQueryCom(BaseInterface bf, Context context,
			Message msg) {
		if (bf == null)
			return;
		if (bf.progressDialog != null)
			bf.progressDialog.dismiss();
		if (msg.obj == null) {
			Toast.makeText(context, "解析数据失败", Toast.LENGTH_SHORT).show();
		}
		modelQuery = (HOTELS) msg.obj;
		
		//newhotels = modelQuery.getResult().getHotels();
		//newhotels1.addAll(newhotels);
		if ("1".equals(modelQuery.getCode())) {
			commercialHotels.addAll(modelQuery.getResult().getHotels());

		}
		Log.e("newhotels ---> ", modelQuery.getResult().getHotels().toString());
		onFilterLoad();
		hqAdapter = new HotelQueryAdapter(HotelQueryResultActivity.this, commercialHotels);
		mFilterListView.setAdapter(hqAdapter);
		filterListView.setVisibility(View.INVISIBLE);
		mListView.setVisibility(View.INVISIBLE);
		mFilterListView.setVisibility(View.VISIBLE);	
		mFilterListView.setFilterPullLoadEnable(true);
		mFilterListView.setFilterPullRefreshEnable(true);
		mFilterListView.setXListViewListener(new ComHotelFilterIXListViewListener());
		mFilterListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.e("item num is  ", "" + position);
				position = position - 1;
				Log.e("item num is arg  ", "" + position);
				Log.e("listview", ""
						+ modelQuery.getResult().getHotels().get(position).Phone);
				ArrayList<Room> aryList = modelQuery.getResult().getHotels()
						.get(position).Rooms;
				Log.e("listview2", ""
						+ modelQuery.getResult().getHotels().get(position).Rooms);
				Log.e("listview3", "" + aryList.get(0).RoomName);
				Log.e("listview3" , "" + aryList.get(0).Area);
				Intent intent = new Intent(HotelQueryResultActivity.this,
						HotelOrderActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("HotelName", modelQuery.getResult().getHotels()
						.get(position).HotelName);
				bundle.putString("HotelAddress", modelQuery.getResult().getHotels()
						.get(position).HotelAddress);
				bundle.putString("HotelId",
						modelQuery.getResult().getHotels().get(position).HotelId);
				bundle.putString("HotelInvStatusCode", modelQuery.getResult()
						.getHotels().get(position).HotelInvStatusCode);
				bundle.putString("HotelSource", modelQuery.getResult().getHotels()
						.get(position).HotelSource);
				bundle.putString("StarCode",
						modelQuery.getResult().getHotels().get(position).StarCode);
				bundle.putString("Lat",
						modelQuery.getResult().getHotels().get(position).Lat);
				bundle.putString("Lon",
						modelQuery.getResult().getHotels().get(position).Lon);
				bundle.putString("LowestPrice", modelQuery.getResult().getHotels()
						.get(position).LowestPrice);
				bundle.putString("Phone",
						modelQuery.getResult().getHotels().get(position).Phone);
				bundle.putString("OutdoorSceneImage", modelQuery.getResult()
						.getHotels().get(position).OutdoorSceneImage);
				bundle.putString("leaveTime", leaveTimeString);
				bundle.putString("liveTime", liveTimeString);
				intent.putExtras(bundle);
				intent.putExtra("rooms", (Serializable) aryList);

				startActivityForResult(intent, RequestCode.TO_SELECT_ORDER);
			}
			
		});
	}
	/*********************************************************************************************************/
	/***************************************************************************************************/

	private void handMessageDefaultQueryDis(BaseInterface bf, Context context,
			Message msg) {
		if (bf == null)
			return;
		if (bf.progressDialog != null)
			bf.progressDialog.dismiss();
		if (msg.obj == null) {
			Toast.makeText(context, "解析数据失败", Toast.LENGTH_SHORT).show();
		}
		modelQuery = (HOTELS) msg.obj;
		
		//newhotels = modelQuery.getResult().getHotels();
		//newhotels1.addAll(newhotels);
		if ("1".equals(modelQuery.getCode())) {
			districtHotels.addAll(modelQuery.getResult().getHotels());

		}
		Log.e("districtHotels ---> ", modelQuery.getResult().getHotels().toString());
		onFilterLoad();
		hqAdapter = new HotelQueryAdapter(HotelQueryResultActivity.this, districtHotels);
		mFilterListView.setAdapter(hqAdapter);
		filterListView.setVisibility(View.INVISIBLE);
		mListView.setVisibility(View.INVISIBLE);
		mFilterListView.setVisibility(View.VISIBLE);	
		mFilterListView.setFilterPullLoadEnable(true);
		mFilterListView.setFilterPullRefreshEnable(true);
		mFilterListView.setXListViewListener(new DisHotelFilterIXListViewListener());
	}
	/*********************************************************************************************************/
	protected HashMap<String, String> getLocationMap(String url){
		HashMap<String, String> locationMap = new HashMap<String, String>();
		nameList = new ArrayList<String>();
	
				jsonData = getURL(url);
				System.out.println("2.jsonData ---> " + jsonData.toString());
				System.out.println("开始解析City Data");
				try {
					JSONObject job = new JSONObject(jsonData);
					JSONArray jarray = job.getJSONArray("Result");
					for(int i=0;i<jarray.length();i++){
						JSONObject job2 = (JSONObject) jarray.opt(i);
						locationMap.put("Name", job2.getString("Name"));
						nameList.add(job2.getString("Name"));
						locationMap.put(job2.getString("Name"), job2.getString("LocationId"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			return locationMap;
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
	
	class ComHotelFilterIXListViewListener implements IFilterXListViewListener{
		private int pageIndex = 1;
		
		public ComHotelFilterIXListViewListener(){
			test = new TestInterface(HotelQueryResultActivity.this, handlerQuery);
		}
		
		@Override
		public void onFilterRefresh() {
			// TODO Auto-generated method stub
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					pageIndex++;
					mapParamsQuery.put("pageIndex", pageIndex + "");
					test = new TestInterface(HotelQueryResultActivity.this, handlerQuery);
					String testUrl = test.prepareGetFullURL(mapParamsQuery);
					test.disableProgressDialog();
					test.getModelFromGET(testUrl, TripgMessage.COM, "0");
					commercialHotels.addAll(modelQuery.getResult().getHotels());
					for (int i = 0; i < commercialHotels.size(); i++) {
						System.out.println("onFilterRefresh() newhotels ---> "
								+ commercialHotels.get(i).HotelName);
					}
				}
				
			}, 2000);
		}

		@Override
		public void onFilterLoadMore() {
			// TODO Auto-generated method stub
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {

					pageIndex++;
					mapParamsQuery.put("pageIndex", pageIndex + "");
					
					test = new TestInterface(HotelQueryResultActivity.this, handlerQuery);
					String testUrl = test.prepareGetFullURL(mapParamsQuery);
					test.disableProgressDialog();
					test.getModelFromGET(testUrl, TripgMessage.COM, "0");
					commercialHotels.addAll(modelQuery.getResult().getHotels());
					for (int i = 0; i < commercialHotels.size(); i++) {
						System.out.println("onFilterLoadMore() newhotels ---> "
								+ commercialHotels.get(i).HotelName);
					}
				}
			}, 2000);
		}
		
	}
	
	class DisHotelFilterIXListViewListener implements IFilterXListViewListener{
		private int pageIndex = 1;
		
		public DisHotelFilterIXListViewListener(){
			test = new TestInterface(HotelQueryResultActivity.this, handlerQuery);
		}
		
		@Override
		public void onFilterRefresh() {
			// TODO Auto-generated method stub
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					pageIndex++;
					mapParamsQuery.put("pageIndex", pageIndex + "");
					test = new TestInterface(HotelQueryResultActivity.this, handlerQuery);
					String testUrl = test.prepareGetFullURL(mapParamsQuery);
					test.disableProgressDialog();
					test.getModelFromGET(testUrl, TripgMessage.DIS, "0");
					districtHotels.addAll(modelQuery.getResult().getHotels());
					for (int i = 0; i < districtHotels.size(); i++) {
						System.out.println("onFilterRefresh() newhotels ---> "
								+ districtHotels.get(i).HotelName);
					}
				}
				
			}, 2000);
		}

		@Override
		public void onFilterLoadMore() {
			// TODO Auto-generated method stub
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {

					pageIndex++;
					mapParamsQuery.put("pageIndex", pageIndex + "");
					
					test = new TestInterface(HotelQueryResultActivity.this, handlerQuery);
					String testUrl = test.prepareGetFullURL(mapParamsQuery);
					test.disableProgressDialog();
					test.getModelFromGET(testUrl, TripgMessage.DIS, "0");
					districtHotels.addAll(modelQuery.getResult().getHotels());
					for (int i = 0; i < districtHotels.size(); i++) {
						System.out.println("onFilterLoadMore() newhotels ---> "
								+ districtHotels.get(i).HotelName);
					}
				}
			}, 2000);
		}
		
	}
	
	
	class HotelIXListViewListener implements IXListViewListener {
		private int pageIndex = 1;

		public HotelIXListViewListener() {
			test = new TestInterface(HotelQueryResultActivity.this, handler);
		}

		@Override
		public void onRefresh() {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {

					pageIndex++;
					urlMap.put("pageIndex", pageIndex + "");
					String testUrl = test.prepareGetFullURL(urlMap);
					test.disableProgressDialog();
					test.getModelFromGET(testUrl, TripgMessage.DEFAULT, "0");
					hotels = model.getResult().getHotels();
					for (int i = 0; i < hotels.size(); i++) {
						System.out.println("onRefresh() hotels ---> "
								+ hotels.get(i).HotelName);
					}
				}
			}, 2000);
		}


		@Override
		public void onLoadMore() {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {

					pageIndex++;
					urlMap.put("pageIndex", pageIndex + "");
					String testUrl = test.prepareGetFullURL(urlMap);
					test.disableProgressDialog();
					test.getModelFromGET(testUrl, TripgMessage.DEFAULT, "0");
					hotels = model.getResult().getHotels();
					for (int i = 0; i < hotels.size(); i++) {
						System.out.println("onLoadMore() hotels ---> "
								+ hotels.get(i).HotelName);
					}
				}
			}, 2000);
		}





	}
	class DistrictItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			TextView tV = (TextView)view.findViewById(R.id.hotel_filter_item);
			String location = tV.getText().toString();
			System.out.println("ItemClickListener Item Clicked --->" + location);
			locationId = locationHashMap.get(location);
			System.out.println("ItemClickListener locationId --->" + locationId);
			
			mapParamsQuery.put("districtId", locationId);
			test = new TestInterface(HotelQueryResultActivity.this, handlerQuery);
			String testUrl = test.prepareGetFullURL(mapParamsQuery);
			test.getModelFromGET(testUrl, TripgMessage.DIS, "0");
		}
		
	}
	class CommercialItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			TextView tV = (TextView)view.findViewById(R.id.hotel_filter_item);
			String location = tV.getText().toString();
			System.out.println("ItemClickListener Item Clicked --->" + location);
			locationId = locationHashMap.get(location);
			System.out.println("ItemClickListener locationId --->" + locationId);
//			mapParamsQuery = new HashMap<String, String>();
//			mapParamsQuery.clear();
//			mapParamsQuery.put("action", "GetDynamicHotelList");
//			mapParamsQuery.put("checkInDate", liveTimeString);
//			mapParamsQuery.put("checkOutDate", leaveTimeString);
//			mapParamsQuery.put("cityId", cityId);// change
//			mapParamsQuery.put("pageIndex", "1");
//			mapParamsQuery.put("pageSize", "20");
			mapParamsQuery.put("commercialLocationId", locationId);
			test = new TestInterface(HotelQueryResultActivity.this, handlerQuery);
			String testUrl = test.prepareGetFullURL(mapParamsQuery);
			test.getModelFromGET(testUrl, TripgMessage.COM, "0");
			
		}
		
	}
	class ButtonOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId() == R.id.hotel_area_commercial_textview){
				System.out.println("11111");	
				new Thread(){
					public void run(){
						String url = cityUrl + cityId + "&Type=" + commercialType;
						System.out.println("1. shuaixuan url ---> " + url);
						if(getInternet() == true){
							locationHashMap = getLocationMap(url);		
							for(int i=0;i<nameList.size();i++){
								System.out.println("11111 ---> " + nameList.get(i));
							}						
							sendHandlerMessage(UPDATE_COMMERCIAL_LIST_VIEW, null);	
						}else{
							Toast.makeText(HotelQueryResultActivity.this, "网络链接已断开", Toast.LENGTH_LONG).show();
						}
						
					}
				}.start();
				
				
				
			}else if(v.getId() == R.id.hotel_area_district_textview){
				System.out.println("22222");
				final ArrayList<String> areaList = new ArrayList<String>();
				new Thread(){
					public void run(){
						String url = cityUrl + cityId + "&Type=" + districtType;
						System.out.println("2. shuaixuan url ---> " + url);
						if(getInternet() == true){
							locationHashMap = getLocationMap(url);
							for(int i=0;i<nameList.size();i++){
								System.out.println("222222 ---> " + nameList.get(i));
							}
							sendHandlerMessage(UPDATE_DISTRICT_LIST_VIEW, nameList);	
						}else{
							Toast.makeText(HotelQueryResultActivity.this, "网络链接已断开", Toast.LENGTH_LONG).show();
						}
						
					}
				}.start();
			}else{
				
				areaLayout.setVisibility(View.INVISIBLE);
			}
		}
		
	}
}
