package httpdelegate;

import java.util.HashMap;
import java.util.Iterator;

import order.pnr.yidao.OrderAirList;

import org.json.JSONArray;
import org.json.JSONObject;

public class OrderAirHttpConnection extends HttpConnUtil{

	OrderAirList delegate;
	private String citykey;
	public OrderAirHttpConnection(OrderAirList delegate){
		
		this.delegate = delegate;
	}
	
	
	/**
	 * 发送请求
	 * @param url 请求地址 
	 */
	public void sendGetConnection(final String url) {
		new Thread() {
			public void run() {
				sendGet(url);
			};
		}.start();
	}

	@Override
	void getParse(String parse) {
		if (delegate != null) {
			delegate.getOrderAirList(jsonParse(parse));
		}
	}
	
	
	@SuppressWarnings({ "unused", "unchecked" })
	private HashMap<String, Object> jsonParse(String parse) {
		System.out.println("carlist"+parse);
		JSONObject json = null;
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
	
		try {
			json = new JSONObject(parse);
			
			JSONObject resultJsonObject  = json.getJSONObject("result");
			
			if (resultJsonObject != null) {
				Iterator<String> inIterator = resultJsonObject.keys();
				while (inIterator.hasNext()) {
					HashMap<String, Object> hashMapTop = new HashMap<String, Object>();
					citykey = inIterator.next();
					System.out.println(citykey);
					JSONObject jsonValue = resultJsonObject.getJSONObject(citykey);
					hashMapTop.put("short", jsonValue.getString("short"));
					hashMapTop.put("en", jsonValue.getString("en"));
					hashMapTop.put("name", jsonValue.getString("name"));
					 JSONObject airJsonObject = null;
					 JSONArray airJsonArray = null;
					 
//					JSONObject terlistJsonObject = null;
//					JSONArray terlistArray = null;
					 
					 try {
						 	airJsonObject = jsonValue.getJSONObject("airport");
						 if (airJsonObject != null && airJsonObject.length() == 1) {
								Iterator<String>inIterator2 = airJsonObject.keys();
								String jmString = inIterator2.next();
								JSONObject jmJsonObject = airJsonObject.getJSONObject(jmString);
								hashMapTop.put("airname", jmJsonObject.getString("name"));
								System.out.println(jmJsonObject.getString("name"));
								hashMapTop.put("airlng", jmJsonObject.getString("lng"));
								hashMapTop.put("airlat", jmJsonObject.getString("lat"));
								hashMapTop.put("airkey", jmString);
								
//								terlistJsonObject = jmJsonObject.getJSONObject("terminal_list");
//								if (terlistJsonObject != null && terlistJsonObject.length() == 1) {
//									hashMapTop.put("airterminal", jmJsonObject.getJSONObject("terminal_list"));
//									
//								}
								
							}
					} catch (Exception e) {
						airJsonArray = jsonValue.getJSONArray("airport");
					}

					hashMap.put(citykey, hashMapTop);
				}
				
				
			}
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		
		}
		
		
		
		
		return hashMap;
	}
	
	
	

}
