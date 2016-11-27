package cn.tripg.activity.flight;

import java.util.ArrayList;

import model.flight.CabinVo;
import model.flight.FlightsVo;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.internet.Exit;
import cn.tripg.R;

public class FlightQueryDetailActivity extends Activity{

	private ArrayList<CabinVo> cabin;
	private FlightsVo vo;
	private FlightsVo depVo;
	private String index;
	private ImageView backButton;
	public String getIndex() {
        return index;
    }
    public void setIndex(String index) {
        this.index = index;
    }
    public FlightsVo getDepVo() {
        return depVo;
    }
    public FlightsVo getVo() {
        return vo;
    }
    public String depCityStr;
	public String getDepCityStr() {
        return depCityStr;
    }
    public String arrCityStr;

    public String getArrCityStr() {
        return arrCityStr;
    }

	private String type;
	private String flightDateBack;

	public String getFlightDateBack() {
        return flightDateBack;
    }

    private FlightDetailAdapter flightDetailAdapter;
	private ListView listview;
	public Handler handler;

	public void setTitle(String dep, String arr){
		TextView title = (TextView)findViewById(R.id.title_text_detail);
		title.setText(dep + ">" + arr);
	}
	@SuppressWarnings("unchecked")
	public void prepareParams(){
		cabin = (ArrayList<CabinVo>) getIntent().getSerializableExtra("list");
		vo = (FlightsVo)getIntent().getSerializableExtra("FlightsVo");
		depVo = (FlightsVo)getIntent().getSerializableExtra("depVo");
		index = getIntent().getExtras().getString("index");
		depCityStr = getIntent().getExtras().getString("depCityStr");
		arrCityStr = getIntent().getExtras().getString("arrCityStr");
		flightDateBack = getIntent().getExtras().getString("flightDateBack");
		type = getIntent().getExtras().getString("type");
	}
	public void setListHeader(FlightsVo fvo){
		String depTime = fvo.getDepTime().substring(0, 2).trim() + ":"+ vo.getDepTime().substring(2, 4);
		String arrTime = fvo.getArrTime().substring(0, 2).trim() + ":"+ vo.getArrTime().substring(2, 4);
		TextView deparr = (TextView)findViewById(R.id.flight_detail_time);
		deparr.setText(depTime + "-" + arrTime);
	}
	public void setTower(FlightsVo fvo){
		TextView deparr = (TextView)findViewById(R.id.flight_detail_deparr);
		String depTower = fvo.getDepCityReferred();
		String arrTower = fvo.getArrCityReferred();
		deparr.setText(depTower + "-" + arrTower);
	}
	public void setCraftState(FlightsVo fvo){
		String flightNoRef = fvo.getCarrierReferred();
		String flightNoAndSoOn = fvo.getFlightNo();
		String stop = ("1".equals(fvo.getStop()))? "æ≠Õ£":"÷’µ„";
		TextView ref = (TextView)findViewById(R.id.flight_detail_craft);
		ref.setText(flightNoRef + " " + flightNoAndSoOn + " " + stop);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flight_detail);
		Exit.getInstance().addActivity(this);
		prepareParams();
		setTitle(depCityStr, arrCityStr);
		setListHeader(vo);
		setTower(vo);
		setCraftState(vo);
		backButton = (ImageView)findViewById
				(R.id.title_back_detail);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);  
			}
		});
		handler = new Handler();
		listview = (ListView)findViewById(R.id.detaillist);
		showListView();
	}

	public void showListView() {
		new Thread() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						setAdapters();
					}
				});
			}
		}.start();
	}

	public void setAdapters() {
		flightDetailAdapter = new FlightDetailAdapter(this, cabin, type, vo);
		listview.setAdapter(flightDetailAdapter);
	}
}
