package order.pnr.yidao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.internet.Exit;
import cn.tripg.R;
import cn.tripg.activity.flight.MainActivity;

public class OrderSuccse extends Activity{
	
	TextView dingTextView;
	ImageView queImageView;
	public String dingdanhaoString;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yu_order_main);
		Exit.getInstance().addActivity(this);
		
		Intent intent = getIntent();
		dingdanhaoString = intent.getExtras().getString("DingDanNo");
		
		dingTextView = (TextView)findViewById(R.id.textViewy1);
		dingTextView.setText("¶©µ¥ºÅ:"+dingdanhaoString);

		
		ImageView backBtn = (ImageView) findViewById(R.id.car_order_title_back);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);
			}
		});
		
		findViewById(R.id.car_zhibubaobtn).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OrderPnrMain.getInstance().execute();
			}
		});
		
		findViewById(R.id.car_backtomain).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(OrderSuccse.this, MainActivity.class);
				startActivity(intent);
			}
		});
		
	}

}
