package cn.vip.main;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import cn.internet.Exit;
import cn.internet.Tools;
import cn.tripg.R;
import cn.tripg.activity.flight.FillOrderActivity;
import cn.tripg.activity.flight.ResultCode;
import cn.tripg.interfaces.TripgMessage;
import cn.tripg.interfaces.impl.VipGeRenHttpIntfaces;
import cn.tripg.interfaces.impl.XiuHttpGeturlInterfaces;
import cn.tripg.widgit.ProgressDialogTripg;

public class VipGeRenmain extends Activity {

	public HashMap<String, Object> hashMap;
	public VipGeRenHttpIntfaces vipGeRenHttpIntfaces;
	public XiuHttpGeturlInterfaces xiuHttpGeturlInterfaces;
	private final int UPDATE_LIST_VIEW = 1;
	private final int UPDATE_LIST_VIEWTH = 3;
	public String resultString;
	private List<HashMap<String, Object>> listData;
	public TextView textView;
	public TextView textView2;
	public TextView textView3;
	private TextView creadit;

	public EditText nameEditText;
	public TextView baEditText;
	public String nameString;
	public String birthdayString;
	public String typeDayString;
	
	private Context context;
	public int year;
	public int month;
	public int day;
	public DatePicker dp;
	public EditText et;
	public AlertDialog datepick;
	public LayoutParams params;
	
	private String cUrl = "http://www.tripg.cn/phone_api/shop_integral.php?username=";
	private ProgressDialog progressDialog;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vipgeren_main);
		Exit.getInstance().addActivity(this);
		listData = new ArrayList<HashMap<String, Object>>();

		new AsyncTask<Void, Void, String>() {

			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				progressDialog = ProgressDialogTripg.show(VipGeRenmain.this, null, null);
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				progressDialog.dismiss();
				Message text = handler.obtainMessage();
				text.what = 101;
				text.obj = result;
				handler.sendMessage(text);
			}

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String msg = "";
				String url = cUrl + new Tools().getUserName(getApplicationContext());
				String data = new Tools().getURL(url);
				System.out.println(data);
				
				try {
					JSONObject job = new JSONObject(data);
					String code = job.getString("Code");
					msg = job.getString("Message");
					if(code.equals("1")){
						String credit = job.getString("Result");
						Message text = handler.obtainMessage();
						text.what = 102;
						text.obj = credit;
						handler.sendMessage(text);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return msg;
			}
			
		}.execute();
		
		
		ImageView backImageView = (ImageView) findViewById(R.id.title_back);
		backImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = getIntent();
				setResult(ResultCode.SUCCESS, intent);
				finish();
			}
		});

		textView = (TextView) findViewById(R.id.textViewvipgeren2);
		textView2 = (TextView) findViewById(R.id.textViewvipgeren6);
		textView3 = (TextView) findViewById(R.id.textViewvipgeren8);
		creadit = (TextView)findViewById(R.id.textViewvipgeren4);
		
		ImageView xgimageView = (ImageView) findViewById(R.id.imageViewgeren1);
		xgimageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.e("修改被点击", "xgimageView");
				// 此处直接new一个Dialog对象出来，在实例化的时候传入主题
				final Dialog dialog = new Dialog(VipGeRenmain.this,
						R.style.MyDialog);
				// 设置它的ContentView
				dialog.setContentView(R.layout.dialog);
				 typeDayString = "1";

				nameEditText = (EditText) dialog
						.findViewById(R.id.editTextdialog1);
				baEditText = (TextView) dialog
						.findViewById(R.id.editTextdialog2);
				baEditText.setText(textView3.getText());
				

				et = new EditText(VipGeRenmain.this);
				params = new LayoutParams(0, 0);
				dp = new DatePicker(VipGeRenmain.this);
				
				baEditText.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						System.out.println("baEditText ---> " + baEditText.getText().toString());
						String spStr[] = baEditText.getText().toString()
								.split("-");

						String tiemStringmon = (String) spStr[1];// mon
						String tiemStringday = (String) spStr[2];// day
						String tiemStringyear = (String) spStr[0];// year
						Log.e("tiemString33333------", "" + tiemStringmon
								+ "---" + tiemStringday + "---"
								+ tiemStringyear);

						double i = Double.valueOf(tiemStringyear).doubleValue();
						int year = (int) i;
						double m = Double.valueOf(tiemStringmon).doubleValue();
						int month = (int) m;
						double d = Double.valueOf(tiemStringday).doubleValue();
						int day = (int) d;
						
						dp.init(year, month, day, new OnDateChangedListener());
		
						datepick = new AlertDialog.Builder(VipGeRenmain.this)
						.setView(dp)
						.setPositiveButton("确定", new PositiveButtonOnClickListener())
						.show();
						datepick.addContentView(et, params);
						
					}
				});
				
//				baEditText.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View arg0) {
//						final LinearLayout linearLayout1 = (LinearLayout) dialog
//								.findViewById(R.id.linerar);
//						final LinearLayout linearLayout = (LinearLayout) dialog
//								.findViewById(R.id.linerar1);
//						linearLayout.setVisibility(View.VISIBLE);
//						linearLayout1.setVisibility(View.GONE);
//						final DatePicker datePicker = (DatePicker) dialog
//								.findViewById(R.id.datePicker);
//						// datePicker.setVisibility(View.VISIBLE);
//						System.out.println("baEditText ---> " + baEditText.getText().toString());
//						String spStr[] = baEditText.getText().toString()
//								.split("-");
////						String tiemString1 = (String) spStr[0];
////						String tiemString2 = (String) spStr[1];
////						Log.e("tiemString------", "" + tiemString1
////								+ tiemString2);
////						String spStr2[] = tiemString1.split("/");
//						String tiemStringmon = (String) spStr[1];// mon
//						String tiemStringday = (String) spStr[2];// day
//						String tiemStringyear = (String) spStr[0];// year
//						Log.e("tiemString33333------", "" + tiemStringmon
//								+ "---" + tiemStringday + "---"
//								+ tiemStringyear);
//
//						double i = Double.valueOf(tiemStringyear).doubleValue();
//						int year = (int) i;
//						double m = Double.valueOf(tiemStringmon).doubleValue();
//						int month = (int) m;
//						double d = Double.valueOf(tiemStringday).doubleValue();
//						int day = (int) d;
//
//						// Calendar calendar = Calendar.getInstance();
//						// int year = calendar.get(Calendar.YEAR);
//						// int month = calendar.get(Calendar.MONDAY);
//						// int day = calendar.get(Calendar.DAY_OF_MONTH);
//						Log.e("datepicker", "" + year + month + day);
//						//datePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
//						datePicker.init(year, month, day,
//								new OnDateChangedListener() {
//
//									public void onDateChanged(DatePicker view,
//											int year, int monthOfYear,
//											int dayOfMonth) {
//										
//										
//										Calendar c = Calendar.getInstance();
//										int year1 = c.get(Calendar.YEAR);
//										int month1 = c.get(Calendar.MONTH) + 1;
//										int day1 = c.get(Calendar.DAY_OF_MONTH);
//										Log.e("当前时间", year1+"-"+month1+"-"+day1);
//										
//										String liveTimeString = year1+"-"+month1+"-"+day1;
//										
//										String leaveTimeString = year+"-"+(monthOfYear + 1)+"-"+dayOfMonth;
//										
//										SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//										   Date date_start = null;
//										   Date date_end = null;
//										try {
//										date_start = sdf.parse(liveTimeString);
//										date_end = sdf.parse(leaveTimeString);
//										} catch (java.text.ParseException e) {
//										
//										e.printStackTrace();
//										}
//										
//										int aday =getGapCount(date_start,date_end);
//										Log.e("时间差计算结果 ----- ", ""+aday);
//										if (aday <= 0) {
//											baEditText.setText(year + "-"
//													+ (monthOfYear + 1) + "-"
//													+ dayOfMonth);
//											// datePicker.setVisibility(View.GONE);
//											typeDayString = "1";
//											Log.e("判断日期结果_--------", "111111111111");
//										}else {
//											typeDayString = "0";
//											Log.e("typeDayString-----", typeDayString);
//										}
//	
//									}
//								});//
//						
//						final ImageView timeImageView = (ImageView) dialog
//								.findViewById(R.id.imageViewdialog21);
//						// timeImageView.setVisibility(View.VISIBLE);
//						timeImageView.setOnClickListener(new OnClickListener() {
//
//							@Override
//							public void onClick(View arg0) {
//								if (typeDayString.equals("1")) {
//									linearLayout.setVisibility(View.GONE);
//									linearLayout1.setVisibility(View.VISIBLE);
//									// datePicker.setVisibility(View.GONE);
//									// timeImageView.setVisibility(View.GONE);
//								}else {
//									Toast.makeText(VipGeRenmain.this,
//											"出生日期应小于当前日期!", Toast.LENGTH_SHORT).show();
//								}
//								
//							}
//						});
//
//					}
//				});

				ImageView imageView = (ImageView) dialog
						.findViewById(R.id.imageViewdialog1);
				imageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						nameString = nameEditText.getText().toString();
						birthdayString = baEditText.getText().toString();
						Log.e("获取到人名和生日", "" + nameString + "------------"
								+ birthdayString);
						dialog.dismiss();
						try {
							httpxiugaiInterfaces();
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
//						if (typeDayString.equals("1")) {
//							dialog.dismiss();
//							try {
//								httpxiugaiInterfaces();
//							} catch (UnsupportedEncodingException e) {
//								e.printStackTrace();
//							}
//						}else {
//							Toast.makeText(VipGeRenmain.this,
//									"选择正确日期2!", Toast.LENGTH_SHORT).show();
//						}
					}
				});

				dialog.show();

			}
		});

		getLoginInfo();
		httpziliaointerfaces();

	}

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
  
        return (int) ((fromCalendar.getTime().getTime() - toCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
}
	
	private void getLoginInfo() {
		SharedPreferences sharedPre = getSharedPreferences("config",
				MODE_PRIVATE);

		resultString = sharedPre.getString("Result", "null");

	}

	public void httpxiugaiInterfaces() throws UnsupportedEncodingException {
		nameString = URLEncoder.encode(nameString, "UTF-8");
		String urlString = "http://mapi.tripglobal.cn/MemApi.aspx?action=ModifyMember&memberId="
				+ resultString
				+ "&birthday="
				+ birthdayString
				+ "&cnName="
				+ nameString + "&token=IEEW9lg9hHa1qMC2LrAgHuluilAAX_q0";
		Log.e("修改功能 接口调用信息 urlString", "" + urlString);

		xiuHttpGeturlInterfaces = new XiuHttpGeturlInterfaces(this, handler);
		xiuHttpGeturlInterfaces.getModelFromGET(urlString,
				TripgMessage.HANGBANA, "0");

	}

	public void httpziliaointerfaces() {
		// 修改接口
		// http://mapi.tripglobal.cn/MemApi.aspx?action=ModifyMember&memberId=60000028&birthday=1992-02-09&cnName=%E4%B8%BD%E6%B5%8B%E8%AF%95&token=IEEW9lg9hHa1qMC2LrAgHuluilAAX_q0
		String urlString = "http://mapi.tripglobal.cn/MemApi.aspx?action=GetMember&memberId="
				+ resultString + "&token=IEEW9lg9hHa1qMC2LrAgHuluilAAX_q0";
		Log.e("订单详情页 接口调用信息 urlString", "" + urlString);
		vipGeRenHttpIntfaces = new VipGeRenHttpIntfaces(this, handler);
		vipGeRenHttpIntfaces.getModelFromGET(urlString, TripgMessage.HANGBAN,
				"0");

	}

	private void sendHandlerMessage(int what, Object obj) {

		Message msg = new Message();
		msg.what = what;
		handler.sendMessage(msg);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TripgMessage.HANGBAN:
				handMessageDefault(vipGeRenHttpIntfaces, VipGeRenmain.this, msg);
				break;
			case UPDATE_LIST_VIEW:
				Log.e("订单详情页 接口调用解析之后返回调用 函数", "****");
				hashMap = listData.get(0);				
				textView.setText((String) hashMap.get("UserName"));
				if(hashMap.get("CnName").toString().equals("null")){
					textView2.setText(" ");
				}else{
					textView2.setText(hashMap.get("CnName").toString());
				}
				if(!(hashMap.get("Birthday").toString().equals("null"))){
					String string = (String) hashMap.get("RegisterTime");
					System.out.println("string ---> " + string);
					String s[] = string.split(" ");
					System.out.println("s[0]  ---> " + s[0]);
					String s1[] = s[0].split("/");
					textView3.setText(s1[2]+"-"+s1[0]+"-"+s1[1]);
				}else{
//					String bString = hashMap.get("Birthday").toString();
//					String s[] = bString.split(" ");
//					System.out.println("s[0]  ---> " + s[0]);
//					String s1[] = s[0].split("/");
					textView3.setText(new Tools().Today());
				}
				
				break;
			case TripgMessage.HANGBANA:
				handMessageDefaultXiu(xiuHttpGeturlInterfaces,
						VipGeRenmain.this, msg);
				break;
			case UPDATE_LIST_VIEWTH:
				break;
				
			case 101:
				String text = (String) msg.obj;
				Toast.makeText(VipGeRenmain.this, text, Toast.LENGTH_LONG).show();
				break;
			case 102:
				String t = (String) msg.obj;
				creadit.setText(t);
				break;
			default:
				break;
			}

		}

		private void handMessageDefaultXiu(
				XiuHttpGeturlInterfaces xiuHttpGeturlInterfaces,
				VipGeRenmain vipGeRenmain, Message msg) {
			if (xiuHttpGeturlInterfaces == null)
				return;
			if (xiuHttpGeturlInterfaces.progressDialog != null)
				xiuHttpGeturlInterfaces.progressDialog.dismiss();
			if (msg.obj == null) {
				Toast.makeText(vipGeRenmain, "网络链接超时", Toast.LENGTH_SHORT)
						.show();
				vipGeRenHttpIntfaces.progressDialog.dismiss();
			} else {

				String objString = (String) msg.obj;

				try {
					JSONObject jsonObject = new JSONObject(objString);

					if (jsonObject.get("Code").equals("1")) {
						httpziliaointerfaces();
					}

				} catch (JSONException e) {

					e.printStackTrace();
				}

			}

		}

		@SuppressWarnings("unchecked")
		private void handMessageDefault(
				VipGeRenHttpIntfaces vipGeRenHttpIntfaces,
				VipGeRenmain vipGeRenmain, Message msg) {

			if (vipGeRenHttpIntfaces == null)
				return;
			if (vipGeRenHttpIntfaces.progressDialog != null)
				vipGeRenHttpIntfaces.progressDialog.dismiss();
			if (msg.obj == null) {
				Toast.makeText(vipGeRenmain, "网络链接超时", Toast.LENGTH_SHORT)
						.show();
				vipGeRenHttpIntfaces.progressDialog.dismiss();
			} else {
				listData = (List<HashMap<String, Object>>) msg.obj;

				// 通知刷新界面
				sendHandlerMessage(UPDATE_LIST_VIEW, null);

			}

		}

	};

	public class PositiveButtonOnClickListener implements android.content.DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub	
       
			dp.clearFocus();
			et.requestFocus();
			Field field = null;
			try {
				field = dialog.getClass().getSuperclass()
						.getDeclaredField("mShowing");
				field.setAccessible(true);
				// 设置mShowing值，欺骗android系统
				field.set(dialog, false);
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			
			year = dp.getYear();
			month = dp.getMonth()+1;
			day = dp.getDayOfMonth();
			
			
			
			Calendar c = Calendar.getInstance();
			int year1 = c.get(Calendar.YEAR);
			int month1 = c.get(Calendar.MONTH) + 1;
			int day1 = c.get(Calendar.DAY_OF_MONTH);
			Log.e("当前时间", year1+"-"+month1+"-"+day1);
			
			String currentDate = year1+"-"+month1+"-"+day1;
			
			String chosenDate = year+"-"+month+"-"+day;
			System.out.println("chosenDate ----> " + chosenDate);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			   	Date date_start = null;
			   	Date date_end = null;
			try {
				date_start = sdf.parse(currentDate);
				date_end = sdf.parse(chosenDate);
				System.out.println("date_start ----> " + date_start);
				System.out.println("date_end ----> " + date_end);
			} catch (java.text.ParseException e) {
			
				e.printStackTrace();
			}
			int aday =getGapCount(date_start,date_end);
			Log.e("时间差计算结果 ----- ", ""+aday);
			if (aday >= 0) {
				baEditText.setText(year+"-"+month+"-"+day);
				textView3.setText(year+"-"+month+"-"+day);
				try {
					field.set(dialog, true);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				et = new EditText(VipGeRenmain.this);
				params = new LayoutParams(0, 0);
				dp = new DatePicker(VipGeRenmain.this);	
				datepick.dismiss();

			}else {
				Toast.makeText(VipGeRenmain.this, "出生日期应小于当前日期", 1000).show();
				
			}
	
		}


		
	}
	
	public class OnDateChangedListener implements android.widget.DatePicker.OnDateChangedListener{

		@Override
		public void onDateChanged(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub	
			System.out.println("OnDateChangedListener ---> " + year + " " + (monthOfYear+1) + " " + dayOfMonth);
		}
		
	}
}
