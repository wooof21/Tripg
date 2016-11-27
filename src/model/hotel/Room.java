package model.hotel;

import java.io.Serializable;
import java.util.ArrayList;

public class Room implements Serializable{

	private static final long serialVersionUID = 120L;
	public String RoomName;
	public String RoomTypeId;
	public String RoomInvStatusCode;
	public String Area;
	public String Floor;
	public String HasBroadBand;
	public String HasBroadBandFee;
	public String BedType;
	public String BedDescription;
	public ArrayList<RatePlan> RatePlans;
	
	
	
	
	public String getRoomName() {
		return RoomName;
	}
	public void setRoomName(String roomName) {
		RoomName = roomName;
	}
	public String getRoomTypeId() {
		return RoomTypeId;
	}
	public void setRoomTypeId(String roomTypeId) {
		RoomTypeId = roomTypeId;
	}
	public String getRoomInvStatusCode() {
		return RoomInvStatusCode;
	}
	public void setRoomInvStatusCode(String roomInvStatusCode) {
		RoomInvStatusCode = roomInvStatusCode;
	}
	public String getArea() {
		return Area;
	}
	public void setArea(String area) {
		Area = area;
	}
	public String getFloor() {
		return Floor;
	}
	public void setFloor(String floor) {
		Floor = floor;
	}
	public String getHasBroadBand() {
		return HasBroadBand;
	}
	public void setHasBroadBand(String hasBroadBand) {
		HasBroadBand = hasBroadBand;
	}
	public String getHasBroadBandFee() {
		return HasBroadBandFee;
	}
	public void setHasBroadBandFee(String hasBroadBandFee) {
		HasBroadBandFee = hasBroadBandFee;
	}
	public String getBedType() {
		return BedType;
	}
	public void setBedType(String bedType) {
		BedType = bedType;
	}
	public String getBedDescription() {
		return BedDescription;
	}
	public void setBedDescription(String bedDescription) {
		BedDescription = bedDescription;
	}
	public ArrayList<RatePlan> getRatePlans() {
		return RatePlans;
	}
	public void setRatePlans(ArrayList<RatePlan> ratePlans) {
		RatePlans = ratePlans;
	}
}
