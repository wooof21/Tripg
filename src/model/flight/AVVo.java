package model.flight;

import java.io.Serializable;
import java.util.ArrayList;

public class AVVo implements Serializable {

	/**
	 * ��������ֵ
	 */
	private static final long serialVersionUID = 1L;
	public String Code;// �ɹ���ʶ
	public String Message;// ��ʾ��Ϣ
	public ArrayList<FlightsVo> Flights;// ���༯
	public String Error;// �����ʶ
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

