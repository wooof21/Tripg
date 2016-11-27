package cn.tripg.activity.hotel;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import model.hotel.CommercialDistrict;
import model.hotel.HOTELS;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.internet.Exit;
import cn.tripg.R;
import cn.tripg.activity.flight.DateSelectActivity;
import cn.tripg.activity.flight.FlightActivity;
import cn.tripg.activity.flight.MainActivity;
import cn.tripg.activity.flight.RequestCode;
import cn.tripg.activity.flight.ResultCode;
import cn.tripg.interfaces.BaseInterface;
import cn.tripg.interfaces.TripgMessage;
import cn.tripg.interfaces.impl.CommercialLocationDistrictInterface;
import cn.tripg.interfaces.impl.TestInterface;

public class HotelMainActivity extends Activity {
	private HashMap<String, String> mapParamsQuery;// for hotel query
	private HashMap<String, String> mapParamsCD;// for commercial location and
												// district selection
	// 周边酒店 常规查询
	private Button normalSurroundBtn;
	private NormalSurroundBtnOnClickListener normalSurroundBtnOnClickListener;
	private SeekBarOnSeekBarChangeListener seekBarOnSeekBarChangeListener;
	private KeyWordsBtnOnClickListener keyWordsBtnOnClickListener;
	private PriceStarBtnOnClickListener priceStarBtnOnClickListener;
	// 拖动条
	private SeekBar seekBar;
	// 周边5公里
	private TextView textViewSurroundText;
	// 入住城市 长春
	private Button liveCityBtn;
	private FrameLayout liveCityBtn1;
	// 入住时间
	private Button liveTimeBtn;
	// 退房时间
	private Button leaveTimeBtn;
	// 关键字
	private Button keyWordsBtn;
	// 价格星级
	private Button priceStarBtn;

	private HOTELS modelQuery = null;
	private CommercialDistrict modelCD = null;
	private TestInterface test;
	private CommercialLocationDistrictInterface cdi;
	private Button query;
	private String liveCityStr;
	private String liveCityCode;
	private String commLocationId = "";
	private String distLocationId = "";
	private String liveTime = "";
	private String leaveTime = "";
	@SuppressLint("HandlerLeak")
	private Handler handlerQuery = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TripgMessage.DEFAULT:
				handMessageDefaultQuery(test, HotelMainActivity.this, msg);
				break;
			}
		}
	};
	@SuppressLint("HandlerLeak")
	private Handler handlerCD = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TripgMessage.DEFAULT:
				handMessageDefaultCD(cdi, HotelMainActivity.this, msg);
				break;
			}
		}
	};

	private void handMessageDefaultQuery(BaseInterface bf, Context context,
			Message msg) {
		if (bf == null)
			return;
		if (bf.progressDialog != null)
			bf.progressDialog.dismiss();
		if (msg.obj == null) {
			Toast.makeText(context, "解析数据失败", Toast.LENGTH_SHORT).show();
		} else {
			// TO do
			modelQuery = (HOTELS) msg.obj;
			Log.e("model", modelQuery.Code);
			Intent intent = new Intent(HotelMainActivity.this,
					HotelQueryResultActivity.class);
			intent.putExtra("model", (Serializable) modelQuery);
			intent.putExtra("urlMap", (Serializable) mapParamsQuery);
			intent.putExtra("leaveTime", leaveTime);
			intent.putExtra("liveTime", liveTime);
			intent.putExtra("cityId", liveCityCode);
			// startActivity(intent);
			startActivityForResult(intent, RequestCode.TO_HOTEL_QUERY);

			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		}
	}

	private void handMessageDefaultCD(BaseInterface bf, Context context,
			Message msg) {
		if (bf == null)
			return;
		if (bf.progressDialog != null)
			bf.progressDialog.dismiss();
		if (msg.obj == null) {
			Toast.makeText(context, "解析数据失败", Toast.LENGTH_SHORT).show();
		} else {
			// TO do
			modelCD = (CommercialDistrict) msg.obj;
			Log.e("modelCD", modelCD.Code);
			Intent intent = new Intent(HotelMainActivity.this,
					SelectCommercialLocationDistrictActivity.class);
			intent.putExtra("model", (Serializable) modelCD);

			startActivityForResult(intent, RequestCode.TO_SELECT_CD);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		}
	}

	private void startAnmi(BaseInterface test) {
		if (test == null) {
			return;
		}
		if (test.progressDialog == null) {
			return;
		}
		ImageView imageView = (ImageView) test.progressDialog
				.findViewById(R.id.loading_img);
		AnimationDrawable animationDrawable = (AnimationDrawable) imageView
				.getDrawable();
		animationDrawable.start();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		Log.e("focus change", "--");
		startAnmi(test);
		startAnmi(cdi);
	}

	private void prepareAllView() {

		// 周边酒店 常规查询
		liveCityCode = "0901";
		normalSurroundBtn = (Button) findViewById(R.id.live_in_bg_right);
		normalSurroundBtnOnClickListener = new NormalSurroundBtnOnClickListener();
		normalSurroundBtn.setOnClickListener(normalSurroundBtnOnClickListener);
		// 拖动条
		seekBar = (SeekBar) findViewById(R.id.seek_bar);
		seekBarOnSeekBarChangeListener = new SeekBarOnSeekBarChangeListener();
		seekBar.setOnSeekBarChangeListener(seekBarOnSeekBarChangeListener);
		// 周边5公里
		textViewSurroundText = (TextView) findViewById(R.id.changed_text);
		// 入住城市 长春
		liveCityBtn = (Button) findViewById(R.id.live_in_bg_left);
		liveCityBtn.setOnClickListener(new LiveCityBtnOnClickListener());
		liveCityBtn1 = (FrameLayout) findViewById(R.id.live_in_bg_left1);
		// 入住时间
		liveTimeBtn = (Button) findViewById(R.id.live_time_bg);
		liveTimeBtn.setOnClickListener(new LiveTimeBtnOnClickListener());
		// 退房时间
		leaveTimeBtn = (Button) findViewById(R.id.leave_time_bg);
		leaveTimeBtn.setOnClickListener(new LeaveTimeBtnOnClickListener());
		// 关键字
		keyWordsBtn = (Button) findViewById(R.id.key_words);
		keyWordsBtnOnClickListener = new KeyWordsBtnOnClickListener();
		keyWordsBtn.setOnClickListener(keyWordsBtnOnClickListener);
		// 价格星级
		priceStarBtn = (Button) findViewById(R.id.price_star_bg);
		priceStarBtnOnClickListener = new PriceStarBtnOnClickListener();
		priceStarBtn.setOnClickListener(priceStarBtnOnClickListener);

		query = (Button) findViewById(R.id.commit);
		query.setOnClickListener(new QueryOnClickListener());
		ImageView backimageView = (ImageView) findViewById(R.id.title_back);
		backimageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				Intent ticket = new Intent(HotelMainActivity.this,
//						MainActivity.class);
//				startActivity(ticket);
				finish();

			}
		});
		setLiveTime();
		setLeaveTime();
	}

	private void setLiveTime() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		String time = year + "-" + month + "-" + day;
		liveTime = time;
		if (liveTimeBtn != null)
			liveTimeBtn.setText(time);
	}

	private void setLeaveTime() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, 1);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		String time = year + "-" + month + "-" + day;
		leaveTime = time;
		if (leaveTimeBtn != null)
			leaveTimeBtn.setText(year + "-" + month + "-" + day);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case RequestCode.TO_SELECT_LIVE_CITY:
			if (resultCode == ResultCode.SUCCESS) {
				Bundle city = data.getExtras();
				liveCityStr = city.getString("cityName");
				liveCityBtn.setText("          " + liveCityStr);
				liveCityCode = CityMapping.cityMap.get(liveCityStr);
				Log.e("liveCityCode ---> ", liveCityCode);
				Log.e("liveCityStr ---->", liveCityStr);
				// liveCityCode = ;// you have to finish this
			}
			break;
		case RequestCode.TO_SELECT_LIVE_TIME:
			if (resultCode == ResultCode.SUCCESS) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Bundle bundle = data.getExtras();
				liveTime = bundle.getString("date");
						
				Date date1 = java.sql.Date.valueOf(liveTime); 
				Calendar calendar1   =  getCalendarDate(date1); 
				calendar1.setTime(date1); 
				calendar1.add(calendar1.DATE,0);//把日期往后增加一天.整数往后推,负数往前移动 
			    date1=calendar1.getTime();   //这个时间就是日期往后推一天的结果 
			    sdf.format(date1);
				Log.e("liveTime ---", sdf.format(date1));
				
				Date date = java.sql.Date.valueOf(liveTime); 
				Calendar calendar   =  getCalendarDate(date); 
				calendar.setTime(date); 
				calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
			    date=calendar.getTime();   //这个时间就是日期往后推一天的结果 
			    sdf.format(date);
			    Log.e("next day ---", sdf.format(date));
			    
				Log.e("bundle ----",liveTime);
				liveTimeBtn.setText(sdf.format(date1));
				String leaTime = leaveTimeBtn.getText().toString().replace("-", "");
				String livTime = liveTimeBtn.getText().toString().replace("-", "");

				if((Long.parseLong(leaTime) - Long.parseLong(livTime)) >= 0){
					
				}else{
					leaveTime = (String)sdf.format(date);
					leaveTimeBtn.setText(leaveTime);
				}
				
				
			}
			break;
		case RequestCode.TO_SELECT_LEAVE_TIME:
			if (resultCode == ResultCode.SUCCESS) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Bundle bundle = data.getExtras();
				leaveTime = bundle.getString("date");
				Date date1 = java.sql.Date.valueOf(leaveTime); 
				Calendar calendar1   =  getCalendarDate(date1); 
				calendar1.setTime(date1); 
				calendar1.add(calendar1.DATE,0);//把日期往后增加一天.整数往后推,负数往前移动 
			    date1=calendar1.getTime();   //这个时间就是日期往后推一天的结果 
			    sdf.format(date1);
				Log.e("liveTime ---", sdf.format(date1));
				
				leaveTimeBtn.setText(sdf.format(date1));
			}
			break;
		case RequestCode.TO_SELECT_CD:
			if (resultCode == ResultCode.SUCCESS) {
				Bundle bundle = data.getExtras();
				String locationId = bundle.getString("locationId");
				String locationName = bundle.getString("locationName");
				commLocationId = locationId;
				distLocationId = locationId;
				keyWordsBtn.setText(locationName);
				Log.e("return location id = ", locationId);
			}
			if (resultCode == ResultCode.FAILURE) {
				keyWordsBtnOnClickListener.state = KeyWordsBtnOnClickListener.State.NO_LIMIT;
				keyWordsBtn.setText("不限");
			}
			break;
		case RequestCode.TO_HOTEL_QUERY:
			Log.e("返回", "接收界面");
			break;
		}
	}
	public Calendar getCalendarDate(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar;
	}

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
		setContentView(R.layout.activity_hotel);
		Exit.getInstance().addActivity(this);
		prepareAllView();
		mapParamsQuery = new HashMap<String, String>();
		mapParamsCD = new HashMap<String, String>();
	}

	class NormalSurroundBtnOnClickListener implements OnClickListener {
		private int state = 0;

		public int getState() {
			return this.state;
		}

		@Override
		public void onClick(View v) {
			if (state == 0) {
				state = 1;
				normalSurroundBtn
						.setBackgroundResource(R.drawable.live_in_right);
				liveCityBtn1.setVisibility(View.VISIBLE);
				liveCityBtn.setVisibility(View.GONE);
			} else {
				state = 0;
				normalSurroundBtn
						.setBackgroundResource(R.drawable.live_in_right1);
				liveCityBtn1.setVisibility(View.GONE);
				liveCityBtn.setVisibility(View.VISIBLE);
			}
		}
	}

	class LiveCityBtnOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Log.e("zzz", "select city");
			// start select city activity.
			// startActivity for result.
			Intent intent = new Intent(HotelMainActivity.this,
					SelectCityActivity.class);
			startActivityForResult(intent, RequestCode.TO_SELECT_LIVE_CITY);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		}
	}

	class LiveTimeBtnOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// start select date activity.
			// startActivity for result.
			Log.e("zzz", "select live time");
			Intent intent = new Intent(HotelMainActivity.this,
					DateSelectActivity.class);
			Bundle bundle = new Bundle();
			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int day = c.get(Calendar.DAY_OF_MONTH);
			String currentDate = year + "-" + month + "-" + day;
			System.out.println("HotelMainActivity passing Date, date ---> " + (String)liveTimeBtn.getText().toString());
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = java.sql.Date.valueOf(currentDate); 
			Calendar calendar1   =  getCalendarDate(date1); 
			calendar1.setTime(date1); 
			calendar1.add(calendar1.DATE,0);//把日期往后增加一天.整数往后推,负数往前移动 
		    date1=calendar1.getTime();   //这个时间就是日期往后推一天的结果 
		    sdf.format(date1);
			Log.e("hotel chosen date ---", sdf.format(date1));
			
			bundle.putInt("hotelOrflight", 0);;//系统日期
			bundle.putString("liveDate", sdf.format(date1));//选择的入住日期
			intent.putExtras(bundle);
			intent.putExtra("type", "h");
			startActivityForResult(intent, RequestCode.TO_SELECT_LIVE_TIME);
		}
	}

	class LeaveTimeBtnOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// start select date activity.
			// startActivity for result.
			Log.e("zzz", "select leave time");
			Intent intent = new Intent(HotelMainActivity.this,
					DateSelectActivity.class);
			Bundle bundle = new Bundle();
			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int day = c.get(Calendar.DAY_OF_MONTH);
			String currentDate = year + "-" + month + "-" + day;
			System.out.println("HotelMainActivity passing Date, date ---> " + currentDate);
			
			
			
			System.out.println("HotelMainActivity passing Date, liveDate ---> " + (String)liveTimeBtn.getText().toString());
			bundle.putInt("hotelOrflight", 1);
			bundle.putString("liveDate", (String)liveTimeBtn.getText().toString());
			intent.putExtras(bundle);
			intent.putExtra("type", "h");
			startActivityForResult(intent, RequestCode.TO_SELECT_LEAVE_TIME);
		}
	}

	class SeekBarOnSeekBarChangeListener implements OnSeekBarChangeListener {
		private int state = 0;

		public int getState() {
			return this.state;
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if (fromUser) {
				state = (progress + 4) / 10;
				textViewSurroundText.setText("周边" + state + "公里");
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}
	}

	class KeyWordsBtnOnClickListener implements OnClickListener {
		private int state = State.NO_LIMIT;

		class State {
			public static final int DISTRICT = 0;
			public static final int COMMERCIAL = 1;
			public static final int NO_LIMIT = 2;
		}

		@Override
		public void onClick(View v) {
			Log.e("zzz", "select key words");
			AlertDialog.Builder builder = new AlertDialog.Builder(
					HotelMainActivity.this);
			final AlertDialog al = builder.create();
			LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			final LinearLayout rl = (LinearLayout) inflater.inflate(
					R.layout.dialog_id_type, null);

			LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			ListView types = (ListView) rl.findViewById(R.id.id_type_list);

			ArrayAdapter<CharSequence> adapter = ArrayAdapter
					.createFromResource(HotelMainActivity.this,
							R.array.key_words_area, R.layout.item_id_type);
			types.setAdapter(adapter);
			al.show();
			al.setContentView(rl, llp);
			al.setCanceledOnTouchOutside(true);
			types.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TextView tv = (TextView) view;
					// call interface to select details.
					// commercialLocation
					// districtId
					cdi = new CommercialLocationDistrictInterface(
							HotelMainActivity.this, handlerCD);
					mapParamsCD.clear();
					mapParamsCD.put("action", "GetStaticCityLocations");
					mapParamsCD.put("cityId", liveCityCode);
					if (position == 0) {
						KeyWordsBtnOnClickListener.this.state = KeyWordsBtnOnClickListener.State.NO_LIMIT;
						keyWordsBtn.setText("不限");
					} else if (position == 1) {
						KeyWordsBtnOnClickListener.this.state = KeyWordsBtnOnClickListener.State.COMMERCIAL;
						mapParamsCD.put("Type", "2");
						String testUrl = cdi.prepareGetFullURL(mapParamsCD);
						cdi.getModelFromGET(testUrl, TripgMessage.DEFAULT, "0");
					} else if (position == 2) {
						KeyWordsBtnOnClickListener.this.state = KeyWordsBtnOnClickListener.State.DISTRICT;
						mapParamsCD.put("Type", "1");
						String testUrl = cdi.prepareGetFullURL(mapParamsCD);
						cdi.getModelFromGET(testUrl, TripgMessage.DEFAULT, "0");
					}
					al.dismiss();
				}
			});
		}
	}

	// 查询点击按钮 新建类基础按钮
	class QueryOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if(getInternet() == false){
				Toast.makeText(HotelMainActivity.this, "网络链接已断开", Toast.LENGTH_LONG).show();
			}else{
					mapParamsQuery.clear();
					mapParamsQuery.put("action", "GetDynamicHotelList");
					mapParamsQuery.put("checkInDate", liveTime);
					mapParamsQuery.put("checkOutDate", leaveTime);
					mapParamsQuery.put("cityId", liveCityCode);// change
					mapParamsQuery.put("pageIndex", "1");
					mapParamsQuery.put("pageSize", "20");
					if (priceStarBtnOnClickListener.getState() == PriceStarBtnOnClickListener.State.SELECT_PRICE) {
						mapParamsQuery.put("lowRate", ""
								+ priceStarBtnOnClickListener.rateStar.lowRate);
						mapParamsQuery.put("highRate", ""
								+ priceStarBtnOnClickListener.rateStar.highRate);
					} else if (priceStarBtnOnClickListener.getState() == PriceStarBtnOnClickListener.State.SELECT_STAR) {
						mapParamsQuery.put("starCode", ""
								+ priceStarBtnOnClickListener.rateStar.star);
					} else {
						// No limit , do nothing.
					}
					if (keyWordsBtnOnClickListener.state == KeyWordsBtnOnClickListener.State.COMMERCIAL) {
						mapParamsQuery.put("commercialLocationId", commLocationId);
					} else if (keyWordsBtnOnClickListener.state == KeyWordsBtnOnClickListener.State.DISTRICT) {
						mapParamsQuery.put("districtId", distLocationId);
					} else {
						// No limit , do nothing
					}
					test = new TestInterface(HotelMainActivity.this, handlerQuery);
					String testUrl = test.prepareGetFullURL(mapParamsQuery);
					test.getModelFromGET(testUrl, TripgMessage.DEFAULT, "0");
			}
		}
	}

	class PriceStarBtnOnClickListener implements OnClickListener {
		class State {
			public static final int SELECT_PRICE = 0;
			public static final int SELECT_STAR = 1;
			public static final int NO_LIMIT = 2;
		}

		class RateStar {
			public int lowRate = 0;
			public int highRate = 0;
			public int star = 0;
		}

		public RateStar rateStar = new RateStar();
		private int state = State.NO_LIMIT;// 0 price, 1 star, 2 no limit

		public int getState() {
			return this.state;
		}

		@Override
		public void onClick(View v) {
			Log.e("zzz", "select price star");
			AlertDialog.Builder builder = new AlertDialog.Builder(
					HotelMainActivity.this);
			final AlertDialog al = builder.create();
			LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			final LinearLayout rl = (LinearLayout) inflater.inflate(
					R.layout.price_star, null);

			LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			final ListView price = (ListView) rl.findViewById(R.id.price_range);
			final ListView star = (ListView) rl.findViewById(R.id.star_range);
			final Button priceBtn = (Button) rl.findViewById(R.id.price_select);
			final Button starBtn = (Button) rl.findViewById(R.id.star_select);
			ArrayAdapter<CharSequence> adapterPrice = ArrayAdapter
					.createFromResource(HotelMainActivity.this,
							R.array.price_range, R.layout.item_id_type);
			ArrayAdapter<CharSequence> adapterStar = ArrayAdapter
					.createFromResource(HotelMainActivity.this,
							R.array.star_range, R.layout.item_id_type);
			price.setAdapter(adapterPrice);
			star.setAdapter(adapterStar);

			al.show();
			al.setContentView(rl, llp);
			al.setCanceledOnTouchOutside(true);
			price.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					TextView tv = (TextView) view;
					priceStarBtn.setText(tv.getText());
					if (position == 0) {
						state = State.NO_LIMIT;
					} else {
						state = State.SELECT_PRICE;
						if ("150以下".equals(tv.getText())) {
							rateStar.lowRate = 0;
							rateStar.highRate = 150;
						} else if ("150-300".equals(tv.getText())) {
							rateStar.lowRate = 150;
							rateStar.highRate = 300;
						} else if ("301-450".equals(tv.getText())) {
							rateStar.lowRate = 301;
							rateStar.highRate = 450;
						} else if ("451-600".equals(tv.getText())) {
							rateStar.lowRate = 451;
							rateStar.highRate = 600;
						} else if ("601-1000".equals(tv.getText())) {
							rateStar.lowRate = 601;
							rateStar.highRate = 1000;
						} else if ("1000-5000".equals(tv.getText())) {
							rateStar.lowRate = 1000;
							rateStar.highRate = 5000;
						}
					}
					al.dismiss();
				}
			});
			star.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					TextView tv = (TextView) view;
					priceStarBtn.setText(tv.getText());
					if (position == 0) {
						state = State.NO_LIMIT;
					} else {
						state = State.SELECT_STAR;
						if ("一星级".equals(tv.getText())) {
							rateStar.star = 1;
						} else if ("二星级".equals(tv.getText())) {
							rateStar.star = 2;
						} else if ("三星级".equals(tv.getText())) {
							rateStar.star = 3;
						} else if ("四星级".equals(tv.getText())) {
							rateStar.star = 4;
						} else if ("五星级".equals(tv.getText())) {
							rateStar.star = 5;
						}
					}
					al.dismiss();
				}
			});
			priceBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					starBtn.setBackgroundResource(R.drawable.star_select2);
					priceBtn.setBackgroundResource(R.drawable.price_select1);
					price.setVisibility(View.VISIBLE);
					star.setVisibility(View.GONE);
				}
			});
			starBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					starBtn.setBackgroundResource(R.drawable.star_select1);
					priceBtn.setBackgroundResource(R.drawable.price_select2);
					price.setVisibility(View.GONE);
					star.setVisibility(View.VISIBLE);
				}
			});
		}
	}
}