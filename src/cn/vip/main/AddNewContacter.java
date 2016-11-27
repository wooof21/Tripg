package cn.vip.main;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import cn.internet.Exit;
import cn.internet.Tools;
import cn.tripg.R;
import cn.tripg.activity.flight.RequestCode;
import cn.tripg.activity.flight.ResultCode;
import cn.tripg.widgit.ProgressDialogTripg;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddNewContacter extends Activity{

	private ImageView back;
	private EditText name;
	private TextView code;
	private EditText phone;
	private TextView save;
	
	private ProgressDialog progressDialog;
	private String eora;
	private String position;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_add_contacter);
		Exit.getInstance().addActivity(this);
		back = (ImageView)findViewById(R.id.nac_back);
		name = (EditText)findViewById(R.id.nac_name);
		code = (TextView)findViewById(R.id.nac_areacode);
		phone = (EditText)findViewById(R.id.nac_phone);
		save = (TextView)findViewById(R.id.nac_save);
		
		String nameT = getIntent().getExtras().getString("name");
		String codeT = getIntent().getExtras().getString("areaCode");
		String phoneT = getIntent().getExtras().getString("phone");
		eora = getIntent().getExtras().getString("eora");
		position = getIntent().getExtras().getString("id");
		
		name.setText(nameT);
		if(codeT.equals("")){
			code.setText("+86");
		}else{
			code.setText(codeT);
		}
		phone.setText(phoneT);
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(ResultCode.FAILURE);
				finish();
			}
		});
		
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(name.getText().length() == 0){
					Toast.makeText(AddNewContacter.this, "Çë±à¼­ÐÕÃû!", Toast.LENGTH_LONG).show();
				}else if(phone.getText().length() == 0){
					Toast.makeText(AddNewContacter.this, "Çë±à¼­µç»°!", Toast.LENGTH_LONG).show();
				}else if(phone.getText().length() != 11){
					Toast.makeText(AddNewContacter.this, "ÇëÌîÐ´11Î»ÊÖ»úºÅ!", Toast.LENGTH_LONG).show();
				}else{
					new AsyncTask<Void, Void, String>() {

						
						@Override
						protected void onPreExecute() {
							// TODO Auto-generated method stub
							super.onPreExecute();
							progressDialog = ProgressDialogTripg.show(AddNewContacter.this, null, null);
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

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			setResult(ResultCode.FAILURE);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}


	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			String text = (String) msg.obj;
			Toast.makeText(AddNewContacter.this, text, Toast.LENGTH_LONG).show();
		}
		
	};
	private String postAData(){
		StringBuilder sb = new StringBuilder();
		sb.append("a=add_contacter&u=");
		sb.append(new Tools().getUserName(getApplicationContext()));
		sb.append("&ContacterName=");
		sb.append(name.getText().toString());
		sb.append("&ContacterArea=");
		sb.append(code.getText().toString());
		sb.append("&ContacterMobile=");
		sb.append(phone.getText().toString());
		
		return sb.toString();
	}
	private String postEData(){
		StringBuilder sb = new StringBuilder();
		sb.append("a=edit_contacter_id&u=");
		sb.append(new Tools().getUserName(getApplicationContext()));
		sb.append("&id=");
		sb.append(position);
		sb.append("&ContacterName=");
		sb.append(name.getText().toString());
		sb.append("&ContacterArea=");
		sb.append(code.getText().toString());
		sb.append("&ContacterMobile=");
		sb.append(phone.getText().toString());
		
		return sb.toString();
	}
	
}
