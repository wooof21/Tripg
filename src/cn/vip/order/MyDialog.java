package cn.vip.order;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import cn.tripg.R;

public class MyDialog extends Dialog {

	Context context;

	public MyDialog(Context context) {
		super(context);

		this.context = context;
	}

	public MyDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog);

	}

}
