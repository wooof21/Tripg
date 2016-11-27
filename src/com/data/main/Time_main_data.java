package com.data.main;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import cn.internet.Exit;
import cn.tripg.R;
import cn.tripg.activity.flight.ResultCode;


public class Time_main_data extends Activity{

	

	public TimePicker timePicker;
	public ImageView imageView;
	public String timeString;
	
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_viewxml);
		Exit.getInstance().addActivity(this);
		timeString = null;
		imageView = (ImageView)findViewById(R.id.imageView1);
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (timeString != null) {
					Intent intent = getIntent();
					intent.putExtra("time",timeString);
					setResult(ResultCode.SUCCESS, intent);
					finish();
				}else {
					Calendar c = Calendar.getInstance();
					int hour = c.get(Calendar.HOUR_OF_DAY);
					int min = c.get(Calendar.MINUTE) + 1;
					String	datatiemString = hour + ":" + min;
					Intent intent = getIntent();
					intent.putExtra("time",datatiemString);
					setResult(ResultCode.SUCCESS, intent);
					finish();
				}
				
				
			}
		});
		timePicker = (TimePicker)findViewById(R.id.timePicker1);
		timePicker.setIs24HourView(true);
		timePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
			
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				//timePicker.clearFocus();
				timeString = hourOfDay+":"+minute;
				timeString = pad(hourOfDay)+":"+pad(minute);
				Log.e("1.delegate*******", timeString);
				
				
			}
		});
	

		

		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if (timeString != null) {
				Intent intent = getIntent();
				intent.putExtra("time",timeString);
				setResult(ResultCode.SUCCESS, intent);
				finish();
			}else {
				Calendar c = Calendar.getInstance();
				int hour = c.get(Calendar.HOUR_OF_DAY);
				int min = c.get(Calendar.MINUTE) + 1;
				String	datatiemString = hour + ":" + min;
				Intent intent = getIntent();
				intent.putExtra("time",datatiemString);
				setResult(ResultCode.SUCCESS, intent);
				finish();
			}
		}
		
		
		return super.onKeyDown(keyCode, event);
	}
	private String  pad(int c)
    {
        if(c>=10)
        {
            return String.valueOf(c);
        }else
        {
            return "0"+String.valueOf(c);
        }

    }
	
}
