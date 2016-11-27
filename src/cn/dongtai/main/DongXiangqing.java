package cn.dongtai.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.internet.Exit;
import cn.tripg.R;
import cn.tripg.activity.flight.ResultCode;



public class DongXiangqing extends Activity{

	
	public String fNoString;
	public String fCompanyString;
	public String fDepString;
	public String fArrString;
	public String fDepAirportString;
	public String fArrAirportString;
	public String fDeptimePlanString;
	public String fArrtimePlanString;
	public String fDeptimeString;
	public String fArrtimeString;
	public String fStateString;
	public String ftermianlString;
	
	public TextView textView1;
	public TextView textView2;
	public TextView textView3;
	public TextView textView4;
	public TextView textView5;
	public TextView textView6;
	public TextView textView7;
	public TextView textView8;
	public TextView textView9;
	public ImageView imageView;
	public ImageView imageViewtitle;
	public DongXiangqing dongXiangqing;
	
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dong_xiang_view);
		Exit.getInstance().addActivity(this);
		dongXiangqing = this;
		Intent intent = getIntent();
		fNoString = intent.getExtras().getString("FilghtNo");
		fCompanyString = intent.getExtras().getString("FlightCompany");
		fArrString = intent.getExtras().getString("FlightArr");
		fDepString = intent.getExtras().getString("FlightDep");
		fDepAirportString = intent.getExtras().getString("FlightDepAirport");
		fArrAirportString = intent.getExtras().getString("FlightArrAirport");
		fDeptimePlanString = intent.getExtras().getString("FlightDeptimePlan");
		fArrtimePlanString = intent.getExtras().getString("FlightArrtimePlan");
		fDeptimeString = intent.getExtras().getString("FlightDeptime");
		fArrtimeString = intent.getExtras().getString("FlightArrtime");
		fStateString = intent.getExtras().getString("FlightState");
		ftermianlString = intent.getExtras().getString("FlightTerminal");
		
		imageViewtitle = (ImageView)findViewById(R.id.title_back);
		imageViewtitle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				finish();
			}
		});
	
		textView1 = (TextView)findViewById(R.id.textViewdx1);
		textView1.setText(fCompanyString+fNoString);
		textView2 = (TextView)findViewById(R.id.textViewdx2);
		textView2.setText("提示:航班动态为免费服务。我们尽可能保证信息的准确，由于航空公司会随时调整，详细信息以机场告知为准。");
		textView3 = (TextView)findViewById(R.id.textViewdx3);
		textView3.setText(fDepString);
		textView4 = (TextView)findViewById(R.id.textViewdx4);
		textView4.setText(fDeptimePlanString);
		textView5 = (TextView)findViewById(R.id.textViewdx5);
		textView5.setText("---");
		textView6 = (TextView)findViewById(R.id.textViewdx6);
		textView6.setText(fArrString);
		textView7 = (TextView)findViewById(R.id.textViewdx7);
		textView7.setText(fArrtimePlanString);
		textView8 = (TextView)findViewById(R.id.textViewdx8);
		textView8.setText("---");
		textView9 = (TextView)findViewById(R.id.textViewdx9);
		textView9.setText(ftermianlString);
	
		imageView = (ImageView)findViewById(R.id.imageViewdx5);
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = getIntent();
				setResult(ResultCode.SUCCESS, intent);
				finish();
			}
		});
	
	}
	
}
