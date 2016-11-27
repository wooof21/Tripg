package tools.json;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import model.flight.G11Result;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DomParse {

	private DocumentBuilder builder;
	public DomParse(){
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
	                    s.getBytes());
	            return stringInputStream;
	        } catch (Exception e) {

	            e.printStackTrace();
	        }
	    }
	    return null;
	}
	public List<G11Result> doParse(String xmlStr) {
		List<G11Result> results = new ArrayList<G11Result>();
		G11Result result = null ;
		try {
			InputStream is = getStringInputStream(xmlStr);
			Document doc = builder.parse(is);
			NodeList list = doc.getElementsByTagName("INFO");
			Element ele = null ;
			for(int i = 0 ; i < list.getLength() ; i ++){
				ele = (Element) list.item(i);
				result = new G11Result();
				result.result = getSubElementTextContent(ele,"RESULT");
				result.msg = getSubElementTextContent(ele,"Msg");
				results.add(result);
			}
			if(is != null)
				is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}
}
