package cn.tripg.activity.login;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.user.LoginResponse;

import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;

import tools.des.Api;
import tools.json.JsonUtils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
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
import cn.tripg.activity.flight.MainActivity;
import cn.tripg.activity.flight.ResultCode;
import cn.tripg.widgit.ProgressDialogTripg;
import cn.vip.main.VipMainConterllor;

public class RegisterActivity extends Activity implements OnClickListener {

	private EditText username;
	private EditText password;
	private EditText repassword;
	private ImageView commit;
	private Api api;
	private LoginResponse lr;
	private SharedPreferences sharedPre;
	private Editor editor;
	private Context context;
	private ProgressDialog progressDialog;
	private Tools tools;
	private EditText code;
	private TextView resend;
	
	private Uri SMS_INBOX = Uri.parse("content://sms/");  
	private SmsObserver smsObserver;  
	private TimerTask timerTask;
	private Timer timer;
	private int count = 120;
		
	private void prepareAllView() {
		api = new Api();
		username = (EditText) findViewById(R.id.user_name);
		password = (EditText) findViewById(R.id.password);
		password.setVisibility(View.GONE);
		repassword = (EditText) findViewById(R.id.repassword);
		repassword.setVisibility(View.GONE);
		commit = (ImageView) findViewById(R.id.btn_commit);
		commit.setVisibility(View.GONE);
		code = (EditText) findViewById(R.id.register_code);
		resend = (TextView) findViewById(R.id.register_resend);
		if(resend.getText().toString().contains("获取")){
			resend.setOnClickListener(this);
		}else{
			resend.setClickable(false);
		}

		tools = new Tools();
	}

	private int userInfoCheck(String un, String pw, String rpw) {
		ArrayList<String> err = new ArrayList<String>();
		err.clear();
		if (un != null && un.length() != 11) {
			Toast.makeText(RegisterActivity.this, "用户名需为11位手机号",
					Toast.LENGTH_SHORT).show();
			err.add("用户名需为11位手机号");
		} else if (un == null || "".equals(un.trim())) {
			Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_SHORT)
					.show();
			err.add("用户名不能为空");//
		} else if (pw == null || "".equals(pw.trim())) {
			Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT)
					.show();
			err.add("密码不能为空");
		} else if (pw != null && !pw.equals(rpw.trim())) {
			Toast.makeText(RegisterActivity.this, "两次密码不一致", Toast.LENGTH_SHORT)
					.show();
			err.add("两次密码不一致");
		}else if(code.getText().length() != 4){
			Toast.makeText(RegisterActivity.this, "请填写4位验证码", Toast.LENGTH_SHORT)
			.show();
			err.add("请填写4位验证码");
		}else if(code.getText().length() == 0){
			Toast.makeText(RegisterActivity.this, "请填写验证码", Toast.LENGTH_SHORT)
			.show();
			err.add("请填写验证码");
		}
		if (err.size() != 0) {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < err.size(); i++) {
				builder.append(err.get(i));
				builder.append("\n");
			}

		}
		return err.size();
	}

	private void saveLoginInfo(Context context, LoginResponse lr, String pswd) {
		sharedPre = context
				.getSharedPreferences("config", Context.MODE_PRIVATE);
		editor = sharedPre.edit();
		editor.putString("username", (String) username.getText().toString());
		editor.putString("password", (String) password.getText().toString());
		editor.putString("compayid", lr.compayidString);
		editor.commit();
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

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String text = (String) msg.obj;
				Toast.makeText(RegisterActivity.this, text, Toast.LENGTH_LONG).show();
				break;
			case 1:
				String res = (String) msg.obj;
				code.setText(res);
				password.setVisibility(View.VISIBLE);
				repassword.setVisibility(View.VISIBLE);
				commit.setVisibility(View.VISIBLE);
				
				resend.setBackgroundResource(R.drawable.corners_bg_gray);
				startCount();
				break;
			case 2:
				resend.setText(""+ count +"s" + " " + "后可重发");
				resend.setClickable(false);
				break;
			case 3:		
				resend.setText("重新获取");
				timer.cancel();
            	resend.setBackgroundResource(R.drawable.corners_bg_sblue);
            	resend.setClickable(true);
				break;
			default:
				break;
			}
		}
		
	};
	
	private void startCount() {
		
	    timer = new Timer();
	    timerTask = new TimerTask() {
	            @Override
	            public void run() {
	                if (count > 0){
	                	Message msg = handler.obtainMessage();
	                	msg.what = 2;
	                	handler.sendMessage(msg);              	
	                }
	                else{
	                	
	                	Message msg = handler.obtainMessage();
	                	msg.what = 3;
	                	handler.sendMessage(msg);
	                }
	                count --;

	}
	        };       
	        timer.schedule(timerTask, 0, 1200);
	        
	}
	
	private void readSMS() {
		ContentResolver cr = getContentResolver();
		String[] projection = new String[] { "body" };//"_id", "address", "person",, "date", "type
		String where = "address = '10690999099999' AND date >  "
				+ (System.currentTimeMillis() - 10 * 60 * 1000);
		Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
		if (null == cur)
			return;
		if (cur.moveToNext()) {
//			String number = cur.getString(cur.getColumnIndex("address"));//手机号
//			String name = cur.getString(cur.getColumnIndex("person"));//联系人姓名列表
			String body = cur.getString(cur.getColumnIndex("body"));
			System.out.println(body);
			//这里我是要获取自己短信服务号码中的验证码~~
			Pattern pattern = Pattern.compile("[a-zA-Z0-9]{4}");
			Matcher matcher = pattern.matcher(body);
			if (matcher.find()) {
				String res = matcher.group().substring(0, 4);
				Message msg = handler.obtainMessage();
				msg.what = 1;
				msg.obj = res;
				handler.sendMessage(msg);
			}
		}
	}
		
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		password.setVisibility(View.GONE);
		repassword.setVisibility(View.GONE);
		commit.setVisibility(View.GONE);
		code.getText().clear();
		resend.setBackgroundResource(R.drawable.corners_bg_sblue);
		resend.setClickable(true);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		try {
			timer.cancel();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		Exit.getInstance().addActivity(this);
		prepareAllView();

		ImageView backImageView = (ImageView) findViewById(R.id.title_back);
		backImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = getIntent();
				setResult(ResultCode.FAILURE, intent);
				finish();
			}
		});
		
		code.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if(s.toString().length() == 4){
					password.setVisibility(View.VISIBLE);
					repassword.setVisibility(View.VISIBLE);
					commit.setVisibility(View.VISIBLE);
				}else{
					password.setVisibility(View.GONE);
					repassword.setVisibility(View.GONE);
					commit.setVisibility(View.GONE);
				}
			}
		});

		commit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(getInternet() == false){
					 Toast.makeText(RegisterActivity.this, "网络链接已断开",
					 Toast.LENGTH_LONG).show();
				}else{
					 final String username =
					 RegisterActivity.this.username.getText().toString();
					 final String password =
					 RegisterActivity.this.password.getText().toString();
					 final String repassword =
					 RegisterActivity.this.repassword.getText().toString();
					 final String vc = code.getText().toString();
					 int code = userInfoCheck(username, password, repassword);
					 if(code == 0){
						 new AsyncTask<Void, Void, String>(){ 
							@Override
							protected void onPreExecute() {
								// TODO Auto-generated method stub
								super.onPreExecute();
								progressDialog = ProgressDialog.show(RegisterActivity.this, null, null);
							}

							@Override
							protected void onPostExecute(String result) {
								// TODO Auto-generated method stub
								super.onPostExecute(result);
								progressDialog.dismiss();
								if(result.equalsIgnoreCase("success")){
									Toast.makeText(RegisterActivity.this, "登录成功!", Toast.LENGTH_LONG).show();
								}else{
									Toast.makeText(RegisterActivity.this, result, Toast.LENGTH_LONG).show();
								}
								
							}

							@Override
							protected String doInBackground(Void... params) {
								// TODO Auto-generated method stub
								String text = "";
								String url = "http://mapi.tripglobal.cn/MemApi.aspx?action=RegisterByVerifyCode&username="
											 + username 
											 + "&password="
											 + password
											 + "&VerifyCode="
											 + vc;
								Log.e("url", url);
								String data = tools.getURL(url);
								System.out.println("data ---> " + data);
								
								try {
									JSONObject job = new JSONObject(data);
									String code = job.getString("Code");
									text = job.getString("Message");
									if(code.equals("1")){
										Intent intent = getIntent();
										setResult(ResultCode.SUCCESS, intent);
										finish();
									}else{
										Intent intent = getIntent();
										setResult(ResultCode.FAILURE, intent);
										finish();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								return text;
							}
							 
						 }.execute();
					 }
				}
			}
		});
		
		// commit.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// if(getInternet() == false){
		// Toast.makeText(RegisterActivity.this, "网络链接已断开",
		// Toast.LENGTH_LONG).show();
		// }else{
		// final String username =
		// RegisterActivity.this.username.getText().toString();
		// String password =
		// RegisterActivity.this.password.getText().toString();
		// final String repassword =
		// RegisterActivity.this.repassword.getText().toString();
		// int code = userInfoCheck(username, password, repassword);
		// if(code == 0){
		// new AsyncTask<Void, Void, String>() {
		//
		//
		// @Override
		// protected void onPreExecute() {
		// // TODO Auto-generated method stub
		// super.onPreExecute();
		// progressDialog = ProgressDialogTripg.show(RegisterActivity.this,
		// null, null);
		// }
		//
		// @Override
		// protected void onPostExecute(String result) {
		// // TODO Auto-generated method stub
		// super.onPostExecute(result);
		// progressDialog.dismiss();
		// Toast.makeText(RegisterActivity.this, result,
		// Toast.LENGTH_LONG).show();
		// }
		//
		// @Override
		// protected String doInBackground(Void... params) {
		// // TODO Auto-generated method stub
		// String msg = "";
		// String registerURL =
		// "http://mapi.tripglobal.cn/MemApi.aspx?action=CreateMember"
		// + "&username=" + username
		// + "&password=" +
		// repassword+"&gender=0&token=c4B50F0QkyWVwK2raVbXrvCFgIUR4c5N29dcHZArb7k@";
		// Log.e("registerURL", registerURL);
		// String data = tools.getURL(registerURL);
		// System.out.println("data ---> " + data);
		//
		// try {
		// JSONObject job = new JSONObject(data);
		// String code = job.getString("Code");
		// msg = job.getString("Message");
		// if(code.equals("1")){
		// msg = "注册成功";
		// finish();
		// }
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// return msg;
		// }
		//
		// }.execute();
		// }
		// // if(code == 0){
		// // String registerURL =
		// "http://mapi.tripglobal.cn/MemApi.aspx?action=CreateMember"
		// // + "&username=" + username
		// // + "&password=" +
		// repassword+"&gender=0&token=c4B50F0QkyWVwK2raVbXrvCFgIUR4c5N29dcHZArb7k@";
		// // final String result = api.doGetData(registerURL);
		// // String tokenString = "6QIp1iWQ3LePoFCykN6h_RgyOA6nL3-U";
		// // final String loginURL =
		// "http://mapi.tripglobal.cn/MemApi.aspx?action=Login"
		// // + "&username=" + username
		// // + "&password=" + password
		// // + "&token="+ tokenString;
		// // Log.e("", loginURL);
		// //// final String lresult = api.doGetData(loginURL);
		// //// lr = parseJsonData(lresult);
		// //// Log.e("result", ""+lr);
		// //// Log.e("code", lr.getCode());
		// //
		// // try {
		// // JSONObject err = new JSONObject(result);
		// // String e = err.getString("Code");
		// // String mString = err.getString("Message");
		// // Log.e("Message----", ""+err);
		// // if("1".equals(e)){
		// // Toast.makeText(RegisterActivity.this, "注册成功",
		// Toast.LENGTH_SHORT).show();
		// // //saveLoginInfo(RegisterActivity.this, lr, password);
		// // if("1".equals(lr.getCode())){
		// // saveLoginInfo(RegisterActivity.this, lr, password);
		// // Intent intent = new Intent(RegisterActivity.this,
		// VipMainConterllor.class);
		// // startActivity(intent);
		// // }
		// // // sharedPre =
		// // // context.getSharedPreferences("config", Context.MODE_PRIVATE);
		// // // editor = sharedPre.edit();
		// // // editor.putString("username", username);
		// // // editor.putString("password", password);
		// // // editor.commit();
		// //
		// //
		// // //finish();
		// // }else {
		// // Toast.makeText(RegisterActivity.this, mString,
		// Toast.LENGTH_SHORT).show();
		// //
		// // }
		// // } catch (JSONException e) {
		// // Log.e("zzz", "RegisterActivity 解析json数据失败");
		// // }
		// // }
		// }
		// }
		// });

	}
	
	class SmsObserver extends ContentObserver {

		public SmsObserver(Context context, Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			//每当有新短信到来时，使用我们获取短消息的方法
			readSMS();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		password.setVisibility(View.GONE);
		repassword.setVisibility(View.GONE);
		commit.setVisibility(View.GONE);
		if (username.getText().length() == 0) {
			Toast.makeText(RegisterActivity.this, "请填写电话!",
					Toast.LENGTH_LONG).show();
		} else if (username.getText().length() != 11) {
			Toast.makeText(RegisterActivity.this, "请输入11位电话号码!",
					Toast.LENGTH_LONG).show();
		} else {
			new AsyncTask<Void, Void, String>() {

				@Override
				protected void onPreExecute() {
					// TODO Auto-generated method stub
					super.onPreExecute();
					progressDialog = ProgressDialogTripg.show(
							RegisterActivity.this, null, null);
				}

				@Override
				protected void onPostExecute(String result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					progressDialog.dismiss();
					if(result.equals("1")){
						smsObserver = new SmsObserver(RegisterActivity.this, handler);
						getContentResolver().registerContentObserver(SMS_INBOX, true,
								smsObserver);
					}
				}

				@Override
				protected String doInBackground(Void... params) {
					// TODO Auto-generated method stub
					String code = "";
					String url = "http://mapi.tripglobal.cn/MemApi.aspx?action=GetSmsVerify&TelPhone="
							+ username.getText().toString();
					Log.e("url", url);
					String data = tools.getURL(url);
					System.out.println("data ---> " + data);
					
					try {
						JSONObject job = new JSONObject(data);
						code = job.getString("Code");
						if(code.equals("1")){
							
						}else{
							Message msg = new Message();
							msg.what = 0;
							msg.obj = job.get("Message");
							handler.sendMessage(msg);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					

					return code;
				}

			}.execute();

		}
	
	}

}
