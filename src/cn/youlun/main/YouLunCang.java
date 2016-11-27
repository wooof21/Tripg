package cn.youlun.main;

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

import android.R.bool;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.tripg.activity.flight.FlightQueryResultActivity;
import cn.tripg.R;

public class YouLunCang extends ArrayAdapter<HashMap<String, Object>>{

	private List<HashMap<String, Object>> listData;
	private Context context;
	// private ViewHolder viewHolder;
	public ImageView imageView;
	private Bitmap bitmap;
	private HashMap<String, Object> hashMap;
	private boolean btnbool = true;

	
	public YouLunCang(Context context, int textViewResourceId,
			List<HashMap<String, Object>> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub

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
	
	
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder viewHolder;

		hashMap = listData.get(position);
		if (convertView == null) {

			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.boardyoulunc, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.yxiangimageView1);
			viewHolder.textView1 = (TextView) convertView
					.findViewById(R.id.yxiangtextView1);
			viewHolder.textView2 = (TextView) convertView
					.findViewById(R.id.yxiangtextView2);
			viewHolder.textView3 = (TextView) convertView
					.findViewById(R.id.yxiangtextView3);
			viewHolder.onImageView = (ImageView)convertView.findViewById(R.id.imageViewliner1);

			
			
			if (((YouLunXiangMain)context).key == position) {
				Log.e("key----", ""+((YouLunXiangMain)context).key);
				viewHolder.onImageView.setImageResource(R.drawable.gl1);
				((YouLunXiangMain)context).cangString = (String) hashMap.get("name");
				((YouLunXiangMain)context).danjiaString = (String) hashMap.get("price");
				
			}else {
				Log.e("position----", ((YouLunXiangMain)context).key+"----"+position);
				viewHolder.onImageView.setImageResource(R.drawable.gl2);
				
			}
			
			ImageLoader.getInstance().displayImage((String) hashMap.get("pic"), viewHolder.imageView);

			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			ImageLoader.getInstance().displayImage((String) hashMap.get("pic"), viewHolder.imageView);
		}
		
		
		
		
		viewHolder.textView1.setText((String) hashMap.get("name"));
		viewHolder.textView2.setText("￥"+(String) hashMap.get("price"));
		viewHolder.textView3.setText((String) hashMap.get("content"));
		if (((YouLunXiangMain)context).key == position) {
			Log.e("key----", ""+((YouLunXiangMain)context).key);
			viewHolder.onImageView.setImageResource(R.drawable.gl1);
		}else {
			Log.e("position----", ((YouLunXiangMain)context).key+"----"+position);
			viewHolder.onImageView.setImageResource(R.drawable.gl2);
		}
		viewHolder.onImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub   .setImageResource(R.drawable.gl1);
				Log.e("仓位信息 选中-----", "-----00000000-----");
				((YouLunXiangMain)context).key = position;
				notifyDataSetChanged();
				
				HashMap<String, Object> hashMap2 = listData.get(position);
				((YouLunXiangMain)context).cangString = (String) hashMap2.get("name");
				((YouLunXiangMain)context).danjiaString = (String) hashMap2.get("price");
				Log.e("仓位点击---", (String) hashMap2.get("name")+"------"+(String) hashMap2.get("price"));
				
				
			}
		});
		
		

		return convertView;
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
	
	

	class ViewHolder {

		public TextView textView1;
		public TextView textView2;
		public TextView textView3;
		public ImageView imageView;
		public ImageView onImageView;
	}
	
	
}
