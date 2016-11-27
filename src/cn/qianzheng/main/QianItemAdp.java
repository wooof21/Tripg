package cn.qianzheng.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.tripg.R;
import cn.youlun.main.YouLunMain;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class QianItemAdp extends ArrayAdapter<HashMap<String, Object>> {

	
	private List<HashMap<String, Object>> listData;
	private Context context;
//	private ViewHolder viewHolder;
	public  ImageView imageView;
	private Bitmap bitmap;
	private  HashMap<String, Object> hashMap;
	
	public QianItemAdp(Context context, int textViewResourceId,
			List<HashMap<String, Object>> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		
		this.context = context;
		this.listData = objects;
		preperImageLoader();
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
	
	
	/**
	 * @param getView
	 * 自定义的cell内容在这里赋值操作
	 * 
	 * */
	@SuppressLint({ "InflateParams", "ViewHolder" }) @SuppressWarnings("unused")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{

		ViewHolder viewHolder;

		hashMap = listData.get(position);
		if (convertView == null) {
			
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.qianliebiaoitem, null);
			viewHolder = new ViewHolder();			
			viewHolder.imageView = (ImageView)convertView.findViewById(R.id.qianxiangimageView1);			
			viewHolder.textView1 = (TextView) convertView.findViewById(R.id.qianxiangtextView1);
			viewHolder.textView2 = (TextView) convertView.findViewById(R.id.qianxiangtextView2);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
			
		ImageLoader.getInstance().displayImage((String) hashMap.get("Flag"), viewHolder.imageView);

		viewHolder.textView1.setText((String) hashMap.get("CountryName"));
		viewHolder.textView2.setText((String) hashMap.get("SellPrice"));

		
		
		
		
		
		return convertView;
		
	}

		
	
	class ViewHolder {

		public TextView textViewId;
		public TextView textView1;
		public TextView textView2;
		public ImageView imageView;
	}
}
