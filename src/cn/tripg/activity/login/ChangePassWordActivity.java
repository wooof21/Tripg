package cn.tripg.activity.login;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import model.user.LoginResponse;

import cn.internet.Exit;
import cn.tripg.activity.flight.MainActivity;
import cn.tripg.widgit.ProgressDialogTripg;
import cn.tripg.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ChangePassWordActivity extends Activity{

	private String memberId;
	private String username;
	private EditText pass1;
	private EditText pass2;
	private EditText pass3;
	private ImageView submit;
	private ImageView back;
	private SharedPreferences sharedPre;
    private Editor editor;
    
	private ProgressDialog progressDialog;
	private String getURL(String urlStr) {
		HttpURLConnection httpcon = null;
		InputStream in = null;
		String data = "";
		StringBuilder builder = new StringBuilder();

		try {
			URL url = new URL(urlStr);
			httpcon = (HttpURLConnection) url.openConnection();
			httpcon.setDoInput(true);
			httpcon.setDoOutput(true);
			httpcon.setUseCaches(false);
			httpcon.setRequestMethod("GET");
			in = httpcon.getInputStream();
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader bufferReader = new BufferedReader(isr);
			String input = "";
			while ((input = bufferReader.readLine()) != null) {
				data = input + data;
			}
			// readJson(data);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (httpcon != null) {
				httpcon.disconnect();
			}
		}

		return data;

	}
	private void saveLoginInfo(Context context, String pswd){
        sharedPre =
                context.getSharedPreferences("config", Context.MODE_PRIVATE);
        editor = sharedPre.edit();
        editor.clear();
        editor.putString("username", username);
        editor.putString("password", pswd);
        editor.putString("Result", memberId);
        editor.commit();
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_password);
		Exit.getInstance().addActivity(this);
		pass1 = (EditText)findViewById(R.id.change_pass_orignal);
		pass2 = (EditText)findViewById(R.id.change_pass_new);
		pass3 = (EditText)findViewById(R.id.change_pass_retype);
		submit = (ImageView)findViewById(R.id.change_pass_submit);
		back = (ImageView)findViewById(R.id.change_pass_back);
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		
		Intent intent  = getIntent();
		memberId = intent.getExtras().getString("memberId");
		username = intent.getExtras().getString("username");
		Log.e("ChangePassWordActivity memberId", memberId);
		Log.e("ChangePassWordActivity username", username);
		
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(pass1.getText().toString().equals("")){
					Toast.makeText(ChangePassWordActivity.this, "请输入原密码", Toast.LENGTH_LONG).show();
				}else if(pass2.getText().toString().equals("")){
					Toast.makeText(ChangePassWordActivity.this, "请输入新密码", Toast.LENGTH_LONG).show();
				}else if(pass3.getText().toString().equals("")){
					Toast.makeText(ChangePassWordActivity.this, "请确认新密码", Toast.LENGTH_LONG).show();
				}else{
					if (pass2.getText().toString().equals(pass3.getText().toString())) {
						progressDialog = ProgressDialogTripg.show(ChangePassWordActivity.this, null, null);
						String tokenString = "6QIp1iWQ3LePoFCykN6h_RgyOA6nL3-U";
				        final String loginURL = "http://mapi.tripglobal.cn/MemApi.aspx?action=Login"
				        + "&username=" + username
				        + "&password=" + pass1.getText().toString()
				        + "&token="+ tokenString;
				        String changeUrl = "http://mapi.tripglobal.cn/MemApi.aspx?action=ModifyMember&memberId="
				        		+ memberId
				        		+"&PassWord="
				        		+ pass2.getText().toString()
				        		+ "&token=IEEW9lg9hHa1qMC2LrAgHuluilAAX_q0";
				        Log.e("", loginURL);
				        Log.e("changeUrl", changeUrl);	
				        String result = getURL(loginURL);
				        System.out.println("result ---> " + result);
				        try {
							JSONObject job = new JSONObject(result);
							String code = job.getString("Code");
							if(code.equals("1")){
								if(pass2.getText().toString().equals(pass3.getText().toString())){
									String result1 = getURL(changeUrl);
									System.out.println("result1 ---> " + result1);
									JSONObject job1 = new JSONObject(result1);
									String code1 = job1.getString("Code");
									if(code1.equals("1")){
										progressDialog.dismiss();
										Toast.makeText(ChangePassWordActivity.this, "修改密码成功", Toast.LENGTH_LONG).show();
										saveLoginInfo(ChangePassWordActivity.this, pass2.getText().toString());
										Intent intent1 = new Intent(ChangePassWordActivity.this, MainActivity.class);
										startActivity(intent1);
									}else{
										progressDialog.dismiss();
										Toast.makeText(ChangePassWordActivity.this, job1.getString("Message").toString(), Toast.LENGTH_LONG).show();
									}
								}else{
									progressDialog.dismiss();
									Toast.makeText(ChangePassWordActivity.this, "新密码俩次输入不同", Toast.LENGTH_LONG).show();
								}
							}else{
								progressDialog.dismiss();
								Toast.makeText(ChangePassWordActivity.this, "原密码输入错误", Toast.LENGTH_LONG).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							progressDialog.dismiss();
							e.printStackTrace();
						}
					}else {
						Toast.makeText(ChangePassWordActivity.this, "新密码俩次输入不同", Toast.LENGTH_LONG).show();
					}
				}
				
				
				
				
				
				
			}
		});
		
	}
	
	
	

}
