package city_info_list;

import java.util.HashMap;

public class CarInfo {
	private String shortString;//
	private String en;
	private String name;
	private HashMap<String, Object> productList;

	public CarInfo() {
		productList = new HashMap<String, Object>();
	}

	public String getShortString() {
		return shortString;
	}

	public void setShortString(String shortString) {
		this.shortString = shortString;
	}

	public String getEn() {
		return en;
	}

	public void setEn(String en) {
		this.en = en;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<String, Object> getProductList() {
		return productList;
	}

	public void setProductList(HashMap<String, Object> productList) {
		this.productList = productList;
	}

	@Override
	public String toString() {
		return "CarInfo [shortString=" + shortString + ", en=" + en + ", name="
				+ name + ", productList=" + productList + "]";
	}
}
