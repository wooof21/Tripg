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

public class CListAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<HashMap<String, String>> list;
	private LayoutInflater inflater;
	
	public CListAdapter(Context context, ArrayList<HashMap<String, String>> list) {
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
			convertView = inflater.inflate(R.layout.contacter_item, null);
		}
		
		TextView name = (TextView)convertView.findViewById(R.id.ci_name);
		TextView code = (TextView)convertView.findViewById(R.id.ci_area_code);
		TextView phone = (TextView)convertView.findViewById(R.id.ci_phone);
		
		HashMap<String, String> hashMap = list.get(position);
		
		name.setText(hashMap.get("name").toString());
		code.setText(hashMap.get("areaCode").toString());
		phone.setText(hashMap.get("phone").toString());
		
		
		return convertView;
	}

}
