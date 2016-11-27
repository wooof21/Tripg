package model.hotel;

import java.io.Serializable;
import java.util.ArrayList;

public class Rate implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 107L;
	public String TotalPrice;
	public String CurrencyCode;
	public String TaxesAmount;
	public ArrayList<SmallRate> rates;
	
	
	
	
	public String getTotalPrice() {
		return TotalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		TotalPrice = totalPrice;
	}
	public String getCurrencyCode() {
		return CurrencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		CurrencyCode = currencyCode;
	}
	public String getTaxesAmount() {
		return TaxesAmount;
	}
	public void setTaxesAmount(String taxesAmount) {
		TaxesAmount = taxesAmount;
	}
	public ArrayList<SmallRate> getRates() {
		return rates;
	}
	public void setRates(ArrayList<SmallRate> rates) {
		this.rates = rates;
	}
}
