package cn.tripg.activity.hotel;

import model.hotel.CommercialDistrict;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.internet.Exit;
import cn.tripg.R;
import cn.tripg.activity.flight.MainActivity;
import cn.tripg.activity.flight.ResultCode;

public class SelectCommercialLocationDistrictActivity extends Activity{

	private CommercialDistrict modelCD = null;
	private void setModel(){
		Intent intent = getIntent();
		if(intent != null){
			modelCD = (CommercialDistrict)intent.getSerializableExtra("model");
		}
	}
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        	Intent intent = getIntent();
        	setResult(ResultCode.FAILURE, intent);
        	finish();
            return false;
        } 
        return false;
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hotel_cd_list);
		Exit.getInstance().addActivity(this);
		setModel();
		Log.e("code in = ", modelCD.Code);
		
		ImageView backimageView = (ImageView) findViewById(R.id.title_back);
		backimageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				Intent ticket = new Intent(SelectCommercialLocationDistrictActivity.this,
//						HotelMainActivity.class);
//				startActivity(ticket);
				Intent intent = getIntent();
				setResult(ResultCode.FAILURE, intent);
				finish();

			}
		});
		
		ListView lv = (ListView)findViewById(R.id.cdListView);
		lv.setAdapter(new CdAdapter(SelectCommercialLocationDistrictActivity.this,
				modelCD.Result));
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = getIntent();
				Bundle bundle = new Bundle();
				bundle.putString("locationId", modelCD.Result.get(position).LocationId);
				bundle.putString("locationName", modelCD.Result.get(position).Name);
				intent.putExtras(bundle);
				setResult(ResultCode.SUCCESS, intent);
				finish();
			}
		});
	}
}
