package model.flight;

import java.util.ArrayList;

public class LowPrices {

	public ArrayList<LowPrice> Result;
	public String Code;// �ɹ���ʶ
	public String Message;// ��ʾ��Ϣ
	public String Error;
	
	public String getError() {
		return Error;
	}

	public void setError(String error) {
		Error = error;
	}

	public ArrayList<LowPrice> getResult() {
		return Result;
	}

	public void setResult(ArrayList<LowPrice> result) {
		Result = result;
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
