package cn.lvyou.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import cn.internet.Exit;
import cn.tripg.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class LvYouXItemMain extends Activity {

	public String pic1String;
	public String pic2String;
	public String titString;
	public String jieString;

	private Bitmap bitmap;

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.youlunzhanshi_main);
		Exit.getInstance().addActivity(this);
		preperImageLoader();

		ImageView imageViewback = (ImageView) findViewById(R.id.title_youlun_back);
		imageViewback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				finish();
			}
		});

		pic1String = (String) getIntent().getStringExtra("pic1");
		Log.e("lvyou  预订--", pic1String);
		pic2String = (String) getIntent().getStringExtra("pic2");
		Log.e("lvyou  预订--", pic2String);
		titString = (String) getIntent().getStringExtra("tit");
		Log.e("lvyou  预订--", titString);
		jieString = (String) getIntent().getStringExtra("jie");
		Log.e("lvyou  预订--", jieString);
		
		TextView titTextView = (TextView)findViewById(R.id.textView1);
		titTextView.setText(titString);
		TextView jieTextView = (TextView)findViewById(R.id.textView2);
		jieTextView.setText(jieString);
		
		ImageView pic1ImageView = (ImageView)findViewById(R.id.imageViewyoulunz1);
		ImageLoader.getInstance().displayImage(pic1String, pic1ImageView);

		ImageView pic2ImageView = (ImageView)findViewById(R.id.imageViewyoulunz2);
		ImageLoader.getInstance().displayImage(pic2String, pic2ImageView);
		
		
		

	}
	
	private void preperImageLoader(){
		
		
		/******************* 配置ImageLoder ***********************************************/
		File cacheDir =
					StorageUtils.getOwnCacheDirectory(LvYouXItemMain.this,
								"imageloader/Cache");

		ImageLoaderConfiguration config =
					new ImageLoaderConfiguration.Builder(LvYouXItemMain.this)
								.denyCacheImageMultipleSizesInMemory()
								.discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
								.build();// 开始构建
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory().cacheOnDisc()
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
		
		ImageLoader.getInstance().init(config);// 全局初始化此配置
		/*********************************************************************************/
	}


	
	

}
