package cn.tripg.activity.login;

import model.user.LoginResponse;

import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import tools.des.Api;
import tools.json.JsonUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.internet.Exit;
import cn.tripg.R;
import cn.tripg.activity.flight.ResultCode;

public class LoginActivity extends Activity{

    private TextView forgetPassWordText;
    private ImageView loginButton;
    private ImageView registerButton;
    private EditText userName;
    private EditText passWord;
    private AlertDialog myDialog = null;
    private Api api;
    private ProgressDialog pd;
    private LoginResponse lr;
    private SharedPreferences sharedPre;
    private Editor editor;
    private String useridString;
    
    private void prepareAllView(){
        api = new Api();
        forgetPassWordText = (TextView)findViewById(R.id.forget_pswd);
        loginButton = (ImageView)findViewById(R.id.btn_login);
        registerButton = (ImageView)findViewById(R.id.btn_register);
        userName = (EditText)findViewById(R.id.user_name);
        passWord = (EditText)findViewById(R.id.password);
        
    }
    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //pd.dismiss();// 关闭ProgressDialog
        }
    };
    public LoginResponse parseJsonData(String jsonStr){
        if(jsonStr == null){
            Toast.makeText(this, "获取数据失败", Toast.LENGTH_LONG).show();
            //finish();//???
        }
        try {
            if(jsonStr.equals("error")){
                Toast.makeText(this, "网络异常", Toast.LENGTH_LONG).show();
                //finish();//???
            }else{
//            TypeReference<LoginResponse> tvo = new TypeReference<LoginResponse>() {};
//            lr = JsonUtils.json2GenericObject(jsonStr,tvo);
            try {
            	lr = new LoginResponse();
				JSONObject jsonObject = new JSONObject(jsonStr);
				lr.Code = (String)jsonObject.getString("Code").toString();
            	lr.Message = (String)jsonObject.getString("Message").toString();
				
            	JSONObject resJsonObject = jsonObject.getJSONObject("Result");
            	useridString = (String)resJsonObject.getString("Id");
				lr.compayidString = (String)resJsonObject.getString("CompanyId").toString();
				lr.countryString = (String)resJsonObject.getString("Country").toString();
				
				//Log.e("lr.countryString", ""+lr.countryString);
				
			} catch (Exception e) {
				// TODO: handle exception
				Log.e("Exception----", ""+e);
			}

            if ("0".equals(lr.getCode())) {
                Toast.makeText(this, "登录失败", Toast.LENGTH_LONG).show();
                //finish();//???
            }
        }
        } catch (Exception e) {
            Toast.makeText(this, "登录失败", Toast.LENGTH_LONG).show();
            //finish();//???
        }
        return lr;
    }
    private void saveLoginInfo(Context context, LoginResponse lr, String pswd){
        sharedPre =
                context.getSharedPreferences("config", Context.MODE_PRIVATE);
        editor = sharedPre.edit();
        editor.putString("username", (String)userName.getText().toString());
        editor.putString("password", (String)passWord.getText().toString());
        editor.putString("Result", useridString);
        editor.putString("memberid", useridString);
        editor.putString("compayid", lr.compayidString);
        editor.commit();
    }
    private void clearLoginInfo(){
    	if(editor != null){
    		editor.clear();  
            editor.commit();
    	}
    }
    private void getLoginInfo(){
        SharedPreferences sharedPre = getSharedPreferences("config", MODE_PRIVATE);
        String username1 = sharedPre.getString("username", "null");
        String password1 = sharedPre.getString("password", "null");
        //Log.e("username", username1);
        //Log.e("username", password1);
    }
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		Exit.getInstance().addActivity(this);
		prepareAllView();
		
		
		ImageView backImageView = (ImageView)findViewById(R.id.title_back);
		backImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = getIntent();
				setResult(ResultCode.FAILURE, intent);
				finish();
			}
		});
		
		
		
		//forget password text action
		forgetPassWordText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                notifyPswdFindBackWay();
                return false;
            }
        });
	    // login button action
		loginButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
            	if(getInternet() == false){
            		Toast.makeText(LoginActivity.this, "网络链接已断开", Toast.LENGTH_LONG).show();
            	}else{
            		//clear last login info. if exist.
                	clearLoginInfo();
                    String username = userName.getText().toString();
                    String password = passWord.getText().toString();
                    if(username == null || "".equals(username)){
                    	Toast.makeText(LoginActivity.this, "请填写用户名", Toast.LENGTH_SHORT).show();
                    	return;
                    }
                    if(password == null || "".equals(password)){
                    	Toast.makeText(LoginActivity.this, "请填写密码", Toast.LENGTH_SHORT).show();
                    	return;
                    }
                    //username = "HDKJCO004";
                    //password = "HDKJCO004";
                    String tokenString = "IEEW9lg9hHa1qMC2LrAgHuluilAAX/q0";
                    final String loginURL = "http://mapi.tripglobal.cn/MemApi.aspx?action=LoginForMobile"
                    + "&username=" + username
                    + "&password=" + password
                    + "&token="+ tokenString;
                    Log.e("", loginURL);
                    final String result = api.doGetData(loginURL);
                    
                    lr = parseJsonData(result);
                    saveLoginInfo(LoginActivity.this, lr, password);
//                    Log.e("result", ""+lr);
//                    Log.e("code", lr.getCode());
                    if(lr == null){
                        Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    }
                    else{
                    	if("1".equals(lr.getCode())){
                    		Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    		saveLoginInfo(LoginActivity.this, lr, password);
                    		finish();
                    	}else{
                    		Toast.makeText(LoginActivity.this, lr.getMessage(), Toast.LENGTH_SHORT).show();
                    	}
                    }
            	}
            }
        });

		// register button action
		registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //start register activity.
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
	}
	private void notifyPswdFindBackWay(){
	    myDialog = new AlertDialog.Builder(LoginActivity.this)
	    .setTitle("密码找回方式").setItems(R.array.pswd_find_back_way,
	            new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which  == 0){//go to ForgetPassWordActivity
                    Intent intent = new Intent(LoginActivity.this,
                            ForgetPassWordActivity.class);
                    startActivity(intent);
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
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
