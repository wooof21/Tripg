package tools.des;

import java.security.MessageDigest;

/**
 * @作者：潘海江
 * @类名：MD5
 * @包名：com.ctrip.Util
 * @描述：（此处用一句话来描述此类的作用）
 * @创建时间：2012-7-3 下午3:47:41
 */
public class MD5 {

	public static String getMD5(String MD5) {
		StringBuffer sb = new StringBuffer();
		String part = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] md5 = md.digest(MD5.getBytes("GBK"));
			for (int i = 0; i < md5.length; i++) {
				part = Integer.toHexString(md5[i] & 0xFF);
				if (part.length() == 1) {
					part = "0" + part;
				}
				sb.append(part);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * des加密串
	 * 
	 * @return 加密串
	 */

	public static String appendData(String param1, String param2) {
		String sign = "";
		try {
			sign = DesCodeUtils.encode(Api.key, param1 + param2);
			sign = sign.replace("=", "@");
			sign = sign.replace("+", "-");
			sign = sign.replace("/", "_");
			sign = DesCodeUtils.encode(Api.key,"android#depCity|flightDate#" + sign);
			sign = sign.replace("=", "@");
			sign = sign.replace("+", "-");
			sign = sign.replace("/", "_");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sign;
	}
	/**
	 * 客人信息
	 * @param passenger
	 * @return
	 */
	public static String appendData(String passenger) {
		String sign = "";
		try {
			sign = DesCodeUtils.encode(Api.key, passenger);
			sign = sign.replace("=", "@");
			sign = sign.replace("+", "-");
			sign = sign.replace("/", "_");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sign;
	}
	/**
	 * 日期
	 * @param passenger
	 * @return
	 */
	public static String appendDatas(String date) {
		String sign = "";
		try {
			sign = DesCodeUtils.encode(Api.key, date);
			sign = sign.replace("=", "@");
			sign = sign.replace("+", "-");
			sign = sign.replace("/", "_");
			sign = DesCodeUtils.encode(Api.key,"android#carrier#" + sign);
			sign = sign.replace("=", "@");
			sign = sign.replace("+", "-");
			sign = sign.replace("/", "_");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sign;
	}
}
