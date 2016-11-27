package cn.qianzheng.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.internet.Exit;
import cn.tripg.R;

public class QianZhViewReq extends Activity{

	private String naString;
	private String picString;
	private String neiString;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qianviewreq);//
		Exit.getInstance().addActivity(this);
		ImageView imageViewback = (ImageView)findViewById(R.id.title_qianzheng_back);
		imageViewback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				finish();
			}
		});
		
		naString = getIntent().getStringExtra("n");
		picString = getIntent().getStringExtra("p");
		neiString = getIntent().getStringExtra("r");
		
		TextView nameTextView = (TextView)findViewById(R.id.textView2);
		nameTextView.setText(naString);
		TextView pictTextView = (TextView)findViewById(R.id.textView4);
		pictTextView.setText("гд"+picString);
		TextView neiTextView = (TextView)findViewById(R.id.textView6);
		neiTextView.setText(neiString);
		
	
	}
	
	
	
}
