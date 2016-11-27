package httpdelegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import city_info_list.CarInfo;
import city_info_list.CityList;

/**
 * ��ȡ������Ϣ
 * @author   http����  json������
 */
public class CityHttpConnection extends HttpConnUtil {
	//���ô����� 
	private CityList delegate;

	/**
	 * ���췽��
	 * @param delegate ��ȡ���нӿ�ʵ�� 
	 * �ڳ�ʼ����ʱ�����ô���
	 */
	public CityHttpConnection(CityList delegate) {
		this.delegate = delegate;
	}

	/**
	 * ��������
	 * @param url �����ַ 
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
			//����������������֮������ݴ������������в���  
			delegate.getCityList(jsonParse(parse));
		}
	}

	/**
	 * ����JSON
	 * @param parse JSON�ַ���
	 * @return ����   ArrayList<CarInfo> �Ƿ�������  carinfo�������� 
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
