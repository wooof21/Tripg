package city_info_list;

import java.util.HashMap;

import org.json.JSONObject;

public class CatTypeInfo {

	private String code;
	private String msgString;
	private String cityString;
	private String productString;
	private JSONObject carType;
	
	public CatTypeInfo(){
		
		//carType = new JSONObject();
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsgString() {
		return msgString;
	}
	public void setMsgString(String msgString) {
		this.msgString = msgString;
	}
	public String getCityString() {
		return cityString;
	}
	public void setCityString(String cityString) {
		this.cityString = cityString;
	}
	public String getProductString() {
		return productString;
	}
	public void setProductString(String productString) {
		this.productString = productString;
	}
	public JSONObject getCarType() {
		return carType;
	}
	public void setCarType(JSONObject carType) {
		this.carType = carType;
	}
	
	
	
}
