package model.hotel;

import java.io.Serializable;

public class BookingRuleValue implements Serializable{

	private static final long serialVersionUID = 102L;
	public String StartDate;
	public String EndDate;
	public String StartHour;
	public String EndHour;
	public String DateType;
	public String ChangeRule;
	public String getStartDate() {
		return StartDate;
	}
	public void setStartDate(String startDate) {
		StartDate = startDate;
	}
	public String getEndDate() {
		return EndDate;
	}
	public void setEndDate(String endDate) {
		EndDate = endDate;
	}
	public String getStartHour() {
		return StartHour;
	}
	public void setStartHour(String startHour) {
		StartHour = startHour;
	}
	public String getEndHour() {
		return EndHour;
	}
	public void setEndHour(String endHour) {
		EndHour = endHour;
	}
	public String getDateType() {
		return DateType;
	}
	public void setDateType(String dateType) {
		DateType = dateType;
	}
	public String getChangeRule() {
		return ChangeRule;
	}
	public void setChangeRule(String changeRule) {
		ChangeRule = changeRule;
	}
	
	
}
