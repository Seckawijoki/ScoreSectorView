package com.seckawijoki.scoresectorview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.BounceInterpolator;

public class ScoreSectorView extends View {
  private static final int MAX_SCORE = 100;
  private static final int DEFAULT_COLOR = 0xFF97EA4C;
  private Paint mBottomArcPaint;
  private Paint mTopArcPaint;
  private Paint mDigitPaint;
  private Paint mUnitPaint;
  private Paint mStatePaint;
  private RectF mArcRectF;
  private Rect mDigitBound;
  private Rect mUnitBound;
  private Rect mStateBound;
  private int mBottomArcColor;
  private int mTopArcColor;
  private int mDigitColor;
  private int mUnitColor;
  private int mStateColor;
  private int mStartColor;
  private int mEndColor;
  private float mArcWidth;
  private boolean mDrawState;
  private int mDigitSize;
  private int mUnitSize;
  private int mStateSize;
  private int mRadius;
  private float mCurrentScore;
  private float mScore;
  private String mUnit;
  private OnClickListener mOnClickListener;
  private final String[] mHealthState = {
      "非常好", "良好", "达标", "很差"
  };
  private LinearGradient linearGradient;

  public void setStateSize(int mStateSize) {
    this.mStateSize = mStateSize;
    mStatePaint.setStrokeWidth(mStateSize);
  }

  public void setStateColor(int mStateColor) {
    this.mStateColor = mStateColor;
    mStatePaint.setColor(mStateColor);
  }

  public void setUnit(String mUnit) {
    this.mUnit = mUnit;
  }

  public void setDrawHealthState(boolean mDrawState) {
    this.mDrawState = mDrawState;
  }

  public void setStartColor(int mStartColor) {
    this.mStartColor = mStartColor;
  }

  public void setEndColor(int mEndColor) {
    this.mEndColor = mEndColor;
  }

  public void setBottomArcColor(int mBottomArcColor) {
    this.mBottomArcColor = mBottomArcColor;
    mBottomArcPaint.setColor(mBottomArcColor);
  }

  public void setTopArcColor(int mTopArcColor) {
    this.mTopArcColor = mTopArcColor;
    mTopArcPaint.setColor(mTopArcColor);
  }

  public void setArcWidth(float mArcWidth) {
    this.mArcWidth = mArcWidth;
    mTopArcPaint.setStrokeWidth(mArcWidth);
    mBottomArcPaint.setStrokeWidth(mArcWidth);
  }

  public void setDigitColor(int mDigitColor) {
    this.mDigitColor = mDigitColor;
    mDigitPaint.setColor(mDigitColor);
  }

  public void setDigitSize(int mDigitSize) {
    this.mDigitSize = mDigitSize;
    mDigitPaint.setTextSize(mDigitSize);
  }

  public void setUnitColor(int mUnitColor) {
    this.mUnitColor = mUnitColor;
    mUnitPaint.setColor(mUnitColor);
  }

  public void setUnitSize(int mUnitSize) {
    this.mUnitSize = mUnitSize;
    mUnitPaint.setTextSize(mUnitSize);
  }

  public void setRadius(int mRadius) {
    this.mRadius = mRadius;
  }

  public ScoreSectorView(Context context) {
    this(context, null);
  }

  public ScoreSectorView(Context context, AttributeSet attr) {
    this(context, attr, 0);
  }

  public ScoreSectorView(Context context, AttributeSet attr, int defStyleAttr) {
    super(context, attr, defStyleAttr);
    TypedArray ta = context.obtainStyledAttributes(attr, R.styleable.ScoreSectorView, defStyleAttr, 0);
    mBottomArcColor = ta.getColor(R.styleable.ScoreSectorView_bottomArcColor, 0xFF1D3C08);
    mTopArcColor = ta.getColor(R.styleable.ScoreSectorView_topArcColor, 0xffffee00);
    mArcWidth = ta.getDimension(R.styleable.ScoreSectorView_arcWidth, dp2px(context, 13));
    mDigitColor = ta.getColor(R.styleable.ScoreSectorView_digitTextColor, DEFAULT_COLOR);
    mDigitSize = ta.getDimensionPixelSize(R.styleable.ScoreSectorView_digitTextSize, sp2px(context, 48));
    mUnitColor = ta.getColor(R.styleable.ScoreSectorView_unitTextColor, 0xFF58B008);
    mUnitSize = ta.getDimensionPixelSize(R.styleable.ScoreSectorView_unitTextSize, sp2px(context, 24));
    mUnit = ta.getString(R.styleable.ScoreSectorView_unitText);
    mStateColor = ta.getColor(R.styleable.ScoreSectorView_stateColor, DEFAULT_COLOR);
    mStateSize = ta.getDimensionPixelSize(R.styleable.ScoreSectorView_stateSize, sp2px(context, 20));
    mRadius = ta.getDimensionPixelOffset(R.styleable.ScoreSectorView_radius, dp2px(context, 83));
    mDrawState = ta.getBoolean(R.styleable.ScoreSectorView_drawState, false);
    mStartColor = ta.getColor(R.styleable.ScoreSectorView_startColor, 0xFF58B008);
    mEndColor = ta.getColor(R.styleable.ScoreSectorView_endColor, 0xFFECFFD5);

    if (TextUtils.isEmpty(mUnit)) {
      mUnit = "分";
    }
    mTopArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mTopArcPaint.setStyle(Paint.Style.STROKE);
    mTopArcPaint.setStrokeWidth(mArcWidth);
    mTopArcPaint.setStrokeCap(Paint.Cap.ROUND);

    mBottomArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mBottomArcPaint.setStyle(Paint.Style.STROKE);
    mBottomArcPaint.setStrokeWidth(mArcWidth);
    mBottomArcPaint.setColor(mBottomArcColor);
    mBottomArcPaint.setStrokeCap(Paint.Cap.ROUND);

    mDigitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mDigitPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    mDigitPaint.setTextSize(mDigitSize);
    mDigitPaint.setColor(mDigitColor);

    mUnitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mUnitPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    mUnitPaint.setTextSize(mUnitSize);
    mUnitPaint.setColor(mUnitColor);

    mStatePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mStatePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    mStatePaint.setTextSize(mStateSize);
    mStatePaint.setColor(mStateColor);

    mArcRectF = new RectF();

    mDigitBound = new Rect();
    mUnitBound = new Rect();
    mStateBound = new Rect();

    mOnClickListener = new OnClickListener() {
      @Override
      public void onClick(View v) {
        animateScore(0, mScore);
      }
    };

    setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mOnClickListener != null) {
          mOnClickListener.onClick(ScoreSectorView.this);
        }
      }
    });
  }


  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int w = measureDimension(widthMeasureSpec);
    int h = measureDimension(heightMeasureSpec);
    mRadius = Math.min(w, h) / 2;
    setMeasuredDimension(w, h);
  }

  private int measureDimension(int measureSpec) {
    int result;
    int specMode = MeasureSpec.getMode(measureSpec);
    int specSize = MeasureSpec.getSize(measureSpec);
    if (specMode == MeasureSpec.EXACTLY) {
      result = specSize;
    } else {
      result = 2 * mRadius;
      if (specMode == MeasureSpec.AT_MOST) {
        result = Math.min(result, specSize);
      }
    }
    return result;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    mArcRectF.set(
        getWidth() / 2 - mRadius + mArcWidth / 2,
        getHeight() / 2 - mRadius + mArcWidth / 2,
        getWidth() / 2 + mRadius - mArcWidth / 2,
        getHeight() / 2 + mRadius - mArcWidth / 2
    );
    //canvas.drawBitmap(mCenterBitmap, 0, 0, mCenterBitmapPaint);

    //画底部圆弧
    canvas.drawArc(mArcRectF, 0, 360, true, mBottomArcPaint);

    //画顶部变化的圆弧
    linearGradient = new LinearGradient(getWidth(), getHeight() / 2, 0, getHeight() / 2,
        mStartColor,
        mEndColor,
        LinearGradient.TileMode.CLAMP);

    mTopArcPaint.setShader(linearGradient);
    canvas.drawArc(mArcRectF, 270, 360 * mCurrentScore / MAX_SCORE, false, mTopArcPaint);

    String digit = String.valueOf((int) mCurrentScore);
    mDigitPaint.getTextBounds(digit, 0, String.valueOf(digit).length(), mDigitBound);
    mUnitPaint.getTextBounds(mUnit, 0, String.valueOf(mUnit).length(), mUnitBound);
    if (mDrawState) {
      String healthState = getHealthState(mCurrentScore);
      mStatePaint.getTextBounds(healthState, 0, String.valueOf(healthState).length(), mStateBound);
      canvas.drawText(digit,
          getWidth() / 2 - mDigitBound.width() / 2 - mUnitBound.width() / 2,
          getHeight() / 2 - 1,
          mDigitPaint);
      canvas.drawText(mUnit,
          getWidth() / 2 + mDigitBound.width() / 2 - 6,
          getHeight() / 2 - 1,
          mUnitPaint);
      canvas.drawText(healthState,
          getWidth() / 2 - mStateBound.width() / 2,
          getHeight() / 2 + mStateBound.height() + 4,
          mStatePaint);
    } else {
      canvas.drawText(digit,
          getWidth() / 2 - mDigitBound.width() / 2 - mUnitBound.width() / 2,
          getHeight() / 2 + mDigitBound.height() / 2 - 4,
          mDigitPaint);
      canvas.drawText(mUnit,
          getWidth() / 2 + mDigitBound.width() / 2 - 4,
          getHeight() / 2 + mDigitBound.height() / 2 - 2,
          mUnitPaint);
    }
  }

  public void animateScore(float start, float end) {
    mScore = end;
    ValueAnimator animator = ValueAnimator.ofFloat(start, end);
    animator.setInterpolator(new BounceInterpolator());
    //动画时长由百分比大小决定
    animator.setDuration((long) Math.abs(end - start) * 16);
    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        float value = (float) animation.getAnimatedValue();
        mCurrentScore = (float) (Math.round(value * 10)) / 10;
        invalidate();
      }
    });
    animator.start();
  }

  public void animateScore(){
    animateScore(mScore);
  }

  public void animateScore(float score){
    animateScore(0, score);
  }

  public void setOnCircleClickListener(OnClickListener onClickListener) {
    mOnClickListener = onClickListener;
  }

  private String getHealthState(float score) {
    if (score <= 48) {
      return mHealthState[3];
    } else if (score <= 72) {
      return mHealthState[2];
    } else if (score <= 84) {
      return mHealthState[1];
    } else {
      return mHealthState[0];
    }
  }
  private static int sp2px(Context context, float spVal) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
            spVal, context.getResources().getDisplayMetrics());
  }

  private static int dp2px(Context context, float dpVal) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            dpVal, context.getResources().getDisplayMetrics());
  }
}