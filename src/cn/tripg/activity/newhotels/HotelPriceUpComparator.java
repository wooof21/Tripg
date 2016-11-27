package cn.tripg.activity.newhotels;

import java.util.Comparator;

import model.hotel.Hotel;

public class HotelPriceUpComparator implements Comparator<Hotel>{

	@Override
	public int compare(Hotel lhs, Hotel rhs) {
		// TODO Auto-generated method stub
		double price1 = Double.valueOf(lhs.getLowestPrice());
		double price2 = Double.valueOf(rhs.getLowestPrice());
		System.out.println("up price1 ---> " + price1);
		System.out.println("up price2 ---> " + price2);
		if(price1 > price2){
			return 1;
		}else if(price1 < price2){
			return -1;
		}else{
			return 0;
		}
	}

}
