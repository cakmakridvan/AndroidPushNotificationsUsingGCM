package com.show.picture;

import java.util.Timer;
import java.util.TimerTask;

import com.androidhive.pushnotifications.MainActivity;


import com.androidhive.pushnotifications.R;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SetVolumeLevel extends Activity implements OnClickListener {
	/** Called when the activity is first created. */

	private SeekBar volumeSeekbar = null;
	private AudioManager audioManager,audioManager_sound = null;
	
    private SeekBar notification_sound;
	
	Button go_back;
	EditText edt_secret;
	Button btn_secret,btn_wifi,btn_up,btn_notify_sound;
	
	TextView charge_stuation;
	int status;
	String info;
	int level;
	
	Timer timer;
	int userInteractionTimeout;
	
	AudioManager audio;
	
	private String chosenRingtone;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		setContentView(R.layout.setvolume);
		go_back = (Button) findViewById(R.id.btn_geri);
		go_back.setOnClickListener(this);
		edt_secret = (EditText) findViewById(R.id.secret_editText);
		btn_secret = (Button) findViewById(R.id.secret_button);
		btn_wifi = (Button) findViewById(R.id.btn_wifi);
		btn_wifi.setOnClickListener(this);
		//btn_up = (Button) findViewById(R.id.btn_notify_up);
		btn_secret.setOnClickListener(this);
		//btn_up.setOnClickListener(this);
		btn_notify_sound = (Button) findViewById(R.id.btn_notificationsound);
		btn_notify_sound.setOnClickListener(this);
		
		audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		

		
		charge_stuation = (TextView) findViewById(R.id.charge_state);
		this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
		
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		if ((tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSDPA)) {
			Toast.makeText(getApplicationContext(), "3g", 1000).show();
			Log.d("Type", "3g");// for 3g HSDPA networktype will be return as
								// per testing(real) in device with 3g enable
								// data
			// and speed will also matters to decide 3g network type
		} else if ((tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSPAP)) {
			Toast.makeText(getApplicationContext(), "4g", 1000).show();
			Log.d("Type", "4g"); // /No specification for the 4g but from wiki
									// i found(HSPAP used in 4g)
									// http://goo.gl/bhtVT
		} else if ((tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_GPRS)) {
			Toast.makeText(getApplicationContext(), "GPRS", 1000).show();
			Log.d("Type", "GPRS");
		} else if ((tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_EDGE)) {
			Toast.makeText(getApplicationContext(), "Edge 2g", 1000).show();
			Log.d("Type", "EDGE 2g");
		}
		
		initControls();
		notification_sound_kontrol();
		
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				if (userInteractionTimeout == 180) {
					// Do your @Override
					Intent intent = new Intent(getApplicationContext(),
							MainActivity.class);
					intent.putExtra("EXIT", false);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);

				}
				userInteractionTimeout++;
			}
		}, 0, 1000);
	}
	
	
	
	private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			level = intent.getIntExtra("level", 0);
			info = "Batarya Seviyesi: " + level + "%\n";
			// charge_stuation.setText(String.valueOf(level) + "%");
			status = intent.getIntExtra("status", 0);
			info += ("Durum: " + getStatusString(status) + "\n");
			charge_stuation.setText(info);

			/*
			 * TelephonyManager telMgr = (TelephonyManager)
			 * getSystemService(Context.TELEPHONY_SERVICE); int simState =
			 * telMgr.getSimState(); switch (simState) { case
			 * TelephonyManager.SIM_STATE_ABSENT: // do something break; case
			 * TelephonyManager.SIM_STATE_NETWORK_LOCKED: // do something
			 * Toast.makeText(getApplicationContext(), "Network Locked", 1000)
			 * .show();
			 * 
			 * break; case TelephonyManager.SIM_STATE_PIN_REQUIRED: // do
			 * something break; case TelephonyManager.SIM_STATE_PUK_REQUIRED: //
			 * do something break; case TelephonyManager.SIM_STATE_READY: // do
			 * something Toast.makeText(getApplicationContext(),
			 * "Sım State ready", 1000) .show(); break; case
			 * TelephonyManager.SIM_STATE_UNKNOWN: // do something
			 * 
			 * Toast.makeText(getApplicationContext(), "SIM State Unknown",
			 * 1000);
			 * 
			 * break; }
			 */

		}
	};
	
	
	private String getStatusString(int status) {
		String statusString = "Unknown";

		switch (status) {
		case BatteryManager.BATTERY_STATUS_CHARGING:
			statusString = "Şarj oluyor";
			break;
		case BatteryManager.BATTERY_STATUS_DISCHARGING:
			statusString = "Şarja takılı değil";
			break;
		case BatteryManager.BATTERY_STATUS_FULL:
			statusString = "Dolu";
			break;
		case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
			statusString = "Şarj olmuyor";
			break;
		}

		return statusString;
	}

	private void initControls() {
		try {
			volumeSeekbar = (SeekBar) findViewById(R.id.seekBar);
			audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			volumeSeekbar.setMax(audioManager
					.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
			volumeSeekbar.setProgress(audioManager
					.getStreamVolume(AudioManager.STREAM_MUSIC));

			volumeSeekbar
					.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
						@Override
						public void onStopTrackingTouch(SeekBar arg0) {
						}

						@Override
						public void onStartTrackingTouch(SeekBar arg0) {
						}

						@Override
						public void onProgressChanged(SeekBar arg0,
								int progress, boolean arg2) {
							audioManager.setStreamVolume(
									AudioManager.STREAM_MUSIC, progress, 0);
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}
	
	public void notification_sound_kontrol(){
		
		try {
			
			
			notification_sound = (SeekBar) findViewById(R.id.seekBar_notification);
			audioManager_sound = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			
			notification_sound.setMax(audioManager_sound.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION));
			
			notification_sound.setProgress(audioManager_sound
					.getStreamVolume(AudioManager.STREAM_NOTIFICATION));
			
			 notification_sound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				 @Override
				 public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
				 
				 audioManager_sound.setStreamVolume(AudioManager.STREAM_NOTIFICATION, i, 0);
				 }

				 @Override
				 public void onStartTrackingTouch(SeekBar seekBar) {

				 }

				 @Override
				 public void onStopTrackingTouch(SeekBar seekBar) {

				 }
				 
				 
				 
				 });
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stubs
		String getText = edt_secret.getText().toString();

		switch (v.getId()) {
		
		case R.id.secret_button:

			if (getText.equals("Spintek")) {

				// Intent i = getBaseContext().getPackageManager()
				// .getLaunchIntentForPackage( getBaseContext().getPackageName()
				// );
				// i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// startActivity(i);

				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);

				edt_secret.setText("");
				
				
				//Reboot device
/*				try {
			        Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", "reboot" });
			        proc.waitFor();
			    } catch (Exception ex) {
			        Log.i("TAG", "Could not reboot", ex);
			    }*/
				
				timer.cancel();
			}

			else
				Toast.makeText(getApplicationContext(), "Şifre yanlış",
						Toast.LENGTH_SHORT).show();

			break;
		
		
		case R.id.btn_geri:

			Intent go_video_player = new Intent(getApplicationContext(),
					MainActivity.class);
			go_video_player.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(go_video_player);
			
			timer.cancel();

			break;
			
		case R.id.btn_wifi:

			startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
			//beep(5);
			
			//audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                   // AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);


			break;
			
		case R.id.btn_notificationsound:

			Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
			intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
			intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Bildirim Sesini Seçiniz");
			intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
			this.startActivityForResult(intent, 5);
		
			
			


			break;
/*			
		case R.id.btn_notify_up:

			//startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
			//beep(5);
			
			 audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                     AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);


			break;
*/
		default:
			break;
		}

	}
	

	
	
	// Motion
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		View view = getCurrentFocus();
		boolean ret = super.dispatchTouchEvent(event);

		if (view instanceof EditText) {
			View w = getCurrentFocus();
			int scrcoords[] = new int[2];
			w.getLocationOnScreen(scrcoords);
			float x = event.getRawX() + w.getLeft() - scrcoords[0];
			float y = event.getRawY() + w.getTop() - scrcoords[1];

			if (event.getAction() == MotionEvent.ACTION_UP
					&& (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w
							.getBottom())) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
						.getWindowToken(), 0);
			}
		}
		return ret;
	}
	
	 @Override
	 protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent)
	 {
	     if (resultCode == Activity.RESULT_OK && requestCode == 5)
	     {
	          Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
	          RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION, uri);
	       

	          

	          if (uri != null)
	          {
	              this.chosenRingtone = uri.toString();
	              
	              
	              
	          }
	          else
	          {
	              this.chosenRingtone = null;
	          }
	      }            
	  }
	
	private void beep(int volume)
	{
//	    AudioManager manager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
//	    manager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
//
//	    Uri notification = RingtoneManager
//	            .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//	    MediaPlayer player = MediaPlayer.create(getApplicationContext(), notification);
//	    player.start();
		

	}
	
	

}
