package model.flight;

import java.io.Serializable;

public class CabinVo implements Serializable {

	/**
	 * G11�ӿ�
	 * 
	 * @param ��λ��Ϣ
	 */
	private static final long serialVersionUID = 1L;
	public String Name;// ��λ����
	public String TicketStatus;// ��Ʊ״̬ A�п�λ L ֻ�ܺ� C ������ȫ�ر� S �������� X ����ȡ��
	public String SinglePrice;// ���̼۸�
	public String ListPrice;//ԭ�� ��ӵ�
	public String Fuel;// ȼ�͸��ӷ�
	public String Tax;// ���������
	public String Ei;// �˸�ǩ
	public String Discount;// �ۿ�
	public String Origin;//�۸���Դ ��ӵ�
	public String ForChild;//�Ƿ������ͯԤ�� ��ӵ�
	public String NeedApply;//�Ƿ�Ϊ�����

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
