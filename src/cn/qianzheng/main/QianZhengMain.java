package cn.qianzheng.main;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.internet.Exit;
import cn.internet.Tools;
import cn.model.QianZhengModel;
import cn.tripg.R;

public class QianZhengMain extends Activity implements OnClickListener {

	private ImageView qian1ImageView;
	private ImageView qian2ImageView;
	private ImageView qian3ImageView;
	private ImageView qian4ImageView;
	private ImageView qian5ImageView;
	private ImageView qian6ImageView;
	private ImageView qian7ImageView;
	private ImageView qian8ImageView;
	private ImageView qian9ImageView;

	private TextView titqian1TextView;
	private TextView titqian2TextView;
	private TextView titqian3TextView;
	private TextView titqian4TextView;
	private TextView titqian5TextView;
	private TextView titqian6TextView;
	private TextView titqian7TextView;
	private TextView titqian8TextView;
	private TextView titqian9TextView;

	private TextView nameqian1TextView;
	private TextView nameqian2TextView;
	private TextView nameqian3TextView;
	private TextView nameqian4TextView;
	private TextView nameqian5TextView;
	private TextView nameqian6TextView;
	private TextView nameqian7TextView;
	private TextView nameqian8TextView;
	private TextView nameqian9TextView;

	private TextView picqian1TextView;
	private TextView picqian2TextView;
	private TextView picqian3TextView;
	private TextView picqian4TextView;
	private TextView picqian5TextView;
	private TextView picqian6TextView;
	private TextView picqian7TextView;
	private TextView picqian8TextView;
	private TextView picqian9TextView;

	private EditText sousuoEditText;
	private ImageView sousuoImageView;
	private TextView moreTextView;
	public int postion;
	private Bitmap bitmap;
	private List<QianZhengModel> qzModels;
	private final int UPDATE_LIST_VIEW = 1;
	private HashMap<String, Object> hashMap;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qianzheng_main);
		Exit.getInstance().addActivity(this);
		preperImageLoader();
		ImageView imageViewback = (ImageView) findViewById(R.id.title_qianzheng_back);
		imageViewback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				finish();
			}
		});

		qian1ImageView = (ImageView) findViewById(R.id.imageViewqianzhengit1);
		qian1ImageView.setTag(0);
		qian1ImageView.setOnClickListener(new QianzhengImageOnClinckListener());
		qian2ImageView = (ImageView) findViewById(R.id.imageViewqianzhengit2);
		qian2ImageView.setTag(1);
		qian2ImageView.setOnClickListener(new QianzhengImageOnClinckListener());
		qian3ImageView = (ImageView) findViewById(R.id.imageViewqianzhengit3);
		qian3ImageView.setTag(2);
		qian3ImageView.setOnClickListener(new QianzhengImageOnClinckListener());
		qian4ImageView = (ImageView) findViewById(R.id.imageViewqianzhengit4);
		qian4ImageView.setTag(3);
		qian4ImageView.setOnClickListener(new QianzhengImageOnClinckListener());
		qian5ImageView = (ImageView) findViewById(R.id.imageViewqianzhengit5);
		qian5ImageView.setTag(4);
		qian5ImageView.setOnClickListener(new QianzhengImageOnClinckListener());
		qian6ImageView = (ImageView) findViewById(R.id.imageViewqianzhengit6);
		qian6ImageView.setTag(5);
		qian6ImageView.setOnClickListener(new QianzhengImageOnClinckListener());
		qian7ImageView = (ImageView) findViewById(R.id.imageViewqianzhengit7);
		qian7ImageView.setTag(6);
		qian7ImageView.setOnClickListener(new QianzhengImageOnClinckListener());
		qian8ImageView = (ImageView) findViewById(R.id.imageViewqianzhengit8);
		qian8ImageView.setTag(7);
		qian8ImageView.setOnClickListener(new QianzhengImageOnClinckListener());
		qian9ImageView = (ImageView) findViewById(R.id.imageViewqianzhengit9);
		qian9ImageView.setTag(8);
		qian9ImageView.setOnClickListener(new QianzhengImageOnClinckListener());

		titqian1TextView = (TextView) findViewById(R.id.textViewqianzhengitt1);
		titqian2TextView = (TextView) findViewById(R.id.textViewqianzhengitt2);
		titqian3TextView = (TextView) findViewById(R.id.textViewqianzhengitt3);
		titqian4TextView = (TextView) findViewById(R.id.textViewqianzhengitt4);
		titqian5TextView = (TextView) findViewById(R.id.textViewqianzhengitt5);
		titqian6TextView = (TextView) findViewById(R.id.textViewqianzhengitt6);
		titqian7TextView = (TextView) findViewById(R.id.textViewqianzhengitt7);
		titqian8TextView = (TextView) findViewById(R.id.textViewqianzhengitt8);
		titqian9TextView = (TextView) findViewById(R.id.textViewqianzhengitt9);

		nameqian1TextView = (TextView) findViewById(R.id.textViewqianzhengty1);
		nameqian1TextView.setTag(10);
		nameqian1TextView
				.setOnClickListener(new QianzhengNameOnClinckListener());
		nameqian2TextView = (TextView) findViewById(R.id.textViewqianzhengty2);
		nameqian2TextView.setTag(11);
		nameqian2TextView
				.setOnClickListener(new QianzhengNameOnClinckListener());
		nameqian3TextView = (TextView) findViewById(R.id.textViewqianzhengty3);
		nameqian3TextView.setTag(12);
		nameqian3TextView
				.setOnClickListener(new QianzhengNameOnClinckListener());
		nameqian4TextView = (TextView) findViewById(R.id.textViewqianzhengty4);
		nameqian4TextView.setTag(13);
		nameqian4TextView
				.setOnClickListener(new QianzhengNameOnClinckListener());
		nameqian5TextView = (TextView) findViewById(R.id.textViewqianzhengty5);
		nameqian5TextView.setTag(14);
		nameqian5TextView
				.setOnClickListener(new QianzhengNameOnClinckListener());
		nameqian6TextView = (TextView) findViewById(R.id.textViewqianzhengty6);
		nameqian6TextView.setTag(15);
		nameqian6TextView
				.setOnClickListener(new QianzhengNameOnClinckListener());
		nameqian7TextView = (TextView) findViewById(R.id.textViewqianzhengty7);
		nameqian7TextView.setTag(16);
		nameqian7TextView
				.setOnClickListener(new QianzhengNameOnClinckListener());
		nameqian8TextView = (TextView) findViewById(R.id.textViewqianzhengty8);
		nameqian8TextView.setTag(17);
		nameqian8TextView
				.setOnClickListener(new QianzhengNameOnClinckListener());
		nameqian9TextView = (TextView) findViewById(R.id.textViewqianzhengty9);
		nameqian9TextView.setTag(18);
		nameqian9TextView
				.setOnClickListener(new QianzhengNameOnClinckListener());

		picqian1TextView = (TextView) findViewById(R.id.textViewqianzhengpir1);
		picqian2TextView = (TextView) findViewById(R.id.textViewqianzhengpir2);
		picqian3TextView = (TextView) findViewById(R.id.textViewqianzhengpir3);
		picqian4TextView = (TextView) findViewById(R.id.textViewqianzhengpir4);
		picqian5TextView = (TextView) findViewById(R.id.textViewqianzhengpir5);
		picqian6TextView = (TextView) findViewById(R.id.textViewqianzhengpir6);
		picqian7TextView = (TextView) findViewById(R.id.textViewqianzhengpir7);
		picqian8TextView = (TextView) findViewById(R.id.textViewqianzhengpir8);
		picqian9TextView = (TextView) findViewById(R.id.textViewqianzhengpir9);

		sousuoEditText = (EditText) findViewById(R.id.editText1);
		sousuoImageView = (ImageView) findViewById(R.id.imageViewqianzhengsou1);
		sousuoImageView
				.setOnClickListener(new QianzhengSousuoOnClinckListener());
		moreTextView = (TextView) findViewById(R.id.textViewqianzheng2);
		moreTextView.setOnClickListener(this);

		int k = 0;
		for (int row = 0; row < 15; row++) {

			if (row != 0) {
				if (row % 4 == 0) {
					k++;
				}
			}

			int y = row - (k + k * 3);
			if (y >= 0) {
				Log.e("k-----", "" + k);
				Log.e("y----", "" + y);
			}
		}

		new Thread() {

			public void run() {
				QianzhengHttp();
				countryHttp();
			}

		}.start();

		
		
//		QianzhengHttp();
//		countryHttp();
	}

	private void countryHttp() {

		String urlString = "http://www.tripg.cn/phone_api/trave/index.php/visa/get_Country";
		Log.e("urlString--", urlString);
		Tools tools = new Tools();
		String resultString = tools.getURL(urlString);
		Log.e("签证国家接口在此获取 返回值", "" + resultString);
		hashMap = new HashMap<String, Object>();

		try {

			JSONArray jsonArray = new JSONArray(resultString);
			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject jsonObject = jsonArray.getJSONObject(i);
				hashMap.put((String) jsonObject.getString("CountryName")
						.toString(), (String) jsonObject.getString("Id")
						.toString());
				Log.e("json ----",
						"key --"
								+ (String) jsonObject.getString("CountryName")
										.toString()
								+ "----value"
								+ (String) jsonObject.getString("Id")
										.toString());

			}

		} catch (Exception e) {
			// TODO: handle exception
			Log.e("Exception", "---" + e);

		}

	}

	private void QianzhengHttp() {
		String urlString = "http://www.tripg.cn/phone_api/trave/index.php/visa/get_Visas?cid=&limit=9";
		Log.e("urlString--", urlString);
		Tools tools = new Tools();
		String resultString = tools.getURL(urlString);
		Log.e("签证主视图接口在此获取 返回值", "" + resultString);

		try {
			qzModels = new ArrayList<QianZhengModel>();
			JSONArray jsaArray = new JSONArray(resultString);
			for (int i = 0; i < jsaArray.length(); i++) {
				QianZhengModel qz = new QianZhengModel();
				JSONObject jsonObject = jsaArray.getJSONObject(i);
				qz.productIdString = (String) jsonObject.getString("ProductId")
						.toString();
				qz.countryNameString = (String) jsonObject.getString(
						"CountryName").toString();
				qz.typeNameString = (String) jsonObject.getString("TypeName")
						.toString();
				qz.sellPriceString = (String) jsonObject.getString("SellPrice")
						.toString();
				qz.imageUrlString = "http://210.72.225.98:8080/"+(String) jsonObject.getString("Flag")
						.toString();

				qzModels.add(qz);
			}
			if (qzModels.size() > 0) {
				// 通知刷新界面
				sendHandlerMessage(UPDATE_LIST_VIEW, null);
				
			}

		} catch (Exception e) {
			// TODO: handle exception
			Log.e("Exception", "---" + e);
		}

	}
	
	private void preperImageLoader(){
		
		
		/******************* 配置ImageLoder ***********************************************/
		File cacheDir =
					StorageUtils.getOwnCacheDirectory(QianZhengMain.this,
								"imageloader/Cache");

		ImageLoaderConfiguration config =
					new ImageLoaderConfiguration.Builder(QianZhengMain.this)
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

				titqian1TextView
						.setText((String) qzModels.get(0).countryNameString);
				nameqian1TextView
						.setText((String) qzModels.get(0).typeNameString);
				picqian1TextView
						.setText((String) qzModels.get(0).sellPriceString);
				
				ImageLoader.getInstance().displayImage(qzModels.get(0).imageUrlString, qian1ImageView);


				titqian2TextView
						.setText((String) qzModels.get(1).countryNameString);
				nameqian2TextView
						.setText((String) qzModels.get(1).typeNameString);
				picqian2TextView
						.setText((String) qzModels.get(1).sellPriceString);
				ImageLoader.getInstance().displayImage(qzModels.get(1).imageUrlString, qian2ImageView);


				titqian3TextView
						.setText((String) qzModels.get(2).countryNameString);
				nameqian3TextView
						.setText((String) qzModels.get(2).typeNameString);
				picqian3TextView
						.setText((String) qzModels.get(2).sellPriceString);
				ImageLoader.getInstance().displayImage(qzModels.get(2).imageUrlString, qian3ImageView);

				
				titqian4TextView
						.setText((String) qzModels.get(3).countryNameString);
				nameqian4TextView
						.setText((String) qzModels.get(3).typeNameString);
				picqian4TextView
						.setText((String) qzModels.get(3).sellPriceString);
				ImageLoader.getInstance().displayImage(qzModels.get(3).imageUrlString, qian4ImageView);

				titqian5TextView
						.setText((String) qzModels.get(4).countryNameString);
				nameqian5TextView
						.setText((String) qzModels.get(4).typeNameString);
				picqian5TextView
						.setText((String) qzModels.get(4).sellPriceString);
				ImageLoader.getInstance().displayImage(qzModels.get(4).imageUrlString, qian5ImageView);

				titqian6TextView
						.setText((String) qzModels.get(5).countryNameString);
				nameqian6TextView
						.setText((String) qzModels.get(5).typeNameString);
				picqian6TextView
						.setText((String) qzModels.get(5).sellPriceString);
				ImageLoader.getInstance().displayImage(qzModels.get(5).imageUrlString, qian6ImageView);

				titqian7TextView
						.setText((String) qzModels.get(6).countryNameString);
				nameqian7TextView
						.setText((String) qzModels.get(6).typeNameString);
				picqian7TextView
						.setText((String) qzModels.get(6).sellPriceString);
				ImageLoader.getInstance().displayImage(qzModels.get(6).imageUrlString, qian7ImageView);

				titqian8TextView
						.setText((String) qzModels.get(7).countryNameString);
				nameqian8TextView
						.setText((String) qzModels.get(7).typeNameString);
				picqian8TextView
						.setText((String) qzModels.get(7).sellPriceString);
				ImageLoader.getInstance().displayImage(qzModels.get(7).imageUrlString, qian8ImageView);

				titqian9TextView
						.setText((String) qzModels.get(8).countryNameString);
				nameqian9TextView
						.setText((String) qzModels.get(8).typeNameString);
				picqian9TextView
						.setText((String) qzModels.get(8).sellPriceString);
				ImageLoader.getInstance().displayImage(qzModels.get(8).imageUrlString, qian9ImageView);

				break;

			default:

				break;
			}
		};
	};

	class QianzhengImageOnClinckListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.e("tag-----", "" + (Integer) v.getTag());
			String idString = (String) qzModels.get((Integer) v.getTag()).productIdString;
			Intent intent = new Intent(QianZhengMain.this, QianXiangMain.class);
			intent.putExtra("id", idString);
			startActivity(intent);
		}

	}

	class QianzhengNameOnClinckListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.e("tag-----", "" + (Integer) v.getTag());
			String idString = (String) qzModels.get((Integer) v.getTag()).productIdString;
			Intent intent = new Intent(QianZhengMain.this, QianXiangMain.class);
			intent.putExtra("id", idString);
			startActivity(intent);

		}

	}

	class QianzhengSousuoOnClinckListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(sousuoEditText.getText().toString().length() == 0){
				Toast.makeText(QianZhengMain.this, "请输入国家名称!", Toast.LENGTH_LONG).show();
			}else{
				Log.e("sousuo----", "" + sousuoEditText.getText());
				String countryidString = (String) hashMap
						.get((String) sousuoEditText.getText().toString());
				Log.e("获取国家id----", "" + countryidString);
				if(countryidString == null){
					Toast.makeText(QianZhengMain.this, "无此签证国家,请重新选择!", Toast.LENGTH_LONG).show();
					
				}else{
					Intent intent = new Intent(QianZhengMain.this,
							QianZhengLieBiao.class);
					intent.putExtra("id", countryidString);
					
					startActivity(intent);
				}
				
			}

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
				Intent intent = new Intent(QianZhengMain.this,
						QianZhengLieBiao.class);
				intent.putExtra("id", "0");
				startActivity(intent);
			}
		}, 800);
	}

}