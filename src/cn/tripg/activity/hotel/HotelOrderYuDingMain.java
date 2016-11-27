package cn.tripg.activity.hotel;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.internet.Exit;
import cn.internet.Tools;
import cn.tripg.R;
import cn.tripg.activity.flight.ResultCode;
import cn.tripg.interfaces.TripgMessage;
import cn.tripg.interfaces.impl.HotelOrderInterFaces;
import cn.tripg.widgit.ProgressDialogTripg;

public class HotelOrderYuDingMain extends Activity implements OnClickListener{

	public ImageView rImageViewmic;
	public ImageView rImageViewadd;
	public ImageView dImageViewmic;
	public ImageView dImageViewadd;
	public TextView roomtTextView;
	public TextView dayTextView;
	public TextView zongpicTextView;
	public TextView timeTextView;
	public ImageView tijiaoImageView;
	public ImageView backImageView;
	public EditText nameEditText;
	public EditText phoneEditText;
	public Builder alertDialog;
	public String[] arrayString;
	public int roomNum;
	public int dayNum;
	public int zongpicNum;
	public int j;
	public String roomNameString;
	public String roolIdString;
	public String hotelNameString;
	public String hotelIdString;
	public String ratePlanNameString;
	public String ratePlanIdString;
	public String ratePlanCodeString;
	public String hotelAddString;
	public String leaveTimeString;
	public String liveTimeString;
	public String danbaoString;
	public String userid;
	public HotelOrderInterFaces hotelOrderInterFaces;
	public ProgressDialog progressDialog1;
	private ProgressDialog dialog;
	
	private int dateType;
	private String[] timeString;
	//arriving time
	private String arrivingTime = "";
	private String postArrivalEarlyTime = "";
	private String postArrivalLateTime = "";
	
	
	/**
	 * @param  getGapCount  
	 * 获取日期差的函数，将两个日期传入此函数，返回一个int 类型的日期差
	 * 
	 * */
	public static int getGapCount(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();  
        fromCalendar.setTime(startDate);  
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);  
        fromCalendar.set(Calendar.MINUTE, 0);  
        fromCalendar.set(Calendar.SECOND, 0);  
        fromCalendar.set(Calendar.MILLISECOND, 0);  
  
        Calendar toCalendar = Calendar.getInstance();  
        toCalendar.setTime(endDate);  
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);  
        toCalendar.set(Calendar.MINUTE, 0);  
        toCalendar.set(Calendar.SECOND, 0);  
        toCalendar.set(Calendar.MILLISECOND, 0);  
  
        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
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
		setContentView(R.layout.hoteorderyuding);
		Exit.getInstance().addActivity(this);
		

		
		//返回按钮
		backImageView = (ImageView)findViewById(R.id.title_back);
		backImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = getIntent();
				setResult(ResultCode.FAILURE, intent);
				finish();
				
			}
		});
		
		Intent intent = getIntent();
		roomNameString = (String)intent.getExtras().getString("RoomName");
		roolIdString = (String)intent.getExtras().getString("RoomTypeId");
		hotelNameString =(String)intent.getExtras().getString("HotelName");
		hotelIdString = (String)intent.getExtras().getString("HotelId");
		ratePlanIdString = (String)intent.getExtras().getString("RatePlanID");
		ratePlanNameString = (String)intent.getExtras().getString("RatePlanName");
		ratePlanCodeString = (String)intent.getExtras().getString("RatePlanCode");
		hotelAddString = (String)intent.getExtras().getString("HotelAddress");
		leaveTimeString = (String)intent.getExtras().getString("leaveTime");
		liveTimeString = (String)intent.getExtras().getString("liveTime");
		danbaoString = (String)intent.getExtras().getString("GarKey");
		
		System.out.println("danbaoString --->" + danbaoString);
		
	
		roomNum = 1;
		//dayNum = 1;
		//测试
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		   Date date_start = null;
		   Date date_end = null;
		try {
		date_start = sdf.parse(liveTimeString);
		date_end = sdf.parse(leaveTimeString);
		} catch (java.text.ParseException e) {
		
		e.printStackTrace();
		}
		  System.out.println("日期差在这呢=====  "+getGapCount(date_start,date_end));
		  dayNum = getGapCount(date_start,date_end);
		
		System.out.println("cn name --> " + new Tools().getCnName(getApplicationContext()));
		nameEditText = (EditText)findViewById(R.id.editTexthotelPername);
		phoneEditText = (EditText)findViewById(R.id.editTexthotelphoneNum);
		phoneEditText.setText(new Tools().getUserName(getApplicationContext()));
		timeTextView = (TextView)findViewById(R.id.textViewhoteltime1);
		timeTextView.setOnClickListener(this);
		
		rImageViewmic = (ImageView)findViewById(R.id.imageViewhotelmic);
		rImageViewmic.setOnClickListener(this);
		rImageViewadd = (ImageView)findViewById(R.id.imageViewaddhetle1);
		rImageViewadd.setOnClickListener(this);
		dImageViewmic = (ImageView)findViewById(R.id.imageViewhotelnummic);
		dImageViewmic.setOnClickListener(this);
		dImageViewadd = (ImageView)findViewById(R.id.imageViewnumaddhetle1);
		dImageViewadd.setOnClickListener(this);
		roomtTextView = (TextView)findViewById(R.id.textViewfanghotel1);
		dayTextView = (TextView)findViewById(R.id.textViewfanghotelnum1);
		dayTextView.setText(""+dayNum);
		zongpicTextView = (TextView)findViewById(R.id.textViewhotelpic);
		String sp = intent.getExtras().getString("TotalPrice");
		double i = Double.valueOf(sp).doubleValue();
		 j = (int) i;
		zongpicNum = j;
		Log.e("转型 j ", ""+j);
		zongpicTextView.setText("￥"+(j));
		tijiaoImageView = (ImageView)findViewById(R.id.imageViewhotelyuding);
		tijiaoImageView.setOnClickListener(this);

		
/*************************************************************************************************/		
		/*
		 * 设定到店时间
		 */
		dateType = getDateOrdertype();
		int hr = getTimeCurrentHr();
		int hr_nt1 = hr + 1;
		int hr_nt3 = hr + 3;
		System.out.println("hr_nt1 ---> " + hr_nt1 + "\n" + "hr_nt3 ---> " + hr_nt3);

		if(dateType == 1){
			//当天
			if(danbaoString.equals("1")){
				timeTextView.setText(hr_nt1+":00"+"-"+hr_nt3+":00");
			}
		}else{
			//非当天
			if(danbaoString.equals("1")){
				timeTextView.setText("14:00-20:00");
			}
		}
//		if(dateType == 0){
//			arriveTime.setText("请选择到店时间");
//			today = false;
//		}else{
//			today = true;
//			if(hr_nt1 > 24){
//				arriveTime.setText("次日"+(hr_nt1-24)+":00-"+"次日"+(hr_nt3-24)+":00");
//			}else if(hr_nt1 < 24 && hr_nt3 > 24){
//				arriveTime.setText(hr_nt1+":00-"+"次日"+(hr_nt3-24)+":00");
//			}else if(hr_nt1 < 24 && hr_nt3 < 24){
//				arriveTime.setText(hr_nt1+":00-"+hr_nt3+":00");
//			}
//			
//		}
/*************************************************************************************************/				
		
	}
	
	
/*************************************************************************************************/	
			/*
			 * 检测预定类型
			 */
		private int getDateOrdertype(){
		int type = 0;
/*************************************************************************************************/		
				/*
				 * 获取当前系统日期
				 */
		Time time = new Time();// or Time t=new Time("GMT+8"); 加上Time Zone资料。  
		time.setToNow();// 取得系统时间。  
		String year = ""+time.year;
		String month = ""+(time.month+1);
		String day = ""+time.monthDay;
		System.out.println("year ---> " + year + "\n" + "month ---> " + month +"\n" + "day ---> " + day);
/*************************************************************************************************/		
		String checkIn[] = liveTimeString.split("-");
		String checkIn_yr = checkIn[0];
		String checkIn_mn = checkIn[1];
		String checkIn_dy = checkIn[2];
		System.out.println("checkIn_yr ---> " + checkIn_yr + "\n" + "checkIn_mn ---> " + checkIn_mn +"\n" + "checkIn_dy ---> " + checkIn_dy);
		if(checkIn_yr.equals(year) && checkIn_mn.equals(month) && checkIn_dy.equals(day)){
		//当天
		type = 1;
		}else{
		type = 0;
		}
		return type;
		}
/*************************************************************************************************/	

/*************************************************************************************************/	
					/*
					 * 检测预定类型
					 */
		private int getTimeCurrentHr(){
		/*************************************************************************************************/		
			/*
			 * 获取当前系统时间
			 */
		Time time = new Time();// or Time t=new Time("GMT+8"); 加上Time Zone资料。  
		time.setToNow();// 取得系统时间。  
		String hr = ""+time.hour;
		System.out.println("hour ---> " + hr);
		/*************************************************************************************************/		
		String hour[] = hr.split(":");
		int hr_int = Integer.parseInt(hour[0]);
		return (hr_int);
		}
/*************************************************************************************************/	
/*************************************************************************************************/	
		/*
		* 检测预定类型
		*/
		private int getTimeCurrentMin(){
		/*************************************************************************************************/		
		/*
		* 获取当前系统时间
		*/
		Time time = new Time();// or Time t=new Time("GMT+8"); 加上Time Zone资料。  
		time.setToNow();// 取得系统时间。  
		String min = ""+time.minute;
		System.out.println("minute ---> " + min);
		/*************************************************************************************************/		
		//String hour[] = hr.split(":");
		//int hr_int = Integer.parseInt(hour[0]);
		return (Integer.parseInt(min));
		}
/*************************************************************************************************/	

	private void showAlaerDialog(){
		
		alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("请选择服务类型");
		arrayString = new String[]{"12:00-15:00","13:00-16:00","14:00-17:00","15:00-18:00","16:00-19:00","17:00-20:00","18:00-21:00","19:00-22:00","20:00-23:00","21:00-23:59","22:00-次日6:00"};
		//String[] typeArrayStrings = new String[]{"7","8","12","11","1"};

		final ArrayList<String> arrayList = new ArrayList<String>();
		//final ArrayList<String> arrayListType = new ArrayList<String>();

			for (int i = 0; i < arrayString.length; i++) {
				if (!arrayString[i].equals("null")) {
					arrayList.add(arrayString[i]);	
					//arrayListType.add(typeArrayStrings[i]);

				}
			}
			//将遍历之后的数组 arraylist的内容在选择器上显示 
		alertDialog.setSingleChoiceItems(arrayList.toArray(new String[0]), 0, new DialogInterface.OnClickListener(){

			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
				System.out.println("***被点击***"+arg1);
			
				arg0.toString();
				switch (arg1) {
				case 0:
					timeTextView.setText(arrayList.get(arg1));
					
					break;
				case 1:
					timeTextView.setText(arrayList.get(arg1));
					
					break;
				case 2:
					timeTextView.setText(arrayList.get(arg1));
					
					break;
				case 3:
					timeTextView.setText(arrayList.get(arg1));

					break;
				case 4:
					timeTextView.setText(arrayList.get(arg1));

					break;
				case 5:
					timeTextView.setText(arrayList.get(arg1));

					break;
				case 6:
					timeTextView.setText(arrayList.get(arg1));

					break;
				case 7:
					timeTextView.setText(arrayList.get(arg1));

					break;
				case 8:
					timeTextView.setText(arrayList.get(arg1));

					break;
				case 9:
					timeTextView.setText(arrayList.get(arg1));

					break;
				case 10:
					timeTextView.setText(arrayList.get(arg1));

					break;
				default:
					break;
				}
			}
			
			
		}).show();
	}
	


	@Override
	public void onClick(View arg0) {
		
	switch (arg0.getId()) {
		case R.id.editTexthotelPername:
			Log.e("editTexthotelPername", "");
			break;
		case R.id.editTexthotelphoneNum:
			Log.e("editTexthotelphoneNum", "");
			break;
		case R.id.textViewhoteltime1:
			Log.e("textViewhoteltime1", "");
			
			//showAlaerDialog();
			if(dateType == 0 && danbaoString.equals("0")){
				//非当天&不需担保
				showChooseNonCurNonGarTimeDialog();
			}else if(dateType == 1 && danbaoString.equals("0")){
				//当天&不需担保
				if(getTimeCurrentHr() < 14){
					showChooseNonCurNonGarTimeDialog();//same as 非当天&不需担保
				}else if ((getTimeCurrentHr() == 14
						&& getTimeCurrentMin() > 0
						&& getTimeCurrentHr() < 19) || (getTimeCurrentHr() > 14 && getTimeCurrentHr() < 19)) {
					showChooseCurNonGarTimeDialog1();
				}else if((getTimeCurrentHr() == 19 && getTimeCurrentMin() > 0) || (getTimeCurrentHr() > 19)){
					if(getTimeCurrentHr() <= 23 && getTimeCurrentMin() <= 59){
						showChooseCurNonGarTimeDialog2();
					}
				}else if(getTimeCurrentHr() >= 24 && getTimeCurrentHr() <= 6){
					showChooseCurNonGarTimeDialog3();
				}
			}
			
			break;
		case R.id.imageViewhotelmic:
			Log.e("imageViewhotelmic", "");
			int dayCount4 = Integer.parseInt(dayTextView.getText().toString());
			int roomCount4 = Integer.parseInt(roomtTextView.getText().toString());
			if (roomCount4 != 0) {
				//roomtTextView.setText(""+roomNum);
				if (roomCount4 != 1) {
					roomCount4--;
					roomtTextView.setText(""+roomCount4);
					zongpicNum = j * roomCount4;
					zongpicTextView.setText("￥"+zongpicNum);
				}
				

					

//				if (roomNum != 1 && dayNum != 1) {
					
//					Log.e("a  zongpicNum is ", ""+zongpicNum);
//				}else if (roomNum == 1 && dayNum != 1) {
//					zongpicNum = j * dayNum;
//					Log.e("b  zongpicNum is ", ""+zongpicNum);
//					
//				}else if (roomNum != 1 && dayNum == 1) {
//					zongpicNum = j * roomNum  ;
//					Log.e("c  zongpicNum is ", ""+zongpicNum);
//				}
				//Log.e("roomNum is ", ""+roomNum);
				
			}
			
			break;
		case R.id.imageViewaddhetle1:
			Log.e("imageViewaddhetle1", "");
			int dayCount3 = Integer.parseInt(dayTextView.getText().toString());
			int roomCount3 = Integer.parseInt(roomtTextView.getText().toString());
			if (roomCount3 != 10) {
				roomCount3++;
				roomtTextView.setText(""+roomCount3);
				zongpicNum = j * roomCount3;
				zongpicTextView.setText("￥"+zongpicNum);

//				if (roomNum != 1 && dayNum != 1) {
					
//					Log.e("a  zongpicNum is ", ""+zongpicNum);
//				}else if (roomNum == 1 && dayNum != 1) {
//					zongpicNum = j * dayNum;
//					Log.e("b  zongpicNum is ", ""+zongpicNum);
//					
//				}else if (roomNum != 1 && dayNum == 1) {
//					zongpicNum = j * roomNum  ;
//					Log.e("c  zongpicNum is ", ""+zongpicNum);
//				}
				//Log.e("roomNum is ", ""+roomNum);
				
			}
			
			break;
		case R.id.imageViewhotelnummic:
			Log.e("imageViewhotelnummic", "");
			
			int dayCount1 = Integer.parseInt(dayTextView.getText().toString());
			int roomCount1 = Integer.parseInt(roomtTextView.getText().toString());
			
//			if (dayCount1 != 0) {				
				//dayTextView.setText(""+dayNum);
				if (dayCount1 != 1) {
					leaveTimeString = getDateBack(leaveTimeString);
					dayCount1--;
					dayTextView.setText(""+dayCount1);
					zongpicNum = j * roomCount1;
					zongpicTextView.setText("￥"+zongpicNum);
					}
				
				
//				if (roomNum != 1 && dayNum != 1) {
					
//					Log.e("a  zongpicNum is ", ""+zongpicNum);
//				}else if (roomNum == 1 && dayNum != 1) {
//					zongpicNum = j * dayNum;
//					Log.e("b  zongpicNum is ", ""+zongpicNum);
//					
//				}else if (roomNum != 1 && dayNum == 1) {
//					zongpicNum = j * roomNum  ;
//					Log.e("c  zongpicNum is ", ""+zongpicNum);
//				}
				//Log.e("daynum is ", ""+dayNum);
				
//			}
			
			break;
		case R.id.imageViewnumaddhetle1:
			Log.e("imageViewnumaddhetle1", "");
			
			int dayCount2 = Integer.parseInt(dayTextView.getText().toString());
			int roomCount2 = Integer.parseInt(roomtTextView.getText().toString());
			if (dayCount2 != 30) {
				leaveTimeString = getDateNext(leaveTimeString);
				dayCount2++;
				dayTextView.setText(""+dayCount2);
				Log.e("daynum is ", ""+dayCount2);
//				if (roomNum != 1 && dayNum != 1) {
				zongpicNum = j * (dayCount2) * roomCount2;
				zongpicTextView.setText("￥"+zongpicNum);
				
//					Log.e("roomNum != 1 && dayNum != 1   zongpicNum is ", ""+zongpicNum);
//				}else if (roomNum == 1 && dayNum != 1) {
//					zongpicNum = j * dayNum;
//					Log.e("roomNum == 1 && dayNum != 1   zongpicNum is ", ""+zongpicNum);
//					
//				}else if (roomNum != 1 && dayNum == 1) {
//					zongpicNum = j * roomNum  ;
//					Log.e("roomNum != 1 && dayNum == 1   zongpicNum is ", ""+zongpicNum);
//				}
				
			}
			
			break;
		case R.id.textViewfanghotel1:
			Log.e("textViewfanghotel1", "");
			break;
		case R.id.textViewfanghotelnum1:
			Log.e("textViewfanghotelnum1", "");
			break;
		case R.id.textViewhotelpic:
			Log.e("textViewhotelpic", "");
			break;
		case R.id.imageViewhotelyuding:
			Log.e("imageViewhotelyuding", "");
//			progressDialog1 = ProgressDialogTripg.show(HotelOrderYuDingMain.this, null, null);
//			
//		try {
//			httpyudingpost();
//		} catch (UnsupportedEncodingException e) {
//			
//			e.printStackTrace();
//		}
			if(getInternet() == false){
				Toast.makeText(HotelOrderYuDingMain.this, "网络链接已断开", Toast.LENGTH_LONG).show();
			}else if(nameEditText.getText().toString().equals("")){
				Toast.makeText(HotelOrderYuDingMain.this, "请填写姓名", Toast.LENGTH_SHORT).show();
			}else if(phoneEditText.getText().toString().length() == 0){
				Toast.makeText(HotelOrderYuDingMain.this, "请填写电话", Toast.LENGTH_SHORT).show();
			}else if(phoneEditText.getText().toString().length() < 11){
				Toast.makeText(HotelOrderYuDingMain.this, "电话长度错误", Toast.LENGTH_SHORT).show();
			}else if(timeTextView.getText().toString().contains("时间")){
				Toast.makeText(HotelOrderYuDingMain.this, "请选择预留时间", Toast.LENGTH_SHORT).show();
			}else{
				HttpPostTask hpt = new HttpPostTask();
				hpt.execute();
			}
			
			
			break;

		default:
			break;
		}
		
	}
	/*************************************************************************************************/	
	/*
	 * dialog for choosing the arriving time if garKey = 0 and non current day
	 */
	private void showChooseNonCurNonGarTimeDialog(){
		alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("请选择添到店时间");
		timeString = new String[] { "20:00", "23:59","次日06:00"};
		final ArrayList<String> arrayList = new ArrayList<String>();
		//final ArrayList<String> arrayListType = new ArrayList<String>();

		for (int i = 0; i < timeString.length; i++) {
			if (!timeString[i].equals("null")) {
				arrayList.add(timeString[i]);	
				//arrayListType.add(typeArrayStrings[i]);
				//timeTextView.setText(timeString[0]);
			}
		}
		//将遍历之后的数组 arraylist的内容在选择器上显示 
		alertDialog.setSingleChoiceItems(arrayList.toArray(new String[0]), 0, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				System.out.println("***被点击***"+which);
				dialog.toString();
				switch (which) {
				case 0:
					arrivingTime = "14:00-20:00";
					timeTextView.setText(arrivingTime);
					postArrivalEarlyTime = liveTimeString + " " +"14:00:00";
					postArrivalLateTime = leaveTimeString + " " + "20:00:00";
					setTime(arrivingTime);
					break;					
				case 1:
					
					arrivingTime = "20:00-23:59";
					timeTextView.setText(arrivingTime);
					postArrivalEarlyTime = liveTimeString + " " +"20:00:00";
					postArrivalLateTime = leaveTimeString + " " + "23:59:00";
					setTime(arrivingTime);
					break;			
				case 2:			
					
					arrivingTime = "23:59-次日06:00";
					timeTextView.setText(arrivingTime);
					postArrivalEarlyTime = liveTimeString + " " +"23:59:00";
					postArrivalLateTime = getDateNext(liveTimeString) + " " + "06:00:00";
					int dayNum1 = Integer.parseInt(dayTextView.getText().toString());
					int roomNum1 = Integer.parseInt(roomtTextView.getText().toString());
					if(dayNum1 != 1){
						//dayTextView.setText(""+(dayNum1+1));
						//zongpicTextView.setText("￥"+((j/dayNum1)*(dayNum1+1)*roomNum1));
					}else{
						//dayTextView.setText("1");
						//zongpicTextView.setText("￥"+(j*1*roomNum1));
					}
					
					setTime(arrivingTime);
					break;				
				default:
					arrivingTime = "";
					setTime(arrivingTime);
					break;
				}
			}
			
		}).show();
	}
/*************************************************************************************************/	
/*************************************************************************************************/	
	/*
	 * dialog for choosing the arriving time if garKey = 0 and current day
	 */
	private void showChooseCurNonGarTimeDialog2(){
		alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("请选择添到店时间");
		timeString = new String[] {"23:59","次日06:00"};
		final ArrayList<String> arrayList = new ArrayList<String>();
		//final ArrayList<String> arrayListType = new ArrayList<String>();

		for (int i = 0; i < timeString.length; i++) {
			if (!timeString[i].equals("null")) {
				arrayList.add(timeString[i]);	
				//arrayListType.add(typeArrayStrings[i]);

			}
		}
		//将遍历之后的数组 arraylist的内容在选择器上显示 
		alertDialog.setSingleChoiceItems(arrayList.toArray(new String[0]), 0, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				System.out.println("***被点击***"+which);
				dialog.toString();
				switch (which) {
				case 0:
					
					arrivingTime = (getTimeCurrentHr()+1)+":00"+"-"+"23:59";
					timeTextView.setText(arrivingTime);
					postArrivalEarlyTime = liveTimeString + " " + (getTimeCurrentHr()+1)+":00:00";
					postArrivalLateTime = leaveTimeString + " " + "23:59:00";
					setTime(arrivingTime);
					break;					
				case 1:
					
					arrivingTime = "23:59-次日06:00";
					timeTextView.setText(arrivingTime);
					postArrivalEarlyTime = liveTimeString + " " +"23:59:00" ;
					postArrivalLateTime =  getDateNext(liveTimeString)+ " " + "06:00:00";
					setTime(arrivingTime);
					int dayNum1 = Integer.parseInt(dayTextView.getText().toString());
					int roomNum1 = Integer.parseInt(roomtTextView.getText().toString());
					if(dayNum1 != 1){
						//dayTextView.setText(""+(dayNum1+1));
						//zongpicTextView.setText("￥"+((j/dayNum1)*(dayNum1-1)*roomNum1));
					}else{
						//dayTextView.setText("1");
						//zongpicTextView.setText("￥"+(j*1*roomNum1));
					}
					break;						
				default:
					arrivingTime = "";
					setTime(arrivingTime);
					break;
				}
			}
			
		}).show();
	}
/*************************************************************************************************/	

	public Calendar getCalendarDate(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar;
	}
	
	private String getDateNext(String s){
		Date date = java.sql.Date.valueOf(s); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar calendar   =  getCalendarDate(date); 
		calendar.setTime(date); 
		calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
	    date=calendar.getTime();   //这个时间就是日期往后推一天的结果 
	    sdf.format(date);
	    Log.e("next day ---", sdf.format(date));
	    return sdf.format(date);
	}
	
	private String getDateBack(String s){
		Date date = java.sql.Date.valueOf(s); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar calendar   =  getCalendarDate(date); 
		calendar.setTime(date); 
		calendar.add(calendar.DATE,-1);//把日期往后增加一天.整数往后推,负数往前移动 
	    date=calendar.getTime();   //这个时间就是日期往后推一天的结果 
	    sdf.format(date);
	    Log.e("back day ---", sdf.format(date));
	    return sdf.format(date);
	}
/*************************************************************************************************/	
	/*
	 * dialog for choosing the arriving time if garKey = 0 and current day
	 */
	private void showChooseCurNonGarTimeDialog3(){
		alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("请选择添到店时间");
		timeString = new String[] {"次日06:00"};
		final ArrayList<String> arrayList = new ArrayList<String>();
		//final ArrayList<String> arrayListType = new ArrayList<String>();

		for (int i = 0; i < timeString.length; i++) {
			if (!timeString[i].equals("null")) {
				arrayList.add(timeString[i]);	
				//arrayListType.add(typeArrayStrings[i]);

			}
		}
		//将遍历之后的数组 arraylist的内容在选择器上显示 
		alertDialog.setSingleChoiceItems(arrayList.toArray(new String[0]), 0, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				System.out.println("***被点击***"+which);
				dialog.toString();
				switch (which) {
				case 0:
					
					arrivingTime = "23:59-次日06:00";
					timeTextView.setText(arrivingTime);
					postArrivalEarlyTime = liveTimeString + " " +"23:59:00";
					postArrivalLateTime = getDateNext(liveTimeString) + " " + "06:00:00";
					setTime(arrivingTime);
					int dayNum1 = Integer.parseInt(dayTextView.getText().toString());
					int roomNum1 = Integer.parseInt(roomtTextView.getText().toString());
					if(dayNum1 != 1){
						//dayTextView.setText(""+(dayNum1+1));
						//zongpicTextView.setText("￥"+((j/dayNum1)*(dayNum1-1)*roomNum1));
					}else{
						//dayTextView.setText("1");
						//zongpicTextView.setText("￥"+(j*1*roomNum1));
					}
					break;					
			
				default:
					arrivingTime = "";
					setTime(arrivingTime);
					break;
				}
			}
			
		}).show();
	}
/*************************************************************************************************/	
	/*************************************************************************************************/	
	/*
	 * dialog for choosing the arriving time if garKey = 0 and current day
	 */
	private void showChooseCurNonGarTimeDialog1(){
		alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("请选择添到店时间");
		timeString = new String[] { "20:00", "23:59","次日06:00"};
		final ArrayList<String> arrayList = new ArrayList<String>();
		//final ArrayList<String> arrayListType = new ArrayList<String>();

		for (int i = 0; i < timeString.length; i++) {
			if (!timeString[i].equals("null")) {
				arrayList.add(timeString[i]);	
				//arrayListType.add(typeArrayStrings[i]);

			}
		}
		//将遍历之后的数组 arraylist的内容在选择器上显示 
		alertDialog.setSingleChoiceItems(arrayList.toArray(new String[0]), 0, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				System.out.println("***被点击***"+which);
				dialog.toString();
				switch (which) {
				case 0:
					
					arrivingTime = (getTimeCurrentHr()+1)+":00"+"-"+"20:00";
					postArrivalEarlyTime = liveTimeString + " " + (getTimeCurrentHr()+1)+":00:00";
					postArrivalLateTime = leaveTimeString + " " + "20:00:00";
					timeTextView.setText(arrivingTime);
					setTime(arrivingTime);
					break;					
				case 1:
					
					arrivingTime = "20:00-23:59";
					timeTextView.setText(arrivingTime);
					postArrivalEarlyTime = liveTimeString + " " +"20:00:00";
					postArrivalLateTime = leaveTimeString + " " + "23:59:00";
					setTime(arrivingTime);
					break;			
				case 2:			
					
					arrivingTime = "23:59-次日06:00";
					timeTextView.setText(arrivingTime);
					postArrivalEarlyTime = liveTimeString + " " +"23:59:00";
					postArrivalLateTime = getDateNext(liveTimeString) + " " + "06:00:00";
					setTime(arrivingTime);
					int dayNum1 = Integer.parseInt(dayTextView.getText().toString());
					int roomNum1 = Integer.parseInt(roomtTextView.getText().toString());
					if(dayNum1 != 1){
						//dayTextView.setText(""+(dayNum1+1));
						//zongpicTextView.setText("￥"+((j/dayNum1)*(dayNum1-1)*roomNum1));
					}else{
						//dayTextView.setText("1");
						//zongpicTextView.setText("￥"+(j*1*roomNum1));
					}
					
					break;				
				default:
					arrivingTime = "";
					setTime(arrivingTime);
					break;
				}
			}
			
		}).show();
	}
/*************************************************************************************************/	
	private String setTime(String time){
		System.out.println("time ----> " + time);
		
		String arrTime = time;
		return arrTime;
	}
	
	private boolean isUserLogin(){
        SharedPreferences sharedPre = HotelOrderYuDingMain.this.getSharedPreferences(
        		"config", Context.MODE_PRIVATE);
        //清除数据
        //sharedPre.edit().clear();
        //sharedPre.edit().commit();
        //存储数据
       // sharedPre.edit().putString("", "");
       // sharedPre.edit().commit();
        userid = sharedPre.getString("Result", "");
      
        Log.e("userid ---", "" + userid);

        if("".equals(userid) ){
        	return false;
        }else{
        	return true;
        }
    }
	
	public JSONObject jsonObjectDict() throws UnsupportedEncodingException{
		isUserLogin();
		JSONObject dict = new JSONObject();  
		JSONObject roomJson = new JSONObject();  
		JSONObject contactersJson = new JSONObject();  
		JSONObject guestsJson = new JSONObject();  
		
		String stringname = (String)nameEditText.getText().toString();
		stringname = URLEncoder.encode(stringname, "UTF-8");
		Log.e("string name is ----", ""+stringname);
		String stringphone = (String)phoneEditText.getText().toString();
//		String timeString = (String)timeTextView.getText();
//		System.out.println("timeString ---> " + timeString);
//		String spStr[] = timeString.split("-"); 
//		String tiemString1 = (String)spStr[0];
//		String tiemString2 = (String)spStr[1];
		
		Log.e("ArrivalEarlyTime", ""+ postArrivalEarlyTime);
		Log.e("ArrivalLateTime", ""+postArrivalLateTime);
		Log.e("new hotelIdString", ""+hotelIdString);
		try {			
			roomJson.put("HotelId", hotelIdString);
			roomJson.put("MemberId", userid);
			roomJson.put("RoomTypeId", roolIdString);
			roomJson.put("RatePlanId", ratePlanIdString);
			roomJson.put("RatePlanCode", ratePlanCodeString);
			roomJson.put("OrderGarantee", danbaoString);
			roomJson.put("CheckInDate", liveTimeString + " " + "00:00:00");
			roomJson.put("CheckOutDate", leaveTimeString + " " + "00:00:00");
			roomJson.put("GuestTypeCode", "0");
			String roomNumsString = ""+roomNum;
			roomJson.put("RoomAmount", roomtTextView.getText().toString());
			roomJson.put("GuestAmount", roomtTextView.getText().toString());
			roomJson.put("PaymentTypeCode", "0");
			roomJson.put("ArrivalEarlyTime", postArrivalEarlyTime);
			roomJson.put("ArrivalLateTime", postArrivalLateTime);
			roomJson.put("CurrencyCode", "RMB");

			roomJson.put("TotalPrice", zongpicTextView.getText().toString().replace("￥", ""));
			roomJson.put("ConfirmTypeCode", "phone");
			
			contactersJson.put("Name", stringname);
			contactersJson.put("GenderCode", "2");
			contactersJson.put("Mobile", stringphone);
			
			JSONArray jsonArray = new JSONArray();
			
			int count = Integer.parseInt(roomtTextView.getText().toString());
			for(int i=0;i<count;i++){
				guestsJson = new JSONObject();
				guestsJson.put("Name", stringname);
				guestsJson.put("GenderCode", "2");
				jsonArray.put(guestsJson);
			}		
			
			roomJson.put("Contacters", contactersJson);
			roomJson.put("Guests", jsonArray);
			
			dict.put("RoomGroups", roomJson);
			dict.put("Source", "Elong");
			dict.put("CustomerIp", "192.168.2.16");
			
		
		} catch (JSONException e) {
			e.printStackTrace();
		}
		

		return dict;
	}
	
	@SuppressWarnings("deprecation")
	public void httpyudingpost() throws UnsupportedEncodingException{
		Log.e("http post ", "调用");
		
		
		
		JSONObject jsonObject = jsonObjectDict();
		String jsonString = "OrderRequest="+jsonObject.toString();
		
		Log.e("jsonString****", ""+jsonString);
		
		String url = "http://mapi.tripglobal.cn/Hotel.aspx?action=SubmitHotelOrder&";
		
		
//		hotelOrderInterFaces = new HotelOrderInterFaces(HotelOrderYuDingMain.this, handler);
//		hotelOrderInterFaces.getModelFromPOST(url, jsonString, TripgMessage.HANGBAN);
		
		try {  
		    HttpClient httpclient = new DefaultHttpClient();   
		    HttpPost httppost = new HttpPost(url);   
		    //添加http头信息   
		    
		    //认证token   
		    httppost.addHeader("OrderRequest", "application/json");  
		    httppost.addHeader("Content-type", "application/x-www-form-urlencoded");  

		    httppost.setEntity(new StringEntity(jsonString));     
		    HttpResponse response;  
		    response = httpclient.execute(httppost);  
		    String rev1 = EntityUtils.toString(response.getEntity());
		   
		    //obj = new JSONObject(rev);  
		    //检验状态码，如果成功接收数据
//		    int code = response.getStatusLine().getStatusCode();
		    if (rev1.length() != 0) {
				progressDialog1.dismiss();
				Log.e("http response is ", rev1);
				System.out.println("order request result -----> " + rev1);
		    	checkingStatusCode(rev1);
	
			}

		    } catch (ClientProtocolException e) {   
		    	
		    } catch (IOException e) {
		    	
		    } catch (Exception e) {   
		    	
		    }  
		
	}
	
	@SuppressWarnings("deprecation")
	private void checkingStatusCode(String s){
		String result = null;
		try {
			JSONObject jObject = new JSONObject(s);
			result = jObject.getString("Result");
			String code = jObject.getString("Code");
			if(code.equals("1")){
				Intent intent = new Intent(HotelOrderYuDingMain.this, HotelOrderSucces.class);
				intent.putExtra("Result", result);
				startActivity(intent);
			}else{
				String msg = jObject.getString("Message");
				//Toast.makeText(HotelNewOrderYuDingMain.this, msg, 3000).show();
				dialog = new ProgressDialog(this);
				dialog.setTitle("提交订单失败");
				dialog.setMessage(msg);//
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
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	//预订酒店  回调方法
		
		private Handler handler = new Handler(){
	        public void handleMessage(Message msg) {
	        	switch(msg.what){
	        	case TripgMessage.HANGBAN:
	        		handMessageDefault(hotelOrderInterFaces,HotelOrderYuDingMain.this, msg);
	        		break;
	        	}
	        	
	        	
	        }

			private void handMessageDefault(HotelOrderInterFaces bf,
					HotelOrderYuDingMain hotelOrderYuDingMain, Message msg) {
				
				if(bf == null)
		    		return;
		    	if(bf.progressDialog != null)
					bf.progressDialog.dismiss();
		    	if(msg.obj == null){
		    		Toast.makeText(hotelOrderYuDingMain, "网络链接超时",
		    				Toast.LENGTH_SHORT).show();
		    		
		    	}else{
		    		//TO do
//		    		model = (AVVo)msg.obj;
//		    		Log.e("model", model.Code);
//		    		String jString = (String)msg.obj;
//		    		Log.e("航班号解析完成", jString);
//		    		xmlList = (List<XmlCityModel>)msg.obj;
//		    		Log.e("解析内容", xmlList.get(0).fNoString);
//		    		Intent intent = new Intent(dongTaiMainthis,DongXiangqing.class);
//		    		Bundle bundle1 = new Bundle();
//		    		bundle1.putString("FilghtNo", xmlList.get(0).fNoString);
//		    		bundle1.putString("FlightCompany", xmlList.get(0).fCompanyString);
//		    		bundle1.putString("FlightDep", xmlList.get(0).fDepString);
//		    		bundle1.putString("FlightArr", xmlList.get(0).fArrString);
//		    		bundle1.putString("FlightDepAirport", xmlList.get(0).fDepAirportString);
//		    		bundle1.putString("FlightArrAirport", xmlList.get(0).fArrAirportString);
//		    		bundle1.putString("FlightDeptimePlan", xmlList.get(0).fDeptimePlanString);
//		    		bundle1.putString("FlightArrtimePlan", xmlList.get(0).fArrtimePlanString);
//		    		bundle1.putString("FlightDeptime", xmlList.get(0).fDeptimeString);
//		    		bundle1.putString("FlightArrtime", xmlList.get(0).fArrtimeString);
//		    		bundle1.putString("FlightState", xmlList.get(0).fStateString);
//		    		bundle1.putString("FlightTerminal", xmlList.get(0).fTerminalString);
//		    		//bundle1.putStringArray("list", xmlList.toArray(new String[0]));
//		    		intent.putExtras(bundle1);
//		    		startActivityForResult(intent, RequestCode.TO_SELECT_DONGXIANG);

		    		//startActivity(intent);
		    		
		    	}
				
			}

	    };
	    class HttpPostTask extends AsyncTask<Void, Void, String>{

	    	
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				progressDialog1 = ProgressDialogTripg.show(HotelOrderYuDingMain.this, null, null);
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				progressDialog1.dismiss();
				
				if (result.length() != 0) {
					progressDialog1.dismiss();
					Log.e("http response is ", result);
					System.out.println("order request result -----> " + result);

			    	checkingStatusCode(result);
		
				}
				
			}

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String rev1 = null;
				JSONObject jsonObject = null;
				try {
					jsonObject = jsonObjectDict();
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String jsonString = "OrderRequest="+jsonObject.toString();
				
				Log.e("jsonString****", ""+jsonString);
				
				String url = "http://mapi.tripglobal.cn/Hotel.aspx?action=SubmitHotelOrder";
				
				
//				hotelOrderInterFaces = new HotelOrderInterFaces(HotelOrderYuDingMain.this, handler);
//				hotelOrderInterFaces.getModelFromPOST(url, jsonString, TripgMessage.HANGBAN);
				
				try {  
				    HttpClient httpclient = new DefaultHttpClient();   
				    HttpPost httppost = new HttpPost(url);   
				    //添加http头信息   
				    
				    //认证token   
				    httppost.addHeader("OrderRequest", "application/json");  
				    httppost.addHeader("Content-type", "application/x-www-form-urlencoded");  

				    httppost.setEntity(new StringEntity(jsonString));     
				    HttpResponse response;  
				    response = httpclient.execute(httppost);  
				    rev1 = EntityUtils.toString(response.getEntity());
				   
				    //obj = new JSONObject(rev);  
				    //检验状态码，如果成功接收数据
//				    int code = response.getStatusLine().getStatusCode();
				    

				    } catch (ClientProtocolException e) {   
				    	
				    } catch (IOException e) {
				    	
				    } catch (Exception e) {   
				    	
				    }  
				
				return rev1;
			}
	    	
	    }
}

