package com.example.android.dragrace;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;


// A new NHRA-style staging indicator (a circle with a horizontal bar).
public class StageBulb extends Bulb {

	public StageBulb(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void onDraw(Canvas onCan) {
		Rect bounds = new Rect();
		if(!onCan.getClipBounds(bounds)) return;

	    paint.setColor(mColor);

		float xP = bounds.exactCenterX();
		float yP = bounds.exactCenterY();
		
		float minDim = (float) Math.min(bounds.width(), bounds.height());
		float rad = minDim / 2.0f - 8.0f;
		

	    if(!lit) {
	    	paint.setStyle(Paint.Style.STROKE);
	    	paint.setStrokeWidth(2);

	    } else {
	    	paint.setStyle(Paint.Style.STROKE);
	    	paint.setStrokeWidth(8);
	    	onCan.drawLine(xP - rad, yP, xP + rad, yP, paint);
	    	paint.setStrokeWidth(6);
	    }

		onCan.drawCircle(xP, yP, rad, paint);
	}

}
