package model.hotel;

import java.io.Serializable;
import java.util.ArrayList;

public class Hotel implements Serializable{

	private static final long serialVersionUID = 105L;
	public String HotelName;
	public String HotelId;
	public String HotelSource;
	public String StarCode;
	public String HotelAddress;
	public String BusinessZone;
	public String Phone;
	public String Fax;
	public String Lon;
	public String Lat;
	public String HotelInvStatusCode;
	public String LowestPrice;
	public ArrayList<Room> Rooms;
	//’’∆¨
	public String OutdoorSceneImage;
	
	

	
	public String getHotelName() {
		return HotelName;
	}
	public void setHotelName(String hotelName) {
		HotelName = hotelName;
	}
	public String getHotelId() {
		return HotelId;
	}
	public void setHotelId(String hotelId) {
		HotelId = hotelId;
	}
	public String getHotelSource() {
		return HotelSource;
	}
	public void setHotelSource(String hotelSource) {
		HotelSource = hotelSource;
	}
	public String getStarCode() {
		return StarCode;
	}
	public void setStarCode(String starCode) {
		StarCode = starCode;
	}
	public String getHotelAddress() {
		return HotelAddress;
	}
	public void setHotelAddress(String hotelAddress) {
		HotelAddress = hotelAddress;
	}
	public String getBusinessZone() {
		return BusinessZone;
	}
	public void setBusinessZone(String businessZone) {
		BusinessZone = businessZone;
	}
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public String getFax() {
		return Fax;
	}
	public void setFax(String fax) {
		Fax = fax;
	}
	public String getLon() {
		return Lon;
	}
	public void setLon(String lon) {
		Lon = lon;
	}
	public String getLat() {
		return Lat;
	}
	public void setLat(String lat) {
		Lat = lat;
	}
	public String getHotelInvStatusCode() {
		return HotelInvStatusCode;
	}
	public void setHotelInvStatusCode(String hotelInvStatusCode) {
		HotelInvStatusCode = hotelInvStatusCode;
	}
	public String getLowestPrice() {
		return LowestPrice;
	}
	public void setLowestPrice(String lowestPrice) {
		LowestPrice = lowestPrice;
	}
	public ArrayList<Room> getRooms() {
		return Rooms;
	}
	public void setRooms(ArrayList<Room> rooms) {
		Rooms = rooms;
	}
	public String getOutdoorSceneImage() {
		return OutdoorSceneImage;
	}
	public void setOutdoorSceneImage(String outdoorSceneImage) {
		OutdoorSceneImage = outdoorSceneImage;
	}
}
