package com.example.customgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Intro extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.introduction);
		music.play(this, R.raw.urf);
		Button btn1=(Button) findViewById(R.id.button10);
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent=new Intent(Intro.this,MainActivity.class) ;
				startActivity(intent);
			}
		});
	}
}
