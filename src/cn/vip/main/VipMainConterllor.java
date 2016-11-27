package cn.vip.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.internet.Exit;
import cn.tripg.R;
import cn.tripg.activity.flight.MainActivity;
import cn.tripg.activity.flight.ResultCode;
import cn.tripg.activity.login.ChangePassWordActivity;
import cn.tripg.activity.login.LoginActivity;
import cn.vip.next.main.VipNextChoujmain;
import cn.vip.next.main.VipNextGerenmain;
import cn.vip.next.main.VipNextJiFenmain;
import cn.vip.next.main.VipNextJiYimain;
import cn.vip.next.main.VipNextOrdermain;

public class VipMainConterllor extends Activity implements OnClickListener{///

	public TextView textViewname;
	public TextView textViewnum;
	public ImageView orderImageView;
	public ImageView jifenImageView;
	public ImageView jiyiImageView;
	public ImageView gerenImageView;
	public ImageView about;
	public ImageView bkiImageView;
	private ImageView traveler;
	private ImageView contacter;
	private ImageView address;
	private TextView version;//
	private ImageView change;
	public String username;
	private String memberId;
	
	private ImageView main;
	private ImageView order;
	private ImageView phone;
	private ImageView mine;
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {///
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent(VipMainConterllor.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
		
		return true;
	}
	
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
		setContentView(R.layout.vip_main_con);
		Exit.getInstance().addActivity(this);
		isUserLogin();
		textViewname = (TextView)findViewById(R.id.textViewname);
		textViewname.setText(username);
		version = (TextView)findViewById(R.id.vip_version_no);
		/***************************************************************/
		/*
		 * 获取版本号， VersionCode：对消费者不可见，仅用于应用市场、程序内部识别版本，判断新旧等用途。
		 * VersionName：展示给消费者，消费者会通过它认知自己安装的版本。一般我们说的版本号就是这个。
		 */
		PackageManager pm = getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
			String name = pi.versionName;
			int code = pi.versionCode;
			version.setText("版本："+name);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//getPackageName()是你当前类的包名，0代表是获取版本信息
		
		/***************************************************************/
		
		ImageView backImageView = (ImageView)findViewById(R.id.title_back);
		backImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = getIntent();
				setResult(ResultCode.SUCCESS, intent);
				finish();
			}
		});
		bkiImageView = (ImageView)findViewById(R.id.imageViewvipback);
		bkiImageView.setOnClickListener(this);
		orderImageView = (ImageView)findViewById(R.id.imageViewviporder);
		orderImageView.setOnClickListener(this);
		jifenImageView = (ImageView)findViewById(R.id.imageViewvipjifen);
		jifenImageView.setOnClickListener(this);
		jiyiImageView = (ImageView)findViewById(R.id.imageViewvipjiyi);
		jiyiImageView.setOnClickListener(this);
		gerenImageView = (ImageView)findViewById(R.id.imageViewvipgeren);
		gerenImageView.setOnClickListener(this);
		about = (ImageView)findViewById(R.id.about);
		about.setOnClickListener(this);
		change = (ImageView)findViewById(R.id.vip_maincon_change);
		change.setOnClickListener(this);
		
		traveler = (ImageView)findViewById(R.id.traveler);
		contacter = (ImageView)findViewById(R.id.contacter);
		address = (ImageView)findViewById(R.id.address);
		traveler.setOnClickListener(this);
		contacter.setOnClickListener(this);
		address.setOnClickListener(this);
		
		main = (ImageView)findViewById(R.id.vmc_main);
		main.setOnClickListener(this);
		order = (ImageView)findViewById(R.id.vmc_my_order);
		order.setOnClickListener(this);
		phone = (ImageView)findViewById(R.id.vmc_phone);
		phone.setOnClickListener(this);
		mine = (ImageView)findViewById(R.id.vmc_mine);
		mine.setOnClickListener(this);
		
	}
	
	private boolean isUserLogin(){
        SharedPreferences sharedPre = VipMainConterllor.this.getSharedPreferences(
        		"config", Context.MODE_PRIVATE);
        //清除数据
        //sharedPre.edit().clear();
        //sharedPre.edit().commit();
        //存储数据
       // sharedPre.edit().putString("", "");
       // sharedPre.edit().commit();
        username = sharedPre.getString("username", "");
        memberId = sharedPre.getString("Result", "");
        String password = sharedPre.getString("password", "");
        Log.e("username", "A" + username);
        Log.e("password", "B" + password);
        if("".equals(username) || "".equals(password)){
        	return false;
        }else{
        	return true;
        }
    }

	@Override
	public void onClick(View arg0) {
		
		switch (arg0.getId()) {
		case R.id.imageViewvipback:
			Log.e("imageViewvipback", "imageViewvipback");
			
			AlertDialog.Builder builder =  new AlertDialog.Builder(VipMainConterllor.this);
	
            builder.setMessage("是否确定注销账号");
            builder.setPositiveButton("确定",    
                    new DialogInterface.OnClickListener() {    
                        public void onClick(DialogInterface dialog, int whichButton) {   

                			SharedPreferences sharedPre = VipMainConterllor.this.getSharedPreferences(
                	        		"config", Context.MODE_PRIVATE);
                			SharedPreferences.Editor editor = sharedPre.edit();

                			editor.remove("username");
                			editor.remove("password");
                			editor.clear();
                	        editor.commit();
                	        
//                	        Intent intent = getIntent();
//                	        setResult(ResultCode.SUCCESS, intent);
                	        Intent intent = new Intent(VipMainConterllor.this, LoginActivity.class);
                	        startActivity(intent);
                			finish();
                        }    
                    });    
            builder.setNegativeButton("取消",    
                    new DialogInterface.OnClickListener() {    
                        public void onClick(DialogInterface dialog, int whichButton) {   
                        	finish();
//                        	Intent intent4 = new Intent(OrderPnrMain.this, MainActivity.class);
//                        	startActivity(intent4);
                        }    
                    });    
            builder.create();  
            builder.show();
			
			
	        
			break;
		case R.id.about:
			Log.e("AboutUs", "AboutUs");
			if(getInternet() == true){
				Intent intent2 = new Intent(VipMainConterllor.this,AboutUs.class);
				startActivity(intent2);
			}else{
				Toast.makeText(VipMainConterllor.this, "网络链接已断开", Toast.LENGTH_LONG).show();
			}
			
			break;
		case R.id.imageViewvipgeren:
			Log.e("imageViewvipgeren", "imageViewvipgeren");
			if(getInternet() == true){
				Intent intent3 = new Intent(VipMainConterllor.this,VipGeRenmain.class);
				startActivity(intent3);
			}else{
				Toast.makeText(VipMainConterllor.this, "网络链接已断开", Toast.LENGTH_LONG).show();
			}
			
			break;
		case R.id.imageViewvipjifen:
			Log.e("imageViewvipjifen", "imageViewvipjifen");
			if(getInternet() == true){
				Intent intent4 = new Intent(VipMainConterllor.this,VipMyCredit.class);
				startActivity(intent4);
			}else{
				Toast.makeText(VipMainConterllor.this, "网络链接已断开", Toast.LENGTH_LONG).show();
			}
			
			break;
		case R.id.imageViewvipjiyi:
			Log.e("imageViewvipjiyi", "imageViewvipjiyi");
			if(getInternet() == true){
				Intent intent5 = new Intent(VipMainConterllor.this,VipJiYimain.class);
				startActivity(intent5);
			}else{
				Toast.makeText(VipMainConterllor.this, "网络链接已断开", Toast.LENGTH_LONG).show();
			}
			
			break;
		case R.id.imageViewviporder:
			Log.e("imageViewviporder", "imageViewviporder");
			if(getInternet() == true){
				Intent intent6 = new Intent(VipMainConterllor.this,VipOrdermain.class);
				startActivity(intent6);
			}else{
				Toast.makeText(VipMainConterllor.this, "网络链接已断开", Toast.LENGTH_LONG).show();
			}

			break;
		case R.id.vip_maincon_change:
			Log.e("change password clicked", "");
			
			Intent intent7 = new Intent(VipMainConterllor.this, ChangePassWordActivity.class);
			intent7.putExtra("memberId", memberId);
			intent7.putExtra("username", username);
			startActivity(intent7);//
			
			break;
			
		case R.id.traveler:
			Intent intent8 = new Intent(VipMainConterllor.this, TCAMainActivity.class);
			intent8.putExtra("type", "t");
			startActivity(intent8);
			break;
		case R.id.contacter:
			Intent intent9 = new Intent(VipMainConterllor.this, TCAMainActivity.class);
			intent9.putExtra("type", "c");
			startActivity(intent9);
			break;
		case R.id.address:
			Intent intent10 = new Intent(VipMainConterllor.this, TCAMainActivity.class);
			intent10.putExtra("type", "a");
			startActivity(intent10);
			break;
		case R.id.vmc_main:
			if(getInternet() == true){
				Intent intent6 = new Intent(VipMainConterllor.this, MainActivity.class);
				startActivity(intent6);
				finish();
			}else{
				Toast.makeText(VipMainConterllor.this, "网络链接已断开", Toast.LENGTH_LONG).show();
			}
			
			break;
		case R.id.vmc_my_order:
			if(getInternet() == true){
				Intent intent11 = new Intent(VipMainConterllor.this, VipOrdermain.class);
				startActivity(intent11);
				finish();
			}else{
				Toast.makeText(VipMainConterllor.this, "网络链接已断开", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.vmc_phone:
			Intent intent12 = new Intent();
			intent12.setAction("android.intent.action.DIAL");
			intent12.setData(Uri.parse("tel:400 656 8777"));
			startActivity(intent12);
			break;
		case R.id.vmc_mine:

			break;
		default:
			break;
		}
		
		
		
	}
	
}
