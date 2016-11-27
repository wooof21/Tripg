package cn.tripg.activity.flight;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.internet.Exit;
import cn.internet.Tools;
import cn.tripg.R;
import cn.tripg.widgit.ProgressDialogTripg;
import cn.vip.main.AddNewTraveler;
import cn.vip.main.TCAMainActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChoosePassengers extends Activity {

	private ImageView back;
	private TextView add;
	private ListView lv;
	private TextView pCount;
	private ImageView finish;

	private ArrayList<HashMap<String, String>> pList;
	private CPAdapter cpAdapter;
	private ProgressDialog progressDialog;

	private void prepareView() {
		back = (ImageView) findViewById(R.id.cp_back);
		add = (TextView) findViewById(R.id.cp_add);
		lv = (ListView) findViewById(R.id.cp_lv);
		pCount = (TextView) findViewById(R.id.cp_pcount_tv);
		finish = (ImageView) findViewById(R.id.cp_finish);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_passenger);
		Exit.getInstance().addActivity(this);
		prepareView();

		new pList().execute();
		
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
				Intent intent = new Intent(ChoosePassengers.this, AddNewTraveler.class);
				intent.putExtra("name", "");
				intent.putExtra("idType", "");
				intent.putExtra("idNo", "");
				intent.putExtra("eora", "a");
				intent.putExtra("id", "");
				startActivityForResult(intent, 1);
			}
		});

	}

	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if(resultCode == ResultCode.SUCCESS){
				new pList().execute();				
			}else{
				Toast.makeText(ChoosePassengers.this, "添加常用旅客失败!", Toast.LENGTH_LONG).show();
			}
			break;

		default:
			break;
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

	class pList extends AsyncTask<Void, Void, String>{

		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = ProgressDialogTripg.show(ChoosePassengers.this, null, null);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			if(result.equals("1")){
				cpAdapter = new CPAdapter(ChoosePassengers.this, pList);
				lv.setAdapter(cpAdapter);
				
				
				
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						Intent intent = getIntent();
						Bundle bundle = new Bundle();
						bundle.putString("userName", pList.get(position).get("name").toString());
						bundle.putString("idType", pList.get(position).get("idType").toString());
						bundle.putString("idNum", pList.get(position).get("idNo").toString());

						intent.putExtras(bundle);
			            setResult(ResultCode.SUCCESS, intent);
			            finish();
			            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
					}
				});
			}
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			pList = new ArrayList<HashMap<String,String>>();
			String code = "";
			Log.e("T list url", getTList());
			String data = new Tools().getURL(getTList());
			System.out.println(data);
			
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
							hashMap.put("idNo", job1.getString("CertificateNumber"));
							hashMap.put("id", job1.getString("Id"));
							hashMap.put("idType", job1.getString("CertificateType"));
							
							pList.add(hashMap);
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return code;
		}
	};
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Toast.makeText(ChoosePassengers.this, "暂无常用旅客,请添加!", Toast.LENGTH_LONG).show();
				break;

			default:
				break;
			}
		}
		
		
	};
	
	
	
	
	private String getTList() {
		return ("http://www.tripg.cn/phone_api/contact_information/api.php"
				+ "?a=get_passengers&u=" + new Tools()
				.getUserName(getApplicationContext()));
	}

	
	
	class CPAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<HashMap<String, String>> list;
		private LayoutInflater inflater;

		public CPAdapter(Context context,
				ArrayList<HashMap<String, String>> list) {
			super();
			this.context = context;
			this.list = list;

			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.cp_item, null);
			}

			TextView name = (TextView) convertView.findViewById(R.id.cpi_name);
			TextView id = (TextView) convertView.findViewById(R.id.cpi_id);

			HashMap<String, String> hashMap = list.get(position);
			name.setText(hashMap.get("name"));
			id.setText(hashMap.get("idNo"));
			
			
			return convertView;
		}

	}

}
