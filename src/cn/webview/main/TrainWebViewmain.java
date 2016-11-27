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
		//��
		//http://m.tieyou.com/?utm_source=bjcltx&ad=close&top=close&buy=close&free=close
		//����
		//http://jlsydgjlxs.m.tmall.com/shop/shop_auction_search.htm?suid=1647038696&sort=default&scid=874930177&sid=e1f295222852620c
		//����
		//http://jlsydgjlxs.m.tmall.com/shop/shop_auction_search.htm?suid=1647038696&sort=default&scid=875012869&sid=e1f295222852620c
		//ǩ֤
		//http://jlsydgjlxs.m.tmall.com/shop/shop_auction_search.htm?suid=1647038696&sort=default&scid=874930175&sid=e1f295222852620c
		
		
		webView = (WebView) findViewById(R.id.webView1);
		WebSettings settings = webView.getSettings();
		 // �õ�WebSettings��������֧��JavaScript����
		  // ������ʵ�ҳ������JavaScript����WebView��������֧��JavaScript ��������ʾ�հ�ҳ��
		settings.setJavaScriptEnabled(true);
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		// ����URL
		webView.loadUrl("http://m.tieyou.com/?utm_source=bjcltx&ad=close&top=close&buy=close&free=close");	
		
	}
	
}
