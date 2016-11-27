package httpdelegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import city_info_list.CarInfo;
import city_info_list.CityList;

/**
 * 获取城市信息
 * @author   http代理  json解析类
 */
public class CityHttpConnection extends HttpConnUtil {
	//设置代理方法 
	private CityList delegate;

	/**
	 * 构造方法
	 * @param delegate 获取城市接口实例 
	 * 在初始化的时候设置代理
	 */
	public CityHttpConnection(CityList delegate) {
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
			//启动代理方法将解析之后的数据传回主函数进行操作  
			delegate.getCityList(jsonParse(parse));
		}
	}

	/**
	 * 解析JSON
	 * @param parse JSON字符串
	 * @return 数组   ArrayList<CarInfo> 是返回数组  carinfo是数据类 
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<CarInfo> jsonParse(String parse) {
		JSONObject json = null;
		ArrayList<CarInfo> list = new ArrayList<CarInfo>();
		try {
			json = new JSONObject(parse);
			Iterator<String> iterator = json.keys();

			while (iterator.hasNext()) {
				String keyString = iterator.next();

				JSONObject key = null;
				if (!keyString.equals("code") && !keyString.equals("msg")) {
					key = json.getJSONObject(keyString);
				}

				if (key != null) {
					CarInfo carInfo = new CarInfo();
					carInfo.setShortString(key.getString("short"));
					carInfo.setEn(key.getString("en"));
					carInfo.setName(key.getString("name"));
					JSONObject product_list = key.getJSONObject("product_list");

					if (product_list != null) {
						Iterator<String> product = product_list.keys();
						HashMap<String, Object> hashMap = new HashMap<String, Object>();
						while (product.hasNext()) {
							String hashKey = product.next();
							String value = product_list.getString(hashKey);
							hashMap.put(hashKey, value);
						}
						carInfo.setProductList(hashMap);
					}

					list.add(carInfo);
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return list;
	}
}
