package cn.lvyou.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
import android.widget.TextView;
import android.widget.Toast;
import cn.internet.Exit;
import cn.internet.Tools;
import cn.model.LvYouMmodel;
import cn.model.QianZhengModel;
import cn.model.YouMainModel;
import cn.qianzheng.main.QianZhengMain;
import cn.tripg.R;
import cn.tripg.xlistview.MarqueeTV;

public class LvYouMain extends Activity implements OnClickListener {

	private ImageView lvyou1ImageView;
	private ImageView lvyou2ImageView;
	private ImageView lvyou3ImageView;
	private ImageView lvyou4ImageView;
	private ImageView lvyou5ImageView;
	private ImageView lvyou6ImageView;
	private ImageView lvyou7ImageView;
	private ImageView lvyou8ImageView;
	private ImageView lvyou9ImageView;

	private MarqueeTV titlvyou1TextView;
	private MarqueeTV titlvyou2TextView;
	private MarqueeTV titlvyou3TextView;
	private MarqueeTV titlvyou4TextView;
	private MarqueeTV titlvyou5TextView;
	private MarqueeTV titlvyou6TextView;
	private MarqueeTV titlvyou7TextView;
	private MarqueeTV titlvyou8TextView;
	private MarqueeTV titlvyou9TextView;

	private TextView namelvyou1TextView;
	private TextView namelvyou2TextView;
	private TextView namelvyou3TextView;
	private TextView namelvyou4TextView;
	private TextView namelvyou5TextView;
	private TextView namelvyou6TextView;
	private TextView namelvyou7TextView;
	private TextView namelvyou8TextView;
	private TextView namelvyou9TextView;

	public int postion;
	private Bitmap bitmap;
	private TextView moreTextView;
	private final int UPDATE_LIST_VIEW = 1;
	private HashMap<String, Object> hashMap;
	private List<LvYouMmodel> qzModels;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lv_main_new);
		Exit.getInstance().addActivity(this);
		preperImageLoader();
		
		ImageView imageViewback = (ImageView) findViewById(R.id.title_lvyou_back);
		imageViewback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		lvyou1ImageView = (ImageView) findViewById(R.id.imageViewlvyouit1);
		lvyou1ImageView.setTag(0);
		lvyou1ImageView.setOnClickListener(new lvlvyouImageOnClinckListener());
		lvyou2ImageView = (ImageView) findViewById(R.id.imageViewlvyouit2);
		lvyou2ImageView.setTag(1);
		lvyou2ImageView.setOnClickListener(new lvlvyouImageOnClinckListener());
		lvyou3ImageView = (ImageView) findViewById(R.id.imageViewlvyouit3);
		lvyou3ImageView.setTag(2);
		lvyou3ImageView.setOnClickListener(new lvlvyouImageOnClinckListener());
		lvyou4ImageView = (ImageView) findViewById(R.id.imageViewlvyouit4);
		lvyou4ImageView.setTag(3);
		lvyou4ImageView.setOnClickListener(new lvlvyouImageOnClinckListener());
		lvyou5ImageView = (ImageView) findViewById(R.id.imageViewlvyouit5);
		lvyou5ImageView.setTag(4);
		lvyou5ImageView.setOnClickListener(new lvlvyouImageOnClinckListener());
		lvyou6ImageView = (ImageView) findViewById(R.id.imageViewlvyouit6);
		lvyou6ImageView.setTag(5);
		lvyou6ImageView.setOnClickListener(new lvlvyouImageOnClinckListener());
		lvyou7ImageView = (ImageView) findViewById(R.id.imageViewlvyouit7);
		lvyou7ImageView.setTag(6);
		lvyou7ImageView.setOnClickListener(new lvlvyouImageOnClinckListener());
		lvyou8ImageView = (ImageView) findViewById(R.id.imageViewlvyouit8);
		lvyou8ImageView.setTag(7);
		lvyou8ImageView.setOnClickListener(new lvlvyouImageOnClinckListener());
		lvyou9ImageView = (ImageView) findViewById(R.id.imageViewlvyouit9);
		lvyou9ImageView.setTag(8);
		lvyou9ImageView.setOnClickListener(new lvlvyouImageOnClinckListener());

		titlvyou1TextView = (MarqueeTV) findViewById(R.id.textViewlvyouitt1);
		titlvyou2TextView = (MarqueeTV) findViewById(R.id.textViewlvyouitt2);
		titlvyou3TextView = (MarqueeTV) findViewById(R.id.textViewlvyouitt3);
		titlvyou4TextView = (MarqueeTV) findViewById(R.id.textViewlvyouitt4);
		titlvyou5TextView = (MarqueeTV) findViewById(R.id.textViewlvyouitt5);
		titlvyou6TextView = (MarqueeTV) findViewById(R.id.textViewlvyouitt6);
		titlvyou7TextView = (MarqueeTV) findViewById(R.id.textViewlvyouitt7);
		titlvyou8TextView = (MarqueeTV) findViewById(R.id.textViewlvyouitt8);
		titlvyou9TextView = (MarqueeTV) findViewById(R.id.textViewlvyouitt9);

		namelvyou1TextView = (TextView) findViewById(R.id.textViewlvyouty1);
		namelvyou1TextView.setTag(10);
		namelvyou1TextView
				.setOnClickListener(new lvlvyouNameOnClinckListener());
		namelvyou2TextView = (TextView) findViewById(R.id.textViewlvyouty2);
		namelvyou2TextView.setTag(11);
		namelvyou2TextView
				.setOnClickListener(new lvlvyouNameOnClinckListener());
		namelvyou3TextView = (TextView) findViewById(R.id.textViewlvyouty3);
		namelvyou3TextView.setTag(12);
		namelvyou3TextView
				.setOnClickListener(new lvlvyouNameOnClinckListener());
		namelvyou4TextView = (TextView) findViewById(R.id.textViewlvyouty4);
		namelvyou4TextView.setTag(13);
		namelvyou4TextView
				.setOnClickListener(new lvlvyouNameOnClinckListener());
		namelvyou5TextView = (TextView) findViewById(R.id.textViewlvyouty5);
		namelvyou5TextView.setTag(14);
		namelvyou5TextView
				.setOnClickListener(new lvlvyouNameOnClinckListener());
		namelvyou6TextView = (TextView) findViewById(R.id.textViewlvyouty6);
		namelvyou6TextView.setTag(15);
		namelvyou6TextView
				.setOnClickListener(new lvlvyouNameOnClinckListener());
		namelvyou7TextView = (TextView) findViewById(R.id.textViewlvyouty7);
		namelvyou7TextView.setTag(16);
		namelvyou7TextView
				.setOnClickListener(new lvlvyouNameOnClinckListener());
		namelvyou8TextView = (TextView) findViewById(R.id.textViewlvyouty8);
		namelvyou8TextView.setTag(17);
		namelvyou8TextView
				.setOnClickListener(new lvlvyouNameOnClinckListener());
		namelvyou9TextView = (TextView) findViewById(R.id.textViewlvyouty9);
		namelvyou9TextView.setTag(18);
		namelvyou9TextView
				.setOnClickListener(new lvlvyouNameOnClinckListener());
		
		moreTextView = (TextView) findViewById(R.id.textViewlvyou2);
		moreTextView.setOnClickListener(this);
		
		new Thread(){
			public void run(){
				lvyoumainHttp();
			}
			
		}.start();
		
		

	}

	private void preperImageLoader(){
		
		
		/******************* 配置ImageLoder ***********************************************/
		File cacheDir =
					StorageUtils.getOwnCacheDirectory(LvYouMain.this,
								"imageloader/Cache");

		ImageLoaderConfiguration config =
					new ImageLoaderConfiguration.Builder(LvYouMain.this)
								.denyCacheImageMultipleSizesInMemory()
								.discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
								.build();// 开始构建
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory().cacheOnDisc()
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
		
		ImageLoader.getInstance().init(config);// 全局初始化此配置
		/*********************************************************************************/
	}

	
	
	
	private void lvyoumainHttp() {

		String urlString = "http://www.tripg.cn/phone_api/trave/index.php/Travel?title=&PageSize=9&Page=1";
		Tools tools = new Tools();
		String resultString = tools.getURL(urlString);
		Log.e("游轮主视图接口在此获取 返回值", "" + resultString);

		try {

			qzModels = new ArrayList<LvYouMmodel>();
			JSONObject jsonObject = new JSONObject(resultString);
			String codeString = jsonObject.getString("Code");
			if (codeString.equals("1")) {
				JSONObject resjsonObject = jsonObject.getJSONObject("Result");
				JSONArray listjArray = resjsonObject.getJSONArray("list");

				for (int i = 0; i < listjArray.length(); i++) {
					
					LvYouMmodel yMainModel = new LvYouMmodel();
					JSONObject yJsonObject = listjArray.getJSONObject(i);
					yMainModel.lmidString = (String) yJsonObject.getString("id").toString();
					yMainModel.lmtitString = (String) yJsonObject.getString("title").toString();
					yMainModel.lmpriString = (String) yJsonObject.getString("price").toString();
					yMainModel.lmupString = "http://www.tripg.cn/"+ (String)yJsonObject.getString("upload").toString();
					yMainModel.lmteseString = (String) yJsonObject.getString("tese").toString();
					
					qzModels.add(yMainModel);
					
				}
				if (qzModels.size() > 0) {
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

				titlvyou1TextView
						.setText((String) qzModels.get(0).lmtitString);
				namelvyou1TextView
						.setText("￥"+(String) qzModels.get(0).lmpriString);

				ImageLoader.getInstance().displayImage(qzModels.get(0).lmupString, lvyou1ImageView);


				titlvyou2TextView
						.setText((String) qzModels.get(1).lmtitString);
				namelvyou2TextView
						.setText("￥"+(String) qzModels.get(1).lmpriString);

				ImageLoader.getInstance().displayImage(qzModels.get(1).lmupString, lvyou2ImageView);


				titlvyou3TextView
						.setText((String) qzModels.get(2).lmtitString);
				namelvyou3TextView
						.setText("￥"+(String) qzModels.get(2).lmpriString);

				ImageLoader.getInstance().displayImage(qzModels.get(2).lmupString, lvyou3ImageView);

				titlvyou4TextView
						.setText((String) qzModels.get(3).lmtitString);
				namelvyou4TextView
						.setText("￥"+(String) qzModels.get(3).lmpriString);

				ImageLoader.getInstance().displayImage(qzModels.get(3).lmupString, lvyou4ImageView);

				titlvyou5TextView
						.setText((String) qzModels.get(4).lmtitString);
				namelvyou5TextView
						.setText("￥"+(String) qzModels.get(4).lmpriString);

				ImageLoader.getInstance().displayImage(qzModels.get(4).lmupString, lvyou5ImageView);

				titlvyou6TextView
						.setText((String) qzModels.get(5).lmtitString);
				namelvyou6TextView
						.setText("￥"+(String) qzModels.get(5).lmpriString);

				ImageLoader.getInstance().displayImage(qzModels.get(5).lmupString, lvyou6ImageView);

				titlvyou7TextView
						.setText((String) qzModels.get(6).lmtitString);
				namelvyou7TextView
						.setText("￥"+(String) qzModels.get(6).lmpriString);

				ImageLoader.getInstance().displayImage(qzModels.get(6).lmupString, lvyou7ImageView);

				titlvyou8TextView
						.setText((String) qzModels.get(7).lmtitString);
				namelvyou8TextView
						.setText("￥"+(String) qzModels.get(7).lmpriString);

				ImageLoader.getInstance().displayImage(qzModels.get(7).lmupString, lvyou8ImageView);

				titlvyou9TextView
						.setText((String) qzModels.get(8).lmtitString);
				namelvyou9TextView
						.setText("￥"+(String) qzModels.get(8).lmpriString);

				ImageLoader.getInstance().displayImage(qzModels.get(8).lmupString, lvyou9ImageView);

				break;

			default:

				break;
			}
		};
	};

	
	class lvlvyouImageOnClinckListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.e("tag-----", "" + (Integer) v.getTag());
			Intent intent = new Intent(LvYouMain.this, LvYouXiangMain.class);
			intent.putExtra("tid", qzModels.get((Integer) v.getTag()).lmidString);
			startActivity(intent);//
		}

	}

	class lvlvyouNameOnClinckListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.e("tag-----", "" + (Integer) v.getTag());
			Intent intent = new Intent(LvYouMain.this, LvYouXiangMain.class);
			intent.putExtra("tid", qzModels.get(((Integer) v.getTag())-10).lmidString);
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
				Intent intent = new Intent(LvYouMain.this, LvYouLieBiaoMain.class);
				startActivity(intent);
			}
		}, 800);
		
	}

}
