package cn.tripg.activity.flight;

import java.util.List;

import cn.tripg.R;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PersonList extends BaseAdapter{
	private List<Person> list;
	private Context context;
	public PersonList(Context context,List<Person> list){
		this.context=context;
		this.list=list;
	}
	public int getCount(){//implements android.widget.Adapter.getCount 
	
		return list.size();
	}

	public Object getItem(int position){//implements android.widget.Adapter.getItem
	
		return position;
	}

	public long getItemId(int position){//implements android.widget.Adapter.getItemId
	
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent){//implements android.widget.Adapter.getView
	//Get a View that displays the data at the specified position in the data set. 
		LayoutInflater inflater=LayoutInflater.from(context);
		if(convertView == null){
			convertView = inflater.inflate(R.layout.contacter_item, null);
		}
		Person person=list.get(position);
		TextView textName=(TextView) convertView.findViewById(R.id.ci_name);
		textName.setText(person.getName());
		TextView textPhone=(TextView) convertView.findViewById(R.id.ci_phone);
		for(String phone:person.getPhone()){
			textPhone.setText(phone);
    	}
		return convertView;
	}
}
