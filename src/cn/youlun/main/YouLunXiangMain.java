package cn.youlun.main;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import cn.internet.Exit;
import cn.internet.Tools;
import cn.lvyou.main.LvYouLieBiaoMain;
import cn.lvyou.main.LvYouMain;
import cn.lvyou.main.LvYouXItemMain;
import cn.model.YouXiangModel;
import cn.model.YouXingModel;
import cn.tripg.R;

public class YouLunXiangMain extends Activity implements OnClickListener {

	private Bitmap bitmap;
	private final int UPDATE_LIST_VIEW = 1;
	public String tidString;
	private ImageView yudingImageView;

	private List<YouXiangModel> list;
	private List<YouXingModel> lxlist;
	private String titleString;
	private String imageUrlString;

	private LinearLayout aBoardmanx;
	private LinearLayout aBoardmanc;

	private ImageView titImageView;
	private TextView titleTextView;
	public int key = 9000;
	private Boolean bool = true;
	public String cangString = "1";
	public String danjiaString ="1";
	
	private ListView cangListView;
	private List<HashMap<String, Object>> listData;
	private static YouLunXiangMain instance;
	private YouLunCang cell;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.youlunxiang_main);
		Exit.getInstance().addActivity(this);
		preperImageLoader();
		
		ImageView imageViewback = (ImageView) findViewById(R.id.title_youlunxiang_back);
		imageViewback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				finish();
			}
		});

		tidString = (String) getIntent().getStringExtra("tid");
		Log.e("tidString----", "" + tidString);

		aBoardmanx = (LinearLayout) findViewById(R.id.a_boardyoulunx);
		//aBoardmanc = (LinearLayout) findViewById(R.id.a_boardyoulunc);

		titImageView = (ImageView) findViewById(R.id.imageViewyoulunx1);
		titleTextView = (TextView) findViewById(R.id.textView1);

		yudingImageView = (ImageView) findViewById(R.id.imageView2);
		yudingImageView.setOnClickListener(new YouXiangtijiao());
		
		instance = this;
		listData = new ArrayList<HashMap<String, Object>>();
		cangListView = (ListView) findViewById(R.id.listViewyoulunlr1);
		
		new Thread(){
			public void run(){
				youlunXiangHttp();
			}
		}.start();
		

	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		//获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter(); 
		if (listAdapter == null) {
		// pre-condition
		return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { //listAdapter.getCount()返回数据项的数目
		View listItem = listAdapter.getView(i, null, listView);
		listItem.measure(0, 0); //计算子项View 的宽高
		totalHeight += listItem.getMeasuredHeight(); //统计所有子项的总高度
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + 130+ (listView.getDividerHeight()  * (listAdapter.getCount() - 1));
		//listView.getDividerHeight()获取子项间分隔符占用的高度
		//params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
		}
	
	
	public void youlunXiangHttp() {

		String urlString = "http://www.tripg.cn/phone_api/trave/index.php/cruise/single?id="
				+ tidString;
		Tools tools = new Tools();
		String resultString = tools.getURL(urlString);
		Log.e("游轮详情接口 urlString---", "" + urlString);
		Log.e("游轮详情接口在此获取 返回值", "" + resultString);

		try {

			JSONObject jsonObject = new JSONObject(resultString);
			String codeString = jsonObject.getString("Code");
			if (codeString.equals("1")) {
				list = new ArrayList<YouXiangModel>();
				lxlist = new ArrayList<YouXingModel>();
				JSONObject resJsonObject = jsonObject.getJSONObject("Result");
				titleString = (String) resJsonObject.getString("r_title");
				imageUrlString = "http://www.tripg.cn/cruise2013/"
						+ (String) resJsonObject.getString("r_listpic");

				JSONArray strArray = resJsonObject.getJSONArray("stroke");
				for (int i = 0; i < strArray.length(); i++) {
					YouXingModel yx = new YouXingModel();
					JSONObject sjJsonObject = strArray.getJSONObject(i);
					yx.yxS_title = (String) sjJsonObject.getString("s_title")
							.toString();
					yx.yxS_Time = (String) sjJsonObject.getString("s_time")
							.toString();
					yx.yxS_Content = (String) sjJsonObject.getString(
							"s_content").toString();
					lxlist.add(yx);
				}

				
				JSONArray roomsArray = resJsonObject.getJSONArray("rooms");
				for (int j = 0; j < roomsArray.length(); j++) {
					JSONObject roomjJsonObject = roomsArray.getJSONObject(j);
					YouXiangModel xm = new YouXiangModel();
					xm.yqNameString = (String) roomjJsonObject.getString("name").toString();
					xm.yqPriceString = (String) roomjJsonObject.getString("price").toString();
					xm.yqContentString = (String) roomjJsonObject.getString(
							"content").toString();
					xm.yqPicString = "http://www.tripg.cn/cruise2013/"
							+ (String) roomjJsonObject.getString("pic")
									.toString();
					list.add(xm);
									
					
				}

				getDongTaiList();

			} else {
				Toast.makeText(getApplicationContext(), "获取数据失败",
						Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getApplicationContext(), "获取数据失败", Toast.LENGTH_LONG)
					.show();
		}

	}
	
	public void getDongTaiList() {

		for (YouXiangModel xml : list) {
			listData.add(getHashMap(xml));

		}
		// 通知刷新界面
		sendHandlerMessage(UPDATE_LIST_VIEW, null);
	}

	private HashMap<String, Object> getHashMap(YouXiangModel youMainModel) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		try {

			hashMap.put("name", youMainModel.yqNameString);
			hashMap.put("price", youMainModel.yqPriceString);
			hashMap.put("content", youMainModel.yqContentString);
			hashMap.put("pic", youMainModel.yqPicString);
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		return hashMap;
	}
	
	private void preperImageLoader(){
		
		
		/******************* 配置ImageLoder ***********************************************/
		File cacheDir =
					StorageUtils.getOwnCacheDirectory(YouLunXiangMain.this,
								"imageloader/Cache");

		ImageLoaderConfiguration config =
					new ImageLoaderConfiguration.Builder(YouLunXiangMain.this)
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

				titleTextView.setText(titleString);
				ImageLoader.getInstance().displayImage(imageUrlString, titImageView);


				if (lxlist.size() > 0) {

					for (int i = 0; i < lxlist.size(); i++) {
						YouXingModel xc = lxlist.get(i);
						LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
						final RelativeLayout rl = (RelativeLayout) inflater
								.inflate(R.layout.boardyoulunitemx, null);
						RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
								RelativeLayout.LayoutParams.MATCH_PARENT,
								RelativeLayout.LayoutParams.WRAP_CONTENT);
						TextView onTextView = (TextView) rl
								.findViewById(R.id.textViewitem1);
						onTextView.setTag(i);
						onTextView.setText(xc.yxS_title);
						onTextView
								.setOnClickListener(YouLunXiangMain.this);
						aBoardmanx.addView(rl, rlp);

					}

				}
				
				if (list.size() > 0) {

					Log.e("cell 数组长度-----", ""+list.size());
					
					cell = new YouLunCang(instance,
							R.layout.boardyoulunc, listData);
					cangListView.setAdapter(cell);
					setListViewHeightBasedOnChildren(cangListView);
					cangListView.setFocusable(false);


				}

				break;

			default:

				break;
			}
		};
	};

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ScrollView sl = (ScrollView)findViewById(R.id.qianzheng_scroll);
		sl.scrollTo(0, 0);
		
		
	}

//	class linearlayOnitemyoulun implements OnClickListener {
//
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			Log.e("tag-----", "" + (Integer) v.getTag());
//			YouXiangModel xcModel = list.get(0);
//			YouXingModel xl = lxlist.get((Integer) v.getTag());
//			Intent intent = new Intent(YouLunXiangMain.this,
//					LvYouXItemMain.class);
//			intent.putExtra("tit", titleString);
//			intent.putExtra("jie", titleString +"。"+xl.yxS_Content);
//			intent.putExtra("pic1", imageUrlString);
//			intent.putExtra("pic2", xcModel.yqPicString);
//			startActivity(intent);
//
//		}
//
//	}

	class YouXiangtijiao implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.e("YouXiangtijiao-----", "YouXiangtijiao");
			
			if (cangString.equals("1")) {
				
				Toast.makeText(getApplicationContext(), "请选择仓位信息!", Toast.LENGTH_LONG)
				.show();
				
			}else {
				
				Intent intent = new Intent(YouLunXiangMain.this,YouLunYuDingMain.class);
				intent.putExtra("cang", cangString);
				intent.putExtra("jia", danjiaString);
				intent.putExtra("title", titleString);
				startActivity(intent);
				
				
				
			}

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
				
				YouXiangModel xcModel = list.get(0);
				YouXingModel xl = lxlist.get((Integer) v.getTag());
				Intent intent = new Intent(YouLunXiangMain.this,
						LvYouXItemMain.class);
				intent.putExtra("tit", titleString);
				intent.putExtra("jie", titleString +"。"+xl.yxS_Content);
				intent.putExtra("pic1", imageUrlString);
				intent.putExtra("pic2", xcModel.yqPicString);
				startActivity(intent);
			}
		}, 800);
	}
	
	
	
}
