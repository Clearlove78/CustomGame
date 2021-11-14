package com.example.customgame;

import android.graphics.Bitmap;

/**
 * 中敌机类，体积中等，抗打击能力中等
 */
public class MiddleEnemyPlane extends EnemyPlane {

	public MiddleEnemyPlane(Bitmap bitmap){
		super(bitmap);
		setPower(4);//4 bullet to destroy
		setValue(6000);//score：6000
	}

}