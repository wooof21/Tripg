package cn.tripg.interfaces;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import cn.model.XMLResultModel;
import cn.tripg.widgit.ProgressDialogTripg;

public abstract class BaseInterface {
	public ProgressDialog progressDialog;
	protected Handler handler;
	protected Context context;
	private boolean progressDialogFlag;
	private String jsonOrXmlStr;

	@SuppressLint("HandlerLeak")
    public BaseInterface(Context context, Handler handler){
		jsonOrXmlStr = "";
		this.context = context;
		this.handler = handler;
		this.progressDialogFlag = true;//default.
    }
	
	public final void disableProgressDialog(){
		progressDialogFlag = false;
	}
	public final void enableProgressDialog(){
		progressDialogFlag = true;
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
			progressDialog.dismiss();
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

public final String doGetDataHang(String url) {
		
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
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"GBK"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			progressDialog.dismiss();
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
	
	
	public final String doPostData(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// �򿪺�URL֮�������
			URLConnection conn = realUrl.openConnection();
			// ����ͨ�õ���������
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// ����POST�������������������
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// ��ȡURLConnection�����Ӧ�������
			out = new PrintWriter(conn.getOutputStream());
			// �����������
			out.print(param);
			// flush������Ļ���
			out.flush();
			// ����BufferedReader����������ȡURL����Ӧ
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			progressDialog.dismiss();
			Log.e("send post", "���� POST ��������쳣��");
			return "";
			//System.out.println("���� POST ��������쳣��" + e);
			//e.printStackTrace();
		}
		// ʹ��finally�����ر��������������
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				//progressDialog.dismiss();
				Log.e("close post", "�ر��������쳣��");
				return "";
			}
		}
		return result;
	}

	/**
	 * 
	 * @param jsonOrXmlStr the json or xml string passed by system.
	 * @return return null if parse failed.
	 */
	public abstract Object parseJSONXML(String jsonOrXmlStr);
	
	public String prepareGetFullURL(HashMap<String, String> params){ return null;}
	
	public String preparePostURL(){ return null;}
	
	public String preparePostParams(HashMap<String, String> params){ return null;}

	/**
	 * 
	 * @param url the valid url that represents the request.
	 * @return return null if get data fail.You should check this return value in client (null or not)
	 */
	public final void getModel1FromGET(final String url,final int msgCode,final String name){
		if(progressDialogFlag)
			progressDialog = ProgressDialogTripg.show(context, null, null);
		new Thread(){
			@Override
			public void run() {
				Looper.prepare();
				if (name.equals("1")) {
					Log.e("name url -- ", ""+name);
					jsonOrXmlStr = doGetDataHang(url);
				}else {
					jsonOrXmlStr = doGetData(url);
				}
				
				Log.e("jsonOrXmlStr ----", jsonOrXmlStr);
				
//				if("".equals(jsonOrXmlStr)){
//					handler.sendEmptyMessage(0);
//					return;
//				}
//				Object model = parseJSONXML(jsonOrXmlStr);
//				final Message message = new Message();
//				message.what = msgCode;
//				message.obj = model;
//				handler.post(new Runnable() {
//					@Override
//					public void run() {
//						handler.sendMessage(message);
//					}
//				});
				
				JSONObject job = new JSONObject();
				try {
					job = XML.toJSONObject(jsonOrXmlStr);
					JSONObject job1 = new JSONObject();
					job1 = job.getJSONObject("Flight");
					String fNullCode = job1.getString("FlightNullCode");
					String fNote = job1.getString("FlightNote");
					System.out.println("fNullCode ---> " + fNullCode);
					System.out.println("fNote ---> " + fNote);
					
					if("".equals(jsonOrXmlStr)){
						handler.sendEmptyMessage(0);
						return;
					}else if(fNullCode.equals("1")){
						final Message message = new Message();
						message.what = 2;
						message.obj = fNote;
						handler.post(new Runnable() {
							@Override
							public void run() {
								handler.sendMessage(message);
							}
						});
					}else{
						Object model = parseJSONXML(jsonOrXmlStr);
						final Message message = new Message();
						message.what = msgCode;
						message.obj = model;
						handler.post(new Runnable() {
							@Override
							public void run() {
								handler.sendMessage(message);
							}
						});
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
			}
		}.start();
	}
	
	/**
	 * 
	 * @param url the valid url that represents the request.
	 * @return return null if get data fail.You should check this return value in client (null or not)
	 */
	public final void getModelFromGET(final String url,final int msgCode,final String name){
		if(progressDialogFlag)
			progressDialog = ProgressDialogTripg.show(context, null, null);
		new Thread(){
			@Override
			public void run() {
				Looper.prepare();
				if (name.equals("1")) {
					Log.e("name url -- ", ""+name);
					jsonOrXmlStr = doGetDataHang(url);
				}else {
					jsonOrXmlStr = doGetData(url);
				}
				
				Log.e("jsonOrXmlStr ----", jsonOrXmlStr);
				
				if("".equals(jsonOrXmlStr)){
					handler.sendEmptyMessage(0);
					return;
				}
				Object model = parseJSONXML(jsonOrXmlStr);
				final Message message = new Message();
				message.what = msgCode;
				message.obj = model;
				handler.post(new Runnable() {
					@Override
					public void run() {
						handler.sendMessage(message);
					}
				});
				
//				JSONObject job = new JSONObject();
//				try {
//					job = XML.toJSONObject(jsonOrXmlStr);
//					JSONObject job1 = new JSONObject();
//					job1 = job.getJSONObject("Flight");
//					String fNullCode = job1.getString("FlightNullCode");
//					String fNote = job1.getString("FlightNote");
//					System.out.println("fNullCode ---> " + fNullCode);
//					System.out.println("fNote ---> " + fNote);
//					
//					if("".equals(jsonOrXmlStr)){
//						handler.sendEmptyMessage(0);
//						return;
//					}else if(fNullCode.equals("1")){
//						final Message message = new Message();
//						message.what = 10;
//						message.obj = fNote;
//						handler.post(new Runnable() {
//							@Override
//							public void run() {
//								handler.sendMessage(message);
//							}
//						});
//					}else{
//						Object model = parseJSONXML(jsonOrXmlStr);
//						final Message message = new Message();
//						message.what = msgCode;
//						message.obj = model;
//						handler.post(new Runnable() {
//							@Override
//							public void run() {
//								handler.sendMessage(message);
//							}
//						});
//					}
//					
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
				
				
			}
		}.start();
	}
	
	
	/**
	 * @param url the first part of url
	 * @param param the second part of url, request param string
	 * @return return null if get data fail.You should check this return value in client (null or not)
	 */
	public final void getModelFromPOST(final String url,
			final String param, final int msgCode){
		if(progressDialogFlag)
			progressDialog = ProgressDialogTripg.show(context, null, null);
		new Thread(){
			@Override
			public void run() {
				Looper.prepare();
				jsonOrXmlStr = doPostData(url, param);
				if("".equals(jsonOrXmlStr)){
					handler.sendEmptyMessage(0);
					return;
				}
				Object model = parseJSONXML(jsonOrXmlStr);
				final Message message = new Message();
				message.what = msgCode;
				message.obj = model;
				handler.post(new Runnable() {
					@Override
					public void run() {
						handler.sendMessage(message);
					}
				});
			}
		}.start();
	}
}
