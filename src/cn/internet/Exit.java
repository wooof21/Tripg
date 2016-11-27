package cn.internet;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

public class Exit extends Application{

	 private List<Activity> activityList=new LinkedList<Activity>();
	 
	 private static Exit instance;
	 
	 private Exit(){
		 
	 }
	 public static Exit getInstance(){
		 if(instance == null){
			 instance = new Exit();
		 }
		 return instance;
	 }
	 public void addActivity(Activity activity){
		 if (activityList != null && activityList.size() > 0) {
			 if(!activityList.contains(activity)){
				 activityList.add(activity);
			 }
		 }else{
			 activityList.add(activity);
			}
		 
	 }
	 public void exit(){
		 if (activityList != null && activityList.size() > 0) {
			 for(Activity activity:activityList){
				 activity.finish();
			 }
		 }
		 
		 System.exit(0);
	 }
}
