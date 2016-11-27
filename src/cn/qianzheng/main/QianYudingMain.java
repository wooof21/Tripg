package cn.qianzheng.main;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.internet.Exit;
import cn.internet.Tools;
import cn.lvyou.main.LvYouYuDingMain;
import cn.tripg.R;
import cn.tripg.activity.flight.DateSelectActivity;
import cn.tripg.activity.flight.RequestCode;
import cn.tripg.activity.flight.ResultCode;


public class QianYudingMain extends Activity {

	private TextView nameTextView;
	private TextView picTextView;
	private TextView dateTextView;
	private TextView numTextView;
	private TextView zongjiaTextView;
	private TextView tijiaoTextView;
	private ImageView addImageView;
	private ImageView micImageView;
	private EditText psoEditText;
	private EditText phoneEditText;
	private EditText emailEditText;
	private EditText addresEditText;

	public String nameString;
	public String picString;
	public String idString;
	public String zongString;
	public String currentDate;
	public String yuDataString;
	public String userIdString;
	public String countryIdString;
	public String visaTypeidString;
	private String typeName;
	public int k = 1;

	private String userName;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qianyudingmain);
		Exit.getInstance().addActivity(this);

		ImageView imageViewback = (ImageView) findViewById(R.id.title_qianzheng_back);
		imageViewback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				finish();
			}
		});

		nameString = getIntent().getStringExtra("n");
		picString = getIntent().getStringExtra("p");
		idString = getIntent().getStringExtra("d");
		countryIdString = getIntent().getStringExtra("c");
		visaTypeidString = getIntent().getStringExtra("t");
		typeName = getIntent().getStringExtra("typeName");
		
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		currentDate = year + "-" + month + "-" + day;
		yuDataString =currentDate;
		nameTextView = (TextView) findViewById(R.id.textView2);
		nameTextView.setText(nameString);
		picTextView = (TextView) findViewById(R.id.textView4);
		picTextView.setText("￥"+picString);

		dateTextView = (TextView) findViewById(R.id.textView6);
		dateTextView.setText(currentDate);
		numTextView = (TextView) findViewById(R.id.textView8);
		numTextView.setText("" + k);

		addImageView = (ImageView) findViewById(R.id.imageView2);
		addImageView.setOnClickListener(new Addnumber());
		micImageView = (ImageView) findViewById(R.id.imageView1);
		micImageView.setOnClickListener(new MicNumber());

		psoEditText = (EditText) findViewById(R.id.editText1);
		phoneEditText = (EditText) findViewById(R.id.editText2);
		emailEditText = (EditText) findViewById(R.id.editText3);
		addresEditText = (EditText) findViewById(R.id.editText4);

		zongjiaTextView = (TextView) findViewById(R.id.textView13);
		zongjiaTextView.setText("￥"+picString);
		tijiaoTextView = (TextView) findViewById(R.id.textView14);
		tijiaoTextView.setOnClickListener(new TiJiaoYuding());
		LinearLayout dataLayout = (LinearLayout) findViewById(R.id.linearLayoutyu1);
		dataLayout.setOnClickListener(new DataLinearLayout());

		Tools tools = new Tools();
		userName = tools.getUserName(getApplicationContext());
	}

	private boolean validation(){
		if(psoEditText.getText().toString().isEmpty()){
			Toast.makeText(QianYudingMain.this, "请填写联系人", Toast.LENGTH_LONG).show();
			return false;
		}else if(phoneEditText.getText().toString().isEmpty()){
			Toast.makeText(QianYudingMain.this, "请填写手机号", Toast.LENGTH_LONG).show();
			return false;
			
		}else if(phoneEditText.getText().toString().length() != 11){
			Toast.makeText(QianYudingMain.this, "请检查手机号", Toast.LENGTH_LONG).show();
			return false;
			
		}else if(emailEditText.getText().toString().isEmpty()){
			Toast.makeText(QianYudingMain.this, "请填写邮箱地址", Toast.LENGTH_LONG).show();
			return false;
			
		}else if(!(emailEditText.getText().toString().matches("[a-zA-Z0-9._-]+@[a-zA-Z0-9]+[a-zA-Z0-9]+\\.[a-zA-Z]+[a-zA-Z]+[a-zA-Z]"))){
			Toast.makeText(QianYudingMain.this, "邮箱格式错误", Toast.LENGTH_LONG).show();
			return false;
		}else if(addresEditText.getText().toString().isEmpty()){
			Toast.makeText(QianYudingMain.this, "请填写地址", Toast.LENGTH_LONG).show();
			return false;
			
		}else{
			return true;
		}
	}
	private String stringToDate(String dateStr, String formatStr) {
		DateFormat sdf = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String str = sdf.format(date);
		return str;
	}

	private void selectDate(int requestCode) {

		Intent intent = new Intent(QianYudingMain.this,
				DateSelectActivity.class);

		Bundle bundle = new Bundle();
		if (requestCode == RequestCode.TO_SELECT_DATE) {
			bundle.putString("liveDate",
					stringToDate(currentDate, "yyyy-MM-dd"));
			bundle.putInt("hotelOrflight", 0);
		}
		intent.putExtra("type", "v");
		intent.putExtras(bundle);
		startActivityForResult(intent, requestCode);

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case RequestCode.TO_SELECT_DATE:
			if (resultCode == ResultCode.SUCCESS) {
				Bundle bundle = data.getExtras();
				String dateSelected = bundle.getString("date");
				Log.e("date111111111", dateSelected);

				dateTextView.setText(dateSelected);
				yuDataString = dateSelected;
			}
			break;

		}

	}

	class DataLinearLayout implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.e("DataLinearLayout", "----DataLinearLayout");
			selectDate(RequestCode.TO_SELECT_DATE);

		}

	}

	class Addnumber implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.e("Addnumber", "----Addnumber");
			k++;
			numTextView.setText("" + k);
			int j = Integer.valueOf(picString);
			int l = j*k;
			zongjiaTextView.setText("￥"+l);
		}

	}

	class MicNumber implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.e("MicNumber", "----MicNumber");
			if (k != 0) {
				numTextView.setText("" + k);
				int j = Integer.valueOf(picString);
				int l = j*k;
				zongjiaTextView.setText("￥"+l);
				k--;
			} else {
				Toast.makeText(QianYudingMain.this, "订单数不能小于1",
						Toast.LENGTH_SHORT).show();
			}

		}

	}

	private boolean isUserLogin() {
		SharedPreferences sharedPre = QianYudingMain.this.getSharedPreferences(
				"config", Context.MODE_PRIVATE);

		userIdString = sharedPre.getString("Result", "");
		Log.e("userIdString", "---------" + userIdString);
		if ("".equals(userIdString)) {
			return false;
		} else {
			return true;
		}
	}

	public String getStringUTF8(String xml) {
		// A StringBuffer Object
		StringBuffer sb = new StringBuffer();
		sb.append(xml);
		String xmString = "";
		String xmlUTF8 = "";
		try {
			xmString = new String(sb.toString().getBytes("UTF-8"));
			xmlUTF8 = URLEncoder.encode(xmString, "UTF-8");
			// System.out.println("utf-8 编码：" + xmlUTF8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// return to String Formed
		return xmlUTF8;
	}

	class TiJiaoYuding implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.e("TiJiaoYuding", "----TiJiaoYuding");
			if (isUserLogin()) {
				//String urlString = "http://192.168.2.17/phone_api/trave/index.php/visa/book_Visa_order";
				if(validation() == false){
					
				}else{
					String urlString = "http://www.tripg.cn/phone_api/trave/index.php/visa/book_Visa_order";
					String parmeString = "MemberId=" + userIdString + "&ProductId="
							+ idString + "&Distributor=android_Q&CountryId="
							+ countryIdString + "&TypeId=" + visaTypeidString
							+ "&Quantity=" + numTextView.getText() + "&OutDate="
							+ yuDataString + "&ReturnDate=" + yuDataString
							+ "&ContactName="
							+ getStringUTF8(psoEditText.getText().toString())
							+ "&ContactMobile="
							+ phoneEditText.getText().toString()
							+ "&ContactAddress="
							+ getStringUTF8(addresEditText.getText().toString())
							+ "&ContactMail=" + emailEditText.getText().toString()
							+ "&username="
							+ userName
							+ "&Price=" + picString
							+ "&CountryName=" + getStringUTF8(nameString)
							+ "&TypeName=" + getStringUTF8(typeName);
					Log.e("预订上传 参数 ", "url----"+urlString+"-------parme--"+parmeString);
					//url----http://www.tripg.cn/phone_api/trave/index.php/visa/book_Visa_order-------parme MemberId=60000028&ProductId=SLLK001&Distributor=android_Q&CountryId=31&TypeId=134&Quantity=1&OutDate=2015-1-29&ReturnDate=2015-1-29&ContactName=%E5%BC%A0%E4%B8%89%E6%B5%8B&ContactMobile=18686340001&ContactAddress=%E9%95%BF%E6%98%A5%E5%B8%82%E9%AB%98%E6%96%B0%E5%8C%97%E5%8C%BA&ContactMail=gjfdf.com
					//{'Code':'0','Message':'fail','Result':''}
					//{'Code':'1','Message':'success','Result':''}
					
					try {

						byte[] xmlbyte = parmeString.getBytes("UTF-8");
						Log.e("预订接口上传 格式的内容---utf8---", parmeString);

						URL url = new URL(urlString);

						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setConnectTimeout(5000);
						conn.setDoOutput(true);// 允许输出
						conn.setDoInput(true);
						conn.setUseCaches(false);// 不使用缓存
						conn.setRequestMethod("POST");
						conn.getOutputStream().write(xmlbyte);
						conn.getOutputStream().flush();
						conn.getOutputStream().close();

						Log.e("conn.getResponseCode()----", "" + conn.getResponseCode());
						if (conn.getResponseCode() != 200)
							throw new RuntimeException("请求url失败");
						int codeOrder = conn.getResponseCode();

						if (codeOrder == 200) {

							InputStream inStream = conn.getInputStream();// 获取返回数据

							// 使用输出流来输出字符(可选)
							ByteArrayOutputStream out = new ByteArrayOutputStream();
							byte[] buf = new byte[1024];
							int len;
							while ((len = inStream.read(buf)) != -1) {
								out.write(buf, 0, len);
							}
							String string = out.toString("UTF-8");						
							Log.e("预订返回结果--------", ""+string);
							out.close();

							try {
								JSONObject jsonObject = new JSONObject(string);
								String codeString = jsonObject.getString("Code");
								if (codeString.equals("1")) {
									Toast.makeText(QianYudingMain.this, "预订成功！稍后会有客服联系您！",
											Toast.LENGTH_SHORT).show();
									finish();
								}else {
									Toast.makeText(QianYudingMain.this, "预订失败",
											Toast.LENGTH_SHORT).show();
								}
								
							} catch (Exception e) {
								// TODO: handle exception
							}
							
						} else {
							Toast.makeText(QianYudingMain.this, "预订失败",
									Toast.LENGTH_SHORT).show();
						}

					} catch (Exception e) {
						Log.e("解析出错了 -----", "" + e);
						Toast.makeText(QianYudingMain.this, "预订失败",
								Toast.LENGTH_SHORT).show();
					}
				}

				

			}

		}

	}

}
