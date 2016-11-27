package cn.dongtai.main;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.internet.Exit;
import cn.model.XmlCityModel;
import cn.tripg.R;
import cn.tripg.activity.flight.CitySelectActivity;
import cn.tripg.activity.flight.MainActivity;
import cn.tripg.activity.flight.RequestCode;
import cn.tripg.activity.flight.ResultCode;
import cn.tripg.interfaces.TripgMessage;
import cn.tripg.interfaces.impl.DongCityInterfaces;
import cn.tripg.interfaces.impl.DongHangInterfaces;

@SuppressLint("HandlerLeak")
public class DongTaiMain extends Activity implements OnClickListener {

	public ImageView qiImageView;
	public ImageView jImageView;
	public ImageView cImageView;
	public ImageView fliiImageView;
	public ImageView depiImageView;
	public ImageView imageViewTitle;
	public FrameLayout frameLayout;
	public EditText hbeditText;
	public boolean hanbool;

	public TextView qTextView;
	public TextView jTextView;
	public TextView rTextView;
	public String jlString;
	public String depCity;
	public String cityname2;
	public String cityname;
	public String currentDate;
	public String hangbanString;
	public List<XmlCityModel> xmlList;

	private DongHangInterfaces hangInterfaces;
	private DongCityInterfaces cityInterfaces;
	public DongTaiMain dongTaiMainthis;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dongtai_main);
		Exit.getInstance().addActivity(this);
		dongTaiMainthis = this;

		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		currentDate = year + "-" + month + "-" + day;

		cityname = "长春";
		depCity = "CGQ";
		cityname2 = "北京";
		jlString = "PEK";
		hangbanString = null;

		frameLayout = (FrameLayout) findViewById(R.id.layoutdong);
		frameLayout.setVisibility(View.INVISIBLE);
		hbeditText = (EditText) findViewById(R.id.editTexthangban);
		imageViewTitle = (ImageView) findViewById(R.id.title_back);
		imageViewTitle.setOnClickListener(this);
		fliiImageView = (ImageView) findViewById(R.id.imageViewd2);
		fliiImageView.setOnClickListener(this);
		depiImageView = (ImageView) findViewById(R.id.imageViewd3);
		depiImageView.setOnClickListener(this);
		qiImageView = (ImageView) findViewById(R.id.imageViewd4);
		qiImageView.setOnClickListener(this);
		jImageView = (ImageView) findViewById(R.id.imageViewd5);
		jImageView.setOnClickListener(this);
		cImageView = (ImageView) findViewById(R.id.imageViewd7);
		cImageView.setOnClickListener(this);
		qTextView = (TextView) findViewById(R.id.textViewd1);
		qTextView.setText(cityname);
		jTextView = (TextView) findViewById(R.id.textViewd2);
		jTextView.setText(cityname2);
		rTextView = (TextView) findViewById(R.id.textViewd3);
		rTextView.setText(currentDate);
	}

	private String getCityCode(String name) {
		Properties proCity = new Properties();
		InputStream isCity = getResources().openRawResource(R.raw.flightcity);
		String codeCity = "";
		try {
			proCity.load(isCity);

			codeCity = (String) proCity.get(name);
		} catch (IOException e) {
			Log.e("zzz", "getCityCode IOException");
		} catch (Exception e) {
			Log.e("zzz", "no such city in dictionary");
		}
		try {
			isCity.close();
		} catch (IOException e) {
			Log.e("zzz", "getCityCode IOException");
		}
		if ("null".equals(codeCity))
			codeCity = "";
		return codeCity;
	}

	// 获取城市内容的回调方法
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case RequestCode.TO_SELECT_ARRIVE_CITY:
			if (resultCode == ResultCode.SUCCESS) {

				Bundle city = data.getExtras();
				cityname = city.getString("cityName");
				qTextView.setText(cityname);
				depCity = getCityCode(cityname);
				Log.e("城市3字码", depCity);
			}

			break;
		case RequestCode.TO_SELECT_LEAVE_CITY:

			if (resultCode == ResultCode.SUCCESS) {
				Bundle city2 = data.getExtras();
				cityname2 = city2.getString("cityName");
				jTextView.setText(cityname2);
				jlString = getCityCode(cityname2);
				Log.e("城市3字码", jlString);
			}
			break;
		case RequestCode.TO_SELECT_DONGXIANG:
			if (resultCode == ResultCode.SUCCESS) {

			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.imageViewd2:

			fliiImageView.setImageResource(R.drawable.qjon);
			depiImageView.setImageResource(R.drawable.hboff);
			frameLayout.setVisibility(View.INVISIBLE);
			hanbool = false;
			break;
		case R.id.imageViewd3:

			hanbool = true;
			fliiImageView.setImageResource(R.drawable.qjoff);
			depiImageView.setImageResource(R.drawable.hbon);
			frameLayout.setVisibility(View.VISIBLE);

			break;
		case R.id.imageViewd4:
			// 查找城市内容
			Intent intent = new Intent(DongTaiMain.this,
					CitySelectActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("cityName", "");
			intent.putExtras(bundle);
			startActivityForResult(intent, RequestCode.TO_SELECT_ARRIVE_CITY);
			break;
		case R.id.imageViewd5:
			// 查找城市内容
			Intent intent2 = new Intent(DongTaiMain.this,
					CitySelectActivity.class);
			Bundle bundle2 = new Bundle();
			bundle2.putString("cityname", "");
			intent2.putExtras(bundle2);
			startActivityForResult(intent2, RequestCode.TO_SELECT_LEAVE_CITY);

			break;
		case R.id.imageViewd7:
			if(getInternet() == true){
				if (hanbool == true) {
					hangbanString = (String) hbeditText.getText().toString();
					if (hangbanString != null) {
						httpHangBang();
					}

				} else {
					httpCityflight();

				}
			}else{
				Toast.makeText(dongTaiMainthis, "网络链接已断开", Toast.LENGTH_LONG).show();
			}
			

			break;
		case R.id.title_back:
//			Intent intentmian = new Intent(this, MainActivity.class);
//			startActivity(intentmian);
			finish();
			break;

		default:
			break;
		}

	}

	// 获取城市查询解析内容 回调方法
	private Handler hanlderCity = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TripgMessage.DEFAULT:
				handMessageDefault(cityInterfaces, DongTaiMain.this, msg);
				break;
			}

		}

		@SuppressWarnings("unchecked")
		private void handMessageDefault(DongCityInterfaces bf,
				DongTaiMain dongTaiMain, Message msg) {
			if (bf == null)
				return;
			if (bf.progressDialog != null)
				bf.progressDialog.dismiss();
			if (msg.obj == null) {
				Toast.makeText(dongTaiMain, "网络链接超时", Toast.LENGTH_SHORT)
						.show();
			} else {
				// TO do
				// model = (AVVo)msg.obj;
				// Log.e("model", model.Code);

				xmlList = (List<XmlCityModel>) msg.obj;
				
				if (xmlList.size() != 0) {
					Log.e("解析内容", xmlList.get(0).fNoString);
					Intent intent = new Intent(dongTaiMainthis,
							DongTaiTableview.class);
					intent.putExtra("xmllist", (Serializable) xmlList);
					startActivityForResult(intent,
							RequestCode.TO_SELECT_DONGXIANG);
				}else {
					Toast.makeText(dongTaiMain, "无航班信息", Toast.LENGTH_SHORT)
					.show();
				}
				
				
			}
		}

	};

	// 获取航班号解析内容 回调方法
	@SuppressWarnings("unused")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TripgMessage.HANGBAN:
				handMessageDefault(hangInterfaces, DongTaiMain.this, msg);
				break;
			case 2:
				handMsg(hangInterfaces, DongTaiMain.this, msg);
				break;
			}

		}

		private void handMsg(DongHangInterfaces bf, DongTaiMain dongTaiMain,
				Message msg) {
			if (bf == null)
				return;
			if (bf.progressDialog != null)
				bf.progressDialog.dismiss();
			if (msg.obj == null) {
				Toast.makeText(dongTaiMain, "网络链接超时", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(dongTaiMain, "" + msg.obj, Toast.LENGTH_LONG)
						.show();
			}
		}

		@SuppressWarnings("unchecked")
		private void handMessageDefault(DongHangInterfaces bf,
				DongTaiMain dongTaiMain, Message msg) {

			if (bf == null)
				return;
			if (bf.progressDialog != null)
				bf.progressDialog.dismiss();
			if (msg.obj == null) {
				Toast.makeText(dongTaiMain, "网络链接超时", Toast.LENGTH_SHORT)
						.show();
			} else {
				// TO doq
				// model = (AVVo)msg.obj;
				// Log.e("model", model.Code);
				// String jString = (String)msg.obj;
				// Log.e("航班号解析完成", jString);
				xmlList = (List<XmlCityModel>) msg.obj;
				if (xmlList.size() != 0) {

					Log.e("解析内容", xmlList.get(0).fNoString);
					Intent intent = new Intent(dongTaiMainthis,
							DongXiangqing.class);
					Bundle bundle1 = new Bundle();
					bundle1.putString("FilghtNo", xmlList.get(0).fNoString);
					bundle1.putString("FlightCompany",
							xmlList.get(0).fCompanyString);
					bundle1.putString("FlightDep", xmlList.get(0).fDepString);
					bundle1.putString("FlightArr", xmlList.get(0).fArrString);
					bundle1.putString("FlightDepAirport",
							xmlList.get(0).fDepAirportString);
					bundle1.putString("FlightArrAirport",
							xmlList.get(0).fArrAirportString);
					bundle1.putString("FlightDeptimePlan",
							xmlList.get(0).fDeptimePlanString);
					bundle1.putString("FlightArrtimePlan",
							xmlList.get(0).fArrtimePlanString);
					bundle1.putString("FlightDeptime",
							xmlList.get(0).fDeptimeString);
					bundle1.putString("FlightArrtime",
							xmlList.get(0).fArrtimeString);
					bundle1.putString("FlightState",
							xmlList.get(0).fStateString);
					bundle1.putString("FlightTerminal",
							xmlList.get(0).fTerminalString);
					// bundle1.putStringArray("list", xmlList.toArray(new
					// String[0]));
					intent.putExtras(bundle1);
					startActivityForResult(intent,
							RequestCode.TO_SELECT_DONGXIANG);

					// startActivity(intent);
				}else {
					Toast.makeText(dongTaiMain, "无航班信息", Toast.LENGTH_SHORT)
					.show();
				}

			}

		}

	};

	public void httpHangBang() {
		Log.e("httpHangBang", "航班号" + "----" + hangbanString);
		String url = "http://www.variflight.com/datainterface/Currentinterface.asp?Uid=fuL3jIsgEm&vNum="
				+ hangbanString;
		Log.e("httphangbanhao----url", url);
		// 调用封装的http方法 并且绑定回调函数
		hangInterfaces = new DongHangInterfaces(DongTaiMain.this, handler);
		hangInterfaces.getModel1FromGET(url, TripgMessage.HANGBAN, "1");

	}

	public void httpCityflight() {
		Log.e("httpCityflight", "touch" + "起降地");

		String url = "http://www.variflight.com/datainterface/Currentinterface.asp?Uid=fuL3jIsgEm&vOrg="
				+ depCity + "&vDst=" + jlString;
		Log.e("httpcityflight----url", url);
		// 调用封装的http方法 并且绑定回调函数
		cityInterfaces = new DongCityInterfaces(DongTaiMain.this, hanlderCity);
		cityInterfaces.getModelFromGET(url, TripgMessage.DEFAULT, "1");

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
}
