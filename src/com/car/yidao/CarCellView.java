package com.car.yidao;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import order.pnr.yidao.OrderPnrMain;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.tripg.R;
import cn.tripg.activity.flight.MainActivity;
import cn.tripg.activity.login.LoginActivity;


@SuppressLint("HandlerLeak")
public class CarCellView extends ArrayAdapter<HashMap<String, Object>> {



	private List<HashMap<String, Object>> listData;
	private Context context;
	private Bitmap bitmap;
	private ViewHolder viewHolder;
	private String im = "http://mapi.tripglobal.cn";
	private String UrlType, carLvl, carName, maxLoad, outFee, fee, carImg;
	private String imageURL;
	private TypeCarActivity tca;
	protected ImageLoader imageLoader = ImageLoader.getInstance();  
	
	public CarCellView(Context context, int textViewResourceId,
			List<HashMap<String, Object>> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.listData = objects;
		preperImageLoader();
		
	}

	
	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public HashMap<String, Object> getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public void add(HashMap<String, Object> object) {
		super.add(object);
		listData.add(object);
	}

	@Override
	public void clear() {
		super.clear();
		listData.clear();
	}

	private void preperImageLoader(){
		
		
		/******************* 配置ImageLoder ***********************************************/
		File cacheDir =
					StorageUtils.getOwnCacheDirectory(context,
								"imageloader/Cache");

		ImageLoaderConfiguration config =
					new ImageLoaderConfiguration.Builder(context)
								.denyCacheImageMultipleSizesInMemory()
								.discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
								.build();// 开始构建
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory().cacheOnDisc()
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
		
		ImageLoader.getInstance().init(config);// 全局初始化此配置
		/*********************************************************************************/
	}
	
	
	/**
	 * @param getView
	 * 自定义的cell内容在这里赋值操作
	 * 
	 * */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		LayoutInflater mInflater = LayoutInflater.from(context);
		convertView = mInflater.inflate(R.layout.carcell_xml_item, null);
		viewHolder = new ViewHolder();
		final HashMap<String, Object> hashMap = listData.get(position);
		System.out.println("hashmap*****"+hashMap);
		
		//在cell中绑定预订按钮的点击事件 
		final ImageButton imageButton = (ImageButton)convertView.findViewById(R.id.imageButtonyuding);
		imageButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if(event.getAction() == MotionEvent.ACTION_DOWN){
					Log.e("cellview button", "down");
				}
				if(event.getAction() == MotionEvent.ACTION_UP){
					Log.e("cellview button", "up");
					//跳转界面 设置传值参数  ((TypeCarActivity)context).typeString 是父类参数这样调用 当前类参数用Bundle bundle调用
					Intent intent2 = new Intent(context, OrderPnrMain.class);
					String typestring = ((TypeCarActivity)context).typeString;
					String ciytstring = ((TypeCarActivity)context).cityString;
					//String carIdString = (String) hashMap.get("car_type_id");
					Bundle bundle = new Bundle();
					
					bundle.putString("totalFee", (String) hashMap.get("Fee"));
					bundle.putString("ProductId", (String) hashMap.get("ProductId"));
					bundle.putString("UseStatus", (String) hashMap.get("UseStatus"));
					bundle.putString("DepCityId", (String) hashMap.get("DepCityId"));
					bundle.putString("DepCity", (String) hashMap.get("DepCity"));
					bundle.putString("ProductTypeId", (String) hashMap.get("ProductTypeId"));
					bundle.putString("PriductTypeDesc", (String) hashMap.get("PriductTypeDesc"));
					bundle.putString("carName", (String) hashMap.get("CarBrandName"));
					bundle.putString("CarModelId", (String)hashMap.get("CarModelId"));
					bundle.putString("carId", (String) hashMap.get("CarBrandId"));
					bundle.putString("carLvl", (String) hashMap.get("CarLevelName"));
					bundle.putString("carLvlId", (String) hashMap.get("CarLevelId"));
					bundle.putString("maxLoad", (String) hashMap.get("MaxPeopleNumber"));
					bundle.putString("ProductBaseFeeId", (String) hashMap.get("ProductBaseFeeId"));
					bundle.putString("outFee", (String) hashMap.get("outFee"));
					
					bundle.putString("type", typestring);
					bundle.putString("city", ciytstring);
					//bundle.putString("carid", carIdString);
					bundle.putString("person_number", (String) hashMap.get("person_number"));
					Log.e("typeString*******",typestring+"*****"+ciytstring);
					intent2.putExtras(bundle);
					
					context.startActivity(intent2);
					
				}
				return false;
			}
		});
		//575773634   

		viewHolder.cellImage = (ImageView)convertView.findViewById(R.id.imageView1);
		//preperImageLoader();
		Log.e("imageurl****", im + (String) hashMap.get("CarModelImage"));
		imageURL = new String(im + (String) hashMap.get("CarModelImage"));

		ImageLoader.getInstance().displayImage(imageURL, viewHolder.cellImage);
		
//		if(viewHolder.isImageLoaded == false){
//			
//			LoadHotelImage ldThread = new LoadHotelImage(viewHolder,imageURL);
//			System.out.println("Loading which car image ---> " + imageURL);
//			ldThread.start();
//			
//		}

		
		
		
//		final Handler handler=new Handler(){
//			public void handleMessage(Message msg){
//				imageView.setImageBitmap(bitmap);
//		  	 }
//		};
//
//		handler.post(new Runnable()
//		{	public void run() 
//			{//这里下载数据
//			    new Thread(){
//			    	public void run() {
//			    		try{
//							URL url= new URL(imageURL);
//							HttpURLConnection conn= (HttpURLConnection)url.openConnection();
//
//							InputStream inputStream=conn.getInputStream();
//							bitmap = BitmapFactory.decodeStream(inputStream);
//							
//							handler.sendEmptyMessage(0);
//							}catch (MalformedURLException e1) {
//								e1.printStackTrace();
//							}catch (IOException e) {
//								e.printStackTrace();
//							}
//			    	};
//			    }.start();
//				
//			}
//		});
		UrlType = (String) hashMap.get("URLtype");
		if(UrlType.equals("ziyou")){
			//tca.dissDialog();
			TextView textView = (TextView) convertView.findViewById(R.id.textView1);
			textView.setText((String) hashMap.get("CarBrandName"));
			System.out.println("CarBrandName*****"+(String) hashMap.get("CarBrandName"));
			
			TextView textView2 = (TextView) convertView.findViewById(R.id.textViewche);
			textView2.setText((String) hashMap.get("CarBrandName") + "(" + hashMap.get("CarLevelName") + ")");
			System.out.println("CarBrandName*****"+(String) hashMap.get("CarBrandName") + hashMap.get("CarLevelName"));
			
			TextView textviewFee = (TextView) convertView.findViewById(R.id.textViewfee);
			textviewFee.setText((String) hashMap.get("Fee").toString().replace(".00", ""));
			
			TextView textviewyuan = (TextView) convertView.findViewById(R.id.textViewyuan);
			textviewyuan.invalidate();
//			textviewyuan.setText("元/次起(含"+(String) hashMap.get("IncludesHours")+"小时"+(String) hashMap.get("IncludesKilometers")+"公里)");
			
			TextView textView3 = (TextView) convertView.findViewById(R.id.textViewperson);
			textView3.setText("限乘人数:"+(String) hashMap.get("MaxPeopleNumber"));
			System.out.println("MaxPeopleNumber*****"+(String) hashMap.get("MaxPeopleNumber"));
			
			TextView textviewhour = (TextView) convertView.findViewById(R.id.textViewhuor);
			textviewhour.setText("超小时费:"+(String) hashMap.get("TimeOutFee")+"元/小时");
			
			TextView textviewli = (TextView) convertView.findViewById(R.id.textViewli);
			textviewli.invalidate();
//			textviewli.setText("超公里费:"+(String) hashMap.get("fee_per_kilometer")+"元/公里;");
		}
		else{
		TextView textView = (TextView) convertView.findViewById(R.id.textView1);
		textView.setText((String) hashMap.get("name"));
		System.out.println("name*****"+(String) hashMap.get("name"));
		
		TextView textView2 = (TextView) convertView.findViewById(R.id.textViewche);
		textView2.setText((String) hashMap.get("brand"));
		System.out.println("brand*****"+(String) hashMap.get("brand"));
		
		TextView textviewFee = (TextView) convertView.findViewById(R.id.textViewfee);
		textviewFee.setText((String) hashMap.get("fee").toString().replace(".00", ""));
		
		TextView textviewyuan = (TextView) convertView.findViewById(R.id.textViewyuan);
		textviewyuan.setText("元/次起(含"+(String) hashMap.get("time_length")+"小时"+(String) hashMap.get("distance")+"公里)");
	
		TextView textView3 = (TextView) convertView.findViewById(R.id.textViewperson);
		textView3.setText("限乘人数:"+(String) hashMap.get("person_number"));
		System.out.println("person_number*****"+(String) hashMap.get("person_number"));
		
		TextView textviewhour = (TextView) convertView.findViewById(R.id.textViewhuor);
		textviewhour.setText("超小时费:"+(String) hashMap.get("fee_per_hour")+"元/小时");
		
		TextView textviewli = (TextView) convertView.findViewById(R.id.textViewli);
		textviewli.setText("超公里费:"+(String) hashMap.get("fee_per_kilometer")+"元/公里;");
		}
		return convertView;
	}
	
	@SuppressWarnings("unused")
	class LoadHotelImage extends Thread{
		private String imgUrl;
		private ViewHolder viewHolder;
		public LoadHotelImage(ViewHolder holder, String url){
			this.viewHolder = holder;
			this.imgUrl = url;
		}
		
		 Handler handler = new Handler(){
			public void handleMessage(Message msg){
				viewHolder.cellImage.setImageBitmap(bitmap);
		  	 }
		};

		@Override
		public void run() {
			try{
				URL url= new URL(imgUrl);
				HttpURLConnection conn= (HttpURLConnection)url.openConnection();
				InputStream inputStream=conn.getInputStream();
				bitmap = BitmapFactory.decodeStream(inputStream);
			}catch (MalformedURLException e1) {
				e1.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}
			handler.sendEmptyMessage(0);
			if(bitmap != null){
				this.viewHolder.isImageLoaded = true;
			}
		}
	}
	
	class ViewHolder{

		public ImageView cellImage;
		public boolean isImageLoaded = false;
	}
	
}