package tools.json;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ser.StdSerializerProvider;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class JsonUtils {

	// private static final Logger log = Logger.getLogger(JsonUtils.class);

	final static ObjectMapper objectMapper;

	/**
	 * 是否打印美观格式
	 */
	static boolean isPretty = true;

	static {
		StdSerializerProvider sp = new StdSerializerProvider();
		// sp.setNullValueSerializer(new NullSerializer());
		objectMapper = new ObjectMapper(null, sp, null).setVisibility(JsonMethod.FIELD, Visibility.ANY);
		objectMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}

	public static ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	/**
	 * JSON串转换为Java泛型对象，可以是各种类型，此方法最为强大。用法看测试用例。
	 * 
	 * @param <T>
	 * @param jsonString
	 *            JSON字符串
	 * @param tr
	 *            TypeReference,例如: new TypeReference< List<FamousUser> >(){}
	 * @return List对象列表
	 */
	@SuppressWarnings("unchecked")
	public static <T> T json2GenericObject(String jsonString,
			TypeReference<T> tr) {
		if (jsonString == null || "".equals(jsonString)
				|| "error".equals(jsonString)) {
			return null;
		} else {
			try {
				return (T) objectMapper.readValue(jsonString, tr);
			} catch (Exception e) {
				Log.e("json2GenericObject", e.toString());
				return null;
			}
		}
	}

	/**
	 * Java对象转Json字符串
	 * 
	 * @param object
	 *            Java对象，可以是对象，数组，List,Map等
	 * @return json 字符串
	 */
	@SuppressWarnings("deprecation")
	public static String toJson(Object object) {
		String jsonString = "";
		try {
			if (isPretty) {
				jsonString = objectMapper.defaultPrettyPrintingWriter()
						.writeValueAsString(object);
			} else {
				jsonString = objectMapper.writeValueAsString(object);
			}
		} catch (Exception e) {
			// log.warn("json error:" + e.getMessage());
		}
		return jsonString;

	}

	/**
	 * Json字符串转Java对象
	 * 
	 * @param jsonString
	 * @param c
	 * @return
	 */
	public static Object json2Object(String jsonString, Class<?> c) {

		if (jsonString == null || "".equals(jsonString)) {
			return "";
		} else {
			try {
				return objectMapper.readValue(jsonString, c);
			} catch (Exception e) {
				// log.warn("json error:" + e.getMessage());
			}

		}
		return "";
	}
	/**
	 * 机票订单接口
	 * 
	 * @param data
	 * @return
	 * @throws JSONException
	 */
	public String orderId(String data) throws JSONException {
		try {
			JSONObject json = new JSONObject(data);
			return json.getString("result");
		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		}
	}

	/**
	 * 4.0 去除UTF-8 BOM头
	 * 
	 * @param in
	 * @return
	 */
	public static String JSONTokener(String in) {
		// consume an optional byte order mark (BOM) if it exists
		if (in != null && in.startsWith("\ufeff")) {
			in = in.substring(1);
		}
		return in;
	}
}