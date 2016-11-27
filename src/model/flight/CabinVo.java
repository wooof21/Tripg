package model.flight;

import java.io.Serializable;

public class CabinVo implements Serializable {

	/**
	 * G11接口
	 * 
	 * @param 仓位信息
	 */
	private static final long serialVersionUID = 1L;
	public String Name;// 舱位名称
	public String TicketStatus;// 客票状态 A有空位 L 只能候补 C 航班完全关闭 S 限制销售 X 航班取消
	public String SinglePrice;// 单程价格
	public String ListPrice;//原价 后加的
	public String Fuel;// 燃油附加费
	public String Tax;// 机场建设费
	public String Ei;// 退改签
	public String Discount;// 折扣
	public String Origin;//价格来源 后加的
	public String ForChild;//是否允许儿童预订 后加的
	public String NeedApply;//是否为申请舱

	public String getListPrice() {
		return ListPrice;
	}

	public void setListPrice(String listPrice) {
		ListPrice = listPrice;
	}

	public String getOrigin() {
		return Origin;
	}

	public void setOrigin(String origin) {
		Origin = origin;
	}

	public String getForChild() {
		return ForChild;
	}

	public void setForChild(String forChild) {
		ForChild = forChild;
	}

	public String getNeedApply() {
		return NeedApply;
	}

	public void setNeedApply(String needApply) {
		NeedApply = needApply;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getTicketStatus() {
		return TicketStatus;
	}

	public void setTicketStatus(String ticketStatus) {
		TicketStatus = ticketStatus;
	}

	public String getSinglePrice() {
		return SinglePrice;
	}

	public void setSinglePrice(String singlePrice) {
		SinglePrice = singlePrice;
	}

	public String getFuel() {
		return Fuel;
	}

	public void setFuel(String fuel) {
		Fuel = fuel;
	}

	public String getTax() {
		return Tax;
	}

	public void setTax(String tax) {
		Tax = tax;
	}

	public String getEi() {
		return Ei;
	}

	public void setEi(String ei) {
		Ei = ei;
	}

	public String getDiscount() {
		return Discount;
	}

	public void setDiscount(String discount) {
		Discount = discount;
	}

}
