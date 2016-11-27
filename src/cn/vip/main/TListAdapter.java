package cn.vip.main;

import java.util.ArrayList;
import java.util.HashMap;

import cn.tripg.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TListAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<HashMap<String, String>> list;
	private LayoutInflater inflater;
	
	public TListAdapter(Context context, ArrayList<HashMap<String, String>> list) {
		super();
		this.context = context;
		this.list = list;
		
		inflater = LayoutInflater.from(context);
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			convertView = inflater.inflate(R.layout.traveler_item, null);
		}
		HashMap<String, String> hashMap = list.get(position);
		
		TextView name = (TextView)convertView.findViewById(R.id.ti_name);
		TextView type_id = (TextView)convertView.findViewById(R.id.ti_id);
		TextView birthday = (TextView)convertView.findViewById(R.id.ti_birthday);
		TextView sex = (TextView)convertView.findViewById(R.id.ti_sex);
		
		name.setText(hashMap.get("name").toString());
		type_id.setText(hashMap.get("idType").toString() + ":" + hashMap.get("idNo").toString());
		//birthday.setText(hashMap.get("Birthday").toString());
		//sex.setText("ÐÔ±ð:" + hashMap.get("sex").toString());
		
		return convertView;
	}

}
