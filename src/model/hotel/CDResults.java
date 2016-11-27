package model.hotel;

import java.io.Serializable;

public class CDResults implements Serializable{

	private static final long serialVersionUID = 132L;
	public String LocationId;
	public String CityId;
	public String Name;
	public String Type;
	public String getLocationId() {
		return LocationId;
	}
	public void setLocationId(String locationId) {
		LocationId = locationId;
	}
	public String getCityId() {
		return CityId;
	}
	public void setCityId(String cityId) {
		CityId = cityId;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
}
