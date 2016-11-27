package model.hotel;

import java.io.Serializable;
import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
//�����ʹ��"AddValuesDescription","DRRRules" �������ֶΣ���ȥ��ע�⣬Ȼ����е��ԣ��ᷢ��Mapping����
@JsonIgnoreProperties(value = {"AddValuesDescription","DRRRules"})
public class RatePlan implements Serializable{

	private static final long serialVersionUID = 108L;
	public String RatePlanID;
	public String RatePlanCode;
	public String RatePlanName;
	public String GuestTypeCode;
	public Rate Rates;
	public ArrayList<GaranteeRull> GaranteeRules;
	public ArrayList<BookingRull> BookingRuless;
	public ArrayList<AddValue> AddValues;
	
	public ArrayList<AVDescription> AddValuesDescription;
	
	public ArrayList<DRRRule> DRRRules;
	public String getRatePlanID() {
		return RatePlanID;
	}
	public void setRatePlanID(String ratePlanID) {
		RatePlanID = ratePlanID;
	}
	public String getRatePlanCode() {
		return RatePlanCode;
	}
	public void setRatePlanCode(String ratePlanCode) {
		RatePlanCode = ratePlanCode;
	}
	public String getRatePlanName() {
		return RatePlanName;
	}
	public void setRatePlanName(String ratePlanName) {
		RatePlanName = ratePlanName;
	}
	public String getGuestTypeCode() {
		return GuestTypeCode;
	}
	public void setGuestTypeCode(String guestTypeCode) {
		GuestTypeCode = guestTypeCode;
	}
	public Rate getRates() {
		return Rates;
	}
	public void setRates(Rate rates) {
		Rates = rates;
	}
	public ArrayList<GaranteeRull> getGaranteeRules() {
		return GaranteeRules;
	}
	public void setGaranteeRules(ArrayList<GaranteeRull> garanteeRules) {
		GaranteeRules = garanteeRules;
	}
	public ArrayList<BookingRull> getBookingRuless() {
		return BookingRuless;
	}
	public void setBookingRuless(ArrayList<BookingRull> bookingRules) {
		BookingRuless = bookingRules;
	}
	public ArrayList<AddValue> getAddValues() {
		return AddValues;
	}
	public void setAddValues(ArrayList<AddValue> addValues) {
		AddValues = addValues;
	}
	public ArrayList<AVDescription> getAddValuesDescription() {
		return AddValuesDescription;
	}
	public void setAddValuesDescription(
			ArrayList<AVDescription> addValuesDescription) {
		AddValuesDescription = addValuesDescription;
	}


	public ArrayList<DRRRule> getDRRRules() {
		return DRRRules;
	}
	public void setDRRRules(ArrayList<DRRRule> dRRRules) {
		DRRRules = dRRRules;
	}
	
	
}
