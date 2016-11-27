package com.data.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import cn.internet.Exit;
import cn.tripg.R;
import cn.tripg.activity.flight.ResultCode;

public class Fapiaoview extends Activity implements OnClickListener {

	public ImageView imageView;
	public EditText ttouEditText;
	public EditText addEditText;
	public EditText youBianText;
	public String youString;
	public String fapString;
	public String addString;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			fapString = ttouEditText.getText().toString();
			addString = addEditText.getText().toString();
			youString = youBianText.getText().toString();
			if (!fapString.equals("") && !addString.equals("")
					&& !youString.equals("")) {
				Intent intent = getIntent();
				intent.putExtra("fapiao", fapString);
				intent.putExtra("address", addString);
				intent.putExtra("youbian", youString);
				setResult(ResultCode.SUCCESS, intent);
				finish();
			} else {
				Intent intent = getIntent();
				intent.putExtra("fapiao", "");
				intent.putExtra("address", "");
				intent.putExtra("youbian", "");
				setResult(ResultCode.FAILURE, intent);
				finish();
			}
		}
		return true;
		// return super.onKeyDown(keyCode, event);

	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fapiao_main_view);
		Exit.getInstance().addActivity(this);
		ttouEditText = (EditText) findViewById(R.id.editTextf1);
		ttouEditText.setOnClickListener(this);
		addEditText = (EditText) findViewById(R.id.editTextf2);
		addEditText.setOnClickListener(this);
		youBianText = (EditText) findViewById(R.id.editTextf33);
		youBianText.setOnClickListener(this);
		imageView = (ImageView) findViewById(R.id.imageViewfpview);
		imageView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageViewfpview:
			// System.out.println("order ÃßÕ∑ "+ttouEditText.getText().toString());
			// System.out.println("order µÿ÷∑ "+addEditText.getText().toString());
			// System.out.println("order ” ±‡ "+youBianText.getText().toString());
			fapString = ttouEditText.getText().toString();
			addString = addEditText.getText().toString();
			youString = youBianText.getText().toString();
			if (!fapString.equals("") && !addString.equals("")
					&& !youString.equals("")) {
				Intent intent = getIntent();
				intent.putExtra("fapiao", fapString);
				intent.putExtra("address", addString);
				intent.putExtra("youbian", youString);
				setResult(ResultCode.SUCCESS, intent);
				finish();
			} else {
				Intent intent = getIntent();
				intent.putExtra("fapiao", "");
				intent.putExtra("address", "");
				intent.putExtra("youbian", "");
				setResult(ResultCode.FAILURE, intent);
				finish();
			}

			break;

		default:
			break;
		}
	}

}
