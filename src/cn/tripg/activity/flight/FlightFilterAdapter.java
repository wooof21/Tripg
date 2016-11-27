package cn.tripg.activity.flight;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;

import android.content.Context;
import android.graphics.Picture;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.tripg.R;

public class FlightFilterAdapter extends BaseAdapter {

	private LayoutInflater mInflater = null;
	private ArrayList<String> flightList;
	@SuppressWarnings("unused")
	private Context context;

	public FlightFilterAdapter(Context myContex, HashSet<String> set) {
		this.mInflater = LayoutInflater.from(myContex);
		this.context = myContex;
		this.flightList = new ArrayList<String>();
		for (String s : set)
			this.flightList.add(s);
	}

	public int getCount() {
		return flightList.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.flight_filter_item, null);
		}
		TextView item = (TextView) convertView
				.findViewById(R.id.filtered_company_name);
		item.setText(this.flightList.get(position));
		ImageView pic = (ImageView) convertView
				.findViewById(R.id.filtered_company_pic);
		String id = getCompanyCode(item.getText().toString());
		setImage(pic, id);
		return convertView;
	}

	private void setImage(ImageView pic, String id) {
		if (id.equalsIgnoreCase("3u")) {
			pic.setImageResource(R.drawable.u3);
		} else if (id.equalsIgnoreCase("8l")) {
			pic.setImageResource(R.drawable.l8);
		} else if (id.equalsIgnoreCase("bk")) {
			pic.setImageResource(R.drawable.bk);
		} else if (id.equalsIgnoreCase("ca")) {
			pic.setImageResource(R.drawable.ca);
		} else if (id.equalsIgnoreCase("cz")) {
			pic.setImageResource(R.drawable.cz);
		} else if (id.equalsIgnoreCase("eu")) {
			pic.setImageResource(R.drawable.eu);
		} else if (id.equalsIgnoreCase("fm")) {
			pic.setImageResource(R.drawable.fm);
		} else if (id.equalsIgnoreCase("g5")) {
			pic.setImageResource(R.drawable.g5);
		} else if (id.equalsIgnoreCase("gs")) {
			pic.setImageResource(R.drawable.gs);
		} else if (id.equalsIgnoreCase("ho")) {
			pic.setImageResource(R.drawable.ho);
		} else if (id.equalsIgnoreCase("hu")) {
			pic.setImageResource(R.drawable.hu);
		} else if (id.equalsIgnoreCase("hx")) {
			pic.setImageResource(R.drawable.hx);
		} else if (id.equalsIgnoreCase("jd")) {
			pic.setImageResource(R.drawable.jd);
		} else if (id.equalsIgnoreCase("jr")) {
			pic.setImageResource(R.drawable.jr);
		} else if (id.equalsIgnoreCase("kn")) {
			pic.setImageResource(R.drawable.kn);
		} else if (id.equalsIgnoreCase("ky")) {
			pic.setImageResource(R.drawable.ky);
		} else if (id.equalsIgnoreCase("mf")) {
			pic.setImageResource(R.drawable.mf);
		} else if (id.equalsIgnoreCase("mu")) {
			pic.setImageResource(R.drawable.mu);
		} else if (id.equalsIgnoreCase("ns")) {
			pic.setImageResource(R.drawable.ns);
		} else if (id.equalsIgnoreCase("pn")) {
			pic.setImageResource(R.drawable.pn);
		} else if (id.equalsIgnoreCase("sc")) {
			pic.setImageResource(R.drawable.sc);
		} else if (id.equalsIgnoreCase("vd")) {
			pic.setImageResource(R.drawable.vd);
		} else if (id.equalsIgnoreCase("zh")) {
			pic.setImageResource(R.drawable.zh);
		} else if (id.equalsIgnoreCase("aq")) {
			pic.setImageResource(R.drawable.aq);
		} else if (id.equalsIgnoreCase("8c")) {
			pic.setImageResource(R.drawable.c8);
		} else if (id.equalsIgnoreCase("9c")) {
			pic.setImageResource(R.drawable.c9);
		} else if (id.equalsIgnoreCase("dr")) {
			pic.setImageResource(R.drawable.dr);
		} else if (id.equalsIgnoreCase("dz")) {
			pic.setImageResource(R.drawable.dz);
		} else if (id.equalsIgnoreCase("fu")) {
			pic.setImageResource(R.drawable.fu);
		} else if (id.equalsIgnoreCase("gj")) {
			pic.setImageResource(R.drawable.gj);
		} else if (id.equalsIgnoreCase("gx")) {
			pic.setImageResource(R.drawable.gx);
		} else if (id.equalsIgnoreCase("qw")) {
			pic.setImageResource(R.drawable.qw);
		} else if (id.equalsIgnoreCase("tv")) {
			pic.setImageResource(R.drawable.tv);
		} else if (id.equalsIgnoreCase("uq")) {
			pic.setImageResource(R.drawable.uq);
		} else if (id.equalsIgnoreCase("yi")) {
			pic.setImageResource(R.drawable.yi);
		} else if (id.equalsIgnoreCase("cn")) {
			pic.setImageResource(R.drawable.cn);
		} else {
			pic.setImageResource(R.drawable.all);
		}

	}

	private String getCompanyCode(String name) {
		Properties proCompany = new Properties();
		InputStream isCompany = context.getResources().openRawResource(
				R.raw.company);
		String codeCompany = "";
		try {
			proCompany.load(isCompany);
			codeCompany = (String) proCompany.get(name);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			Log.e("zzz", "no such air company in dictionary");
		}
		try {
			isCompany.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		if ("null".equals(codeCompany) || null == codeCompany)
			codeCompany = "";
		return codeCompany;
	}
}
