package cn.youlun.main;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONObject;

import cn.internet.Exit;
import cn.internet.Tools;
import cn.lvyou.main.LvYouYuDingMain;
import cn.qianzheng.main.QianYudingMain;
import cn.tripg.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class YouLunYuDingMain extends Activity {

	public String cangString;
	public String jiaGeString;
	public String titleString;

	private TextView titTextView;
	private TextView jiaTextView;
	private TextView neiTextView;
	private TextView zongjTextView;
	private TextView tijiaoTextView;

	private EditText namEditText;
	private EditText phoneEditText;
	private EditText emEditText;
	private EditText bzEditText;
	private String beizhuString;

	private String userName;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.youlunyudingmain);
		Exit.getInstance().addActivity(this);
		ImageView imageViewback = (ImageView) findViewById(R.id.title_youlun_back);
		imageViewback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				finish();
			}
		});

		titleString = getIntent().getStringExtra("title");
		jiaGeString = getIntent().getStringExtra("jia");
		cangString = getIntent().getStringExtra("cang");
		Log.e("仓位信息预订界面-----", titleString + "------" + jiaGeString + "------"
				+ cangString);

		titTextView = (TextView) findViewById(R.id.textView2);
		titTextView.setText(titleString);
		neiTextView = (TextView) findViewById(R.id.textView21);
		neiTextView.setText(cangString);
		jiaTextView = (TextView) findViewById(R.id.textView4);
		jiaTextView.setText("￥" + jiaGeString);
		zongjTextView = (TextView) findViewById(R.id.textView13);
		zongjTextView.setText("总价金额:￥" + jiaGeString);
		tijiaoTextView = (TextView) findViewById(R.id.textView14);
		tijiaoTextView.setOnClickListener(new YoulunTijiaoyuding());

		namEditText = (EditText) findViewById(R.id.editText1);
		phoneEditText = (EditText) findViewById(R.id.editText2);
		emEditText = (EditText) findViewById(R.id.editText3);
		bzEditText = (EditText)findViewById(R.id.editText4);//
		
		Tools tools = new Tools();
		userName = tools.getUserName(getApplicationContext());
	}

	private boolean validation(){
		if(namEditText.getText().toString().isEmpty()){
			Toast.makeText(YouLunYuDingMain.this, "请填写联系人", Toast.LENGTH_LONG).show();
			return false;
		}else if(phoneEditText.getText().toString().isEmpty()){
			Toast.makeText(YouLunYuDingMain.this, "请填写手机号", Toast.LENGTH_LONG).show();
			return false;
			
		}else if(phoneEditText.getText().toString().length() != 11){
			Toast.makeText(YouLunYuDingMain.this, "请检查手机号", Toast.LENGTH_LONG).show();
			return false;
			
		}else if(!(emEditText.getText().toString().matches("[a-zA-Z0-9._-]+@[a-zA-Z0-9]+[a-zA-Z0-9]+\\.[a-zA-Z]+[a-zA-Z]+[a-zA-Z]"))){
			Toast.makeText(YouLunYuDingMain.this, "邮箱格式错误", Toast.LENGTH_LONG).show();
			return false;
		}else if(emEditText.getText().toString().isEmpty()){
			Toast.makeText(YouLunYuDingMain.this, "请填写邮箱地址", Toast.LENGTH_LONG).show();
			return false;
			
		}else{
			return true;
		}
	}
	class YoulunTijiaoyuding implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.e("YoulunTijiaoyuding", "YoulunTijiaoyuding");
			if (bzEditText.getText().toString().length() == 0) {
				beizhuString = "无";
			}else {
				beizhuString = bzEditText.getText().toString();
			}
			if(validation() == false){
				
			}else{
				String urlString = "http://www.tripg.cn/phone_api/trave/index.php/cruise/add_cruise?types=y"
						+ "&realname="
						+ getStringUTF8(namEditText.getText().toString())
						+ "&telphone="
						+ phoneEditText.getText().toString()
						+ "&email="+emEditText.getText().toString()+"&cangwei="+getStringUTF8(cangString)+"&remark="+getStringUTF8(beizhuString)
						+ "&username="
						+ userName;
				Log.e("预订 url----", ""+urlString);
				
				Tools tools = new Tools();
				String resultString = tools.getURL(urlString);
				Log.e("游轮预订接口在此获取 返回值---", "" + resultString);
				
				try {
					
					JSONObject jsonObject = new JSONObject(resultString);
					String codeString = jsonObject.getString("Code");
					if (codeString.equals("1")) {
						Toast.makeText(YouLunYuDingMain.this, "预订成功！稍后会有客服联系您！",
								Toast.LENGTH_SHORT).show();
						finish();
					}else {
						Toast.makeText(YouLunYuDingMain.this, "预订失败！请稍后再试！",
								Toast.LENGTH_SHORT).show();
					}
					
					
				} catch (Exception e) {
					// TODO: handle exception
					
				}
			}

			
			

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
}
