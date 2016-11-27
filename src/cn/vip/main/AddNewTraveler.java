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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class AddNewTraveler extends Activity{

	private ImageView back;
	private TextView name;
	private TextView type;
	private TextView id;
	private EditText idNo;
	private TextView save;
	
	private Tools tools;
	private ProgressDialog progressDialog;
	private String eora;
	private String position;
	
	private void prepareView(){
		back = (ImageView)findViewById(R.id.nat_back);
		name = (TextView)findViewById(R.id.nat_name);
		type = (TextView)findViewById(R.id.nat_type);
		id = (TextView)findViewById(R.id.nat_id);
		idNo = (EditText)findViewById(R.id.nat_id_no);
		save = (TextView)findViewById(R.id.nat_save);
		
		tools = new Tools();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_add_traveler);
		Exit.getInstance().addActivity(this);
		prepareView();
		
		String nameTV = getIntent().getExtras().getString("name");
		String idTypeTV = getIntent().getExtras().getString("idType");
		String idNoTV = getIntent().getExtras().getString("idNo");
		eora = getIntent().getExtras().getString("eora");
		position = getIntent().getExtras().getString("id");
		Log.e("eora + position", eora + " + " + position);
		name.setText(nameTV);
		if(idTypeTV.equals("")){
			id.setText("身份证");
		}else{
			id.setText(idTypeTV);
		}
		idNo.setText(idNoTV);

		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(ResultCode.FAILURE);
				finish();
			}
		});
		
		id.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				createDialog();
			}
		});
		
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(name.getText().length() == 0){
					Toast.makeText(AddNewTraveler.this, "请编辑姓名!", Toast.LENGTH_LONG).show();
				}else if(idNo.getText().length() == 0){
					Toast.makeText(AddNewTraveler.this, "请编辑证件号!", Toast.LENGTH_LONG).show();
				}else{
					new AsyncTask<Void, Void, String>() {

						
						@Override
						protected void onPreExecute() {
							// TODO Auto-generated method stub
							super.onPreExecute();
							progressDialog = ProgressDialogTripg.show(AddNewTraveler.this, null, null);
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
									data = tools.doPostData("http://www.tripg.cn/phone_api/contact_information/api.php", postAData());
									System.out.println(data);
								}else{
									data = tools.doPostData("http://www.tripg.cn/phone_api/contact_information/api.php", postEData());
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
			Toast.makeText(AddNewTraveler.this, text, Toast.LENGTH_LONG).show();
		}
		
	};
	private void createDialog(){
		final String [] list = new String[]{"身份证", "护照", "军官证"};
		new AlertDialog.Builder(AddNewTraveler.this)
			.setTitle("选择证件类型")
			.setSingleChoiceItems(list, 0, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					Log.e("clicked ", "" + which);
					id.setText(list[which]);
				}
			})
			.show();
			
	}
	
	private String postAData(){
		StringBuilder sb = new StringBuilder();
		sb.append("a=add_passenger&u=");
		sb.append(tools.getUserName(getApplicationContext()));
		sb.append("&PassengerName=");
		sb.append(name.getText().toString());
		sb.append("&PassengerType=成人&CertificateType=");
		sb.append(id.getText().toString());
		sb.append("&CertificateNumber=");
		sb.append(idNo.getText().toString());
		
		return sb.toString();
	}
	
	private String postEData(){
		StringBuilder sb = new StringBuilder();
		sb.append("a=edit_passenger_id&u=");
		sb.append(tools.getUserName(getApplicationContext()));
		sb.append("&id=");
		sb.append(position);
		sb.append("&PassengerName=");
		sb.append(name.getText().toString());
		sb.append("&PassengerType=成人&CertificateType=");
		sb.append(id.getText().toString());
		sb.append("&CertificateNumber=");
		sb.append(idNo.getText().toString());
		
		return sb.toString();
	}
	
	
}
