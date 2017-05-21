package xh.snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by G1494458 on 2017/5/19.
 */

public class ScorePanel extends View {

    String scoreText;
    String valueText;
    private Rect bounds = new Rect();

    Paint mScorePaint;
    Paint mValuePaint;

    public ScorePanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        scoreText = "Score: ";
        float scoreSize = getContext().getResources().getDimensionPixelSize(R.dimen.score_title);
        mScorePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScorePaint.setTextSize(scoreSize);
        mScorePaint.setColor(Color.WHITE);
        mScorePaint.setTextAlign(Paint.Align.LEFT);
        mScorePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        valueText = "0";
        mValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mValuePaint.setTextSize(scoreSize);
        mValuePaint.setColor(Color.WHITE);
        mValuePaint.setTextAlign(Paint.Align.CENTER);
        mValuePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mScorePaint.getTextBounds(scoreText, 0, scoreText.length(), bounds);
        //文字居中
        float y = (getPaddingTop() + getPaddingBottom() + getHeight() - bounds.height()) / 2 + bounds.height();
        float x = 50;
        canvas.drawText(scoreText, x, y, mScorePaint);
//        bounds.setEmpty();
        float xValue = getPaddingStart() + x + bounds.width() + 65;
        mValuePaint.getTextBounds(valueText, 0, valueText.length(), bounds);
        canvas.drawText(valueText, xValue, y, mValuePaint);
    }

    public String getScore() {
        return valueText;
    }

    public void setScore(String valueText) {
        this.valueText = valueText;
    }
}
