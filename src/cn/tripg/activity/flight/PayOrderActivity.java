package cn.tripg.activity.flight;

import java.net.URLEncoder;

import model.flight.Keys;
import model.flight.Result;
import model.flight.Rsa;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.internet.Exit;
import cn.tripg.R;

import com.alipay.android.app.sdk.AliPay;

@SuppressLint("HandlerLeak")
public class PayOrderActivity extends Activity{

	private static final int RQF_PAY = 1;

	private static final int RQF_LOGIN = 2;
	private String pnr;
	private String orderNo;
	private String totalPrice;
	private TextView pnrTv;
	private TextView orderNoTv;
	private TextView totalPriceTv;
	private TextView payBtn;
	private ProgressDialog dialog;
	private TextView toMain;
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			String result = (String) msg.obj;
			
			
			switch (msg.what) {
			case RQF_PAY:
				
				Toast.makeText(PayOrderActivity.this, "订单支付成功", Toast.LENGTH_LONG).show();
//				checkingStatusCode(memo,true);
				Intent intent = new Intent(PayOrderActivity.this, MainActivity.class);
				startActivity(intent);
				break;

			case 3:
//				Toast.makeText(PayOrderActivity.this, "正在处理...", 5000).show();
//				checkingStatusCode(memo,false);
				break;
			case 4:
//				Toast.makeText(PayOrderActivity.this, "订单支付失败", Toast.LENGTH_LONG).show();
//				checkingStatusCode(memo,false);
				break;
			case 5:
//				Toast.makeText(PayOrderActivity.this, "支付已取消", Toast.LENGTH_LONG).show();
//				checkingStatusCode(memo,false);
				break;
			case 6:
//				Toast.makeText(PayOrderActivity.this, "网络链接出错", Toast.LENGTH_LONG).show();
//				checkingStatusCode(memo,false);
				break;
			default:
				break;
			}
		};
	};
	@SuppressWarnings("deprecation")
	private void checkingStatusCode(String s){
		
		//Toast.makeText(HotelNewOrderYuDingMain.this, msg, 3000).show();
		dialog = new ProgressDialog(PayOrderActivity.this);
		dialog.setMessage(s);
		dialog.setIndeterminate(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {							
				dialog.dismiss();
				
			}
		});

		dialog.show();
	}
	private void prepareAllview(){
		pnrTv = (TextView)findViewById(R.id.pnr_val);
		orderNoTv = (TextView)findViewById(R.id.orderno_val);
		totalPriceTv = (TextView)findViewById(R.id.price_val);
		payBtn = (TextView)findViewById(R.id.zhibubaobtn);
	
		payBtn.setOnClickListener(new PayBtnOnclickListener());
	}
	private void getPassedValues(){
		Intent intent = getIntent();
		if(intent != null){
			Bundle bundle = intent.getExtras();
			if(bundle != null){
				pnr = bundle.getString("pnr");
				orderNo = bundle.getString("orderNo");
				totalPrice = bundle.getString("totalPrice");
				System.out.println("totalPrice ---> " + totalPrice);
				if(pnr != null){
					pnrTv.setText(pnr);
				}
				if(orderNo != null){
					orderNoTv.setText(orderNo);
				}
				if(totalPrice != null){
					totalPriceTv.setText(totalPrice + "元");
				}
			}
		}
	}

	public String getOutTradeNo(){
		return orderNo;
	}
	public String getTotalFee(){
		return totalPrice;
		
	}
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}
	@SuppressWarnings("deprecation")
	private String getNewOrderInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(getOutTradeNo());
		sb.append("\"&subject=\"");
		sb.append("Android机票支付业务");
		sb.append("\"&body=\"");
//		sb.append("客户对预订的机票进行支付");
		sb.append("1");
		sb.append("\"&total_fee=\"");
		sb.append(totalPrice);
		
		sb.append("\"&notify_url=\"");

		// 网址需要做URL编码
//		sb.append(URLEncoder.encode("http://139.210.99.29:83/alipay_wuxian/notify_url.php"));
		sb.append(URLEncoder.encode("http://www.tripg.cn/phone_api/alipay_wuxian/notify_url.php"));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&show_url=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);

		// 如果show_url值为空，可不传 4008009888
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");
		System.out.println("String(sb) ----> " + sb);
		return new String(sb);
	}
	
	
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
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_new);
		Exit.getInstance().addActivity(this);
		prepareAllview();
		getPassedValues();
		
		toMain = (TextView)findViewById(R.id.flight_backtomain);
		toMain.setOnClickListener(new PayBtnOnclickListener());
		
	}
	
	class PayBtnOnclickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.flight_backtomain:
				Intent intent = new Intent(PayOrderActivity.this, MainActivity.class);
				startActivity(intent);
				break;

			case R.id.zhibubaobtn:
				if(getInternet() == true){
					handler.post(new Runnable() {
						String info = getNewOrderInfo();
						
						@Override
						public void run() {
							
							new Thread(){
								@SuppressWarnings("deprecation")
								public void run() {
									try{
										AliPay aliPay= new AliPay(PayOrderActivity.this, handler);
										String sign = Rsa.sign(info, Keys.PRIVATE);
										sign = URLEncoder.encode(sign);
										info += "&sign=\"" + sign + "\"&" + getSignType();
										String result = aliPay.pay(info);
										Log.e("zzz", result);
										//String result = "resultStatus={9000};memo={};result={partner=\"2088801264377844\"&out_trade_no=\"13000480328\"&subject=\"Android机票支付业务\"&body=\"客户对预订的机票进行支付\"&total_fee=\"0.01\"&notify_url=\"http%3A%2F%2Fwww.tripg.com\"&service=\"mobile.securitypay.pay\"&_input_charset=\"UTF-8\"&return_url=\"http%3A%2F%2Fm.alipay.com\"&payment_type=\"1\"&seller_id=\"2088801264377844\"&it_b_pay=\"1m\"&success=\"true\"&sign_type=\"RSA\"&sign=\"XzwTN0FJrTx4yFuwnwAiMlqc8odokn7N+ogC8kBKWr/I0DMWAhDTSHXNKTJQ79oL2zCYrBnH6+4JA7YASq9h3TC9YhKxyvYjuW7Ighwt+gtQiw7A9f5aOPVkAbiNbI3iEybTxU8m8s+9wTpIbCHCRrk4vn9jf8Y1rZZd5v+8VwI=\"}";
										System.out.println("filght pay result ---> " + result);
										System.out.println("2.result ---> " + result);
										String s[] = result.split(";");
										System.out.println("s[0] ---> " + s[0]);
										//String s1[] = s[0].split("{");
										String s2 = s[0].substring(14, s[0].length()-1);
										//String s3[] = s[1].split("{");
										String memo = s[1].substring(6,s[1].length()-1);
										System.out.println("s2 ---> " + s2);
										System.out.println("memo ---> " + memo);
										if(s2.equals("9000")){
											Log.e("订单支付成功", "");
											Message msg = new Message();
											msg.what = RQF_PAY;
											msg.obj = result;
											handler.sendMessage(msg);
											//checkingStatusCode(memo,true);
										}else if(s2.equals("8000")){
//											Message msg = new Message();
//											msg.what = 3;
//											msg.obj = result;
//											handler.sendMessage(msg);
//											checkingStatusCode(memo);
											checkingStatusCode(memo);
										}else if(s2.equals("4000")){
//											Message msg = new Message();
//											msg.what = 4;
//											msg.obj = result;
//											handler.sendMessage(msg);
//											checkingStatusCode(memo);
											checkingStatusCode(memo);
										}else if(s2.equals("6001")){
//											Message msg = new Message();
//											msg.what = 5;
//											msg.obj = memo;
//											handler.sendMessage(msg);
//											checkingStatusCode(memo);
											checkingStatusCode(memo);
										}else if(s2.equals("6002")){
//											Message msg = new Message();
//											msg.what = 6;
//											msg.obj = result;
//											handler.sendMessage(msg);
//											checkingStatusCode(memo);
											checkingStatusCode(memo);
										}
										
									}catch(Exception e){

									}
								}
							}.start();
						}
					});
				}else{
					Toast.makeText(PayOrderActivity.this, "网络链接已断开", Toast.LENGTH_LONG).show();
				}

				break;
			default:
				break;
			}

		}
		
	}
}
