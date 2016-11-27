package cn.vip.main;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import cn.internet.Exit;
import cn.tripg.R;
import cn.tripg.activity.flight.MainActivity;
import cn.tripg.activity.flight.ResultCode;
import cn.vip.next.main.VipNextOrdermain;
import cn.vip.next.main.VipTrainOrderMain;
import cn.vip.order.VipJDOrdermian;
import cn.vip.order.VipYcarOrdermain;
import cn.vip.tsv.ShipOrderMain;
import cn.vip.tsv.TravelOrderMain;
import cn.vip.tsv.VisaOrderMain;

public class VipOrdermain extends Activity implements OnClickListener{

	public ImageView jpImageView;
	public ImageView jdImageView;
	public ImageView ycImageView;
	public ImageView hcImageView;
	public ImageView sjImageView;
	
	private ImageView travel;
	private ImageView ship;
	private ImageView visa;
	
	private ImageView main;
	private ImageView order;
	private ImageView phone;
	private ImageView mine;
	
	
	
	/****************************************************************************/
	public boolean getInternet() {
		ConnectivityManager con = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.isConnectedOrConnecting();
		boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.isConnectedOrConnecting();
		if (wifi || internet) {
			return true;
		} else {
			return false;
		}
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viporder_main);
		Exit.getInstance().addActivity(this);
		ImageView backImageView = (ImageView)findViewById(R.id.title_back);
		backImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = getIntent();
				setResult(ResultCode.SUCCESS, intent);
				finish();
			}
		});
		
		jpImageView = (ImageView)findViewById(R.id.imageViewvipordera1);
		jpImageView.setOnClickListener(this);
		jdImageView = (ImageView)findViewById(R.id.imageViewvipordera2);
		jdImageView.setOnClickListener(this);
		ycImageView = (ImageView)findViewById(R.id.imageViewvipordera3);
		ycImageView.setOnClickListener(this);
		hcImageView = (ImageView)findViewById(R.id.imageViewvipordera4222);
		hcImageView.setOnClickListener(this);
		
		travel = (ImageView)findViewById(R.id.order_travel);
		travel.setOnClickListener(this);
		ship = (ImageView)findViewById(R.id.order_ship);
		ship.setOnClickListener(this);
		visa = (ImageView)findViewById(R.id.order_visa);
		visa.setOnClickListener(this);
		
//		sjImageView = (ImageView)findViewById(R.id.imageViewvipordera5);
//		sjImageView.setOnClickListener(this);
		
		main = (ImageView)findViewById(R.id.vom_main);
		main.setOnClickListener(this);
		order = (ImageView)findViewById(R.id.vom_my_order);
		order.setOnClickListener(this);
		phone = (ImageView)findViewById(R.id.vom_phone);
		phone.setOnClickListener(this);
		mine = (ImageView)findViewById(R.id.vom_mine);
		mine.setOnClickListener(this);
		
	}



	@Override
	public void onClick(View arg0) {
		
		switch (arg0.getId()) {
			case R.id.imageViewvipordera1:
				Log.e("imageViewvipordera1", "imageViewvipordera1");
			
				if(getInternet() == true){
					Intent intent =  new Intent(VipOrdermain.this,VipNextOrdermain.class);
					startActivity(intent);
				}else{
					Toast.makeText(VipOrdermain.this, "网络链接已断开", Toast.LENGTH_LONG).show();
				}
				
				break;
			case R.id.imageViewvipordera2:
				Log.e("imageViewvipordera2", "imageViewvipordera2");
				if(getInternet() == true){
					Intent intent2 = new Intent(VipOrdermain.this,VipJDOrdermian.class);
					startActivity(intent2);
				}else{
					Toast.makeText(VipOrdermain.this, "网络链接已断开", Toast.LENGTH_LONG).show();
				}

				break;
			case R.id.imageViewvipordera3:
				Log.e("imageViewvipordera3", "imageViewvipordera3");
				if(getInternet() == true){
					Intent intent3 = new Intent(VipOrdermain.this,VipYcarOrdermain.class);
					startActivity(intent3);
				}else{
					Toast.makeText(VipOrdermain.this, "网络链接已断开", Toast.LENGTH_LONG).show();
				}
				
				break;
			case R.id.imageViewvipordera4222:
				Log.e("imageViewvipordera4", "imageViewvipordera4");
				Intent intent4 = new Intent(VipOrdermain.this, VipTrainOrderMain.class);
				startActivity(intent4);
				break;
//			case R.id.imageViewvipordera5:
//				Log.e("imageViewvipordera5", "imageViewvipordera5");
//				Intent intent5 = new Intent(VipOrdermain.this, VipTrainOrderMain.class);
//				startActivity(intent5);
//				break;
			case R.id.order_travel:
				Intent intent = new Intent(VipOrdermain.this, TravelOrderMain.class);
				startActivity(intent);
				break;
				
			case R.id.order_ship:
				Intent intent1 = new Intent(VipOrdermain.this, ShipOrderMain.class);
				startActivity(intent1);
				break;
			case R.id.order_visa:
				Intent intent5 = new Intent(VipOrdermain.this, VisaOrderMain.class);
				startActivity(intent5);
				break;
			case R.id.vom_main:
				if(getInternet() == true){
					Intent intent6 = new Intent(VipOrdermain.this, MainActivity.class);
					startActivity(intent6);
					finish();
				}else{
					Toast.makeText(VipOrdermain.this, "网络链接已断开", Toast.LENGTH_LONG).show();
				}
				
				break;
			case R.id.vom_my_order:
				
				break;
			case R.id.vom_phone:
				Intent intent7 = new Intent();
				intent7.setAction("android.intent.action.DIAL");
				intent7.setData(Uri.parse("tel:400 656 8777"));
				startActivity(intent7);
				break;
			case R.id.vom_mine:
				if(getInternet() == true){
					Intent intent8 = new Intent(VipOrdermain.this, VipMainConterllor.class);
					startActivity(intent8);
					finish();
				}else{
					Toast.makeText(VipOrdermain.this, "网络链接已断开", Toast.LENGTH_LONG).show();
				}
				break;
		default:
			break;
		}
		
	}
	
	
	
	
}
