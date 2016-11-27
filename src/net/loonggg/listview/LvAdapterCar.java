package net.loonggg.listview;

import java.util.HashMap;
import java.util.List;

import cn.tripg.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LvAdapterCar extends BaseAdapter{

	private List<HashMap<String, Object>> list;
	private Context context;
	public HashMap<String, Object> osHashMap;
	private int flag;
	public LvAdapterCar(List<HashMap<String, Object>> listData, Context context, int flag) {
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
		HashMap<String, Object> hashMap= list.get(position);
		
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
		nametv.setText((String)hashMap.get("HotelName"));
		TextView datetv = (TextView)convertView.findViewById(R.id.textViewhitem3);
		datetv.setText((String)hashMap.get("AddTime"));
		TextView ostartv = (TextView)convertView.findViewById(R.id.textViewhitem4);
		ostartv.setText((String)osHashMap.get(hashMap.get("OrderStatus")));
		TextView idTextView = (TextView)convertView.findViewById(R.id.textViewhitem6);
		idTextView.setText((String)hashMap.get("OrderId"));
		TextView picTextView = (TextView)convertView.findViewById(R.id.textViewhitem8);
		picTextView.setText((String)hashMap.get("TotalPrice").toString().replace(".0", ""));
		TextView addtiemTextView = (TextView)convertView.findViewById(R.id.textViewhitem10);
		addtiemTextView.setText((String)hashMap.get("CheckInDate"));
		
		
		return convertView;
	}


}
