package com.example.customgame;

import android.graphics.Bitmap;

public class BigEnemyPlane extends EnemyPlane {

	public BigEnemyPlane(Bitmap bitmap){
		super(bitmap);
		setPower(10);//10 bullet to destroy
		setValue(30000);//score:30000
	}

}
