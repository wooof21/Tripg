package cn.tripg.widgit;

import javax.crypto.spec.IvParameterSpec;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.tripg.R;

public class ProgressDialogTripg extends ProgressDialog{

	private static AnimationDrawable ad;
	private static ImageView iv;
	public ProgressDialogTripg(Context context, int theme) {
		super(context, theme);
	}
	public ProgressDialogTripg(Context context) {
		super(context);
	}
	public static ProgressDialog show(Context context, CharSequence title,
            CharSequence message){
		
		LayoutInflater inflater = (LayoutInflater)
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout ll = (LinearLayout)inflater.inflate(R.layout.progress_dialog_layout, null);
		iv = (ImageView) ll.findViewById(R.id.loading_img);
		ad = (AnimationDrawable) iv.getDrawable();
		
		ProgressDialog pd = ProgressDialog.show(context, title, message);
		pd.setContentView(ll);
		ad.start();
		return pd;
		
	}
	
}
