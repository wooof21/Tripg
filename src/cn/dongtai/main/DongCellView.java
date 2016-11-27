package cn.dongtai.main;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.tripg.R;



public class DongCellView extends ArrayAdapter<HashMap<String, Object>> {



		private List<HashMap<String, Object>> listData;
		private Context context;
		private Bitmap bitmap;
		private ViewHolder viewHolder;
		
		public DongCellView(Context context, int textViewResourceId,
				List<HashMap<String, Object>> objects) {
			super(context, textViewResourceId, objects);
			this.context = context;
			this.listData = objects;
	
		}
		
		
		@Override
		public int getCount() {
			
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

		/**
		 * @param getView
		 * 自定义的cell内容在这里赋值操作
		 * 
		 * */
		@SuppressWarnings("unused")
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.dongcellxml, null);
			HashMap<String, Object> hashMap = listData.get(position);
			
			TextView textView1 = (TextView) convertView.findViewById(R.id.textViewdc1);
			textView1.setText((String) hashMap.get("FlightCompany"));
			TextView textView2 = (TextView) convertView.findViewById(R.id.textViewdc2);
			textView2.setText((String) hashMap.get("FilghtNo"));
			if(convertView == null){
				convertView = new TextView(this.context);
			}
			
			
			return convertView;
			
		}
		
		
	
}
	
	class ViewHolder{

		public ImageView cellImage;
		public boolean isImageLoaded = false;
	}
