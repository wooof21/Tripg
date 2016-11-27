package cn.tripg.interfaces.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import cn.tripg.interfaces.BaseInterface;

public class VipGeRenHttpIntfaces extends BaseInterface{

	
	
	private DocumentBuilder builder;
	public VipGeRenHttpIntfaces(Context context, Handler handler) {
		super(context, handler);
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub
		
		List<HashMap<String, Object>>listAry = new ArrayList<HashMap<String,Object>>();
		Log.e("详情界面 解析开始 ", ""+jsonOrXmlStr);
		
		try {
			JSONObject myJsonObject = new JSONObject(jsonOrXmlStr);
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			JSONObject resjJsonObject = myJsonObject.getJSONObject("Result");
			hashMap.put("CnName",resjJsonObject.get("CnName") );
			hashMap.put("UserName",resjJsonObject.get("UserName") );
			hashMap.put("Birthday",resjJsonObject.get("Birthday") );
			hashMap.put("RegisterTime", resjJsonObject.get("RegisterTime"));
			hashMap.put("Id", resjJsonObject.get("Id"));
			listAry.add(hashMap);
			
			
			
		} catch (Exception e) {
			
			e.printStackTrace();	
		}
		
		return listAry;
	}

}
