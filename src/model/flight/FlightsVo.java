package model.flight;

import java.io.Serializable;
import java.util.ArrayList;

public class FlightsVo implements Serializable {

	/**
	 * G11接口 航班
	 */
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "航空公司简称:" + CarrierReferred + "\n" +
                "航空公司二字码:" + Carrier + "\n" +
                "航班日期:" + FlightDate + "\n" +
                "出发时间:" + DepTime + "\n" +
                "到达时间:" + ArrTime + "\n" +
                "航班号:" + FlightNo + "\n" +
                DepTower + "-" + ArrTower + "\n";
        
    }
    public String getFlightInfo(int index){
        return this.toString() +
                "价格:" + Cabins.get(index).getSinglePrice();
    }
	public static final long serialVersionUID = 1L;
	public String Carrier;// 航空公司二字码
	public String CarrierFullName;// 航空公司全称
	public String CarrierReferred;// 简称
	public String CarrierEName;// 英文名
	public String BigPic;// 大图
	public String SmallPic;// 小图
	public String FlightNo;// 航班号
	public String IsShare;// 是否共享1为共享 0为不共享
	public String ShareCarrier;// 共享承运航空公司
	public String ShareFlightNo;// 共享航班号
	public String DepCity;// 出发城市
	public String DepCityFullName;// 城市全称
	public String DepCityReferred;// 简称
	public String ArrCity;// 到达城市
	public String ArrCityFullName;// 全称
	public String ArrCityReferred;// 简称
	public String DepTime;// 出发时间
	public String ArrTime;// 到达时间
	public String Aircraft;// 机型
	public String Stop;// 经停 1停 0 不停
	public String Meal;// 餐饮
	public String ETicket;// 电子客票标识
	public String DepTower;// 起飞航站楼
	public String ArrTower;// 抵达航站楼
	public ArrayList<CabinVo> Cabins;// 仓位集
	public String FlightDate;// 航班日期
	public String Error;// 错误标识
	public String Fuel;// 燃油附加费 后加的
	public String Tax;// 机场建设费 后加

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
