package cn.tripg.activity.flight;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import cn.tripg.R;

public class SpecialPriceButton extends FrameLayout{
	public TextView date;
	public TextView price;
	public ImageView background;
	public SpecialPriceButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		FrameLayout fl = (FrameLayout)LayoutInflater.from(context).
				inflate(R.layout.special_price_button,this, true);
		date = (TextView) fl.findViewById(R.id.btn_date);
		price = (TextView) fl.findViewById(R.id.btn_price);
		background = (ImageView) fl.findViewById(R.id.btn_background);	
	}

	public SpecialPriceButton(Context context) {
		this(context, null);
	}

	public SpecialPriceButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
}
