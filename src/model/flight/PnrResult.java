package model.flight;

import java.io.Serializable;

public class PnrResult implements Serializable{

	private static final long serialVersionUID = 8L;
	//{"Code":"E2000","Message":"Ô¤¶¨³É¹¦","OfficeCode":"CGQ182","Pnr":"JTS6HD"}
	public String Code;
	public String Message;
	public String OfficeCode;
	public String Pnr;
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
	public String getOfficeCode() {
		return OfficeCode;
	}
	public void setOfficeCode(String officeCode) {
		OfficeCode = officeCode;
	}
	public String getPnr() {
		return Pnr;
	}
	public void setPnr(String pnr) {
		Pnr = pnr;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
