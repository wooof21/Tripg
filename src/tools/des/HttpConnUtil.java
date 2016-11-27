package tools.des;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import android.util.Log;

/**
 * @���ߣ��˺���
 * @������HttpConnUtil
 * @������com.cvit.phj.Util
 * @�������������ࣺ����webService��ȡ���ݣ�
 * @����ʱ�䣺2012-7-3 ����11:33:11
 */

public class HttpConnUtil {
	@SuppressWarnings("unused")
	private static final String TAG = HttpConnUtil.class.getSimpleName();
    /**
     * ��ָ��URL����GET����������
     * 
     * @param url
     *            ���������URL
     * @param param
     *            ����������������Ӧ���� name1=value1&name2=value2 ����ʽ��
     * @return URL ������Զ����Դ����Ӧ���
     */
    public static String sendGet(String url) {
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            // �򿪺�URL֮�������
            URLConnection connection = realUrl.openConnection();
            // ����ͨ�õ���������
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // ����ʵ�ʵ�����
            connection.connect();
            // ��ȡ������Ӧͷ�ֶ�
            Map<String, List<String>> map = connection.getHeaderFields();
            // �������е���Ӧͷ�ֶ�
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // ���� BufferedReader����������ȡURL����Ӧ
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            Log.e("HttpConnUtil.java", "����GET��������쳣BBB��");
        }
        // ʹ��finally�����ر�������
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                Log.e("HttpConnUtil.java", "�ر����쳣��");
            }
        }
        return result;
    }
	/**
	 * ��ָ��URL����GET����������
	 * 
	 * @param url
	 *            ���������URL
	 * @param param
	 *            ����������������Ӧ����name1=value1&name2=value2����ʽ��
	 * @return URL������Զ����Դ����Ӧ
	 */

	public static String getData(String url, String type){
		Log.e("url ------", url);
		String result = "";
		BufferedReader in = null;
		try {
			URL realUrl = new URL(url);
			// �򿪺�URL֮�������
			URLConnection conn = realUrl.openConnection();
			// ����ͨ�õ���������
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// ����ʵ�ʵ�����
			conn.connect();
			// ��ȡ������Ӧͷ�ֶ�
			Map<String, List<String>> map = conn.getHeaderFields();
			// �������е���Ӧͷ�ֶ�
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// ����BufferedReader����������ȡURL����Ӧ
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(),
					type));
			String line;
			StringBuffer sb = new StringBuffer();
			while ((line = in.readLine()) != null) {
				sb.append(line + "\n");
			}
			result = sb.toString();
		} catch (UnsupportedEncodingException e) {
			Log.e("HttpConnUtil", "�ַ������쳣");
		} catch (IOException e) {
			Log.e("HttpConnUtil", "IOException");
		} 
		// ʹ��finally�����ر�������
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
			    Log.e("HttpConnUtil.java", "�ر����쳣��");
			}
		}
		//System.out.println("���:" + result);
		return result;
	}

	public static String getData1(String URL, String type) {
		//System.out.println("URL:" + URL);
		String result = "";
		try {
			URL url = new URL(URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setConnectTimeout(1000 * 30);
			conn.setReadTimeout(1000 * 3);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() != 200) {
				//System.out.println("���:" + "error");
				return "error";
			}
			conn.connect();
			StringBuffer sb = null;
			InputStream is = conn.getInputStream();
			System.setProperty("http.keepAlive", "false");
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					type));
			sb = new StringBuffer();
			while (br.ready()) {
				String data = br.readLine();
				//System.out.println(data);
				sb.append(data + "\n");
			}
			result = sb.toString();
			is.close();
			conn.disconnect();
		} catch (Exception ee) {
		    Log.e("HttpConnUtil.java", "����GET�����쳣��");
			//System.out.println("���:" + "error");
			return "error";
		}
		//System.out.println("���:" + result);
		return result;
	}

	public static String submitPost(String url, String paramContent, String type) {
		StringBuffer responseMessage = null;
		java.net.URLConnection connection = null;
		java.net.URL reqUrl = null;
		OutputStreamWriter reqOut = null;
		InputStream in = null;
		BufferedReader br = null;
		String param = paramContent;
		try {
			responseMessage = new StringBuffer();
			reqUrl = new java.net.URL(url);
			connection = reqUrl.openConnection();
			// connection.setDoOutput(true);
			reqOut = new OutputStreamWriter(connection.getOutputStream(), type);
			connection.setConnectTimeout(10000);
			reqOut.write(param);
			reqOut.flush();
			int charCount = -1;
			in = connection.getInputStream();
			System.setProperty("http.keepAlive", "false");
			br = new BufferedReader(new InputStreamReader(in, type));
			while ((charCount = br.read()) != -1) {
				responseMessage.append((char) charCount);
			}
			in.close();
			reqOut.close();
		} catch (Exception ex) {
		    Log.e("HttpConnUtil.java", "����GET�����쳣��");
			return "error";// �������������쳣
		}
		return responseMessage.toString();
	}

	// ����ͼƬ
	public static InputStream getStreamFromURL(String imageURL) {
		InputStream in = null;
		try {
			URL url = new URL(imageURL);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			in = connection.getInputStream();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return in;
	}

}
