package tools.des;

public class Api {
	
	private String URL = "http://flightapi.tripglobal.cn:8080/";
	private String Url = "http://139.210.99.29:83";
	public static String key = "11119688";
	private String hotelURL = "http://139.210.99.29:83/yuanda_hotel_show/index.php/welcome/";
	//private String dURL = "http://www.variflight.com/datainterface/Currentinterface.asp";
	//private String payURL = "http://139.210.99.29:99/alipy_interface/";
	public static String vUrl = "http://139.210.99.29:8090/android.aspx";

	/**
	 * 8个参数
	 * 
	 */
	public String doGetData(String param1, String param2, String param3,
			String param4, String param5, String param6, String param7,
			String param8) {
		String responseData = "";
		responseData = HttpConnUtil.getData(URL + param1 + param2 + param3
				+ param4 + param5 + param6 + param7 + param8, "UTF-8");
		return responseData;
	}

	/**
	 * 9个参数
	 * 
	 */
	public String doGetData(String param1, String param2, String param3,
			String param4, String param5, String param6, String param7,
			String param8, String param9) {
		String responseData = "";
		String plus = URL + param1 + param2 + param3 + param4 + param5 + param6
				+ param7 + param8 + param9;
		responseData = HttpConnUtil.getData(plus.replace("\n", ""), "UTF-8");
		return responseData;
	}
	public String doGetRequestURL(String param1, String param2, String param3,
			String param4, String param5, String param6, String param7,
			String param8, String param9) {
		String plus = URL + param1 + param2 + param3 + param4 + param5 + param6
				+ param7 + param8 + param9;
		return plus;
	}
	public String doGetData(String plus) {
		String responseData = "";
		responseData = HttpConnUtil.getData(plus.replace("\n", ""), "UTF-8");
		return responseData;
	}
/**
 * 10个参数
 * */
	
	public String doGetTENRequestURL(String param1, String param2, String param3,
			String param4, String param5, String param6, String param7,
			String param8, String param9,String param10) {
		String plus = URL + param1 + param2 + param3 + param4 + param5 + param6
				+ param7 + param8 + param9 + param10;
		return plus;
	}
	

	/**
	 * 13个参数
	 * 
	 */
	public String doGetHotelData(String param0, String param1, String param2,
			String param3, String param4, String param5, String param6,
			String param7, String param8, String param9, String param10,
			String param11, String param12) {
		String responseData = "";
		String plus = hotelURL + param0 + param1 + param2 + param3 + param4
				+ param5 + param6 + param7 + param8 + param9 + param10
				+ param11 + param12;
		responseData = HttpConnUtil.getData(plus, "UTF-8");
		return responseData;
	}

	/**
	 * 14个参数
	 * 
	 */
	public String doGetHotelData(String param0, String param1, String param2,
			String param3, String param4, String param5, String param6,
			String param7, String param8, String param9, String param10,
			String param11, String param12, String param13, String param14) {
		String responseData = "";
		String plus = hotelURL + param0 + param1 + param2 + param3 + param4
				+ param5 + param6 + param7 + param8 + param9 + param10
				+ param11 + param12 + param13 + param14;
		responseData = HttpConnUtil.getData(plus, "UTF-8");
		return responseData;
	}

	/**
	 * 12个参数
	 * 
	 */
	public String doGetHotelData(String param0, String param1, String param2,
			String param3, String param4, String param5, String param6,
			String param7, String param8, String param9, String param10,
			String param11) {
		String responseData = "";
		String plus = hotelURL + param0 + param1 + param2 + param3 + param4
				+ param5 + param6 + param7 + param8 + param9 + param10
				+ param11;
		responseData = HttpConnUtil.getData(plus, "UTF-8");
		return responseData;
	}


	/**
	 * 4个参数
	 * 
	 */
	public String doGetHotelData(String param1, String param2, String param3,
			String param4) {
		String responseData = "";
		responseData = HttpConnUtil.getData(hotelURL + param1 + param2 + param3
				+ param4, "utf-8");
		return responseData;
	}






	/**
	 * 获取订单号
	 * 
	 * @param pnr
	 * @return
	 */
	public String insertSqls(String pnr) {
		String data = "";
		try {
			data = HttpConnUtil.getData(Url + "/interface/Get_Order_Resid.php"
					+ pnr, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 机票订单查询 4个参数
	 * 
	 * @param pnr
	 * @return
	 */
	public String inquiryData(String param1, String param2) {
		String data = "";
		try {
			data = HttpConnUtil.getData(Url
					+ "/interface_dept_show/phone_order_select.php?" + param1
					+ param2, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	/**
	 * 8个参数
	 * 
	 */
	public String doGetDatas(String param1, String param2, String param3,
			String param4, String param5, String param6,String param7,String param8) {
		String responseData = "";
		responseData = HttpConnUtil.getData(Url + param1 + param2 + param3
				+ param4 + param5 + param6+param7+param8, "UTF-8");
		return responseData;
	}
	
	/**
	 * 3个参数 找回密码
	 * 
	 */
	public String doGetDatas(String param1, String param2, String param3) {
		String responseData = "";
		responseData = HttpConnUtil.getData(Url + param1 + param2 + param3,"UTF-8");
		return responseData;
	}
}
