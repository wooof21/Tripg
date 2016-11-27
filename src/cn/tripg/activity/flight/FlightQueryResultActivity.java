package cn.tripg.activity.flight;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.flight.AVVo;
import model.flight.CabinVo;
import model.flight.FlightsVo;
import model.flight.LowPrice;
import model.flight.LowPrices;

import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tools.des.Api;
import tools.des.MD5;
import tools.json.JsonUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.internet.Exit;
import cn.internet.Tools;
import cn.tripg.R;
import cn.tripg.widgit.ProgressDialogTripg;

public class FlightQueryResultActivity extends Activity{
    private String requestURL = "";
    private String changedURL = "";
    private String specialPriceUrl = "";
    private String cmd;
    private String filter;
    private String depCity;
    private String arrCity;

    public String depCityStr;
    public String arrCityStr;
    private String arrTower;
    private String depTower;
    private String carrier;
    private String currentLeaveDate;
    private String currentCompanyCode = "";
    private String flightDate;
    private String flightDateLeave;
    private String flightDateBack;
    
    private String officeCode;
    private String flightTime;

    private String jsonLowPrice;
    private String type;
    private ViewHolder viewHolder;

    private Api api;
    private AVVo av;
    private LowPrices lps;
    private ArrayList<FlightsVo> list;
    private ArrayList<SpecialPriceButton> btns;
    private FlightAdapter flightAdapter;
    private ArrayList<ImageView> threeBottomBtn;
    private SpecialPriceButton currentSPBtn;
    private SortByPriceListener sortByPriceListener;
    private SortByTakeOffListener sortByTakeOffListener;
    private FilterByCompanyListener filterByCompanyListener;
    private HashSet<String> distinctCompanyName;
    private FlightsVo depVo;
    private String index;
    
    private ListView listViewlv;
	private String sdl;
	private String sdb;
	private String cd;
	private ArrayList<HashMap<String, String>> spList;
	private String spDate;
	private String dateSelected;
	private ProgressDialog progressDialog;
	
    public String getIndex() {
        return index;
    }
    public void setIndex(String index) {
        this.index = index;
    }
    public FlightsVo getDepVo() {
        return depVo;
    }
    private ProgressDialog pd;
    public Handler handler;
    
    public String getFlightDateBack() {
    	TextView tvd = (TextView) findViewById(R.id.day01).findViewById(
				R.id.btn_date);
		cd = tvd.getText().toString();
		Long cdd = Date.valueOf(cd).getTime();
		Long sdbd = Date.valueOf(sdb).getTime();
		
		if(cdd >= sdbd){
			return new Tools().nextDay(cd);
		}else{
			return sdb;
		}
		
    	
       // return flightDateBack;
    }
    public String getDepCityStr() {
        return depCityStr;
    }
    public String getArrCityStr() {
        return arrCityStr;
    }
	public void onWindowFocusChanged(boolean hasFocus){
    	if(pd == null){
    		Log.e("tag", "pd == null");
    		return;
    	}
    	Log.e("tag", "pd != null");	
        ImageView imageView = (ImageView) pd.findViewById(R.id.loading_img);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
    }
    class ViewHolder{
    	public final ImageView backButton = (ImageView)findViewById
				(R.id.title_back_query);
        public final ImageView leftButton    = (ImageView)findViewById
                (R.id.left_btn);
        public final ImageView middleButton  = (ImageView)findViewById
                (R.id.middle_btn);
        public final ImageView rightButton   = (ImageView)findViewById
                (R.id.right_btn);
        public final TextView titleText      = (TextView) findViewById
                (R.id.title_text_query);
        public final ListView publicList     = (ListView) findViewById
                (R.id.publiclist);
        public final ListView filterListView = (ListView) findViewById
                (R.id.filter_flight_list);
//        public final HorizontalScrollView topScrollView =
//                (HorizontalScrollView)findViewById(R.id.day15);
    }

    private String getCompanyCode(String name){
        Properties proCompany = new Properties();
        InputStream isCompany = getResources().openRawResource(R.raw.company);
        String codeCompany = "";
        try {
            proCompany.load(isCompany);
            codeCompany = (String)proCompany.get(name);
        } catch (IOException e) {
            e.printStackTrace();
        } catch(Exception e){
            Log.e("zzz", "no such air company in dictionary");
        }
        try {
            isCompany.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if("null".equals(codeCompany)|| null == codeCompany)
            codeCompany = "";
        return codeCompany;
    }
    private void getPassedParams(){
        Intent intent = getIntent();
        type = intent.getExtras().getString("type");
        requestURL = intent.getExtras().getString("url");
        cmd = intent.getExtras().getString("cmd");
        filter = intent.getExtras().getString("filter");
        depCity = intent.getExtras().getString("depCity");
        arrCity = intent.getExtras().getString("arrCity");
        depCityStr = intent.getExtras().getString("depCityStr");
        arrCityStr = intent.getExtras().getString("arrCityStr");
        carrier = intent.getExtras().getString("carrier");
        depVo = (FlightsVo)(intent.getSerializableExtra("depVo"));
        index = intent.getExtras().getString("index");
        currentCompanyCode = carrier;
        if("single".equals(type)){
            flightDate = intent.getExtras().getString("flightDate");
            currentLeaveDate = flightDate;
            sdl = sdb = getIntent().getExtras().getString("sdl");
        }
        if("round".equals(type)){
            flightDateLeave = intent.getExtras().getString("flightDateLeave");
            flightDate = flightDateLeave;
            currentLeaveDate = flightDate;
            flightDateBack = intent.getExtras().getString("flightDateBack");
            sdl = getIntent().getExtras().getString("sdl");
			sdb = getIntent().getExtras().getString("sdb");
			Log.e("sdl + sdb", sdl + " + " + sdb);
        }
        officeCode = intent.getExtras().getString("officeCode");
        flightTime = intent.getExtras().getString("flightTime");
        specialPriceUrl = intent.getExtras().getString("specialPriceUrl");
        Log.e("specialPriceUrl", specialPriceUrl);
    }
    private void prepareThreeBottomBtn(){
        threeBottomBtn.add(viewHolder.leftButton);
        threeBottomBtn.add(viewHolder.middleButton);
        threeBottomBtn.add(viewHolder.rightButton);
    }
    public void showScrollView(int flag){
//        if(flag == 1)
//            viewHolder.topScrollView.setVisibility(View.VISIBLE);
//        else
//            viewHolder.topScrollView.setVisibility(View.INVISIBLE);
    }

    public void setTitle(String dep, String arr){
        viewHolder.titleText.setText(dep + ">" + arr);
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
    
	private void readRawData() throws IOException{
		Resources res = this.getResources();	
		InputStream in = res.openRawResource(R.raw.flycompany);
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();
		sb.append("");
		String str;
		br = new BufferedReader(new InputStreamReader(in));
		while((str = br.readLine()) != null){
			sb.append(str);
			sb.append("\n");
			decodeUnicode(str);
		}
		
		if(in != null){
			in.close();
		}else if(br != null){
			br.close();
		}
		
		System.out.println(sb.toString());
	}
	
	private void decodeUnicode(String str) {
		Charset set = Charset.forName("UTF-16");
		Pattern p = Pattern.compile("\\\\u([0-9a-fA-F]{4})");
		Matcher m = p.matcher(str);
		int start = 0 ;
		int start2 = 0 ;
		StringBuffer sb = new StringBuffer();
		while(m.find(start)) {
			start2 = m.start() ;
			if(start2 > start){
				String seg = str.substring(start, start2) ;
				sb.append(seg);
			}
			String code = m.group(1);
			int i = Integer.valueOf(code , 16);
			byte[] bb = new byte[4] ;
			bb[0] = (byte) ((i >> 8) & 0xFF);
			bb[1] = (byte) (i & 0xFF) ;
			ByteBuffer b = ByteBuffer.wrap(bb);
			sb.append(String.valueOf(set.decode(b)).trim());
			start = m.end() ;
		}
		start2 = str.length() ;
		if(start2 > start){
			String seg = str.substring(start, start2) ;
			sb.append(seg);
		}
		Log.e("中文", sb.toString());
		
		distinctCompanyName.add(sb.toString());
	}
    
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_query);
        Exit.getInstance().addActivity(this);
        list = new ArrayList<FlightsVo>();
        btns = new ArrayList<SpecialPriceButton>();
        threeBottomBtn = new ArrayList<ImageView>();
        distinctCompanyName = new HashSet<String>();
        
        try {
			readRawData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        viewHolder = new ViewHolder();
        
        api = new Api();
        getPassedParams();
        prepareThreeBottomBtn();
        setTitle(depCityStr, arrCityStr);
        
        sortByPriceListener = new SortByPriceListener();
        sortByTakeOffListener = new SortByTakeOffListener();
        filterByCompanyListener = new FilterByCompanyListener();
        viewHolder.backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
        viewHolder.rightButton.setOnClickListener(sortByPriceListener);
        viewHolder.middleButton.setOnClickListener(sortByTakeOffListener);
        viewHolder.leftButton.setOnClickListener(filterByCompanyListener);

        viewHolder.filterListView.setOnItemClickListener(
                                               new FilterListItemListener());
        viewHolder.publicList.setOnItemClickListener(new ListItemListener());
        handler = new Handler(){
            public void handleMessage(Message msg) {
                pd.dismiss();// 关闭ProgressDialog
                showScrollView(1);
                viewHolder.publicList.setVisibility(View.VISIBLE);
            }
        };
        showListView(requestURL, viewHolder.publicList);
    }
    private void sortByPriceASC(){
        Collections.sort(list, new Comparator<FlightsVo>() {

            @Override
            public int compare(FlightsVo lhs, FlightsVo rhs) {
                if(Double.valueOf(lhs.getCabins().get(0).getSinglePrice())
                   > Double.valueOf(rhs.getCabins().get(0).getSinglePrice()))
                    return 1;
                else if(Double.valueOf(lhs.getCabins().get(0).getSinglePrice())
                   < Double.valueOf(rhs.getCabins().get(0).getSinglePrice()))
                    return -1;
                else
                    return 0;
            }
        });
    }
    private void sortByPriceDESC(){
        Collections.sort(list, new Comparator<FlightsVo>() {

            @Override
            public int compare(FlightsVo lhs, FlightsVo rhs) {
                if(Double.valueOf(lhs.getCabins().get(0).getSinglePrice())
                    > Double.valueOf(rhs.getCabins().get(0).getSinglePrice()))
                    return -1;
                else if(Double.valueOf(lhs.getCabins().get(0).getSinglePrice())
                    < Double.valueOf(rhs.getCabins().get(0).getSinglePrice()))
                    return 1;
                else
                    return 0;
            }
        });
    }
    private void changeDataSet(ArrayList<FlightsVo> alist) {
        alist.clear();
        //distinctCompanyName.clear();
        if(av == null){
            Log.e("zzz", "av == null获取数据失败");
            finish();// ????
            return;
        }
        if(av.getFlights() == null){
            Log.e("zzz", "av.getFlights() == null获取数据失败");
            finish();// ????
            return;
        }
        for (int i = 0; i < av.getFlights().size(); i++) {
            if (av.getFlights().get(i).getCabins().size() != 0) {
                alist.add(av.getFlights().get(i));
//                distinctCompanyName.add(av.getFlights().get(i)
//                                                   .CarrierFullName);
            }
        }
    }
    // update public for once
    public void showListView(final String URL, final ListView lv) {
        if(getInternet() == true){
        	pd = ProgressDialogTripg.show(this, null, null);
        	 new Thread() {
                 @Override
                 public void run() {
                     final String result = api.doGetData(URL);
                     JSONObject job;
					try {
						job = new JSONObject(result);
						String code = job.getString("Code");
						String msg = job.getString("Message");
						if(code.equals("0")){
							pd.dismiss();
							Looper.prepare();
							Toast.makeText(FlightQueryResultActivity.this, msg, Toast.LENGTH_LONG).show();
							Looper.loop();
						}else{
							handler.post(new Runnable() {
		                         @Override
		                         public void run() {
		                         	parseJsonData(result);
		                             setAdapters(lv);
		                             showLowPrice();
		                             handler.sendEmptyMessage(0);
		                         }
		                     });
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 
                     
                     
                 }
             }.start();
             
        }else{
        	Toast.makeText(FlightQueryResultActivity.this
        			, "网络链接已断开", Toast.LENGTH_LONG).show();
        }
       
    }
    public void changeDataSet(final String changedurl) {
    	if(getInternet() == true){
    		 pd = ProgressDialogTripg.show(this, null, null);
    	        new Thread() {
    	            @Override
    	            public void run() {
    	                final String result = api.doGetData(changedurl);
    	                JSONObject job;
    	                try {
							job = new JSONObject(result);
							String code = job.getString("Code");
							String msg = job.getString("Message");
							if(code.equals("0")){
								pd.dismiss();
								Looper.prepare();
								Toast.makeText(FlightQueryResultActivity.this, msg, Toast.LENGTH_LONG).show();
								Looper.loop();
							}else{
								handler.post(new Runnable() {
		    	                    @Override
		    	                    public void run() {
		    	                        parseJsonData(result);//change the place to top run.
		    	                        //changeDataSet(list);
		    	                        setAdapters(viewHolder.publicList);
		    	                        handler.sendEmptyMessage(0);
		    	                    }
		    	                });
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
    	                
    	            }
    	        }.start();
    	}else{
    		Toast.makeText(FlightQueryResultActivity.this
        			, "网络链接已断开", Toast.LENGTH_LONG).show();
    	}
       
    }
    public void showLowPrice() {
    	if(getInternet() == true){
    		new Thread() {
                @Override
                public void run() {
                    jsonLowPrice = api.doGetData(specialPriceUrl);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setLowPrice();
                        }
                    });
                }
            }.start();
    	}else{
    		Toast.makeText(FlightQueryResultActivity.this
        			, "网络链接已断开", Toast.LENGTH_LONG).show();
    	}
        
    }
    public void setLowPriceSrcollView(ArrayList<LowPrice> list){
//        int[] ids = new int[]{
//                R.id.day00,
//                R.id.day01,
//                R.id.day02,
//                R.id.day03,
//                R.id.day04,
//                R.id.day05,
//                R.id.day06,
//                R.id.day07,
//                R.id.day08,
//                R.id.day09,
//                R.id.day10,
//                R.id.day11,
//                R.id.day12,
//                R.id.day13,
//                R.id.day14};
    	int[] ids = new int[] { R.id.day00, R.id.day01, R.id.day02, R.id.day03 };

        btns.clear();
        for(int i = 0; i < ids.length; i++){
            final SpecialPriceButton spb = (SpecialPriceButton)findViewById(ids[i]);
            btns.add(spb);
            Log.e("sepical price", lps.getResult().get(i).Price);

            if(spb.getId() == R.id.day00){
            	spb.setBackgroundResource(R.drawable.day00);
				if(lps.getResult().get(0).Price.equals("-")){
					// spb.price.setText("无特价");
				}else{
					// spb.price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
					// spb.price.setText("￥" + lps.getResult().get(0).Price);
				}
				spb.price.setVisibility(View.GONE);
				spb.date.setVisibility(View.GONE);
				// spb.date.setText(lps.getResult().get(0).Date);
				spb.date.setText("前一天");
			}else if(spb.getId() == R.id.day01){
				spb.setBackgroundResource(R.drawable.day01);
				if (lps.getResult().get(0).Price.equals("-")) {
					spb.price.setText("无特价");
				}else{
					// spb.price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
					spb.price.setText("￥" + lps.getResult().get(0).Price);
				}
				// spb.date.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
				spb.date.setText(lps.getResult().get(0).Date);
				
			}else if(spb.getId() == R.id.day02){
				spb.setBackgroundResource(R.drawable.day02);
				if (lps.getResult().get(1).Price.equals("-")) {
					// spb.price.setText("无特价");
				} else {
					// spb.price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
					// spb.price.setText("￥" + lps.getResult().get(1).Price);
				}
				spb.price.setVisibility(View.GONE);
				spb.date.setVisibility(View.GONE);
			}else if(spb.getId() == R.id.day03){
				spb.setBackgroundResource(R.drawable.day03);
			}
		
            TextView tvd = (TextView) findViewById(R.id.day01).findViewById(
					R.id.btn_date);
			cd = tvd.getText().toString();
			
			findViewById(R.id.day00).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					sortByPriceListener.graySortByPriceButton();
					sortByTakeOffListener.graySortByTakeOffButton();
					filterByCompanyListener.graySortByCompanyButton();
					Date d = Date.valueOf(cd);
					Log.e("System.currentTimeMillis()",
							"" + new Tools().Today());
					Log.e("d.getTime()", "" + d.getTime());
					if (java.sql.Date.valueOf(new Tools().Today()).getTime() >= d
							.getTime()) {
						Toast.makeText(FlightQueryResultActivity.this,
								"航班已起飞!", Toast.LENGTH_LONG).show();
					} else {
						new AsyncTask<Void, Void, String>() {
							@Override
							protected void onPreExecute() {
								// TODO Auto-generated method stub
								super.onPreExecute();
								progressDialog = ProgressDialogTripg.show(
										FlightQueryResultActivity.this, null,
										null);
							}

							@Override
							protected void onPostExecute(String result) {
								// TODO Auto-generated method stub
								super.onPostExecute(result);
								progressDialog.dismiss();
								if (result.equals("1")) {
									Log.e("cd 1", cd);
									cd = new Tools().previousDay(cd);
									Message msg = new Message();
									msg.what = 1;
									msg.obj = cd;
									mHandler.sendMessage(msg);
									Log.e("cd 2", cd);
									String newSign = MD5
											.appendData(depCity, cd);
									changedURL = api.doGetTENRequestURL(cmd,
											filter, "&depCity=" + depCity,
											"&arrCity=" + arrCity, "&carrier="
													+ carrier, "&flightDate="
													+ cd, officeCode,
											flightTime, "&share=0", "&sign="
													+ newSign);
									changeDataSet(changedURL);
								}
							}

							@Override
							protected String doInBackground(Void... params) {
								// TODO Auto-generated method stub
								String code = "";
								String specialUrl = "http://flightapi.tripglobal.cn:8080/Default.aspx?cmd=floor&depCity="
										+ depCity
										+ "&arrCity="
										+ arrCity
										+ "&flightDate="
										+ new Tools().previousDay(cd)
										+ "&days=15&option=D&output=json";
								Log.e("new special price url", specialUrl);
								String data = new Tools().getURL(specialUrl);
								System.out.println("data ---> " + data);

								try {
									JSONObject job = new JSONObject(data);
									code = job.getString("Code");
									if (code.equals("1")) {
										JSONArray jArray = job
												.getJSONArray("Result");
										JSONObject job1 = jArray
												.optJSONObject(0);
										Message msg = new Message();
										msg.what = 0;
										msg.obj = job1.getString("Price");
										mHandler.sendMessage(msg);

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
			});
			
			findViewById(R.id.day01).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					sortByPriceListener.graySortByPriceButton();
					sortByTakeOffListener.graySortByTakeOffButton();
					filterByCompanyListener.graySortByCompanyButton();
					TextView tvd = (TextView) findViewById(R.id.day01)
							.findViewById(R.id.btn_date);
					String d = tvd.getText().toString();
					String newSign = MD5.appendData(depCity, d);
					changedURL = api.doGetTENRequestURL(cmd, filter,
							"&depCity=" + depCity, "&arrCity=" + arrCity,
							"&carrier=" + carrier, "&flightDate=" + d,
							officeCode, flightTime, "&share=0", "&sign="
									+ newSign);
					changeDataSet(changedURL);
				}
			});
			
			cd = tvd.getText().toString();
			Log.e("cd", cd);
			
			findViewById(R.id.day02).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new AsyncTask<Void, Void, String>() {
						@Override
						protected void onPreExecute() {
							// TODO Auto-generated method stub
							super.onPreExecute();
							progressDialog = ProgressDialogTripg.show(
									FlightQueryResultActivity.this, null, null);
						}

						@Override
						protected void onPostExecute(String result) {
							// TODO Auto-generated method stub
							super.onPostExecute(result);
							progressDialog.dismiss();
							if (result.equals("1")) {
								Log.e("nd 1", cd);
								cd = new Tools().nextDay(cd);
								Message msg = new Message();
								msg.what = 3;
								msg.obj = cd;
								mHandler.sendMessage(msg);
								Log.e("nd 2", cd);
								String newSign = MD5.appendData(depCity, cd);
								changedURL = api.doGetTENRequestURL(cmd,
										filter, "&depCity=" + depCity,
										"&arrCity=" + arrCity, "&carrier="
												+ carrier, "&flightDate=" + cd,
										officeCode, flightTime, "&share=0",
										"&sign=" + newSign);
								changeDataSet(changedURL);
							}
						}

						@Override
						protected String doInBackground(Void... params) {
							// TODO Auto-generated method stub
							String code = "";
							String specialUrl = "http://flightapi.tripglobal.cn:8080/Default.aspx?cmd=floor&depCity="
									+ depCity
									+ "&arrCity="
									+ arrCity
									+ "&flightDate="
									+ new Tools().nextDay(cd)
									+ "&days=15&option=D&output=json";
							Log.e("new special price url", specialUrl);
							String data = new Tools().getURL(specialUrl);
							System.out.println("data ---> " + data);

							try {
								JSONObject job = new JSONObject(data);
								code = job.getString("Code");
								if (code.equals("1")) {
									JSONArray jArray = job
											.getJSONArray("Result");
									JSONObject job1 = jArray.optJSONObject(0);
									Message msg = new Message();
									msg.what = 2;
									msg.obj = job1.getString("Price");
									mHandler.sendMessage(msg);

								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							return code;
						}
					}.execute();
				}
			});
			
			findViewById(R.id.day03).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					TextView tvd = (TextView) findViewById(R.id.day01)
							.findViewById(R.id.btn_date);
					spDate = tvd.getText().toString();
					Log.e("sp date", spDate);
					selectDate(RequestCode.TO_SELECT_DATE);
				}
			});
            
            
//            if(lps.getResult().get(i).Price.equals("-")){
//            	spb.price.setText("无特价");
//            }else{
//            	spb.price.setText("￥" + lps.getResult().get(i).Price);
//            }
//            
//
//            spb.date.setText(lps.getResult().get(i).Date.replace("-", "月") + "日");
//            if(i == 0){
//                spb.background.setImageResource(R.drawable.special_price1);
//                currentSPBtn = spb;
//                spb.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //reset three bottom button;
//                        sortByPriceListener.graySortByPriceButton();
//                        sortByTakeOffListener.graySortByTakeOffButton();
//                        filterByCompanyListener.graySortByCompanyButton();
//                        currentSPBtn = (SpecialPriceButton)v;
//                        for(int j = 0; j < btns.size(); j++){
//                            btns.get(j).background.setImageResource(R.drawable.special_price2);
//                        }
//                        currentSPBtn.background.setImageResource(R.drawable.special_price1);
//                        String year = flightDate.substring(0,5);
//
//                        String date = spb.date.getText().toString();
//                        date = date.replace("月", "-").replace("日", "");
//                        currentLeaveDate = year + date;
//
//                        String newSign = MD5.appendData(depCity, currentLeaveDate);
//                        changedURL = api.doGetTENRequestURL(cmd,filter,
//                                "&depCity=" + depCity,
//                                "&arrCity=" + arrCity,
//                                "&carrier=" + carrier,
//                                "&flightDate=" + currentLeaveDate,
//                                officeCode,flightTime,
//                                "&share=0",
//                                "&sign=" + newSign);
//                        Log.e("specialPrice", changedURL);
//                        	changeDataSet(changedURL);
//             
//                    }
//                });
//            }else{
//               spb.background.setImageResource(R.drawable.special_price2);
//               spb.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    //reset three bottom button;
//                    sortByPriceListener.graySortByPriceButton();
//                    sortByTakeOffListener.graySortByTakeOffButton();
//                    filterByCompanyListener.graySortByCompanyButton();
//                    currentSPBtn = (SpecialPriceButton)v;
//                    for(int j = 0; j < btns.size(); j++){
//                        btns.get(j).background.setImageResource(R.drawable.special_price2);
//                    }
//                    currentSPBtn.background.setImageResource(R.drawable.special_price1);
//                    String year = flightDate.substring(0,5);
//
//                    String date = spb.date.getText().toString();
//                    date = date.replace("月", "-").replace("日", "");
//                    currentLeaveDate = year + date;
//
//                    String newSign = MD5.appendData(depCity, currentLeaveDate);
//                    changedURL = api.doGetTENRequestURL(cmd,filter,
//                            "&depCity=" + depCity,
//                            "&arrCity=" + arrCity,
//                            "&carrier=" + carrier,
//                            "&flightDate=" + currentLeaveDate,
//                            officeCode,flightTime,
//                            "&share=0",
//                            "&sign=" + newSign);
//
//                    	changeDataSet(changedURL);
//          
//                }
//            });
//            }
        }
        
    }
    
	class NewSP extends AsyncTask<String, Void, String>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = ProgressDialogTripg.show(
					FlightQueryResultActivity.this, null,
					null);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (result.equals("1")) {
				String newSign = MD5
						.appendData(depCity, dateSelected);
				changedURL = api.doGetTENRequestURL(cmd,
						filter, "&depCity=" + depCity,
						"&arrCity=" + arrCity, "&carrier="
								+ carrier, "&flightDate="
								+ dateSelected, officeCode,
						flightTime, "&share=0", "&sign="
								+ newSign);
				changeDataSet(changedURL);
			}
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String code = "";
			String specialUrl = "http://flightapi.tripglobal.cn:8080/Default.aspx?cmd=floor&depCity="
					+ depCity
					+ "&arrCity="
					+ arrCity
					+ "&flightDate="
					+ params[0]
					+ "&days=15&option=D&output=json";
			Log.e("new special price url", specialUrl);
			String data = new Tools().getURL(specialUrl);
			System.out.println("data ---> " + data);

			try {
				JSONObject job = new JSONObject(data);
				code = job.getString("Code");
				if (code.equals("1")) {
					JSONArray jArray = job
							.getJSONArray("Result");
					JSONObject job1 = jArray
							.optJSONObject(0);
					Message msg = new Message();
					msg.what = 20;
					msg.obj = job1.getString("Price");
					mHandler.sendMessage(msg);

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return code;
		}	
	}
    
	private void selectDate(final int requestCode) {
		new AsyncTask<Void, Void, String>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				progressDialog = ProgressDialogTripg.show(
						FlightQueryResultActivity.this, null, null);
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				progressDialog.dismiss();
				Message msg = Message.obtain();
				if (result.equals("1")) {

					if (requestCode == RequestCode.TO_SELECT_DATE) {
						msg.what = 10;
						mHandler.sendMessage(msg);
					} else if (requestCode == RequestCode.TO_SELECT_DATE_LEAVE) {
						msg.what = 11;
						mHandler.sendMessage(msg);
					} else {
						msg.what = 12;
						mHandler.sendMessage(msg);
					}

				} else {

				}
			}

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String code = "";
				// String specialUrl =
				// "http://flightapi.tripglobal.cn:8080/?cmd=lowprice&depCity="
				String specialUrl = "http://flightapi.tripglobal.cn:8080/Default.aspx?cmd=floor&depCity="
						+ depCity
						+ "&arrCity="
						+ arrCity
						+ "&flightDate="
						+ spDate + "&days=15&option=D&output=json";
				Log.e("special price url", specialUrl);
				Tools tools = new Tools();
				String data = tools.getURL(specialUrl);
				System.out.println("data --->" + data);

				try {
					JSONObject job = new JSONObject(data);
					code = job.getString("Code");
					if (code.equals("1")) {
						spList = new ArrayList<HashMap<String, String>>();
						JSONArray jArray = job.getJSONArray("Result");
						for (int i = 0, j = jArray.length(); i < j; i++) {
							JSONObject job1 = jArray.optJSONObject(i);
							HashMap<String, String> hashMap = new HashMap<String, String>();
							String[] d = job1.getString("Date").split("-");
							Log.e("d[2]", "" + Integer.parseInt(d[2]));
							hashMap.put("date", "" + Integer.parseInt(d[2]));
							hashMap.put("" + Integer.parseInt(d[2]),
									job1.getString("Price"));

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

	}
    
	@SuppressLint("SimpleDateFormat") 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case RequestCode.TO_SELECT_DATE:
			if (resultCode == ResultCode.SUCCESS) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Bundle bundle = data.getExtras();
				dateSelected = bundle.getString("date");
				Log.e("date111111111", dateSelected);
				
				TextView tvd = (TextView) findViewById(R.id.day01).findViewById(
						R.id.btn_date);
				tvd.setText(dateSelected);
				specialPriceUrl = "http://flightapi.tripglobal.cn:8080/Default.aspx?cmd=floor"
						+ "&depCity="
						+ arrCity
						+ "&arrCity="
						+ depCity
						+ "&flightDate="
						+ dateSelected
						+ "&days=15&option=D&output=json";
				showLowPrice();
				
				new NewSP().execute(dateSelected);

			}
			break;
		case RequestCode.TO_SELECT_DATE_LEAVE:
			if (resultCode == ResultCode.SUCCESS) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Bundle bundle = data.getExtras();
				dateSelected = bundle.getString("date");
							
				Log.e("TO_SELECT_DATE_LEAVE", dateSelected);
				TextView tvd = (TextView) findViewById(R.id.day01).findViewById(
						R.id.btn_date);
				tvd.setText(dateSelected);
				specialPriceUrl = "http://flightapi.tripglobal.cn:8080/Default.aspx?cmd=floor"
						+ "&depCity="
						+ arrCity
						+ "&arrCity="
						+ depCity
						+ "&flightDate="
						+ dateSelected
						+ "&days=15&option=D&output=json";
				showLowPrice();
				
				new NewSP().execute(dateSelected);
				
				Long ld = (long) Integer.parseInt(sdf.format(dateSelected));
				Long lb = (long) Integer.parseInt(sdf.format(sdb));
				Log.e("ld + lb", ld + " + " + lb);
				
				Log.e("selectedLeaveDate 1111", dateSelected);


			}
			break;
		case RequestCode.TO_SELECT_DATE_BACK:
			if (resultCode == ResultCode.SUCCESS) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Bundle bundle = data.getExtras();
				dateSelected = bundle.getString("date");

				Log.e("TO_SELECT_DATE_BACK", dateSelected);
				TextView tvd = (TextView) findViewById(R.id.day01).findViewById(
						R.id.btn_date);
				tvd.setText(dateSelected);
				specialPriceUrl = "http://flightapi.tripglobal.cn:8080/Default.aspx?cmd=floor"
						+ "&depCity="
						+ arrCity
						+ "&arrCity="
						+ depCity
						+ "&flightDate="
						+ dateSelected
						+ "&days=15&option=D&output=json";
				showLowPrice();
				
				new NewSP().execute(dateSelected);
				
				
				Log.e("selectedBackDate 2222", dateSelected);
			}
			break;
		}
	}
	
	
	@SuppressLint({ "CutPasteId", "HandlerLeak" }) 
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String price = (String) msg.obj;
				TextView tvp = (TextView) findViewById(R.id.day01)
						.findViewById(R.id.btn_price);
				tvp.setText("￥" + price);
				break;
			case 1:
				String d = (String) msg.obj;
				TextView tvd = (TextView) findViewById(R.id.day01)
						.findViewById(R.id.btn_date);
				tvd.setText(d);
				break;
			case 2:
				String _price = (String) msg.obj;
				TextView _tvp = (TextView) findViewById(R.id.day01)
						.findViewById(R.id.btn_price);
				_tvp.setText("￥" + _price);
				break;
			case 3:
				String _d = (String) msg.obj;
				TextView _tvd = (TextView) findViewById(R.id.day01)
						.findViewById(R.id.btn_date);
				_tvd.setText(_d);
				break;
			case 10:
				Intent intent = new Intent(FlightQueryResultActivity.this,
						DateSelectActivity.class);

				Bundle bundle = new Bundle();
				bundle.putString("liveDate", new Tools().Today());
				Log.e("liveDate", new Tools().Today());
				bundle.putInt("hotelOrflight", 0);
				bundle.putString("type", "f");
				intent.putExtras(bundle);
				intent.putExtra("sp", spList);
				intent.putExtra("dateSelected", spDate);
				
				startActivityForResult(intent, RequestCode.TO_SELECT_DATE);
				break;
			case 11:
				Intent intent1 = new Intent(FlightQueryResultActivity.this,
						DateSelectActivity.class);

				Bundle bundle1 = new Bundle();
				bundle1.putString("liveDate", new Tools().Today());
				bundle1.putInt("hotelOrflight", 2);
				bundle1.putString("type", "f");
				intent1.putExtras(bundle1);
				intent1.putExtra("sp", spList);
				intent1.putExtra("dateSelected", spDate);
				startActivityForResult(intent1,
						RequestCode.TO_SELECT_DATE_LEAVE);
				break;
			case 12:
				Intent intent2 = new Intent(FlightQueryResultActivity.this,
						DateSelectActivity.class);

				Bundle bundle2 = new Bundle();
				bundle2.putString("liveDate", new Tools().Today());
				bundle2.putInt("hotelOrflight", 3);
				bundle2.putString("type", "f");
				intent2.putExtras(bundle2);
				intent2.putExtra("sp", spList);
				intent2.putExtra("dateSelected", spDate);
				startActivityForResult(intent2,
						RequestCode.TO_SELECT_DATE_BACK);
				break;
			case 20:
				String price1 = (String) msg.obj;
				TextView tvp1 = (TextView) findViewById(R.id.day01)
						.findViewById(R.id.btn_price);
				tvp1.setText("￥" + price1);
				break;
			default:
				break;
			}
		}

	};
    
	public String flightDateBack(){
    	TextView tvd = (TextView) findViewById(R.id.day01).findViewById(
				R.id.btn_date);
		cd = tvd.getText().toString();
		Long cdd = Date.valueOf(cd).getTime();
		Long sdbd = Date.valueOf(sdb).getTime();
		
		if(cdd >= sdbd){
			return new Tools().nextDay(cd);
		}else{
			return sdb;
		}
	}
	
    /**
     * @param setLowPrice
     * 
     * 此方法是特价接口 解析回调函数 
     * 
     * */
    public void setLowPrice() {
        showScrollView(1);//show
        if(jsonLowPrice == null){
            Toast.makeText(this, "网络异常", Toast.LENGTH_LONG).show();
            Log.e("zzz", "jsonLowPrice == null");
            showScrollView(0);//hide
            finish();//???
        }
        try {
            if(jsonLowPrice.equals("error")){
                Toast.makeText(this, "网络异常", Toast.LENGTH_LONG).show();
                showScrollView(0);//hide
                finish();//???
            }else{

            TypeReference<LowPrices> tlp = new TypeReference<LowPrices>() {};
            if(getInternet() == false){
            	Toast.makeText(FlightQueryResultActivity.this
            			, "网络链接已断开", Toast.LENGTH_LONG).show();
            }else{
            	lps = JsonUtils.json2GenericObject(jsonLowPrice,tlp);
            	
                if (lps == null) {
                    Toast.makeText(this, "获取特价航班失败", Toast.LENGTH_LONG).show();
                    showScrollView(0);//hide
                    //finish();//???
                    } else {
                        if (lps.getResult() != null && lps.getResult().size() != 0) {
                            setLowPriceSrcollView(lps.getResult());
                        } else {
                            Toast.makeText(this, "获取特价航班失败", Toast.LENGTH_LONG).show();
                            showScrollView(0);//hide
                          //  finish();//???
                        }
                    }
            }
            


            }
        } catch (Exception e) {
            Toast.makeText(this, "数据解析失败", Toast.LENGTH_LONG).show();
            showScrollView(0);//hide
            finish();//???
        }
    }
    public void parseJsonData(String jsonStr){
        if(jsonStr == null){
            Toast.makeText(this, "获取数据失败", Toast.LENGTH_LONG).show();
            finish();//???
        }
        try {
            if(jsonStr.equals("error")){
                Toast.makeText(this, "网络异常", Toast.LENGTH_LONG).show();
                finish();//???
            }else{
            TypeReference<AVVo> tvo = new TypeReference<AVVo>() {};
            if(getInternet() == false){
            	Toast.makeText(FlightQueryResultActivity.this
            			, "网络链接已断开", Toast.LENGTH_LONG).show();
            }else{
            	av = JsonUtils.json2GenericObject(jsonStr,tvo);
                if (av == null) {
                    Toast.makeText(this, "暂无航班", Toast.LENGTH_LONG).show();
                    finish();//???
                }
            }
            
        }
        } catch (Exception e) {
            Toast.makeText(this, "数据解析失败", Toast.LENGTH_LONG).show();
            finish();//???
        }
    }
    /**
     * @param setAdapters
     * 
     * 航班信息回调函数 
     * 
     * */
    public void setAdapters(ListView lv) {
        try {
            if(av != null) {
                if (av.getFlights() != null && av.getFlights().size() != 0) {
                    changeDataSet(list);
                    flightAdapter = new FlightAdapter(this, list, type);
                    lv.setAdapter(flightAdapter);
                } else {
                    Toast.makeText(this, "暂无航班", Toast.LENGTH_LONG).show();
                    finish();//???
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "未知异常", Toast.LENGTH_LONG).show();
            finish();//???
        }
    }
    public void notifyDataSetChanged(FlightAdapter fa){
        if(fa != null)
            fa.notifyDataSetChanged();
    }
    public void hidTitleAndList(){
        viewHolder.filterListView.setVisibility(View.VISIBLE);
        //viewHolder.topScrollView.setVisibility(View.INVISIBLE);
        viewHolder.publicList.setVisibility(View.INVISIBLE);
    }
    public void showTitleAndList(){
        viewHolder.filterListView.setVisibility(View.INVISIBLE);
        //viewHolder.topScrollView.setVisibility(View.VISIBLE);
        viewHolder.publicList.setVisibility(View.VISIBLE);
    }
    class ListItemListener implements OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            Intent intent = new Intent(FlightQueryResultActivity.this,
                                              FlightQueryDetailActivity.class);
            FlightsVo vo = list.get(position);
            ArrayList<CabinVo> cabin = vo.getCabins();
            intent.putExtra("list", (Serializable)cabin);
            intent.putExtra("FlightsVo", (Serializable)vo);
            intent.putExtra("depVo", (Serializable)depVo);
            Bundle bundle = new Bundle();
            bundle.putString("depCityStr", depCityStr);
            bundle.putString("arrCityStr", arrCityStr);
            bundle.putString("flightDateBack", flightDateBack);
            bundle.putString("type", type);
            bundle.putString("index", index);
            intent.putExtras(bundle);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        }
    }
    class FilterListItemListener implements OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            viewHolder.filterListView.setVisibility(View.INVISIBLE);
            TextView tv = (TextView)(((RelativeLayout)view).findViewById
                                                 (R.id.filtered_company_name));
            String cpnyCode = getCompanyCode(tv.getText().toString());
            currentCompanyCode = cpnyCode;

            String newSign = MD5.appendData(depCity, currentLeaveDate);
            changedURL = api.doGetTENRequestURL(
                    "?cmd=av&output=json",
                    "&filter=1",
                    "&depCity=" + depCity,
                    "&arrCity=" + arrCity,
                    "&carrier=" + cpnyCode,
                    "&flightDate=" + currentLeaveDate,
                    "&officeCode=CGQ182",
                    "&flightTime=",
                    "&share=0",
                    "&sign=" + newSign);
            Log.e("url", changedURL);
            
          
            	changeDataSet(changedURL);
            
            
            
            
            Log.e("zzz", "date:" + currentLeaveDate + " company:" 
                                                    + tv.getText().toString());
        }
    }
    class SortByPriceListener implements OnClickListener{
        private int flag = 0;
        private int[] grayBg = new int[]{R.drawable.filter_flight2,
                R.drawable.flight_tkoff1,R.drawable.flight_price1};
        public void graySortByPriceButton(){
            flag = 0;
            for(int i = 0; i < grayBg.length; i++){
                threeBottomBtn.get(i).setImageResource(grayBg[i]);
            }
        }
        @Override
        public void onClick(View v) {
            for(int i = 0; i < grayBg.length; i++){
                threeBottomBtn.get(i).setImageResource(grayBg[i]);
            }
            showTitleAndList();
            ImageView view = (ImageView)v;
            if(flag == 0){
                view.setImageResource(R.drawable.flight_price2);
                sortByPriceASC();
                flightAdapter = new FlightAdapter(
						FlightQueryResultActivity.this, list, type);
				viewHolder.publicList.setAdapter(flightAdapter);
                flag = 1;
            }
            else{
                view.setImageResource(R.drawable.flight_price3);
                sortByPriceDESC();
                flightAdapter = new FlightAdapter(
						FlightQueryResultActivity.this, list, type);
				viewHolder.publicList.setAdapter(flightAdapter);
                flag = 0;
            }        
    
            notifyDataSetChanged(flightAdapter);
        }
    }
    class SortByTakeOffListener implements OnClickListener{

        private int flag = 0;
        private int[] grayBg = new int[]{R.drawable.filter_flight2,
                R.drawable.flight_tkoff1,R.drawable.flight_price1};
        public void graySortByTakeOffButton(){
            flag = 0;
            for(int i = 0; i < grayBg.length; i++){
                threeBottomBtn.get(i).setImageResource(grayBg[i]);
            }

        }
        public void SortByTime(String flag){
            String newSign = MD5.appendData(depCity, currentLeaveDate);
            //ASC REQUEST URL SORT BY TAKE OFF TIME
            changedURL = api.doGetTENRequestURL(cmd,filter,
                    "&depCity=" + depCity,
                    "&arrCity=" + arrCity,
                    "&carrier=" + currentCompanyCode,
                    "&flightDate=" + currentLeaveDate,
                    officeCode,flightTime +"&sortby=1&sorttype="+flag,
                    "&share=0",
                    "&sign=" + newSign);
            
            
           
            	changeDataSet(changedURL);
            
            
            
            Log.e("sort by take off", "date:" + currentLeaveDate + " company:"
                                                         + currentCompanyCode);
        }
        @Override
        public void onClick(View v) {
            for(int i = 0; i < grayBg.length; i++){
                threeBottomBtn.get(i).setImageResource(grayBg[i]);
            }
            showTitleAndList();
            ImageView view = (ImageView)v;
            if(flag == 0){
                view.setImageResource(R.drawable.flight_tkoff2);
                SortByTime("ASC");
                flag = 1;
            }
            else{
                view.setImageResource(R.drawable.flight_tkoff3);
                SortByTime("DESC");
                flag = 0;
            }
        }
    }
    class FilterByCompanyListener implements OnClickListener{
        private int flag = 0;
        private int[] grayBg = new int[]{R.drawable.filter_flight2,
                R.drawable.flight_tkoff1,R.drawable.flight_price1};
        public void graySortByCompanyButton(){
            flag = 0;
            for(int i = 0; i < grayBg.length; i++){//
                threeBottomBtn.get(i).setImageResource(grayBg[i]);
            }
        }
        @Override
        public void onClick(View v) {
            for(int i = 0; i < grayBg.length; i++){
                threeBottomBtn.get(i).setImageResource(grayBg[i]);
            }
            ImageView view = (ImageView)v;
                if(flag == 0){
                    view.setImageResource(R.drawable.filter_flight1);
                    //viewHolder.topScrollView.setVisibility(View.INVISIBLE);
                    viewHolder.publicList.setVisibility(View.INVISIBLE);
                    viewHolder.filterListView.setVisibility(View.VISIBLE);
                    viewHolder.filterListView.setAdapter(new FlightFilterAdapter(
                            FlightQueryResultActivity.this, distinctCompanyName));
                    flag = 1;
                }
                else{
                    view.setImageResource(R.drawable.filter_flight2);
                    //viewHolder.topScrollView.setVisibility(View.VISIBLE);
                    viewHolder.publicList.setVisibility(View.VISIBLE);
                    viewHolder.filterListView.setVisibility(View.INVISIBLE);
                    flag = 0;
                }
        }
    }
}
