package cn.tripg.interfaces.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import cn.tripg.interfaces.BaseInterface;

public class XiuHttpGeturlInterfaces extends BaseInterface{

	public XiuHttpGeturlInterfaces(Context context, Handler handler) {
		super(context, handler);
		// TODO Auto-generated constructor stub
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
		
		Log.e("详情界面 解析开始 ", ""+jsonOrXmlStr);
		
		
		return jsonOrXmlStr;
	}

}
