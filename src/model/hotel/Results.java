package model.hotel;

import java.io.Serializable;
import java.util.ArrayList;

public class Results implements Serializable{

	private static final long serialVersionUID = 109L;
	public String PageIndex;
	public String PageSize;
	public String HotelCount;
	public String PageCount;
	public ArrayList<Hotel> Hotels;
	
	
	
	
	public String getPageIndex() {
		return PageIndex;
	}
	public void setPageIndex(String pageIndex) {
		PageIndex = pageIndex;
	}
	public String getPageSize() {
		return PageSize;
	}
	public void setPageSize(String pageSize) {
		PageSize = pageSize;
	}
	public String getHotelCount() {
		return HotelCount;
	}
	public void setHotelCount(String hotelCount) {
		HotelCount = hotelCount;
	}
	public String getPageCount() {
		return PageCount;
	}
	public void setPageCount(String pageCount) {
		PageCount = pageCount;
	}
	public ArrayList<Hotel> getHotels() {
		return Hotels;
	}
	public void setHotels(ArrayList<Hotel> hotels) {
		Hotels = hotels;
	}

}
