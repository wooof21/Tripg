package cn.lvyou.main;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
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
import cn.qianzheng.main.QianYudingMain;
import cn.tripg.R;
import cn.tripg.activity.flight.DateSelectActivity;
import cn.tripg.activity.flight.RequestCode;
import cn.tripg.activity.flight.ResultCode;


public class LvYouYuDingMain extends Activity {

	public String tidString;

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

	public String nameString;
	public String picString;
	public String zongString;
	public String currentDate;
	public String yuDataString;
	public String userIdString;
	public String countryIdString;
	public String visaTypeidString;
	public int k = 1;

	private String userName;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lvyouyudingmain);
		Exit.getInstance().addActivity(this);
		ImageView imageViewback = (ImageView) findViewById(R.id.title_lvyouyu_back);
		imageViewback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				finish();
			}
		});
		nameString = getIntent().getStringExtra("n");
		picString = getIntent().getStringExtra("p");
		tidString = (String) getIntent().getStringExtra("tid");
		Log.e("lvyou  预订--", tidString);

		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		currentDate = year + "-" + month + "-" + day;
		yuDataString = currentDate;

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
		emailEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		
		zongjiaTextView = (TextView) findViewById(R.id.textView13);
		zongjiaTextView.setText("￥"+picString);
		tijiaoTextView = (TextView) findViewById(R.id.textView14);
		tijiaoTextView.setOnClickListener(new LvTiJiaoYuding());
		LinearLayout dataLayout = (LinearLayout) findViewById(R.id.linearLayoutyu1);
		dataLayout.setOnClickListener(new DataLinearLayout());

		Tools tools = new Tools();
		userName = tools.getUserName(getApplicationContext());
		
	}

	private boolean validation(){
		if(psoEditText.getText().toString().isEmpty()){
			Toast.makeText(LvYouYuDingMain.this, "请填写联系人", Toast.LENGTH_LONG).show();
			return false;
		}else if(phoneEditText.getText().toString().isEmpty()){
			Toast.makeText(LvYouYuDingMain.this, "请填写手机号", Toast.LENGTH_LONG).show();
			return false;
			
		}else if(phoneEditText.getText().toString().length() != 11){
			Toast.makeText(LvYouYuDingMain.this, "请检查手机号", Toast.LENGTH_LONG).show();
			return false;
			
		}else if(emailEditText.getText().toString().isEmpty()){
			Toast.makeText(LvYouYuDingMain.this, "请填写邮箱地址", Toast.LENGTH_LONG).show();
			return false;
			
		}else if(!(emailEditText.getText().toString().matches("[a-zA-Z0-9._-]+@[a-zA-Z0-9]+[a-zA-Z0-9]+\\.[a-zA-Z]+[a-zA-Z]+[a-zA-Z]"))){
			Toast.makeText(LvYouYuDingMain.this, "邮箱格式错误", Toast.LENGTH_LONG).show();
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

		Intent intent = new Intent(LvYouYuDingMain.this,
				DateSelectActivity.class);

		Bundle bundle = new Bundle();
		if (requestCode == RequestCode.TO_SELECT_DATE) {
			bundle.putString("liveDate",
					stringToDate(currentDate, "yyyy-MM-dd"));
			bundle.putInt("hotelOrflight", 0);
		}
		intent.putExtra("type", "t");
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
			int l = j * k;
			zongjiaTextView.setText("￥" + l);
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
				int l = j * k;
				zongjiaTextView.setText("￥" + l);
				k--;
			} else {
				Toast.makeText(LvYouYuDingMain.this, "订单数不能小于1",
						Toast.LENGTH_SHORT).show();
			}

		}

	}

	private boolean isUserLogin() {
		SharedPreferences sharedPre = LvYouYuDingMain.this
				.getSharedPreferences("config", Context.MODE_PRIVATE);

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

	class LvTiJiaoYuding implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.e("TiJiaoYuding", "----TiJiaoYuding");

			if(validation() == false){
				
			}else{
				String urlString = "http://www.tripg.cn/phone_api/trave/index.php/Travel/add_travel?title="
						+ getStringUTF8(nameString)
						+ "&price="
						+ picString
						+ "&personnum="
						+ numTextView.getText()
						+ "&personname="
						+ getStringUTF8(psoEditText.getText().toString())
								+ "&phone=" + phoneEditText.getText().toString()
								+ "&email=" + emailEditText.getText().toString()
								+ "&go_date="+yuDataString
						+ "&username="
						+ userName;
				Log.e("urlstring-----", ""+urlString);
				Tools tools = new Tools();
				String resultString = tools.getURL(urlString);
				Log.e("旅游预订接口在此获取 返回值", "" + resultString);
				try {
					
					JSONObject jsonObject = new JSONObject(resultString);
					String codeString = jsonObject.getString("Code");
					if (codeString.equals("1")) {
						Toast.makeText(LvYouYuDingMain.this, "预订成功！稍后会有客服联系您！",
								Toast.LENGTH_SHORT).show();
						finish();
					}else {
						Toast.makeText(LvYouYuDingMain.this, "预订失败",
								Toast.LENGTH_SHORT).show();
					}
					
					
				} catch (Exception e) {
					// TODO: handle exception
					
				}
				
			}

			

		}

	}

}
