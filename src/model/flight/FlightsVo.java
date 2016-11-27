package model.flight;

import java.io.Serializable;
import java.util.ArrayList;

public class FlightsVo implements Serializable {

	/**
	 * G11�ӿ� ����
	 */
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "���չ�˾���:" + CarrierReferred + "\n" +
                "���չ�˾������:" + Carrier + "\n" +
                "��������:" + FlightDate + "\n" +
                "����ʱ��:" + DepTime + "\n" +
                "����ʱ��:" + ArrTime + "\n" +
                "�����:" + FlightNo + "\n" +
                DepTower + "-" + ArrTower + "\n";
        
    }
    public String getFlightInfo(int index){
        return this.toString() +
                "�۸�:" + Cabins.get(index).getSinglePrice();
    }
	public static final long serialVersionUID = 1L;
	public String Carrier;// ���չ�˾������
	public String CarrierFullName;// ���չ�˾ȫ��
	public String CarrierReferred;// ���
	public String CarrierEName;// Ӣ����
	public String BigPic;// ��ͼ
	public String SmallPic;// Сͼ
	public String FlightNo;// �����
	public String IsShare;// �Ƿ���1Ϊ���� 0Ϊ������
	public String ShareCarrier;// ������˺��չ�˾
	public String ShareFlightNo;// �������
	public String DepCity;// ��������
	public String DepCityFullName;// ����ȫ��
	public String DepCityReferred;// ���
	public String ArrCity;// �������
	public String ArrCityFullName;// ȫ��
	public String ArrCityReferred;// ���
	public String DepTime;// ����ʱ��
	public String ArrTime;// ����ʱ��
	public String Aircraft;// ����
	public String Stop;// ��ͣ 1ͣ 0 ��ͣ
	public String Meal;// ����
	public String ETicket;// ���ӿ�Ʊ��ʶ
	public String DepTower;// ��ɺ�վ¥
	public String ArrTower;// �ִﺽվ¥
	public ArrayList<CabinVo> Cabins;// ��λ��
	public String FlightDate;// ��������
	public String Error;// �����ʶ
	public String Fuel;// ȼ�͸��ӷ� ��ӵ�
	public String Tax;// ��������� ���

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

	public String getError() {
		return Error;
	}

	public void setError(String error) {
		Error = error;
	}

	public String getCarrier() {
		return Carrier;
	}

	public void setCarrier(String carrier) {
		Carrier = carrier;
	}

	public String getCarrierFullName() {
		return CarrierFullName;
	}

	public void setCarrierFullName(String carrierFullName) {
		CarrierFullName = carrierFullName;
	}

	public String getCarrierReferred() {
		return CarrierReferred;
	}

	public void setCarrierReferred(String carrierReferred) {
		CarrierReferred = carrierReferred;
	}

	public String getCarrierEName() {
		return CarrierEName;
	}

	public void setCarrierEName(String carrierEName) {
		CarrierEName = carrierEName;
	}

	public String getBigPic() {
		return BigPic;
	}

	public void setBigPic(String bigPic) {
		BigPic = bigPic;
	}

	public String getSmallPic() {
		return SmallPic;
	}

	public void setSmallPic(String smallPic) {
		SmallPic = smallPic;
	}

	public String getFlightNo() {
		return FlightNo;
	}

	public void setFlightNo(String flightNo) {
		FlightNo = flightNo;
	}

	public String getIsShare() {
		return IsShare;
	}

	public void setIsShare(String isShare) {
		IsShare = isShare;
	}

	public String getShareCarrier() {
		return ShareCarrier;
	}

	public void setShareCarrier(String shareCarrier) {
		ShareCarrier = shareCarrier;
	}

	public String getShareFlightNo() {
		return ShareFlightNo;
	}

	public void setShareFlightNo(String shareFlightNo) {
		ShareFlightNo = shareFlightNo;
	}

	public String getDepCity() {
		return DepCity;
	}

	public void setDepCity(String depCity) {
		DepCity = depCity;
	}

	public String getDepCityFullName() {
		return DepCityFullName;
	}

	public void setDepCityFullName(String depCityFullName) {
		DepCityFullName = depCityFullName;
	}

	public String getDepCityReferred() {
		return DepCityReferred;
	}

	public void setDepCityReferred(String depCityReferred) {
		DepCityReferred = depCityReferred;
	}

	public String getArrCity() {
		return ArrCity;
	}

	public void setArrCity(String arrCity) {
		ArrCity = arrCity;
	}

	public String getArrCityFullName() {
		return ArrCityFullName;
	}

	public void setArrCityFullName(String arrCityFullName) {
		ArrCityFullName = arrCityFullName;
	}

	public String getArrCityReferred() {
		return ArrCityReferred;
	}

	public void setArrCityReferred(String arrCityReferred) {
		ArrCityReferred = arrCityReferred;
	}

	public String getDepTime() {
		return DepTime;
	}

	public void setDepTime(String depTime) {
		DepTime = depTime;
	}

	public String getArrTime() {
		return ArrTime;
	}

	public void setArrTime(String arrTime) {
		ArrTime = arrTime;
	}

	public String getAircraft() {
		return Aircraft;
	}

	public void setAircraft(String aircraft) {
		Aircraft = aircraft;
	}

	public String getStop() {
		return Stop;
	}

	public void setStop(String stop) {
		Stop = stop;
	}

	public String getMeal() {
		return Meal;
	}

	public void setMeal(String meal) {
		Meal = meal;
	}

	public String getETicket() {
		return ETicket;
	}

	public void setETicket(String eTicket) {
		ETicket = eTicket;
	}

	public String getDepTower() {
		return DepTower;
	}

	public void setDepTower(String depTower) {
		DepTower = depTower;
	}

	public String getArrTower() {
		return ArrTower;
	}

	public void setArrTower(String arrTower) {
		ArrTower = arrTower;
	}

	public ArrayList<CabinVo> getCabins() {
		return Cabins;
	}

	public void setCabins(ArrayList<CabinVo> cabins) {
		Cabins = cabins;
	}

	public String getFlightDate() {
		return FlightDate;
	}

	public void setFlightDate(String flightDate) {
		FlightDate = flightDate;
	}
}
