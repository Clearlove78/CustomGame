package com.example.customgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class End extends AppCompatActivity {
	public static int level = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(level == 1) {
			setContentView(R.layout.end);
		}
		if(level == 2) {
			setContentView(R.layout.end1);
		}
		if(level == 3) {
			setContentView(R.layout.end2);
		}
		if(level == 4) {
			setContentView(R.layout.end3);
		}
		music.play(this, R.raw.hua);
		Button btn1=(Button) findViewById(R.id.button1);
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				level = 1;
				Intent intent = new Intent(End.this, MainActivity.class);
				startActivity(intent);
			}
		});
		if(level<4) {
			Button btn2 = (Button) findViewById(R.id.button);
			btn2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					level++;
					Intent intent = new Intent(End.this, GameActivity.class);
					startActivity(intent);
				}
			});
		}
	}
}
