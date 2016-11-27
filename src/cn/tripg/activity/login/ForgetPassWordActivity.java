package cn.tripg.activity.login;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import tools.des.Api;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import cn.internet.Exit;
import cn.tripg.R;
import cn.tripg.activity.flight.ResultCode;
import cn.tripg.activity.hotel.HotelOrderSucces;
import cn.tripg.activity.hotel.HotelOrderYuDingMain;
import cn.tripg.widgit.ProgressDialogTripg;

public class ForgetPassWordActivity extends Activity {

	private EditText userName;
	private ImageView resetPassWord;
	private Api api;
	public ProgressDialog progressDialog1;
	public String userPass;
	public String cellPhone;

	private void prepareAllView() {
		api = new Api();
		userName = (EditText) findViewById(R.id.user_name);
		resetPassWord = (ImageView) findViewById(R.id.btn_reset);
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_back_pswd);
		Exit.getInstance().addActivity(this);
		prepareAllView();

		ImageView backImageView = (ImageView) findViewById(R.id.title_back);
		backImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = getIntent();
				setResult(ResultCode.FAILURE, intent);
				finish();
			}
		});

		resetPassWord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				progressDialog1 = ProgressDialog.show(ForgetPassWordActivity.this, null, "申请处理中...");
				String username = userName.getText().toString();
				if ("".equals(username.trim())) {
					Toast.makeText(ForgetPassWordActivity.this, "请填写用户名",
							Toast.LENGTH_SHORT).show();
					return;
				}
				
				final String resetPswdURL = "http://www.tripg.cn/phone_api/shouji/index.php/UserApi/queryUser_byUserName?username="
						+ username;
				Log.e("zzz", resetPswdURL);
				final String result = api.doGetData(resetPswdURL);
				Log.e("result", result);
				try {
					JSONObject err = new JSONObject(result);
					Log.e("1111111---------", ""+result);
					String e = err.getString("Code");
					Log.e("e---", "" + e);
					if ("1".equals(e)) {
						// Toast.makeText(ForgetPassWordActivity.this, "重置成功",
						// Toast.LENGTH_SHORT).show();
						// finish();
						JSONObject resultJsonObject = err
								.getJSONObject("Result");
						userPass = (String) resultJsonObject
								.getString("userpass");
						cellPhone = (String) resultJsonObject
								.getString("cellphone");
						if (cellPhone.length()==0) {
							cellPhone = username;
						}
						Log.e("下载成功之后的数据  ", "" + userPass + cellPhone);
						try {
		
							httpNumPhone();

						} catch (UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					} else {
						progressDialog1.dismiss();
						Toast.makeText(ForgetPassWordActivity.this, "用户名有误",
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					progressDialog1.dismiss();
					Toast.makeText(ForgetPassWordActivity.this, "解析数据失败",
							Toast.LENGTH_SHORT).show();
					Log.e("zzz", "ForgetPassWordActivity 解析json数据失败");
				}

			}
		});
	}

	public void httpNumPhone() throws UnsupportedEncodingException {

		String url = "http://www.tripg.cn/sendmessage/demo_gbk.php?";
		Log.e("发送短信接口 调用", "" + url);
		try {
			  List<NameValuePair> params = new 
					  ArrayList<NameValuePair>(); 
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			 params.add(new BasicNameValuePair
					 ("password", userPass));  
			 params.add(new BasicNameValuePair
					 ("telphone", cellPhone));  
			 httppost.setEntity(new 
					 UrlEncodedFormEntity(params,HTTP.UTF_8)); 
			HttpResponse response;
			response = httpclient.execute(httppost);
			String rev1 = EntityUtils.toString(response.getEntity());

			if (rev1.length() != 0) {
				
				Log.e("http response is ", rev1);
				Toast.makeText(ForgetPassWordActivity.this, "找回成功，请查收短信!",
						Toast.LENGTH_SHORT).show();
				progressDialog1.dismiss();
				finish();
			}

		} catch (ClientProtocolException e) {

		} catch (IOException e) {

		} catch (Exception e) {

		}
	}

}
