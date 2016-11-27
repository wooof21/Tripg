package com.example.yidaocardemo;

import httpdelegate.CityHttpConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import city_info_list.CarInfo;
import city_info_list.CityCellView;
import city_info_list.CityList;
import cn.internet.Exit;
import cn.tripg.R;
import cn.tripg.activity.flight.MainActivity;
import cn.tripg.widgit.ProgressDialogTripg;
import com.car.yidao.TypeCarActivity;

public class CarActivity extends Activity implements OnClickListener, CityList,OnItemClickListener {

	private ProgressDialog dialog;
	private List<HashMap<String, Object>> listData;
	private static CarActivity instance;
	private ListView listView;
	
	private final int UPDATE_LIST_VIEW = 1;

	public ImageButton imageButton1;
	public ImageView imageView1;
	public ImageView imageView2;
	public ImageView imageView3;

	public TextView textViewCity;
	public TextView textViewType;
	
	
	public Builder alertDialog;
	public String jiejiString;
	public String songjiString;
	public String rizuString;
	public String shizuString;
	public String banriString;
	public String typeString;
	public String typeIntString;
	public String[] arrayString;
	public String cityErString;
	
	public ProgressDialog progressDialog;
	

	
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
		setContentView(R.layout.car_main_activity);
		Exit.getInstance().addActivity(this);
		Log.e("caractivity", "-----------------");
		imageButton1 = (ImageButton)findViewById(R.id.imageButton1);
		imageButton1.setOnClickListener(this);
		imageView1 = (ImageView)findViewById(R.id.imageView1);
		imageView1.setOnClickListener(this);
		imageView2 = (ImageView)findViewById(R.id.imageView2);
		imageView2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("image 2 is here ");
				showAlaerDialog();
			}
		});
		
		imageView3 = (ImageView)findViewById(R.id.imageView3);
		imageView3.setOnClickListener(this);
		
		
		textViewCity = (TextView)findViewById(R.id.textView1);
		textViewType = (TextView)findViewById(R.id.textView2);
		ImageView backimageView = (ImageView)findViewById(R.id.title_back);
		backimageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				Intent intent = new Intent(CarActivity.this,MainActivity.class);
//				startActivity(intent);
				finish();
			}
		});
		
		
		
		//给当前类指定代理参数
		instance = this;
		//设置listview   INVISIBLE隐藏
		listView = (ListView)findViewById(R.id.listView1);
		listView.setCacheColorHint(0);
		listView.setVisibility(View.INVISIBLE);
		/**
		 * @param listData  
		 * 			列表的返回数据
		 * */
		listData = new ArrayList<HashMap<String,Object>>();
		for (int i = 0; i < 5; i++) {
			HashMap<String, Object> hMap = new HashMap<String, Object>();
			if (i==0 ) {
				hMap.put("name", "北京");
				hMap.put("short", "bj");
				listData.add(hMap);
			}else if (i==1) {
				hMap.put("name", "长春");
				hMap.put("short", "cc");
				listData.add(hMap);
			}else if (i==2) {
				hMap.put("name", "成都");
				hMap.put("short", "cd");
				listData.add(hMap);
			}else if (i==3) {
				hMap.put("name", "广州");
				hMap.put("short", "gz");
				listData.add(hMap);
			}else if (i==4) {
				hMap.put("name", "上海");
				hMap.put("short", "sh");
				listData.add(hMap);
			}
		}
		
		//通知刷新界面
		sendHandlerMessage(UPDATE_LIST_VIEW, null);
				
	//	showDiaLog();
		//progressDialog = ProgressDialogTripg.show(this, null, null);

		/*
		//启动下载
		String urlString = "http://open.yongche.com/v2/service?access_token=jeIGaNbTnfMwE5yQF91DiDruZFYHXm7lS99tCBgM";
		System.out.println(urlString);
		CityHttpConnection cityHttpConnection = new CityHttpConnection(this);
		cityHttpConnection.sendGetConnection(urlString);
		*/
		
		
	}
	private void showDiaLog() {
		dialog = new ProgressDialog(this);
		dialog.setTitle("加载数据请稍后");
		dialog.setMessage("Loading...");
		dialog.show();
	}
	
	/**
	 * @param showAlaerDialog
	 * 自定义条件选择器  这个是用车类型选择器 以下是定制代码
	 * */
	private void showAlaerDialog(){
	
		alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("请选择服务类型");
//		arrayString = new String[]{jiejiString,songjiString,rizuString,banriString,shizuString};
//		String[] typeArrayStrings = new String[]{"7","8","12","11","1"};
		arrayString = new String[]{"接机","送机"};
		String[] typeArrayStrings = new String[]{"7","8"};
		
			final ArrayList<String> arrayList = new ArrayList<String>();
			final ArrayList<String> arrayListType = new ArrayList<String>();
			for (int i = 0; i < arrayString.length; i++) {
				if (!arrayString[i].equals("null")) {
					arrayList.add(arrayString[i]);
					arrayListType.add(typeArrayStrings[i]);
					
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
					typeString = ""+arrayList.get(arg1);
					typeIntString = ""+arrayListType.get(arg1);
					textViewType.setText(arrayList.get(arg1)) ;
					System.out.println(typeString+typeIntString );
					break;
				case 1:
					typeString = ""+arrayList.get(arg1);
					typeIntString = ""+arrayListType.get(arg1);
					textViewType.setText(arrayList.get(arg1)) ;
					System.out.println(typeString+typeIntString);
					break;
				case 2:
					typeString = ""+arrayList.get(arg1);
					typeIntString = ""+arrayListType.get(arg1);
					textViewType.setText(arrayList.get(arg1)) ;
					System.out.println(typeString+typeIntString);
					break;
				case 3:
					typeString = ""+arrayList.get(arg1);
					typeIntString = ""+arrayListType.get(arg1);
					textViewType.setText(arrayList.get(arg1)) ;
					System.out.println(typeString+typeIntString);

					break;
				case 4:
					typeString = ""+arrayString[arg1];
					typeIntString = ""+arrayListType.get(arg1);
					textViewType.setText(arrayList.get(arg1)) ;
					System.out.println(typeString+typeIntString);

					break;
				default:
					break;
				}
			}
			
			
		}).show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageButton1:
			listView.setVisibility(View.VISIBLE);
			System.out.println("listview is here1");
			break;
		case R.id.imageView1:
			System.out.println("listview is here1");
			break;
		case R.id.imageView2:
			System.out.println("listview is here2");
			showAlaerDialog();
			break;
		case R.id.imageView3:
			System.out.println("listview is here3");
			
			if(getInternet() == false){
				Toast.makeText(CarActivity.this, "网络链接已断开", Toast.LENGTH_LONG).show();
			}else{
				if (typeIntString != null && cityErString != null) {

					Intent intent2 = new Intent(this,TypeCarActivity.class);
//					Intent intent2 = new Intent(this,NewTypeCarActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("type", typeIntString);
					bundle.putString("city", cityErString);
					bundle.putString("cityname", (String)textViewCity.getText());
					intent2.putExtras(bundle);
					startActivity(intent2);
				}else if (typeIntString == null&&cityErString == null) {
					Builder alertDg = new AlertDialog.Builder(this);
					alertDg.setTitle("提示");
					alertDg.setMessage("请选择城市和用车类型!");
					alertDg.setPositiveButton("确定", null);
					alertDg.show();
				}else if (typeIntString == null) {
					Builder alertDg = new AlertDialog.Builder(this);
					alertDg.setTitle("提示");
					alertDg.setMessage("请选择用车类型!");
					alertDg.setPositiveButton("确定", null);
					alertDg.show();
					
				}else if (cityErString == null) {
					Builder alertDg = new AlertDialog.Builder(this);
					alertDg.setTitle("提示");
					alertDg.setMessage("请选择城市!");
					alertDg.setPositiveButton("确定", null);
					alertDg.show();
					
				}
			}
			
			break;
		default:
			break;
		}
	}
	/**
	 * @param getCityList  
	 * http代理回调函数
	 * 			解析之后，该代理方法回调到实体类使用
	 * 			getHashMap方法将数据转转hashMap类型方便listView使用
	 * 
	 * */
	public void getCityList(ArrayList<CarInfo> list) {
		//dialog.dismiss();
		progressDialog.dismiss();
		for (CarInfo carInfo : list) {
			listData.add(getHashMap(carInfo));
		}
		//通知刷新界面
		sendHandlerMessage(UPDATE_LIST_VIEW, null);
	}
	
	/**
	 * @param sendHandlerMessage
	 * 调用界面数据，更新数据
	 * 
	 * */
	
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
				CityCellView cell = new CityCellView(instance,
						R.layout.car_main_activity, listData);
				
				listView.setAdapter(cell);
				listView.setOnItemClickListener(instance);
				break;

			default:
				break;
			}
		};
	};
	/**
	 * @param getHashMap
	 * 此方法将解析的数据转化成listview 可用的数据hashMap类型
	 * 
	 * */
	private HashMap<String, Object> getHashMap(CarInfo carInfo) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		
		hashMap.put("short", carInfo.getShortString());
		hashMap.put("en", carInfo.getEn());
		hashMap.put("name", carInfo.getName());
		hashMap.put("product_list", carInfo.getProductList());
		
		return hashMap;
	}

	
	/**
	 * @param onItemClick
	 * listView的点击函数，这个里面可以获取到cell上显示的内容，利用这个内容进行赋值操作。
	 * 
	 * 
	 * */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		
		HashMap<String, Object> hashMap = listData.get(position);
		System.out.println(String.format("name is:%s", hashMap.get("name")));
		
		cityErString = String.format("%s", hashMap.get("short"));
		System.out.println("short "+ cityErString);
		
		
		/*
		@SuppressWarnings("unchecked")
		HashMap<String, Object> product = (HashMap<String, Object>) hashMap.get("product_list");
		for (String key : product.keySet()) {
			System.out.println(String.format("in product:%s=%s %s", product.get("1"), product.get(key),key));
			
		}
		shizuString = ""+product.get("1");
		System.out.println(shizuString);
		songjiString = ""+product.get("8");
		System.out.println(songjiString);
		jiejiString = ""+product.get("7");
		System.out.println(jiejiString);
		banriString = ""+product.get("11");
		System.out.println(banriString);
		rizuString = ""+product.get("12");
		System.out.println(rizuString);
		imageView2.setOnClickListener(this);
		Log.e("******", ""+jiejiString.length());
		if(banriString.length() != 4){
			typeString = banriString;
			typeIntString = "11";
			textViewType.setText(banriString) ;
		}else if(shizuString.length() != 4){
			typeString = shizuString;
			typeIntString = "1";
			textViewType.setText(shizuString) ;
		}else if(songjiString.length() != 4){
			typeString = songjiString;
			typeIntString = "8";
			textViewType.setText(songjiString) ;
		}else if(rizuString.length() != 4){
			typeString = rizuString;
			typeIntString = "12";
			textViewType.setText(rizuString) ;
		} else if (jiejiString.length() != 4) {
			typeString = jiejiString;
			typeIntString = "7";
			textViewType.setText(jiejiString) ;
		}
		
		*/
		
		textViewCity.setText(String.format("%s", hashMap.get("name")));
		listView.setVisibility(View.INVISIBLE);
		
	}

}
