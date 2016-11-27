package cn.vip.main;

import org.json.JSONException;
import org.json.JSONObject;

import cn.internet.Exit;
import cn.internet.Tools;
import cn.tripg.R;
import cn.tripg.widgit.ProgressDialogTripg;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class VipMyCredit extends Activity{

	private ImageView back;
	private TextView totalCredit;
	private TextView available;
	
	private Tools tools;
	private String cUrl = "http://www.tripg.cn/phone_api/shop_integral.php?username=";
	private ProgressDialog progressDialog;
	
	private void prepareView(){
		back = (ImageView) findViewById(R.id.vc_back);
		totalCredit = (TextView) findViewById(R.id.vc_totalcredit);
		available = (TextView) findViewById(R.id.vc_avaliable);
		
		tools = new Tools();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vipcredit);
		Exit.getInstance().addActivity(this);
		prepareView();
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		new Handler().post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				progressDialog = ProgressDialogTripg.show(VipMyCredit.this, null, null);
				String url = cUrl + tools.getUserName(getApplicationContext());
				Log.e("url", url);
				String data = tools.getURL(url);
				System.out.println("data ---> " + data);
				
				try {
					JSONObject job = new JSONObject(data);
					String code = job.getString("Code");
					if(code.equals("1")){
						progressDialog.dismiss();
						totalCredit.setText(job.getString("Result"));
						available.setText(job.getString("Result"));
					}else{
						progressDialog.dismiss();
						totalCredit.setText("0");
						available.setText(job.getString("0"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	
}
