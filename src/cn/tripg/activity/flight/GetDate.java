package cn.tripg.activity.flight;

import android.annotation.SuppressLint;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class GetDate {

	public static ArrayList<String> getdate(int year, int month, int day) {
		ArrayList<String> list = new ArrayList<String>();
		GregorianCalendar g = new GregorianCalendar(year, month - 1, day);
		int thisMonth = g.get(Calendar.MONTH);
		for (int j = 1; j <= 31; ++j) {
			int d = g.get(Calendar.DAY_OF_MONTH);
			list.add(d + "");
			g.add(Calendar.DAY_OF_YEAR, 1);
			if (g.get(Calendar.MONDAY) != thisMonth) {
				break;
			}
		}
		return list;
	}

	public static String get(String str, int number) {
		int year = Integer.parseInt(str.substring(0, 4));
		int month = Integer.parseInt(str.substring(str.indexOf("-") + 1,
				str.lastIndexOf("-")));
		int day = Integer.parseInt(str.substring(str.lastIndexOf("-") + 1,
				str.length()));
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < getdate(year, month, day).size(); i++) {
			list.add(getdate(year, month, day).get(i));
		}
		if ((list.size() - 1) >= number) {
			return year + "-" + month + "-" + list.get(number);
		} else {
			if (month == 12) {
				year++;
				month = 1;
				day = 1;
			} else {
				month++;
				day = 1;
			}
			for (int i = 0; i < getdate(year, month, day).size(); i++) {
				list.add(getdate(year, month, day).get(i));
			}
			return year + "-" + month + "-" + list.get(number);
		}
	}

	@SuppressLint("SimpleDateFormat")
	public static String StringToDate(String dateStr, String formatStr) {
		DateFormat sdf = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String str = sdf.format(date);
		return str;
	}


	public static String addDate(String day, int x) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// SimpleDateFormat format = new
		// SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = null;
		try {
			date = format.parse(day);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (date == null)
			return "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, x);
		// cal.add(Calendar.HOUR, x);
		date = cal.getTime();
		System.out.println("front:" + date);
		cal = null;
		return format.format(date);
	}

	public static long dateDiff(String startTime, String endTime, String format) {
		SimpleDateFormat sd = new SimpleDateFormat(format);
		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		long ns = 1000;
		long diff;
		long day = 0;
		try {

			diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
			day = diff / nd;
			long hour = diff % nd / nh;
			long min = diff % nd % nh / nm;
			long sec = diff % nd % nh % nm / ns;
			System.out.println("时间相差：" + day + "天" + hour + "小时" + min + "分钟"
					+ sec + "秒。");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return day;
	}
}
