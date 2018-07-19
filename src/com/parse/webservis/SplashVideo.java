package com.parse.webservis;

import java.util.Timer;

import com.androidhive.pushnotifications.MainActivity;



import com.androidhive.pushnotifications.R;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.VideoView;

public class SplashVideo extends Activity{
	
	VideoView video_play;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.splash_video);
		

		
		try {
		    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
		    r.play();
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		video_play = (VideoView) findViewById(R.id.videoView1);
		
		
		String path = "android.resource://" + getPackageName() + "/" + R.raw.videom;
		video_play.setVideoURI(Uri.parse(path));
		video_play.start();
		
//		Connect_send_data_video();
//		Connect_send_data_work();


		
		

	/*	
		// video finish listener
		video_play.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {


				@Override
				public void onCompletion(MediaPlayer arg0) {
					// TODO Auto-generated method stub
					
					
					Intent intent = new Intent(getApplicationContext(),MainActivity.class);
					//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(intent);
					
				}
		});
	*/
	}

}
