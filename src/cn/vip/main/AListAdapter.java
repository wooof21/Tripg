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

public class AListAdapter extends BaseAdapter{
	
	private Context context;
	private ArrayList<HashMap<String, String>> list;
	private LayoutInflater inflater;
	

	public AListAdapter(Context context, ArrayList<HashMap<String, String>> list) {
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
			convertView = inflater.inflate(R.layout.address_item, null);
		}
		
		TextView address = (TextView)convertView.findViewById(R.id.ai_address);
		HashMap<String, String> hashMap = list.get(position);
		
		address.setText(hashMap.get("province").toString()
				+ hashMap.get("city").toString()
				+ hashMap.get("area").toString()
				+ hashMap.get("address").toString()
				+ " ” ±‡:"
				+ hashMap.get("code").toString());
		
		return convertView;
	}

}
