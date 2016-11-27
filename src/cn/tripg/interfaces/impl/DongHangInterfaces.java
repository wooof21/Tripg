package cn.tripg.interfaces.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.client.utils.URLEncodedUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import cn.model.XMLResultModel;
import cn.model.XmlCityModel;
import cn.tripg.interfaces.BaseInterface;

public class DongHangInterfaces extends BaseInterface{

	
	private DocumentBuilder builder;

	public DongHangInterfaces(Context context, Handler handler) {
		super(context, handler);
		try {
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			this.builder = f.newDocumentBuilder();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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
		                    s.getBytes("GBK"));
	            return stringInputStream;
	        } catch (Exception e) {

	            e.printStackTrace();
	        }
	    }
	    return null;
	}
	
	@Override
	public Object parseJSONXML(String jsonOrXmlStr) {

		Log.e("xml-----", jsonOrXmlStr);
		List<XmlCityModel> results = new ArrayList<XmlCityModel>();
		XmlCityModel result = null;
		XMLResultModel s = new XMLResultModel();
		try {
			InputStream is = getStringInputStream(jsonOrXmlStr);
			Document doc = builder.parse(is);
			NodeList list = doc.getElementsByTagName("FlightInfo");
			NodeList fNote = doc.getElementsByTagName("FlightNote");
//			s.fNote = getSubElementTextContent((Element)fNote.item(0),"FlightNote");
//			Log.e("2.FlightNote", s.fNote);
			Element ele = null ;
			for(int i = 0 ; i < list.getLength() ; i ++){
				ele = (Element) list.item(i);
				result = new XmlCityModel();
				result.fNoString = getSubElementTextContent(ele,"FlightNo");
				result.fCompanyString = getSubElementTextContent(ele,"FlightCompany");
				result.fDepString = getSubElementTextContent(ele,"FlightDep");
				result.fArrString = getSubElementTextContent(ele,"FlightArr");
				result.fDepAirportString = getSubElementTextContent(ele,"FlightDepAirport");
				result.fArrAirportString = getSubElementTextContent(ele,"FlightArrAirport");
				result.fDeptimePlanString = getSubElementTextContent(ele,"FlightDeptimePlan");
				result.fArrtimePlanString = getSubElementTextContent(ele,"FlightArrtimePlan");
				result.fDeptimeString = getSubElementTextContent(ele,"FlightDeptime");
				result.fArrtimeString = getSubElementTextContent(ele,"FlightArrtime");
				result.fStateString = getSubElementTextContent(ele,"FlightState");
//				result.fNote = getSubElementTextContent(ele,"FlightNote");
//				Log.e("2.FlightNote", result.fNote);
				results.add(result);
				Log.e("2.flight", result.fCompanyString);
				
			}
			if(is != null)
				is.close();
		} catch (Exception e) {			 
			e.printStackTrace();			
		}
		return results;
	}

}
