package com.androidhive.pushnotifications;

import java.util.Timer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.show.picture.ShowPicture;




import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

public class ResimYuklu extends Activity {
	
	String get_name = "";
	SharedPreferences prefs;
	String device_id;
	String get_result;
	int userInteractionTimeout;
	Timer timer;

@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	
	setContentView(R.layout.resim_yukle);
	
	timer = new Timer();
	
	onPause();
	onUserInteraction();
	
	//Get Device IMEI number
	TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
	device_id = telephonyManager.getDeviceId();
	Log.i("IMEI NUMBER", device_id);
	
	
	prefs = getSharedPreferences("ex1",MODE_PRIVATE); 
	get_name = prefs.getString("get_message_name","Url alınamadı");
	
	//Connect_send_data();
	
     //new AsyncCallBanner_sendata_work().execute();
	
	//Intent go_main_menu = new Intent(getApplicationContext(),MainActivity.class);
	//startActivity(go_main_menu);
	
	AsyncCallBanner_sendata_work work = new AsyncCallBanner_sendata_work();
	work.execute();
	
	
}

private void Connect_send_data() {
	// TODO Auto-generated method stub

	JSONObject returndata = null;
	HttpClient httpclient = new DefaultHttpClient();
	HttpPost httppost = new HttpPost(
			"http://78.186.62.169:9666/svc.asmx/SendMessage");
	httppost.setHeader("Content-type", "application/json");
	JSONObject jsonparameter = new JSONObject();
	


	
	try {
			
		jsonparameter.put("Imei", device_id);
		jsonparameter.put("MSG", "CLIENT_DOWNLOAD_OK");
		jsonparameter.put("Par1", get_name);
		jsonparameter.put("Par2", "Download");
		
		
		
		
	} catch (JSONException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	

	try {

		// jsonparameter.put("AnketID", "3");

		httppost.setEntity(new StringEntity(jsonparameter.toString(),
				"UTF-8"));
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		String responseString = EntityUtils.toString(entity);

		Log.i("@banner_responseString", "" + responseString);

		try {
			returndata = new JSONObject(responseString);
			String d = returndata.get("d").toString();
			
//			Icerik = d;
//			

			returndata = new JSONObject(responseString);
			

			
 			 get_result = returndata.optString("d");
 			
 			Log.i("IMEI NUMBER", ""+get_result);
//			
		
//			
		

//            String al = get_result;
			

		} catch (JSONException e) {

		}

	} catch (Exception e) {

	}

}


private class AsyncCallBanner_sendata_work extends AsyncTask<Void, Void, String> {

//	ProgressDialog dialog;

	@Override
	protected String doInBackground(Void... params) {
		Log.i("TAG", "doInBackground");
		Connect_send_data();
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		Log.i("TAG", "onPostExecute");
		// txt1.setText(Icerik);
		
		
		
		
			
			Intent intent = new Intent(getApplicationContext(),
					MainActivity.class);

			startActivity(intent);
		    
		

		
/*			else{
			
			Toast.makeText(getApplicationContext(), "Gitmedi", Toast.LENGTH_SHORT).show();
			
			Intent intent = new Intent(getApplicationContext(),
					MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
		}*/
		

	}

	@Override
	protected void onPreExecute() {
		Log.i("TAG", "onPreExecute");

	}

}


@Override
public void onUserInteraction() {
	// TODO Auto-generated method stub
	super.onUserInteraction();

	userInteractionTimeout = 0;

	// Log.d(LOG_TAG,"User Interaction : "+userInteractionTimeout);

}

@Override
protected void onPause() {
    // TODO Auto-generated method stub
    super.onPause();
    timer.cancel();
    
}


}
