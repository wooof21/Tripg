package cn.internet;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class Tools {

	public String getURL(String urlStr) {
		HttpURLConnection httpcon = null;
		InputStream in = null;
		String data = "";

		try {
			URL url = new URL(urlStr);
			httpcon = (HttpURLConnection) url.openConnection();
			httpcon.setDoInput(true);
			httpcon.setDoOutput(true);
			httpcon.setUseCaches(false);
			httpcon.setRequestMethod("GET");
			in = httpcon.getInputStream();
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader bufferReader = new BufferedReader(isr);
			String input = "";
			while ((input = bufferReader.readLine()) != null) {
				data = input + data;
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (httpcon != null) {
				httpcon.disconnect();
			}
		}

		return data;

	}
	
	public String previousDay(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d = java.sql.Date.valueOf(date);
		Calendar calendar1 = getCalendarDate(d);
		calendar1.setTime(d);
		calendar1.add(calendar1.DATE, -1);
		d = calendar1.getTime(); 
		sdf.format(d);
		Log.e("previousDay ---", sdf.format(d));
		return sdf.format(d);
	}
	public String Today(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		String d = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH)+1) + "-" + c.get(Calendar.DAY_OF_MONTH);
		Date date = java.sql.Date.valueOf(d);
		
		Log.e("today", sdf.format(date));

		return sdf.format(date);
	}
	public String nextDay(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d = java.sql.Date.valueOf(date);
		Calendar calendar1 = getCalendarDate(d);
		calendar1.setTime(d);
		calendar1.add(calendar1.DATE, 1);
		d = calendar1.getTime(); 
		sdf.format(d);
		Log.e("nextDay ---", sdf.format(d));
		return sdf.format(d);
	}
	
	public Calendar getCalendarDate(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar;
	}
	public void scaleInAnimation(View v){
		ScaleAnimation sa = new ScaleAnimation(1.0f, 0.95f, 1.0f, 0.95f,  
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,  
                0.5f);  
		sa.setRepeatCount(0);
		sa.setFillAfter(false);
		sa.setDuration(100);
		v.startAnimation(sa);
	}
	public void upDown(View v){
		TranslateAnimation ta = new TranslateAnimation(0, 0, 0, 5);
		ta.setDuration(500);
		ta.setInterpolator(new CycleInterpolator(5));
		v.startAnimation(ta);
	}
	public final String doGetData(String url) {
		
		String result = "";
		BufferedReader in = null;
		try {
			URL realUrl = new URL(url);
			// �򿪺�URL֮�������
			URLConnection connection = realUrl.openConnection();
			// ����ͨ�õ���������
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// ����ʵ�ʵ�����
			connection.connect();
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			
			Log.e("send get", "����GET��������쳣��");
			return "";
		}
		// ʹ��finally�����ر�������
		finally {
			try {
				if (in != null) {
					in.close();
					//progressDialog.dismiss();
				}
			} catch (Exception e2) {
				//progressDialog.dismiss();
				Log.e("close get", "�ر��������쳣��");
				return "";
			}
		}
		return result;
	}
	
	public String doPostData(String urlStr) {
		HttpURLConnection httpcon = null;
		InputStream in = null;
		String data = "";
		
		try {
			URL url = new URL(urlStr);
			httpcon = (HttpURLConnection) url.openConnection();
			httpcon.setDoInput(true);
			
			httpcon.setDoOutput(true);
			httpcon.setUseCaches(false);
			httpcon.setRequestMethod("POST");
			in = httpcon.getInputStream();
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader bufferReader = new BufferedReader(isr);
			String input = "";
			while ((input = bufferReader.readLine()) != null) {
				data = input + data;
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (httpcon != null) {
				httpcon.disconnect();
			}
		}

		return data;
		
	}
	
	public String doPostData(String urlStr, String data) throws IOException {
		String result = "";
		byte[] xmlbyte = data.getBytes("UTF-8");
		Log.e("post�ӿ��ϴ� ��ʽ������---utf8---", data);
		
		URL url = new URL(urlStr);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setDoOutput(true);// �������
		conn.setDoInput(true);
		conn.setUseCaches(false);// ��ʹ�û���
		conn.setRequestMethod("POST");
		conn.getOutputStream().write(xmlbyte);
		conn.getOutputStream().flush();
		conn.getOutputStream().close();
		
		Log.e("conn.getResponseCode()----", "" + conn.getResponseCode());
		
		if (conn.getResponseCode() != 200)
			throw new RuntimeException("����urlʧ��");
		int codeOrder = conn.getResponseCode();
		
		if (codeOrder == 200) {

			InputStream inStream = conn.getInputStream();// ��ȡ��������

			// ʹ�������������ַ�(��ѡ)
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int len;
			while ((len = inStream.read(buf)) != -1) {
				out.write(buf, 0, len);
			}
			result = out.toString("UTF-8");						
			Log.e("post���ؽ��--------", "" + result);
			out.close();
			
		} else {
			
		}
		
		return result;
	}
	
	public String getUserName(Context context){
		SharedPreferences sharedPre = context.getSharedPreferences(
        		"config", Context.MODE_PRIVATE);
		String userName = sharedPre.getString("username", "");
		System.out.println("memberId ---> " + userName);
		return userName;
	}
	
	public String getUserId(Context context){
		SharedPreferences sharedPre = context.getSharedPreferences(
        		"config", Context.MODE_PRIVATE);
		String memberId = sharedPre.getString("Result", "");
		System.out.println("memberId ---> " + memberId);
		return memberId;
	}
	public String getDepId(Context context){
		SharedPreferences sharedPre = context.getSharedPreferences(
        		"config", Context.MODE_PRIVATE);
		String depId = sharedPre.getString("depId", "");
		System.out.println("depId ---> " + depId);
		return depId;
	}
	public String getComId(Context context){
		SharedPreferences sharedPre = context.getSharedPreferences(
        		"config", Context.MODE_PRIVATE);
		String companyId = sharedPre.getString("companyId", "");
		System.out.println("companyId ---> " + companyId);
		return companyId;
	}
	public String getCnName(Context context){
		SharedPreferences sharedPre = context.getSharedPreferences(
        		"config", Context.MODE_PRIVATE);
		String cnName = sharedPre.getString("cnName", "");
		System.out.println("cnName ---> " + cnName);
		return cnName;
	}
	public String getRoleId(Context context){
		SharedPreferences sharedPre = context.getSharedPreferences(
        		"config", Context.MODE_PRIVATE);
		String role_id = sharedPre.getString("role_id", "");
		System.out.println("role_id ---> " + role_id);
		return role_id;
	}
}
