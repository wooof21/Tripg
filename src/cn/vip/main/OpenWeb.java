package cn.vip.main;

import cn.internet.Exit;
import cn.tripg.R;
import cn.tripg.activity.flight.ResultCode;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

public class OpenWeb extends Activity{

	private WebView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trainwebview);
		Exit.getInstance().addActivity(this);
		ImageView backImageView = (ImageView) findViewById(R.id.title_back);
		backImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = getIntent();
				setResult(ResultCode.SUCCESS, intent);
				finish();
			}
		});
		
		webView = (WebView) findViewById(R.id.webView1);
		WebSettings settings = webView.getSettings();
		
		settings.setJavaScriptEnabled(true);
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		// º”‘ÿURL
		webView.loadUrl("http://www.tripg.cn");

	}

	
	
}
