package cn.tripg.activity.hotel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;



import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.tripg.R;
import cn.tripg.activity.login.LoginActivity;

public class HotelCell extends ArrayAdapter<HashMap<String, Object>> {

	private List<HashMap<String, Object>> listData;
	private Context context;
	private Bitmap bitmap;
//	private ViewHolder viewHolder;
	private ImageView imageButton;
//	private OnTouch onTouchListener;	
	private final int TYPE_COUNT=2;  
    private final int FIRST_TYPE=0;  
    private final int OTHERS_TYPE=1;  
    private int currentType; 
    private int dayCount;
    private double k;
 //   private HashMap<String, Object> hashMap;


	public HotelCell(Context context, int resource,
			List<HashMap<String, Object>> objects) {
		super(context, resource, objects);
		this.context = context;
		this.listData = objects;
		preperImageLoader();
		
		for(int i=0;i<listData.size();i++){
			Log.e("1.listData", "" + listData.get(i));
		}
	}
	@Override
	public int getCount() {
		Log.e("listData.size() is ", "" + listData.size());
		return listData.size()+1;
	}

	@Override
	public HashMap<String, Object> getItem(int position) {
//		if(position > 0){
//			return listData.get(position-1);
//		}else{
//			return listData.get(position+1);
//		}
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

	@Override 
    public int getViewTypeCount() {  
        return TYPE_COUNT;  
    }  
       
    @Override 
    public int getItemViewType(int position) {  
        if (position==0) {  
            return FIRST_TYPE;  
        } else {  
            return OTHERS_TYPE;  
        }  
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
	
	private boolean isUserLogin() {
		SharedPreferences sharedPre = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		// 清除数据
		// sharedPre.edit().clear();
		// sharedPre.edit().commit();
		// 存储数据
		// sharedPre.edit().putString("", "");
		// sharedPre.edit().commit();
		String username = sharedPre.getString("username", "");
		String password = sharedPre.getString("password", "");
		Log.e("username", "A" + username);
		Log.e("password", "B" + password);
		if ("".equals(username) || "".equals(password)) {
			return false;
		} else {
			return true;
		}
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		//LayoutInflater mInflater = LayoutInflater.from(context);
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		View firstItemView = null;  
        View othersItemView=null; 
        
     //   onTouchListener = new OnTouch();

        
        currentType= getItemViewType(position);  
        System.out.println("type="+currentType);
		
	//	viewHolder = new ViewHolder();

		//hashMap = listData.get(position);
		System.out.println("position ----> " + position);
		
		if (currentType== FIRST_TYPE) {  
			firstItemView = convertView;  
            FirstItemViewHolder firstItemViewHolder;  
 //           if (firstItemView==null) {  
            	firstItemView = mInflater.inflate(
						R.layout.hotelcellitemorder, null);
            	firstItemViewHolder = new FirstItemViewHolder();
            	firstItemViewHolder.textView = (TextView) firstItemView
						.findViewById(R.id.textViewhotelonecell1);
            	firstItemViewHolder.textView2 = (TextView) firstItemView
						.findViewById(R.id.textViewhotelonecell2);
            	firstItemViewHolder.textView3 = (TextView) firstItemView
						.findViewById(R.id.textViewhotelonecell3);
            	firstItemViewHolder.starImageView = (ImageView) firstItemView
						.findViewById(R.id.imageViewhotelstar);
            	firstItemViewHolder.cellImage = (ImageView) firstItemView
						.findViewById(R.id.imageViewhotelonecell1);
//            	firstItemView.setTag(firstItemViewHolder);
 //           }else{
 //           	firstItemViewHolder = (FirstItemViewHolder) firstItemView.getTag();
//            }
            firstItemViewHolder.textView.setText((String) listData.get(0)
					.get("HotelName"));
			System.out.println("HotelName*****"
					+ (String) listData.get(0).get("HotelName"));

			firstItemViewHolder.textView2.setText((String) listData.get(0)
					.get("HotelAddress"));

			firstItemViewHolder.textView3.setText((String) listData.get(0).get("Phone"));
			String starString = (String) listData.get(0).get("StarCode");

			if (starString.equals("5")) {
				firstItemViewHolder.starImageView
						.setBackgroundResource(R.drawable.star5);
			}
			if (starString.equals("2")) {
				firstItemViewHolder.starImageView
						.setBackgroundResource(R.drawable.star2);
			}
			if (starString.equals("3")) {
				firstItemViewHolder.starImageView
						.setBackgroundResource(R.drawable.star3);
			}
			if (starString.equals("4")) {
				firstItemViewHolder.starImageView
						.setBackgroundResource(R.drawable.star4);
			}


//			Log.e("imageurl****",
//					(String) listData.get(0).get("OutdoorSceneImage"));
			
			final String imageURL = (String) listData.get(0)
					.get("OutdoorSceneImage");
			
			ImageLoader.getInstance().displayImage(imageURL, firstItemViewHolder.cellImage);
            convertView=firstItemView;  
            
		}else if(currentType== OTHERS_TYPE){
			othersItemView = convertView;  
            OthersViewHolder othersViewHolder; 
 //           if(othersItemView==null){
            	othersItemView = mInflater.inflate(
						R.layout.hotelcellitemordertwo, null);
            	othersViewHolder = new OthersViewHolder();
            	othersViewHolder.textView1 = (TextView) othersItemView.findViewById(R.id.textViewhotelitemtwo1);
            	othersViewHolder.textView22 = (TextView) othersItemView
						.findViewById(R.id.textViewhotelitemtwo2);
            	othersViewHolder.textView33 = (TextView) othersItemView
						.findViewById(R.id.textViewhotelitemtwo3);
            	othersViewHolder.textView4 = (TextView) othersItemView
						.findViewById(R.id.textViewhotelitemtwo4);
            	othersViewHolder.imageViewdanbao = (ImageView) othersItemView
						.findViewById(R.id.imageViewhotelitem3);
				// 在cell中绑定预订按钮的点击事件
				imageButton = (ImageView) othersItemView
						.findViewById(R.id.imageViewhotelitem2);
//				othersItemView.setTag(othersViewHolder);
  //          }else{
 //           	othersViewHolder=(OthersViewHolder) othersItemView.getTag();
 //           }

            //listData.get(position -1)

			othersViewHolder.textView1.setText((String) listData.get(position-1)
					.get("RoomName"));
			othersViewHolder.textView22.setText((String) listData.get(position-1)
					.get("BedDescription"));

			othersViewHolder.textView33.setText((String) listData.get(position-1)
					.get("RatePlanName"));
			
			String liveTime = (String) listData.get(position-1).get("liveTime");
			String leaveTime = (String) listData.get(position-1).get("leaveTime");
			
			System.out.println("time ----> " + liveTime + "    " + leaveTime);
	/*************************************************************************************************/		
			/*
			 * calculate Data difference
			 */
//			dayCount = (Integer) listData.get(0).get("dayCount");
//			System.out.println("dayCount ---> " + dayCount);
			String garKey = (String)listData.get(0).get("GarKey");
			Log.e("garKey ----", garKey);
			
			if (garKey.equals("1")) {
				Log.e("GarKey ---- 1", "1");
					othersViewHolder.imageViewdanbao
							.setBackgroundResource(R.drawable.danbao);

			}else{
				othersViewHolder.imageViewdanbao.setVisibility(View.INVISIBLE);
			}
	/*************************************************************************************************/	
			
			String sp = (String) listData.get(position-1).get("TotalPrice");
			double i = Double.valueOf(sp).doubleValue();
		    k = i;
			//int m = Integer.parseInt(new java.text.DecimalFormat("0").format(k));
			//int m = Integer.parseInt(String.valueOf(k));
			othersViewHolder.textView4.setText("￥" + (int)k);
			
			System.out.println("2.j ---> " + k);		

				//viewHolder.imageViewdanbao.getDrawable().setLevel(0);
				//othersViewHolder.imageViewdanbao.setImageResource(R.drawable.danbao);
				imageButton.setImageResource(R.drawable.bookbutton);
		        //imageButton.setOnTouchListener(onTouchListener);	
				imageButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Log.e("position ====> ", ""+position);
						
						for(int i=0;i<listData.size();i++){
							Log.e("listData -------> ", ""+listData.get(i));
						}
						Log.e("listData ======> ", ""+listData.get(position-1));
						
						if (isUserLogin()) {
							Intent intent = new Intent(context,
									HotelOrderYuDingMain.class);
//							if(position > 1){
//								hashMap = listData.get(position-1);
//							}
							
							Bundle bundle = new Bundle();
							bundle.putString("cityId", (String)listData.get(position-1).get("cityId"));
							System.out.println("cityId ------------> " + (String)listData.get(position-1).get("cityId"));
							bundle.putString("RoomName",
									(String) listData.get(position-1).get("RoomName"));
							bundle.putString("RoomTypeId",
									(String) listData.get(position-1).get("RoomTypeId"));
							bundle.putString("Area",
									(String) listData.get(position-1).get("Area"));
							bundle.putString("RoomInvStatusCode",
									(String) listData.get(position-1)
											.get("RoomInvStatusCode"));
							bundle.putString("BedDescription",
									(String) listData.get(position-1)
											.get("BedDescription"));
							bundle.putString("BedType",
									(String) listData.get(position-1).get("BedType"));
							bundle.putString("HasBroadBand",
									(String) listData.get(position-1)
											.get("HasBroadBand"));
							bundle.putString("HasBroadBandFee",
									(String) listData.get(position-1)
											.get("HasBroadBandFee"));
							bundle.putString("Floor",
									(String) listData.get(position-1).get("Floor"));
							bundle.putString("HotelName",
									(String) listData.get(position-1).get("HotelName"));
							bundle.putString("HotelAddress",
									(String) listData.get(position-1)
											.get("HotelAddress"));
							bundle.putString("HotelId",
									(String) listData.get(position-1).get("HotelId"));
							bundle.putString("HotelInvStatusCode",
									(String) listData.get(position-1)
											.get("HotelInvStatusCode"));
							bundle.putString("HotelSource",
									(String) listData.get(position-1).get("HotelSource"));
							bundle.putString("StarCode",
									(String) listData.get(position-1).get("StarCode"));
							bundle.putString("Lat",
									(String) listData.get(position-1).get("Lat"));
							bundle.putString("Lon",
									(String) listData.get(position-1).get("Lon"));
							bundle.putString("LowestPrice",
									(String) listData.get(position-1).get("LowestPrice"));
							bundle.putString("Phone",
									(String) listData.get(position-1).get("Phone"));
							bundle.putString("OutdoorSceneImage",
									(String) listData.get(position-1)
											.get("OutdoorSceneImage"));
							bundle.putString("TotalPrice",
									(String) listData.get(position-1).get("TotalPrice"));
							bundle.putString("RatePlanName",
									(String) listData.get(position-1)
											.get("RatePlanName"));
							bundle.putString("RatePlanID",
									(String) listData.get(position-1).get("RatePlanID"));
							bundle.putString("RatePlanCode",
									(String) listData.get(position-1)
											.get("RatePlanCode"));
							bundle.putString("GarKey",
									(String) listData.get(position-1).get("GarKey"));
							//bundle.putInt("dayCount", dayCount);
							bundle.putString(
									"leaveTime",
									((HotelOrderActivity) context).leaveTimeString);
							bundle.putString(
									"liveTime",
									((HotelOrderActivity) context).liveTimeString);
							intent.putExtras(bundle);
							context.startActivity(intent);

						} else {// not login

							Intent intent = new Intent(context,
									LoginActivity.class);
							context.startActivity(intent);
						}
						
					}
				});
		        
			
/*************************************************************************************************/					
//			if (listData.get(0).get("GarKey").equals("1")) {
//				Log.e("GarKey ---- 1", "1");
//				String garsString = (String) listData.get(0)
//						.get("GaranteeRulesTypeCode");
//				Log.e("garsString", garsString);
//				othersViewHolder.imageViewdanbao = (ImageView) othersItemView
//						.findViewById(R.id.imageViewhotelitem3);
////				if (garsString.equals("1")) {
////					othersViewHolder.imageViewdanbao
////							.setBackgroundResource(R.drawable.danbao);
////				}
//			}
/**************************************************************************************************/
			
			

//			imageButton.setOnTouchListener(new OnTouchListener() {
//
//				@Override
//				public boolean onTouch(View v, MotionEvent event) {
//					if (event.getAction() == MotionEvent.ACTION_DOWN) {
//						Log.e("cellview button", "down");
//						if (isUserLogin()) {
//							Intent intent = new Intent(context,
//									HotelOrderYuDingMain.class);
//							Bundle bundle = new Bundle();
//							bundle.putString("RoomName",
//									(String) hashMap.get("RoomName"));
//							bundle.putString("RoomTypeId",
//									(String) hashMap.get("RoomTypeId"));
//							bundle.putString("Area",
//									(String) hashMap.get("Area"));
//							bundle.putString("RoomInvStatusCode",
//									(String) hashMap
//											.get("RoomInvStatusCode"));
//							bundle.putString("BedDescription",
//									(String) hashMap
//											.get("BedDescription"));
//							bundle.putString("BedType",
//									(String) hashMap.get("BedType"));
//							bundle.putString("HasBroadBand",
//									(String) hashMap
//											.get("HasBroadBand"));
//							bundle.putString("HasBroadBandFee",
//									(String) hashMap
//											.get("HasBroadBandFee"));
//							bundle.putString("Floor",
//									(String) hashMap.get("Floor"));
//							bundle.putString("HotelName",
//									(String) hashMap.get("HotelName"));
//							bundle.putString("HotelAddress",
//									(String) hashMap
//											.get("HotelAddress"));
//							bundle.putString("HotelId",
//									(String) hashMap.get("HotelId"));
//							bundle.putString("HotelInvStatusCode",
//									(String) hashMap
//											.get("HotelInvStatusCode"));
//							bundle.putString("HotelSource",
//									(String) hashMap.get("HotelSource"));
//							bundle.putString("StarCode",
//									(String) hashMap.get("StarCode"));
//							bundle.putString("Lat",
//									(String) hashMap.get("Lat"));
//							bundle.putString("Lon",
//									(String) hashMap.get("Lon"));
//							bundle.putString("LowestPrice",
//									(String) hashMap.get("LowestPrice"));
//							bundle.putString("Phone",
//									(String) hashMap.get("Phone"));
//							bundle.putString("OutdoorSceneImage",
//									(String) hashMap
//											.get("OutdoorSceneImage"));
//							bundle.putString("TotalPrice",
//									(String) hashMap.get("TotalPrice"));
//							bundle.putString("RatePlanName",
//									(String) hashMap
//											.get("RatePlanName"));
//							bundle.putString("RatePlanID",
//									(String) hashMap.get("RatePlanID"));
//							bundle.putString("RatePlanCode",
//									(String) hashMap
//											.get("RatePlanCode"));
//							bundle.putString("GarKey",
//									(String) hashMap.get("GarKey"));
//							bundle.putString(
//									"leaveTime",
//									((HotelOrderActivity) context).leaveTimeString);
//							bundle.putString(
//									"liveTime",
//									((HotelOrderActivity) context).liveTimeString);
//							intent.putExtras(bundle);
//							context.startActivity(intent);
//
//						} else {// not login
//
//							Intent intent = new Intent(context,
//									LoginActivity.class);
//							context.startActivity(intent);
//						}
//					}
//					return false;
//				}
//			});
            convertView = othersItemView;  
		}
		
//		if (listData.size() != position) {
//
//			final HashMap<String, Object> hashMap = listData.get(position);
//
//			if (convertView == null ) {
//				if(position == 0){
//					convertView = mInflater.inflate(
//							R.layout.hotelcellitemorder, null);
//					viewHolder.textView = (TextView) convertView
//							.findViewById(R.id.textViewhotelonecell1);
//					viewHolder.textView2 = (TextView) convertView
//							.findViewById(R.id.textViewhotelonecell2);
//					viewHolder.textView3 = (TextView) convertView
//							.findViewById(R.id.textViewhotelonecell3);
//					viewHolder.starImageView = (ImageView) convertView
//							.findViewById(R.id.imageViewhotelstar);
//					viewHolder.cellImage = (ImageView) convertView
//							.findViewById(R.id.imageViewhotelonecell1);
//					convertView.setTag(viewHolder);
//					
//					
//					viewHolder.textView.setText((String) hashMap
//							.get("HotelName"));
//					System.out.println("HotelName*****"
//							+ (String) hashMap.get("HotelName"));
//
//					viewHolder.textView2.setText((String) hashMap
//							.get("HotelAddress"));
//
//					viewHolder.textView3.setText((String) hashMap.get("Phone") + "position" + position);
//					String starString = (String) hashMap.get("StarCode");
//
//					if (starString.equals("5")) {
//						viewHolder.starImageView
//								.setBackgroundResource(R.drawable.star5);
//					}
//					if (starString.equals("2")) {
//						viewHolder.starImageView
//								.setBackgroundResource(R.drawable.star2);
//					}
//					if (starString.equals("3")) {
//						viewHolder.starImageView
//								.setBackgroundResource(R.drawable.star3);
//					}
//					if (starString.equals("4")) {
//						viewHolder.starImageView
//								.setBackgroundResource(R.drawable.star4);
//					}
//
//
//					Log.e("imageurl****",
//							(String) hashMap.get("OutdoorSceneImage"));
//					final String imageURL = (String) hashMap
//							.get("OutdoorSceneImage");
//					ImageLoader.getInstance().displayImage(imageURL, viewHolder.cellImage);
//
//
//			
//				}else{
//					Log.e("position", "not equal to zero");
//					convertView = mInflater.inflate(
//							R.layout.hotelcellitemordertwo, null);
//					viewHolder.textView = (TextView) convertView
//							.findViewById(R.id.textViewhotelitemtwo1);
//					viewHolder.textView2 = (TextView) convertView
//							.findViewById(R.id.textViewhotelitemtwo2);
//					viewHolder.textView3 = (TextView) convertView
//							.findViewById(R.id.textViewhotelitemtwo3);
//					viewHolder.textView4 = (TextView) convertView
//							.findViewById(R.id.textViewhotelitemtwo4);
//					viewHolder.imageViewdanbao = (ImageView) convertView
//							.findViewById(R.id.imageViewhotelitem3);
//					// 在cell中绑定预订按钮的点击事件
//					imageButton = (ImageView) convertView
//							.findViewById(R.id.imageViewhotelitem2);
//					convertView.setTag(viewHolder);
//					
//
//
//					viewHolder.textView.setText((String) hashMap
//							.get("RoomName") + "position" + position);
//					viewHolder.textView2.setText((String) hashMap
//							.get("BedDescription"));
//
//					viewHolder.textView3.setText((String) hashMap
//							.get("RatePlanName"));
//					String sp = (String) hashMap.get("TotalPrice");
//					double i = Double.valueOf(sp).doubleValue();
//					int j = (int) i;
//
//					viewHolder.textView4.setText("￥" + j);
//					
//					System.out.println("2.j ---> " + j);		
//					if(j <= 399){
//						//viewHolder.imageViewdanbao.getDrawable().setLevel(0);
//						viewHolder.imageViewdanbao.setImageResource(R.drawable.danbao);
//					}else{
//						//viewHolder.imageViewdanbao.getDrawable().setLevel(1);
//						viewHolder.imageViewdanbao.setImageResource(R.drawable.sj);
//					}
//	/*************************************************************************************************/					
//					if (hashMap.get("GarKey").equals("1")) {
//						String garsString = (String) hashMap
//								.get("GaranteeRulesTypeCode");
//						viewHolder.imageViewdanbao = (ImageView) convertView
//								.findViewById(R.id.imageViewhotelitem3);
//						if (garsString.equals("1")) {
//							viewHolder.imageViewdanbao
//									.setBackgroundResource(R.drawable.danbao);
//						}
//					}
//	/**************************************************************************************************/
//					
//					
//
//					imageButton.setOnTouchListener(new OnTouchListener() {
//
//						@Override
//						public boolean onTouch(View v, MotionEvent event) {
//							if (event.getAction() == MotionEvent.ACTION_DOWN) {
//								Log.e("cellview button", "down");
//								if (isUserLogin()) {
//									Intent intent = new Intent(context,
//											HotelOrderYuDingMain.class);
//									Bundle bundle = new Bundle();
//									bundle.putString("RoomName",
//											(String) hashMap.get("RoomName"));
//									bundle.putString("RoomTypeId",
//											(String) hashMap.get("RoomTypeId"));
//									bundle.putString("Area",
//											(String) hashMap.get("Area"));
//									bundle.putString("RoomInvStatusCode",
//											(String) hashMap
//													.get("RoomInvStatusCode"));
//									bundle.putString("BedDescription",
//											(String) hashMap
//													.get("BedDescription"));
//									bundle.putString("BedType",
//											(String) hashMap.get("BedType"));
//									bundle.putString("HasBroadBand",
//											(String) hashMap
//													.get("HasBroadBand"));
//									bundle.putString("HasBroadBandFee",
//											(String) hashMap
//													.get("HasBroadBandFee"));
//									bundle.putString("Floor",
//											(String) hashMap.get("Floor"));
//									bundle.putString("HotelName",
//											(String) hashMap.get("HotelName"));
//									bundle.putString("HotelAddress",
//											(String) hashMap
//													.get("HotelAddress"));
//									bundle.putString("HotelId",
//											(String) hashMap.get("HotelId"));
//									bundle.putString("HotelInvStatusCode",
//											(String) hashMap
//													.get("HotelInvStatusCode"));
//									bundle.putString("HotelSource",
//											(String) hashMap.get("HotelSource"));
//									bundle.putString("StarCode",
//											(String) hashMap.get("StarCode"));
//									bundle.putString("Lat",
//											(String) hashMap.get("Lat"));
//									bundle.putString("Lon",
//											(String) hashMap.get("Lon"));
//									bundle.putString("LowestPrice",
//											(String) hashMap.get("LowestPrice"));
//									bundle.putString("Phone",
//											(String) hashMap.get("Phone"));
//									bundle.putString("OutdoorSceneImage",
//											(String) hashMap
//													.get("OutdoorSceneImage"));
//									bundle.putString("TotalPrice",
//											(String) hashMap.get("TotalPrice"));
//									bundle.putString("RatePlanName",
//											(String) hashMap
//													.get("RatePlanName"));
//									bundle.putString("RatePlanID",
//											(String) hashMap.get("RatePlanID"));
//									bundle.putString("RatePlanCode",
//											(String) hashMap
//													.get("RatePlanCode"));
//									bundle.putString("GarKey",
//											(String) hashMap.get("GarKey"));
//									bundle.putString(
//											"leaveTime",
//											((HotelOrderActivity) context).leaveTimeString);
//									bundle.putString(
//											"liveTime",
//											((HotelOrderActivity) context).liveTimeString);
//									intent.putExtras(bundle);
//									context.startActivity(intent);
//
//								} else {// not login
//
//									Intent intent = new Intent(context,
//											LoginActivity.class);
//									context.startActivity(intent);
//								}
//							}
//							return false;
//						}
//					});
//				
//			
//				}
//			}else{
//				viewHolder = (ViewHolder)convertView.getTag();
//			}
//			
//		}
		return convertView;
	}

//	protected class OnTouch implements OnTouchListener{
//
//		
//		@Override
//		public boolean onTouch(View v, MotionEvent event){
//			// TODO Auto-generated method stub
//			if (event.getAction() == MotionEvent.ACTION_DOWN) {
//				Log.e("cellview button", "down");
//				
//				if (isUserLogin()) {
//					Intent intent = new Intent(context,
//							hotel_reason_activity.class);
//					
//					Bundle bundle = new Bundle();
//					bundle.putString("cityId", (String)hashMap.get("cityId"));
//					System.out.println("cityId ------------> " + (String)hashMap.get("cityId"));
//					bundle.putString("RoomName",
//							(String) hashMap.get("RoomName"));
//					bundle.putString("RoomTypeId",
//							(String) hashMap.get("RoomTypeId"));
//					bundle.putString("Area",
//							(String) hashMap.get("Area"));
//					bundle.putString("RoomInvStatusCode",
//							(String) hashMap
//									.get("RoomInvStatusCode"));
//					bundle.putString("BedDescription",
//							(String) hashMap
//									.get("BedDescription"));
//					bundle.putString("BedType",
//							(String) hashMap.get("BedType"));
//					bundle.putString("HasBroadBand",
//							(String) hashMap
//									.get("HasBroadBand"));
//					bundle.putString("HasBroadBandFee",
//							(String) hashMap
//									.get("HasBroadBandFee"));
//					bundle.putString("Floor",
//							(String) hashMap.get("Floor"));
//					bundle.putString("HotelName",
//							(String) hashMap.get("HotelName"));
//					bundle.putString("HotelAddress",
//							(String) hashMap
//									.get("HotelAddress"));
//					bundle.putString("HotelId",
//							(String) hashMap.get("HotelId"));
//					bundle.putString("HotelInvStatusCode",
//							(String) hashMap
//									.get("HotelInvStatusCode"));
//					bundle.putString("HotelSource",
//							(String) hashMap.get("HotelSource"));
//					bundle.putString("StarCode",
//							(String) hashMap.get("StarCode"));
//					bundle.putString("Lat",
//							(String) hashMap.get("Lat"));
//					bundle.putString("Lon",
//							(String) hashMap.get("Lon"));
//					bundle.putString("LowestPrice",
//							(String) hashMap.get("LowestPrice"));
//					bundle.putString("Phone",
//							(String) hashMap.get("Phone"));
//					bundle.putString("OutdoorSceneImage",
//							(String) hashMap
//									.get("OutdoorSceneImage"));
//					bundle.putString("TotalPrice",
//							(String) hashMap.get("TotalPrice"));
//					bundle.putString("RatePlanName",
//							(String) hashMap
//									.get("RatePlanName"));
//					bundle.putString("RatePlanID",
//							(String) hashMap.get("RatePlanID"));
//					bundle.putString("RatePlanCode",
//							(String) hashMap
//									.get("RatePlanCode"));
//					bundle.putString("GarKey",
//							(String) hashMap.get("GarKey"));
//					bundle.putInt("dayCount", dayCount);
//					bundle.putString(
//							"leaveTime",
//							((HotelOrderActivity) context).leaveTimeString);
//					bundle.putString(
//							"liveTime",
//							((HotelOrderActivity) context).liveTimeString);
//					intent.putExtras(bundle);
//					context.startActivity(intent);
//
//				} else {// not login
//
//					Intent intent = new Intent(context,
//							LoginActivity.class);
//					context.startActivity(intent);
//				}
//			}
//			return false;
//		}
//		
//	}
//	class LoadHotelImage extends Thread {
//		private String imgUrl;
//		private ViewHolder viewHolder;
//
//		public LoadHotelImage(ViewHolder holder, String url) {
//			this.viewHolder = holder;
//			this.imgUrl = url;
//		}
//
//		final Handler handler = new Handler() {
//			public void handleMessage(Message msg) {
//				viewHolder.cellImage.setImageBitmap(bitmap);
//			}
//		};
//
//		@Override
//		public void run() {
//			try {
//				URL url = new URL(imgUrl);
//				HttpURLConnection conn = (HttpURLConnection) url
//						.openConnection();
//				InputStream inputStream = conn.getInputStream();
//				bitmap = BitmapFactory.decodeStream(inputStream);
//			} catch (MalformedURLException e1) {
//				e1.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			handler.sendEmptyMessage(0);
//			if (bitmap != null) {
//				this.viewHolder.isImageLoaded = true;
//			}
//		}
//	}

	 //第一个Item的ViewHolder  
    private class FirstItemViewHolder{  
		public ImageView cellImage;
		public TextView textView;
		public TextView textView2;
		public TextView textView3; 
		public ImageView starImageView;
    }  
       
    //除第一个Item以外其余Item的ViewHolder  
    private class OthersViewHolder{  
		public ImageView imageViewdanbao;
		public TextView textView1;
		public TextView textView22;
		public TextView textView33;
		public TextView textView4;
    }  
    
//	class ViewHolder {
//		public ImageView cellImage;
//		public TextView textView;
//		public TextView textView2;
//		public TextView textView3;
//		public TextView textView4;
//		public ImageView starImageView;
//		public ImageView imageViewdanbao;
//		public boolean isImageLoaded = false;
//	}

}