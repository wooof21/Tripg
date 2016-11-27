package cn.vip.order;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import model.flight.Keys;
import model.flight.Rsa;

import com.alipay.android.app.sdk.AliPay;

import order.pnr.yidao.OrderSuccse;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.internet.Exit;
import cn.tripg.R;
import cn.tripg.activity.flight.ResultCode;
import cn.tripg.interfaces.TripgMessage;
import cn.tripg.interfaces.impl.VipYiCarXiangInterfaces;
import cn.vip.next.main.VipXiangCell.TimeCurrent;

public class VipYcarNextOrderm extends Activity implements OnClickListener {

	public String memberString;
	public String orderidString;
	private final int UPDATE_LIST_VIEW = 1;
	private List<HashMap<String, Object>> listData;
	public HashMap<String, Object> osHashMap;
	public VipYiCarXiangInterfaces vipYiCarXiangInterfaces;
	public TextView textView1;
	public TextView textView2;
	public TextView textView3;
	public TextView textView4;
	public TextView textView5;
	public TextView textView6;
	public TextView textView7;
	public TextView textView8;
	private static final int PAY_OK = 9;
	private String OrderInfo;
	private ProgressDialog dialog;
	public String order_idString;
	public String totalAmountString;
	public String payStatuString;
	public VipYcarNextOrderm vp;
	public String sssString1;

	/****************************************************************************/
	public boolean getInternet() {
		ConnectivityManager con = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.isConnectedOrConnecting();
		boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.isConnectedOrConnecting();
		if (wifi || internet) {
			return true;
		} else {
			return false;
		}
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vipycarxiangmian);//
		Exit.getInstance().addActivity(this);
		Intent intent = getIntent();
		memberString = intent.getExtras().getString("memberid");
		orderidString = intent.getExtras().getString("orderid");
		Log.e("memberString ", "" + memberString + "----------" + orderidString);
		vp = this;
		ImageView backImageView = (ImageView) findViewById(R.id.title_back);
		backImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = getIntent();
				setResult(ResultCode.FAILURE, intent);
				finish();
			}
		});

		listData = new ArrayList<HashMap<String, Object>>();

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
		osHashMap.put("14", "�ȴ��û�����");
		osHashMap.put("15", "�ȴ�ѡ����");
		osHashMap.put("16", "�ȴ�˾��ȷ��");
		osHashMap.put("17", "˾����ȷ��");
		osHashMap.put("18", "˾���ѵ���");
		osHashMap.put("19", "����ʼ");
		osHashMap.put("20", "�������");
		osHashMap.put("21", "�⳵ȡ��");

		textView1 = (TextView) findViewById(R.id.textViewycarx2);
		textView2 = (TextView) findViewById(R.id.textViewycarx4);
		// textView3 = (TextView)findViewById(R.id.textViewycarx6);
		textView4 = (TextView) findViewById(R.id.textViewycarx8);
		textView5 = (TextView) findViewById(R.id.textViewycarx10);
		textView6 = (TextView) findViewById(R.id.textViewycarx12);
		textView7 = (TextView) findViewById(R.id.textViewycarx14);
		textView8 = (TextView) findViewById(R.id.textViewycarx16);

		if (getInternet() == true) {
			httpycarxiangGet();
		} else {
			Toast.makeText(VipYcarNextOrderm.this, "���������ѶϿ�", Toast.LENGTH_LONG)
					.show();
		}

	}

	public void httpycarxiangGet() {

		String urlString = "http://mapi.tripglobal.cn/MemApi.aspx?action=GetRentCarOrder&memberId="
				+ memberString
				+ "&orderId="
				+ orderidString
				+ "&token=IEEW9lg9hHa1qMC2LrAgHuluilAAX_q0";
		Log.e("urlString", "" + urlString);
		vipYiCarXiangInterfaces = new VipYiCarXiangInterfaces(
				VipYcarNextOrderm.this, handler);
		vipYiCarXiangInterfaces.getModelFromGET(urlString,
				TripgMessage.HANGBAN, "0");

	}

	private void sendHandlerMessage(int what, Object obj) {

		Message msg = new Message();
		msg.what = what;
		handler.sendMessage(msg);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TripgMessage.HANGBAN:
				handMessageDefault(vipYiCarXiangInterfaces,
						VipYcarNextOrderm.this, msg);
				break;
			case UPDATE_LIST_VIEW:
				Log.e("��������ҳ �ӿڵ��ý���֮�󷵻ص��� ����", "****");
				if (listData.size() != 0) {
					HashMap<String, Object> hashMap = listData.get(0);
					textView1.setText(osHashMap.get(
							hashMap.get("OrderStatus").toString()).toString());
					textView2.setText(hashMap.get("DepAddress").toString());
					// textView3.setText(hashMap.get("ArrAddress").toString());
					textView4.setText(hashMap.get("RentCarTime").toString());
					textView5.setText(hashMap.get("TotalAmount").toString()
							+ "Ԫ");

					order_idString = (String) hashMap.get("orderid").toString();
					totalAmountString = (String) hashMap.get("TotalAmount")
							.toString();
					payStatuString = (String) hashMap.get("PayStatus")
							.toString();

					textView6
							.setText(hashMap.get("PriductTypeDesc").toString());
					textView7.setText(hashMap.get("PassengerName").toString());
					textView8
							.setText(hashMap.get("PassengerMobile").toString());

					if (!payStatuString.equals("3")) {

						TextView textViewzf = (TextView) findViewById(R.id.vipcarzhibubaobtn);
						textViewzf.setVisibility(View.VISIBLE);
						textViewzf.setOnClickListener(vp);

					}

				}

				break;
			case PAY_OK:
				Bundle b = new Bundle();
				b.putString("DingDanNo", order_idString);
				Intent intent10 = new Intent(VipYcarNextOrderm.this,
						OrderSuccse.class);
				intent10.putExtras(b);
				startActivity(intent10);
				Toast.makeText(VipYcarNextOrderm.this, "֧���ɹ�!",
						Toast.LENGTH_SHORT).show();

				break;

			default:

				break;
			}

		}

		@SuppressWarnings("unchecked")
		private void handMessageDefault(
				VipYiCarXiangInterfaces vipYiCarXiangInterfaces,
				VipYcarNextOrderm vipYcarNextOrderm, Message msg) {

			if (vipYiCarXiangInterfaces == null)
				return;
			if (vipYiCarXiangInterfaces.progressDialog != null)
				vipYiCarXiangInterfaces.progressDialog.dismiss();
			if (msg.obj == null) {
				Toast.makeText(vipYcarNextOrderm, "�������ӳ�ʱ", Toast.LENGTH_SHORT)
						.show();
			} else {
				listData = (List<HashMap<String, Object>>) msg.obj;
				Log.e("listData----���� ", "" + listData.size());

			}

			// ֪ͨˢ�½���
			sendHandlerMessage(UPDATE_LIST_VIEW, null);

		}

	};

	public class TimeCurrent {

		private Date timeDate;
		private Date endDate;

		/**
		 * @author mac
		 * */
		@SuppressLint("SimpleDateFormat")
		public Date getDateTime(String time) {
			// SimpleDateFormat sdf = new
			// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			try {
				timeDate = sdf.parse(time);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return timeDate;
		}

		/**
		 * @param MicDateTime
		 *            ����ʱ���һ��ķ���
		 * 
		 * */
		public Date MicDateTime(String micDateString) {

			SimpleDateFormat dft = new SimpleDateFormat("MM/dd/yyyy");
			SimpleDateFormat dft1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

			try {
				Date beginDate = dft.parse(micDateString);
				Calendar date = Calendar.getInstance();
				date.setTime(beginDate);
				date.set(Calendar.DATE, date.get(Calendar.DATE) - 1);

				endDate = dft.parse(dft.format(date.getTime()));
				String sssString = dft.format(endDate);
				endDate = dft1.parse(sssString + " " + "15:00:00");
				sssString1 = dft1.format(endDate);
				Log.e("�ó�ʱ���һ��----", sssString1 + "*********" + endDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return endDate;
		}

		/**
		 * @author getCalendartime ��ȡ��ǰ����
		 * */
		public String getCalendartime() {
			// SimpleDateFormat sDateFormat = new
			// SimpleDateFormat("yyyy-MM-dd HH:mm");
			SimpleDateFormat sDateFormat = new SimpleDateFormat(
					"MM/dd/yyyy HH:mm:ss");
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

	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

	@SuppressWarnings("deprecation")
	private void checkingStatusCode(String s) {

		// Toast.makeText(HotelNewOrderYuDingMain.this, msg, 3000).show();
		dialog = new ProgressDialog(VipYcarNextOrderm.this);
		dialog.setMessage(s);
		dialog.setIndeterminate(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				dialog.dismiss();

			}
		});

		dialog.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.vipcarzhibubaobtn:
			HashMap<String, Object> hashMap = listData.get(0);

			TimeCurrent tCurrent = new TimeCurrent();
			String calenderString = (String) tCurrent.getCalendartime();
			String ordertimeString = hashMap.get("RentCarTime").toString();
			Log.e("��ȡ����ʱ����Ϣ----��ȡϵͳ��Ϣ", "" + ordertimeString + "........"
					+ calenderString);
			int timeNum = tCurrent.getGapCount(
					tCurrent.MicDateTime(ordertimeString),
					tCurrent.getDateTime(calenderString));
			Log.e("��ȡ��ǰ�����붩�����ڵ�ʱ���", "" + timeNum);

			int timehour = tCurrent.getTimeCurrentHr();
			String hourString = sssString1;
			Log.e("hour---------", sssString1 + "����--" + hourString + "---"
					+ hourString.length());

			String hour[] = hourString.split(" ");
			String hourTimeString = (String) hour[1].toString();
			Log.e("hourTimeString---------", hourTimeString + "---"
					+ hourTimeString.length());
			hourString = (String) hourTimeString.substring(0, 2);
			int orderhour = Integer.valueOf(hourString);
			Log.e("��ȡСʱ������Ϣ��ϵͳ-----���� ", "" + timehour + "-----" + orderhour);

			int timemin = tCurrent.getTimeCurrentMin();
			String min[] = hourTimeString.split(":");
			String minTimeString = (String) min[1].toString();
			int ordermin = Integer.valueOf(minTimeString);
			Log.e("��ȡ���Ӿ�����Ϣ��ϵͳ-----���� ", "" + timemin + "-----" + ordermin);

			if (timeNum < 0 && timeNum != -1) {

				zhibubaoHttp();
				
			} else if (timeNum == -1) {

				if (orderhour > timehour) {

					zhibubaoHttp();
					
				} else {
					Toast.makeText(
							VipYcarNextOrderm.this,
							"���������Ѿ��ѽ������ó����Ž������޷�Ϊ�����ų���������£�����������벦��ͷ�����400-6568-777",
							Toast.LENGTH_SHORT).show();
				}

			} else {
				Toast.makeText(
						VipYcarNextOrderm.this,
						"���������Ѿ��ѽ������ó����Ž������޷�Ϊ�����ų���������£�����������벦��ͷ�����400-6568-777",
						Toast.LENGTH_SHORT).show();
			}

			break;

		default:
			break;
		}
	}

	public void zhibubaoHttp() {

		String result = null;

		AliPay aliPay = new AliPay(VipYcarNextOrderm.this, handler);
		// ֧����֧����ȥ�ĵķ���ֵ��orderResult
		result = aliPay.pay(OrderInfo);
		System.out.println("orderResult ---> " + result);
		try {
			Log.i("ExternalPartner", "onItemClick");
			OrderInfo = getOrderInfo();
			String sign = Rsa.sign(OrderInfo, Keys.PRIVATE);
			sign = URLEncoder.encode(sign);
			OrderInfo += "&sign=\"" + sign + "\"&" + getSignType();

			Log.i("ExternalPartner", "start pay");
			// start the pay.
			Log.e("----pay  ", "info = " + OrderInfo);

			final String orderInfo = OrderInfo;
			new Thread() {
				@SuppressWarnings("unused")
				public void run() {
					Looper.prepare();

					AliPay alipay = new AliPay(VipYcarNextOrderm.this, handler);

					// ����Ϊɳ��ģʽ��������Ĭ��Ϊ���ϻ���
					// alipay.setSandBox(true);

					String result = alipay.pay(orderInfo);
					// String result =
					// "resultStatus={9000};memo={};result={partner=\"2088801264377844\"&out_trade_no=\"13000480328\"&subject=\"Android��Ʊ֧��ҵ��\"&body=\"�ͻ���Ԥ���Ļ�Ʊ����֧��\"&total_fee=\"0.01\"&notify_url=\"http%3A%2F%2Fwww.tripg.com\"&service=\"mobile.securitypay.pay\"&_input_charset=\"UTF-8\"&return_url=\"http%3A%2F%2Fm.alipay.com\"&payment_type=\"1\"&seller_id=\"2088801264377844\"&it_b_pay=\"1m\"&success=\"true\"&sign_type=\"RSA\"&sign=\"XzwTN0FJrTx4yFuwnwAiMlqc8odokn7N+ogC8kBKWr/I0DMWAhDTSHXNKTJQ79oL2zCYrBnH6+4JA7YASq9h3TC9YhKxyvYjuW7Ighwt+gtQiw7A9f5aOPVkAbiNbI3iEybTxU8m8s+9wTpIbCHCRrk4vn9jf8Y1rZZd5v+8VwI=\"}";
					if (result.length() != 0 || result != null) {

						Log.e("===pay", "result = " + result);

						String s[] = result.split(";");
						System.out.println("s[0] ---> " + s[0]);
						;
						String s2 = s[0].substring(14, s[0].length() - 1);

						String memo = s[1].substring(6, s[1].length() - 1);
						System.out.println("s2 ---> " + s2);
						System.out.println("memo ---> " + memo);

						if (s2.equals("9000")) {
							Log.e("����֧���ɹ�", "");
							Message msg = new Message();
							msg.what = PAY_OK;
							msg.obj = result;
							handler.sendMessage(msg);
							// checkingStatusCode(memo,true);
						} else if (s2.equals("8000")) {
							checkingStatusCode(memo);
						} else if (s2.equals("4000")) {

							checkingStatusCode(memo);
						} else if (s2.equals("6001")) {

							checkingStatusCode(memo);
						} else if (s2.equals("6002")) {

							checkingStatusCode(memo);
						}
					} else {
						Toast.makeText(VipYcarNextOrderm.this, "֧��ʧ��!",
								Toast.LENGTH_SHORT).show();
					}
					Looper.loop();
				}

			}.start();

		} catch (Exception ex) {
			ex.printStackTrace();
			Toast.makeText(VipYcarNextOrderm.this, R.string.remote_call_fail,
					Toast.LENGTH_SHORT).show();
		}

	}

	protected String getOrderInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(order_idString);// ������
		sb.append("\"&subject=\"");
		sb.append("Android�⳵֧��ҵ��");
		sb.append("\"&body=\"");
		// sb.append("�ͻ���Ԥ������������֧��");
		sb.append("8");
		sb.append("\"&total_fee=\"");

		sb.append(totalAmountString);// �ܼ�
		// sb.append(totalFee);
		sb.append("\"&notify_url=\"");
		// ��ַ��Ҫ��URL����
		sb.append(URLEncoder
				.encode("http://www.tripg.cn/phone_api/alipay_wuxian/notify_url.php"));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");

		System.out.println("sb ---> " + new String(sb));
		return new String(sb);
	}

}
