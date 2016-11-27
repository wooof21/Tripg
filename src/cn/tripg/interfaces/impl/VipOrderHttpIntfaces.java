package cn.tripg.interfaces.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import cn.tripg.interfaces.BaseInterface;

public class VipOrderHttpIntfaces extends BaseInterface{

	
	@SuppressWarnings("unused")
	private DocumentBuilder builder;
	public VipOrderHttpIntfaces(Context context, Handler handler) {
		super(context, handler);
		try {
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			this.builder = f.newDocumentBuilder();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

		


	@SuppressWarnings("unused")
	private String getSubElementTextContent(Element ele, String tagName) {
		NodeList list = ele.getElementsByTagName(tagName);
		Element e = (Element) list.item(0);
		//得到中间的文本节点
		return e.getTextContent();
	}
	public static InputStream getStringInputStream(String s) {
	    if (s != null && !s.equals("")) {
	        try {

	            ByteArrayInputStream stringInputStream = new ByteArrayInputStream(
	                    s.getBytes());
	            return stringInputStream;
	        }catch (Exception e) {

	            e.printStackTrace();
	        }
	    }
	    return null;
	}
	
	@Override
	public Object parseJSONXML(String jsonOrXmlStr) {//
		
		
		List<HashMap<String, Object>>listAry = new ArrayList<HashMap<String,Object>>();
		
		Log.e("订单号解析工作开始", ""+jsonOrXmlStr);
		try {
			JSONObject jsonObject = new JSONObject(jsonOrXmlStr);
			JSONArray resultArray = jsonObject.getJSONArray("Result");
			Log.e("resultArray  -----******", ""+resultArray.length());
			for (int i = 0; i < resultArray.length() ; i++) {
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				JSONObject aryJsonObject = resultArray.getJSONObject(i);
				hashMap.put("order_id", aryJsonObject.get("order_id"));
				hashMap.put("order_dtime", aryJsonObject.get("order_dtime"));
				hashMap.put("order_date", aryJsonObject.get("order_date"));
				hashMap.put("order_acity", aryJsonObject.get("order_acity"));
				hashMap.put("order_scity", aryJsonObject.get("order_scity"));
				hashMap.put("order_jpice", aryJsonObject.get("order_jpice"));
				hashMap.put("order_resid", aryJsonObject.get("order_resid"));
				hashMap.put("order_safe", aryJsonObject.get("order_safe"));
				hashMap.put("order_status", aryJsonObject.get("order_status"));
				hashMap.put("order_tax", aryJsonObject.get("order_tax"));
				hashMap.put("order_yq", aryJsonObject.get("order_yq"));
				hashMap.put("res_type", aryJsonObject.get("res_type"));
				Log.e("resultArray", ""+i+"---------------"+aryJsonObject.get("order_resid"));
				listAry.add(hashMap);
				Log.e("listAry-------", ""+listAry.size());
			}
			

			
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		
		return listAry;
	}
}
