package model.hotel;

import java.io.Serializable;

public class SmallRate implements Serializable{

	private static final long serialVersionUID = 104L;
	public String Date;
	public String CurrencyCode;
	public String InvStatusCode;
	public String RetailRate;
	public String MemberRate;
	public String AddBedRate;
	
	
	
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public String getCurrencyCode() {
		return CurrencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		CurrencyCode = currencyCode;
	}
	public String getInvStatusCode() {
		return InvStatusCode;
	}
	public void setInvStatusCode(String invStatusCode) {
		InvStatusCode = invStatusCode;
	}
	public String getRetailRate() {
		return RetailRate;
	}
	public void setRetailRate(String retailRate) {
		RetailRate = retailRate;
	}
	public String getMemberRate() {
		return MemberRate;
	}
	public void setMemberRate(String memberRate) {
		MemberRate = memberRate;
	}
	public String getAddBedRate() {
		return AddBedRate;
	}
	public void setAddBedRate(String addBedRate) {
		AddBedRate = addBedRate;
	}
}
