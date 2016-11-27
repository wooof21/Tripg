package httpdelegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONObject;

import city_info_list.CatTypeInfo;

import com.car.yidao.CarList;

public class CarHttpConnection extends HttpConnUtil {

	private CarList delegate;
	public CarHttpConnection(CarList delegate) {
		
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
			delegate.getCarList (jsonParse(parse));
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
		
				JSONObject carkey = json.getJSONObject("carType");
				HashMap<String, Object> hashMapTop = new HashMap<String, Object>();
	
					hashMapTop.put("code", json.getString("code"));
					hashMapTop.put("msg", json.getString("msg"));
					hashMapTop.put("city", json.getString("city"));
					hashMapTop.put("product", json.getString("product"));
					
					if (carkey != null) {
					Iterator<String> inIterator1 = carkey.keys();
					while (inIterator1.hasNext()) {
						CatTypeInfo catTypeInfo = new CatTypeInfo();
						String hashkey = inIterator1.next();
						String value = carkey.getString(hashkey);
						JSONObject jsonValue = carkey.getJSONObject(hashkey);
						catTypeInfo.setCarType(jsonValue);
						System.out.println(value);
//						catTypeInfo.getCarType().put(hashkey, value);
						
						list.add(catTypeInfo);
					}
			}
			hashMap.put("hashtop", hashMapTop);
			hashMap.put("array", list);
			
			
		} catch (Exception e) {

			e.printStackTrace();
			
		}
		
		return hashMap;

	}


	
}
