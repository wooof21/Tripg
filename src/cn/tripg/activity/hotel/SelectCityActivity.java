package cn.tripg.activity.hotel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tools.sortlistview.CharacterParser;
import tools.sortlistview.ClearEditText;
import tools.sortlistview.PinyinComparator;
import tools.sortlistview.SideBar;
import tools.sortlistview.SideBar.OnTouchingLetterChangedListener;
import tools.sortlistview.SortAdapter;
import tools.sortlistview.SortModel;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.internet.Exit;
import cn.tripg.R;
import cn.tripg.activity.flight.ResultCode;

public class SelectCityActivity extends Activity {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	
	/**
	 * ����ת����ƴ������
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	
	/**
	 * ����ƴ��������ListView�����������
	 */
	private PinyinComparator pinyinComparator;
	//Listen back key , or it will crush.
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Exit.getInstance().addActivity(this);
		getWindow().setSoftInputMode(  
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_city_select);
		initViews();
		ImageView backButton = (ImageView)findViewById
				(R.id.title_back_city);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = getIntent();
	        	setResult(ResultCode.FAILURE, intent);
	        	finish();
			}
		});
	}

	protected void initViews() {
		//ʵ��������תƴ����
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();
		
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		
		//�����Ҳഥ������
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			
			@Override
			public void onTouchingLetterChanged(String s) {
				//����ĸ�״γ��ֵ�λ��
				int position = adapter.getPositionForSection(s.charAt(0));
				if(position != -1){
					sortListView.setSelection(position);
				}
				
			}
		});
		
		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {
/////////////////////////////////////////////////////////////////////////////////////////////////
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//����Ҫ����adapter.getItem(position)����ȡ��ǰposition����Ӧ�Ķ���
				//Toast.makeText(getApplication(), ((SortModel)adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
			Intent intent = getIntent();
			intent.putExtra("cityName",((SortModel)adapter.getItem(position)).getName());
			setResult(ResultCode.SUCCESS, intent);
			finish();
			}
		});
		//��ʼ����������  ����Դ��citymap ��ʵ��
//		SourceDateList = filledData(getResources().getStringArray(R.array.citys));
		SourceDateList = filledData(CityMapping.cityMap.keySet().toArray(new String[0]));
		
		// ����a-z��������Դ����
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(this, SourceDateList);
		sortListView.setAdapter(adapter);
		
		
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		
		//�������������ֵ�ĸı�����������
		mClearEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//������������ֵΪ�գ�����Ϊԭ�����б�����Ϊ���������б�
				filterData(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}


	/**
	 * ΪListView�������
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(String [] date){
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for(int i=0; i<date.length; i++){
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			//����ת����ƴ��
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
			if(sortString.matches("[A-Z]")){
				sortModel.setSortLetters(sortString.toUpperCase());
			}else{
				sortModel.setSortLetters("#");
			}
			
			mSortList.add(sortModel);
		}
		return mSortList;
		
	}
	
	/**
	 * ����������е�ֵ���������ݲ�����ListView
	 * @param filterStr
	 */
	private void filterData(String filterStr){
		List<SortModel> filterDateList = new ArrayList<SortModel>();
		
		if(TextUtils.isEmpty(filterStr)){
			filterDateList = SourceDateList;
		}else{
			filterDateList.clear();
			for(SortModel sortModel : SourceDateList){
				String name = sortModel.getName();
				if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
					filterDateList.add(sortModel);
				}
			}
		}
		
		// ����a-z��������
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}
	
}
