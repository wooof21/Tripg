package model.hotel;

import java.io.Serializable;

public class BookingRull implements Serializable{
	private static final long serialVersionUID = 103L;
	public String BookingRuleTypeCode;
	public BookingRuleValue RuleValues;
	public String Description;
	public String RoomTypeId;
	public String getBookingRuleTypeCode() {
		return BookingRuleTypeCode;
	}
	public void setBookingRuleTypeCode(String bookingRuleTypeCode) {
		BookingRuleTypeCode = bookingRuleTypeCode;
	}
	public BookingRuleValue getRuleValues() {
		return RuleValues;
	}
	public void setRuleValues(BookingRuleValue ruleValues) {
		RuleValues = ruleValues;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getRoomTypeId() {
		return RoomTypeId;
	}
	public void setRoomTypeId(String roomTypeId) {
		RoomTypeId = roomTypeId;
	}
	
	
}
