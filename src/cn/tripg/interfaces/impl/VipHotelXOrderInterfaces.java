package cn.tripg.interfaces.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import cn.tripg.interfaces.BaseInterface;

public class VipHotelXOrderInterfaces extends BaseInterface{

	private DocumentBuilder builder;
	public VipHotelXOrderInterfaces(Context context, Handler handler) {
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
	public Object parseJSONXML(String jsonOrXmlStr) {
		
		
		List<HashMap<String, Object>>listAry = new ArrayList<HashMap<String,Object>>();
		Log.e("详情界面 解析开始 ", ""+jsonOrXmlStr);
		
		try {
		
			JSONObject myJsonObject = new JSONObject(jsonOrXmlStr);
			JSONObject orderJsonObject = myJsonObject.getJSONObject("Result");

				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("ArrivalEarlyTime", orderJsonObject.get("ArrivalEarlyTime").toString());
				hashMap.put("CheckInDate", orderJsonObject.get("CheckInDate").toString());
				hashMap.put("CheckOutDate", orderJsonObject.get("CheckOutDate").toString());
				hashMap.put("TotalPrice", orderJsonObject.get("TotalPrice").toString());
				hashMap.put("HotelName", orderJsonObject.get("HotelName").toString());
				hashMap.put("OrderStatus", orderJsonObject.get("OrderStatus").toString());
				hashMap.put("OrderId", orderJsonObject.get("OrderId").toString());
				hashMap.put("RoomType", orderJsonObject.get("RoomType").toString());
				hashMap.put("AddTime", orderJsonObject.get("AddTime").toString());
				JSONObject conJsonObject = new JSONObject(orderJsonObject.getString("Contacters"));	
				hashMap.put("Name", conJsonObject.get("Name").toString());
				hashMap.put("Mobile", conJsonObject.get("Mobile").toString());
				listAry.add(hashMap);
				

			
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
		
		
		return listAry;
	}
		

}
