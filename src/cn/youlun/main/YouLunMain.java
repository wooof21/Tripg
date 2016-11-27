package cn.youlun.main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.internet.Exit;
import cn.internet.Tools;
import cn.model.YouMainModel;
import cn.tripg.R;
import cn.tripg.xlistview.MarqueeTV;

public class YouLunMain extends Activity implements OnClickListener {

	private ImageView you1ImageView;
	private ImageView you2ImageView;
	private ImageView you3ImageView;
	private ImageView you4ImageView;
	private ImageView you5ImageView;
	private ImageView you6ImageView;
	private ImageView you7ImageView;
	private ImageView you8ImageView;
	private ImageView you9ImageView;

	private MarqueeTV tityou1TextView;
	private MarqueeTV tityou2TextView;
	private MarqueeTV tityou3TextView;
	private MarqueeTV tityou4TextView;
	private MarqueeTV tityou5TextView;
	private MarqueeTV tityou6TextView;
	private MarqueeTV tityou7TextView;
	private MarqueeTV tityou8TextView;
	private MarqueeTV tityou9TextView;

	private TextView nameyou1TextView;
	private TextView nameyou2TextView;
	private TextView nameyou3TextView;
	private TextView nameyou4TextView;
	private TextView nameyou5TextView;
	private TextView nameyou6TextView;
	private TextView nameyou7TextView;
	private TextView nameyou8TextView;
	private TextView nameyou9TextView;

	public int postion;
	private Bitmap bitmap;
	private TextView moreTextView;
	private final int UPDATE_LIST_VIEW = 1;
	private HashMap<String, Object> hashMap;
	private List<YouMainModel> ylModel;
	


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.youlun_main_new);
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


		
		you1ImageView = (ImageView) findViewById(R.id.imageViewyoulunit1);
		you1ImageView.setTag(0);
		you1ImageView.setOnClickListener(new youlunImageOnClinckListener());
		you2ImageView = (ImageView) findViewById(R.id.imageViewyoulunit2);
		you2ImageView.setTag(1);
		you2ImageView.setOnClickListener(new youlunImageOnClinckListener());
		you3ImageView = (ImageView) findViewById(R.id.imageViewyoulunit3);
		you3ImageView.setTag(2);
		you3ImageView.setOnClickListener(new youlunImageOnClinckListener());
		you4ImageView = (ImageView) findViewById(R.id.imageViewyoulunit4);
		you4ImageView.setTag(3);
		you4ImageView.setOnClickListener(new youlunImageOnClinckListener());
		you5ImageView = (ImageView) findViewById(R.id.imageViewyoulunit5);
		you5ImageView.setTag(4);
		you5ImageView.setOnClickListener(new youlunImageOnClinckListener());
		you6ImageView = (ImageView) findViewById(R.id.imageViewyoulunit6);
		you6ImageView.setTag(5);
		you6ImageView.setOnClickListener(new youlunImageOnClinckListener());
		you7ImageView = (ImageView) findViewById(R.id.imageViewyoulunit7);
		you7ImageView.setTag(6);
		you7ImageView.setOnClickListener(new youlunImageOnClinckListener());
		you8ImageView = (ImageView) findViewById(R.id.imageViewyoulunit8);
		you8ImageView.setTag(7);
		you8ImageView.setOnClickListener(new youlunImageOnClinckListener());
		you9ImageView = (ImageView) findViewById(R.id.imageViewyoulunit9);
		you9ImageView.setTag(8);
		you9ImageView.setOnClickListener(new youlunImageOnClinckListener());

		tityou1TextView = (MarqueeTV) findViewById(R.id.textViewyoulunitt1);
		tityou1TextView.setTag(10);
		tityou1TextView.setOnClickListener(new youlunNameOnClinckListener());
		tityou2TextView = (MarqueeTV) findViewById(R.id.textViewyoulunitt2);
		tityou2TextView.setTag(11);
		tityou2TextView.setOnClickListener(new youlunNameOnClinckListener());
		tityou3TextView = (MarqueeTV) findViewById(R.id.textViewyoulunitt3);
		tityou3TextView.setTag(12);
		tityou3TextView.setOnClickListener(new youlunNameOnClinckListener());
		tityou4TextView = (MarqueeTV) findViewById(R.id.textViewyoulunitt4);
		tityou4TextView.setTag(13);
		tityou4TextView.setOnClickListener(new youlunNameOnClinckListener());
		tityou5TextView = (MarqueeTV) findViewById(R.id.textViewyoulunitt5);
		tityou5TextView.setTag(14);
		tityou5TextView.setOnClickListener(new youlunNameOnClinckListener());
		tityou6TextView = (MarqueeTV) findViewById(R.id.textViewyoulunitt6);
		tityou6TextView.setTag(15);
		tityou6TextView.setOnClickListener(new youlunNameOnClinckListener());
		tityou7TextView = (MarqueeTV) findViewById(R.id.textViewyoulunitt7);
		tityou7TextView.setTag(16);
		tityou7TextView.setOnClickListener(new youlunNameOnClinckListener());
		tityou8TextView = (MarqueeTV) findViewById(R.id.textViewyoulunitt8);
		tityou8TextView.setTag(17);
		tityou8TextView.setOnClickListener(new youlunNameOnClinckListener());
		tityou9TextView = (MarqueeTV) findViewById(R.id.textViewyoulunitt9);
		tityou9TextView.setTag(18);
		tityou9TextView.setOnClickListener(new youlunNameOnClinckListener());

		nameyou1TextView = (TextView) findViewById(R.id.textViewyoulunty1);

		nameyou2TextView = (TextView) findViewById(R.id.textViewyoulunty2);

		nameyou3TextView = (TextView) findViewById(R.id.textViewyoulunty3);

		nameyou4TextView = (TextView) findViewById(R.id.textViewyoulunty4);

		nameyou5TextView = (TextView) findViewById(R.id.textViewyoulunty5);

		nameyou6TextView = (TextView) findViewById(R.id.textViewyoulunty6);

		nameyou7TextView = (TextView) findViewById(R.id.textViewyoulunty7);

		nameyou8TextView = (TextView) findViewById(R.id.textViewyoulunty8);

		nameyou9TextView = (TextView) findViewById(R.id.textViewyoulunty9);

		moreTextView = (TextView) findViewById(R.id.textViewyoulun2);
		moreTextView.setOnClickListener(this);

		new Thread(){
			public void run(){
				youlunHttp();
			}
		}.start();
		

	}

	private void youlunHttp() {

		String urlString = "http://www.tripg.cn/phone_api/trave/index.php/cruise/index?times=&route&counts=&cruises&PageSize=9&Page=1";
		Tools tools = new Tools();
		String resultString = tools.getURL(urlString);
		Log.e("游轮主视图接口在此获取 返回值", "" + resultString);

		try {

			ylModel = new ArrayList<YouMainModel>();
			JSONObject jsonObject = new JSONObject(resultString);
			String codeString = jsonObject.getString("Code");
			if (codeString.equals("1")) {
				JSONObject resjsonObject = jsonObject.getJSONObject("Result");
				JSONArray listjArray = resjsonObject.getJSONArray("list");

				for (int i = 0; i < listjArray.length(); i++) {
					YouMainModel yMainModel = new YouMainModel();
					JSONObject yJsonObject = listjArray.getJSONObject(i);
					yMainModel.mTidString = (String) yJsonObject.getString(
							"tid").toString();
					yMainModel.mR_fangString = (String) yJsonObject.getString(
							"r_fang1").toString();
					yMainModel.mR_listpicString = "http://www.tripg.cn/cruise2013/"
							+ (String) yJsonObject.getString("r_listpic")
									.toString();
					yMainModel.mR_TitleString = (String) yJsonObject.getString(
							"r_title").toString();
					yMainModel.mR_CompanyString = (String) yJsonObject
							.getString("r_company").toString();
					yMainModel.mR_GocityString = (String) yJsonObject
							.getString("r_gocity").toString();
					yMainModel.mR_GotimeString = (String) yJsonObject
							.getString("r_gotime").toString();
					yMainModel.mR_AllcityString = (String) yJsonObject
							.getString("r_allcity").toString();
					yMainModel.mC_TitleString = (String) yJsonObject.getString(
							"c_title").toString();

					ylModel.add(yMainModel);
				}
				if (ylModel.size() > 0) {
					// 通知刷新界面
					sendHandlerMessage(UPDATE_LIST_VIEW, null);
				}

			} else {
				Toast.makeText(getApplicationContext(), "获取数据失败",
						Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			// TODO: handle exception
			Log.e("Exception", "---" + e);

		}

	}

	private void preperImageLoader(){
		
		
		/******************* 配置ImageLoder ***********************************************/
		File cacheDir =
					StorageUtils.getOwnCacheDirectory(YouLunMain.this,
								"imageloader/Cache");//

		ImageLoaderConfiguration config =
					new ImageLoaderConfiguration.Builder(YouLunMain.this)
								.denyCacheImageMultipleSizesInMemory()
								.discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
								.build();// 开始构建
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory().cacheOnDisc()
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				
				.build();
		
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

				tityou1TextView.setText((String) ylModel.get(0).mR_TitleString);
				nameyou1TextView.setText("￥"
						+ (String) ylModel.get(0).mR_fangString);

				ImageLoader.getInstance().displayImage(ylModel.get(0).mR_listpicString, you1ImageView);
				

				
				tityou2TextView.setText((String) ylModel.get(1).mR_TitleString);
				nameyou2TextView.setText("￥"
						+ (String) ylModel.get(1).mR_fangString);

				ImageLoader.getInstance().displayImage(ylModel.get(1).mR_listpicString, you2ImageView);

				tityou3TextView.setText((String) ylModel.get(2).mR_TitleString);
				nameyou3TextView.setText("￥"
						+ (String) ylModel.get(2).mR_fangString);

				ImageLoader.getInstance().displayImage(ylModel.get(2).mR_listpicString, you3ImageView);

				tityou4TextView.setText((String) ylModel.get(3).mR_TitleString);
				nameyou4TextView.setText("￥"
						+ (String) ylModel.get(3).mR_fangString);

				ImageLoader.getInstance().displayImage(ylModel.get(3).mR_listpicString, you4ImageView);

				tityou5TextView.setText((String) ylModel.get(4).mR_TitleString);
				nameyou5TextView.setText("￥"
						+ (String) ylModel.get(4).mR_fangString);

				ImageLoader.getInstance().displayImage(ylModel.get(4).mR_listpicString, you5ImageView);
				

				tityou6TextView.setText((String) ylModel.get(5).mR_TitleString);
				nameyou6TextView.setText("￥"
						+ (String) ylModel.get(5).mR_fangString);

				ImageLoader.getInstance().displayImage(ylModel.get(5).mR_listpicString, you6ImageView);
				
				tityou7TextView.setText((String) ylModel.get(6).mR_TitleString);
				nameyou7TextView.setText("￥"
						+ (String) ylModel.get(6).mR_fangString);

				ImageLoader.getInstance().displayImage(ylModel.get(6).mR_listpicString, you7ImageView);

				tityou8TextView.setText((String) ylModel.get(7).mR_TitleString);
				nameyou8TextView.setText("￥"
						+ (String) ylModel.get(7).mR_fangString);

				ImageLoader.getInstance().displayImage(ylModel.get(7).mR_listpicString, you8ImageView);

				tityou9TextView.setText((String) ylModel.get(8).mR_TitleString);
				nameyou9TextView.setText("￥"
						+ (String) ylModel.get(8).mR_fangString);
				ImageLoader.getInstance().displayImage(ylModel.get(8).mR_listpicString, you9ImageView);

				
				break;

			default:

				break;
			}
		};
	};


	class youlunImageOnClinckListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.e("tag-----", "" + (Integer) v.getTag());
			String idString = (String)ylModel.get((Integer) v.getTag()).mTidString;
			Intent intent = new Intent(YouLunMain.this, YouLunXiangMain.class);
			intent.putExtra("tid", idString);
			startActivity(intent);
			
			
		}

	}

	class youlunNameOnClinckListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.e("tag-----", "" + (Integer) v.getTag());
			String idString = (String)ylModel.get(((Integer) v.getTag())-10).mTidString;
			Intent intent = new Intent(YouLunMain.this, YouLunXiangMain.class);
			intent.putExtra("tid", idString);
			startActivity(intent);
			
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		moreTextView.startAnimation(shake);
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(YouLunMain.this,YouLunLieBiaoMain.class);
				startActivity(intent);
			}
		}, 800);
	}
	
	


}
