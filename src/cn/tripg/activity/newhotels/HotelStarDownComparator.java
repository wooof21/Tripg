package cn.tripg.activity.newhotels;

import java.util.Comparator;

import model.hotel.Hotel;

public class HotelStarDownComparator implements Comparator<Hotel>{

	@Override
	public int compare(Hotel lhs, Hotel rhs) {
		// TODO Auto-generated method stub
		double star1 = Double.valueOf(lhs.getStarCode());
		double star2 = Double.valueOf(rhs.getStarCode());
		System.out.println("down star1 ---> " + star1);
		System.out.println("down star2 ---> " + star2);
		if(star1 > star2){
			return -1;
		}else if(star1 < star2){
			return 1;
		}else{
			return 0;
		}
	}

}
