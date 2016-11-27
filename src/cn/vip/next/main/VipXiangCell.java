package cn.vip.next.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.tripg.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class VipXiangCell extends BaseAdapter{

	
	
	private List<HashMap<String, Object>> listData;
	private Context context;
	private Bitmap bitmap;
	private LayoutInflater mInflater = null;
	
	public VipXiangCell(Context context, int resource,
			List<HashMap<String, Object>> objects) {
		this.context = context;
		this.listData = objects;
		mInflater = LayoutInflater.from(context);
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
	
	
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		

			convertView = mInflater.inflate(R.layout.test, null);
			Log.e("position  a is ", ""+position);
			Log.e("listData.size()  a is------- ", ""+listData.size());


			HashMap<String, String> map = new HashMap<String, String>();
			map = getDeliveryMap();
			Log.e("position is ", ""+position);
			Log.e("listData.size() is------- ", ""+listData.size());
			
			final HashMap<String, Object> hashMap = listData.get(position);
			TextView textView = (TextView)convertView.findViewById(R.id.textViewvipxiang1);
			textView.setText((String)hashMap.get("order_scity")+">"+(String)hashMap.get("order_acity")+(String)hashMap.get("order_date"));
			TextView textView2 = (TextView)convertView.findViewById(R.id.textViewvipxiang2);
			textView2.setText(((VipNOrderXiang)context).nameString+(String)hashMap.get("order_company")+(String)hashMap.get("order_number"));
			TextView textView3 = (TextView)convertView.findViewById(R.id.textViewvipxiang4);
			StringBuffer str1 = new StringBuffer((String)hashMap.get("depart"));
			String strInsert = ":";
			str1.insert(2,strInsert + " ");
			textView3.setText(str1);
			
			TextView textView4 = (TextView)convertView.findViewById(R.id.textViewvipxiang6);
			StringBuffer str2 = new StringBuffer((String)hashMap.get("arrive"));
			String strInsert2 = ":";
			str2.insert(2,strInsert2 + " ");
			textView4.setText(str2);
			
			TextView textView5 = (TextView)convertView.findViewById(R.id.textViewvipxiang7);
			textView5.setText((String)hashMap.get("order_space")+"��");
			TextView textView6 = (TextView)convertView.findViewById(R.id.textViewvipxiang8);
			textView6.setText((Integer.parseInt((String)hashMap.get("order_discount")) / 10)+"��");
			TextView textView7 = (TextView)convertView.findViewById(R.id.textViewvipxiang10);
			textView7.setText("��"+(String)hashMap.get("order_jpice"));
			System.out.println("order_jpice ---> " + (String)hashMap.get("order_jpice"));
			TextView textView8 = (TextView)convertView.findViewById(R.id.textViewvipxiang12);
			textView8.setText("��"+(String)hashMap.get("order_tax"));
			System.out.println("order_tax ---> " + (String)hashMap.get("order_tax"));
			TextView textView9 = (TextView)convertView.findViewById(R.id.textViewvipxiang14);
			textView9.setText("��"+(String)hashMap.get("order_yq"));
			System.out.println("order_yq ---> " + (String)hashMap.get("order_yq"));
			TextView textView10 = (TextView)convertView.findViewById(R.id.textViewvipxiang15);
			textView10.setText((String)hashMap.get("order_cmt"));//
			TextView textView11 = (TextView)convertView.findViewById(R.id.textViewvipxiang17);
			textView11.setText((String)hashMap.get("order_people"));
			TextView textView12 = (TextView)convertView.findViewById(R.id.textViewvipxiang19);
			textView12.setText((String)hashMap.get("telphone"));
			TextView textView13 = (TextView)convertView.findViewById(R.id.textViewvipxiang21);
			System.out.println("121241245 ---�� " + (String)hashMap.get("order_safe"));
			String string = (String)hashMap.get("order_safe").toString();
			System.out.println("safe ---> " + string);
			if(string.equals("null")){
				System.out.println("true");
				textView13.setText("0");
			}else{
				System.out.println("false");
				textView13.setText((String)hashMap.get("order_safe"));
			}
			TextView textView14 = (TextView)convertView.findViewById(R.id.textViewvipxiang23);
			//textView14.setText((String)hashMap.get("delivery"));
			textView14.setText(map.get((String)hashMap.get("delivery")));
			System.out.println("delivery ---> " + (String)hashMap.get("delivery"));
			TextView textView15 = (TextView)convertView.findViewById(R.id.textViewvipxiang25);
			textView15.setText((String)hashMap.get("order_status"));
			System.out.println("order_status ---> " + (String)hashMap.get("order_status"));
			TextView textView16 = (TextView)convertView.findViewById(R.id.textViewvipxiang27);
			textView16.setText((String)hashMap.get("order_resid"));
			TextView textView17 = (TextView)convertView.findViewById(R.id.textViewvipxiang29);
			textView17.setText((String)hashMap.get("order_dtime"));
			
			
			TimeCurrent tCurrent = new TimeCurrent();
			String ordertimeString = (String) hashMap.get("order_dtime").toString().substring(0, 10);
			String calenderString = (String)tCurrent.getCalendartime();
			Log.e("��ȡ����ʱ����Ϣ----��ȡϵͳ��Ϣ", ""+ordertimeString+"........"+calenderString);
			int timeNum = tCurrent.getGapCount(tCurrent.getDateTime(ordertimeString), tCurrent.getDateTime(calenderString));
			Log.e("��ȡ��ǰ�����붩�����ڵ�ʱ���", ""+timeNum);
			
			int timehour = tCurrent.getTimeCurrentHr();
			String hourString = (String) hashMap.get("order_dtime").toString();
			Log.e("hour---------", hourString+"---"+hourString.length());
			
			String hour[] = hourString.split(" ");
			String hourTimeString = (String)hour[1].toString();
			
			Log.e("hourTimeString---------", hourTimeString+"---"+hourTimeString.length());
			hourString = (String)hourTimeString.substring(0, 2);
			int orderhour = Integer.valueOf(hourString);
			Log.e("��ȡСʱ������Ϣ��ϵͳ-----���� ", ""+timehour+"-----"+orderhour);
			
			int timemin = tCurrent.getTimeCurrentMin();
			String min[] = hourTimeString.split(":");
			String minTimeString = (String)min[1].toString();
			int ordermin = Integer.valueOf(minTimeString);
			Log.e("��ȡ���Ӿ�����Ϣ��ϵͳ-----���� ", ""+timemin+"-----"+ordermin);
			
			if(hashMap.get("is_pay").toString().equalsIgnoreCase("Y")){
				TextView vipzhifuTextView = (TextView) convertView
						.findViewById(R.id.vipzhibubaobtn);
				vipzhifuTextView.setVisibility(View.VISIBLE);
				vipzhifuTextView.setText("��֧��");
				Log.e("����ʱ�䲻��ȷ ����20����", "------");
			}else{
				if (timeNum <= 0) {
					if (orderhour <= timehour) {
						int at = ( timehour * 60 + timemin ) - (orderhour * 60 + ordermin);
						if (at < 20) {
							TextView vipzhifuTextView = (TextView) convertView
									.findViewById(R.id.vipzhibubaobtn);
							vipzhifuTextView.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									VipNOrderXiang vx = (VipNOrderXiang)context;
									vx.zhifubaopay();
								}
							});
						}else {
							TextView vipzhifuTextView = (TextView) convertView
									.findViewById(R.id.vipzhibubaobtn);
							vipzhifuTextView.setVisibility(View.GONE);
							Log.e("����ʱ�䲻��ȷ ����20����", "------");
						}

					}else {
						TextView vipzhifuTextView = (TextView) convertView
								.findViewById(R.id.vipzhibubaobtn);
						vipzhifuTextView.setVisibility(View.GONE);
						Log.e("����ʱ�䲻��ȷ ����20����", "------");
					}
					
				}else {
					TextView vipzhifuTextView = (TextView) convertView
							.findViewById(R.id.vipzhibubaobtn);
					vipzhifuTextView.setVisibility(View.GONE);
					Log.e("����ʱ�䲻��ȷ ����20����", "------");
				}
			}

			
			
			
			
			return convertView;
	}
	private HashMap<String, String> getDeliveryMap(){
		HashMap<String, String> hashMap = new HashMap<String, String>();
		
		hashMap.put("Q", "ǰ̨��ȡ");
		hashMap.put("D", "��������");
		hashMap.put("M", "�ʼ��г̵�");
		hashMap.put("S", "��Ʊ����");
		hashMap.put("J", "����ȡƱ");
		hashMap.put("N", "����Ҫ�г̵�");
		
		return hashMap;
	}
	
	public class TimeCurrent {

		private Date timeDate;

		/**
		 * @author mac
		 * */
		@SuppressLint("SimpleDateFormat") 
		
		public Date getDateTime(String time){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			try {
				 timeDate = sdf.parse(time);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return timeDate;
		}
		
		
		
		/**
		 * @author getCalendartime
		 *         ��ȡ��ǰ����
		 * */
		public String getCalendartime() {
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = sDateFormat.format(new java.util.Date());

			return date;
		}

		/**
		 * @param getGapCount
		 *            ��ȡ���ڲ�ĺ��������������ڴ���˺���������һ��int ���͵����ڲ�
		 * 
		 * */
		public int getGapCount(Date startDate, Date endDate) {
			Calendar fromCalendar = Calendar.getInstance();
			fromCalendar.setTime(startDate);
			fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
			fromCalendar.set(Calendar.MINUTE, 0);
			fromCalendar.set(Calendar.SECOND, 0);
			fromCalendar.set(Calendar.MILLISECOND, 0);

			Calendar toCalendar = Calendar.getInstance();
			toCalendar.setTime(endDate);
			toCalendar.set(Calendar.HOUR_OF_DAY, 0);
			toCalendar.set(Calendar.MINUTE, 0);
			toCalendar.set(Calendar.SECOND, 0);
			toCalendar.set(Calendar.MILLISECOND, 0);

			return (int) ((toCalendar.getTime().getTime() - fromCalendar
					.getTime().getTime()) / (1000 * 60 * 60 * 24));
		}

		/*************************************************************************************************/

		public int getTimeCurrentHr() {
			/*
			 * ��ȡ��ǰϵͳʱ��
			 */
			Time time = new Time();// or Time t=new Time("GMT+8"); ����Time
									// Zone���ϡ�
			time.setToNow();// ȡ��ϵͳʱ�䡣
			String hr = "" + time.hour;
			System.out.println("hour ---> " + hr);
			String hour[] = hr.split(":");
			int hr_int = Integer.parseInt(hour[0]);
			return (hr_int);
		}

		/*************************************************************************************************/

		/*
		 * ���Ԥ������
		 */
		public int getTimeCurrentMin() {
			/*
			 * ��ȡ��ǰϵͳʱ��
			 */
			Time time = new Time();// or Time t=new Time("GMT+8"); ����Time
									// Zone���ϡ�
			time.setToNow();// ȡ��ϵͳʱ�䡣
			String min = "" + time.minute;
			System.out.println("minute ---> " + min);
			// String hour[] = hr.split(":");
			// int hr_int = Integer.parseInt(hour[0]);
			return (Integer.parseInt(min));
		}

	}
	
	
}
