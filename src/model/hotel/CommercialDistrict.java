package model.hotel;

import java.io.Serializable;
import java.util.ArrayList;

public class CommercialDistrict implements Serializable{
	private static final long serialVersionUID = 130L;
	public String Code;
	public String Message;
	public ArrayList<CDResults> Result;
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
	public ArrayList<CDResults> getResult() {
		return Result;
	}
	public void setResult(ArrayList<CDResults> result) {
		Result = result;
	}
	
	
	
}
