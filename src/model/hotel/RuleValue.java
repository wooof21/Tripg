package model.hotel;

import java.io.Serializable;

public class RuleValue implements Serializable{

	private static final long serialVersionUID = 121L;
	public String StartDate;
	public String EndDate;
	public String DateType;
	public String WeekSet;
	public String IsArriveTimeVouch;
	public String ArriveStatTime;
	public String ArriveEndTime;
	public String IsTomorrow;
	public String IsRoomCountVouch;
	public String RoomCount;
	public String VouchMoneyType;
	public String IsChange;
	public String ChangeRule;
	public String DayNum;
	public String TimeNum;
	public String HourNum;
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
	public String getDateType() {
		return DateType;
	}
	public void setDateType(String dateType) {
		DateType = dateType;
	}
	public String getWeekSet() {
		return WeekSet;
	}
	public void setWeekSet(String weekSet) {
		WeekSet = weekSet;
	}
	public String getIsArriveTimeVouch() {
		return IsArriveTimeVouch;
	}
	public void setIsArriveTimeVouch(String isArriveTimeVouch) {
		IsArriveTimeVouch = isArriveTimeVouch;
	}
	public String getArriveStatTime() {
		return ArriveStatTime;
	}
	public void setArriveStatTime(String arriveStatTime) {
		ArriveStatTime = arriveStatTime;
	}
	public String getArriveEndTime() {
		return ArriveEndTime;
	}
	public void setArriveEndTime(String arriveEndTime) {
		ArriveEndTime = arriveEndTime;
	}
	public String getIsTomorrow() {
		return IsTomorrow;
	}
	public void setIsTomorrow(String isTomorrow) {
		IsTomorrow = isTomorrow;
	}
	public String getIsRoomCountVouch() {
		return IsRoomCountVouch;
	}
	public void setIsRoomCountVouch(String isRoomCountVouch) {
		IsRoomCountVouch = isRoomCountVouch;
	}
	public String getRoomCount() {
		return RoomCount;
	}
	public void setRoomCount(String roomCount) {
		RoomCount = roomCount;
	}
	public String getVouchMoneyType() {
		return VouchMoneyType;
	}
	public void setVouchMoneyType(String vouchMoneyType) {
		VouchMoneyType = vouchMoneyType;
	}
	public String getIsChange() {
		return IsChange;
	}
	public void setIsChange(String isChange) {
		IsChange = isChange;
	}
	public String getChangeRule() {
		return ChangeRule;
	}
	public void setChangeRule(String changeRule) {
		ChangeRule = changeRule;
	}
	public String getDayNum() {
		return DayNum;
	}
	public void setDayNum(String dayNum) {
		DayNum = dayNum;
	}
	public String getTimeNum() {
		return TimeNum;
	}
	public void setTimeNum(String timeNum) {
		TimeNum = timeNum;
	}
	public String getHourNum() {
		return HourNum;
	}
	public void setHourNum(String hournum) {
		HourNum = hournum;
	}
	
	
}
