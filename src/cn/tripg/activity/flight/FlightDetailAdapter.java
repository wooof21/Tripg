package cn.tripg.activity.flight;

import java.io.Serializable;
import java.util.ArrayList;

import model.flight.CabinVo;
import model.flight.FlightsVo;
import model.flight.ViewCabinHolder;
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
import android.widget.Toast;
import cn.tripg.R;
import cn.tripg.activity.login.LoginActivity;

public class FlightDetailAdapter extends BaseAdapter {

	private LayoutInflater mInflater = null;
	private ArrayList<CabinVo> cabinList;
	private FlightsVo vo;
	private ViewCabinHolder holder;
	private Context context;
	private String type;

	public FlightDetailAdapter(Context myContex,ArrayList<CabinVo> list,
	        String type, FlightsVo vo) {
		this.mInflater = LayoutInflater.from(myContex);
		this.holder = new ViewCabinHolder();
		this.context = myContex;
	    this.cabinList = list;
	    this.type = type;
	    this.vo = vo;
	}

	public int getCount() {
		return cabinList.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}
	public void queryBackTicket(
            String carrier,
            String depCity,
            String arrCity,
            int index){

        Toast.makeText(context, "查询返程机票", Toast.LENGTH_SHORT).show();
        FlightQueryDetailActivity fQueryDActivity =
                                        (FlightQueryDetailActivity)context;
        Intent intent = new Intent(fQueryDActivity,FlightQueryResultActivity.class);
        String flightDate = fQueryDActivity.getFlightDateBack();
        String depCityStr = fQueryDActivity.getArrCityStr();
        String arrCityStr = fQueryDActivity.getDepCityStr();
        Api api = new Api();

        String sign = MD5.appendData(depCity, flightDate);//encode.
        String specialPriceUrl ="http://flightapi.tripglobal.cn:8080/Default.aspx?cmd=floor"
                +"&depCity=" + depCity
                +"&arrCity=" + arrCity
                +"&flightDate=" + flightDate
                +"&days=15&option=D&output=json";
        String url = api.doGetTENRequestURL(
                "?cmd=av&output=json",
                "&filter=1",
                "&depCity=" + depCity,
                "&arrCity=" + arrCity,
                "&carrier=" + carrier,
                "&flightDate=" + flightDate,
                "&officeCode=CGQ182",
                "&flightTime=",
                "&share=0",
                "&sign=" + sign);

        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("cmd", "?cmd=av&output=json");
        bundle.putString("filter", "&filter=1");
        bundle.putString("depCity", depCity);
        bundle.putString("arrCity", arrCity);
        bundle.putString("depCityStr", depCityStr);
        bundle.putString("arrCityStr", arrCityStr);
        bundle.putString("carrier", carrier);
        bundle.putString("flightDate", flightDate);
        bundle.putString("officeCode", "&officeCode=CGQ182");
        bundle.putString("flightTime", "&flightTime=");
        bundle.putString("type", "single");
        bundle.putString("specialPriceUrl", specialPriceUrl);
        intent.putExtra("depVo", (Serializable)vo);
        bundle.putString("index", index + "");
        intent.putExtras(bundle);
        fQueryDActivity.startActivity(intent);

    }
	private boolean isUserLogin(){
        SharedPreferences sharedPre = context.getSharedPreferences(
        		"config", Context.MODE_PRIVATE);
        String username = sharedPre.getString("username", "");
        String password = sharedPre.getString("password", "");
        Log.e("username", username);
        Log.e("password", password);
        if("".equals(username) || "".equals(password)){
        	return false;
        }else{
        	return true;
        }
    }
	public View getView(int position, View convertView, ViewGroup parent) {
		//need optimized convertView setTag getTag
	    final int pos = position;
		if(convertView == null){
		convertView = mInflater.inflate(R.layout.cabin_list_item, null);
		}
		CabinVo cabin = cabinList.get(position);
		holder.buttonBook = (ImageView) convertView.findViewById(R.id.book1);
		holder.buttonBook.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			    if("single".equals(type)){
			        //Toast.makeText(context, "预定cabin index " + pos +"", Toast.LENGTH_SHORT).show();
			        FlightQueryDetailActivity fQueryDActivity =
                                          (FlightQueryDetailActivity)context;
			        if(fQueryDActivity.getDepVo() != null){
			        	if(isUserLogin()){//login state
				    		//jump to book activity
					    	Intent intent = new Intent(context, FillOrderActivity.class);
					    	intent.putExtra("goVo", (Serializable)fQueryDActivity.getDepVo());
					    	intent.putExtra("backVo", (Serializable)vo);
					    	Bundle bundle = new Bundle();
					    	bundle.putString("goCabinIndex", fQueryDActivity.getIndex().toString());
					    	bundle.putString("backCabinIndex", "" + pos);
					    	bundle.putString("depCityStr", ((FlightQueryDetailActivity)context).depCityStr);
					    	bundle.putString("arrCityStr", ((FlightQueryDetailActivity)context).arrCityStr);
					    	bundle.putString("type", "round");
					    	intent.putExtras(bundle);
					    	context.startActivity(intent);
				    	}else{// not login
				    		//jump to login activity.
				    		Intent intent = new Intent(context, LoginActivity.class);
				    		context.startActivity(intent);
				    	}
                        //Log.e("zzz","detail pass 2 vo to book.");
                        //Log.e("back-info", vo.toString());
                        //Log.e(" dep-info", fQueryDActivity.getDepVo().toString());
                        //Log.e("index-back", "" + pos);
                        //Log.e("index-dep ", fQueryDActivity.getIndex().toString());
			        }else{
			            //FlightQueryDetailActivity->Book
			            //pass vo and index pos
			        	if(isUserLogin()){//login state
				    		//jump to book activity
			        		Intent intent = new Intent(context, FillOrderActivity.class);
					    	intent.putExtra("goVo", (Serializable)vo);
					    	Bundle bundle = new Bundle();
					    	bundle.putString("goCabinIndex", "" + pos);
					    	bundle.putString("type", "single");
					    	bundle.putString("depCityStr", ((FlightQueryDetailActivity)context).depCityStr);
					    	bundle.putString("arrCityStr", ((FlightQueryDetailActivity)context).arrCityStr);
					    	intent.putExtras(bundle);
					    	context.startActivity(intent);
				    	}else{//not login
				    		//jump to login activity.
				    		Intent intent = new Intent(context, LoginActivity.class);
				    		context.startActivity(intent);
				    	}
			        	
			            //Log.e("zzz","detail pass 1 vo to book.");
			            //Log.e("debug", vo.toString());
			            //Log.e("index-debug","" + pos );
			        }
			    }
			    else{
			        //Toast.makeText(context, "查询返程机票", Toast.LENGTH_SHORT).show();
			        queryBackTicket(vo.getCarrier(),vo.getArrCity(),vo.getDepCity(),pos);
			    }
			}
		});
		holder.ticketPrice = (TextView)convertView.findViewById(R.id.price1);
		String price = "￥" + cabin.getSinglePrice().replace(".00", "");
		holder.ticketPrice.setText(price);
		holder.ticketStatus = (TextView)convertView.findViewById(R.id.status22);
		String status = ("A".equals(cabin.getTicketStatus()))?"充足":cabin.getTicketStatus() + "张";
		holder.ticketStatus.setText(status);
		String cabinName = cabin.getName();
		String discount = cabin.getDiscount();
		if (cabin.getDiscount().equals("") || cabin.getDiscount().equals("N"))
			discount = "无";
		else{
			double ca = Double.valueOf((String)cabin.getDiscount().toString());
			double bca = Double.valueOf(ca/10);
			Log.e("折扣显示具体信息 ----", ""+bca);
			if (bca >= 10) {
				discount =  "全价";
				holder.cabinTypeDiscount = (TextView)convertView.findViewById(R.id.line11);
				holder.cabinTypeDiscount.setText(status + " " + cabinName
						+ " " + discount);
			}else {
				discount = ""+ bca  + "折";
				holder.cabinTypeDiscount = (TextView)convertView.findViewById(R.id.line11);
				holder.cabinTypeDiscount.setText(status + " " + cabinName
						+ " " + discount);
			}
			
		}
		return convertView;
	}
}
