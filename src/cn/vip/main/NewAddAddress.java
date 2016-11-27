package cn.vip.main;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import cn.internet.Exit;
import cn.internet.Tools;
import cn.tripg.R;
import cn.tripg.activity.flight.ResultCode;
import cn.tripg.widgit.ProgressDialogTripg;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NewAddAddress extends Activity{

	private ImageView back;
	private EditText address;
	private EditText code;
	private TextView save;
	
	private ProgressDialog progressDialog;
	private String eora;
	private String position;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_add_address);
		Exit.getInstance().addActivity(this);
		back = (ImageView)findViewById(R.id.naa_back);
		address = (EditText)findViewById(R.id.naa_address);
		code = (EditText)findViewById(R.id.naa_code);
		save = (TextView)findViewById(R.id.naa_save);
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(ResultCode.FAILURE);
				finish();
			}
		});
		

		
		String province = getIntent().getExtras().getString("province");
		String city = getIntent().getExtras().getString("city");
		String area = getIntent().getExtras().getString("area");
		String addressT = getIntent().getExtras().getString("address");
		String codeT = getIntent().getExtras().getString("code");
		
		address.setText(province + city + area + addressT);
		code.setText(codeT);
		
		eora = getIntent().getExtras().getString("eora");
		position = getIntent().getExtras().getString("id");
		
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(address.getText().length() == 0){
					Toast.makeText(NewAddAddress.this, "«Î±‡º≠µÿ÷∑!", Toast.LENGTH_LONG).show();
				}else if(code.getText().length() == 0){
					Toast.makeText(NewAddAddress.this, "«Î±‡º≠” ±‡!", Toast.LENGTH_LONG).show();
				}else{
					new AsyncTask<Void, Void, String>() {

						
						@Override
						protected void onPreExecute() {
							// TODO Auto-generated method stub
							super.onPreExecute();
							progressDialog = ProgressDialogTripg.show(NewAddAddress.this, null, null);
						}

						@Override
						protected void onPostExecute(String result) {
							// TODO Auto-generated method stub
							super.onPostExecute(result);
							progressDialog.dismiss();
							if(result.equals("1")){
								setResult(ResultCode.SUCCESS);
								finish();
							}
						}

						@Override
						protected String doInBackground(Void... params) {
							// TODO Auto-generated method stub
							String code = "";
							String data = "";
							try {
								if(eora.equals("a")){
									data = new Tools().doPostData("http://www.tripg.cn/phone_api/contact_information/api.php", postAData());
									System.out.println(data);
								}else{
									data = new Tools().doPostData("http://www.tripg.cn/phone_api/contact_information/api.php", postEData());
									System.out.println(data);
								}
								
								JSONObject job = new JSONObject(data);
								code = job.getString("Code");
								String text = job.getString("Message");
								Message msg = handler.obtainMessage();
								msg.obj = text;
								handler.sendMessage(msg);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							return code;
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
			Toast.makeText(NewAddAddress.this, text, Toast.LENGTH_LONG).show();
		}
		
	};
	
	private String postAData(){
		StringBuilder sb = new StringBuilder();
		sb.append("a=add_addr&u=");
		sb.append(new Tools().getUserName(getApplicationContext()));
		sb.append("&Provinces=&City=&County=&DetailsAddress=");
		sb.append(address.getText().toString());
		sb.append("&ZipCode=");
		sb.append(code.getText().toString());
		
		return sb.toString();
	}
	
	private String postEData(){
		StringBuilder sb = new StringBuilder();
		sb.append("a=edit_addr_id&u=");
		sb.append(new Tools().getUserName(getApplicationContext()));
		sb.append("&id=");
		sb.append(position);
		sb.append("&Provinces=&City=&County=&DetailsAddress=");
		sb.append(address.getText().toString());
		sb.append("&ZipCode=");
		sb.append(code.getText().toString());
		
		return sb.toString();
	}
	
}
