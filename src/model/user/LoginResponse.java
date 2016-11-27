package model.user;

import java.io.Serializable;

public class LoginResponse implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 10L;
    public String Code;
    public String Message;
    public String compayidString;
    public String countryString;
    
    
    
	public String getCompayidString() {
		return compayidString;
	}
	public void setCompayidString(String compayidString) {
		this.compayidString = compayidString;
	}
	public String getCountryString() {
		return countryString;
	}
	public void setCountryString(String countryString) {
		this.countryString = countryString;
	}
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
	
	
    
    
}
