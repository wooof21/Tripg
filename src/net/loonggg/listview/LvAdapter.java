
package net.loonggg.listview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.tripg.R;
import cn.tripg.activity.newhotels.HotelOrder;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LvAdapter extends BaseAdapter {
	private ArrayList<HotelOrder> list;
	private Context context;
	public HashMap<String, Object> osHashMap;
	private int flag;
	public LvAdapter(ArrayList<HotelOrder> listData, Context context, int flag) {
		this.list = listData;
		this.context = context;
		this.flag = flag;
		osHashMap = new HashMap<String, Object>();
		osHashMap.put("1", "��Ԥ��");
		osHashMap.put("2", "������");
		osHashMap.put("3", "�������");
		osHashMap.put("4", "����ʧ��");
		osHashMap.put("5", "������");
		osHashMap.put("6", "�����˴�����");
		osHashMap.put("7", "�����˴������");
		osHashMap.put("8", "�����˴���ʧ��");
		osHashMap.put("9", "�����");
		osHashMap.put("10", "����Ĵ�����");
		osHashMap.put("11", "����Ĵ������");
		osHashMap.put("12", "����Ĵ���ʧ��");
		osHashMap.put("13", "����ȡ��");
		
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {//
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater mInflater = LayoutInflater.from(context);
		convertView = mInflater.inflate(R.layout.hotelviporderitem, null);
		HotelOrder ho = list.get(position);
		
//		Log.e("HotelName", (String)hashMap.get("HotelName"));
//		Log.e("CheckInDate", (String)hashMap.get("CheckInDate"));
//		Log.e("OrderStatus", (String)hashMap.get("OrderStatus"));
//		Log.e("OrderId", (String)hashMap.get("OrderId"));
//		Log.e("TotalPrice", (String)hashMap.get("TotalPrice"));
//		Log.e("AddTime", (String)hashMap.get("AddTime"));
		
		TextView date = (TextView)convertView.findViewById(R.id.textViewhitem9);
		if(flag == 1){
			date.setText("��ס����:");
		}else{
			date.setText("�ó�����:");
		}
		TextView nametv= (TextView)convertView.findViewById(R.id.textViewhitem2);
		nametv.setText(ho.getHotelName());
		TextView datetv = (TextView)convertView.findViewById(R.id.textViewhitem3);
		datetv.setText(ho.getAddTime());
		TextView ostartv = (TextView)convertView.findViewById(R.id.textViewhitem4);
		ostartv.setText((String)osHashMap.get(ho.getOrderStatus()));
		TextView idTextView = (TextView)convertView.findViewById(R.id.textViewhitem6);
		idTextView.setText(ho.getOrderId());
		TextView picTextView = (TextView)convertView.findViewById(R.id.textViewhitem8);
		picTextView.setText(ho.getTotalPrice().replace(".0", ""));
		TextView addtiemTextView = (TextView)convertView.findViewById(R.id.textViewhitem10);
		addtiemTextView.setText(ho.getCheckInDate());
		
		
		return convertView;
	}

}
