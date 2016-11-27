package cn.tripg.activity.flight;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.internet.Exit;
import cn.internet.Tools;
import cn.tripg.R;
import cn.tripg.widgit.ProgressDialogTripg;
import cn.vip.main.AddNewContacter;
import cn.vip.main.CListAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ChooseContacter extends Activity {

	private ImageView back;
	private TextView add;
	private TextView _import;
	private ListView lv;

	private ProgressDialog progressDialog;
	private ArrayList<HashMap<String, String>> cList;
	private CListAdapter cAdapter;
	private String phoneNo;

	private void prepareView() {
		back = (ImageView) findViewById(R.id.cc_back);
		add = (TextView) findViewById(R.id.cc_add);
		_import = (TextView) findViewById(R.id.cc_import);
		lv = (ListView) findViewById(R.id.cc_lv);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_contacter);
		Exit.getInstance().addActivity(this);
		prepareView();

		new CList().execute();

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = getIntent();
				setResult(ResultCode.FAILURE, intent);
				finish();
			}
		});

		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ChooseContacter.this,
						AddNewContacter.class);
				intent.putExtra("name", "");
				intent.putExtra("areaCode", "");
				intent.putExtra("phone", "");
				intent.putExtra("eora", "a");
				intent.putExtra("id", "");
				startActivityForResult(intent, 3);
			}
		});

		_import.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ListView listView = new ListView(ChooseContacter.this);
				PersonList list2 = new PersonList(ChooseContacter.this,
						getPerson());
				listView.setAdapter(list2);
				setContentView(listView);

				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							final int position, long id) {
						// TODO Auto-generated method stub
						Log.e("name", getPerson().get(position).getName());
						if (getPerson().get(position).getPhone().toString()
								.contains(",")) {
							String[] p = getPerson().get(position).getPhone()
									.toString().split(",");
							if (p[0].toString().contains("-")) {
								String[] _p = p[0].split("-");
								phoneNo = _p[0].toString().substring(1) + _p[1]
										+ _p[2];
							} else {
								phoneNo = p[0].toString().substring(1);
							}
							Log.e("p 0", p[0].toString().substring(1));
						} else {
							if (getPerson().get(position).getPhone().toString()
									.contains("-")) {
								String[] _p = getPerson().get(position)
										.getPhone().toString().split("-");
								phoneNo = _p[0].toString().substring(1)
										+ _p[1]
										+ _p[2].toString().substring(0,
												_p[2].toString().length() - 1);
							} else {
								phoneNo = getPerson()
										.get(position)
										.getPhone()
										.toString()
										.substring(
												1,
												getPerson().get(position)
														.getPhone().toString()
														.length() - 1);
							}
							Log.e("phone", ""
									+ getPerson().get(position).getPhone());
						}
						Log.e("phone1", ""
								+ getPerson().get(position).getPhone());

						new AsyncTask<Void, Void, String>() {

							@Override
							protected void onPreExecute() {
								// TODO Auto-generated method stub
								super.onPreExecute();
								progressDialog = ProgressDialogTripg.show(
										ChooseContacter.this, null, null);
							}

							@Override
							protected void onPostExecute(String result) {
								// TODO Auto-generated method stub
								super.onPostExecute(result);
								progressDialog.dismiss();
								if (result.equals("1")) {
									Intent intent = getIntent();
									Bundle bundle = new Bundle();
									bundle.putString("name",
											getPerson().get(position).getName());
									bundle.putString("phone", "" + phoneNo);

									intent.putExtras(bundle);
									setResult(ResultCode.SUCCESS, intent);
									finish();
									overridePendingTransition(
											android.R.anim.fade_in,
											android.R.anim.fade_out);
								} else {
									Intent intent = getIntent();
									setResult(ResultCode.FAILURE, intent);
									finish();
								}
							}

							@Override
							protected String doInBackground(Void... params) {//
								// TODO Auto-generated method stub
								String code = "";
								try {
									String data = new Tools()
											.doPostData(
													"http://www.tripg.cn/phone_api/contact_information/api.php",
													postAData(
															getPerson().get(
																	position)
																	.getName(),
															phoneNo));
									System.out.println(data);

									JSONObject job = new JSONObject(data);
									code = job.getString("Code");
									String text = job.getString("Message");
									Message msg = handler.obtainMessage();
									msg.what = 1;
									msg.obj = text;
									handler.sendMessage(msg);

								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								;

								return code;
							}

						}.execute();

					}
				});
			}
		});

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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 3:
			if (resultCode == ResultCode.SUCCESS) {
				new CList().execute();
			} else {
				Toast.makeText(ChooseContacter.this, "添加常用联系人失败!",
						Toast.LENGTH_LONG).show();
			}
			break;

		default:
			break;
		}
	}

	class CList extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = ProgressDialogTripg.show(ChooseContacter.this,
					null, null);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (result.equals("1")) {
				cAdapter = new CListAdapter(ChooseContacter.this, cList);
				lv.setAdapter(cAdapter);

				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						Intent intent = getIntent();
						Bundle bundle = new Bundle();
						bundle.putString("name", cList.get(position)
								.get("name").toString());
						bundle.putString("phone",
								cList.get(position).get("phone").toString());

						intent.putExtras(bundle);
						setResult(ResultCode.SUCCESS, intent);
						finish();
						overridePendingTransition(android.R.anim.fade_in,
								android.R.anim.fade_out);
					}
				});

			}
		}

		@Override
		protected String doInBackground(Void... params) {//
			// TODO Auto-generated method stub
			cList = new ArrayList<HashMap<String, String>>();
			String code = "";
			Log.e("C list url", getCList());
			String data = new Tools().getURL(getCList());
			System.out.println("data ---> " + data);

			try {
				JSONObject job = new JSONObject(data);
				code = job.getString("Code");
				String text = job.getString("Message");//
				if (code.equals("1")) {
					JSONArray jArray = job.getJSONArray("Result");
					if (jArray.length() == 0) {
						Message msg = handler.obtainMessage();
						msg.what = 0;
						handler.sendMessage(msg);
					} else {
						for (int i = 0, j = jArray.length(); i < j; i++) {
							JSONObject job1 = jArray.optJSONObject(i);
							HashMap<String, String> hashMap = new HashMap<String, String>();
							hashMap.put("name", job1.getString("ContacterName"));
							hashMap.put("areaCode",
									job1.getString("ContacterArea"));
							hashMap.put("phone",
									job1.getString("ContacterMobile"));
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

	@SuppressLint("NewApi")
	public List<Person> getPerson() {
		Uri uri = ContactsContract.Data.CONTENT_URI;
		Cursor cursor = getContentResolver().query(uri, null, null, null,
				"display_name");
		cursor.moveToFirst();
		List<Person> list = new ArrayList<Person>();

		int Index_CONTACT_ID = cursor
				.getColumnIndex(ContactsContract.Data.CONTACT_ID);
		int Index_DATA1 = cursor.getColumnIndex(ContactsContract.Data.DATA1);
		int Index_MIMETYPE = cursor
				.getColumnIndex(ContactsContract.Data.MIMETYPE);

		while (cursor.getCount() > cursor.getPosition()) {
			Person person = null;
			String id = cursor.getString(Index_CONTACT_ID);
			String info = cursor.getString(Index_DATA1);
			String mimeType = cursor.getString(Index_MIMETYPE);

			for (int n = 0; n < list.size(); n++) {
				if (list.get(n).getID() != null) {
					if (list.get(n).getID().equals(id)) {
						person = list.get(n);
						break;
					}
				}
			}

			if (person == null) {
				person = new Person();
				person.setID(id);
				list.add(person);
			}
			if (mimeType.equals("vnd.android.cursor.item/email_v2")) {// 该行数据为邮箱

				person.setEmail(info);
			} else if (mimeType
					.equals("vnd.android.cursor.item/postal-address_v2")) {// 该行数据为地址

				person.setAddress(info);
			} else if (mimeType.equals("vnd.android.cursor.item/phone_v2")) {// 该行数据为电话号码

				person.addPhone(info);
			} else if (mimeType.equals("vnd.android.cursor.item/name")) {// 该行数据为名字

				person.setName(info);
			}
			cursor.moveToNext();
		}
		return list;
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Toast.makeText(ChooseContacter.this, "暂无常用联系人,请添加!",
						Toast.LENGTH_LONG).show();
				break;
			case 1:
				String text = (String) msg.obj;
				Toast.makeText(ChooseContacter.this, text, Toast.LENGTH_LONG)
						.show();
				break;
			default:
				break;
			}
		}

	};

	private String getCList() {
		return ("http://www.tripg.cn/phone_api/contact_information/api.php"
				+ "?a=get_contacters&u=" + new Tools()
				.getUserName(getApplicationContext()));
	}

	private String postAData(String name, String phone) {
		StringBuilder sb = new StringBuilder();
		sb.append("a=add_contacter&u=");
		sb.append(new Tools().getUserName(getApplicationContext()));
		sb.append("&ContacterName=");
		sb.append(name);
		sb.append("&ContacterArea=");
		sb.append("");
		sb.append("&ContacterMobile=");
		sb.append(phone);

		return sb.toString();
	}
}
