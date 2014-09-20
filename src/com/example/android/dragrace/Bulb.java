package com.example.android.dragrace;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Rect;

public class Bulb extends View {

	int mColor;
	boolean lit = false;

	private void init(AttributeSet attrs) {
		  if (attrs != null) {
		    String namespace = "http://schemas.android.com/apk/res-auto";
		    mColor = attrs.getAttributeIntValue(namespace, "color", 0xFF202020);
		  }
	}

    protected Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

	public Bulb(Context context, AttributeSet attrs) {
		super(context, attrs);

		init(attrs);
	}

	@Override
	public void onDraw(Canvas onCan) {

		Rect bounds = new Rect();
		if(!onCan.getClipBounds(bounds)) return;

		paint.setColor(mColor);

		if(!lit) {
			   paint.setStyle(Paint.Style.STROKE);
			   paint.setStrokeWidth(5);
		 } else {
			   paint.setStyle(Paint.Style.FILL);
		}

		float xP = bounds.exactCenterX();
		float yP = bounds.exactCenterY();
		float minDim = Math.min(bounds.width(), bounds.height());
		float rad = minDim / 2.0f - 8.0f;
		onCan.drawCircle(xP, yP, rad, paint);
	}


	public void setLamp(boolean lampOn) {
		if(lit ==lampOn) return;
		lit = lampOn;
		this.postInvalidate();
	}
}