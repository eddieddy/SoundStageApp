package com.example.cet191.soundstageapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class Speedometer extends View{
	private static final String TAG = Speedometer.class.getSimpleName();
	public static final float DEFAULT_MAX_SPEED = 300; // Assuming this is km/h and you drive a super-car

	// Speedometer internal state
	private float mMaxSpeed;
	private float mCurrentSpeed;
	
	// Scale drawing tools
	private Paint onMarkLowPaint;
	private Paint onMarkPaint;
	private Paint offMarkPaint;
	private Paint scalePaint;
	private Paint readingPaint;
	private Path onPath;
	private Path offPath;
	final RectF oval = new RectF();
	
	// Drawing colors
	private int ON_LOW_COLOR = Color.argb(255, 0xff, 0xA5, 0x00);
	private int ON_COLOR = Color.argb(255, 128, 255, 0);	//green
	private int MID_COLOR = Color.argb(255, 255, 128, 0);	//orange
	private int HIGH_COLOR = Color.argb(255, 255, 0, 0);	//red
	private int OFF_COLOR = Color.argb(255, 0x3e, 0x3e, 0x3e);
	private int SCALE_COLOR = Color.argb(255, 255, 255, 255);
	private float SCALE_SIZE = 14f;
	private float READING_SIZE = 60f;
	private float MARK_WIDTH = 35f;
	
	// Scale configuration
	private float centerX;
	private float centerY;
	private float radius;

	public Speedometer(Context context){
		super(context);
	}
	
	public Speedometer(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.Speedometer, 
				0, 0);
		try{
			mMaxSpeed = a.getFloat(R.styleable.Speedometer_maxSpeed, DEFAULT_MAX_SPEED);
			mCurrentSpeed = a.getFloat(R.styleable.Speedometer_currentSpeed, 0);
			ON_LOW_COLOR = a.getColor(R.styleable.Speedometer_onColor, ON_LOW_COLOR);
			ON_COLOR = a.getColor(R.styleable.Speedometer_onColor, ON_COLOR);
			MID_COLOR = a.getColor(R.styleable.Speedometer_onColor, MID_COLOR);
			HIGH_COLOR = a.getColor(R.styleable.Speedometer_onColor, HIGH_COLOR);
			OFF_COLOR = a.getColor(R.styleable.Speedometer_offColor, OFF_COLOR);
			SCALE_COLOR = a.getColor(R.styleable.Speedometer_scaleColor, SCALE_COLOR);
			SCALE_SIZE = a.getDimension(R.styleable.Speedometer_scaleTextSize, SCALE_SIZE);
			READING_SIZE = a.getDimension(R.styleable.Speedometer_readingTextSize, READING_SIZE);
		} finally{
			a.recycle();
		}
		initDrawingTools();
	}
	
	private void initDrawingTools(){
		onMarkLowPaint = new Paint();
		onMarkLowPaint.setStyle(Paint.Style.STROKE);
		onMarkLowPaint.setColor(ON_LOW_COLOR);
		onMarkLowPaint.setStrokeWidth(MARK_WIDTH);
		onMarkLowPaint.setShadowLayer(5f, 0f, 0f, ON_COLOR);
		onMarkLowPaint.setAntiAlias(true);

		onMarkPaint = new Paint();
		onMarkPaint.setStyle(Paint.Style.STROKE);
		onMarkPaint.setColor(ON_COLOR);
		onMarkPaint.setStrokeWidth(MARK_WIDTH);
		onMarkPaint.setShadowLayer(5f, 0f, 0f, ON_COLOR);
		onMarkPaint.setAntiAlias(true);

		offMarkPaint = new Paint(onMarkPaint);
		offMarkPaint.setColor(OFF_COLOR);
		offMarkPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		offMarkPaint.setShadowLayer(0f, 0f, 0f, OFF_COLOR);
		
		scalePaint = new Paint(offMarkPaint);
		scalePaint.setStrokeWidth(2f);
		scalePaint.setTextSize(SCALE_SIZE);
		scalePaint.setShadowLayer(5f, 0f, 0f, Color.RED);
		scalePaint.setColor(SCALE_COLOR);
		
		readingPaint = new Paint(scalePaint);
		readingPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		offMarkPaint.setShadowLayer(3f, 0f, 0f, Color.WHITE);
		readingPaint.setTextSize(65f);
		readingPaint.setTypeface(Typeface.SANS_SERIF);
		readingPaint.setColor(Color.WHITE);
		
		onPath = new Path();
		offPath = new Path();
	}
	
	public float getCurrentSpeed() {
		return mCurrentSpeed;
	}

	public void setCurrentSpeed(float mCurrentSpeed) {
		if(mCurrentSpeed > this.mMaxSpeed)
			this.mCurrentSpeed = mMaxSpeed;
		else if(mCurrentSpeed < 0)
			this.mCurrentSpeed = 0;
		else
			this.mCurrentSpeed = mCurrentSpeed;


		//Changes color on reading gauge based on value
		if(mCurrentSpeed >= 0 && mCurrentSpeed < 50){
			onMarkPaint.setColor(ON_COLOR);
			onMarkPaint.setShadowLayer(5f, 0f, 0f, ON_COLOR);
		}

		if(mCurrentSpeed > 50 && mCurrentSpeed < 70){
			onMarkPaint.setColor(MID_COLOR);
			onMarkPaint.setShadowLayer(5f, 0f, 0f, MID_COLOR);
		}

		if(mCurrentSpeed > 70){
			onMarkPaint.setColor(HIGH_COLOR);
			onMarkPaint.setShadowLayer(5f, 0f, 0f, HIGH_COLOR);
		}

        this.invalidate();
	}
	
	@Override
	protected void onSizeChanged(int width, int height, int oldw, int oldh) {
		
		// Setting up the oval area in which the arc will be drawn
		if (width > height) {
			radius = height / 4;
		} else{
			radius = width/4;
		}
		oval.set(centerX - radius,
				centerY - radius,
				centerX + radius,
				centerY + radius);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		Log.d(TAG, "Width spec: " + MeasureSpec.toString(widthMeasureSpec));
//		Log.d(TAG, "Height spec: " + MeasureSpec.toString(heightMeasureSpec));
		
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		
		int chosenWidth = chooseDimension(widthMode, widthSize);
		int chosenHeight = chooseDimension(heightMode, heightSize);
		
		int chosenDimension = Math.min(chosenWidth, chosenHeight);
		centerX = chosenDimension / 2;
		centerY = chosenDimension / 2;
		setMeasuredDimension(chosenDimension, chosenDimension);
	}
	
	private int chooseDimension(int mode, int size) {
		if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.EXACTLY) {
			return size;
		} else { // (mode == MeasureSpec.UNSPECIFIED)
			return getPreferredSize();
		} 
	}
	
	// in case there is no size specified
	private int getPreferredSize() {
		return 300;
	}

	@Override
	public void onDraw(Canvas canvas){
		drawScaleBackground(canvas);
		drawScale(canvas);
		drawLegend(canvas);
		drawReading(canvas);
	}
	
	/**
	 * Draws the segments in their OFF state
	 * @param canvas
	 */
	private void drawScaleBackground(Canvas canvas){
		offPath.reset();
		for(int i = -180; i < 0; i+=4){
			offPath.addArc(oval, i, 2f);
		}
		canvas.drawPath(offPath, offMarkPaint);
	}
	
	private void drawScale(Canvas canvas){

		onPath.reset();
		// Draw the low marks.
		for(int i = -180; i < (mCurrentSpeed/mMaxSpeed)*180 - 180; i+=4){
			onPath.addArc(oval, i, 2f);
		}
		canvas.drawPath(onPath, onMarkLowPaint);

		onPath.reset();
		for(int i = -180; i < (mCurrentSpeed/mMaxSpeed)*180 - 180; i+=4){
			onPath.addArc(oval, i, 2f);
		}
		canvas.drawPath(onPath, onMarkPaint);
	}
	
	private void drawLegend(Canvas canvas){
		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		canvas.rotate(-180, centerX,centerY);
		Path circle = new Path();
		double halfCircumference = radius * Math.PI;
		double increments = 50;
		for(int i = 0; i <= this.mMaxSpeed; i += increments){
			circle.addCircle(centerX, centerY, radius, Path.Direction.CW);
			canvas.drawTextOnPath(String.format("%d", i),
								circle, 
								(float) ((i*halfCircumference/this.mMaxSpeed ) * .9),
								-30f,
								scalePaint);			
		}
		
		canvas.restore();
	}
	
	private void drawReading(Canvas canvas){
		Path path = new Path();
		String message = String.format("%d", (int) this.mCurrentSpeed);
		float[] widths = new float[message.length()];
		readingPaint.getTextWidths(message, widths);
		float advance = 0;
		for(double width:widths)
			advance += width;
		path.moveTo(centerX - advance/2, centerY);
		path.lineTo(centerX + advance/2, centerY);
		canvas.drawTextOnPath(message, path, 0f, 0f, readingPaint);
	}

//	@Override
//	public void onSpeedChanged(float newSpeedValue) {
//		this.setCurrentSpeed(newSpeedValue);
//		this.invalidate();
//	}
}
