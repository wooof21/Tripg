package cn.tripg.activity.hotel;

import java.util.ArrayList;

import model.hotel.CDResults;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.tripg.R;

public class CdAdapter extends BaseAdapter{

	private ArrayList<CDResults> src;
	private Context context;
	public CdAdapter(Context context, ArrayList<CDResults> src){
		this.src = src;
		this.context = context;
	}
	@Override
	public int getCount() {
		return this.src.size();
	}

	@Override
	public Object getItem(int position) {
		return this.getItem(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			LayoutInflater lf = (LayoutInflater) 
					context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = lf.inflate(R.layout.item_id_type, null);
			TextView tv = ((TextView)convertView);
			tv.setText(this.src.get(position).Name);
		}
		return convertView;
	}

}
