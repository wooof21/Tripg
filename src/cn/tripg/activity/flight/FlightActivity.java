package cn.tripg.activity.flight;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tools.des.Api;
import tools.des.MD5;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.internet.Exit;
import cn.internet.Tools;
import cn.tripg.R;
import cn.tripg.widgit.ProgressDialogTripg;

public class FlightActivity extends Activity{
	private boolean mIsSingleSwitchSelected = true;
	private boolean  mIsRoundSwitchSelected = false;
	private FrameLayout fromCity;
	private FrameLayout toCity;
	private String strFromCity;
	private String strToCity;
	private FrameLayout dateLeave = null;
	private FrameLayout dateBack = null;
	private FrameLayout date = null;//for single trip
	private String strDateLeave;
	private String strDateBack;
	private String strDate;
	private FrameLayout airCompany = null;
	private String strAirCompany;
	private String currentDate = null;
	
	public ProgressDialog progressDialog1;
	private String username;
	private String password;
	private List<Person> results;
	private ArrayList<HashMap<String, String>> spList;
	private String dateSelected;
	private String datePass;
	private String selectedLeaveDate;
	private String selectedBackDate;
	
	private String getCityCode(String name){
		Properties proCity = new Properties();
		InputStream isCity = getResources().openRawResource(R.raw.flightcity);
		String codeCity = "";
		try {
			proCity.load(isCity);
			
			codeCity = (String)proCity.get(name);
		} catch (IOException e) {
		    Log.e("zzz", "getCityCode IOException");
		}catch(Exception e){
			Log.e("zzz", "no such city in dictionary");
		}
		try {
			isCity.close();
		} catch (IOException e) {
		    Log.e("zzz", "getCityCode IOException");
		}
		if("null".equals(codeCity))
			codeCity = "";
		return codeCity;
	}
    private String getCompanyCode(String name){
    	Properties proCompany = new Properties();
    	InputStream isCompany = getResources().openRawResource(R.raw.company);
    	String codeCompany = "";
    	try {
			proCompany.load(isCompany);
			codeCompany = (String)proCompany.get(name);
		} catch (IOException e) {
		    Log.e("zzz", "getCompanyCode IOException");
		} catch(Exception e){
			//Log.e("zzz", "no such air company in dictionary");
		}
    	try {
			isCompany.close();
		} catch (IOException e) {
		    Log.e("zzz", "getCompanyCode IOException");
			return null;
		}
    	if("null".equals(codeCompany)|| null == codeCompany)
    		codeCompany = "";
		return codeCompany;
	}
    @SuppressLint("SimpleDateFormat")
	private String stringToDate(String dateStr, String formatStr) {
		DateFormat sdf = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String str = sdf.format(date);
		return str;
	}
	private void singleTripquery(){
		String depCity = getCityCode(strFromCity);
		String arrCity = getCityCode(strToCity);
		String carrier = getCompanyCode(strAirCompany);
		Api api = new Api();
		String flightDate = stringToDate(strDate,"yyyy-MM-dd");;
		String sign = MD5.appendData(depCity, flightDate);//encode.
		//String specialPriceUrl ="http://flightapi.tripglobal.cn:8080?cmd=lowprice"
		String specialPriceUrl ="http://flightapi.tripglobal.cn:8080/Default.aspx?cmd=floor"
		+"&depCity=" + depCity
		+"&arrCity=" + arrCity
		+"&flightDate=" + flightDate
		+"&days=15&option=D&output=json";
		String url = api.doGetTENRequestURL(
				"?cmd=av&output=json",
				"&filter=1",
				"&depCity=" + depCity,
				"&arrCity=" + arrCity,
				"&carrier=" + carrier,
				"&flightDate=" + flightDate,
				"&officeCode=CGQ182",
				"&flightTime=",
				"&share=0",
				"&sign=" + sign);
		Log.e("@115 String url = api.doGetTENRequestURL", url);
		Intent intent = new Intent(FlightActivity.this, FlightQueryResultActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("url", url);
		bundle.putString("cmd", "?cmd=av&output=json");
		bundle.putString("filter", "&filter=1");
		bundle.putString("depCity", depCity);
		bundle.putString("arrCity", arrCity);
		bundle.putString("depCityStr", strFromCity);
		bundle.putString("arrCityStr", strToCity);
		bundle.putString("carrier", carrier);
		bundle.putString("flightDate", flightDate);
		bundle.putString("officeCode", "&officeCode=CGQ182");
		bundle.putString("flightTime", "&flightTime=");
		bundle.putString("type", "single");
		bundle.putString("specialPriceUrl", specialPriceUrl);
		intent.putExtras(bundle);
		intent.putExtra("xmllist", (Serializable) results);
		intent.putExtra("sdl", selectedLeaveDate);
		intent.putExtra("sbd", selectedLeaveDate);
		startActivity(intent);
	}
    private void roundTripquery(){
    	String depCity = getCityCode(strFromCity);
		String arrCity = getCityCode(strToCity);
		String carrier = getCompanyCode(strAirCompany);
		Api api = new Api();
		//Not done. check leave date <= back date 
		if(strDateLeave == null || strDateBack == null){
			Toast.makeText(FlightActivity.this, "请选择往返日期", Toast.LENGTH_SHORT).show();
			return;
		}
		String flightDateLeave = stringToDate(strDateLeave,"yyyy-MM-dd");
		String flightDateBack = stringToDate(strDateBack,"yyyy-MM-dd");
		String sign = MD5.appendData(depCity, flightDateLeave);//encode.
		//String specialPriceUrl ="http://flightapi.tripglobal.cn:8080?cmd=lowprice"
		String specialPriceUrl ="http://flightapi.tripglobal.cn:8080/Default.aspx?cmd=floor"
		+"&depCity=" + depCity
		+"&arrCity=" + arrCity
		+"&flightDate=" + flightDateLeave
		+"&days=15&option=D&output=json";
		String url = api.doGetTENRequestURL(
				"?cmd=av&output=json",
				"&filter=1",
				"&depCity=" + depCity,
				"&arrCity=" + arrCity,
				"&carrier=" + carrier,
				"&flightDate=" + flightDateLeave,
				"&officeCode=CGQ182",
				"&flightTime=",
				"&share=0",
				"&sign=" + sign);
		Intent intent = new Intent(FlightActivity.this, FlightQueryResultActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("url", url);
		bundle.putString("cmd", "?cmd=av&output=json");
		bundle.putString("filter", "&filter=1");
		bundle.putString("depCity", depCity);
		bundle.putString("arrCity", arrCity);
		bundle.putString("depCityStr", strFromCity);
		bundle.putString("arrCityStr", strToCity);
		bundle.putString("carrier", carrier);
		bundle.putString("flightDateLeave", flightDateLeave);
		bundle.putString("flightDateBack", flightDateBack);
		bundle.putString("officeCode", "&officeCode=CGQ182");
		bundle.putString("flightTime", "&flightTime=");
		bundle.putString("sign", sign);
		bundle.putString("type", "round");
		bundle.putString("specialPriceUrl", specialPriceUrl);
		intent.putExtras(bundle);
		intent.putExtra("xmllist", (Serializable) results);
		intent.putExtra("sdl", flightDateLeave);
		intent.putExtra("sdb", flightDateBack);
		startActivity(intent);
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
	
    private void selectDate(final int requestCode){
    	
		new AsyncTask<Void, Void, String>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				progressDialog1 = ProgressDialogTripg.show(FlightActivity.this,
						null, null);
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				progressDialog1.dismiss();
				
				if(result.equals("1")){
					Intent intent = new Intent(FlightActivity.this,
							DateSelectActivity.class);

					Bundle bundle = new Bundle();
					if (requestCode == RequestCode.TO_SELECT_DATE) {
						bundle.putString("liveDate",
								stringToDate(currentDate, "yyyy-MM-dd"));
						bundle.putInt("hotelOrflight", 0);
					} else if (requestCode == RequestCode.TO_SELECT_DATE_LEAVE) {
						bundle.putString("liveDate",
								stringToDate(currentDate, "yyyy-MM-dd"));
						bundle.putInt("hotelOrflight", 2);
					} else {
						TextView tv = (TextView) dateLeave.getChildAt(1);
						bundle.putString("liveDate",
								stringToDate(tv.getText().toString(), "yyyy-MM-dd"));
						bundle.putInt("hotelOrflight", 3);
					}
					bundle.putString("type", "f");
					intent.putExtras(bundle);
					intent.putExtra("sp", spList);
					intent.putExtra("dateSelected", datePass);
					startActivityForResult(intent, requestCode);
				}else{
					
				}
			}

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String depCity = getCityCode(strFromCity);
				String arrCity = getCityCode(strToCity);
				
				if (requestCode == RequestCode.TO_SELECT_DATE) {
					TextView tv = (TextView) date.getChildAt(1);
					datePass = tv.getText().toString();
					selectedLeaveDate = datePass;
					Log.e("datePass", datePass);
				} else if (requestCode == RequestCode.TO_SELECT_DATE_LEAVE) {
					TextView tv = (TextView) dateLeave.getChildAt(1);
					datePass = tv.getText().toString();
					selectedLeaveDate = datePass;
					Log.e("datePass", datePass);
				} else {
					TextView tv = (TextView) dateBack.getChildAt(1);
					datePass = tv.getText().toString();
					selectedBackDate = datePass;
					Log.e("datePass", datePass);
				}
				String code = "";
				//String specialUrl = "http://flightapi.tripglobal.cn:8080/?cmd=lowprice&depCity="
				String specialUrl ="http://flightapi.tripglobal.cn:8080/Default.aspx?cmd=floor&depCity="
				+ depCity
				+ "&arrCity="
				+ arrCity
				+"&flightDate="
						+ stringToDate(datePass, "yyyy-MM-dd")
						+ "&days=15&option=D&output=json";
				Log.e("special price url", specialUrl);
				Tools tools = new Tools();
				String data = tools.getURL(specialUrl);
				System.out.println("data --->" + data);
				
				try {
					JSONObject job = new JSONObject(data);
					code = job.getString("Code");
					if(code.equals("1")){
						spList = new ArrayList<HashMap<String,String>>();
						JSONArray jArray = job.getJSONArray("Result");
						for(int i=0,j=jArray.length();i<j;i++){
							JSONObject job1 = jArray.optJSONObject(i);
							HashMap<String, String> hashMap = new HashMap<String, String>();
							String[] d = job1.getString("Date").split("-");
							Log.e("d[2]", ""+Integer.parseInt(d[2]));
							hashMap.put("date", ""+Integer.parseInt(d[2]));
							hashMap.put(""+Integer.parseInt(d[2]), job1.getString("Price"));
							
							spList.add(hashMap);
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return code;
			}

		}.execute();
//    	Intent intent = new Intent(FlightActivity.this,
//				DateSelectActivity.class);
//		Bundle bundle = new Bundle();
//		if(requestCode == RequestCode.TO_SELECT_DATE){
//			bundle.putString("liveDate", stringToDate(currentDate, "yyyy-MM-dd"));
//			bundle.putInt("hotelOrflight", 0);
//		}else if(requestCode == RequestCode.TO_SELECT_DATE_LEAVE){
//			bundle.putString("liveDate", stringToDate(currentDate, "yyyy-MM-dd"));
//			bundle.putInt("hotelOrflight", 2);
//		}else{
//			TextView tv = (TextView) dateLeave.getChildAt(1);
//			bundle.putString("liveDate", stringToDate(tv.getText().toString(), "yyyy-MM-dd"));
//			bundle.putInt("hotelOrflight", 3);
//		}
//		intent.putExtras(bundle);
//		startActivityForResult(intent, requestCode);
    }
    private void selectCompany(int RequestCode){
    	Intent intent = new Intent(FlightActivity.this,
				AirCompanySelectActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("companyName", "");
		intent.putExtras(bundle);
		startActivityForResult(intent, RequestCode);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch(requestCode){
    	case RequestCode.TO_SELECT_LEAVE_CITY:
    		if(resultCode == ResultCode.SUCCESS){
    			Bundle city = data.getExtras();
    			String cityName = city.getString("cityName");
    			TextView tv = (TextView)fromCity.getChildAt(1);
    			tv.setText(cityName);
    			strFromCity = cityName;
    		}
    		break;
    	case RequestCode.TO_SELECT_ARRIVE_CITY:
    		if(resultCode == ResultCode.SUCCESS){
    			Bundle city = data.getExtras();
    			String cityName = city.getString("cityName");
    			TextView tv = (TextView)toCity.getChildAt(1);
    			tv.setText(cityName);
    			strToCity = cityName;
    		}
    		break;
    	case RequestCode.TO_SELECT_DATE:
    		if(resultCode == ResultCode.SUCCESS){
    			Bundle bundle = data.getExtras();
    			dateSelected = bundle.getString("date");
    			TextView tv = (TextView) date.getChildAt(1);
				tv.setText(dateSelected);
				strDate = dateSelected;
				dateSelected = tv.getText().toString();
				Log.e("selectedLeaveDate", dateSelected);
    		}
    		break;
    	case RequestCode.TO_SELECT_DATE_LEAVE:
    		if(resultCode == ResultCode.SUCCESS){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Bundle bundle = data.getExtras();
				String dateSelected = bundle.getString("date");
				
				Date date1 = java.sql.Date.valueOf(dateSelected); 
				Calendar calendar1   =  getCalendarDate(date1); 
				calendar1.setTime(date1); 
				calendar1.add(calendar1.DATE,0);//把日期往后增加一天.整数往后推,负数往前移动 
			    date1=calendar1.getTime();   //这个时间就是日期往后推一天的结果 
			    sdf.format(date1);
				Log.e("flight chosen date ---", sdf.format(date1));
				
				Date date = java.sql.Date.valueOf(dateSelected); 
				Calendar calendar   =  getCalendarDate(date); 
				calendar.setTime(date); 
				calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
			    date=calendar.getTime();   //这个时间就是日期往后推一天的结果 
			    sdf.format(date);
			    Log.e("next day ---", sdf.format(date));
				
			    TextView tv = (TextView) dateLeave.getChildAt(1);
				TextView tvb = (TextView) dateBack.getChildAt(1);
				tv.setText(sdf.format(date1));
				strDateLeave = dateSelected;
				dateSelected = tv.getText().toString();
				Log.e("selectedLeaveDate", dateSelected);
				
				String _date1 = tv.getText().toString().replace("-", "");
				String _date2 = tvb.getText().toString().replace("-", "");
				if(Long.parseLong(_date2) - Long.parseLong(_date1) >= 0){
					
				}else{
					tvb.setText(sdf.format(date));
					strDateBack = sdf.format(date);
				}
    		}
    		break;
    	case RequestCode.TO_SELECT_DATE_BACK:
    		if(resultCode == ResultCode.SUCCESS){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Bundle bundle = data.getExtras();
				dateSelected = bundle.getString("date");
				
				Date date1 = java.sql.Date.valueOf(dateSelected); 
				Calendar calendar1   =  getCalendarDate(date1); 
				calendar1.setTime(date1); 
				calendar1.add(calendar1.DATE,0);//把日期往后增加一天.整数往后推,负数往前移动 
			    date1=calendar1.getTime();   //这个时间就是日期往后推一天的结果 
			    sdf.format(date1);
				Log.e("flight chosen date ---", sdf.format(date1));
				
				TextView tv = (TextView) dateBack.getChildAt(1);
				tv.setText(sdf.format(date1));
				strDateBack = dateSelected;
				dateSelected = tv.getText().toString();
				
    		}
    		break;
    	case RequestCode.TO_SELECT_AIR_COMPANY:
    		if(resultCode == ResultCode.SUCCESS){
    			Bundle bundle = data.getExtras();
    			String cpnySelected = bundle.getString("companyName");
    			TextView tv = (TextView)airCompany.getChildAt(1);
    			tv.setText(cpnySelected);
    			strAirCompany = cpnySelected;
    		}
    		break;
    	}
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
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flight);
		Exit.getInstance().addActivity(this);
		fromCity = (FrameLayout)findViewById
				(R.id.from);
		toCity = (FrameLayout)findViewById
				(R.id.to);
		final ImageView backButton = (ImageView)findViewById
				(R.id.title_back);
		final ImageView mSingleSwitch = (ImageView)findViewById
				(R.id.single_switch);
		final ImageView mRoundSwitch = (ImageView)findViewById
				(R.id.round_switch);
		final RelativeLayout mStable = (RelativeLayout)findViewById
				(R.id.fly_ticket);
		LayoutInflater inflater = (LayoutInflater)getSystemService
				(LAYOUT_INFLATER_SERVICE);
		final RelativeLayout singleTripLayout = (RelativeLayout)inflater.
				inflate(R.layout.flight_date_long, null);
		final RelativeLayout roundTripLayout = (RelativeLayout)inflater.
				inflate(R.layout.flight_date_short, null);
		final RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams
				(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		rlp.addRule(RelativeLayout.BELOW,R.id.to);
		mStable.addView(singleTripLayout,rlp);
		strFromCity = "长春";
		strToCity = "北京";
		ImageView singleQuery = (ImageView)findViewById
				(R.id.query);
		
			singleQuery.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(getInternet() == true){
						singleTripquery();
					}else{
						Toast.makeText(FlightActivity.this, "网络链接已断开", Toast.LENGTH_LONG).show();
					}
				}
			});
		
		
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e("zzz", "flight activity back");
				finish();
			}
		});
		date = (FrameLayout)findViewById(R.id.date_long);
		TextView tv = (TextView)date.getChildAt(1);
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		currentDate = year + "-" + month + "-" + day;
		tv.setText(currentDate);
		strDate = currentDate;
		dateSelected = currentDate;
		
		date.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectDate(RequestCode.TO_SELECT_DATE);
			}
		});
		airCompany = (FrameLayout)findViewById(R.id.type);
		airCompany.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectCompany(RequestCode.TO_SELECT_AIR_COMPANY);
			}
		});
		mSingleSwitch.setOnClickListener(new OnClickListener() {
			ImageView singleQuery = null;
			@Override
			public void onClick(View v) {
				//change the button background for single trip
				if(mIsSingleSwitchSelected == false){
					mIsSingleSwitchSelected = true;
					mStable.removeView(roundTripLayout);
					mStable.addView(singleTripLayout,rlp);
					singleQuery = (ImageView)findViewById(R.id.query);
					date = (FrameLayout)findViewById(R.id.date_long);
					airCompany = (FrameLayout)findViewById(R.id.type);

					
						singleQuery.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								if(getInternet() == true){
									singleTripquery();
								}else{
									Toast.makeText(FlightActivity.this, "网络链接已断开", Toast.LENGTH_LONG).show();
								}
							}
						});
					
					date.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							selectDate(RequestCode.TO_SELECT_DATE);
						}
					});
					airCompany.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							selectCompany(RequestCode.TO_SELECT_AIR_COMPANY);
						}
					});
					mSingleSwitch.setImageResource(R.drawable.single_trip_on);
					if(mIsRoundSwitchSelected == true){
						mIsRoundSwitchSelected = false;
						mRoundSwitch.setImageResource
						(R.drawable.round_trip_off);
					}
				}
			}
		});
		mRoundSwitch.setOnClickListener(new OnClickListener() {
			ImageView roundQuery = null;
			@Override
			public void onClick(View v) {
				//change the button background for round trip
				if(mIsRoundSwitchSelected == false){
					mIsRoundSwitchSelected = true;
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					mStable.removeView(singleTripLayout);
					mStable.addView(roundTripLayout, rlp);
					roundQuery = (ImageView) findViewById(R.id.query);
					dateLeave = (FrameLayout) findViewById(R.id.date_short_from);
					dateBack = (FrameLayout) findViewById(R.id.date_short_to);
					airCompany = (FrameLayout) findViewById(R.id.type);
					TextView tv = (TextView) dateLeave.getChildAt(1);
					TextView tvb = (TextView) dateBack.getChildAt(1);
					
					tv.setText(currentDate);
					Date date = java.sql.Date.valueOf(currentDate); 
					Calendar calendar   =  getCalendarDate(date); 
					calendar.setTime(date); 
					calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
				    date=calendar.getTime();   //这个时间就是日期往后推一天的结果 
				    sdf.format(date);
				    Log.e("next day ---", sdf.format(date));
					tvb.setText(sdf.format(date));

					strDateLeave = currentDate;
					strDateBack = sdf.format(date);
					
						roundQuery.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								if(getInternet() == true){
									roundTripquery();
								}else{
									Toast.makeText(FlightActivity.this, "网络链接已断开", Toast.LENGTH_LONG).show();
								}
							}
						});
					
					
					dateLeave.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							selectDate(RequestCode.TO_SELECT_DATE_LEAVE);
						}
					});
					dateBack.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							selectDate(RequestCode.TO_SELECT_DATE_BACK);
						}
					});
					airCompany.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							selectCompany(RequestCode.TO_SELECT_AIR_COMPANY);
						}
					});
					mRoundSwitch.setImageResource(R.drawable.round_trip_on);
					if(mIsSingleSwitchSelected == true){
						mIsSingleSwitchSelected = false;
						mSingleSwitch.setImageResource
						(R.drawable.single_trip_off);
					}
				}
			}
		});
		fromCity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FlightActivity.this,
						CitySelectActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("cityName", "");
				intent.putExtras(bundle);
				startActivityForResult(intent, RequestCode.TO_SELECT_LEAVE_CITY);
			}
		});
		toCity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FlightActivity.this,
						CitySelectActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("cityName", "");
				intent.putExtras(bundle);
				startActivityForResult(intent, RequestCode.TO_SELECT_ARRIVE_CITY);
			}
		});
	}
}
