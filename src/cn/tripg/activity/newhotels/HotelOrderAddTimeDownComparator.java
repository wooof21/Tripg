package cn.tripg.activity.newhotels;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import android.util.Log;

public class HotelOrderAddTimeDownComparator implements Comparator<HotelOrder>{

	@Override
	public int compare(HotelOrder lhs, HotelOrder rhs) {
		// TODO Auto-generated method stub

		String[] l = lhs.getAddTime().split(" ");
		String[] l1 = l[0].split("/");
		String[] l2 = l[1].split(":");
		Log.e("l1", l[0]);
		Log.e("l2", l[1]);
		
		String[] r = rhs.getAddTime().split(" ");
		String[] r1 = r[0].split("/");
		String[] r2 = r[1].split(":");
		Log.e("r1", r[0]);
		Log.e("r2", r[1]);
		
		double left1 = Double.valueOf(l1[0]+l1[1]+l1[2]);
		double right1 = Double.valueOf(r1[0]+r1[1]+r1[2]);
		
		double left2 = Double.valueOf(l2[0]+l2[1]+l2[2]);
		double right2 = Double.valueOf(r2[0]+r2[1]+r2[2]);
		Log.e("left", ""+left1 + " & " + left2);
		Log.e("right", ""+right1 + " & " + right2);
		if(left1 > right1){
			return -1;
		}else if(left1 == right1 && left2 > right2){
			return -1;
		}else if(left1 < right1){
			return 1;
		}else if(left1 == right1 && left2 < right2){
			return 1;
		}else{
			return 0;
		}

	}






}
