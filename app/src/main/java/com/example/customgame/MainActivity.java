package com.example.customgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
	private VideoView videoView;
	private Button btn_start,btn_end;
	private MediaController mediaController;
	private Intent intent;
	private static final String TAG = MainActivity.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		music.play(this, R.raw.hua);
		Button btn2=(Button) findViewById(R.id.button20);
		btn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent=new Intent(MainActivity.this,GameActivity.class) ;
				startActivity(intent);
			}
		});
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		music.stop(this);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		music.play(this, R.raw.hua);
	}
	public void onVolumeAddKeyclick(View view) {

		OnVolumeAddKey();
	}
	public void onVolumeReduceKeyclick(View view) {

		OnVolumeReduceKey();

	}
	public void onHomeKeyclick(View view) {

		OnHomeKey();

	}
	public void OnVolumeAddKey(){

		try
		{
			String keyCommand = "input keyevent " + KeyEvent.KEYCODE_VOLUME_UP;
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec(keyCommand);

			Log.i(TAG, "OnVolumeAddKey: Volume increase");
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void OnVolumeReduceKey(){

		try
		{
			String keyCommand = "input keyevent " + KeyEvent.KEYCODE_VOLUME_DOWN;
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec(keyCommand);

			Log.i(TAG, "OnVolumeAddKey: Volume decrease");
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void OnHomeKey(){

		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);

	}
}