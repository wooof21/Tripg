package model.flight;

import android.widget.ImageView;
import android.widget.TextView;

/*
public class ViewHolder {
	public TextView textDepAndTimeAndTower,
	textArrAndTimeAndTower,
	textCraftAndTicketNoAndState, textPrice,
	textCompanyCode,
	textDiscount;
	public ImageView imageIcon;
	public ImageView imageone;
	public DisplayMetrics display;
	
	public float setSize(Context context) {
		display = new DisplayMetrics();
		display = context.getResources().getDisplayMetrics();
		if (display.widthPixels <= 240) { // 240X320 ��Ļ
			return 10.0f;
		} else if (display.widthPixels <= 320) { // 320X480 ��Ļ
			return 12.0f;
		} else if (display.widthPixels <= 480) { // 480X800 �� 480X854 ��Ļ
			return 14.0f;
		} else if (display.widthPixels <= 540) { // 540X960 ��Ļ
			return 16.0f;
		} else if (display.widthPixels <= 800) { // 800X1280 ��Ļ
			return 18.0f;
		} else { // ���� 800X1280
			return 22.0f;
		}
	}
}
*/

public class ViewHolder {
	public TextView depTimeArrTimeStop3;//line1
	public TextView depTowerArrTower2;//line2
	public TextView flightNo;
	public TextView ticketStatusCabinNameDiscount3;
	public TextView ticketPrice;
	public ImageView buttonBook;
	public TextView isTextView;
}

