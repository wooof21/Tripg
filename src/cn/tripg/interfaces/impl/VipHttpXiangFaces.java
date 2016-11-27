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

public class VipHttpXiangFaces extends BaseInterface{

	
	
	private DocumentBuilder builder;
	public VipHttpXiangFaces(Context context, Handler handler) {
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
			JSONObject resJsonObject = myJsonObject.getJSONObject("Result");
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			
			hashMap.put("order_acity", (String)resJsonObject.get("order_acity"));//抵达城市
			hashMap.put("order_scity", (String)resJsonObject.get("order_scity"));//出发城市
			hashMap.put("order_company", (String)resJsonObject.get("order_company"));//航空公司二字码
			hashMap.put("order_date", (String)resJsonObject.get("order_date"));//起飞日期
			hashMap.put("order_jpice", (String)resJsonObject.get("order_jpice"));//票价
			hashMap.put("order_id", (String)resJsonObject.get("order_id"));//编号
			hashMap.put("order_number", (String)resJsonObject.get("order_number"));//航班号
			hashMap.put("order_member", (String)resJsonObject.get("order_member"));//会员编号
			hashMap.put("order_people", (String)resJsonObject.get("order_people"));//乘机人
			hashMap.put("order_resid", (String)resJsonObject.get("order_resid"));//订单编号
			hashMap.put("order_tax", (String)resJsonObject.get("order_tax"));//机建
			hashMap.put("order_safe", (String)resJsonObject.get("order_safe").toString());//保险
			hashMap.put("order_yq", (String)resJsonObject.get("order_yq"));//燃油
			hashMap.put("ps_date", (String)resJsonObject.get("ps_date"));//ps日期
			hashMap.put("telphone", (String)resJsonObject.get("telphone"));//联系电话
			hashMap.put("order_space", (String)resJsonObject.get("order_space"));//仓位
			hashMap.put("order_status", (String)resJsonObject.get("order_status"));//订单状态
			hashMap.put("order_cmt", (String)resJsonObject.get("order_cmt"));//退改签
			hashMap.put("equip", (String)resJsonObject.get("equip"));//机型
			hashMap.put("order_discount", (String)resJsonObject.get("order_discount"));//折扣率
			hashMap.put("realname", (String)resJsonObject.get("realname"));//
			hashMap.put("res_type", (String)resJsonObject.get("res_type"));//退改签类型
			hashMap.put("order_dtime", (String)resJsonObject.get("order_dtime"));//具体时间
			hashMap.put("delivery", (String)resJsonObject.get("delivery"));//配送类型
			hashMap.put("depart", (String)resJsonObject.get("depart"));//起飞时间
			hashMap.put("arrive", (String)resJsonObject.get("arrive"));//抵达时间
			hashMap.put("arrive_airport", (String)resJsonObject.get("arrive_airport"));
			hashMap.put("depart_airport", (String)resJsonObject.get("depart_airport"));
			hashMap.put("is_pay", (String)resJsonObject.get("is_pay"));
			listAry.add(hashMap);
				
		} catch (Exception e) {			
			e.printStackTrace();		
		}

		return listAry;
		
	}
	

}
