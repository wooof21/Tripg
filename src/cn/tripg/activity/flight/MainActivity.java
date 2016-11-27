package cn.tripg.activity.flight;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tools.des.Api;
import tools.json.JsonUtils;
import model.user.LoginResponse;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.NetworkInfo.State;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView.ScaleType;
import cn.dongtai.main.DongTaiMain;
import cn.internet.Exit;
import cn.internet.InternetService;
import cn.internet.Tools;
import cn.lvyou.main.LvYouMain;
import cn.qianzheng.main.QianZhengMain;
import cn.tripg.R;
import cn.tripg.activity.hotel.HotelMainActivity;
import cn.tripg.activity.login.LoginActivity;
import cn.tripg.interfaces.TripgMessage;
import cn.tripg.interfaces.impl.NumberHttpmain;
import cn.tripg.isv.MyImgScroll;
import cn.tripg.widgit.ProgressDialogTripg;
import cn.vip.main.VipMainConterllor;
import cn.vip.main.VipOrdermain;
import cn.webview.main.LvYouWebviewmain;
import cn.webview.main.QianZhengWebviewmain;
import cn.webview.main.TrainWebViewmain;
import cn.webview.main.YouLunWebviewmain;
import cn.youlun.main.YouLunMain;

import com.example.yidaocardemo.CarActivity;
import com.example.yidaocardemo.CarNewMian;

public class MainActivity extends Activity implements OnClickListener,
		OnPageChangeListener {

	public NumberHttpmain numberHttpmain;
	public MainActivity self;

	// 定义ViewPager对象
	private ViewPager vp;

	// 定义ViewPager适配器
	private ViewPagerAdapter vpAdapter;

	// 定义一个ArrayList来存放View
	private ArrayList<View> views;//

	// 引导图片资源
	// private static final int[] pics = { R.drawable.guide1, R.drawable.guide2,
	// R.drawable.guide3, R.drawable.guide4, R.drawable.guide5 };

	// // 底部小点的图片
	// private ImageView[] points;
	//
	// // 记录当前选中位置
	// private int currentIndex;
	//
	// private LinearLayout linearLayout;

	private LinearLayout oval; 
	private ImageView imageView;
	private MyImgScroll isv;
	private ArrayList<View> adList;
	private ProgressDialog progressDialog;
	private ArrayList<String> urlList;
	private ArrayList<String> webList;
	private Handler rhandler = new Handler();
	private ExecutorService executorService = Executors.newFixedThreadPool(5);
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		System.out.println("---------onStart---------");//
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		System.out.println("---------onPause---------");
		try{
			
			unregisterReceiver(is);
		}catch(IllegalArgumentException e){
			Log.e("IllegalArgumentException", e.toString());
			System.out.println("---------receiver on registered---------");
		}
			
		
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		isv.stopTimer();
		System.out.println("---------onStop---------");

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		
		System.out.println("---------onRestart---------");

	}

	@Override
	protected void onResume() {//
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("---------onResume---------");//

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("---------onDestroy---------");
		//unregisterReceiver(is);
	}

	// 检查更新
	private UpdateManager mUpdateManager;//

	private String username;
	private String password;
	private LoginResponse lr;
	private SharedPreferences sharedPre;
	private Editor editor;
	private Context context;
	private boolean isExit = false;
	public boolean wifi;
	public boolean internet;
	private LinearLayout rl;
	private Tools tools;
	private InternetService is;

	/****************************************************************************/
	// D 24
	private void getLoginInfo() {
		SharedPreferences sharedPre = getSharedPreferences("config",
				MODE_PRIVATE);
		username = sharedPre.getString("username", "null");
		password = sharedPre.getString("password", "null");
		Log.e("登录接口保存--", username + "---" + password);

	}

	private void clearLoginInfo() {

		if (editor != null) {
			editor.clear();
			editor.commit();
		}
	}

	public LoginResponse parseJsonData(String jsonStr) {
		if (jsonStr == null) {
			// Toast.makeText(this, "获取数据失败", Toast.LENGTH_LONG).show();
			// finish();//???
		}
		try {
			if (jsonStr.equals("error")) {
				Toast.makeText(this, "网络异常", Toast.LENGTH_LONG).show();
				// finish();//???
			} else {
				 try {
		            	lr = new LoginResponse();
						JSONObject jsonObject = new JSONObject(jsonStr);
						lr.Code = (String)jsonObject.getString("Code").toString();
		            	lr.Message = (String)jsonObject.getString("Message").toString();
						
		            	//JSONObject resJsonObject = jsonObject.getJSONObject("Result");
//						lr.compayidString = (String)resJsonObject.getString("CompanyId").toString();
//						lr.countryString = (String)resJsonObject.getString("Country").toString();
						//Log.e("lr.countryString", ""+lr.countryString);
						
					} catch (Exception e) {
						// TODO: handle exception
						Log.e("Exception----", ""+e);
					}

		            if ("0".equals(lr.getCode())) {
		               // Toast.makeText(this, "登录失败", Toast.LENGTH_LONG).show();
		                //finish();//???
		            }
			}
		} catch (Exception e) {
			// Toast.makeText(this, "登录失败", Toast.LENGTH_LONG).show();
			// finish();//???
		}
		return lr;
	}

	private void InitViewPager() {

		adList = new ArrayList<View>();
		webList = new ArrayList<String>();
		new AsyncTask<Void, Void, ArrayList<View>>() {

			@Override
			protected void onPostExecute(ArrayList<View> result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				System.out.println("adList size ---> " + adList.size());
				progressDialog.dismiss();

			}

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				urlList = new ArrayList<String>();
				progressDialog = ProgressDialogTripg.show(MainActivity.this,
						null, null);
			}

			@Override
			protected ArrayList<View> doInBackground(Void... arg0) {
				// TODO Auto-generated method stub

				String url = "http://www.tripg.cn/phone_api/index_img_carousel.php?project_id=12";
				String data = tools.getURL(url);
				System.out.println("data ---> " + data);
				JSONObject job;
				try {
					job = new JSONObject(data);
					String code = job.getString("Code");
					if (code.equals("1")) {
						JSONArray jArray = job.getJSONArray("Result");
						for (int i = 0, len = jArray.length(); i < len; i++) {
							JSONObject job1 = jArray.optJSONObject(i);
							String imgUrl = job1.getString("imgRoute");
							urlList.add(imgUrl);
							webList.add(job1.getString("url"));

						}
					}
					for (final String urlStr : urlList) {
						executorService.submit(new Runnable() {

							@Override
							public void run() {
								System.out.println("urlStr --> " + urlStr);
								// TODO
								// Auto-generated
								// method stub

								URL url;
								try {
									url = new URL(urlStr);
									final Drawable drawable = Drawable
											.createFromStream(url.openStream(),
													"src");

									rhandler.post(new Runnable() {

										@Override
										public void run() {
											imageView = new ImageView(
													MainActivity.this);
											// TODO
											// Auto-generated
											// method
											// stub
											imageView
													.setScaleType(ScaleType.CENTER_CROP);
											imageView
													.setImageDrawable(drawable);
											imageView
													.setLayoutParams(new LinearLayout.LayoutParams(
															LayoutParams.FILL_PARENT,
															LayoutParams.FILL_PARENT));
											imageView.setOnClickListener(new OnClickListener() {
												
												@Override
												public void onClick(View v) {
													// TODO Auto-generated method stub
													Log.e("index", ""+isv.getCurIndex());
													Log.e("web url", webList.get(isv.getCurIndex()));
													Log.e("url list", urlList.get(isv.getCurIndex()));
													Intent intent = new Intent(MainActivity.this, LvYouWebviewmain.class);
													intent.putExtra("url", webList.get(isv.getCurIndex()));
													startActivity(intent);
												}
											});
											adList.add(imageView);
											System.out
													.println("adList ------> "
															+ adList.size());
											if (adList.size() == urlList.size()) {
												System.out.println("2");
												isv.start(
														MainActivity.this,
														adList,
														3000,
														oval,
														R.layout.ad_bottom_item,
														R.id.ad_item_v,
														R.drawable.dot_focused,
														R.drawable.dot_normal);
											}
										}
									});
								} catch (MalformedURLException e1) {
									// TODO
									// Auto-generated
									// catch block
									e1.printStackTrace();
								} catch (IOException e) {
									// TODO
									// Auto-generated
									// catch block
									e.printStackTrace();
								}

							}

						});

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return adList;
			}

		}.execute();

	}
	
	
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

		return data;//

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
		setContentView(R.layout.activity_main);
		Exit.getInstance().addActivity(this);
		System.out.println("---------onCreate---------");
		
		self = MainActivity.this;
		//rl = (LinearLayout) findViewById(R.id.main_relative);
		vp = (ViewPager) findViewById(R.id.viewpager);
		sharedPre = MainActivity.this.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		editor = sharedPre.edit();
		tools = new Tools();
		oval = (LinearLayout)findViewById(R.id.vb);
		isv = (MyImgScroll) findViewById(R.id.isv);
		// if(!isUserLogin()){
		// Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		// startActivity(intent);
		// }
		/*********************************************************************/
		// D 24
		if (isFristRun()) {
			SharedPreferences sharedPre = MainActivity.this
					.getSharedPreferences("frist", Context.MODE_PRIVATE);
			Editor editor = sharedPre.edit();
			editor.putString("frist", "0");
			editor.commit();
			Log.e("指导页启动----", "");
			// initView();
			// initData();
			/**********************************************************************************/

			vp.setOnPageChangeListener(new MyOnPageChangeListener());

			// 将要分页显示的View装入数组中
			LayoutInflater mLi = LayoutInflater.from(this);
			View view1 = mLi.inflate(R.layout.view1, null);
			View view2 = mLi.inflate(R.layout.view2, null);
			View view3 = mLi.inflate(R.layout.view3, null);
			View view4 = mLi.inflate(R.layout.view4, null);
			View view5 = mLi.inflate(R.layout.view5, null);

			// 每个页面的view数据
			final ArrayList<View> views = new ArrayList<View>();
			views.add(view1);
			views.add(view2);
			views.add(view3);
			views.add(view4);
			views.add(view5);

			// 填充ViewPager的数据适配器
			PagerAdapter pagerAdapter = new PagerAdapter() {

				@Override
				public boolean isViewFromObject(View arg0, Object arg1) {
					return arg0 == arg1;
				}

				@Override
				public int getCount() {
					return views.size();
				}

				@Override
				public void destroyItem(View container, int position,
						Object object) {
					((ViewPager) container).removeView(views.get(position));
				}

				@Override
				public Object instantiateItem(View container, int position) {
					((ViewPager) container).addView(views.get(position));
					return views.get(position);
				}

			};
			vp.setAdapter(pagerAdapter);

			
			
			
			/**********************************************************************************/
		} else {
			vp.setVisibility(View.GONE);
			//rl.setVisibility(View.VISIBLE);

			System.out.println("---------onCreate2---------");
			IntentFilter filter = new IntentFilter(
					ConnectivityManager.CONNECTIVITY_ACTION);
			is = new InternetService(rHandler);
			this.registerReceiver(is, filter);
			
			

		}
		
		// getLoginInfo();
		// Log.e("username", username);
		// Log.e("password", password);
		// String tokenString = "6QIp1iWQ3LePoFCykN6h_RgyOA6nL3-U";
		// final String loginURL =
		// "http://mapi.tripglobal.cn/MemApi.aspx?action=Login"
		// + "&username=" + username
		// + "&password=" + password
		// + "&token="+ tokenString;
		// Log.e("", loginURL);
		// String result = getURL(loginURL);
		// System.out.println("result ---> " + result);
		// lr = parseJsonData(result);
		// Log.e("result", ""+lr);
		// Log.e("code", lr.getCode());
		// Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		// if(lr == null){
		// Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
		// clearLoginInfo();
		// getLoginInfo();
		// Log.e("2.username", username);
		// Log.e("2.password", password);
		// //startActivity(intent);
		// }
		// else{
		// if("1".equals(lr.getCode())){
		// Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
		// getLoginInfo();
		// Log.e("4.username", username);
		// Log.e("4.password", password);
		// //saveLoginInfo(LoginActivity.this, lr, password);
		// //finish();
		// }else{
		// Toast.makeText(MainActivity.this, lr.getMessage(),
		// Toast.LENGTH_SHORT).show();
		// clearLoginInfo();
		// getLoginInfo();
		// Log.e("3.username", username);
		// Log.e("3.password", password);
		// //startActivity(intent);
		// }
		// }

		//
		/*********************************************************************/
		final LinearLayout frl = (LinearLayout) findViewById(R.id.flight_rl);
		frl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(getInternet()){
					//执行相关操作
//					Toast.makeText(getApplicationContext(),
//							"亲，网络连上了！", Toast.LENGTH_LONG)
//							.show();
					tools.scaleInAnimation(frl);
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Intent ticket = new Intent(MainActivity.this,
									FlightActivity.class);
							
							startActivity(ticket);
						}
					}, 150);
					
				}else{
					tools.upDown(frl);
					Toast.makeText(getApplicationContext(),
							"亲，网络连了么？", Toast.LENGTH_LONG)
							.show();
				}

			}
		});

		final LinearLayout hrl = (LinearLayout) findViewById(R.id.hotel_rl);
		hrl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(getInternet()){
					//执行相关操作
//					Toast.makeText(getApplicationContext(),
//							"亲，网络连上了！", Toast.LENGTH_LONG)
//							.show();
					tools.scaleInAnimation(hrl);
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Intent ticket = new Intent(MainActivity.this,
									HotelMainActivity.class);
							startActivity(ticket);
						}
					}, 150);
					
				}else{
					tools.upDown(hrl);
					Toast.makeText(getApplicationContext(),
							"亲，网络连了么？", Toast.LENGTH_LONG)
							.show();
				}

			}
		});

		final LinearLayout crl = (LinearLayout) findViewById(R.id.car_rl);
		crl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(getInternet()){
					//执行相关操作
//					Toast.makeText(getApplicationContext(),
//							"亲，网络连上了！", Toast.LENGTH_LONG)
//							.show();
					tools.scaleInAnimation(crl);
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Intent intent2 = new Intent(MainActivity.this,
									CarActivity.class);
							startActivity(intent2);
						}
					}, 150);
					
				}else{
					tools.upDown(crl);
					Toast.makeText(getApplicationContext(),
							"亲，网络连了么？", Toast.LENGTH_LONG)
							.show();
				}

			}
		});

		final LinearLayout drl = (LinearLayout) findViewById(R.id.dt_rl);
		drl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(getInternet()){
					//执行相关操作
//					Toast.makeText(getApplicationContext(),
//							"亲，网络连上了！", Toast.LENGTH_LONG)
//							.show();
					tools.scaleInAnimation(drl);
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Intent intent3 = new Intent(MainActivity.this,
									DongTaiMain.class);
							startActivity(intent3);
						}
					}, 150);
					
				}else{
					tools.upDown(drl);
					Toast.makeText(getApplicationContext(),
							"亲，网络连了么？", Toast.LENGTH_LONG)
							.show();
				}

			}
		});


		final LinearLayout trarl = (LinearLayout) findViewById(R.id.trains_rl);
		trarl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(getInternet()){
					//执行相关操作
//					Toast.makeText(getApplicationContext(),
//							"亲，网络连上了！", Toast.LENGTH_LONG)
//							.show();
					tools.scaleInAnimation(trarl);
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Intent intent = new Intent(MainActivity.this,
									TrainWebViewmain.class);
							startActivity(intent);
						}
					}, 150);
					
					
				}else{
					tools.upDown(trarl);
					Toast.makeText(getApplicationContext(),
							"亲，网络连了么？", Toast.LENGTH_LONG)
							.show();
				}

			}
		});

		final LinearLayout vrl = (LinearLayout) findViewById(R.id.visa_rl);
		vrl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(getInternet()){
					//执行相关操作
//					Toast.makeText(getApplicationContext(),
//							"亲，网络连上了！", Toast.LENGTH_LONG)
//							.show();
					tools.scaleInAnimation(vrl);
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Intent intent = new Intent(MainActivity.this,
									QianZhengMain.class);
							startActivity(intent);
						}
					}, 150);
					
					
				}else{
					tools.upDown(vrl);
					Toast.makeText(getApplicationContext(),
							"亲，网络连了么？", Toast.LENGTH_LONG)
							.show();
				}

			}
		});

		final LinearLayout trl = (LinearLayout) findViewById(R.id.travel_rl);
		trl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(getInternet()){
					//执行相关操作
//					Toast.makeText(getApplicationContext(),
//							"亲，网络连上了！", Toast.LENGTH_LONG)
//							.show();
					tools.scaleInAnimation(trl);
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Intent intent = new Intent(MainActivity.this,
									LvYouMain.class);
							startActivity(intent);
						}
					}, 150);
					
					
				}else{
					tools.upDown(trl);
					Toast.makeText(getApplicationContext(),
							"亲，网络连了么？", Toast.LENGTH_LONG)
							.show();
				}

			}
		});

		final LinearLayout srl = (LinearLayout) findViewById(R.id.ship_rl);
		srl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(getInternet()){
					//执行相关操作
//					Toast.makeText(getApplicationContext(),
//							"亲，网络连上了！", Toast.LENGTH_LONG)
//							.show();
					tools.scaleInAnimation(srl);
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Intent intent = new Intent(MainActivity.this,
									YouLunMain.class);
							startActivity(intent);
						}
					}, 150);
					
					
				}else{
					tools.upDown(srl);
					Toast.makeText(getApplicationContext(),
							"亲，网络连了么？", Toast.LENGTH_LONG)
							.show();
				}

			}
		});

		ImageView order = (ImageView)findViewById(R.id.main_my_order);
		order.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(getInternet()){
					//执行相关操作
//					Toast.makeText(getApplicationContext(),
//							"亲，网络连上了！", Toast.LENGTH_LONG)
//							.show();
					if (isUserLogin()) {
						Intent intent4 = new Intent(MainActivity.this,
								VipOrdermain.class);
						startActivity(intent4);
						//finish();
					} else {
						Intent intent = new Intent(MainActivity.this,
								LoginActivity.class);
						startActivity(intent);
						//finish();
					}
				}else{
					Toast.makeText(getApplicationContext(),
							"亲，网络连了么？", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		
		ImageView vipiImageView = (ImageView) findViewById(R.id.main_mine);
		vipiImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(getInternet()){
					//执行相关操作
//					Toast.makeText(getApplicationContext(),
//							"亲，网络连上了！", Toast.LENGTH_LONG)
//							.show();
					if (isUserLogin()) {
						Intent intent4 = new Intent(MainActivity.this,
								VipMainConterllor.class);
						startActivity(intent4);
						//finish();
					} else {
						Intent intent = new Intent(MainActivity.this,
								LoginActivity.class);
						startActivity(intent);
						//finish();
					}
				}else{
					Toast.makeText(getApplicationContext(),
							"亲，网络连了么？", Toast.LENGTH_LONG)
							.show();
				}

			}
		});

		ImageView imagephone = (ImageView) findViewById(R.id.main_phone);
		imagephone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent();
				intent.setAction("android.intent.action.DIAL");
				intent.setData(Uri.parse("tel:400 656 8777"));
				startActivity(intent);

			}
		});
		// if(getInternet()){
		// System.out.println("1");
		// String urlString =
		// "http://www.tripg.cn/phone_api/version_number.php?id=6";
		// Log.e("urlString---", "" + urlString);
		// numberHttpmain = new NumberHttpmain(MainActivity.this, handler);
		// numberHttpmain.getModelFromGET(urlString, TripgMessage.HANGBAN, "0");
		// }else{
		// }

	}

	// /**
	// * 初始化组件
	// */
	// private void initView() {
	// // 实例化ArrayList对象
	// views = new ArrayList<View>();
	//
	// // 实例化ViewPager
	// viewPager = (ViewPager) findViewById(R.id.viewpager);
	// // 实例化ViewPager适配器
	// vpAdapter = new ViewPagerAdapter(views);
	// }
	//
	// /**
	// * 初始化数据
	// */
	// private void initData() {
	// // 定义一个布局并设置参数
	// LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
	// LinearLayout.LayoutParams.WRAP_CONTENT,
	// LinearLayout.LayoutParams.WRAP_CONTENT);
	//
	// // 初始化引导图片列表
	// for (int i = 0; i < pics.length; i++) {
	// ImageView iv = new ImageView(this);
	// iv.setLayoutParams(mParams);
	// iv.setImageResource(pics[i]);
	// views.add(iv);
	// }
	//
	// // 设置数据
	// viewPager.setAdapter(vpAdapter);
	// // 设置监听
	// viewPager.setOnPageChangeListener(this);
	// // viewPager.setOnClickListener(this);
	// // 初始化底部小点
	// // initPoint();
	// }
	//
	// /**
	// * 初始化底部小点
	// */
	// private void initPoint() {
	// //linearLayout = (LinearLayout) findViewById(R.id.ll);
	// linearLayout.setVisibility(View.VISIBLE);
	// points = new ImageView[pics.length];
	//
	// // 循环取得小点图片
	// for (int i = 0; i < pics.length; i++) {
	// // 得到一个LinearLayout下面的每一个子元素
	// points[i] = (ImageView) linearLayout.getChildAt(i);
	// // 默认都设为灰色
	// points[i].setEnabled(true);
	// // 给每个小点设置监听
	// points[i].setOnClickListener(this);
	// // 设置位置tag，方便取出与当前位置对应
	// points[i].setTag(i);
	// }
	//
	// // 设置当面默认的位置
	// currentIndex = 0;
	// // 设置为白色，即选中状态
	// points[currentIndex].setEnabled(false);
	// }
	//
	// /**
	// * 当滑动状态改变时调用
	// */
	//
	// public void onPageScrollStateChanged(int arg0) {
	//
	// }
	//
	// /**
	// * 当当前页面被滑动时调用
	// */
	//
	//
	// public void onPageScrolled(int arg0, float arg1, int arg2) {
	//
	// }
	//
	// /**
	// * 当新的页面被选中时调用
	// */
	//
	//
	// public void onPageSelected(int position) {
	// // 设置底部小点选中状态
	// //setCurDot(position);
	// if (position == 4) {
	// Log.e("position----", "" + position);
	// //viewPager.setVisibility(View.INVISIBLE);
	//
	// // linearLayout.setVisibility(View.INVISIBLE);
	// }
	// }
	//
	// /**
	// * 通过点击事件来切换当前的页面
	// */
	//
	// public void onClick(View v) {
	// int position = (Integer) v.getTag();
	// setCurView(position);
	// // setCurDot(position);s
	//
	// }
	//
	// /**
	// * 设置当前页面的位置
	// */
	// private void setCurView(int position) {
	// if (position < 0 || position >= pics.length) {
	// return;
	// }
	// viewPager.setCurrentItem(position);
	// }

	/**
	 * 设置当前的小点的位置
	 */
	// private void setCurDot(int positon) {
	// if (positon < 0 || positon > pics.length - 1 || currentIndex == positon)
	// {
	// return;
	// }
	// //points[positon].setEnabled(false);
	// points[currentIndex].setEnabled(true);
	//
	// currentIndex = positon;
	// }

	/*******************************************************************/
	/*
	 * 按俩次back键退出程序
	 */

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!isExit) {
				isExit = true;
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				new Handler().postDelayed(new Runnable() {
					public void run() {
						isExit = false;
					}
				}, 2000);

				return false;
			}
			Exit.getInstance().exit();
		}

		return super.onKeyDown(keyCode, event);
		// if(keyCode == KeyEvent.KEYCODE_MENU){
		// super.openOptionsMenu(); // 调用这个，就可以弹出菜单
		// }

	}

	// @Override
	// public void onOptionsMenuClosed(Menu menu) {
	// //在这里做你想做的事情
	//
	// super.onOptionsMenuClosed(menu);
	// }
	// //然后是对menu菜单的配置，如下：
	//
	// @Override
	//
	// public void openOptionsMenu() {
	//
	// // TODO Auto-generated method stub
	//
	// super.openOptionsMenu();
	//
	// }
	// @Override
	//
	// public boolean onCreateOptionsMenu(Menu menu) {
	//
	// // TODO Auto-generated method stub
	//
	// super.onCreateOptionsMenu(menu);
	//
	// int group1 = 1;
	//
	// int gourp2 = 2;
	//
	// menu.add(group1, 1, 1, "item 11");
	// menu.add(gourp2, 2, 1, "item 12");
	// return true;
	//
	// }
	//
	// @Override
	//
	// public boolean onOptionsItemSelected(MenuItem item) {
	//
	// // TODO Auto-generated method stub
	//
	// switch (item.getItemId()) {
	//
	// case 1: // do something here
	//
	// Log.i("MenuTest:", "ItemSelected:1");
	//
	// break;
	//
	// case 2: // do something here
	//
	// Log.i("MenuTest:", "ItemSelected:2");
	//
	// break;
	//
	// default:
	//
	// return super.onOptionsItemSelected(item);
	//
	// }
	//
	// return true;
	//
	// }
	/*******************************************************************/

	// 下载handler函数
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TripgMessage.HANGBAN:
				handMessageDefault(numberHttpmain, MainActivity.this, msg);
				break;

			}
		}

		private void handMessageDefault(NumberHttpmain numberHttpmain,
				MainActivity mainActivity, Message msg) {//

			if (numberHttpmain == null)
				return;
			if (numberHttpmain.progressDialog != null)
				numberHttpmain.progressDialog.dismiss();
				

			if (msg.obj == null) {
				Log.e("版本号", "接口无此数据");//
			} else {
				String jString = (String) msg.obj;//
				Log.e("解析完成", jString);
				if (jString.equals("3.3.0")) {
					Log.e("3.0.0  解析完成", jString);//
				} else {
					
					mUpdateManager = new UpdateManager(self);
					mUpdateManager.checkUpdateInfo();
					

				}

			}

		}

	};

	public boolean isFristRun() {
		Log.e("进入指导页判断-------", "-------");
		SharedPreferences sharedPre = MainActivity.this.getSharedPreferences(
				"frist", Context.MODE_PRIVATE);
		String fristString = sharedPre.getString("frist", "1");
		Log.e("frist====", "" + fristString);
		if ("1".equals(fristString)) {
			return true;
		} else {
			return false;
		}

	}

	private boolean isUserLogin() {
		SharedPreferences sharedPre = MainActivity.this.getSharedPreferences(
				"config", Context.MODE_PRIVATE);

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

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	/********************************** D27 *****************************************/
	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub

		}

	}

	public void start(View v) {
		vp.setVisibility(View.GONE);
		//rl.setVisibility(View.VISIBLE);
		
		InitViewPager();
		
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		is = new InternetService(rHandler);
		this.registerReceiver(is, filter);
		
		 getLoginInfo();
		 Log.e("username", username);
		 Log.e("password", password);
		 String tokenString = "IEEW9lg9hHa1qMC2LrAgHuluilAAX/q0";
		 final String loginURL =
		 "http://mapi.tripglobal.cn/MemApi.aspx?action=LoginForMobile"
		 + "&username=" + username
		 + "&password=" + password
		 + "&token="+ tokenString;
		 Log.e("", loginURL);
		 String result = getURL(loginURL);
		 System.out.println("result ---> " + result);
		 lr = parseJsonData(result);
		 Log.e("result", ""+lr);
		 Log.e("code", lr.getCode());
		 
		 if(lr == null){
			 Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
			 clearLoginInfo();
			 getLoginInfo();
			 Log.e("2.username", username);
			 Log.e("2.password", password);
			 //startActivity(intent);
		 }else{
			 if("1".equals(lr.getCode())){
				 	Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
				 	getLoginInfo();
				 	Log.e("4.username", username);
				 	Log.e("4.password", password);
		 //saveLoginInfo(LoginActivity.this, lr, password);
		 //finish();
		 }else{
			 Toast.makeText(MainActivity.this, "未登录", Toast.LENGTH_SHORT).show();
			 clearLoginInfo();
			 getLoginInfo();
			 Log.e("3.username", username);
			 Log.e("3.password", password);
		 //startActivity(intent);
		 }
		 }

	}

	//
	private Handler rHandler = new Handler() {//

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				break;
			case 2:
				
//				new Handler().postDelayed(new Runnable() {
//					
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						unregisterReceiver(is);
//					}
//				}, 3000);
				

				String urlString = "http://www.tripg.cn/phone_api/version_number.php?id=6";
				Log.e("urlString---", "" + urlString);
				numberHttpmain = new NumberHttpmain(MainActivity.this, handler);
				numberHttpmain.getModelFromGET(urlString, TripgMessage.HANGBAN,
						"0");

				if (isFristRun()) {

				} else {
					/***********************************************************************************/
					getLoginInfo();
					Log.e("username", username);
					Log.e("password", password);
					if (username.length() == 0 || username.equals("null")) {

						// Toast.makeText(MainActivity.this, "未登录",
						// Toast.LENGTH_SHORT).show();

					} else {
						InitViewPager();
						String tokenString = "6QIp1iWQ3LePoFCykN6h_RgyOA6nL3-U";
						final String loginURL = "http://mapi.tripglobal.cn/MemApi.aspx?action=Login"
								+ "&username="
								+ username
								+ "&password="
								+ password + "&token=" + tokenString;
						Log.e("1", loginURL);
						String result = getURL(loginURL);
						System.out.println("1.result ---> " + result);
						lr = parseJsonData(result);
						
						
						if (lr == null) {
							// Toast.makeText(MainActivity.this, "登录失败",
							// Toast.LENGTH_SHORT).show();
							clearLoginInfo();
							getLoginInfo();
							Log.e("2.username", username);
							Log.e("2.password", password);
							// startActivity(intent);
						} else {
							if ("1".equals(lr.getCode())) {
								// Toast.makeText(MainActivity.this, "登录成功",
								// Toast.LENGTH_SHORT).show();
								getLoginInfo();
								Log.e("4.username", username);
								Log.e("4.password", password);
								// saveLoginInfo(LoginActivity.this, lr,
								// password);
								// finish();
							} else {
								clearLoginInfo();
								// System.out.println("123456 ----> " +
								// sharedPre.getString("Result", null));
								Toast.makeText(MainActivity.this,
										lr.getMessage(), Toast.LENGTH_SHORT)
										.show();
								// Intent intent10 = new
								// Intent(MainActivity.this,
								// LoginActivity.class);
								// startActivity(intent10);
							}
						}
					}
					/***********************************************************************************/

				}

				break;
			default:
				break;
			}
		}

	};

	// public class InternetService extends BroadcastReceiver{
	//
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// // TODO Auto-generated method stub
	//
	// boolean success = false;
	//
	// //获得网络连接服务
	// ConnectivityManager connManager = (ConnectivityManager)
	// context.getSystemService(Context.CONNECTIVITY_SERVICE);
	// // State state = connManager.getActiveNetworkInfo().getState();
	// State state = connManager.getNetworkInfo(
	// ConnectivityManager.TYPE_WIFI).getState(); // 获取网络连接状态
	// if (State.CONNECTED == state) { // 判断是否正在使用WIFI网络
	// success = true;
	// }
	//
	// state = connManager.getNetworkInfo(
	// ConnectivityManager.TYPE_MOBILE).getState(); // 获取网络连接状态
	// if (State.CONNECTED != state) { // 判断是否正在使用GPRS网络
	// success = true;
	// }
	//
	// if (!success) {
	// Message msg = new Message();
	// msg.what = 2;
	// rHandler.sendMessage(msg);
	// }else{
	// Message msg = new Message();
	// msg.what = 1;
	// rHandler.sendMessage(msg);
	// }
	//
	//
	// }
	//
	// }
}
