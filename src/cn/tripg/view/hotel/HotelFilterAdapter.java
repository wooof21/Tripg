package cn.tripg.view.hotel;

import java.util.ArrayList;

import cn.tripg.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HotelFilterAdapter extends BaseAdapter{

	private ArrayList<String> areaList;
	private Context context;
	private LayoutInflater mInflater = null;
	public HotelFilterAdapter(Context context, int viewId, ArrayList<String> areaList){
		this.mInflater = LayoutInflater.from(context);
		this.context = context;
		this.areaList = areaList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return areaList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ViewHolder areaHolder = new ViewHolder();
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.hotel_filter_item, null);
			areaHolder.areaView = (TextView)convertView.findViewById(R.id.hotel_filter_item);
			convertView.setTag(areaHolder);
		}else{
			areaHolder = (ViewHolder)convertView.getTag();
		}
		areaHolder.areaView.setText(this.areaList.get(position));
		
		return convertView;
	}

	class ViewHolder{
		public TextView areaView;
	}
}
