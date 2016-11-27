package cn.qianzheng.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.internet.Exit;
import cn.internet.Tools;
import cn.lvyou.main.LvYouLieBiaoMain;
import cn.lvyou.main.LvYouMain;
import cn.model.QXiangqingModel;
import cn.tripg.R;
import cn.youlun.main.YouLunMain;

public class QianXiangMain extends Activity implements OnClickListener {

	public String idString;
	private QXiangqingModel qx;
	private final int UPDATE_LIST_VIEW = 1;
	private TextView nameTextView;
	private TextView picTextView;
	private TextView typeTextView;
	private TextView preTextView;
	private ImageView yudingImageView;
	
	private TextView workerShake;
	private TextView studentShake;
	private TextView olderShake;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qxiangqingmain);
		Exit.getInstance().addActivity(this);
		preperImageLoader();
		idString = getIntent().getStringExtra("id");
		
		Log.e("id------", ""+idString);

		ImageView imageViewback = (ImageView)findViewById(R.id.title_qianzheng_back);
		imageViewback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				finish();
			}
		});
		
		nameTextView = (TextView)findViewById(R.id.textView1);
		picTextView = (TextView)findViewById(R.id.textView2);
		typeTextView = (TextView)findViewById(R.id.textView3);
		preTextView  = (TextView)findViewById(R.id.textView8);
		
		workerShake = (TextView)findViewById(R.id.textView10);
		studentShake = (TextView)findViewById(R.id.textView11);
		olderShake = (TextView)findViewById(R.id.textView12);
	
		LinearLayout linearLayout1 = (LinearLayout)findViewById(R.id.lineatlayoutxiang1);
		linearLayout1.setOnClickListener(this);
		LinearLayout linearLayout2 = (LinearLayout)findViewById(R.id.lineatlayoutxiang2);
		linearLayout2.setOnClickListener(this);
		LinearLayout linearLayout3 = (LinearLayout)findViewById(R.id.lineatlayoutxiang3);
		linearLayout3.setOnClickListener(this);

		
		yudingImageView = (ImageView)findViewById(R.id.imageView2);
		yudingImageView.setOnClickListener(new YudingImageOnClinckListener());
		
		new Thread(){
			public void run(){
				httpXiangQing();
			}
		}.start();
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ImageView titImageView = (ImageView)findViewById(R.id.imageView1);
				ImageLoader.getInstance().displayImage(qx.xqImageUrl, titImageView);
			}
		}, 1000);

		
		
	}
	

	private void preperImageLoader(){
		
		
		/******************* 配置ImageLoder ***********************************************/
		File cacheDir =
					StorageUtils.getOwnCacheDirectory(QianXiangMain.this,
								"imageloader/Cache");

		ImageLoaderConfiguration config =
					new ImageLoaderConfiguration.Builder(QianXiangMain.this)
								.denyCacheImageMultipleSizesInMemory()
								.discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
								.build();// 开始构建
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory().cacheOnDisc()
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
		
		ImageLoader.getInstance().init(config);// 全局初始化此配置
		/*********************************************************************************/
	}
	
	
	class QianzhengImageOnClinckListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.e("LinearLayout", "-----被点击");
			Intent intent = new Intent(QianXiangMain.this, QianZhViewReq.class);
			intent.putExtra("n", qx.xqcountryNameString);
			intent.putExtra("p", qx.xqsellpriceString);
			intent.putExtra("r", qx.xqrequiredString);
			startActivity(intent);
		}
		
		
		
	}
	
	class YudingImageOnClinckListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.e("yuding--", "-----被点击");
			Intent intent = new Intent(QianXiangMain.this, QianYudingMain.class);
			intent.putExtra("n", qx.xqcountryNameString);
			intent.putExtra("p", qx.xqsellpriceString);
			intent.putExtra("d", qx.xqproductidString);
			intent.putExtra("c", qx.xqCountryIdString);
			intent.putExtra("t", qx.xqvisatypeString);
			intent.putExtra("typeName", qx.xqtypeNameString);
			startActivity(intent);
		}
		
		
		
	}
	
	
	private void httpXiangQing(){
		
		String urlString = "http://www.tripg.cn/phone_api/trave/index.php/visa/get_Visas_info?pid="+idString;
		Log.e("签证详情 url----", ""+urlString);
		
		Tools tools = new Tools();
		String resultString = tools.getURL(urlString);
		Log.e("签证详情接口在此获取 返回值", "" + resultString);
		
		
		try {
			JSONArray jsonArray = new JSONArray(resultString);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				qx = new QXiangqingModel();
				qx.xqproductidString = (String)jsonObject.getString("ProductId").toString();
				qx.xqcountryNameString = (String)jsonObject.getString("CountryName").toString();
				qx.xqtypeNameString = (String)jsonObject.getString("TypeName").toString();
				qx.xqvisatypeString = (String)jsonObject.getString("VisaType").toString();
				qx.xqsellpriceString = (String)jsonObject.getString("SellPrice").toString();
				qx.xqprecautionString = (String)jsonObject.getString("Precautions").toString();
				qx.xqrequiredString = (String)jsonObject.getString("Required").toString();
				qx.xqImageUrl = "http://210.72.225.98:8080/"+(String)jsonObject.getString("Flag").toString();
				qx.xqCountryIdString = (String)jsonObject.getString("Country").toString();
				
				
				
			}
			
			// 通知刷新界面
			sendHandlerMessage(UPDATE_LIST_VIEW, null);
			
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("Exception", "---"+e);

		}
		
		
		
		
	}
	
	private void sendHandlerMessage(int what, Object obj) {

		Message msg = new Message();
		msg.what = what;
		handler.sendMessage(msg);

	}

	Handler handler = new Handler() {
		/**
		 * @param handleMessage
		 *            刷新listView的数据
		 * 
		 * */
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_LIST_VIEW:
	
				nameTextView.setText(qx.xqcountryNameString);
				picTextView.setText("￥"+qx.xqsellpriceString);
				typeTextView.setText("签证类型："+qx.xqtypeNameString);
				preTextView.setText("1."+qx.xqprecautionString);
				
				break;

			default:

				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.lineatlayoutxiang1:
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			workerShake.startAnimation(shake);
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Log.e("LinearLayout", "-----被点击");
					Intent intent = new Intent(QianXiangMain.this, QianZhViewReq.class);
					intent.putExtra("n", qx.xqcountryNameString);
					intent.putExtra("p", qx.xqsellpriceString);
					intent.putExtra("r", qx.xqrequiredString);
					startActivity(intent);
				}
			}, 800);
			break;
		case R.id.lineatlayoutxiang2:
			Animation shake1 = AnimationUtils.loadAnimation(this, R.anim.shake);
			studentShake.startAnimation(shake1);
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Log.e("LinearLayout", "-----被点击");
					Intent intent = new Intent(QianXiangMain.this, QianZhViewReq.class);
					intent.putExtra("n", qx.xqcountryNameString);
					intent.putExtra("p", qx.xqsellpriceString);
					intent.putExtra("r", qx.xqrequiredString);
					startActivity(intent);
				}
			}, 800);
			break;
		case R.id.lineatlayoutxiang3:
			Animation shake2 = AnimationUtils.loadAnimation(this, R.anim.shake);
			olderShake.startAnimation(shake2);
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Log.e("LinearLayout", "-----被点击");
					Intent intent = new Intent(QianXiangMain.this, QianZhViewReq.class);
					intent.putExtra("n", qx.xqcountryNameString);
					intent.putExtra("p", qx.xqsellpriceString);
					intent.putExtra("r", qx.xqrequiredString);
					startActivity(intent);
				}
			}, 800);
			break;
		default:
			break;
		}
	}

	
	
}
