package cn.vip.main;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import cn.internet.Exit;
import cn.internet.Tools;
import cn.tripg.R;
import cn.tripg.widgit.ProgressDialogTripg;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FeedBackActivity extends Activity{

	private ImageView back;
	private TextView submit;
	private EditText feedback;
	private EditText email;
	private EditText phone;
	
	private String fbUrl = "http://www.tripg.cn/phone_api/customer_opinion.php";
	private Tools tools;
	private ProgressDialog progressDialog;
	
	private void prepareView(){
		back = (ImageView) findViewById(R.id.fb_back);
		submit = (TextView) findViewById(R.id.fb_submit);
		feedback = (EditText) findViewById(R.id.fb_et);
		email = (EditText) findViewById(R.id.fb_eamils);
		phone = (EditText) findViewById(R.id.fb_phone);
		
		tools = new Tools();
	}
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		Exit.getInstance().addActivity(this);
		prepareView();
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(validation() == true){
					new AsyncTask<Void, Void, Void>() {


						@Override
						protected void onPreExecute() {
							// TODO Auto-generated method stub
							super.onPreExecute();
							progressDialog = ProgressDialogTripg.show(FeedBackActivity.this, null, null);
						}

						@Override
						protected void onPostExecute(Void result) {
							// TODO Auto-generated method stub
							super.onPostExecute(result);
							progressDialog.dismiss();
						}

						@Override
						protected Void doInBackground(Void... params) {
							// TODO Auto-generated method stub
							try {
								String data = tools.doPostData(fbUrl, postData());
								System.out.println("data ---> " + data);
								
								JSONObject job = new JSONObject(data);
								String message = job.getString("Message");
								Message msg = new Message();
								msg.obj = message;
								handler.sendMessage(msg);
								
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							
							return null;
						}

					}.execute();
				}
			}
		});
		
	}

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			String text = (String) msg.obj;
			Toast.makeText(FeedBackActivity.this, text, Toast.LENGTH_LONG).show();
			finish();
		}
		
	};
	private boolean validation(){
		if(feedback.getText().length() == 0){
			Toast.makeText(FeedBackActivity.this, "请填写建议及意见", Toast.LENGTH_LONG).show();
			return false;
		}else if(phone.getText().length() != 11){
			Toast.makeText(FeedBackActivity.this, "请检查手机号是否输入正确", Toast.LENGTH_LONG).show();
			return false;
		}else if(phone.getText().length() == 0){
			Toast.makeText(FeedBackActivity.this, "请填写手机号", Toast.LENGTH_LONG).show();
			return false;
		}else if(email.getText().toString().isEmpty()){
			Toast.makeText(FeedBackActivity.this, "请填写邮箱地址", Toast.LENGTH_LONG).show();
			return false;
			
		}else if(!(email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-zA-Z0-9]+[a-zA-Z0-9]+\\.[a-zA-Z]+[a-zA-Z]+[a-zA-Z]"))){
			Toast.makeText(FeedBackActivity.this, "邮箱格式错误", Toast.LENGTH_LONG).show();
			return false;
		}else{
			return true;
		}
	}
	
	private String postData(){
		StringBuilder sb = new StringBuilder();
		sb.append("username=");
		sb.append(tools.getUserName(getApplicationContext()));
		sb.append("&suggest=");
		sb.append(feedback.getText().toString());
		sb.append("&tel=");
		sb.append(phone.getText().toString());
		sb.append("&email=");
		if(email.getText().length() == 0){
			sb.append("");
		}else{
			sb.append(email.getText().toString());
		}
		sb.append("&plat=android企业版");
		Log.e("postData", sb.toString());
		return sb.toString();
	}
	
}
