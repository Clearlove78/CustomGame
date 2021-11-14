package com.example.customgame;

import android.graphics.Bitmap;


public class Bullet extends AutoSprite {

	public Bullet(Bitmap bitmap){
		super(bitmap);
		setSpeed(-10);//bullet move bottom to up
	}

}
