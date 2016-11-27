package order.pnr.yidao;

import org.json.JSONObject;

public class OrderAirInfo {

	private String codeString;
	private String msgString;
	private JSONObject jsonObject;
	
	public OrderAirInfo(){
		
		
	}

	public String getCode() {
		return codeString;
	}

	public void setCode(String codeString) {
		this.codeString = codeString;
	}

	public String getMsg() {
		return msgString;
	}

	public void setMsg(String msgString) {
		this.msgString = msgString;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}
	
	
}
