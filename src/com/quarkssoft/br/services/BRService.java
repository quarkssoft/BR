package com.quarkssoft.br.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import com.quarkssoft.br.db.DBHelper;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class BRService extends Service {
	int delay = 5000; // delay for 5 sec.
    int period = 1000*60*1; // repeat every sec.
    DBHelper dbHelper;
	
	@Override
	public IBinder onBind(Intent arg0) {
		
		return null;
	}
	
	@Override
	public void onCreate() {	
		super.onCreate();
		System.out.println("Service Started");
		startTimer();
		
	}

	private void startTimer(){
		Timer timer=new Timer();
		timer.scheduleAtFixedRate(new TimerTask(){

			@Override
			public void run() {

				try{
					getData("http://10.0.2.2:3000/blacklist.json");
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
		}, delay, period);
	}
	public void getData(String url){
		if(dbHelper == null){
			dbHelper=new DBHelper(getApplicationContext());
		}
		System.out.println("***** Get Configurations*******");
		String result="";
		HttpResponse httpResponse=null;
		try {
			
			final HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient(httpParams);

			// make GET request to the given URL
			HttpGet httpGet=new HttpGet(url);			
			System.out.println("Url:"+url);
			//System.out.println("Payload:"+payload);	    	    
			// 5. set json to StringEntity		    
			//StringEntity se = new StringEntity(payload);

			// 6. set httpPost Entity
			//httpPost.setEntity(se);

			//httpPost.setHeader("Content-type", "application/json");			
			httpResponse = httpclient.execute(httpGet);

			InputStream inputStream=null;
			if(httpResponse != null){
				System.out.println("Status Code:"+httpResponse.getStatusLine().getStatusCode());
				//System.out.println("Status Text:"+httpResponse.getStatusLine().getReasonPhrase());
				inputStream=httpResponse.getEntity().getContent();
				if(inputStream !=null){
					result=convertInputStreamToString(inputStream);
				}
				else{
					result="";						
				}
				
				if(result != null && !result.trim().equals("")){
					JSONObject obj=new JSONObject(result);
					JSONArray arr=obj.getJSONArray("apps");
					if(arr !=null && arr.length() > 0){
						
						int status=dbHelper.deleteBlacklist();
						System.out.println("Deleted Status:"+status);
					}
					if(arr !=null && arr.length() > 0){
						for(int i=0;i<arr.length();i++){
							System.out.println("Packages:"+arr.get(i).toString());
							dbHelper.insertBlacklist(arr.get(i).toString());
						}
					}
				}
				
			}else{
				System.out.println("Sync Failed");
			}

		} catch (Exception e) {
			e.printStackTrace();
			//Log.d("Call Log sync Error:", e.getLocalizedMessage());
		}
	
		
	}
	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

	public boolean isConnected(){
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) 
			return true;
		else
			return false;	
	}
	
	public  Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {

			case 0:
				
				
				break;
			case 1:

				break;
			case 2:

				break;
			case 3:
				break;

			}
		}
	};
}
