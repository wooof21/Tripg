package cn.tripg.activity.flight;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.internet.Exit;
import cn.tripg.R;

public class FillBoardManActivity extends Activity{

	private FrameLayout idTypeField;
	//姓名
	private EditText userName;
	//证件类型
	private TextView typeText;
	//证件号码
	private EditText typeNum;
	//确定
	private ImageView commit;
	private String userNameStr;
	private String typeTextStr;
	private String typeNumStr;
	
    private void prepareAllView(){
    	userName = (EditText)findViewById(R.id.username);
    	typeText = (TextView)findViewById(R.id.type_text);
    	typeTextStr = typeText.getText().toString();
    	typeNum = (EditText)findViewById(R.id.id_num);
    	idTypeField = (FrameLayout)findViewById(R.id.id_type_field);
    	commit = (ImageView)findViewById(R.id.ok_commit);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fill_board_man);
        Exit.getInstance().addActivity(this);
        prepareAllView();
        
        //title_order_back
        ImageView imageViewback = (ImageView) findViewById(R.id.title_order_back);
		imageViewback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent intent = getIntent();
				Bundle bundle = new Bundle();
				bundle.putString("userName", "0");
				bundle.putString("idType", "0");
				bundle.putString("idNum", "0");

				intent.putExtras(bundle);
	            setResult(ResultCode.SUCCESS, intent);
	            finish();
	            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			}
		});

        idTypeField.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(FillBoardManActivity.this);
				final AlertDialog al = builder.create();
				LayoutInflater inflater = (LayoutInflater)
						getSystemService(LAYOUT_INFLATER_SERVICE);
				final LinearLayout rl = (LinearLayout)inflater
						.inflate(R.layout.dialog_id_type, null);

				LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				ListView types = (ListView)rl.findViewById(R.id.id_type_list);

				ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
						(FillBoardManActivity.this, R.array.id_types, R.layout.item_id_type);
				types.setAdapter(adapter);

				al.show();
				al.setContentView(rl, llp);
				al.setCanceledOnTouchOutside(true);
				types.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						TextView tv = (TextView)view;
						typeText.setText(tv.getText());
						typeTextStr = tv.getText().toString();
						al.dismiss();
					}
				});
			}
		});
        
        commit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = getIntent();
				Bundle bundle = new Bundle();
				userNameStr = userName.getText().toString();
				bundle.putString("userName", userNameStr);

				bundle.putString("idType", typeTextStr);
				typeNumStr = typeNum.getText().toString();
				bundle.putString("idNum", typeNumStr);

				intent.putExtras(bundle);
	            setResult(ResultCode.SUCCESS, intent);
	            finish();
	            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				
			}
		});
    }
}
