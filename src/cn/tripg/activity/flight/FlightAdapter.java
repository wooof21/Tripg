package cn.tripg.activity.flight;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import model.flight.CabinVo;
import model.flight.FlightsVo;
import model.flight.ViewHolder;
import tools.des.Api;
import tools.des.MD5;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.tripg.R;
import cn.tripg.activity.login.LoginActivity;

public class FlightAdapter extends BaseAdapter {

	private LayoutInflater mInflater = null;
	private ArrayList<FlightsVo> flightList;
	private ViewHolder holder;
	private Context context;
	private String type;
	private FlightsVo depVo;
	private String index;
	private HashMap<Integer, View> hashMap;

	public FlightAdapter(Context myContex, ArrayList<FlightsVo> list,
			String type) {
		this.mInflater = LayoutInflater.from(myContex);
		this.holder = new ViewHolder();
		this.context = myContex;
		this.flightList = list;
		this.type = type;
		this.hashMap = new HashMap<Integer, View>();
	}

	public void queryBackTicket(String carrier, String depCity, String arrCity) {

		FlightQueryResultActivity fQueryRActivity = (FlightQueryResultActivity) context;
		Intent intent = new Intent(fQueryRActivity,
				FlightQueryResultActivity.class);
		String flightDate = fQueryRActivity.getFlightDateBack();
		String depCityStr = fQueryRActivity.getArrCityStr();
		String arrCityStr = fQueryRActivity.getDepCityStr();
		Api api = new Api();

		String sign = MD5.appendData(depCity, flightDate);// 
		String specialPriceUrl = "http://flightapi.tripglobal.cn:8080/Default.aspx?cmd=floor"
				+ "&depCity=" + depCity + "&arrCity=" + arrCity
				+ "&flightDate=" + fQueryRActivity.flightDateBack() + "&days=15&option=D&output=json";
		String url = api.doGetTENRequestURL("?cmd=av&output=json", "&filter=1",
				"&depCity=" + depCity, "&arrCity=" + arrCity, "&carrier="
						+ carrier, "&flightDate=" + fQueryRActivity.flightDateBack(),
				"&officeCode=CGQ182", "&flightTime=", "&share=0", "&sign="
						+ sign);
		Bundle bundle = new Bundle();
		bundle.putString("url", url);
		bundle.putString("cmd", "?cmd=av&output=json");
		bundle.putString("filter", "&filter=1");
		bundle.putString("depCity", depCity);
		bundle.putString("arrCity", arrCity);
		bundle.putString("depCityStr", depCityStr);
		bundle.putString("arrCityStr", arrCityStr);
		bundle.putString("carrier", carrier);
		bundle.putString("flightDate", fQueryRActivity.flightDateBack());
		bundle.putString("officeCode", "&officeCode=CGQ182");
		bundle.putString("flightTime", "&flightTime=");
		bundle.putString("type", "single");
		bundle.putString("specialPriceUrl", specialPriceUrl);
		intent.putExtra("depVo", (Serializable) depVo);
		bundle.putString("index", index);
		intent.putExtras(bundle);
		fQueryRActivity.startActivity(intent);

	}

	public int getCount() {
		return flightList.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	private boolean isUserLogin() {
		SharedPreferences sharedPre = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		// sharedPre.edit().clear();
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

	public View getView(int position, View convertView, ViewGroup parent) {
		// need optimized convertView setTag getTag
//		if (hashMap.get(position) == null) {
			convertView = mInflater.inflate(R.layout.flight_query_list_item,
					null);
			holder.depTimeArrTimeStop3 = (TextView) convertView
					.findViewById(R.id.line1);
			holder.depTowerArrTower2 = (TextView) convertView
					.findViewById(R.id.line2);
			holder.flightNo = (TextView) convertView.findViewById(R.id.line3);
			holder.ticketPrice = (TextView) convertView
					.findViewById(R.id.price);
			holder.ticketStatusCabinNameDiscount3 = (TextView) convertView
					.findViewById(R.id.bellow);
			holder.isTextView = (TextView) convertView.findViewById(R.id.gognxiangtext);
			holder.buttonBook = (ImageView) convertView.findViewById(R.id.book);
			
			hashMap.put(position, convertView);
//		} else {
//			return (View) hashMap.get(position);
//		}

		final FlightsVo vo = flightList.get(position);
		CabinVo cabin = vo.getCabins().get(0);// get the first cabin by default.//
		// 共享航班判断
		String isShareString = (String) vo.getIsShare().toString();
		System.out.printf("共享航班-----参数值", "---"+isShareString);
		if (isShareString.equals("1")) {

			holder.isTextView.setVisibility(View.VISIBLE);
		}

		holder.buttonBook.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ("single".equals(type)) {
					// Toast.makeText(context, "预定cabin index 0",
					// Toast.LENGTH_SHORT).show();
					FlightQueryResultActivity fQueryRActivity = (FlightQueryResultActivity) context;
					if (fQueryRActivity.getDepVo() != null) {
						// check user login info
						if (isUserLogin()) {// login state
							// jump to book activity
							Intent intent = new Intent(context,
									FillOrderActivity.class);
							intent.putExtra("goVo",
									(Serializable) fQueryRActivity.getDepVo());
							intent.putExtra("backVo", (Serializable) vo);
							Bundle bundle = new Bundle();
							bundle.putString("goCabinIndex", fQueryRActivity
									.getIndex().toString());
							bundle.putString(
									"depCityStr",
									((FlightQueryResultActivity) context).depCityStr);
							bundle.putString(
									"arrCityStr",
									((FlightQueryResultActivity) context).arrCityStr);
							bundle.putString("backCabinIndex", "0");
							bundle.putString("type", "round");
							intent.putExtras(bundle);
							context.startActivity(intent);
						} else {// not login
								// jump to login activity.
							Intent intent = new Intent(context,
									LoginActivity.class);
							context.startActivity(intent);
						}

						// Log.e("zzz","pass 2 vo to book.");
						// Log.e("back-info", vo.toString());
						// Log.e("index-back", "" + 0);
						// Log.e(" dep-info",
						// fQueryRActivity.getDepVo().toString());
						// Log.e("index-dep",
						// fQueryRActivity.getIndex().toString());
						// Intent intent = new Intent(context,
						// FillOrderActivity.class);
					} else {
						// FlightQueryResultActivity->Book
						// pass vo and index 0
						// jump to book activity
						if (isUserLogin()) {// login state
							// jump to book activity
							Intent intent = new Intent(context,
									FillOrderActivity.class);
							intent.putExtra("goVo", (Serializable) vo);
							Bundle bundle = new Bundle();
							bundle.putString("goCabinIndex", "0");
							bundle.putString("type", "single");
							bundle.putString(
									"depCityStr",
									((FlightQueryResultActivity) context).depCityStr);
							bundle.putString(
									"arrCityStr",
									((FlightQueryResultActivity) context).arrCityStr);
							intent.putExtras(bundle);
							context.startActivity(intent);
						} else {// not login
								// jump to login activity.
							Intent intent = new Intent(context,
									LoginActivity.class);
							context.startActivity(intent);
						}

						// Log.e("zzz","pass 1 vo to book.");
						// Log.e("index-dep", "" + 0);
						// Log.e("debug", vo.toString());
					}
				} else {
					// Toast.makeText(context, "查询返程机票",
					// Toast.LENGTH_SHORT).show();
					depVo = vo;
					index = "0";
					queryBackTicket(vo.getCarrier(), vo.getArrCity(),
							vo.getDepCity());
				}
			}
		});

		String depTime = vo.getDepTime().substring(0, 2).trim() + ":"
				+ vo.getDepTime().substring(2, 4);
		String arrTime = vo.getArrTime().substring(0, 2).trim() + ":"
				+ vo.getArrTime().substring(2, 4);
		String stop = ("1".equals(vo.getStop())) ? "经停" : "终点";
		holder.depTimeArrTimeStop3.setText(depTime + "-" + arrTime);

		String depTower = vo.getDepCityReferred();
		String arrTower = vo.getArrCityReferred();
		holder.depTowerArrTower2.setText(depTower + "-" + arrTower);

		String flightNoAndSoOn = vo.getFlightNo();
		String flightNoRef = vo.getCarrierReferred();
		holder.flightNo.setText(flightNoRef + flightNoAndSoOn);

		String price = "￥" + cabin.getSinglePrice().replace(".00", "");
		holder.ticketPrice.setText(price);

		String status = ("A".equals(cabin.getTicketStatus())) ? "充足" : cabin
				.getTicketStatus() + "张";
		String cabinName = cabin.getName();
		String discount = cabin.getDiscount();
		if (cabin.getDiscount().equals("") || cabin.getDiscount().equals("N"))
			discount = "无折扣";
		else{
			double ca = Double.valueOf((String)cabin.getDiscount().toString());
			double bca = Double.valueOf(ca/10);
			Log.e("折扣显示具体信息 ----", ""+bca);
			if (bca >= 10) {
				discount =  "全价";
				holder.ticketStatusCabinNameDiscount3.setText(status + " " + cabinName
						+ " " + discount);
			}else {
				discount = ""+ bca  + "折";
				holder.ticketStatusCabinNameDiscount3.setText(status + " " + cabinName
						+ " " + discount);
			}
			
		}
		return convertView;
	}
}
