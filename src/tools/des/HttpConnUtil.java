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
 * @作者：潘海江
 * @类名：HttpConnUtil
 * @包名：com.cvit.phj.Util
 * @描述：（工具类：连接webService获取数据）
 * @创建时间：2012-7-3 上午11:33:11
 */

public class HttpConnUtil {
	@SuppressWarnings("unused")
	private static final String TAG = HttpConnUtil.class.getSimpleName();
    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url) {
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            Log.e("HttpConnUtil.java", "发送GET请求出现异常BBB！");
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                Log.e("HttpConnUtil.java", "关闭流异常！");
            }
        }
        return result;
    }
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
	 * @return URL所代表远程资源的响应
	 */

	public static String getData(String url, String type){
		Log.e("url ------", url);
		String result = "";
		BufferedReader in = null;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// 建立实际的连接
			conn.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = conn.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(),
					type));
			String line;
			StringBuffer sb = new StringBuffer();
			while ((line = in.readLine()) != null) {
				sb.append(line + "\n");
			}
			result = sb.toString();
		} catch (UnsupportedEncodingException e) {
			Log.e("HttpConnUtil", "字符编码异常");
		} catch (IOException e) {
			Log.e("HttpConnUtil", "IOException");
		} 
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
			    Log.e("HttpConnUtil.java", "关闭流异常！");
			}
		}
		//System.out.println("结果:" + result);
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
				//System.out.println("结果:" + "error");
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
		    Log.e("HttpConnUtil.java", "发送GET请求异常！");
			//System.out.println("结果:" + "error");
			return "error";
		}
		//System.out.println("结果:" + result);
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
		    Log.e("HttpConnUtil.java", "发送GET请求异常！");
			return "error";// 出现网络连接异常
		}
		return responseMessage.toString();
	}

	// 下载图片
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
