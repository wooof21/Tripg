package cn.vip.main;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.internet.Exit;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.tripg.R;
import cn.tripg.activity.flight.UpdateManager;
import cn.tripg.widgit.ProgressDialogTripg;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class AboutUs extends Activity implements OnClickListener {

	private ImageView back;
	private LinearLayout newLL;
	private LinearLayout update;
	private LinearLayout feedback;
	private LinearLayout webSite;
	private LinearLayout share;

	private String url = "http://mapi.tripglobal.cn/CarModelImages/8e8c89bf-5d2a-45ca-973f-2379451d650a.png";
	private void prepareView() {
		back = (ImageView) findViewById(R.id.about_back);
		newLL = (LinearLayout) findViewById(R.id.about_new_feature);
		update = (LinearLayout) findViewById(R.id.about_update);
		feedback = (LinearLayout) findViewById(R.id.about_feedback);
		webSite = (LinearLayout) findViewById(R.id.about_website);
		share = (LinearLayout) findViewById(R.id.about_share);

		back.setOnClickListener(this);
		newLL.setOnClickListener(this);
		update.setOnClickListener(this);
		feedback.setOnClickListener(this);
		webSite.setOnClickListener(this);
		share.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		Exit.getInstance().addActivity(this);

		prepareView();
		
		getImageFromAssetsFile(AboutUs.this, "share.png");

		
	}

	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		oks.disableSSOWhenAuthorize();

		// 閸掑棔闊╅弮绂tification閻ㄥ嫬娴橀弽鍥ф嫲閺傚洤鐡�
		oks.setNotification(R.drawable.ic_launcher,
				getString(R.string.app_name));
		// title閺嶅洭顣介敍灞藉祪鐠烇紕鐟拋鑸拷渚�鍋栫粻渚匡拷浣蜂繆閹垬锟戒礁浜曟穱掳锟戒椒姹夋禍铏圭秹閸滃Q缁屾椽妫挎担璺ㄦ暏
		//oks.setTitle(getString(R.string.share));
		oks.setTitle("差旅天下机票,酒店,租车,签证...");
		// titleUrl閺勵垱鐖ｆ０妯兼畱缂冩垹绮堕柧鐐复閿涘奔绮庨崷銊ゆ眽娴滆櫣缍夐崪瀛甉缁屾椽妫挎担璺ㄦ暏
		oks.setTitleUrl("http://www.tripg.cn");
		// text閺勵垰鍨庢禍顐ｆ瀮閺堫剨绱濋幍锟介張澶婇挬閸欎即鍏橀棁锟界憰浣界箹娑擃亜鐡у▓锟�
		oks.setText("我下载了差旅天下（企业/公众版），它能够方便预订机票、酒店、火车票、游轮、签证等，快来试试吧，请到http://www.tripg.cn 下载.");
		// imagePath閺勵垰娴橀悧鍥╂畱閺堫剙婀寸捄顖氱窞閿涘inked-In娴犮儱顦婚惃鍕挬閸欎即鍏橀弨顖涘瘮濮濄倕寮弫锟�
		//oks.setImagePath("/sdcard/sharepic.jpg");// 绾喕绻歋Dcard娑撳娼扮�涙ê婀銈呯炊閸ュ墽澧�//
		oks.setImagePath(AboutUs.this.getFilesDir().getPath()+"/data/share.png");
		// url娴犲懎婀顔讳繆閿涘牆瀵橀幏顒�銈介崣瀣嫲閺堝寮搁崷鍫礆娑擃厺濞囬悽锟�
		oks.setUrl("http://www.tripg.cn/shouji_apk/tripg.apk");
		// comment閺勵垱鍨滅�电绻栭弶鈥冲瀻娴滎偆娈戠拠鍕啈閿涘奔绮庨崷銊ゆ眽娴滆櫣缍夐崪瀛甉缁屾椽妫挎担璺ㄦ暏
		oks.setComment("");
		// site閺勵垰鍨庢禍顐ｎ劃閸愬懎顔愰惃鍕秹缁旀瑥鎮曠粔甯礉娴犲懎婀猀Q缁屾椽妫挎担璺ㄦ暏
		oks.setSite(getString(R.string.app_name));
		// siteUrl閺勵垰鍨庢禍顐ｎ劃閸愬懎顔愰惃鍕秹缁旀瑥婀撮崸锟介敍灞肩矌閸︹墻Q缁屾椽妫挎担璺ㄦ暏
		oks.setSiteUrl("http://www.tripg.cn");
		//oks.setImageUrl("http://mapi.tripglobal.cn/CarModelImages/8e8c89bf-5d2a-45ca-973f-2379451d650a.png");
		// 閸氼垰濮╅崚鍡曢煩GUI
		oks.show(this);
	}

	private Bitmap getImageFromAssetsFile(Context context, String fileName) {
        //获取应用的包名
        String packageName = context.getPackageName();
        //定义存放这些图片的内存路径
        String path= AboutUs.this.getFilesDir().getPath()+"/data/";
        //如果这个路径不存在则新建
        File file = new File(path);
        Bitmap image = null;
        boolean isExist = file.exists();
        if(!isExist){
            file.mkdirs();
        }
        //获取assets下的资源
        AssetManager am = context.getAssets();
        try {
            //图片放在img文件夹下
            InputStream is = am.open("img/"+fileName);
            image = BitmapFactory.decodeStream(is);
            FileOutputStream out = new FileOutputStream(path+fileName);
            //这个方法非常赞
            image.compress(Bitmap.CompressFormat.PNG,100,out);
            out.flush();
            out.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.about_back:
			finish();
			break;
		case R.id.about_new_feature:
			Intent intent2 = new Intent(AboutUs.this, NewFeature.class);
			startActivity(intent2);
			break;
		case R.id.about_update:
			final ProgressDialog pd = ProgressDialogTripg.show(AboutUs.this,
					null, null);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					pd.dismiss();
					UpdateManager mUpdateManager = new UpdateManager(
							AboutUs.this);
					mUpdateManager.checkUpdateInfo();
				}
			}, 3000);

			break;
		case R.id.about_feedback:
			Intent intent = new Intent(AboutUs.this, FeedBackActivity.class);
			startActivity(intent);
			break;
		case R.id.about_website:
			Intent intent1 = new Intent(AboutUs.this, OpenWeb.class);
			startActivity(intent1);
			break;
		case R.id.about_share:
			showShare();
			break;

		default:
			break;
		}

	}

}
