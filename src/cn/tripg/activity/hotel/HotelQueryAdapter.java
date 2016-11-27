package cn.tripg.activity.hotel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import model.hotel.Hotel;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.tripg.R;

public class HotelQueryAdapter extends BaseAdapter{

	private Context context;
	private LayoutInflater mInflater = null;
	private ArrayList<Hotel> model;
	public HotelQueryAdapter(Context context, ArrayList<Hotel> model){
		this.mInflater = LayoutInflater.from(context);
		this.context = context;
		this.model = model;
		preperImageLoader();
		for(int i=0;i<model.size();i++){
			try{
				Log.e("", model.get(i).getOutdoorSceneImage());
			}catch(NullPointerException e){
				Log.e("default imgUrl", "http://www.elongstatic.com/gp1/M00/30/0F/pIYBAFIkRRuAaxEmAAAfGH7i3Qw437.jpg?v=20130604134758");
			}
			
		}
	}
	@Override
	public int getCount() {
		return this.model.size();
	}
	@Override
	public Object getItem(int position) {
		return model.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
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
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		String url = model.get(position).getOutdoorSceneImage();
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.xlistview_item1, null);
			viewHolder = new ViewHolder();
			viewHolder.hotelName = (TextView)convertView.findViewById(R.id.hotel_name);
			viewHolder.hotelPrice = (TextView)convertView.findViewById(R.id.hotel_price);
			viewHolder.hotelPhone = (TextView)convertView.findViewById(R.id.hotel_phone);
			viewHolder.hotelAddress = (TextView)convertView.findViewById(R.id.hotel_address);
			viewHolder.hotelImage = (ImageView)convertView.findViewById(R.id.hotel_img);
			viewHolder.starCode = (ImageView)convertView.findViewById(R.id.hotel_star);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
//		if(viewHolder.isImageLoaded == false){
//			LoadHotelImage ldThread = new LoadHotelImage(viewHolder, url);
//			ldThread.start();
//		}
		
			
		
		ImageLoader.getInstance().displayImage(url, viewHolder.hotelImage);
		
		viewHolder.hotelPhone.setText(model.get(position).Phone);
		viewHolder.hotelAddress.setText(model.get(position).HotelAddress);
		viewHolder.hotelName.setText(model.get(position).HotelName);
		viewHolder.hotelPrice.setText("￥" +
		String.format("%.0f", Double.valueOf(model.get(position).LowestPrice)));
		setStarImg(viewHolder, position);
		return convertView;
	}
	
	private void setStarImg(ViewHolder holder, int pos){
		if("2".equals(model.get(pos).StarCode)){
			holder.starCode.setImageResource(R.drawable.star2);
		}else if("3".equals(model.get(pos).StarCode)){
			holder.starCode.setImageResource(R.drawable.star3);
		}else if("4".equals(model.get(pos).StarCode)){
			holder.starCode.setImageResource(R.drawable.star4);
		}else if("5".equals(model.get(pos).StarCode)){
			holder.starCode.setImageResource(R.drawable.star5);
		}
	}
	class LoadHotelImage extends Thread{
		private ViewHolder viewHolder;
		private String imgUrl;
		private Bitmap bitmap;
		public LoadHotelImage(ViewHolder holder, String url){
			this.viewHolder = holder;
			this.imgUrl = url;
			bitmap = null;
		}
		@SuppressLint("HandlerLeak")
		final Handler handler = new Handler(){
			public void handleMessage(Message msg){
				if(bitmap != null){
					viewHolder.isImageLoaded = true;
					viewHolder.hotelImage.setImageBitmap(bitmap);
				}
				
		  	 }
		};
		@Override
		public void run() {
			//loading img
			Looper.prepare();
			try{
				URL url= new URL(imgUrl);
				HttpURLConnection conn= (HttpURLConnection)url.openConnection();
				conn.setReadTimeout(90000);
				InputStream inputStream=conn.getInputStream();
				bitmap = BitmapFactory.decodeStream(inputStream);
			}catch (MalformedURLException e1) {
				e1.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}
			handler.sendEmptyMessage(0);
		}
	}

	class ViewHolder{
		public TextView hotelName;
		public TextView hotelPrice;
		public TextView hotelPhone;
		public TextView hotelAddress;
		public ImageView hotelImage;
		public ImageView starCode;
		public boolean isImageLoaded = false;
	}
}
