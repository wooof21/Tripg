package cn.tripg.activity.flight;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout.LayoutParams;

/**
 * 日历控件单元格绘制类
 * @Description: 日历控件单元格绘制类

 * @FileName: DateWidgetDayCell.java 

 * @Package com.calendar.demo 

 * @Author Hanyonglu 

 * @Date 2012-3-17 下午03:19:34 

 * @Version V1.0
 */
public class DateWidgetDayCell extends View {
	// 字体大小
	private static final int fTextSize = 28;
	
	// 基本元素
	private OnItemClick itemClick = null;
	private Paint pt = new Paint();
	private RectF rect = new RectF();
	private String sDate = "";

	// 当前日期
	private int iDateYear = 0;
	private int iDateMonth = 0;
	private int iDateDay = 0;

	// 布尔变量
	private boolean bSelected = false;
	private boolean bIsActiveMonth = false;
	private boolean bToday = false;
	private boolean bTouchedDown = false;
	private boolean bHoliday = false;
	private boolean hasRecord = false;
	private String type;
	private ArrayList<HashMap<String, String>> spList;
	public static int ANIM_ALPHA_DURATION = 100;
	private String dateSelected;

	public interface OnItemClick {
		public void OnClick(DateWidgetDayCell item);
	}

	// 构造函数
	public DateWidgetDayCell(Context context, int iWidth, int iHeight,
			String type, ArrayList<HashMap<String, String>> spList, String dateSelected) {
		super(context);
		setFocusable(true);
		setLayoutParams(new LayoutParams(iWidth, iHeight));
		this.type = type;
		this.spList = spList;
		this.dateSelected = dateSelected;
	}

	// 取变量值
	public Calendar getDate() {
		Calendar calDate = Calendar.getInstance();
		calDate.clear();
		calDate.set(Calendar.YEAR, iDateYear);
		calDate.set(Calendar.MONTH, iDateMonth);
		calDate.set(Calendar.DAY_OF_MONTH, iDateDay);
		return calDate;
	}

	// 设置变量值
	public void setData(int iYear, int iMonth, int iDay, Boolean bToday,
			Boolean bHoliday, int iActiveMonth, boolean hasRecord) {
		iDateYear = iYear;
		iDateMonth = iMonth;
		iDateDay = iDay;

		this.sDate = Integer.toString(iDateDay);
		this.bIsActiveMonth = (iDateMonth == iActiveMonth);
		this.bToday = bToday;
		this.bHoliday = bHoliday;
		this.hasRecord = hasRecord;
	}

	private long dateM() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d = sdf.parse(("" + iDateYear) + "-" + ("" + (iDateMonth + 1))
				+ "-" + ("" + iDateDay));
		return d.getTime();
	}

	private long currM() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		long timeM = 0;
		try {
			timeM = sdf.parse(dateSelected).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return timeM;
	}

	private long dayF() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		long timeF = 0;
		try {
			timeF = sdf.parse(dateSelected).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (timeF + 14 * 24 * 3600 * 1000);
	}

	// 重载绘制方法
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		rect.set(0, 0, this.getWidth(), this.getHeight());
		rect.inset(1, 1);

		final boolean bFocused = IsViewFocused();

		drawDayView(canvas, bFocused);
		drawDayNumber(canvas);
	}

	public boolean IsViewFocused() {
		return (this.isFocused() || bTouchedDown);
	}

	// 绘制日历方格
	private void drawDayView(Canvas canvas, boolean bFocused) {

		if (bSelected || bFocused) {
			LinearGradient lGradBkg = null;

			if (bFocused) {
				lGradBkg = new LinearGradient(rect.left, 0, rect.right, 0,
						0xffaa5500, 0xffffddbb, Shader.TileMode.CLAMP);
			}

			if (bSelected) {
				lGradBkg = new LinearGradient(rect.left, 0, rect.right, 0,
						0xff225599, 0xffbbddff, Shader.TileMode.CLAMP);
			}

			if (lGradBkg != null) {
				pt.setShader(lGradBkg);
				canvas.drawRect(rect, pt);
			}

			pt.setShader(null);

		} else {
			pt.setColor(getColorBkg(bHoliday, bToday));
			canvas.drawRect(rect, pt);
		}

		if (hasRecord) {
			CreateReminder(canvas, DateSelectActivity.special_Reminder);
		}
		// else if (!hasRecord && !bToday && !bSelected) {
		// CreateReminder(canvas, Calendar_TestActivity.Calendar_DayBgColor);
		// }
	}

	// 绘制日历中的数字
	public void drawDayNumber(Canvas canvas) {
		// draw day number
		pt.setTypeface(null);
		pt.setAntiAlias(true);
		pt.setShader(null);
		pt.setFakeBoldText(true);
		pt.setTextSize(20);
		pt.setColor(DateSelectActivity.isPresentMonth_FontColor);
		pt.setUnderlineText(false);
		
		if (!bIsActiveMonth)
			pt.setColor(DateSelectActivity.unPresentMonth_FontColor);

		if (bToday)
			pt.setUnderlineText(true);

		final int iPosX = (int) rect.left + ((int) rect.width() >> 1)
				- ((int) pt.measureText(sDate) >> 1);

		final int iPosY = (int) (this.getHeight()
				- (this.getHeight() - getTextHeight()) / 2 - pt
				.getFontMetrics().bottom);

//		canvas.drawText(sDate, iPosX, iPosY, pt);
		if (spList.size() == 0) {
			canvas.drawText(sDate, iPosX, iPosY, pt);
		} else {

			if (type.equals("f")) {
				final int y1 = (this.getHeight() / 2) - (this.getHeight() / 8);
				final int y2 = (this.getHeight() / 2)
						+ (3 * this.getHeight() / 8);
				//final int x2 = (3 * (this.getWidth() - getTextWidth())) / 4;
				final int x2 = iPosX - getTextWidth() / 5;
				try {
					if (dateM() < currM()) {
						canvas.drawText(sDate, iPosX, iPosY, pt);
					} else if ((dateM() - dayF()) > 0) {
						canvas.drawText(sDate, iPosX, iPosY, pt);
					} else {
						if (!bIsActiveMonth) {
							pt.setColor(DateSelectActivity.unPresentMonth_FontColor);
						} else {
							pt.setColor(Color.RED);
						}
						pt.setUnderlineText(true);
						canvas.drawText(sDate, iPosX, y1, pt);
						for (int i = 0; i < spList.size(); i++) {
							HashMap<String, String> hashMap = spList.get(i);
							if (hashMap.get("date").equals(sDate)) {
								if (hashMap.get(sDate).equals("-")) {
									pt.setTextSize(16);
									canvas.drawText("无特价", x2, y2, pt);
								} else {
									pt.setTextSize(16);
									canvas.drawText("￥" + hashMap.get(sDate),
											x2, y2, pt);//
								}

							}
						}

					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				canvas.drawText(sDate, iPosX, iPosY, pt);
			}
		}
		pt.setUnderlineText(false);
	}

	// 得到字体高度
	private int getTextHeight() {
		return (int) (-pt.ascent() + pt.descent());
	}
	private int getTextWidth() {

		return (int) (pt.measureText("￥9999"));
	}

	// 根据条件返回不同颜色值
	public static int getColorBkg(boolean bHoliday, boolean bToday) {
		if (bToday)
			return DateSelectActivity.isToday_BgColor;
		// if (bHoliday) //如需周末有特殊背景色，可去掉注释
		// return Calendar_TestActivity.isHoliday_BgColor;
		return DateSelectActivity.Calendar_DayBgColor;
	}

	// 设置是否被选中
	@Override
	public void setSelected(boolean bEnable) {
		if (this.bSelected != bEnable) {
			this.bSelected = bEnable;
			this.invalidate();
		}
	}

	public void setItemClick(OnItemClick itemClick) {
		this.itemClick = itemClick;
	}

	public void doItemClick() {
		if (itemClick != null)
			itemClick.OnClick(this);
	}

	// 点击事件
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean bHandled = false;
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			bHandled = true;
			bTouchedDown = true;
			invalidate();
			startAlphaAnimIn(DateWidgetDayCell.this);
		}
		if (event.getAction() == MotionEvent.ACTION_CANCEL) {
			bHandled = true;
			bTouchedDown = false;
			invalidate();
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			bHandled = true;
			bTouchedDown = false;
			invalidate();
			doItemClick();
		}
		return bHandled;
	}

	// 点击事件
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean bResult = super.onKeyDown(keyCode, event);
		if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
				|| (keyCode == KeyEvent.KEYCODE_ENTER)) {
			doItemClick();
		}
		return bResult;
	}

	// 不透明度渐变
	public static void startAlphaAnimIn(View view) {
		AlphaAnimation anim = new AlphaAnimation(0.5F, 1);
		anim.setDuration(ANIM_ALPHA_DURATION);
		anim.startNow();
		view.startAnimation(anim);
	}

	public void CreateReminder(Canvas canvas, int Color) {
		pt.setStyle(Paint.Style.FILL_AND_STROKE);
		pt.setColor(Color);
		Path path = new Path();
		path.moveTo(rect.right - rect.width() / 4, rect.top);
		path.lineTo(rect.right, rect.top);
		path.lineTo(rect.right, rect.top + rect.width() / 4);
		path.lineTo(rect.right - rect.width() / 4, rect.top);
		path.close();
		canvas.drawPath(path, pt);
	}
}