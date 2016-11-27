package cn.vip.main;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.internet.Exit;
import cn.internet.Tools;
import cn.tripg.R;
import cn.tripg.activity.flight.ResultCode;
import cn.tripg.widgit.ProgressDialogTripg;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TCAMainActivity extends Activity{

	private ImageView back;
	private TextView title;
	private TextView add;
	private ListView lv;
	
	private Tools tools;
	private String tcaUrl = "http://www.tripg.cn/phone_api/contact_information/api.php";
	private String type;
	private ProgressDialog progressDialog;
	private ArrayList<HashMap<String, String>> tList;
	private ArrayList<HashMap<String, String>> cList;
	private ArrayList<HashMap<String, String>> aList;
	private TListAdapter tAdapter;
	private CListAdapter cAdapter;
	private AListAdapter aAdapter;
	
	private void prepareView(){
		back = (ImageView)findViewById(R.id.tca_back);
		title = (TextView)findViewById(R.id.tca_text);
		add = (TextView)findViewById(R.id.tca_add);
		lv = (ListView)findViewById(R.id.tca_lv);
		
		tools = new Tools();
		
	}
	
    @Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        	if(type.equals("ff")){
				setResult(ResultCode.FAILURE);
				finish();
			}else{
				finish();
			}
            return false; 
        } 
        return false; 
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.traveler);
		Exit.getInstance().addActivity(this);
		prepareView();
		type = getIntent().getExtras().getString("type");
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(type.equals("ff")){
					setResult(ResultCode.FAILURE);
					finish();
				}else{
					finish();
				}
					
			}
		});
		
		
		if(type.equals("t")){
			title.setText("常用旅客");		
			new TList().execute();
		}else if(type.equals("c")){
			title.setText("常用联系人");
			add.setBackgroundResource(R.drawable.add_new_c);
			new CList().execute();
		}else if(type.equals("a")){
			title.setText("常用地址");
			add.setBackgroundResource(R.drawable.add_new_a);
			new AList().execute();
		}else if(type.equals("ff")){
			title.setText("常用地址");
			add.setBackgroundResource(R.drawable.add_new_a);
			new AList().execute();
		}
		
		add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(type.equals("t")){
					Intent intent = new Intent(TCAMainActivity.this, AddNewTraveler.class);
					intent.putExtra("name", "");
					intent.putExtra("idType", "");
					intent.putExtra("idNo", "");
					intent.putExtra("eora", "a");
					intent.putExtra("id", "");
					startActivityForResult(intent, 1);
				}else if(type.equals("c")){
					Intent intent = new Intent(TCAMainActivity.this, AddNewContacter.class);
					intent.putExtra("name", "");
					intent.putExtra("areaCode", "");
					intent.putExtra("phone", "");
					intent.putExtra("eora", "a");
					intent.putExtra("id", "");
					startActivityForResult(intent, 3);
				}else if(type.equals("a")){
					Intent intent = new Intent(TCAMainActivity.this, NewAddAddress.class);
					intent.putExtra("province", "");
					intent.putExtra("city", "");
					intent.putExtra("area", "");
					intent.putExtra("address", "");
					intent.putExtra("code", "");
					intent.putExtra("eora", "a");
					intent.putExtra("id", "");
					startActivityForResult(intent, 5);
				}else if(type.equals("ff")){
					Intent intent = new Intent(TCAMainActivity.this, NewAddAddress.class);
					intent.putExtra("province", "");
					intent.putExtra("city", "");
					intent.putExtra("area", "");
					intent.putExtra("address", "");
					intent.putExtra("code", "");
					intent.putExtra("eora", "a");
					intent.putExtra("id", "");
					startActivityForResult(intent, 5);
				}
			}
		});
		
		
	}
	
	class TList extends AsyncTask<Void, Void, String>{

		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = ProgressDialogTripg.show(TCAMainActivity.this, null, null);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			if(result.equals("1")){
				tAdapter = new TListAdapter(TCAMainActivity.this, tList);
				lv.setAdapter(tAdapter);
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(TCAMainActivity.this, AddNewTraveler.class);
						intent.putExtra("name", tList.get(position).get("name"));
						intent.putExtra("idType", tList.get(position).get("idType"));
						intent.putExtra("idNo", tList.get(position).get("idNo"));
						intent.putExtra("id", ""+tList.get(position).get("id"));
						intent.putExtra("eora", "e");
						startActivityForResult(intent, 2);
					}
					
					
				});
				
				lv.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, final int position, long id) {
						// TODO Auto-generated method stub
						
						new AlertDialog.Builder(TCAMainActivity.this)
							.setTitle("提示")
							.setMessage("确定删除乘机人" + tList.get(position).get("name") + "?")
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
									new AsyncTask<Void, Void, String>(){

										
										@Override
										protected void onPreExecute() {
											// TODO Auto-generated method stub
											super.onPreExecute();
											progressDialog = ProgressDialogTripg.show(TCAMainActivity.this, null, null);
										}

										@Override
										protected void onPostExecute(String result) {
											// TODO Auto-generated method stub
											super.onPostExecute(result);
											progressDialog.dismiss();
											if(result.equals("1")){
												//new TList().execute();	
												tList.remove(position);
												tAdapter.notifyDataSetChanged();
											}
										}

										@Override
										protected String doInBackground(Void... params) {
											// TODO Auto-generated method stub
											String code = "";
											try {
												String data = tools.doPostData(tcaUrl, deleteTById(tList.get(position).get("id")));
												System.out.println(data);
												
												JSONObject job = new JSONObject(data);
												code = job.getString("Code");
												String text = job.getString("Message");
												Message msg = handler.obtainMessage();
												msg.what = 1;
												msg.obj = text;
												handler.sendMessage(msg);
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											return code;
										}
										
									}.execute();
								}
							})
							.setNegativeButton("取消", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							})
							.show();
							
						return true;
					}
				});
			}
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			tList = new ArrayList<HashMap<String,String>>();
			String code = "";
			Log.e("T list url", getTList());
			String data = tools.getURL(getTList());
			System.out.println("data ---> " + data);
			
			try {
				JSONObject job = new JSONObject(data);
				code = job.getString("Code");
				String text = job.getString("Message");//
				if(code.equals("1")){
					JSONArray jArray = job.getJSONArray("Result");
					if(jArray.length() == 0){
						Message msg = handler.obtainMessage();
						msg.what = 0;
						handler.sendMessage(msg);
					}else{
						for(int i=0,j=jArray.length();i<j;i++){
							JSONObject job1 = jArray.optJSONObject(i);
							HashMap<String, String> hashMap = new HashMap<String, String>();
							hashMap.put("name", job1.getString("PassengerName"));
							hashMap.put("Birthday", job1.getString("Birthday"));
							hashMap.put("sex", job1.getString("Sex"));
							hashMap.put("idNo", job1.getString("CertificateNumber"));
							hashMap.put("id", job1.getString("Id"));
							hashMap.put("idType", job1.getString("CertificateType"));
							
							tList.add(hashMap);
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return code;
		}
		
	}
	
	class CList extends AsyncTask<Void, Void, String>{

		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = ProgressDialogTripg.show(TCAMainActivity.this, null, null);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			if(result.equals("1")){
				cAdapter = new CListAdapter(TCAMainActivity.this, cList);
				lv.setAdapter(cAdapter);
				
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(TCAMainActivity.this, AddNewContacter.class);
						intent.putExtra("name", cList.get(position).get("name"));
						intent.putExtra("areaCode", cList.get(position).get("areaCode"));
						intent.putExtra("phone", cList.get(position).get("phone"));
						intent.putExtra("id", ""+cList.get(position).get("id"));
						intent.putExtra("eora", "e");
						startActivityForResult(intent, 4);
					}
				});
				
				lv.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, final int position, long id) {
						// TODO Auto-generated method stub
						
						new AlertDialog.Builder(TCAMainActivity.this)
						.setTitle("提示")
						.setMessage("确定删除联系人" + cList.get(position).get("name") + "?")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								new AsyncTask<Void, Void, String>(){

									
									@Override
									protected void onPreExecute() {
										// TODO Auto-generated method stub
										super.onPreExecute();
										progressDialog = ProgressDialogTripg.show(TCAMainActivity.this, null, null);
									}

									@Override
									protected void onPostExecute(String result) {
										// TODO Auto-generated method stub
										super.onPostExecute(result);
										progressDialog.dismiss();
										if(result.equals("1")){
											//new TList().execute();	
											cList.remove(position);
											cAdapter.notifyDataSetChanged();
										}
									}

									@Override
									protected String doInBackground(Void... params) {
										// TODO Auto-generated method stub
										String code = "";
										try {
											String data = tools.doPostData(tcaUrl, deleteCById(cList.get(position).get("id")));
											System.out.println(data);
											
											JSONObject job = new JSONObject(data);
											code = job.getString("Code");
											String text = job.getString("Message");
											Message msg = handler.obtainMessage();
											msg.what = 1;
											msg.obj = text;
											handler.sendMessage(msg);
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										return code;
									}
									
								}.execute();
							}
						})
						.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						})
						.show();
						
						return true;
					}
				});
			}
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			cList = new ArrayList<HashMap<String,String>>();
			String code = "";
			Log.e("C list url", getCList());
			String data = tools.getURL(getCList());
			System.out.println("data ---> " + data);
			
			try {
				JSONObject job = new JSONObject(data);
				code = job.getString("Code");
				String text = job.getString("Message");//
				if(code.equals("1")){
					JSONArray jArray = job.getJSONArray("Result");
					if(jArray.length() == 0){
						Message msg = handler.obtainMessage();
						msg.what = 2;
						handler.sendMessage(msg);
					}else{
						for(int i=0,j=jArray.length();i<j;i++){
							JSONObject job1 = jArray.optJSONObject(i);
							HashMap<String, String> hashMap = new HashMap<String, String>();
							hashMap.put("name", job1.getString("ContacterName"));
							hashMap.put("areaCode", job1.getString("ContacterArea"));
							hashMap.put("phone", job1.getString("ContacterMobile"));
							hashMap.put("id", job1.getString("Id"));
							
							cList.add(hashMap);
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return code;
		}
		
	}
	
	class AList extends AsyncTask<Void, Void, String>{

		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = ProgressDialogTripg.show(TCAMainActivity.this, null, null);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			if(result.equals("1")){
				aAdapter = new AListAdapter(TCAMainActivity.this, aList);
				lv.setAdapter(aAdapter);
				if(type.equals("a")){
					lv.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(TCAMainActivity.this, NewAddAddress.class);
							intent.putExtra("province", aList.get(position).get("province"));
							intent.putExtra("city", aList.get(position).get("city"));
							intent.putExtra("area", aList.get(position).get("area"));
							intent.putExtra("address", aList.get(position).get("address"));
							intent.putExtra("code", aList.get(position).get("code"));
							intent.putExtra("id", ""+aList.get(position).get("id"));
							intent.putExtra("eora", "e");
							startActivityForResult(intent, 6);
							
						}
					});
					
					lv.setOnItemLongClickListener(new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> parent,
								View view, final int position, long id) {
							// TODO Auto-generated method stub
							
							new AlertDialog.Builder(TCAMainActivity.this)
							.setTitle("提示")
							.setMessage("确定删除地址" + aList.get(position).get("address") + "?")
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
									new AsyncTask<Void, Void, String>(){

										
										@Override
										protected void onPreExecute() {
											// TODO Auto-generated method stub
											super.onPreExecute();
											progressDialog = ProgressDialogTripg.show(TCAMainActivity.this, null, null);
										}

										@Override
										protected void onPostExecute(String result) {
											// TODO Auto-generated method stub
											super.onPostExecute(result);
											progressDialog.dismiss();
											if(result.equals("1")){
												//new TList().execute();	
												aList.remove(position);
												aAdapter.notifyDataSetChanged();
											}
										}

										@Override
										protected String doInBackground(Void... params) {
											// TODO Auto-generated method stub
											String code = "";
											try {
												String data = tools.doPostData(tcaUrl, deleteAById(aList.get(position).get("id")));
												System.out.println(data);
												
												JSONObject job = new JSONObject(data);
												code = job.getString("Code");
												String text = job.getString("Message");
												Message msg = handler.obtainMessage();
												msg.what = 1;
												msg.obj = text;
												handler.sendMessage(msg);
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											return code;
										}
										
									}.execute();
								}
							})
							.setNegativeButton("取消", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							})
							.show();
							
							return true;
						}
					});
				}else if(type.equals("ff")){
					lv.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							Log.e("address", aList.get(position).get("address"));
							Log.e("code", aList.get(position).get("code"));
							Intent intent = getIntent();
							intent.putExtra("address", aList.get(position).get("address"));
							intent.putExtra("code", aList.get(position).get("code"));
							setResult(ResultCode.SUCCESS, intent);
							finish();
							overridePendingTransition(android.R.anim.fade_in,
									android.R.anim.fade_out);
						}
					});
				}

			}
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			aList = new ArrayList<HashMap<String,String>>();
			String code = "";
			Log.e("A list url", getAList());
			String data = tools.getURL(getAList());
			System.out.println("data ---> " + data);
			
			try {
				JSONObject job = new JSONObject(data);
				code = job.getString("Code");
				if(code.equals("1")){
					JSONArray jArray = job.getJSONArray("Result");
					if(jArray.length() == 0){
						Message msg = handler.obtainMessage();
						msg.what = 3;
						handler.sendMessage(msg);
					}else{
						for(int i=0,j=jArray.length();i<j;i++){
							JSONObject job1 = jArray.optJSONObject(i);
							HashMap<String, String> hashMap = new HashMap<String, String>();
							hashMap.put("province", job1.getString("Provinces"));
							hashMap.put("city", job1.getString("City"));
							hashMap.put("area", job1.getString("County"));
							hashMap.put("address", job1.getString("DetailsAddress"));
							hashMap.put("code", job1.getString("ZipCode"));
							hashMap.put("id", job1.getString("Id"));
							
							aList.add(hashMap);
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return code;
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if(resultCode == ResultCode.SUCCESS){
				new TList().execute();				
			}else{
				Toast.makeText(TCAMainActivity.this, "添加常用旅客失败!", Toast.LENGTH_LONG).show();
			}
			break;
		case 2:
			if(resultCode == ResultCode.SUCCESS){
				new TList().execute();				
			}else{
				Toast.makeText(TCAMainActivity.this, "编辑常用旅客失败!", Toast.LENGTH_LONG).show();
			}
			break;
		case 3:
			if(resultCode == ResultCode.SUCCESS){
				new CList().execute();				
			}else{
				Toast.makeText(TCAMainActivity.this, "添加常用联系人失败!", Toast.LENGTH_LONG).show();
			}
			break;
		case 4:
			if(resultCode == ResultCode.SUCCESS){
				new CList().execute();				
			}else{
				Toast.makeText(TCAMainActivity.this, "编辑常用联系人失败!", Toast.LENGTH_LONG).show();
			}
			break;
		case 5:
			if(resultCode == ResultCode.SUCCESS){
				new AList().execute();				
			}else{
				Toast.makeText(TCAMainActivity.this, "添加常用地址失败!", Toast.LENGTH_LONG).show();
			}
			break;
		case 6:
			if(resultCode == ResultCode.SUCCESS){
				new AList().execute();				
			}else{
				Toast.makeText(TCAMainActivity.this, "编辑常用地址失败!", Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
		}
	}
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Toast.makeText(TCAMainActivity.this, "暂无常用旅客,请添加!", Toast.LENGTH_LONG).show();
				break;
			case 1:
				String text = (String) msg.obj;
				Toast.makeText(TCAMainActivity.this, text, Toast.LENGTH_LONG).show();
				break;
			case 2:
				Toast.makeText(TCAMainActivity.this, "暂无常用联系人,请添加!", Toast.LENGTH_LONG).show();
				break;
			case 3:
				Toast.makeText(TCAMainActivity.this, "暂无常用地址,请添加!", Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		}
		
		
	};
	
	
	private String getTList(){
		return (tcaUrl + "?a=get_passengers&u=" + tools.getUserName(getApplicationContext()));
	}
	private String getCList(){
		return (tcaUrl + "?a=get_contacters&u=" + tools.getUserName(getApplicationContext()));
	}
	private String getAList(){
		return (tcaUrl + "?a=get_address&u=" + tools.getUserName(getApplicationContext()));
	}
	private String deleteTById(String id){
		StringBuilder sb = new StringBuilder();
		sb.append("a=del_passenger_id");
		sb.append("&u=");
		sb.append(tools.getUserName(getApplicationContext()));
		sb.append("&id=");
		sb.append(""+id);
		return sb.toString();
	}
	private String deleteCById(String id){
		StringBuilder sb = new StringBuilder();
		sb.append("a=del_contacter_id");
		sb.append("&u=");
		sb.append(tools.getUserName(getApplicationContext()));
		sb.append("&id=");
		sb.append(id);
		return sb.toString();
	}
	private String deleteAById(String id){
		StringBuilder sb = new StringBuilder();
		sb.append("a=del_addr_id");
		sb.append("&u=");
		sb.append(tools.getUserName(getApplicationContext()));
		sb.append("&id=");
		sb.append(id);
		return sb.toString();
	}
	
	

}
