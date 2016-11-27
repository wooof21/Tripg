package httpdelegate;

import java.util.ArrayList;
import java.util.HashMap;

import order.pnr.yidao.Orderlist;

import org.json.JSONObject;

import city_info_list.CatTypeInfo;

public class OrderHttpConnection extends HttpConnUtil {

	private Orderlist delegate;
	
	public OrderHttpConnection (Orderlist delegate) {
		
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
			delegate.getOrderList(jsonParse(parse));
		}
	}
	
	@SuppressWarnings("unchecked")
	private HashMap<String, Object> jsonParse(String parse) {
		System.out.println("carlist"+parse);
		JSONObject json = null;
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		ArrayList<CatTypeInfo> list = new ArrayList<CatTypeInfo>();
		
		try {
			
			json = new JSONObject(parse);
			JSONObject orderNightjson = json.getJSONObject("result");
			HashMap<String, Object> hashMapJson = new HashMap<String, Object>();
			hashMapJson.put("code", json.getString("code"));
			hashMapJson.put("msg", json.getString("msg"));
			if (orderNightjson != null) {
				//OrderInFoData  orderInFoData = new OrderInFoData();
				String value = orderNightjson.getString("night_fee");
//				JSONObject jsonValue = orderNightjson.getJSONObject("night_fee");
//				orderInFoData.setOrderJsonObject(jsonValue);
				hashMapJson.put("night_fee", value);
				System.out.println(value);
			}
			
			hashMap.put("hashtop", hashMapJson);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return hashMap;
		
	}
	
}
