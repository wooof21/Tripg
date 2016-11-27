package model.flight;

import java.io.Serializable;
import java.util.ArrayList;

public class AVVo implements Serializable {

	/**
	 * 航班所有值
	 */
	private static final long serialVersionUID = 1L;
	public String Code;// 成功标识
	public String Message;// 提示信息
	public ArrayList<FlightsVo> Flights;// 航班集
	public String Error;// 错误标识
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
	public ArrayList<FlightsVo> getFlights() {
		return Flights;
	}
	public void setFlights(ArrayList<FlightsVo> flights) {
		Flights = flights;
	}
	public String getError() {
		return Error;
	}
	public void setError(String error) {
		Error = error;
	}
}

