package cn.vip.next.main;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.flight.Keys;
import model.flight.Result;
import model.flight.Rsa;

import com.alipay.android.app.sdk.AliPay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import cn.internet.Exit;
import cn.tripg.R;
import cn.tripg.activity.flight.ResultCode;
import cn.tripg.interfaces.TripgMessage;
import cn.tripg.interfaces.impl.VipHttpXiangFaces;
import cn.tripg.interfaces.impl.VipOrderHttpIntfaces;
import cn.vip.main.VipOrderCell;

public class VipNOrderXiang extends Activity implements OnItemClickListener {

	public String orderidString;
	public VipNOrderXiang vipNOrderXiang;
	public VipHttpXiangFaces vipHttpXiangFaces;
	private final int UPDATE_LIST_VIEW = 1;
	public ListView listView;
	private List<HashMap<String, Object>> listData;
	public HashMap<String, Object> fnameHashMap;
	public String nameString;
	private String orderSafe;
	private ProgressDialog dialog;
	public String info;
	private static final int RQF_PAY = 4;
	private static final int RQF_LOGIN = 2;
	
	
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
		setContentView(R.layout.vipxiangforder);
		Exit.getInstance().addActivity(this);
		vipNOrderXiang = this;
		Intent intent = getIntent();
		orderidString = intent.getStringExtra("order_id");
		orderSafe = intent.getStringExtra("order_safe");
		Log.e("����������� orderidString", "" + orderidString);

		listData = new ArrayList<HashMap<String, Object>>();
		listView = (ListView) findViewById(R.id.listViewvipxiang1);

		ImageView backImageView = (ImageView) findViewById(R.id.title_back);
		backImageView.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Intent intent = getIntent();
				setResult(ResultCode.FAILURE, intent);
				finish();

			}
		});

		if(getInternet() == true){
			httpxiangorder();
		}else{
			Toast.makeText(VipNOrderXiang.this, "���������ѶϿ�", Toast.LENGTH_LONG).show();
		}
		

	}

	public String namehashmpafucn(String naString) {
		fnameHashMap = new HashMap<String, Object>();
		fnameHashMap.put("3U", "�Ĵ����չɷ����޹�˾");
		fnameHashMap.put("8L", "�������������������ι�˾");
		fnameHashMap.put("BK", "�¿��������޹�˾");
		fnameHashMap.put("CA", "�й����ʺ��չ�˾");
		fnameHashMap.put("CZ", "�й��Ϸ����չ�˾");
		fnameHashMap.put("EU", "�ɶ��������޹�˾");
		fnameHashMap.put("FM", "�Ϻ��������޹�˾");
		fnameHashMap.put("G5", "���ĺ������޹�˾");
		fnameHashMap.put("GS", "��򺽿��������ι�˾");
		fnameHashMap.put("HO", "�Ϻ����麽�����޹�˾");
		fnameHashMap.put("HU", "���Ϻ��չɷ����޹�˾");
		fnameHashMap.put("KN", "�й����Ϻ������޹�˾");
		fnameHashMap.put("MF", "���ź������޹�˾");
		fnameHashMap.put("MU", "�й��������չ�˾");
		fnameHashMap.put("PN", "���������������ι�˾");
		fnameHashMap.put("SC", "ɽ�����չɷ����޹�˾");
		fnameHashMap.put("ZH", "���ں����������ι�˾");
		fnameHashMap.put("KY", "�����������޹�˾");
		fnameHashMap.put("NS", "�ӱ��������޹�˾");
		fnameHashMap.put("JR", "�Ҹ������������ι�˾");
		fnameHashMap.put("JD", "�����׶����չ�˾");
		fnameHashMap.put("CN", "���»��������޹�˾");
		fnameHashMap.put("VD", "���Ϻ������޹�˾");
		fnameHashMap.put("8C", "�人���Ǻ������޹�˾");
		fnameHashMap.put("9C", "���ﺽ�����޹�˾");
		fnameHashMap.put("TV", "���غ������޹�˾");
		String nString = (String) fnameHashMap.get(naString);
		Log.e("���չ�˾�� -----  ", "" + nString);

		return nString;
	}

	public void httpxiangorder() {

		//String urlString = "http://210.72.225.98:8090/index.php/OrderApi/detail_show?id="
		String urlString = "http://www.tripg.cn/phone_api/shouji/index.php/OrderApi/detail_show?id="
				+ orderidString;
		Log.e("��������ҳ �ӿڵ�����Ϣ urlString", "" + urlString);
		vipHttpXiangFaces = new VipHttpXiangFaces(vipNOrderXiang, handler);
		vipHttpXiangFaces.getModelFromGET(urlString, TripgMessage.HANGBAN,"0");

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
				handMessageDefault(vipHttpXiangFaces, VipNOrderXiang.this, msg);
				break;
			case UPDATE_LIST_VIEW:
				Log.e("��������ҳ �ӿڵ��ý���֮�󷵻ص��� ����", "****");
				VipXiangCell cell = new VipXiangCell(vipNOrderXiang,
						R.layout.vipxiangforder, listData);
				listView.setAdapter(cell);
				listView.setOnItemClickListener(vipNOrderXiang);

				break;
			case RQF_PAY:
				Result result = new Result((String) msg.obj);
				Log.e("RQF_PAY*****", "RQF_PAY result--" + result.getResult());
				break;
			default:

				break;
			}

		}

		@SuppressWarnings("unchecked")
		private void handMessageDefault(VipHttpXiangFaces vipHttpXiangFaces,
				VipNOrderXiang vipNOrderXiang, Message msg) {

			if (vipHttpXiangFaces == null)
				return;
			if (vipHttpXiangFaces.progressDialog != null)
				vipHttpXiangFaces.progressDialog.dismiss();
			if (msg.obj == null) {
				Toast.makeText(vipNOrderXiang, "�������ӳ�ʱ", Toast.LENGTH_SHORT)
						.show();
			} else {
				listData = (List<HashMap<String, Object>>) msg.obj;
				nameString = namehashmpafucn((String) listData.get(0).get(
						"order_company"));
				for (int i = 0; i < listData.size(); i++) {
					Log.e("order_id", "" + listData.get(i).get("order_id"));
				}

				// ֪ͨˢ�½���
				sendHandlerMessage(UPDATE_LIST_VIEW, null);

			}

		}

	};

	/**
	 * @param onItemClick
	 *            listview ����¼�
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		HashMap<String, Object> hashMap = listData.get(arg2);
		Log.e("listview  item  hashMap",
				"****hashMap " + hashMap.get("order_id"));
		Log.e("listview  item  hashMap",
				"****hashMap " + hashMap.get("order_cmt"));

	}
	
	
	private String getNewOrderInfo() {

		HashMap<String, Object> hashMap = listData.get(0);
		double jpice = Double.valueOf(hashMap.get("order_jpice").toString());
		double tax = Double.valueOf(hashMap.get("order_tax").toString());
		double yq = Double.valueOf(hashMap.get("order_yq").toString());
		double safe = Double.valueOf(hashMap.get("order_safe").toString());
		
		double totalPrice = jpice + tax + yq + safe * 20;
		Log.e("totalPrice", ""+totalPrice);
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append((String) hashMap.get("order_resid").toString());// ������
		sb.append("\"&subject=\"");
		sb.append("Android��Ʊ֧��ҵ��");
		sb.append("\"&body=\"");
		// sb.append("�ͻ���Ԥ���Ļ�Ʊ����֧��");
		sb.append("1");
		sb.append("\"&total_fee=\"");
		sb.append(""+totalPrice);// �۸�
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

		// ���show_urlֵΪ�գ��ɲ��� 4008009888
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");
		return new String(sb);
	}

	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

	/**
	 * @param zhifubaopay
	 *            ֧���� ֧������
	 * */

	public void zhifubaopay() {

		if (getInternet() == true) {
			try {
				Log.i("ExternalPartner", "onItemClick");
				info = getNewOrderInfo();
				String sign = Rsa.sign(info, Keys.PRIVATE);
				sign = URLEncoder.encode(sign);
				info += "&sign=\"" + sign + "\"&" + getSignType();

				Log.i("ExternalPartner", "start pay");
				// start the pay.
				Log.e("----pay  ", "info = " + info);

				final String orderInfo = info;
				new Thread() {
					@SuppressWarnings("unused")
					public void run() {
						Looper.prepare(); 
						
						AliPay alipay = new AliPay(VipNOrderXiang.this, handler);

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
								msg.what = RQF_PAY;
								msg.obj = result;
								handler.sendMessage(msg);
								 checkingStatusCode("֧���ɹ�!");
							} else if (s2.equals("8000")) {
								checkingStatusCode(memo);
							} else if (s2.equals("4000")) {

								checkingStatusCode(memo);
							} else if (s2.equals("6001")) {

								checkingStatusCode(memo);
							} else if (s2.equals("6002")) {

								checkingStatusCode(memo);
							}
						}else{
							Toast.makeText(VipNOrderXiang.this, "֧��ʧ��!",
									Toast.LENGTH_SHORT).show();
						}
						 Looper.loop(); 
					}
					
				}.start();

			} catch (Exception ex) {
				ex.printStackTrace();
				Toast.makeText(VipNOrderXiang.this, R.string.remote_call_fail,
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(VipNOrderXiang.this, "���������ѶϿ�", Toast.LENGTH_LONG)
					.show();
		}

	}

	@SuppressWarnings("deprecation")
	private void checkingStatusCode(String s) {

		// Toast.makeText(HotelNewOrderYuDingMain.this, msg, 3000).show();
		dialog = new ProgressDialog(VipNOrderXiang.this);
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
	
	

}
