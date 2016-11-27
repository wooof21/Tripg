package cn.tripg.activity.flight;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.internet.Exit;
import cn.tripg.R;

public class DateSelectActivity extends Activity {
	// create calendar
	private LinearLayout layContent = null;
	private ArrayList<DateWidgetDayCell> days = new ArrayList<DateWidgetDayCell>();

	// define date vars
	public static Calendar calStartDate = Calendar.getInstance();
	private Calendar calToday = Calendar.getInstance();
	private Calendar calCalendar = Calendar.getInstance();
	private Calendar calSelected = Calendar.getInstance();

	// operate current date
	private int iMonthViewCurrentMonth = 0;
	private int iMonthViewCurrentYear = 0;
	private int iFirstDayOfWeek = Calendar.MONDAY;

	private int Calendar_Width = 0;
	private int Cell_Width = 0;

	private String liveDate;
	// related widget for operation
	TextView Top_Date = null;
	Button btn_pre_month = null;
	Button btn_next_month = null;
	TextView arrange_text = null;
	LinearLayout mainLayout = null;
	LinearLayout arrange_layout = null;

	// data source
	ArrayList<String> Calendar_Source = null;
	Hashtable<Integer, Integer> calendar_Hashtable = new Hashtable<Integer, Integer>();
	Boolean[] flag = null;
	Calendar startDate = null;
	Calendar endDate = null;
	int dayvalue = -1;

	public static int Calendar_WeekBgColor = 0;
	public static int Calendar_DayBgColor = 0;
	public static int isHoliday_BgColor = 0;
	public static int unPresentMonth_FontColor = 0;
	public static int isPresentMonth_FontColor = 0;
	public static int isToday_BgColor = 0;
	public static int special_Reminder = 0;
	public static int common_Reminder = 0;
	public static int Calendar_WeekFontColor = 0;
	private int hOf;
	private String type;
	String UserName = "";
	private ArrayList<HashMap<String, String>> spList;

	private String dateSelected;

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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Exit.getInstance().addActivity(this);
		// get the width and height of screen and adapt automatically
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		@SuppressWarnings("deprecation")
		int screenWidth = display.getWidth();
		Calendar_Width = screenWidth;
		Cell_Width = Calendar_Width / 7 + 1;

		mainLayout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.calendar_main, null);
		setContentView(mainLayout);
		
		spList = new ArrayList<HashMap<String,String>>();
		type = getIntent().getStringExtra("type");
		
		Log.e("type", type);
		if(type.equals("f")){
			TextView title = (TextView) findViewById(R.id.title_text_date);
			title.setText("特价日历");
			spList = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("sp");
			System.out.println(spList);
			dateSelected = getIntent().getExtras().getString("dateSelected");
			Log.e("dateSelected", dateSelected);
		}
		ImageView backButton = (ImageView) findViewById(R.id.title_back_date);

		Top_Date = (TextView) findViewById(R.id.Top_Date);
		btn_pre_month = (Button) findViewById(R.id.btn_pre_month);
		btn_next_month = (Button) findViewById(R.id.btn_next_month);
		btn_pre_month.setOnClickListener(new Pre_MonthOnClickListener());
		btn_next_month.setOnClickListener(new Next_MonthOnClickListener());
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = getIntent();
				setResult(ResultCode.FAILURE, intent);
				finish();
			}
		});
		liveDate = getIntent().getExtras().getString("liveDate");
		Log.e("liveDate", liveDate);
		
		
		hOf = getIntent().getExtras().getInt("hotelOrflight");
		// calculate the first day of this month
		// usualy someday of last month
		// update calendar
		calStartDate = getCalendarStartDate();
		mainLayout.addView(generateCalendarMain());
		DateWidgetDayCell daySelected = updateCalendar();

		if (daySelected != null)
			daySelected.requestFocus();

		LinearLayout.LayoutParams Param1 = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		ScrollView view = new ScrollView(this);
		arrange_layout = createLayout(LinearLayout.VERTICAL);
		arrange_layout.setPadding(5, 2, 0, 0);
		arrange_text = new TextView(this);
		mainLayout.setBackgroundColor(Color.WHITE);
		arrange_text.setTextColor(Color.BLACK);
		arrange_text.setTextSize(18);
		arrange_layout.addView(arrange_text);

		startDate = GetStartDate();
		calToday = GetTodayDate();

		endDate = GetEndDate(startDate);
		view.addView(arrange_layout, Param1);
		mainLayout.addView(view);

		new Thread() {
			@Override
			public void run() {
				int day = GetNumFromDate(calToday, startDate);

				if (calendar_Hashtable != null
						&& calendar_Hashtable.containsKey(day)) {
					dayvalue = calendar_Hashtable.get(day);
				}
			}

		}.start();

		Calendar_WeekBgColor = this.getResources().getColor(
				R.color.Calendar_WeekBgColor);
		Calendar_DayBgColor = this.getResources().getColor(
				R.color.Calendar_DayBgColor);
		isHoliday_BgColor = this.getResources().getColor(
				R.color.isHoliday_BgColor);
		unPresentMonth_FontColor = this.getResources().getColor(
				R.color.unPresentMonth_FontColor);
		isPresentMonth_FontColor = this.getResources().getColor(
				R.color.isPresentMonth_FontColor);
		isToday_BgColor = this.getResources().getColor(R.color.isToday_BgColor);
		special_Reminder = this.getResources()
				.getColor(R.color.specialReminder);
		common_Reminder = this.getResources().getColor(R.color.commonReminder);
		Calendar_WeekFontColor = this.getResources().getColor(
				R.color.Calendar_WeekFontColor);
	}

	protected String GetDateShortString(Calendar date) {
		String returnString = date.get(Calendar.YEAR) + "/";
		returnString += date.get(Calendar.MONTH) + 1 + "/";
		returnString += date.get(Calendar.DAY_OF_MONTH);

		return returnString;
	}

	// get the number sequence of current day
	private int GetNumFromDate(Calendar now, Calendar returnDate) {
		Calendar cNow = (Calendar) now.clone();
		Calendar cReturnDate = (Calendar) returnDate.clone();
		setTimeToMidnight(cNow);
		setTimeToMidnight(cReturnDate);

		long todayMs = cNow.getTimeInMillis();
		long returnMs = cReturnDate.getTimeInMillis();
		long intervalMs = todayMs - returnMs;
		int index = millisecondsToDays(intervalMs);

		return index;
	}

	private int millisecondsToDays(long intervalMs) {
		return Math.round((intervalMs / (1000 * 86400)));
	}

	private void setTimeToMidnight(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}

	private LinearLayout createLayout(int iOrientation) {
		LinearLayout lay = new LinearLayout(this);
		lay.setLayoutParams(new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		lay.setOrientation(iOrientation);

		return lay;
	}

	// create calendar header
	private View generateCalendarHeader() {
		LinearLayout layRow = createLayout(LinearLayout.HORIZONTAL);
		// layRow.setBackgroundColor(Color.argb(255, 207, 207, 205));

		for (int iDay = 0; iDay < 7; iDay++) {
			DateWidgetDayHeader day = new DateWidgetDayHeader(this, Cell_Width,
					35);

			final int iWeekDay = DayStyle.getWeekDay(iDay, iFirstDayOfWeek);
			day.setData(iWeekDay);
			layRow.addView(day);
		}

		return layRow;
	}

	// create main body of calendar
	private View generateCalendarMain() {
		layContent = createLayout(LinearLayout.VERTICAL);
		// layContent.setPadding(1, 0, 1, 0);
		layContent.setBackgroundColor(Color.argb(255, 105, 105, 103));
		layContent.addView(generateCalendarHeader());
		days.clear();

		for (int iRow = 0; iRow < 6; iRow++) {
			layContent.addView(generateCalendarRow());
		}

		return layContent;
	}

	// create one line of calendar only draw rect.
	private View generateCalendarRow() {
		LinearLayout layRow = createLayout(LinearLayout.HORIZONTAL);

		for (int iDay = 0; iDay < 7; iDay++) {
			DateWidgetDayCell dayCell = new DateWidgetDayCell(this, Cell_Width,
					Cell_Width, type, spList, dateSelected);
			dayCell.setItemClick(mOnDayCellClick);
			days.add(dayCell);
			layRow.addView(dayCell);
		}

		return layRow;
	}

	private Calendar getCalendarStartDate() {
		calToday.setTimeInMillis(System.currentTimeMillis());
		calToday.setFirstDayOfWeek(iFirstDayOfWeek);

		if (calSelected.getTimeInMillis() == 0) {
			calStartDate.setTimeInMillis(System.currentTimeMillis());
			calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);
		} else {
			calStartDate.setTimeInMillis(calSelected.getTimeInMillis());
			calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);
		}

		UpdateStartDateForMonth();
		return calStartDate;
	}

	private void UpdateStartDateForMonth() {
		iMonthViewCurrentMonth = calStartDate.get(Calendar.MONTH);
		iMonthViewCurrentYear = calStartDate.get(Calendar.YEAR);
		calStartDate.set(Calendar.DAY_OF_MONTH, 1);
		calStartDate.set(Calendar.HOUR_OF_DAY, 0);
		calStartDate.set(Calendar.MINUTE, 0);
		calStartDate.set(Calendar.SECOND, 0);
		// update days for week
		UpdateCurrentMonthDisplay();
		int iDay = 0;
		int iStartDay = iFirstDayOfWeek;

		if (iStartDay == Calendar.MONDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
			if (iDay < 0)
				iDay = 6;
		}

		if (iStartDay == Calendar.SUNDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
			if (iDay < 0)
				iDay = 6;
		}

		calStartDate.add(Calendar.DAY_OF_WEEK, -iDay);
	}

	// update calendar
	private DateWidgetDayCell updateCalendar() {
		DateWidgetDayCell daySelected = null;
		boolean bSelected = false;
		final boolean bIsSelection = (calSelected.getTimeInMillis() != 0);
		final int iSelectedYear = calSelected.get(Calendar.YEAR);
		final int iSelectedMonth = calSelected.get(Calendar.MONTH);
		final int iSelectedDay = calSelected.get(Calendar.DAY_OF_MONTH);
		calCalendar.setTimeInMillis(calStartDate.getTimeInMillis());

		for (int i = 0; i < days.size(); i++) {
			final int iYear = calCalendar.get(Calendar.YEAR);
			final int iMonth = calCalendar.get(Calendar.MONTH);
			final int iDay = calCalendar.get(Calendar.DAY_OF_MONTH);
			final int iDayOfWeek = calCalendar.get(Calendar.DAY_OF_WEEK);
			DateWidgetDayCell dayCell = days.get(i);

			boolean bToday = false;

			if (calToday.get(Calendar.YEAR) == iYear) {
				if (calToday.get(Calendar.MONTH) == iMonth) {
					if (calToday.get(Calendar.DAY_OF_MONTH) == iDay) {
						bToday = true;
					}
				}
			}

			// check holiday
			boolean bHoliday = false;
			if ((iDayOfWeek == Calendar.SATURDAY)
					|| (iDayOfWeek == Calendar.SUNDAY))
				bHoliday = true;
			if ((iMonth == Calendar.JANUARY) && (iDay == 1))
				bHoliday = true;

			bSelected = false;

			if (bIsSelection)
				if ((iSelectedDay == iDay) && (iSelectedMonth == iMonth)
						&& (iSelectedYear == iYear)) {
					bSelected = true;
				}

			dayCell.setSelected(bSelected);

			// exist log ?
			boolean hasRecord = false;

			if (flag != null && flag[i] == true && calendar_Hashtable != null
					&& calendar_Hashtable.containsKey(i)) {
				// hasRecord = flag[i];
				hasRecord = Calendar_Source.get(calendar_Hashtable.get(i))
						.contains(UserName);
			}

			if (bSelected)
				daySelected = dayCell;

			dayCell.setData(iYear, iMonth, iDay, bToday, bHoliday,
					iMonthViewCurrentMonth, hasRecord);
			dayCell.invalidate();
			calCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}

		layContent.invalidate();

		return daySelected;
	}

	// update calendar title showing year month
	private void UpdateCurrentMonthDisplay() {
		String date = calStartDate.get(Calendar.YEAR) + "年"
				+ (calStartDate.get(Calendar.MONTH) + 1) + "月";
		Top_Date.setText(date);
		Top_Date.setTextSize(20);
	}

	// pre-month button listener.
	class Pre_MonthOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {

			arrange_text.setText("");
			calSelected.setTimeInMillis(0);
			iMonthViewCurrentMonth--;

			if (iMonthViewCurrentMonth == -1) {
				iMonthViewCurrentMonth = 11;
				iMonthViewCurrentYear--;
			}

			calStartDate.set(Calendar.DAY_OF_MONTH, 1);
			calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
			calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);
			calStartDate.set(Calendar.HOUR_OF_DAY, 0);
			calStartDate.set(Calendar.MINUTE, 0);
			calStartDate.set(Calendar.SECOND, 0);
			calStartDate.set(Calendar.MILLISECOND, 0);
			UpdateStartDateForMonth();

			startDate = (Calendar) calStartDate.clone();
			endDate = GetEndDate(startDate);

			new Thread() {
				@Override
				public void run() {

					int day = GetNumFromDate(calToday, startDate);

					if (calendar_Hashtable != null
							&& calendar_Hashtable.containsKey(day)) {
						dayvalue = calendar_Hashtable.get(day);
					}
				}
			}.start();

			updateCalendar();
		}

	}

	// next-month button listener
	class Next_MonthOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {

			arrange_text.setText("");
			calSelected.setTimeInMillis(0);
			iMonthViewCurrentMonth++;

			if (iMonthViewCurrentMonth == 12) {
				iMonthViewCurrentMonth = 0;
				iMonthViewCurrentYear++;
			}

			calStartDate.set(Calendar.DAY_OF_MONTH, 1);
			calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
			calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);
			UpdateStartDateForMonth();

			startDate = (Calendar) calStartDate.clone();
			endDate = GetEndDate(startDate);

			new Thread() {
				@Override
				public void run() {
					int day = 5;

					if (calendar_Hashtable != null
							&& calendar_Hashtable.containsKey(day)) {
						dayvalue = calendar_Hashtable.get(day);
					}
				}
			}.start();

			updateCalendar();
		}
	}

	// select date you clicked
	private DateWidgetDayCell.OnItemClick mOnDayCellClick = new DateWidgetDayCell.OnItemClick() {
		public void OnClick(DateWidgetDayCell item) {

			String currentDay = liveDate.replace("-", "");
			Log.e("currentDay ------", currentDay);
			Calendar c = item.getDate();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int day = c.get(Calendar.DAY_OF_MONTH);
			String date = GetDate.StringToDate(year + "-" + month + "-" + day,
					"yyyy-MM-dd").replace("-", "");// 当前选中日期
			Log.e("date ------", date);
			// date that you selected.
			
			String dateSelected = year + "-" + month + "-" + day;
			Log.e("dateSelected ------", dateSelected);
			item.setSelected(true);
			updateCalendar();

			if (Long.parseLong(currentDay) <= Long.parseLong(date)) {
				// Log.e("","current day <= select date");
				// overridePendingTransition(android.R.anim.fade_in,
				// android.R.anim.fade_out);
				// valid.
				Intent intent = getIntent();
				intent.putExtra("date", dateSelected);
				setResult(ResultCode.SUCCESS, intent);
				finish();
			} else {
				String msg = null;
				if(hOf == 0){
					msg = "当天日期为 " + liveDate +"\n" + 
							"不能选择小于当天的日期";
				}else if(hOf == 1){
					msg = "入住日期为 " + liveDate + "\n" +
							"不能选择小于入住时间的日期";
				}else if(hOf == 2){
					msg = "当天日期为 " + liveDate +"\n" + 
							"不能选择小于当天的日期";
				}else if(hOf == 3){
					msg = "出发日期为 " + liveDate +"\n" + 
							"返回日期不能小于出发日期";
				}
				createDialog(msg);
				// invalid.do nothing
				// Log.e("","current day > select date");
			}
		}
	};

	private void createDialog(String msg){
		Builder alertDg = new AlertDialog.Builder(DateSelectActivity.this);
		alertDg.setTitle("提示");
		alertDg.setMessage(msg);
		alertDg.setPositiveButton("确定", null);
		alertDg.show();
	}
	
	public Calendar GetTodayDate() {
		Calendar cal_Today = Calendar.getInstance();
		cal_Today.set(Calendar.HOUR_OF_DAY, 0);
		cal_Today.set(Calendar.MINUTE, 0);
		cal_Today.set(Calendar.SECOND, 0);
		cal_Today.setFirstDayOfWeek(Calendar.MONDAY);

		return cal_Today;
	}

	// get the first date in calendar.
	public Calendar GetStartDate() {
		int iDay = 0;
		Calendar cal_Now = Calendar.getInstance();
		cal_Now.set(Calendar.DAY_OF_MONTH, 1);
		cal_Now.set(Calendar.HOUR_OF_DAY, 0);
		cal_Now.set(Calendar.MINUTE, 0);
		cal_Now.set(Calendar.SECOND, 0);
		cal_Now.setFirstDayOfWeek(Calendar.MONDAY);

		iDay = cal_Now.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;

		if (iDay < 0) {
			iDay = 6;
		}

		cal_Now.add(Calendar.DAY_OF_WEEK, -iDay);
		return cal_Now;
	}

	public Calendar GetEndDate(Calendar startDate) {
		Calendar endDate = Calendar.getInstance();
		endDate = (Calendar) startDate.clone();
		endDate.add(Calendar.DAY_OF_MONTH, 41);
		return endDate;
	}
}