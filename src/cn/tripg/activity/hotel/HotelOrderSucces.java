package cn.tripg.activity.hotel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.internet.Exit;
import cn.tripg.R;
import cn.tripg.activity.flight.MainActivity;
import cn.tripg.activity.flight.ResultCode;

public class HotelOrderSucces extends Activity{
	
	
	public String orderString;
	public TextView textViewNum;
	public ImageView backImageView;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hotelordersuccee);
		Exit.getInstance().addActivity(this);
		
		Intent intent = getIntent();
		orderString = (String)intent.getExtras().getString("Result");
		
		
		//·µ»Ø°´Å¥
		ImageView backImageView = (ImageView)findViewById(R.id.title_back);
				  backImageView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Intent intent = getIntent();
						setResult(ResultCode.FAILURE, intent);
						finish();
						
					}
				});
		
		textViewNum = (TextView)findViewById(R.id.textViewsucces2);
		textViewNum.setText(orderString);
		
		backImageView = (ImageView)findViewById(R.id.imageViewsucces2);
		backImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(HotelOrderSucces.this,MainActivity.class);
				startActivity(intent);
				
			}
		});
	
	
	}
	

}
