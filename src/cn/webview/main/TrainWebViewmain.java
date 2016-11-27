package cn.webview.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import cn.tripg.R;
import cn.tripg.activity.flight.ResultCode;

public class TrainWebViewmain extends Activity {

	private WebView webView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trainwebview);

		ImageView backImageView = (ImageView) findViewById(R.id.title_back);
		backImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = getIntent();
				setResult(ResultCode.FAILURE, intent);
				finish();
			}
		});
		//火车
		//http://m.tieyou.com/?utm_source=bjcltx&ad=close&top=close&buy=close&free=close
		//旅游
		//http://jlsydgjlxs.m.tmall.com/shop/shop_auction_search.htm?suid=1647038696&sort=default&scid=874930177&sid=e1f295222852620c
		//游轮
		//http://jlsydgjlxs.m.tmall.com/shop/shop_auction_search.htm?suid=1647038696&sort=default&scid=875012869&sid=e1f295222852620c
		//签证
		//http://jlsydgjlxs.m.tmall.com/shop/shop_auction_search.htm?suid=1647038696&sort=default&scid=874930175&sid=e1f295222852620c
		
		
		webView = (WebView) findViewById(R.id.webView1);
		WebSettings settings = webView.getSettings();
		 // 得到WebSettings对象，设置支持JavaScript参数
		  // 如果访问的页面中有JavaScript，则WebView必须设置支持JavaScript ，否则显示空白页面
		settings.setJavaScriptEnabled(true);
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		// 加载URL
		webView.loadUrl("http://m.tieyou.com/?utm_source=bjcltx&ad=close&top=close&buy=close&free=close");	
		
	}
	
}
