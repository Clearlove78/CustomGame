package com.example.customgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class GameActivity extends Activity {
	private GameView gameView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		gameView = (GameView)findViewById(R.id.gameView);
		if(End.level==1) {
			music.play(this, R.raw.kinetic);
		}
		if(End.level==2) {
			music.play(this, R.raw.light);
		}
		if(End.level==3) {
			music.play(this, R.raw.stray);
		}
		if(End.level==4) {
			music.play(this, R.raw.ascension);
		}
		int[] bitmapIds = {
				R.drawable.plane,
				R.drawable.explosion,
				R.drawable.yellow_bullet,
				R.drawable.blue_bullet,
				R.drawable.small,
				R.drawable.middle,
				R.drawable.big,
				R.drawable.bullet_award,
				R.drawable.pause1,
				R.drawable.pause2,
		};
		gameView.start(bitmapIds);
	}

	protected void onPause()
	{
		super.onPause();
		if(null != gameView)
		{
			gameView.pause();
		}
	}

	protected void onDestroy()
	{
		super.onDestroy();
		if(null != gameView)
		{
			gameView.destroy();
		}
		gameView = null;
	}
}
