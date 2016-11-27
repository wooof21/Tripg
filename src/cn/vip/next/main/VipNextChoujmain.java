package cn.vip.next.main;

import cn.internet.Exit;
import cn.tripg.R;
import cn.tripg.activity.flight.ResultCode;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class VipNextChoujmain extends Activity{

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vipnextchouj_main);
		Exit.getInstance().addActivity(this);
		ImageView backImageView = (ImageView)findViewById(R.id.title_back);
		backImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = getIntent();
				setResult(ResultCode.FAILURE, intent);
				finish();
			}
		});
		
		
		
	}
	
	
}
