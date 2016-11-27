package cn.tripg.activity.hotel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import model.hotel.Room;
import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import cn.dongtai.main.DongCellView;
import cn.internet.Exit;
import cn.model.XmlCityModel;
import cn.tripg.R;
import cn.tripg.activity.flight.ResultCode;

public class HotelOrderActivity extends Activity implements OnItemClickListener {

	
	public ArrayList<Room> roomsAry;
	public ImageView imageBack;
	public ListView listView;
	public HotelOrderActivity hotelOrderActivity;
	private List<HashMap<String, Object>> listData;
	public String hotelNameString;
	public String hotelAddressString;
	public String hotelIdString;
	public String hotelInvStatusCodeString;
	public String hotelSourceString;
	public String starCodesString;
	public String latsString;
	public String lonsString;
	public String lowestPricesString;
	public String phonesString;
	public String outdoorSceneImagesString;
	public String totalPricesString;
	public String liveTimeString;
	public String leaveTimeString;
	
	
	private final int UPDATE_LIST_VIEW = 1;
	

	
	@SuppressWarnings("unchecked")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hotelordermain);
		Exit.getInstance().addActivity(this);
		@SuppressWarnings("unused")
		
		Intent intent = getIntent();
		intent.getExtras().getString("type");
		roomsAry = (ArrayList<Room>)getIntent().getSerializableExtra("rooms");
//		Log.e("roomlist RoomName", roomsAry.get(0).RoomName);
//		Log.e("roomlist RoomTypeId", roomsAry.get(0).RoomTypeId);
//		Log.e("roomlist Area", roomsAry.get(0).Area);
//		Log.e("roomlist RoomInvStatusCode", roomsAry.get(0).RoomInvStatusCode);
//		Log.e("roomlist BedDescription", roomsAry.get(0).BedDescription);
//		Log.e("roomlist BedType", roomsAry.get(0).BedType);
//		Log.e("roomlist HasBroadBand", roomsAry.get(0).HasBroadBand);
//		Log.e("roomlist HasBroadBandFee", roomsAry.get(0).HasBroadBandFee);
//		Log.e("roomlist Floor", roomsAry.get(0).Floor);
		
		hotelNameString = (String)intent.getExtras().getString("HotelName");
//		Log.e("HotelName", intent.getExtras().getString("HotelName"));
		hotelAddressString = (String)intent.getExtras().getString("HotelAddress");
//		Log.e("HotelAddress", intent.getExtras().getString("HotelAddress"));
		hotelIdString = (String)intent.getExtras().getString("HotelId");
//		Log.e("HotelId", intent.getExtras().getString("HotelId"));
		hotelInvStatusCodeString = (String)intent.getExtras().getString("HotelInvStatusCode");
//		Log.e("HotelInvStatusCode", intent.getExtras().getString("HotelInvStatusCode"));
		hotelSourceString = (String)intent.getExtras().getString("HotelSource");
//		Log.e("HotelSource", intent.getExtras().getString("HotelSource"));
		starCodesString = (String)intent.getExtras().getString("StarCode");
//		Log.e("StarCode", intent.getExtras().getString("StarCode"));
		latsString = (String)intent.getExtras().getString("Lat");
//		Log.e("Lat", intent.getExtras().getString("Lat"));
		lonsString = (String)intent.getExtras().getString("Lon");
//		Log.e("Lon", intent.getExtras().getString("Lon"));
		lowestPricesString = (String)intent.getExtras().getString("LowestPrice");
//		Log.e("LowestPrice", intent.getExtras().getString("LowestPrice"));
		phonesString = (String)intent.getExtras().getString("Phone");
//		Log.e("Phone", intent.getExtras().getString("Phone"));
		outdoorSceneImagesString = (String)intent.getExtras().getString("OutdoorSceneImage");
		leaveTimeString = (String)intent.getExtras().getString("leaveTime");
		liveTimeString = (String)intent.getExtras().getString("liveTime");
		hotelOrderActivity = this;
		
		
		imageBack = (ImageView)findViewById(R.id.title_back);
		imageBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = getIntent();
				setResult(ResultCode.FAILURE, intent);
				finish();
				
			}
		});
		
		listData = new ArrayList<HashMap<String,Object>>();
		listView = (ListView)findViewById(R.id.listViewdainjiudain);
		hotelListData();
	}
	
	public void hotelListData(){
		
		for (int i = 0; i < roomsAry.size(); i++) {
			Room jsonObject = roomsAry.get(i);
			Log.e("***___++++++  Room", jsonObject.RoomName);
			
			listData.add(getHashMap(jsonObject));
			Log.e("listData=======", ""+listData.size());
		}
		
		//通知刷新界面
				sendHandlerMessage(UPDATE_LIST_VIEW, null);
	}
	
	private void sendHandlerMessage(int what, Object obj) {
		Log.e("listview", "if 3");
		Message msg = new Message();
		msg.what = what;
		Log.e("listview", "if 3  1");
		handler.sendMessage(msg);
	}
	
	Handler handler = new Handler() {
		/**
		 * @param handleMessage
		 * 	刷新listView的数据
		 * 
		 * */
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_LIST_VIEW:
				
				HotelCell cell = new HotelCell(hotelOrderActivity,
						R.layout.hotelordermain, listData);			
				listView.setAdapter(cell);
				listView.setOnItemClickListener(hotelOrderActivity);
				
				break;

			default:
				
				break;
			}
		};
	};
	
	
	private HashMap<String, Object> getHashMap(Room room) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("RoomName", room.RoomName);
		hashMap.put("RoomTypeId", room.RoomTypeId);
		hashMap.put("Area", room.Area);
		hashMap.put("RoomInvStatusCode", room.RoomInvStatusCode);
		hashMap.put("BedDescription", room.BedDescription);
		hashMap.put("BedType", room.BedType);
		hashMap.put("HasBroadBand", room.HasBroadBand);
		hashMap.put("HasBroadBandFee", room.HasBroadBandFee);
		hashMap.put("Floor", room.Floor);
		hashMap.put("HotelName", hotelNameString);
		hashMap.put("HotelAddress", hotelAddressString);
		hashMap.put("HotelId", hotelIdString);
		hashMap.put("HotelInvStatusCode", hotelInvStatusCodeString);
		hashMap.put("HotelSource", hotelSourceString);
		hashMap.put("StarCode", starCodesString);
		hashMap.put("Lat", latsString);
		hashMap.put("Lon", lonsString);
		hashMap.put("LowestPrice", lowestPricesString);
		hashMap.put("Phone", phonesString);
		hashMap.put("OutdoorSceneImage", outdoorSceneImagesString);
		hashMap.put("TotalPrice", room.RatePlans.get(0).Rates.TotalPrice);
		Log.e("TotalPrice", ""+room.RatePlans.get(0).Rates.TotalPrice);
		hashMap.put("RatePlanName", room.RatePlans.get(0).RatePlanName);
		hashMap.put("RatePlanID", room.RatePlans.get(0).RatePlanID);
		hashMap.put("RatePlanCode", room.RatePlans.get(0).RatePlanCode);
		
		
		if (room.RatePlans.get(0).GaranteeRules.size() != 0 ) {
			hashMap.put("GarKey", "1");
			hashMap.put("GaranteeRulesTypeCode", room.RatePlans.get(0).GaranteeRules.get(0).GaranteeRulesTypeCode);
			Log.e("GaranteeRulesTypeCode", ""+room.RatePlans.get(0).GaranteeRules.get(0).GaranteeRulesTypeCode);
		}else {
			hashMap.put("GarKey", "0");
		}
		
		
		return hashMap;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Log.e("listview  item", "****item"+arg2);
		if (arg2 != 0) {
			HashMap<String, Object> hashMap = listData.get(arg2);
			
		}
		
	}
	
	
	
	
}
