package cn.vip.main;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.tripg.R;


public class VipOrderCell extends BaseAdapter {

	private List<HashMap<String, Object>> listData;
	private Context context;
	private Bitmap bitmap;
	private ViewHolder viewHolder;
	private LayoutInflater mInflater = null;
	
	public VipOrderCell(Context context, int resource,
			List<HashMap<String, Object>> objects) {
		this.context = context;
		this.listData = objects;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.order_item, null);
			Log.e("position  a is ", ""+position);
			Log.e("listData.size()  a is------- ", ""+listData.size());
		}	

			
			Log.e("position is ", ""+position);
			Log.e("listData.size() is------- ", ""+listData.size());
			
			final HashMap<String, Object> hashMap = listData.get(position);
			
			TextView textView1 = (TextView)convertView.findViewById(R.id.textViewvipfding1);
		    textView1.setText((String)hashMap.get("order_dtime"));
		    TextView textView2 = (TextView)convertView.findViewById(R.id.textViewvipfding2);
			textView2.setText((String)hashMap.get("order_status"));
			TextView textView3 = (TextView)convertView.findViewById(R.id.textViewvipfding3);
			textView3.setText((String)hashMap.get("order_scity")+">"+(String)hashMap.get("order_acity"));
			TextView textView4 = (TextView)convertView.findViewById(R.id.textViewvipfding4);
			if ((String)hashMap.get("order_tax") != null) {
				
				String oyqString = (String)hashMap.get("order_yq");
				String taxString = (String)hashMap.get("order_tax");
				String jpiString = (String)hashMap.get("order_jpice");
				String orderSfae = hashMap.get("order_safe").toString();
				int oy = Integer.valueOf(oyqString);
				int tax = Integer.valueOf(taxString);
				int jpi = Integer.valueOf(jpiString);
				int safe;
				if(orderSfae.equals("1")){
					 safe = 1 * 20;
				}else if(orderSfae.equals("2")){
					 safe = 2 * 20;
				}else{
					 safe = 0;
				}
				int p = oy + tax +jpi + safe;
				textView4.setText("гд"+p);
			}else {
				textView4.setText("гд"+(String)hashMap.get("order_jpice"));
			}
			
			TextView textView5 = (TextView)convertView.findViewById(R.id.textViewvipfding5);
			textView5.setText((String)hashMap.get("order_resid"));
			TextView textView6 = (TextView)convertView.findViewById(R.id.textViewvipfding6);
			textView6.setText((String)hashMap.get("order_date"));
			
			Log.e("cell is here",""+(String)hashMap.get("order_resid"));
			
			return convertView;
	}

	
	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	
}


class ViewHolder{

	public TextView textView1;
	public TextView textView2;
	public TextView textView3;
	public TextView textView4;
	public TextView textView5;
	public TextView textView6;
	public ImageView starImageView;
	public ImageView imageViewdanbao;
	public boolean isImageLoaded = false;
	
}

