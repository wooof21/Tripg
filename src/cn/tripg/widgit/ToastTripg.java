package cn.tripg.widgit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.tripg.R;

public class ToastTripg extends Toast{

	public ToastTripg(Context context) {
		super(context);
	}
	@SuppressLint("ShowToast")
	public static Toast makeText(Context context, CharSequence text, int time){
		Toast toast = Toast.makeText(context, "text", Toast.LENGTH_SHORT);
		LayoutInflater inflater = (LayoutInflater)
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout ll = (LinearLayout)inflater.inflate(R.layout.toast_layout, null);
		TextView tv = (TextView)ll.findViewById(R.id.toast_text);
		tv.setText(text);
		toast.setView(ll);;
		return toast;
	}

}
