package com.example.customgame;

import android.graphics.Bitmap;

public class SmallEnemyPlane extends EnemyPlane {

	public SmallEnemyPlane(Bitmap bitmap){
		super(bitmap);
		setPower(1);//1 bullet to destroy
		setValue(1000);//score:1000
	}
}
