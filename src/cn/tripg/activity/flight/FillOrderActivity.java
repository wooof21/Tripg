package cn.tripg.activity.flight;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import model.flight.CabinVo;
import model.flight.FlightsVo;
import model.flight.G11Result;
import model.flight.PnrResult;

import org.codehaus.jackson.type.TypeReference;

import tools.des.Api;
import tools.des.DesCodeUtils;
import tools.json.DomParse;
import tools.json.JsonUtils;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.internet.Exit;
import cn.internet.Tools;
import cn.tripg.R;
import cn.tripg.widgit.ProgressDialogTripg;
import cn.tripg.xlistview.MarqueeTV;
import cn.vip.main.TCAMainActivity;

public class FillOrderActivity extends Activity {
	private ProgressDialog pdSingle1;
	private ProgressDialog pdSingle2;
	public int keyType;
	private int total;
	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
		// private int flag = 0;
		private Object obj = new Object();//
		public void handleMessage(Message msg) {

			switch (msg.what) {//
			case 1:// go
				int totals = 0;
				synchronized (obj) {// no use
					if (pdSingle1 != null)
						pdSingle1.dismiss();
					Log.e("single trip", "booked");
					keyType = 1;
					int res = bookSingle(goVo, goCabinIndex, 1, 0);
					if (res == 1) {
						if (type.equals("round")) {
							totals += goTotal+backTotal;
						}else {
							totals += goTotal;
						}
					
					// 两张票都预订成功
						Intent intent = new Intent(FillOrderActivity.this,
								PayOrderActivity.class);
					Bundle bundle = new Bundle();
						bundle.putString("pnr",
								FillOrderActivity.this.res.getPnr());
						bundle.putString("orderNo",
								FillOrderActivity.this.orderID);
						bundle.putString("totalPrice", "" + totals);
						intent.putExtras(bundle); 
						FillOrderActivity.this.startActivity(intent);
					} else {
						// notify book failed.
						Toast.makeText(FillOrderActivity.this, "系统繁忙，请稍后预订",
								Toast.LENGTH_SHORT).show();
					}
				}

				break;
			case 2:// back

				if (pdSingle1 != null)
					pdSingle1.dismiss();
				if (pdSingle2 != null)
					pdSingle2.dismiss();
				total = 0;
				synchronized (obj) {
					
					Log.e("round trip", "booked");
					keyType = 1;
					int res1 = bookSingle(goVo, goCabinIndex, 1, 0);
					Log.e("往返 第一张", ""+res1);
					if (res1 == 1) {
						total += goTotal;
						Log.e("handler case 2", "往返 第一张成功");
						
						keyType = 2;
						int res2 = bookSingle(backVo, backCabinIndex, 2, 1);
						Log.e("往返第二张", ""+res);
						if (res2 == 1) {
							Log.e("往返第二张", "往返 第2张成功");
							total += backTotal;
						}
						handler.postDelayed(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (pdSingle1 != null)
									pdSingle1.dismiss();
								if (pdSingle2 != null)
									pdSingle2.dismiss();
								Log.e("goTotal + backTotal + total 预订成功是调用判断", "" + goTotal
										+ " " + backTotal + " " + total);
								if (total != 0 && total == goTotal + backTotal) {
									// 两张票都预订成功
									Intent intent = new Intent(FillOrderActivity.this,
											PayOrderActivity.class);
									Bundle bundle = new Bundle();
									bundle.putString("pnr",
											FillOrderActivity.this.res.getPnr());
									bundle.putString("orderNo",
											FillOrderActivity.this.orderID);
									bundle.putString("totalPrice", "" + total);
									intent.putExtras(bundle);
									FillOrderActivity.this.startActivity(intent);
								} else {
									Toast.makeText(FillOrderActivity.this, "系统繁忙，请稍后预订",
											Toast.LENGTH_LONG).show();
								}
							}
						}, 1000);
					}else{
						Toast.makeText(FillOrderActivity.this, "系统繁忙，请稍后预订",
								Toast.LENGTH_LONG).show();//
					}

				}

				break;
			default:
				if (pdSingle1 != null)
					pdSingle1.dismiss();
				if (pdSingle2 != null)
					pdSingle2.dismiss();
				Toast.makeText(FillOrderActivity.this, "系统繁忙，请稍后预订",
						Toast.LENGTH_LONG).show();

			}
		}
	};
	/********************** Go ************************/
	// 2011-05-20 深航ZH1650
	private TextView orderHeader;
	// 15:30
	private TextView depTime;
	// 长春龙嘉
	private TextView depTower;
	// 15:30
	private TextView arrTime;
	// 长春龙嘉
	private TextView arrTower;
	// Y舱100折
	private TextView cabinDiscount;
	// 机票价格 960
	private TextView ticketPrice;
	// 机建 50
	private TextView constructPrice;
	// 燃油 120
	private TextView oilPrice;
	// 查看退改签
	private LinearLayout returnInfo;

	private TextView arrTowerS;
	private TextView depTowerS;
	private TextView arrTowerR;
	private TextView arrTowerRB;
	private TextView depTowerR;
	private TextView depTowerRB;
	/******************** Back *****************************/
	// 2011-05-20 深航ZH1650
	private TextView orderHeaderBack;
	// 15:30
	private TextView depTimeBack;
	// 长春龙嘉
	private TextView depTowerBack;
	// 15:30
	private TextView arrTimeBack;
	// 长春龙嘉
	private TextView arrTowerBack;
	// Y舱100折
	private TextView cabinDiscountBack;
	// 机票价格 960
	private TextView ticketPriceBack;
	// 机建 50
	private TextView constructPriceBack;
	// 燃油 120
	private TextView oilPriceBack;
	// 查看退改签
	private LinearLayout returnInfoBack;

	/********************** Common ****************************/
	// 添加登机人
	private ImageView addMan;
	private LinearLayout aBoardman;
	// 联系手机
	// 13222326765
	private EditText phoneNum;
	// 保险
	private FrameLayout insuranceArea;
	// 1份
	private TextView insuranceNum;
	// 配送方式
	private FrameLayout jpsArea;
	// 不需报销凭证
	private TextView jpsNum;
	private LinearLayout postInfo;
	// 订单总价
	private TextView totalPrice;
	// 下一步
	private TextView nextBtn;
	private FlightsVo goVo;
	private FlightsVo backVo;
	// single or round.
	private String type;
	private PnrResult res;
	// private String pnrStr;
	// private String totalPriceStr;
	private String passengerName;
	private String orderID;
	private String goCabinIndex;
	private String backCabinIndex;
	private int goSingleTicketPrice = 0;
	private int backSingleTicketPrice = 0;
	private int goTotal = 0;
	private int backTotal = 0;
	private int boardManCount;
	public String depCityString;
	public String arrCityString;
	
	private LinearLayout contact;
	private ImageView addCon;
	private MarqueeTV address;
	private TextView zCode;
	private FrameLayout addressF;
	private FrameLayout zCodeF;
	private String contactName;
	private String contactPhone;
	
	
	public void onWindowFocusChanged(boolean hasFocus) {
		if (pdSingle1 != null) {
			ImageView imageView = (ImageView) pdSingle1
					.findViewById(R.id.loading_img);
			AnimationDrawable animationDrawable = (AnimationDrawable) imageView
					.getDrawable();
			animationDrawable.start();
		}
		if (pdSingle2 != null) {
			ImageView imageView = (ImageView) pdSingle2
					.findViewById(R.id.loading_img);
			AnimationDrawable animationDrawable = (AnimationDrawable) imageView
					.getDrawable();
			animationDrawable.start();
		}

	}

	public String getAPassengerString(String realName, String idCardNum,
			String ticketPrice, String clearPrice, String constructFee,
			String OilFee, String total, String insuranceNum) {

		return realName + "@A@" + idCardNum + "@" + ticketPrice + "@"
				+ clearPrice + "@" + constructFee + "@" + OilFee + "@" + total
				+ "@" + insuranceNum + "@0";
	}

	public String getPassengersString(String[] pas) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < pas.length; i++) {
			buffer.append(pas[i]);
			buffer.append("|");
		}
		String result = buffer.toString();
		result = result.substring(0, result.length() - 1);
		return result;
	}

	private void calculateTotalPrice(int personNum, int insuranceFee) {
		if ("single".equals(type)) {
			int num = 0;
			if ("1份".equals(insuranceNum.getText().toString())) {
				num = 1;
			} else if ("2份".equals(insuranceNum.getText().toString())) {
				num = 2;
			} else {
				num = 0;
			}
			int total = personNum * (goSingleTicketPrice + num * insuranceFee);
			goTotal = total;
			totalPrice.setText("￥" + total);
		} else if ("round".equals(type)) {
			int num = 0;
			if ("1份".equals(insuranceNum.getText().toString())) {
				num = 1;
			} else if ("2份".equals(insuranceNum.getText().toString())) {
				num = 2;
			} else {
				num = 0;
			}
			int total = personNum * (goSingleTicketPrice + num * insuranceFee);
			goTotal = total;
			int total2 = personNum
					* (backSingleTicketPrice + num * insuranceFee);
			backTotal = total2;
			totalPrice.setText("￥" + (total + total2));
		} else {
			totalPrice.setText("￥" + 0);
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	private int getSinglePersonTotalPrice(int insuranceFee, int flag) {
		int num = 0;
		// if ("1份".equals(insuranceNum.getText().toString())) {
		// num = 1;
		// } else if ("2份".equals(insuranceNum.getText().toString())) {
		// num = 2;
		// } else {
		// num = 0;
		// }
		// ///////////////////////////////////////////////
		if ("single".equals(type)) {
			if (flag == 0) {
				int total = (goSingleTicketPrice + num * insuranceFee);
				return total;
			}
		} else if ("round".equals(type)) {
			if (flag == 0) {
				int total = (goSingleTicketPrice + num * insuranceFee);
				return total;
			}
			if (flag == 1) {
				int total = (backSingleTicketPrice + num * insuranceFee);
				return total;
			}
		} else {
			return 0;
		}
		return 0;
	}

	private void fillFlightInfo() {
		if ("single".equals(type)) {
			orderHeader.setText(goVo.getFlightDate() + " "
					+ goVo.getCarrierReferred() + goVo.getFlightNo());
			Log.e("goVo.getFlightNo()------------------", ""+goVo.getFlightNo());
			String depTimeStr = goVo.getDepTime().substring(0, 2).trim() + ":"
					+ goVo.getDepTime().substring(2, 4);
			String arrTimeStr = goVo.getArrTime().substring(0, 2).trim() + ":"
					+ goVo.getArrTime().substring(2, 4);

			depTime.setText(depTimeStr);
			arrTime.setText(arrTimeStr);
			depTower.setText(goVo.getDepCityReferred());
			arrTower.setText(goVo.getArrCityReferred());

			arrTowerS.setText("航站楼：" + goVo.getArrTower());
			depTowerS.setText("航站楼：" + goVo.getDepTower());

			CabinVo cabin = goVo.getCabins().get(Integer.valueOf(goCabinIndex));
			cabinDiscount.setText(cabin.getName() + "舱"
					+ (Integer.parseInt(cabin.getDiscount()) / 10) + "折扣");
			System.out.println("cabin ei ---> " + cabin.getEi());
			ticketPrice
					.setText("￥" + cabin.getSinglePrice().replace(".00", ""));
			constructPrice.setText("￥" + cabin.getTax().replace(".00", ""));
			oilPrice.setText("￥" + cabin.getFuel().replace(".00", ""));
			goSingleTicketPrice += Integer.valueOf(cabin.getSinglePrice()
					.replace(".00", ""));
			goSingleTicketPrice += Integer.valueOf(cabin.getTax().replace(
					".00", ""));
			goSingleTicketPrice += Integer.valueOf(cabin.getFuel().replace(
					".00", ""));

		} else if ("round".equals(type)) {
			/*--------------------go--------------------------------------*/
			Log.e("fillFlightInfo type --->", "round");
			orderHeader.setText(goVo.getFlightDate() + " "
					+ goVo.getCarrierReferred() + goVo.getFlightNo());
			String depTimeStr = goVo.getDepTime().substring(0, 2).trim() + ":"
					+ goVo.getDepTime().substring(2, 4);
			String arrTimeStr = goVo.getArrTime().substring(0, 2).trim() + ":"
					+ goVo.getArrTime().substring(2, 4);

			depTime.setText(depTimeStr);
			arrTime.setText(arrTimeStr);
			depTower.setText(goVo.getDepCityReferred());
			arrTower.setText(goVo.getArrCityReferred());
			depTowerR.setText("航站楼：" + goVo.getDepTower());
			arrTowerR.setText("航站楼：" + goVo.getArrTower());
			depTowerRB.setText("航站楼：" + goVo.getArrTower());
			arrTowerRB.setText("航站楼：" + goVo.getDepTower());

			CabinVo cabin = goVo.getCabins().get(Integer.valueOf(goCabinIndex));
			cabinDiscount.setText(cabin.getName() + "舱"
					+ (Integer.parseInt(cabin.getDiscount()) / 10) + "折扣");
			ticketPrice
					.setText("￥" + cabin.getSinglePrice().replace(".00", ""));
			constructPrice.setText("￥" + cabin.getTax().replace(".00", ""));
			oilPrice.setText("￥" + cabin.getFuel().replace(".00", ""));
			goSingleTicketPrice += Integer.valueOf(cabin.getSinglePrice()
					.replace(".00", ""));
			goSingleTicketPrice += Integer.valueOf(cabin.getTax().replace(
					".00", ""));
			goSingleTicketPrice += Integer.valueOf(cabin.getFuel().replace(
					".00", ""));
			/*--------------------back--------------------------------------*/
			orderHeaderBack.setText(backVo.getFlightDate() + " "
					+ backVo.getCarrierReferred() + backVo.getFlightNo());
			String depTimeBackStr = backVo.getDepTime().substring(0, 2).trim()
					+ ":" + backVo.getDepTime().substring(2, 4);
			String arrTimeBackStr = backVo.getArrTime().substring(0, 2).trim()
					+ ":" + backVo.getArrTime().substring(2, 4);

			depTimeBack.setText(depTimeBackStr);
			arrTimeBack.setText(arrTimeBackStr);
			depTowerBack.setText(backVo.getDepCityReferred());
			arrTowerBack.setText(backVo.getArrCityReferred());
			CabinVo cabinBack = backVo.getCabins().get(
					Integer.valueOf(backCabinIndex));
			cabinDiscountBack.setText(cabinBack.getName() + "舱"
					+ (Integer.parseInt(cabinBack.getDiscount()) / 10) + "折扣");
			ticketPriceBack.setText("￥"
					+ cabinBack.getSinglePrice().replace(".00", ""));
			constructPriceBack.setText("￥"
					+ cabinBack.getTax().replace(".00", ""));
			oilPriceBack.setText("￥" + cabinBack.getFuel().replace(".00", ""));
			backSingleTicketPrice += Integer.valueOf(cabinBack.getSinglePrice()
					.replace(".00", ""));
			backSingleTicketPrice += Integer.valueOf(cabinBack.getTax()
					.replace(".00", ""));
			backSingleTicketPrice += Integer.valueOf(cabinBack.getFuel()
					.replace(".00", ""));

		} else {
			Log.e("FillOrder", "type == null");
		}
	}

	private void prepareTripDataAndAllView() {
		// get type that pass from flight query activity.

		// get FlightsVo and cabin index from flight query activity.
		if ("single".equals(type)) {
			goCabinIndex = getIntent().getExtras().getString("goCabinIndex");
			goVo = (FlightsVo) getIntent().getSerializableExtra("goVo");
			depCityString  = (String)getIntent().getExtras().getString("depCityStr");
			arrCityString = (String)getIntent().getExtras().getString("arrCityStr");
		} else if ("round".equals(type)) {
			goCabinIndex = getIntent().getExtras().getString("goCabinIndex");
			backCabinIndex = getIntent().getExtras()
					.getString("backCabinIndex");
			goVo = (FlightsVo) getIntent().getSerializableExtra("goVo");
			backVo = (FlightsVo) getIntent().getSerializableExtra("backVo");
			depCityString  = (String)getIntent().getExtras().getString("depCityStr");
			arrCityString = (String)getIntent().getExtras().getString("arrCityStr");
		} else {
			Log.e("FillOrder", "type == null");
		}
		Log.e("G11 城市名----	", ""+depCityString +"---"+arrCityString);
		
		/***************************** Go **************************************/
		orderHeader = (TextView) findViewById(R.id.top_text);
		depTime = (TextView) findViewById(R.id.dep_time);
		depTower = (TextView) findViewById(R.id.dep_tower);
		arrTime = (TextView) findViewById(R.id.arr_time);
		arrTower = (TextView) findViewById(R.id.arr_tower);
		depTowerS = (TextView) findViewById(R.id.dep_tower_signle);
		arrTowerS = (TextView) findViewById(R.id.arr_tower_signle);
		cabinDiscount = (TextView) findViewById(R.id.craft_discount_text);
		ticketPrice = (TextView) findViewById(R.id.ticket_price);
		constructPrice = (TextView) findViewById(R.id.construct_price);
		oilPrice = (TextView) findViewById(R.id.oil_price);
		returnInfo = (LinearLayout) findViewById(R.id.return_info);
		returnInfo.setOnClickListener(new ReturnInfoOnClickListener());
		/************************************ Back ***********************************/
		if ("round".equals(type)) {
			orderHeaderBack = (TextView) findViewById(R.id.top_text_back);
			depTimeBack = (TextView) findViewById(R.id.dep_time_back);
			depTowerBack = (TextView) findViewById(R.id.dep_tower_back);
			arrTowerR = (TextView) findViewById(R.id.arr_tower_round);
			depTowerR = (TextView) findViewById(R.id.dep_tower_round);
			arrTowerRB = (TextView) findViewById(R.id.arr_tower_back_round);
			depTowerRB = (TextView) findViewById(R.id.dep_tower_back_round);
			arrTimeBack = (TextView) findViewById(R.id.arr_time_back);
			arrTowerBack = (TextView) findViewById(R.id.arr_tower_back);
			cabinDiscountBack = (TextView) findViewById(R.id.craft_discount_text_back);
			ticketPriceBack = (TextView) findViewById(R.id.ticket_price_back);
			constructPriceBack = (TextView) findViewById(R.id.construct_price_back);
			oilPriceBack = (TextView) findViewById(R.id.oil_price_back);
			returnInfoBack = (LinearLayout) findViewById(R.id.return_info_back);
			returnInfoBack
					.setOnClickListener(new ReturnInfoBackOnClickListener());
		}
		/*********************************** Common **********************************/
		addMan = (ImageView) findViewById(R.id.addman);
		addMan.setOnClickListener(new AddManOnClickListener());
		aBoardman = (LinearLayout) findViewById(R.id.a_boardman);
		// phoneNumberArea = (FrameLayout)findViewById(R.id.contact);
		phoneNum = (EditText) findViewById(R.id.phone_num);
		insuranceArea = (FrameLayout) findViewById(R.id.insurance_area);
		insuranceArea.setOnClickListener(new InsuranceAreaOnClickListener());
		insuranceNum = (TextView) findViewById(R.id.insur_num);
		jpsArea = (FrameLayout) findViewById(R.id.jps_area);
		jpsArea.setOnClickListener(new JpsAreaOnClickListener());
		jpsNum = (TextView) findViewById(R.id.jps_num);
		postInfo = (LinearLayout) findViewById(R.id.post_info_holder);
		totalPrice = (TextView) findViewById(R.id.total_price);
		nextBtn = (TextView) findViewById(R.id.bottom_order_next);
		
			nextBtn.setOnClickListener(new NextBtnOnClickListener());
			contact = (LinearLayout)findViewById(R.id.a_contact);
			addCon = (ImageView)findViewById(R.id.addcontacter);
			addCon.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(contact.getChildCount() == 0){
						Intent intent = new Intent(FillOrderActivity.this,
								ChooseContacter.class);
						startActivityForResult(intent, 10);
						overridePendingTransition(android.R.anim.fade_in,
								android.R.anim.fade_out);
					}else{
						Toast.makeText(FillOrderActivity.this, "一人足矣!", Toast.LENGTH_LONG).show();
					}
					
				}
			});
			address = (MarqueeTV)findViewById(R.id.flightorder_address_tv);
			zCode = (TextView)findViewById(R.id.flightorder_code_tv);
			addressF = (FrameLayout)findViewById(R.id.flightorder_address);
			zCodeF = (FrameLayout)findViewById(R.id.flightorder_code);
			addressF.setVisibility(View.GONE);
			zCodeF.setVisibility(View.GONE);
		
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case RequestCode.TO_FILL_BOARD_MAN_INFO:

			if (resultCode == ResultCode.SUCCESS) {
				Bundle bundle = data.getExtras();
				String userName = bundle.getString("userName");
				String idType = bundle.getString("idType");
				String idNum = bundle.getString("idNum");

				if (userName.equals("0")) {
					Toast.makeText(FillOrderActivity.this, "无乘机人", Toast.LENGTH_LONG).show();
				}else {
					LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
					final RelativeLayout rl = (RelativeLayout) inflater.inflate(
							R.layout.boardman_item, null);
					RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.MATCH_PARENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);
					((TextView) (rl.findViewById(R.id.username))).setText(userName);
					((TextView) (rl.findViewById(R.id.idcard))).setText(idNum);
					((TextView) (rl.findViewById(R.id.idType))).setText(idType);
					// rl.getChildAt(0) is red image icon -
					rl.getChildAt(0).setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// remove the item you clicked.
							aBoardman.removeView(rl);
							boardManCount = aBoardman.getChildCount();
							calculateTotalPrice(boardManCount, 20);
						}
					});
					aBoardman.addView(rl, rlp);
					boardManCount = aBoardman.getChildCount();
					calculateTotalPrice(boardManCount, 20);
				}
				
				
				
				
				
			}
			break;
			
		case 10:
			if (resultCode == ResultCode.SUCCESS) {
				Bundle bundle = data.getExtras();
				contactName = bundle.getString("name");
				contactPhone = bundle.getString("phone").replace(" ", "");
				
				LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				final RelativeLayout rl = (RelativeLayout) inflater.inflate(
						R.layout.boardman_item, null);
				RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				((TextView) (rl.findViewById(R.id.username))).setText(contactName);
				((TextView) (rl.findViewById(R.id.idcard))).setText(contactPhone);
				rl.getChildAt(0).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// remove the item you clicked.
						contact.removeView(rl);

					}
				});
				contact.addView(rl, rlp);
			}
			
			break;
		case 11:
			if (resultCode == ResultCode.SUCCESS) {
				String _address = data.getStringExtra("address");
				String _code = data.getStringExtra("code");
				Log.e("onactivityresult address", _address);
				Log.e("onactivityresult code", _code);
				address.setText(_address);
				zCode.setText(_code);
			}else{
				addressF.setVisibility(View.GONE);
				zCodeF.setVisibility(View.GONE);
			}
			break;
		}
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Exit.getInstance().addActivity(this);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		type = getIntent().getExtras().getString("type");
		if ("single".equals(type)) {
			setContentView(R.layout.fill_order_single);
		} else if ("round".equals(type)) {
			setContentView(R.layout.fill_order_round);
		}
		prepareTripDataAndAllView();
		ImageView backBtn = (ImageView) findViewById(R.id.title_order_back);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);
			}
		});
		fillFlightInfo();
	}

	class ReturnInfoOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					FillOrderActivity.this);
			final AlertDialog al = builder.create();
			LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			final LinearLayout rl = (LinearLayout) inflater.inflate(
					R.layout.return_ticket_dialog, null);
			// LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
			// LinearLayout.LayoutParams.MATCH_PARENT,
			// LinearLayout.LayoutParams.MATCH_PARENT);
		
			TextView cabinInfo = (TextView) rl
					.findViewById(R.id.cabin_info_discount);
			TextView content = (TextView) rl
					.findViewById(R.id.return_content_view);
			CabinVo cabin = goVo.getCabins().get(Integer.valueOf(goCabinIndex));
			cabinInfo.setText(cabin.getName() + "舱" + "  "
					+ (Integer.parseInt(cabin.getDiscount()) / 10) + "折扣");
			TextView cabinPrice = (TextView) rl.findViewById(R.id.cabin_price);
			cabinPrice.setText("￥" + cabin.getSinglePrice().replace(".00", ""));
			System.out.println("2. cabin ei ---> " + cabin.getEi());
			String eiString = (String)cabin.getEi().toString();
			if (eiString.length() != 0 || eiString.equals("null")) {
				content.setText(cabin.getEi());
			}else {
				content.setText("退改签规则以航空公司最新标准执行");
			}
			
			al.show();
			// al.setContentView(rl, llp);
			al.setContentView(rl);
			al.setCanceledOnTouchOutside(true);
			rl.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					al.dismiss();
				}
			});
		}
	}

	class ReturnInfoBackOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					FillOrderActivity.this);
			final AlertDialog al = builder.create();
			LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			final LinearLayout rl = (LinearLayout) inflater.inflate(
					R.layout.return_ticket_dialog, null);
			LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			TextView cabinInfo = (TextView) rl
					.findViewById(R.id.cabin_info_discount);
			TextView content = (TextView) rl
					.findViewById(R.id.return_content_view);
			CabinVo cabin = backVo.getCabins().get(Integer.valueOf(backCabinIndex));
			cabinInfo.setText(cabin.getName() + "舱" + "  "
					+ (Integer.parseInt(cabin.getDiscount()) / 10) + "折扣");
			TextView cabinPrice = (TextView) rl.findViewById(R.id.cabin_price);
			cabinPrice.setText("￥" + cabin.getSinglePrice().replace(".00", ""));
			
			String eiString = (String)cabin.getEi().toString();
			if (eiString.length() != 0 || eiString.equals("null")) {
				content.setText(cabin.getEi());
			}else {
				content.setText("退改签规则以航空公司最新标准执行");
			}
			
			
			al.show();
			al.setContentView(rl, llp);
			al.setCanceledOnTouchOutside(true);
			rl.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					al.dismiss();
				}
			});
		}
	}

	class AddManOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(FillOrderActivity.this,
					ChoosePassengers.class);
			startActivityForResult(intent, RequestCode.TO_FILL_BOARD_MAN_INFO);
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		}
	}

	class InsuranceAreaOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					FillOrderActivity.this);
			final AlertDialog al = builder.create();
			LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			final LinearLayout rl = (LinearLayout) inflater.inflate(
					R.layout.dialog_id_type, null);

			LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			ListView types = (ListView) rl.findViewById(R.id.id_type_list);

			ArrayAdapter<CharSequence> adapter = ArrayAdapter
					.createFromResource(FillOrderActivity.this,
							R.array.insurance_types, R.layout.item_id_type);
			types.setAdapter(adapter);

			al.show();
			al.setContentView(rl, llp);
			al.setCanceledOnTouchOutside(true);
			types.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					TextView tv = (TextView) view;
					insuranceNum.setText(tv.getText());
					calculateTotalPrice(boardManCount, 20);
					al.dismiss();
				}
			});
		}
	}

	class OrderGeneratorSingleTrip {
		public String getAirLineRoundGroupDESCode(String depCityCode,
				String arrCityCode, String flightDate, String flightNo,
				String cabinRef, String roundFilghtdate, String roundFlightNo,
				String roundCabinRef) {
			String airLineGroupStr = depCityCode + "|" + arrCityCode + "|"
					+ flightDate + "|" + flightNo + "|" + cabinRef + "#"
					+ arrCityCode + "|" + depCityCode + "|" + roundFilghtdate
					+ "|" + roundFlightNo + "|" + roundCabinRef;

			String sign = "";
			try {
				sign = DesCodeUtils.encode(Api.key, airLineGroupStr);
				sign = sign.replace("=", "@");
				sign = sign.replace("+", "-");
				sign = sign.replace("/", "_");
			} catch (Exception e) {
				e.printStackTrace();
			}
			Log.e("@ 756 OrderGeneratorSingleTrip ---> ", sign);
			return sign;
		}
		
		public String getAirLineGroupDESCode(String depCityCode,
				String arrCityCode, String flightDate, String flightNo,
				String cabinRef) {
			String airLineGroupStr = depCityCode + "|" + arrCityCode + "|"
					+ flightDate + "|" + flightNo + "|" + cabinRef;

			String sign = "";
			try {
				sign = DesCodeUtils.encode(Api.key, airLineGroupStr);
				sign = sign.replace("=", "@");
				sign = sign.replace("+", "-");
				sign = sign.replace("/", "_");
			} catch (Exception e) {
				e.printStackTrace();
			}
			Log.e("@ 775 getAirLineGroupDESCode ---> ", sign);
			return sign;
		}

		public String getOrderSign(String company2Code) {
			String signTmp = "";
			try {
				signTmp = DesCodeUtils.encode(Api.key, company2Code);
				signTmp = signTmp.replace("=", "@");
				signTmp = signTmp.replace("+", "-");
				signTmp = signTmp.replace("/", "_");
			} catch (Exception e) {
				e.printStackTrace();
			}
			String ff = "android#carrier#" + signTmp;
			String sign = "";
			try {
				sign = DesCodeUtils.encode(Api.key, ff);
				sign = sign.replace("=", "@");
				sign = sign.replace("+", "-");
				sign = sign.replace("/", "_");
			} catch (Exception e) {
				e.printStackTrace();
			}
			Log.e("@ 799 getOrderSign ---> ", sign);
			return sign;
		}

		public String getPassengerGroupStr(String passengerType,
				String passengerName, String passengerIDType,
				String passengerIDNum, String passengerWithChild) {
			String passengerGroupStr = passengerType + "|" + passengerName
					+ "|" + passengerIDType + "|" + passengerIDNum
					+ passengerWithChild;
			Log.e("@ 809 getPassengerGroupStr ---> ", passengerGroupStr);
			return passengerGroupStr;
		}

		public String getAirLineGroupStr(String depCityCode,
				String arrCityCode, String flightDate, String flightNo,
				String cabinName) {
			String airLineGroupStr = depCityCode + "|" + arrCityCode + "|"
					+ flightDate + "|" + flightNo + "|" + cabinName;
			Log.e("@ 818 getAirLineGroupStr ---> ", airLineGroupStr);
			return airLineGroupStr;
		}

		// //////////////////////////////////////////////////////////////////////////////////////
		public String getGroupDESCode(String[] groupStrs) {
			String finalStr = "";
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < groupStrs.length; i++) {
				buffer.append(groupStrs[i] + "#");
			}
			String s = buffer.toString();
			finalStr = s.substring(0, s.length() - 1);
			String sign = "";
			try {
				sign = DesCodeUtils.encode(Api.key, finalStr);
				sign = sign.replace("=", "@");
				sign = sign.replace("+", "-");
				sign = sign.replace("/", "_");
			} catch (Exception e) {
				e.printStackTrace();
			}
			Log.e("@ 840 getGroupDESCode ---> ", sign);
			return sign;
		}
	}

	private PnrResult parseJsonData(String jsonStr) {
		PnrResult pnr = null;
		if (jsonStr == null) {
			Toast.makeText(this, "网络异常", Toast.LENGTH_LONG).show();
			finish();// ???
		}
		try {
			if (jsonStr.equals("error")) {
				Toast.makeText(this, "网络异常", Toast.LENGTH_LONG).show();
				finish();// ???
			} else {
				TypeReference<PnrResult> tvo = new TypeReference<PnrResult>() {
				};
				pnr = JsonUtils.json2GenericObject(jsonStr, tvo);
				if (pnr == null) {
					Toast.makeText(this, "系统繁忙，请稍后预订", Toast.LENGTH_LONG)
							.show();
					finish();// ???
				}
			}
		} catch (Exception e) {
			// Toast.makeText(this, "数据解析失败", Toast.LENGTH_LONG).show();
			finish();// ??? 
		}
		return pnr;
	}

	public String insertSingleTripG11(String userID, String passengers,
			String orderId, String pnr, String boardManName, String flightNo,
			String flightDate, String depCity, String arrCity,
			String ticketPrice, String clearPrice, String cabinName,
			String constructTax, String oilTax, String totalPrice,
			String changeDatePrescribe, String airCompanyCode,
			String companyID, String deptID, String discount, String idNum,
			String depTime, String arrTime, String equip, String userName,
			String realName, String contact, String isStop, String orderSafe,
			String refundPrescrible) {

		Log.e("insertSingleTripG11 flightDate", flightDate);
		Log.e("insertSingleTripG11 depCity", depCity);
		Log.e("insertSingleTripG11 arrCity", arrCity);
		Log.e("insertSingleTripG11 orderId", orderId);
		Log.e("insertSingleTripG11 pnr", pnr);
		// String spStr2[] = changeDatePrescribe.split("|");
		// Log.e("changeDatePrescribe",
		// ""+(String)spStr2[0]+"---"+(String)spStr2[1]);
		Log.e("changeDatePrescribe11111", "" + changeDatePrescribe);
		Log.e("refundPrescrible2222", "" + refundPrescrible);
		String arrG11CityString;
		String depG11CityString;
		if (keyType == 2) {//
			arrG11CityString = arrCityString;
			depG11CityString = depCityString;
			Log.e("G11 round city ", arrG11CityString+"------"+depG11CityString);
		}else {
			arrG11CityString = depCityString;
			depG11CityString = arrCityString;
			Log.e("G11 single city ", arrG11CityString+"------"+depG11CityString);
		}
		Log.e("flightNo.substring(2)------------------", ""+flightNo.substring(2));
		//获取客户单位
		SharedPreferences sharedPre = getSharedPreferences("config",
				MODE_PRIVATE);
		String compayIdone = sharedPre.getString("compayid", "null");
		//192.168.2.17  139.210.99.29:83
		String gOneUrlString = "http://www.tripg.cn/phone_api/save_order2.php";
		String singleG11URL = "User_id="
				+ userID// 用户ID//
				+ "&Passengers="
				+ passengers// 乘机人
				+ "&Order_resid="
				+ orderId// 订单号
				+ "&Order_pnr="
				+ pnr// PNR
				+ "&Order_people="
				+ getStringUTF8(boardManName)// 乘机人姓名
				+ "&Order_number="
				+ flightNo.substring(2)// 航班号
				+ "&Order_date="
				+ flightDate// 航班起飞日期
				+ "&Order_scity="
				+ getStringUTF8(depG11CityString)// 航班起飞城市1
				+ "&Order_acity="
				+ getStringUTF8(arrG11CityString)// 航班降落城市1//
				+ "&Order_jprice="
				+ ticketPrice// 销售价格
				+ "&Clearing_price="
				+ clearPrice// 结算价格123
				+ "&Order_space="
				+ cabinName// 舱位
				+ "&Order_tax="
				+ constructTax// 机场建设费
				+ "&Order_yq="
				+ oilTax// 燃油附加税
				+ "&Order_total="
				+ totalPrice// 机票总价11
				+ "&Order_cmt="
				+ getStringUTF8(changeDatePrescribe)// 改期规定
				+ "&Order_company="
				+ airCompanyCode// 航空公司代码11
				+ "&Company_id="+compayIdone//客户单位
				+ "&Dept_id=null"
				+ "&Order_discount="
				+ discount// 折扣率
				+ "&Order_znum="
				+ idNum// 身份证号码
				+ "&Depart="
				+ depTime// 起飞时间
				+ "&Arrive="
				+ arrTime// 降落时间
				+ "&Equip="
				+ equip// 机型
				+ "&Username="
				+ userName// 订票人账户号HDKJCO002
				+ "&Realname="
				+ getStringUTF8(contactName)// 联系人姓名
				+ "&Telephone="
				+ contactPhone// 联系电话 手机
				+ "&Pay="
				+ "C"
				+ "&Platform="
				+ "4"
				+ "&Order_refund="
				+ getStringUTF8(refundPrescrible)// 退票规定
				+ "&Stop="
				+ isStop// 经停
				+ "&Order_status="
				+ getStringUTF8("1")
				+ "&Cus_type="
				+ "A"
				+ "&Order_safe=" + orderSafe + "&listPrice=" + ticketPrice;
//		Api api = new Api();
		Log.e("3---insertSingleTripG11-------url", singleG11URL+"-------"+gOneUrlString);
//		String result = api.doGetData(singleG11URL);
		String result="";
		
		
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance();
		try {

			byte[] xmlbyte = singleG11URL.getBytes("UTF-8");
			Log.e("预订接口上传 格式的内容---utf8---", singleG11URL);

			URL url = new URL(gOneUrlString);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setDoOutput(true);// 允许输出
			conn.setDoInput(true);
			conn.setUseCaches(false);// 不使用缓存
			conn.setRequestMethod("POST");
			conn.getOutputStream().write(xmlbyte);
			conn.getOutputStream().flush();
			conn.getOutputStream().close();

			Log.e("conn.getResponseCode()----", "" + conn.getResponseCode());
			if (conn.getResponseCode() != 200)
				throw new RuntimeException("请求url失败");
			int codeOrder = conn.getResponseCode();

			if (codeOrder == 200) {

				InputStream inStream = conn.getInputStream();// 获取返回数据

				// 使用输出流来输出字符(可选)
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				int len;
				while ((len = inStream.read(buf)) != -1) {
					out.write(buf, 0, len);
				}
				String string = out.toString("UTF-8");						
				Log.e("预订返回结果--------", ""+string);
				out.close();
				result = string;
				
				
			} else {
				Toast.makeText(FillOrderActivity.this, "预订失败",
						Toast.LENGTH_SHORT).show();
			}

		} catch (Exception e) {
			Log.e("解析出错了 -----", "" + e);
			Toast.makeText(FillOrderActivity.this, "预订失败",
					Toast.LENGTH_SHORT).show();
		}
		
		
		
		Log.e("5---insert G11 response", result);
		return result;
	}


	public String getFinalPassengers(FlightsVo goVo, int flag) {
		ArrayList<String> tmp = new ArrayList<String>();
		tmp.clear();
		int num = 0;
		if ("1份".equals(insuranceNum.getText().toString())) {
			num = 1;
		} else if ("2份".equals(insuranceNum.getText().toString())) {//
			num = 2;
		} else {
			num = 0;
		}
		for (int i = 0; i < aBoardman.getChildCount(); i++) {
			String realName = ((TextView) (aBoardman.getChildAt(i)
					.findViewById(R.id.username))).getText().toString();
			String idCardNum = ((TextView) (aBoardman.getChildAt(i)
					.findViewById(R.id.idcard))).getText().toString();
			String ticketPrice = goVo.getCabins()
					.get(Integer.valueOf(goCabinIndex)).getSinglePrice()
					.replace(".00", "");
			String clearPrice = ticketPrice;
			String constructFee = goVo.getCabins()
					.get(Integer.valueOf(goCabinIndex)).getTax()
					.replace(".00", "");
			String oilFee = goVo.getCabins().get(Integer.valueOf(goCabinIndex))
					.getFuel().replace(".00", "");
			String total = getSinglePersonTotalPrice(20, flag) + "";
			String insuranceNum = num + "";
			tmp.add(getAPassengerString(getStringUTF8(realName), idCardNum,
					ticketPrice, clearPrice, constructFee, oilFee, total,
					insuranceNum));
		}
		String[] arr = tmp.toArray(new String[0]);
		String result = getPassengersString(arr);
		Log.e("Passengers", result);
		return result;
	}

	public int bookSingle(final FlightsVo goVo, final String goCabinIndex,
			final int msg, final int flag) {

		SharedPreferences sharedPre = getSharedPreferences("config",
				MODE_PRIVATE);
		String userName = sharedPre.getString("username", "");
		String userID = sharedPre.getString("Result", "");
		String companyID = sharedPre.getString("company_id", "");
		String deptID = sharedPre.getString("dept_id", "");
		// insert G11 here.
		String idNum = ((TextView) (((RelativeLayout) aBoardman.getChildAt(0))
				.findViewById(R.id.idcard))).getText().toString();
		// get the id card name of first passengers.
		String passengers = getFinalPassengers(goVo, flag);
		/********/
		String[] ei = new String[]{};
		if (goVo.getCabins().get(Integer.valueOf(goCabinIndex))
				.getEi().length() != 0) {
			ei = goVo.getCabins().get(Integer.valueOf(goCabinIndex))
					.getEi().split("\\|");
		}else {
			ei = new String[]{"退改签规则以航空公司最新标准执行","",""}; 
		}
		

		Log.e("ei", goVo.getCabins().get(Integer.valueOf(goCabinIndex)).getEi());
		Log.e("ei length", ei.length + "");
//		if (ei.length < 3) {
//			Toast.makeText(FillOrderActivity.this, "网络异常", Toast.LENGTH_SHORT);
//			return 0;
//		}
		String totalPriceStr = "";
		if ("single".equals(type)) {
			totalPriceStr += goSingleTicketPrice;
		} else if ("round".equals(type)) {
			totalPriceStr += goSingleTicketPrice + backSingleTicketPrice;
		}
		Log.e("goSingleTicketPrice-----", "" + goSingleTicketPrice + "----"
				+ backSingleTicketPrice);

		String finalResult = insertSingleTripG11(userID, passengers, orderID,
				res.getPnr(), passengerName, goVo.getFlightNo(),
				goVo.getFlightDate(), goVo.getDepCity(), goVo.getArrCity(),
				goVo.getCabins().get(Integer.valueOf(goCabinIndex))
						.getSinglePrice().replace(".00", ""), goVo.getCabins()
						.get(Integer.valueOf(goCabinIndex)).getSinglePrice()
						.replace(".00", ""),
				goVo.getCabins().get(Integer.valueOf(goCabinIndex)).getName(),
				goVo.getCabins().get(Integer.valueOf(goCabinIndex)).getTax()
						.replace(".00", ""),
				goVo.getCabins().get(Integer.valueOf(goCabinIndex)).getFuel()
						.replace(".00", ""), totalPriceStr, ei[0],
				goVo.getCarrier(), companyID, deptID,
				goVo.getCabins().get(Integer.valueOf(goCabinIndex))
						.getDiscount(), idNum, goVo.getDepTime(),
				goVo.getArrTime(), goVo.getAircraft(), userName, passengerName,
				phoneNum.getText().toString().trim(), goVo.getStop(),
				insuranceNum.getText().toString().substring(0, 1), ei[1]
						+ ei[2]);
		Log.e("@1075 insertG11", finalResult);

		String result = getMessageFromXML(finalResult, 0);
//		Toast.makeText(FillOrderActivity.this, "预订成功", Toast.LENGTH_SHORT)
//				.show();
		Log.e("result--g11---", "----" + result.length());
		Log.e("result**g11***", "---" + result);
		if (result != null && result.equals("succeed")) {
			return 1;// succeed11
		} else {
			return 0;// failedkk
		}
	}


	class NextBtnOnClickListener implements OnClickListener {
		private HashMap<String, String> map;

		public NextBtnOnClickListener() {
			map = new HashMap<String, String>();
			map.put("身份证", "NI");
			map.put("护照", "PP");
			map.put("军官证", "ID");//
		}

		public void genPnrAndOrderNo(final int msg) {// thread
			String passengerType = "1";// 成人
			String idType = "";
			String idNum = "";
			String withChild = "||false||";
			int num = aBoardman.getChildCount();
			if (num == 0) {
				Toast.makeText(FillOrderActivity.this, "至少添加一个登机人",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (num > 3) {
				Toast.makeText(FillOrderActivity.this, "最多添加三个登机人",
						Toast.LENGTH_SHORT).show();
				return;
			}
			String phone = contactPhone;
			Log.e("phone", "" + phone);
			if ("".equals(phone)) {
				Toast.makeText(FillOrderActivity.this, "请选择联系人!",
						Toast.LENGTH_SHORT).show();
				return;
			} else if (phone.length() != 11) {
				Toast.makeText(FillOrderActivity.this, "请选择正确联系手机!",
						Toast.LENGTH_SHORT).show();
				return;
			}
			OrderGeneratorSingleTrip ogst = new OrderGeneratorSingleTrip();
			ArrayList<String> pasStr = new ArrayList<String>();
			for (int i = 0; i < num; i++) {
				RelativeLayout rl = (RelativeLayout) aBoardman.getChildAt(i);
				passengerName = ((TextView) (rl.findViewById(R.id.username)))
						.getText().toString();

				idType = ((TextView) (rl.findViewById(R.id.idType))).getText()
						.toString();
				idType = map.get(idType);
//				Log.e("idType", idType);
				idNum = ((TextView) (rl.findViewById(R.id.idcard))).getText()
						.toString();
				Log.e("idNum", idNum);

				String apas = ogst.getPassengerGroupStr(passengerType,
						passengerName, idType, idNum, withChild);
				pasStr.add(apas);
			}
			// sign D
			String passengerSign = ogst.getGroupDESCode(pasStr//
					.toArray(new String[0]));
			pasStr.clear();
			/*********************************D3.13_2**********************************/
			String depCityCode = goVo.getDepCity();// deppart city code 3 chars.
			String arrCityCode = goVo.getArrCity();// arrive city code 3 chars.
			String flightDate = goVo.getFlightDate();// flight date.
			String flightNo = goVo.getFlightNo();// flight no.
			// 往返
			String roundFlightNo="";
			String roundFlightDate="";
			if (type.equals("round")) {
				roundFlightNo = backVo.getFlightNo();
				roundFlightDate = backVo.getFlightDate();
			}
		

			String cabinName = goVo.getCabins()
					.get(Integer.valueOf(goCabinIndex)).getName();// cabin
			// 往返
			String roundCabinName="";
			if (type.equals("round")) {
				roundCabinName = backVo.getCabins()
						.get(Integer.valueOf(backCabinIndex)).getName();
			}
			

			String airLineSign = "";
			String sign = "";
			if (type.equals("round")) {

				airLineSign = ogst.getAirLineRoundGroupDESCode(depCityCode,
						arrCityCode, flightDate, flightNo, cabinName,roundFlightDate,roundFlightNo,roundCabinName);
				// sign G
				sign = ogst.getOrderSign(goVo.getCarrier());
				
			} else {
				// sign B
				airLineSign = ogst.getAirLineGroupDESCode(depCityCode,
						arrCityCode, flightDate, flightNo, cabinName);
				// sign G
				sign = ogst.getOrderSign(goVo.getCarrier());
			}
			
//			String depCityCode = goVo.getDepCity();// deppart city code 3 chars.
//			String arrCityCode = goVo.getArrCity();// arrive city code 3 chars.
//			String flightDate = goVo.getFlightDate();// flight date.
//			String flightNo = goVo.getFlightNo();// flight no.
//			
//			
//			
//			String cabinName = goVo.getCabins()
//					.get(Integer.valueOf(goCabinIndex)).getName();// cabin
//																	// reference.
//			/********************************D3.13*************************************************/
//			// sign B
//			String airLineSign = ogst.getAirLineGroupDESCode(depCityCode,
//					arrCityCode, flightDate, flightNo, cabinName);
//			// sign G
//			String sign = ogst.getOrderSign(goVo.getCarrier());

			Calendar c = Calendar.getInstance();
			c.add(Calendar.MINUTE, -30);// get time ahead of current time 30m

			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int day = c.get(Calendar.DAY_OF_MONTH);
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			String currentDate = year + "-" + month + "-" + day + "%20" + hour
					+ ":" + minute;
			Log.e("current time -30minutes", currentDate);

			final String orderURL = "http://flightapi.tripglobal.cn:8080/?cmd=ss&output=json&bookType=1&carrier="
					+ goVo.getCarrier()
					+ "&officeCode=&tk="
					+ goVo.getFlightDate()
					+ "&ct="
					+ phone
					+ "&routes="
					+ airLineSign
					+ "&passengers="
					+ passengerSign
					+ "&sign="
					+ sign;
			Log.e("@1234 orderURL", orderURL);//
			if (msg == 1){
				Log.e("msg == 1", ""+msg);
				pdSingle1 = ProgressDialogTripg.show(FillOrderActivity.this,
						null, null);
			}
				
			if (msg == 2) {
				Log.e("msg == 2", ""+msg);
				// pdSingle1 = ProgressDialogTripg.show(FillOrderActivity.this,
				// null,
				// null);
				pdSingle2 = ProgressDialogTripg.show(FillOrderActivity.this,
						null, null);
			}
			new Thread() {
				public void run() {
					//final Api api = new Api();
					final Tools tools = new Tools();
					Log.e("1111111111---orderURL", orderURL);
					//String result = api.doGetData(orderURL);
					String result = tools.getURL(orderURL);
					Log.e("result------", "" + result);
					res = parseJsonData(result);
					Log.e("res message ", res.Message);
					handler.post(new Runnable() {//
						@Override
						public void run() {
							if (res == null) {
								Toast.makeText(FillOrderActivity.this, "网络异常",
										Toast.LENGTH_SHORT).show();
								handler.sendEmptyMessage(0);//
								return;
							} else if ("E2000".equals(res.Code)) {
//								Log.e("预定成功=----", ""+res.Code);
								// Toast.makeText(FillOrderActivity.this,
								// "正在生成订单..", Toast.LENGTH_SHORT).show();
//								Log.e("2---pnr", res.getPnr());//139.210.99.29:83
								String orderNumURL = "http://www.tripg.cn/phone_api/"
										+ "/Get_Order_Resid1.php?order_pnr="
										+ res.getPnr();
								Log.e("orderNumUrl------", ""+orderNumURL);
								//orderID = api.doGetData(orderNumURL);
								orderID = tools.getURL(orderNumURL);
								Message message = new Message();
								message.what = msg;
								handler.sendMessage(message);
								System.out.println("orderID----------------"+orderID);
							} else {
								if (msg == 1){
									Log.e("msg == 1", ""+msg);
									pdSingle1.dismiss();
								}else if (msg == 2) {
									Log.e("msg == 2", ""+msg);
									// pdSingle1 = ProgressDialogTripg.show(FillOrderActivity.this,
									// null,
									// null);
									pdSingle2.dismiss();
								}
								Toast.makeText(FillOrderActivity.this,
										"系统繁忙，请稍后再试！", Toast.LENGTH_SHORT).show();
								handler.sendEmptyMessage(0);
								return;
							}
						}

					});
				};
			}.start();
		}

		@Override
		public void onClick(View v) {
			if ("single".equals(type)) {
				if(getInternet() == true){
					genPnrAndOrderNo(1);
				}else{
					Toast.makeText(FillOrderActivity.this, "网络链接已断开", Toast.LENGTH_LONG).show();
				}
				
				Log.e("-----------", "单程订单");
			} else if ("round".equals(type)) {
				if(getInternet() == true){
					System.out.println("往返订单被调用");
					genPnrAndOrderNo(2);
				}else{
					Toast.makeText(FillOrderActivity.this, "网络链接已断开", Toast.LENGTH_LONG).show();
				}
				Log.e("-----------", "往返订单");
			}
		}
	}

	public String getMessageFromXML(String xml, int flag) {
//		DomParse parse = new DomParse();
////		Log.e("xml", "xml==" + xml);
//		// List<G11Result> res = parse.doParse(xml);
////		Log.e("6---g11 result num:", "" + xml.length());
//		if (xml.length() == 8) {
//			Log.e("G11----", "插入G11成功");
//			return "succeed";
//		} else {
//			return "获取G11失败";
//		}
		DomParse parse = new DomParse();
//		Log.e("xml", "xml==" + xml);
		// List<G11Result> res = parse.doParse(xml);
//		Log.e("6---g11 result num:", "" + xml.length());
		if (xml.length() == 7) {
			Log.e("G11----", "插入G11成功");
			return "succeed";
		} else {
			return "获取G11失败";
		}

	}

	public String getStringUTF8(String xml) {
		// A StringBuffer Object
		StringBuffer sb = new StringBuffer();
		sb.append(xml);
		String xmString = "";
		String xmlUTF8 = "";
		try {
			xmString = new String(sb.toString().getBytes("UTF-8"));
			xmlUTF8 = URLEncoder.encode(xmString, "UTF-8");
			// System.out.println("utf-8 编码：" + xmlUTF8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// return to String Formed
		return xmlUTF8;
	}

	class JpsAreaOnClickListener implements OnClickListener {
		private boolean addFlag = false;
		private LinearLayout ll = null;

		@Override
		public void onClick(View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					FillOrderActivity.this);
			final AlertDialog al = builder.create();
			LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			final LinearLayout rl = (LinearLayout) inflater.inflate(
					R.layout.dialog_id_type, null);

			LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			ListView types = (ListView) rl.findViewById(R.id.id_type_list);

			ArrayAdapter<CharSequence> adapter = ArrayAdapter
					.createFromResource(FillOrderActivity.this,
							R.array.jps_types, R.layout.item_id_type);
			types.setAdapter(adapter);

			al.show();
			al.setContentView(rl, llp);
			al.setCanceledOnTouchOutside(true);
			types.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					TextView tv = (TextView) view;
					String result = tv.getText().toString();
					jpsNum.setText(result);
					if (position == 1) {
//						if (addFlag == true) {
//							al.dismiss();
//							return;
//						}
//						// add view
//						LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//						if (ll == null) {
//							ll = (LinearLayout) inflater.inflate(
//									R.layout.jps_item, null);
//						}
//						LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
//								LinearLayout.LayoutParams.MATCH_PARENT,
//								LinearLayout.LayoutParams.WRAP_CONTENT);
//						postInfo.addView(ll, llp);
//						addFlag = true;

						Intent intent = new Intent(FillOrderActivity.this, TCAMainActivity.class);
						intent.putExtra("type", "ff");
						startActivityForResult(intent, 11);
						overridePendingTransition(android.R.anim.fade_in,
								android.R.anim.fade_out);
						addFlag = true;
						addressF.setVisibility(View.VISIBLE);
						zCodeF.setVisibility(View.VISIBLE);
					
					}
					if (position == 0) {
						// view exist :remove
//						postInfo.removeAllViews();
//						addFlag = false;
//						// not exist :do nothing
						addFlag = false;
//						// not exist :do nothing
						addressF.setVisibility(View.GONE);
						zCodeF.setVisibility(View.GONE);
					}
					al.dismiss();
				}
			});
		}

	}
}
