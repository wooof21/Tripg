package cn.vip.tsv;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import cn.tripg.R;
import cn.tripg.xlistview.MarqueeTV;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TSVListViewAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<HashMap<String, String>> list;
	private String type;
	
	
	
	public TSVListViewAdapter(Context context,
			ArrayList<HashMap<String, String>> list, String type) {
		this.context = context;
		this.list = list;
		this.type = type;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();	
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" }) 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			LayoutInflater lInflater = LayoutInflater.from(context);
			convertView = lInflater.inflate(R.layout.tsv_item, null);
		}
		
		TextView orderDate = (TextView) convertView
				.findViewById(R.id.tsv_order_data);
		TextView orderStatus = (TextView) convertView
				.findViewById(R.id.tsv_order_status);
		TextView change = (TextView) convertView
				.findViewById(R.id.tsv_order_main_change);
		MarqueeTV changeText = (MarqueeTV) convertView
				.findViewById(R.id.tsv_order_main_change_type);
		TextView count = (TextView) convertView
				.findViewById(R.id.tsv_order_amount);
		TextView total = (TextView) convertView
				.findViewById(R.id.tsv_order_price);
		TextView orderNo = (TextView) convertView
				.findViewById(R.id.tsv_order_no);
		TextView changeText1 = (TextView) convertView
				.findViewById(R.id.tsv_item_change_text1);
		
		
		HashMap<String, String> hashMap = list.get(position);
		if(type.equalsIgnoreCase("T")){
			changeText1.setText("出发日期:");
			orderDate.setText(hashMap.get("go_date"));
			orderStatus.setText("新订单");
			change.setText("产品名称:");
			changeText.setText(hashMap.get("title"));
			count.setText(hashMap.get("count"));
			total.setText("￥"+hashMap.get("total"));
			orderNo.setText(hashMap.get("id"));
		}else if(type.equalsIgnoreCase("S")){
			changeText1.setText("出航日期:");
			orderStatus.setText("新订单");
			change.setText("舱        位:");
			changeText.setText(hashMap.get("cangwei"));
			count.setText("1");
			orderNo.setText(hashMap.get("id"));
			String dt = hashMap.get("datetime");
			long time = Long.parseLong(dt) * 1000L;
			Log.e("time", ""+time);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date d = new Date(time);
			Log.e("dt", sdf.format(d));
			orderDate.setText(sdf.format(d));
			total.setVisibility(View.INVISIBLE);
		}else if(type.equalsIgnoreCase("V")){
			String[] date = hashMap.get("addtime").split(" ");		
			changeText1.setText("下单日期:");
			orderDate.setText(date[0]);
			if(hashMap.get("orderStatus").equals("1")){
				orderStatus.setText("新订单");
			}
			count.setText(hashMap.get("count"));
			total.setText("￥"+hashMap.get("price"));
			orderNo.setText(hashMap.get("id"));
			changeText.setText(hashMap.get("country") + " / " + hashMap.get("typename"));
		}
		
		
		
		return convertView;
	}

}
