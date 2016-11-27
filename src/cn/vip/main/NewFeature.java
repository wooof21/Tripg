package cn.vip.main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import cn.internet.Exit;
import cn.tripg.R;
import cn.tripg.widgit.ProgressDialogTripg;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class NewFeature extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_feature);
		Exit.getInstance().addActivity(this);
		findViewById(R.id.nf_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		ViewPager vp = (ViewPager) findViewById(R.id.nf_viewpager);
		vp.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		LayoutInflater mLi = LayoutInflater.from(NewFeature.this);
		View view1 = mLi.inflate(R.layout.view1, null);
		View view2 = mLi.inflate(R.layout.view2, null);
		View view3 = mLi.inflate(R.layout.view3, null);
		View view4 = mLi.inflate(R.layout.view4, null);
		View view5 = mLi.inflate(R.layout.view5, null);
		Button button = (Button)view5.findViewById(R.id.button1);
		button.setVisibility(View.GONE);
		
		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);
		views.add(view5);
		
		PagerAdapter pagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position,
					Object object) {
				((ViewPager) container).removeView(views.get(position));
				
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}

		};
		vp.setAdapter(pagerAdapter);
		
		
		
	}




	
}
