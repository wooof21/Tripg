package model.hotel;

import java.io.Serializable;

public class AddValue implements Serializable{

	private static final long serialVersionUID = 100L;
	public String AddValueId;
	public String BusinessCode;
	public String IsInclude;
	public String IncludedCount;
	public String CurrencyCode;
	public String PriceDefaultOption;
	public String PriceNumber;
	public String IsAdd;
	public String SinglePriceOption;
	public String SinglePrice;
	public String Description;
	public String getAddValueId() {
		return AddValueId;
	}
	public void setAddValueId(String addValueId) {
		AddValueId = addValueId;
	}
	public String getBusinessCode() {
		return BusinessCode;
	}
	public void setBusinessCode(String businessCode) {
		BusinessCode = businessCode;
	}
	public String getIsInclude() {
		return IsInclude;
	}
	public void setIsInclude(String isInclude) {
		IsInclude = isInclude;
	}
	public String getIncludedCount() {
		return IncludedCount;
	}
	public void setIncludedCount(String includeCount) {
		IncludedCount = includeCount;
	}
	public String getCurrencyCode() {
		return CurrencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		CurrencyCode = currencyCode;
	}
	public String getPriceDefaultOption() {
		return PriceDefaultOption;
	}
	public void setPriceDefaultOption(String priceDefaultOption) {
		PriceDefaultOption = priceDefaultOption;
	}
	public String getPriceNumber() {
		return PriceNumber;
	}
	public void setPriceNumber(String priceNumber) {
		PriceNumber = priceNumber;
	}
	public String getIsAdd() {
		return IsAdd;
	}
	public void setIsAdd(String isAdd) {
		IsAdd = isAdd;
	}
	public String getSinglePriceOption() {
		return SinglePriceOption;
	}
	public void setSinglePriceOption(String singlePriceOption) {
		SinglePriceOption = singlePriceOption;
	}
	public String getSinglePrice() {
		return SinglePrice;
	}
	public void setSinglePrice(String singlePrice) {
		SinglePrice = singlePrice;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	
	
}
