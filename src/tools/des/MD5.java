package tools.des;

import java.security.MessageDigest;

/**
 * @���ߣ��˺���
 * @������MD5
 * @������com.ctrip.Util
 * @���������˴���һ�仰��������������ã�
 * @����ʱ�䣺2012-7-3 ����3:47:41
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
	 * des���ܴ�
	 * 
	 * @return ���ܴ�
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
	 * ������Ϣ
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
	 * ����
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
