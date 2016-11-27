package model.hotel;

import java.io.Serializable;

public class HOTELS implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 106L;
	public String Code;
	public String Message;
	public Results Result;
	
	
	

	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public Results getResult() {
		return Result;
	}
	public void setResult(Results result) {
		Result = result;
	}
	
}
