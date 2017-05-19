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
    private Rect bounds = new Rect();

    Paint mScorePaint;

    public ScorePanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        scoreText = "Score";
        float scoreSize = getContext().getResources().getDimensionPixelSize(R.dimen.score_title);
        mScorePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScorePaint.setTextSize(scoreSize);
        mScorePaint.setColor(Color.RED);
        mScorePaint.setTextAlign(Paint.Align.LEFT);
        mScorePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mScorePaint.getTextBounds(scoreText, 0, scoreText.length(), bounds);
        float y = getPaddingTop() + bounds.height();

        canvas.drawText(scoreText, 0, y, mScorePaint);
    }
}
