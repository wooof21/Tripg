package cn.lvyou.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import cn.internet.Exit;
import cn.internet.Tools;
import cn.model.LXiangListmodel;
import cn.model.LvXiangModel;
import cn.tripg.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LvYouXiangMain extends Activity implements OnClickListener {

	public String tidString;
	private Bitmap bitmap;
	private LinearLayout aBoardman;
	private List<LvXiangModel> lXiangModels;
	private final int UPDATE_LIST_VIEW = 1;
	
	private TextView titTextView;
	private TextView priTextView;
	private TextView teseTextView;
	private TextView baobanTextView;
	private TextView tiaokuanTextView;
	private ImageView upiImageView;
	private ImageView tijiaoimageView;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lvyouxiangmain);
		Exit.getInstance().addActivity(this);
		preperImageLoader();

		ImageView imageViewback = (ImageView) findViewById(R.id.title_lvyouxinag1_back);
		imageViewback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				finish();
			}
		});

		tidString = (String) getIntent().getStringExtra("tid");
		Log.e("tidString-----", "" + tidString);

		aBoardman = (LinearLayout) findViewById(R.id.a_boardman1);
		titTextView = (TextView)findViewById(R.id.textView1);
		priTextView = (TextView)findViewById(R.id.textView2);
		teseTextView = (TextView)findViewById(R.id.textView8);
		baobanTextView = (TextView)findViewById(R.id.textView14);
		upiImageView = (ImageView)findViewById(R.id.imageView1);
		tijiaoimageView = (ImageView)findViewById(R.id.imageView2);
		tijiaoimageView.setOnClickListener(new TijiaoyudingOnitem());
		
		new Thread(){
			public void run(){
				lvyouXiangHttp();
			}
			
		}.start();
		
		
		
		
		

	}

	public void lvyouXiangHttp() {

		String urlString = "http://www.tripg.cn/phone_api/trave/index.php/Travel/single?id="+tidString;
		Tools tools = new Tools();
		String resultString = tools.getURL(urlString);
		Log.e("旅游详情接口 urlString---", "" + urlString);
		Log.e("旅游详情接口在此获取 返回值", "" + resultString);
		
		try {
			lXiangModels = new ArrayList<LvXiangModel>();
			
			JSONObject jsonObject = new JSONObject(resultString);
			JSONObject resJsonObject = jsonObject.getJSONObject("Result");
			JSONObject singJsonObject = resJsonObject.getJSONObject("SingleNews");
			JSONArray hangArray = resJsonObject.getJSONArray("hangcheng");
			
			LvXiangModel lxiangModel = new LvXiangModel();
			lxiangModel.lxtidString = (String)singJsonObject.getString("id");
			lxiangModel.lxtitleString = (String)singJsonObject.getString("title");
			lxiangModel.lxpriString = (String)singJsonObject.getString("price");
			lxiangModel.lxuplString = (String)"http://www.tripg.cn/"+singJsonObject.getString("upload");
			lxiangModel.lxteseString = (String)singJsonObject.getString("tese");
			lxiangModel.lxbaohanString = (String)singJsonObject.getString("baohan");
			lxiangModel.lxtiaokuanString = (String)singJsonObject.getString("tiaokuan");
			
			for (int i = 0; i < hangArray.length(); i++) {
			
				LXiangListmodel llListmodel = new LXiangListmodel();
				JSONObject liljJsonObject = hangArray.getJSONObject(i);
				llListmodel.lxlTidString = (String)liljJsonObject.getString("id").toString();
				llListmodel.lxlFidString = (String)liljJsonObject.getString("fid").toString();
				llListmodel.lxlDayString = (String)liljJsonObject.getString("day").toString();
				llListmodel.lxlHangxString = (String)liljJsonObject.getString("hangxian").toString();
				llListmodel.lxlJieshaoString = (String)liljJsonObject.getString("jieshao").toString();
				llListmodel.lxlPic1String= "http://www.tripg.cn/"+(String)liljJsonObject.getString("pic1").toString();
				llListmodel.lxlPic2String = "http://www.tripg.cn/"+(String)liljJsonObject.getString("pic2").toString();
				
				lxiangModel.listxiang.add(llListmodel);
				
			}
			
			lXiangModels.add(lxiangModel);
			
			if (lXiangModels.size() > 0) {
				// 通知刷新界面
				sendHandlerMessage(UPDATE_LIST_VIEW, null);
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getApplicationContext(), "获取数据失败",
					Toast.LENGTH_LONG).show();
		}
		

		
	}
	
	private void preperImageLoader(){
		
		
		/******************* 配置ImageLoder ***********************************************/
		File cacheDir =
					StorageUtils.getOwnCacheDirectory(LvYouXiangMain.this,
								"imageloader/Cache");

		ImageLoaderConfiguration config =
					new ImageLoaderConfiguration.Builder(LvYouXiangMain.this)
								.denyCacheImageMultipleSizesInMemory()
								.discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
								.build();// 开始构建
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory().cacheOnDisc()
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
		
		ImageLoader.getInstance().init(config);// 全局初始化此配置
		/*********************************************************************************/
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

				//lXiangModels
				
				LvXiangModel lv = lXiangModels.get(0);
				titTextView.setText(lv.lxtitleString);
				priTextView.setText("￥"+lv.lxpriString);
				teseTextView.setText(lv.lxteseString);
				baobanTextView.setText(lv.lxbaohanString);
				ImageLoader.getInstance().displayImage(lv.lxuplString, upiImageView);

				
				// 代码增加layout界面
				for (int i = 0; i < lv.listxiang.size(); i++) {
					LXiangListmodel li = lv.listxiang.get(i);
					
					LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
					final RelativeLayout rl = (RelativeLayout) inflater.inflate(
							R.layout.boardlinelayitem, null);
					RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.MATCH_PARENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);
					TextView onTextView = (TextView) rl
							.findViewById(R.id.textViewitem1);
					onTextView.setText(li.lxlHangxString);
					onTextView.setTag(i);
					onTextView.setOnClickListener(LvYouXiangMain.this);
					// ((TextView)
					// (rl.findViewById(R.id.textViewitem1))).setText("测试一下"+i);
					aBoardman.addView(rl, rlp);

				}
				
				
				
				break;

			default:

				break;
			}
		};
	};
	
	



	class linearlayOnitem implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.e("tag-----", "" + (Integer) v.getTag());
			LvXiangModel lv = lXiangModels.get(0);
			LXiangListmodel li = lv.listxiang.get((Integer) v.getTag());
			
			Intent intent = new Intent(LvYouXiangMain.this, LvYouXItemMain.class);
			intent.putExtra("tit", li.lxlHangxString);
			intent.putExtra("jie", li.lxlJieshaoString);
			intent.putExtra("pic1", li.lxlPic1String);
			intent.putExtra("pic2", li.lxlPic2String);
			startActivity(intent);
			
			
			
		}

	}
	
	class TijiaoyudingOnitem implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			Intent intent = new Intent(LvYouXiangMain.this, LvYouYuDingMain.class);
			intent.putExtra("tid", tidString);
			intent.putExtra("n", lXiangModels.get(0).lxtitleString);
			intent.putExtra("p", lXiangModels.get(0).lxpriString);
			startActivity(intent);
			
		}
		
		
		
	}

	@Override
	public void onClick(final View v) {
		// TODO Auto-generated method stub
		TextView tvShake = (TextView) v.findViewWithTag(v.getTag());
		Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		tvShake.startAnimation(shake);
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.e("tag-----", "" + (Integer) v.getTag());
				LvXiangModel lv = lXiangModels.get(0);
				LXiangListmodel li = lv.listxiang.get((Integer) v.getTag());
				
				Intent intent = new Intent(LvYouXiangMain.this, LvYouXItemMain.class);
				intent.putExtra("tit", li.lxlHangxString);
				intent.putExtra("jie", li.lxlJieshaoString);
				intent.putExtra("pic1", li.lxlPic1String);
				intent.putExtra("pic2", li.lxlPic2String);
				startActivity(intent);
			}
		}, 800);
		
		
	}
	

}
