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
		//�õ��м���ı��ڵ�
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
		Log.e("������� ������ʼ ", ""+jsonOrXmlStr);
		try {
			
			JSONObject myJsonObject = new JSONObject(jsonOrXmlStr);
			JSONObject resJsonObject = myJsonObject.getJSONObject("Result");
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			
			hashMap.put("order_acity", (String)resJsonObject.get("order_acity"));//�ִ����
			hashMap.put("order_scity", (String)resJsonObject.get("order_scity"));//��������
			hashMap.put("order_company", (String)resJsonObject.get("order_company"));//���չ�˾������
			hashMap.put("order_date", (String)resJsonObject.get("order_date"));//�������
			hashMap.put("order_jpice", (String)resJsonObject.get("order_jpice"));//Ʊ��
			hashMap.put("order_id", (String)resJsonObject.get("order_id"));//���
			hashMap.put("order_number", (String)resJsonObject.get("order_number"));//�����
			hashMap.put("order_member", (String)resJsonObject.get("order_member"));//��Ա���
			hashMap.put("order_people", (String)resJsonObject.get("order_people"));//�˻���
			hashMap.put("order_resid", (String)resJsonObject.get("order_resid"));//�������
			hashMap.put("order_tax", (String)resJsonObject.get("order_tax"));//����
			hashMap.put("order_safe", (String)resJsonObject.get("order_safe").toString());//����
			hashMap.put("order_yq", (String)resJsonObject.get("order_yq"));//ȼ��
			hashMap.put("ps_date", (String)resJsonObject.get("ps_date"));//ps����
			hashMap.put("telphone", (String)resJsonObject.get("telphone"));//��ϵ�绰
			hashMap.put("order_space", (String)resJsonObject.get("order_space"));//��λ
			hashMap.put("order_status", (String)resJsonObject.get("order_status"));//����״̬
			hashMap.put("order_cmt", (String)resJsonObject.get("order_cmt"));//�˸�ǩ
			hashMap.put("equip", (String)resJsonObject.get("equip"));//����
			hashMap.put("order_discount", (String)resJsonObject.get("order_discount"));//�ۿ���
			hashMap.put("realname", (String)resJsonObject.get("realname"));//
			hashMap.put("res_type", (String)resJsonObject.get("res_type"));//�˸�ǩ����
			hashMap.put("order_dtime", (String)resJsonObject.get("order_dtime"));//����ʱ��
			hashMap.put("delivery", (String)resJsonObject.get("delivery"));//��������
			hashMap.put("depart", (String)resJsonObject.get("depart"));//���ʱ��
			hashMap.put("arrive", (String)resJsonObject.get("arrive"));//�ִ�ʱ��
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
