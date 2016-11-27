package order.pnr.yidao;

import httpdelegate.HttpRequest;
import httpdelegate.OrderAirHttpConnection;
import httpdelegate.OrderHttpConnection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import model.flight.Keys;
import model.flight.Result;
import model.flight.Rsa;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import tools.des.DesCodeUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import cn.internet.Exit;
import cn.internet.Tools;
import cn.tripg.R;
import cn.tripg.activity.flight.DateSelectActivity;
import cn.tripg.activity.flight.MainActivity;
import cn.tripg.activity.flight.PayOrderActivity;
import cn.tripg.activity.flight.RequestCode;
import cn.tripg.activity.flight.ResultCode;
import cn.tripg.activity.login.LoginActivity;
import cn.tripg.widgit.ProgressDialogTripg;

import com.alipay.android.app.net.HttpClient;
import com.alipay.android.app.sdk.AliPay;
import com.data.main.Fapiaoview;
import com.data.main.Time_main_data;

public class OrderPnrMain extends Activity implements OnClickListener,Orderlist,OrderAirList {

	private ProgressDialog dialog;
	private HashMap<String, Object> httpHashMap;
	private HashMap<String, Object> httpHashAir;
	private final int UPDATE_LIST_VIEW = 1;
	
	private static final int RQF_PAY = 1;
	private static final int RQF_LOGIN = 2;
	private static final int PAY_OK = 9;
	
	public EditText edNameEditText ;
	public EditText edPhoneEditText;
	public EditText edAddressEditText;
	public EditText edpersonNumText;
	public TextView hourTextView;
	public ImageView addImageView;
	public ImageView micImageView;
	public ImageView yuDingImageView;
	public TextView nighTextView;
	public int hourInt;//选择时间数
	public ToggleButton onOffButton;
	public ImageView xiugaiImageView;	
//	public ImageView fapImageView;
	private TextView receite;
//	public TextView fapTextView;
	public TextView timeTextView;//时间
	public TextView dataTextView;//日期
	public boolean isBool;
	public boolean invoicebool;
	public String currentDate;
	public String fapSelected;//发票抬头
	public String addSelected;//发票地址
	public String lngString;//经度
	public String latString;//维度
	public String airNameString;//机场名
	public String airkeyString;//机场城市key
	public String huorTimeString;//用车时常
	public String typePostString;//用车id
	public String cityPostString;//用车城市编码
	public String youBianString;// 城市邮编
	public String carIdString;//汽车id

	public String nameString;
	public String phoneString;
	public String addString;
	public String numPerString;
	public String timePostString;
	public String datePostString;
	public String personNumString;	
	
	private String OrderId;
	private String OrderInfo;
	private JSONObject jOrder;
	private String url =
				"http://mapi.tripglobal.cn/RentCarApi.aspx?action=SubmitOrder&token=6QIp1iWQ3LePoFCykN6h_RgyOA6nL3-U";
	private String des_token;
	private String result;
	private String totalFee, UseStatus, DepCity, PriductTypeDesc, carName,
				carId, carLvl, carLvlId, maxLoad, outFee, ArrCity, DepAddress,
				ArrAddress, TotalAmount, CarModelId;
	private String totalFeeTest;
	private int DepCityId, ArrCityId, ProductBaseFeeId, ProductTypeId,
				ProductId, PassengerCount;
	private int MemberId;
	//private float TotalAmount;
	private String UserID;
	public ProgressDialog progressDialog;
	
	private static OrderPnrMain opm;
	

	
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
		Exit.getInstance().addActivity(this);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
		setContentView(R.layout.order_xml_activity_new);
		
		receite = (TextView)findViewById(R.id.textViewfap);

		opm = this;
        
/**************************************************************************************************/				
		UserID = getUserID();
		if(UserID.equals("")){
			System.out.println("UserID ---> " + UserID);
			AlertDialog.Builder builder =  new AlertDialog.Builder(OrderPnrMain.this);
			builder.setTitle("Warning: 未登录无法预定");   
            builder.setMessage("点击确定登录");
            builder.setPositiveButton("确定",    
                    new DialogInterface.OnClickListener() {    
                        public void onClick(DialogInterface dialog, int whichButton) {   
                        	Intent intent3 = new Intent(OrderPnrMain.this, LoginActivity.class);
                        	startActivity(intent3);
                        }    
                    });    
            builder.setNegativeButton("取消",    
                    new DialogInterface.OnClickListener() {    
                        public void onClick(DialogInterface dialog, int whichButton) {   
                        	finish();
//                        	Intent intent4 = new Intent(OrderPnrMain.this, MainActivity.class);
//                        	startActivity(intent4);
                        }    
                    });    
            builder.create();  
            builder.show();
		}
/*********************************************************************************************************/
		
		totalFeeTest = "0.1";
		Intent intent = getIntent();
		totalFee = intent.getExtras().getString("totalFee");
		UseStatus = intent.getExtras().getString("UseStatus");
		DepCity = intent.getExtras().getString("DepCity");
		PriductTypeDesc = intent.getExtras().getString("PriductTypeDesc");
		CarModelId = intent.getExtras().getString("CarModelId");
		carName = intent.getExtras().getString("carName");
		carId = intent.getExtras().getString("carId");
		carLvl = intent.getExtras().getString("carLvl");
		carLvlId = intent.getExtras().getString("carLvlId");
		maxLoad = intent.getExtras().getString("maxLoad");
		outFee = intent.getExtras().getString("outFee");
		System.out.println("totalFee ---> " + totalFee + "\n"
					+ "totalFee ---> " + totalFee + "\n" + "DepCity ---> "
					+ DepCity + "\n" + "PriductTypeDesc ---> "
					+ PriductTypeDesc + "\n" + "carName ---> " + carName + "\n"
					+ "carId ---> " + carId + "\n" + "carLvl ---> " + carLvl
					+ "\n" + "carLvlId ---> " + carLvlId + "\n"
					+ "maxLoad ---> " + maxLoad + "\n" + "outFee ---> "
					+ outFee + "\n" + "CarModelId ---> " + CarModelId);
		DepCityId = Integer.parseInt(intent.getExtras().getString("DepCityId"));
		ProductBaseFeeId = Integer.parseInt(intent.getExtras().getString("ProductBaseFeeId"));
		ProductTypeId = Integer.parseInt(intent.getExtras().getString("ProductTypeId"));
		ProductId = Integer.parseInt(intent.getExtras().getString("ProductId"));
		DepCityId = Integer.parseInt(intent.getExtras().getString("DepCityId"));
		System.out.println("DepCityId ---> " + DepCityId + "\n"
					+ "ProductBaseFeeId ---> " + ProductBaseFeeId + "\n"
					+ "ProductTypeId ---> " + ProductTypeId + "\n"
					+ "ProductId ---> " + ProductId + "\n" + "DepCityId ---> "
					+ DepCityId);	
		
		//TotalAmount = Float.parseFloat(totalFee);
		
		typePostString = intent.getExtras().getString("type");
		cityPostString = intent.getExtras().getString("city");
		carIdString = intent.getExtras().getString("carid");
		personNumString = intent.getExtras().getString("person_number");
		Log.e("接收信息类", typePostString+"----"+cityPostString+"-----"+carIdString);
		

		
		

		
		ImageView backImageView = (ImageView)findViewById(R.id.title_back);
		
		
		
		backImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = getIntent();
				setResult(ResultCode.SUCCESS, intent);
				System.out.println("ResultCode ---> " + ResultCode.SUCCESS
							+ "Intent ---> " + intent);
				finish();
			}
		});
		
		huorTimeString = "1";
		hourInt = 1;
		edNameEditText = (EditText)findViewById(R.id.editTextName);
		edPhoneEditText = (EditText)findViewById(R.id.editTextPhone);
		edPhoneEditText.setText(new Tools().getUserName(getApplicationContext()));
		edAddressEditText = (EditText)findViewById(R.id.editTextAddress);
		edpersonNumText = (EditText)findViewById(R.id.editTextpersonNum);
		edpersonNumText.setHint("限乘"+maxLoad+"人");
		hourTextView = (TextView)findViewById(R.id.textViewhour);
		hourTextView.setText(""+hourInt);
		micImageView = (ImageView)findViewById(R.id.imageButton1);
		micImageView.setOnClickListener(this);
		addImageView = (ImageView)findViewById(R.id.imageButton2);
		addImageView.setOnClickListener(this);

		nighTextView = (TextView)findViewById(R.id.textViewnight);
//		fapImageView = (ImageView)findViewById(R.id.imageViewfp);
//		fapTextView = (TextView)findViewById(R.id.textViewttou);
		
		yuDingImageView = (ImageView)findViewById(R.id.imageView8);
		yuDingImageView.setOnClickListener(this);
		
		timeTextView = (TextView)findViewById(R.id.textViewTime);
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int min = c.get(Calendar.MINUTE);
		Log.e("hour", ""+hour);
		
		String h = "";
		String m = "";
		if(hour >= 0 && hour <=9){
			h = "0" + hour;
			Log.e("h", h);
		}else{
			h = ""+hour;
		}
		if(min >=0 && min <= 9){
			m = "0" + min;
		}else{
			m = ""+min;
		}
		timePostString = h + ":" + m;
		Log.e("timePostString", timePostString);
		timeTextView.setText(timePostString);
		timeTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.e("time", "*****");
			
				Intent intent = new Intent(OrderPnrMain.this,
						Time_main_data.class);
				startActivityForResult(intent, RequestCode.TO_SELECT_TIME);
			}
		});
		dataTextView = (TextView)findViewById(R.id.textViewData);
		Calendar dc = Calendar.getInstance();
		int year = dc.get(Calendar.YEAR);
		int month = dc.get(Calendar.MONTH) + 1;
		int day = dc.get(Calendar.DAY_OF_MONTH);
		dataTextView.setText(year+"年"+month+"月"+day+"日");
		datePostString = year+"-"+month+"-"+day;
		dataTextView.setOnClickListener(this);
		
		onOffButton = (ToggleButton)findViewById(R.id.toggleButton1);
		onOffButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isBool == false){
					Log.e("The current checked state of the view", ""+onOffButton.isChecked());
					Log.e("receite before clicked", "" + isBool);
					onOffButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.on)); 
					
					isBool = true;//
					invoicebool = true;
					Intent intent = new Intent(OrderPnrMain.this,
							Fapiaoview.class);
					startActivityForResult(intent, RequestCode.TO_SELECT_FAPIAO);
					Log.e("receite after clicked", "" + isBool);
					}else{ 
						Log.e("receite before clicked", "" + isBool);
						onOffButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.off)); 
						
						isBool = false;
						invoicebool = false;
						receite.setText("不需发票");
						Log.e("receite after clicked", "" + isBool);
						} 
				} 
			});
//		onOffButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				Log.e("*********", ""+isChecked);
//				if (isChecked == true) {
//			        isBool = true;
//			        invoicebool = true;
//			        Intent intent = new Intent(OrderPnrMain.this,
//							Fapiaoview.class);
//					startActivityForResult(intent, RequestCode.TO_SELECT_FAPIAO);
//
//				}else {
//					isBool = false;
//					invoicebool = false;
////					fapImageView.setVisibility(View.GONE);
////			        fapTextView.setText("");
//				}
//			}
//		});
//		xiugaiImageView =(ImageView)findViewById(R.id.imageView9);
//		xiugaiImageView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Log.e("修改", "****");
//				if ( isBool == true) {
//					Intent intent = new Intent(OrderPnrMain.this,
//							Fapiaoview.class);
//					startActivityForResult(intent, RequestCode.TO_SELECT_FAPIAO);
//				}
//				 
//			}
//		});
		
		
		//showDialog();
//		
//		progressDialog = ProgressDialogTripg.show(this, null, null);
//		//夜补
//		//http://sandbox.open.yongche.org/v2/nightfee?access_token=6YtJFb5nHBmI8LZxOVBM7IFHSDnNlSwAPBdbp9jB&car_type_id=2&city=zh
//		//获取航站楼
//		//http://sandbox.open.yongche.org/v2/airport?access_token=6YtJFb5nHBmI8LZxOVBM7IFHSDnNlSwAPBdbp9jB
//		String urlString = "http://open.yongche.com/v2/nightfee?access_token=jeIGaNbTnfMwE5yQF91DiDruZFYHXm7lS99tCBgM&car_type_id=2&city="+cityPostString;
//		OrderHttpConnection orderHttpConnection = new OrderHttpConnection(this);
//		orderHttpConnection.sendGetConnection(urlString);
//		
//		String urlString2 = "http://open.yongche.com/v2/airport?access_token=jeIGaNbTnfMwE5yQF91DiDruZFYHXm7lS99tCBgM";
//		OrderAirHttpConnection orderAirHttpConnection = new OrderAirHttpConnection(this);
//		orderAirHttpConnection.sendGetConnection(urlString2);
//		
//		Log.e("urlString", "urlString---"+urlString+"---urlString2--"+urlString2);
		
	}
	
	public static OrderPnrMain getInstance(){
		if(opm != null){
			return opm;
		}else{
			return null;
		}
	}
	
	public void execute(){
		//progressDialog = ProgressDialogTripg.show(this, null, null);
		new PayCarOrderTask().execute();
	}
	
	private boolean isUserLogin() {
		SharedPreferences sharedPre =
					OrderPnrMain.this.getSharedPreferences("config",
								Context.MODE_PRIVATE);

		String username = sharedPre.getString("username", "");
		String password = sharedPre.getString("password", "");
		Log.e("username", "A" + username);
		Log.e("password", "B" + password);
		if ("".equals(username) || "".equals(password)) {
			return false;
		} else {
			return true;
		}
	}
	
	protected String getUserID(){
		SharedPreferences sharedPre = OrderPnrMain.this.getSharedPreferences(
					"config", Context.MODE_PRIVATE);
		String userId = (sharedPre.getString("Result", "")); 
		
		return userId;
	}
	protected JSONObject getJSONOrderRequest(){
		JSONObject jo = new JSONObject();
		ArrAddress = addString;
		ArrCity = DepCity;
		ArrCityId = DepCityId;
		//MemberId = 1;

		MemberId = Integer.parseInt(getUserID());
		System.out.println("MemberId ---> " + MemberId);
	 
		try {
			PassengerCount = Integer.parseInt(numPerString);
			System.out.println("PassengerCount ---> " + PassengerCount);
			String DepCityName =  URLEncoder.encode(DepCity, "UTF-8");  
			String ArrCityName =  URLEncoder.encode(ArrCity, "UTF-8");  
			String PriTypeDesName = URLEncoder.encode(PriductTypeDesc, "UTF-8");  
			String ArrAddName = URLEncoder.encode(addString, "UTF-8"); 
			String DepAddName = URLEncoder.encode(addString, "UTF-8"); 
			String PassName = URLEncoder.encode(nameString, "UTF-8"); 
			System.out.println("DepCityName ---> " + DepCityName);
			System.out.println("ArrCityName ---> " + ArrCityName);
			System.out.println("PriTypeDesName ---> " + PriTypeDesName);
			System.out.println("ArrAddName ---> " + ArrAddName);
			System.out.println("DepAddName ---> " + DepAddName);
			System.out.println("PassName ---> " + PassName);
			jo.put("MemberId", MemberId);
			jo.put("DepCityId", DepCityId);
			jo.put("DepCity", DepCityName);
			jo.put("DepAddress", DepAddName);
			jo.put("ArrCityId", ArrCityId);
			jo.put("ArrCity", ArrCityName);
			jo.put("ArrAddress", ArrAddName);
			jo.put("PassengerCount", PassengerCount);
			
			jo.put("TotalAmount", totalFee);
	
			jo.put("RentCarTime", datePostString + " " + timePostString + ":00");
			//jo.put("RentCarTime", timePostString + ":00");
			jo.put("PassengerName", PassName);
			jo.put("PassengerMobile", phoneString);
			jo.put("ProductId", ProductId);
			jo.put("ProductBaseFeeId", ProductBaseFeeId);
			jo.put("ProductTypeId", ProductTypeId);
			jo.put("PriductTypeDesc", PriTypeDesName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("jo ---> " + jo.toString());
		return jo;
	}

	public void showDialog(){
		
		dialog = new ProgressDialog(this);
		dialog.setTitle("加载数据请稍后");
		dialog.setMessage("Loading...");
		dialog.show();
		
	}
	
	@SuppressWarnings("unchecked")
	public void getOrderList (HashMap<String, Object> hashMap) {

		//dialog.dismiss();
		progressDialog.dismiss();
		//ArrayList<OrderInFoData> list = (ArrayList<OrderInFoData>) hashMap.get("array");
		httpHashMap = (HashMap<String, Object>) hashMap.get("hashtop");
		if (httpHashMap != null) {
			System.out.println("***************"+httpHashMap.get("night_fee"));
		}
//		for (OrderInFoData jsonObject : list) {
//			//listData.add(getHashMap(jsonObject));
//		}
		
		//通知刷新界面
		sendHandlerMessage(2, null);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void getOrderAirList(HashMap<String, Object> hashMap) {
		httpHashAir = (HashMap<String, Object>) hashMap.get(cityPostString);
		System.out.println("*******cityPostString********"+cityPostString);
		lngString = (String) httpHashAir.get("airlng");
		System.out.println("*******airlng********"+httpHashAir.get("airlng"));
		airNameString = (String) httpHashAir.get("airname");
		System.out.println("********airname*******"+httpHashAir.get("airname"));
		latString = (String)httpHashAir.get("airlat");
		System.out.println("*******airlat********"+httpHashAir.get("airlat"));
		airkeyString = (String)httpHashAir.get("airkey");
		System.out.println("*******airkey********"+httpHashAir.get("airkey"));
		
		
	}

	private void sendHandlerMessage(int what, Object obj) {
		Message msg = new Message();
		msg.what = what;
		handler.sendMessage(msg);
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		/**
		 * @param handleMessage
		 * 	刷新listView的数据
		 * 
		 * */
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_LIST_VIEW:
				
				break;
			case 2:
				if (httpHashMap != null) {
					nighTextView.setText("23:00-次日6:00夜间服务费("+httpHashMap.get("night_fee")+"元/次)");
				}
				break;
			case PAY_OK:
				Bundle b = new Bundle();
				b.putString("DingDanNo", OrderId);
				Intent intent10 = new Intent(OrderPnrMain.this,
						OrderSuccse.class);
				intent10.putExtras(b);
				startActivity(intent10);
				break;
			default:
				break;
			}
		};
	};
	
	
//	@SuppressWarnings("unused")
//	private HashMap<String, Object> getHashMap(OrderInFoData orderInFoData) {
//		
//		HashMap<String, Object> hashMap = new HashMap<String, Object>();
//		//JSONObject jsonObject = orderInFoData.getOrderJsonObject();
//		
//		try {
//			hashMap.put("car_type_id", jsonObject.getString("car_type_id"));
//			hashMap.put("name", jsonObject.getString("name"));
//			hashMap.put("brand", jsonObject.getString("brand"));
//			hashMap.put("person_number", jsonObject.getString("person_number"));
//			hashMap.put("pic", jsonObject.getString("pic"));
//			hashMap.put("min_response_time", jsonObject.getString("min_response_time"));
//			hashMap.put("fee", jsonObject.getString("fee"));
//			hashMap.put("time_length", jsonObject.getString("time_length"));
//			hashMap.put("distance", jsonObject.getString("distance"));
//			hashMap.put("fee_per_hour", jsonObject.getString("fee_per_hour"));
//			hashMap.put("fee_per_kilometer", jsonObject.getString("fee_per_kilometer"));
//			
//			System.out.println("carType"+jsonObject.getString("name"));
//			
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		
//		return hashMap;
//		
//
//	}
//	
	/**
	 * @param onActivityResult  
	 * 跳转界面的回调函数
	 * 
	 * */
	
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
    	switch (requestCode) {
    	case RequestCode.TO_SELECT_DATE:
    		if(resultCode == ResultCode.SUCCESS){
    			Bundle bundle = data.getExtras();
    			String dateSelected = bundle.getString("date");
    			Log.e("date *******", dateSelected);
    			dataTextView.setText(dateSelected);
    			datePostString = (String)dateSelected;
//    			TextView tv = (TextView)date.getChildAt(1);
//    			tv.setText(dateSelected);
//    			strDate = dateSelected;
    		}
    		break;
    		case RequestCode.TO_SELECT_TIME:
    			if(resultCode == ResultCode.SUCCESS){
       				Bundle bundle = data.getExtras();
    				String timeSelected = bundle.getString("time");
    				Log.e("time *******",timeSelected);
    				timeTextView.setText(timeSelected);
    				timePostString = (String)timeSelected;
    			}
    			break;
    		case RequestCode.TO_SELECT_FAPIAO:
    			if(resultCode == ResultCode.SUCCESS){
    				
    				Bundle bundle = data.getExtras();
    				fapSelected = bundle.getString("fapiao");
    				addSelected = bundle.getString("address");
    				youBianString = bundle.getString("youbian");
    				Log.e("fapiao", fapSelected + "---------" + addSelected
    						+ "----------" + youBianString);
    				//fapImageView.setVisibility(View.VISIBLE);
    				//fapTextView.setText("发票抬头:" + fapSelected);
    				isBool = true;
    				invoicebool = true;
    				receite.setText("发票抬头:" + fapSelected);

    			}else{
    				isBool = false;
    				invoicebool = false;
    				onOffButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.off)); 
    				receite.setText("不需发票");
    			}

    			break;
		default:
			break;
		}
    }

    protected String getOrderInfo(){
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);	
		sb.append("\"&out_trade_no=\"");
		sb.append(OrderId);
		sb.append("\"&subject=\"");
		sb.append("Android租车支付业务");
		sb.append("\"&body=\"");
//		sb.append("客户对预订的汽车进行支付");
		sb.append("8");
		sb.append("\"&total_fee=\"");
		

		sb.append(totalFee);
		sb.append("\"&notify_url=\"");
		// 网址需要做URL编码
		sb.append(URLEncoder.encode("http://www.tripg.cn/phone_api/alipay_wuxian/notify_url.php"));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&show_url=\"8");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");
	
		System.out.println("sb ---> " + new String(sb));
		return new String(sb);
    }
    protected String getTotalFee(){
		return totalFee;
    	
    }
    protected void setOrderId(String id){
    	this.OrderId = id;
    }
    protected String getOrderId(){
    	return OrderId;
    }
    /**
     * @param httpPostData
     * 
     * 预订post请求 在这里判断url的格式启动请求 
     * */
//    public void httpPostData(){
//    	//showDialog();
//    	
//    	if (phoneString!=null&&addString!=null&&huorTimeString!=null&&nameString!=null) {
//    		progressDialog = ProgressDialogTripg.show(this, null, null);
//    		System.out.println("100.Integer.parseInt(CarModelId) ---> " + Integer.parseInt(CarModelId));
//        	String param = null;
//        	String url = "http://open.yongche.com/v2/order";
//			String urlString =
//						"access_token=jeIGaNbTnfMwE5yQF91DiDruZFYHXm7lS99tCBgM"
//									+ "&city="
//									+ cityPostString
//									+ "&type="
//									+ typePostString
//									+ "&car_type_id="
//									+ carLvlId
//									+ "&start_position="
//									+ addString
//									+ "&start_address="
//									+ addString
//									+ "&expect_start_longitude="
//									+ lngString
//									+ "&expect_start_latitude="
//									+ latString
//									+ "&time="
//									+ datePostString
//									+ " "
//									+ timePostString
//									+ ":00"
//									+ "&end_address="
//									+ addString
//									+ "&passenger_name="
//									+ nameString
//									+ "&passenger_phone="
//									+ phoneString + "&sms_type=1";
//        	param=(String)urlString;
//        	if (typePostString.equals("7") ) {
//        		String airString = "&aircode="+airkeyString;
//        		param = (String)urlString+airString;
//    		}
//        	if (typePostString.equals("8")) {
//        		String airString = "&aircode="+airkeyString+"&end_position="+addString;
//        		param = (String)urlString+airString;
//    		}if (typePostString.equals("1")) {
//    			String airString = "&rent_time="+huorTimeString+"&end_position="+addString;
//    			param = (String)urlString+airString;
//    		}if (invoicebool == true) {
//    			String invsString = "&invoice=1&receipt_title="+fapSelected+"&receipt_content=打车费"+"&address="+addSelected+"&postcode="+youBianString+"&receipt_user="+nameString+"&receipt_phone="+phoneString;
//    			param = (String)urlString+invsString;
//    		}
//    		Log.e("url-----", param);
//    		
//    	    String httpRquestString = (String)HttpRequest.sendPost(url, param);
//    	    JSONObject json = null;
//    	    if (httpRquestString != null) {
//    	    	progressDialog.dismiss();
//    	    	//dialog.dismiss();
//    	    	try {
//    	    		json = new JSONObject(httpRquestString);
//    	    		Log.e("json----", ""+json);
//    	    		String codeString = json.getString("code");
//    	    		JSONObject reJsonObject = json.getJSONObject("result");
//    	    		String dingdanString = reJsonObject.getString("order_id");
//    	    		if (codeString.equals("200")) {
//    	    			Log.e("预订成功----", codeString+"----"+dingdanString);
//    	    			Intent intent2 = new Intent(this,OrderSuccse.class);
//    	    			Bundle bundle = new Bundle();
//    	    			bundle.putString("ding", dingdanString);
//    	    			intent2.putExtras(bundle);
//    	    			startActivity(intent2);
//    	    		}else {
//    	    			Log.e("预订失败", "---------");
//    	    			Builder	alertDialog = new AlertDialog.Builder(this);
//    	    			alertDialog.setTitle("预订失败");
//    	    			alertDialog.setSingleChoiceItems(1, 0, new DialogInterface.OnClickListener(){
//
//    						@Override
//    						public void onClick(DialogInterface arg0, int arg1) {
//    							
//    							
//    						}
//    	    				
//    	    				
//    	    			}).show();
//    	    		}
//    			
//    	    	} catch (Exception e) {
//    	    		e.printStackTrace();
//    	    		Log.e("预订出错", "---------");
//    	    		//dialog.dismiss();
//    	    	}
//    	    }else {
//    	    	dialog.dismiss();
//    		}
//    	    Log.e("httppost---", httpRquestString);
//		}else {
//			Toast.makeText(this, "乘车人资料有误", Toast.LENGTH_LONG).show();
//		}
//    	
//    	//access_token=6YtJFb5nHBmI8LZxOVBM7IFHSDnNlSwAPBdbp9jB&city=nn&type=7&car_type_id=2&start_position=长春市高新区&start_address=长春市高新区&expect_start_longitude=116.59458&expect_start_latitude=40.08597&time=2014-6-1115:45&end_address=长春市高新区&passenger_name=刘军&passenger_phone=18943132390&sms_type=1&aircode=PEK
//   
//    }
    
	protected String OrderId(){
		String orderId = null;
		
		jOrder = new JSONObject();
		jOrder = getJSONOrderRequest();
		System.out.println("jOrder ---> " + jOrder.toString());

		// httpRquestString = (String)HttpRequest.sendPost(url,
		// jOrder.toString());
		// System.out.println("httpRquestString ---> " +
		// httpRquestString);

		result = null;
		String jsonString = "orderRequest=" + jOrder.toString();
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		// 添加http头信息

		// 认证token
		httppost.addHeader("orderRequest", "application/json");
		httppost.addHeader("Content-type",
					"application/x-www-form-urlencoded");

		try {
			httppost.setEntity(new StringEntity(jsonString));
			HttpResponse response;
			response = httpclient.execute(httppost);
			result = EntityUtils.toString(response.getEntity());
			System.out.println("1.result ---> " + result);
			if(result != null){
				JSONObject res = new JSONObject(result.toString()).getJSONObject("Result");
				orderId = res.getString("Id");
				System.out.println("1.orderId ---> " + orderId);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return orderId;
	}
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
			
			//获取bundle对象的值
			String id;
			id = msg.getData().getString("OrderId");
			System.out.println("OrderId in handleMessage ---> " + id);

		}
		
	}
@SuppressWarnings("deprecation")
private void checkingStatusCode(String s){
		
		//Toast.makeText(HotelNewOrderYuDingMain.this, msg, 3000).show();
		dialog = new ProgressDialog(this);
		dialog.setMessage(s);
		dialog.setIndeterminate(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {							
				dialog.dismiss();
				
			}
		});

		dialog.show();
	}
	/***********************************************************************************/
	public Handler mhandler = new Handler() {
		public void handleMessage(Message msg) {
			Result result = new Result((String) msg.obj);
			switch (msg.what) {
			case RQF_PAY:
				Toast.makeText(OrderPnrMain.this, "",
						Toast.LENGTH_LONG).show();
				break;
			case RQF_LOGIN:
				Toast.makeText(OrderPnrMain.this, result.getResult(),
						Toast.LENGTH_SHORT).show();

				break;
			case PAY_OK:
				Bundle b = new Bundle();
				b.putString("DingDanNo", OrderId);
				Intent intent10 = new Intent(OrderPnrMain.this,
						OrderSuccse.class);
				intent10.putExtras(b);
				startActivity(intent10);
				break;
			case 3:
//				Toast.makeText(OrderPnrMain.this, "正在处理...", 5000).show();
//				checkingStatusCode(memo,false);
				break;
			case 4:
//				Toast.makeText(OrderPnrMain.this, "订单支付失败", Toast.LENGTH_LONG).show();
//				checkingStatusCode(memo,false);
				break;
			case 5:
//				Toast.makeText(OrderPnrMain.this, "支付已取消", Toast.LENGTH_LONG).show();
//				checkingStatusCode(memo,false);
				break;
			case 6:
//				Toast.makeText(OrderPnrMain.this, "网络链接出错", Toast.LENGTH_LONG).show();
//				checkingStatusCode(memo,false);
				break;
			default:
				break;
			}
		};
	};
	/************************************************************************************/
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageView8:
		
			nameString = (String)edNameEditText.getText().toString();
			System.out.println("order name "+edNameEditText.getText().toString());
			phoneString = (String)edPhoneEditText.getText().toString();
			System.out.println("order phone "+edPhoneEditText.getText().toString());
			addString = (String)edAddressEditText.getText().toString();
			System.out.println("order address "+edAddressEditText.getText().toString());
			numPerString = (String)edpersonNumText.getText().toString();
			
			//des_token = "6QIp1iWQ3LePoFCykN6h_RgyOA6nL3-U";
//			try {
//				des_token =
//							(String) DesCodeUtils.encode("11119688",
//										"SubmitOrder|2471CB5496F2A8C8");
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			System.out.println("des_token ---> " + des_token);

//			new Thread(){
//				public void run(){
//					Looper.prepare(); 
//					OrderId = OrderId();		
//					System.out.println("2.OrderId ---> " + OrderId);
//					if(OrderId !=null){
//						OrderInfo = getOrderInfo();
//						System.out.println("OrderInfo ---> " + OrderInfo);
//						String sign = Rsa.sign(OrderInfo, Keys.PRIVATE);
//						System.out.println("sign before encode ---> " + sign);
//						sign = URLEncoder.encode(sign);
//						OrderInfo +="&sign=\"" + sign + "\""+ "&sign_type=\"RSA\"";
//						System.out.println("OrderInfo****** " + OrderInfo);
//						new Thread(){
//							@SuppressWarnings("deprecation")
//							public void run(){
//								AliPay aliPay = new AliPay(OrderPnrMain.this, mhandler);
//								
//								
//								//支付宝支付后去的的返回值，orderResult
//								String orderResult = aliPay.pay(OrderInfo);
//								System.out.println("orderResult ---> " + orderResult);
//								
//								Message msg = new Message();
//								msg.what = PAY_OK;
//								msg.obj = orderResult;
//								mhandler.sendMessage(msg);
//							}
//						}.start();
//						
//					//	httpPostData();
//					} else{
//						System.out.println("null OrderId");
//					}
//					Looper.loop(); 
//				}
//				
//			}.start();
			int max = Integer.parseInt(maxLoad);
			if(getInternet() == false){
				Toast.makeText(OrderPnrMain.this, "网络链接已断开", Toast.LENGTH_LONG).show();
			}else if (phoneString.length() == 0) {
				Toast.makeText(this, "请填写联系电话", Toast.LENGTH_LONG).show();
			} else if (phoneString.length() < 11) {
				Toast.makeText(this, "电话填写错误", Toast.LENGTH_LONG).show();
			} else if (addString.equals("")) {
				Toast.makeText(this, "请填写出发地址", Toast.LENGTH_LONG).show();
			} else if (dataTextView.getText().toString().contains("日期")) {
				Toast.makeText(this, "请选择用车日期", Toast.LENGTH_LONG).show();
			} else if (timeTextView.getText().toString().contains("时间")) {
				Toast.makeText(this, "请选择用车时间", Toast.LENGTH_LONG).show();
			} else if (numPerString.equals("")) {
				Toast.makeText(this, "请填写乘车人数", Toast.LENGTH_LONG).show();
			} else if (Integer.parseInt(numPerString) > max) {
				Toast.makeText(this, "乘车人数超出限制", Toast.LENGTH_LONG).show();
			} else if (Integer.parseInt(numPerString) == 0) {
				Toast.makeText(this, "乘车人数不能为0", Toast.LENGTH_LONG).show();
			} else if (nameString.equals("")) {
				Toast.makeText(this, "请填写姓名", Toast.LENGTH_LONG).show();
			}else {

				setOrderId(OrderId());	
				Bundle b = new Bundle();
				b.putString("DingDanNo", getOrderId());
				Intent intent10 = new Intent(OrderPnrMain.this,
						OrderSuccse.class);
				intent10.putExtras(b);
				startActivity(intent10);
			}
			
			
		
			
//			int j=0;
//			int l=4;
//			Log.e("numPerString", ""+numPerString.length()+"----"+numPerString);
//			if (numPerString.length()!=0) {
//				double i = Double.valueOf(numPerString).doubleValue();
//				j = (int) i;
//				double k = Double.valueOf(personNumString).doubleValue();
//				l = (int) k;
//				if (j > l) {
//					Toast.makeText(this, "乘车人数有误", Toast.LENGTH_LONG).show();
//				}
//			}else {
//				System.out.println("order personNum "+edpersonNumText.getText().toString());
//				System.out.println("lat"+latString);
//				System.out.println("lng"+lngString);
//				System.out.println("airname"+airNameString);
//				System.out.println("airkey"+airkeyString);
//				System.out.println("hourtime"+huorTimeString);
//				//if (j <= l&&nameString!=null&&phoneString!=null&&addString!=null&&latString!=null&&lngString!=null&&airNameString!=null&&airkeyString!=null&&huorTimeString!=null) {
//					httpPostData();
//			}
		    

		
			//}else {
			//	Toast.makeText(this, "乘车人资料有误", Toast.LENGTH_LONG).show();
			//}
			
			
			
			break;
		case R.id.imageButton1:
			if (hourInt != 0) {
				hourTextView.setText(""+ hourInt);
				huorTimeString = ""+hourInt;
				hourInt--;
			}
			break;
		case R.id.imageButton2:
				hourInt++;
				hourTextView.setText(""+ hourInt);
				huorTimeString = ""+hourInt;			
			break;
		case R.id.textViewData:
			Log.e("日期按钮", "点击中...");
			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int day = c.get(Calendar.DAY_OF_MONTH);
			currentDate = year + "-" + month + "-" + day;
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = java.sql.Date.valueOf(currentDate); 
			Calendar calendar1   =  getCalendarDate(date1); 
			calendar1.setTime(date1); 
			calendar1.add(calendar1.DATE,0);//把日期往后增加一天.整数往后推,负数往前移动 
		    date1=calendar1.getTime();   //这个时间就是日期往后推一天的结果 
		    sdf.format(date1);
			Log.e("car chosen date --", sdf.format(date1));
			
		//跳转界面 并且设置回调函数方法
			Intent intent = new Intent(OrderPnrMain.this,
					DateSelectActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("liveDate", sdf.format(date1));
			intent.putExtras(bundle);
			intent.putExtra("type", "c");
			startActivityForResult(intent, RequestCode.TO_SELECT_DATE);
			break;
		

		default:
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
	
	class PayCarOrderTask extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String orderResult = null;
			try {
				des_token =
							(String) DesCodeUtils.encode("11119688",
										"SubmitOrder|2471CB5496F2A8C8");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("des_token ---> " + des_token);
			
			OrderId = getOrderId();
			System.out.println("2.OrderId ---> " + OrderId);
			if(OrderId !=null){
				OrderInfo = getOrderInfo();
				System.out.println("OrderInfo ---> " + OrderInfo);
				String sign = Rsa.sign(OrderInfo, Keys.PRIVATE);
				System.out.println("sign before encode ---> " + sign);
				sign = URLEncoder.encode(sign);
				OrderInfo +="&sign=\"" + sign + "\""+ "&sign_type=\"RSA\"";
				System.out.println("OrderInfo****** " + OrderInfo);
				
						AliPay aliPay = new AliPay(OrderPnrMain.this, mhandler);
						
						
						//支付宝支付后去的的返回值，orderResult
						orderResult = aliPay.pay(OrderInfo);
						System.out.println("orderResult ---> " + orderResult);
						
						

				
			//	httpPostData();
			} else{
				System.out.println("null OrderId");
			}
			
			
			return orderResult;
			
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = ProgressDialogTripg.show(OrderPnrMain.this, null, null);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			System.out.println("2.result ---> " + result);
			String s[] = result.split(";");
			System.out.println("s[0] ---> " + s[0]);
			//String s1[] = s[0].split("{");
			String s2 = s[0].substring(14, s[0].length()-1);
			//String s3[] = s[1].split("{");
			String memo = s[1].substring(6,s[1].length()-1);
			System.out.println("s2 ---> " + s2);
			System.out.println("memo ---> " + memo);
			if(s2.equals("9000")){
				Log.e("订单支付成功", "");
				Message msg = new Message();
				msg.what = PAY_OK;
				msg.obj = result;
				handler.sendMessage(msg);
			}else if(s2.equals("8000")){
//				Message msg = new Message();
//				msg.what = 3;
//				msg.obj = result;
//				handler.sendMessage(msg);
				checkingStatusCode(memo);
				Toast.makeText(OrderPnrMain.this, "正在处理...", 5000).show();
			}else if(s2.equals("4000")){
//				Message msg = new Message();
//				msg.what = 4;
//				msg.obj = result;
//				handler.sendMessage(msg);
				Toast.makeText(OrderPnrMain.this, "订单支付失败", Toast.LENGTH_LONG).show();
				checkingStatusCode(memo);
			}else if(s2.equals("6001")){
//				Message msg = new Message();
//				msg.what = 5;
//				msg.obj = memo;
//				handler.sendMessage(msg);
				checkingStatusCode(memo);
				Toast.makeText(OrderPnrMain.this, "支付已取消", Toast.LENGTH_LONG).show();
			}else if(s2.equals("6002")){
//				Message msg = new Message();
//				msg.what = 6;
//				msg.obj = result;
//				handler.sendMessage(msg);
				checkingStatusCode(memo);
				Toast.makeText(OrderPnrMain.this, "网络链接出错", Toast.LENGTH_LONG).show();
			}

		}
		
	}



	 
}
