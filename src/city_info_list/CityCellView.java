package city_info_list;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import cn.tripg.R;

public class CityCellView extends ArrayAdapter<HashMap<String, Object>> {
	private List<HashMap<String, Object>> listData;
	private Context context;
	public CityCellView(Context context, int textViewResourceId,
			List<HashMap<String, Object>> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.listData = objects;
	}
	
	@Override
	public int getCount() {
		System.out.println("list size:"+ listData.size());
		return listData.size();
	}

	@Override
	public HashMap<String, Object> getItem(int position) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater mInflater = LayoutInflater.from(context);
		convertView = mInflater.inflate(R.layout.city_xml_item1, null);

		TextView textView = (TextView) convertView.findViewById(R.id.textView1);
		textView.setText((String) listData.get(position).get("name"));
		
		return convertView;
	}
}
