package com.quarkssoft.br;
import java.util.List;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CurrentAppReceiver  extends BroadcastReceiver{

	@Override
	public void onReceive(Context aContext, Intent anIntent) {

		try{
			System.out.println("Receiver Received ");
			// Using ACTIVITY_SERVICE with getSystemService(String) 
			// to retrieve a ActivityManager for interacting with the global system state.

			ActivityManager am = (ActivityManager) aContext
					.getSystemService(Context.ACTIVITY_SERVICE);

			// Return a list of the tasks that are currently running, 
			// with the most recent being first and older ones after in order.
			// Taken 1 inside getRunningTasks method means want to take only 
			// top activity from stack and forgot the olders.

			List<ActivityManager.RunningTaskInfo> alltasks = am
					.getRunningTasks(1);


			for (ActivityManager.RunningTaskInfo aTask : alltasks) {

				String app1="calc";

				// Used to check for CALL screen  
				String currentApp=aTask.topActivity.getClassName();


				System.out.println("Current Running App:"+currentApp);
				if(currentApp.contains(app1)){
					// Used to check for CALL screen

					System.out.println("********App Detected Blacklisted");
					
						Intent msg=new Intent(aContext, MessageActivity.class);
						msg.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						aContext.startActivity(msg);                   

					            	


				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
