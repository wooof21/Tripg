package tools.des;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import android.annotation.SuppressLint;

@SuppressLint("NewApi")
public class DesCodeUtils {
	public static final String ALGORITHM_DES = "DES/ECB/PKCS7Padding";

	/**
	 * DES�㷨������
	 * 
	 * @param data
	 *            �������ַ���
	 * @param key
	 *            ����˽Կ�����Ȳ��ܹ�С��8λ
	 * @return ���ܺ���ֽ����飬һ����Base64����ʹ��
	 * @throws CryptException
	 *             �쳣
	 */
	public static String encode(String key, String data) throws Exception {
		return encode(key, data.getBytes());
	}

	/**
	 * DES�㷨������
	 * 
	 * @param data
	 *            �������ַ���
	 * @param key
	 *            ����˽Կ�����Ȳ��ܹ�С��8λ
	 * @return ���ܺ���ֽ����飬һ����Base64����ʹ��
	 * @throws CryptException
	 *             �쳣
	 */
	public static String encode(String key, byte[] data) throws Exception {
		try {
			DESKeySpec dks = new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// key�ĳ��Ȳ��ܹ�С��8λ�ֽ�
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			//IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
			//AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);

			byte[] bytes = cipher.doFinal(data);

			return Base64.encode(bytes);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	/**
	 * DES�㷨������
	 * 
	 * @param data
	 *            �������ַ���
	 * @param key
	 *            ����˽Կ�����Ȳ��ܹ�С��8λ
	 * @return ���ܺ���ֽ�����
	 * @throws Exception
	 *             �쳣
	 */
	public static byte[] decode(String key, byte[] data) throws Exception {
		try {
			DESKeySpec dks = new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// key�ĳ��Ȳ��ܹ�С��8λ�ֽ�
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			//IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
			//AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return cipher.doFinal(data);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	/**
	 * ��ȡ������ֵ
	 * 
	 * @param key
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@SuppressLint("NewApi")
	public static String decodeValue(String key, String data) {
		byte[] datas;
		String value = null;
		try {
			datas = decode(key, Base64.decode(data));
			value = new String(datas);
		} catch (Exception e) {
			value = "";
		}
		return value;
	}
}