package xh.snake;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

/**
 * Created by G1494458 on 2017/5/18.
 */

public class SnakeView extends View{

    public static int borderWidth;
    public static int borderHeight;

    private float mBorderWidth;
    private WalkGround mWalkGround = new WalkGround();
    private Snake mSnake;
    private Food mFood;
    boolean isDrawFood;
    static boolean isInitFood;

    private Paint mBorderPaint;
    private Paint mSnakePaint;
    private Paint mFoodPaint;

    public SnakeView(Context context) {
        super(context);
    }

    public SnakeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SnakeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SnakeView);
        mBorderWidth = a.getDimension(R.styleable.SnakeView_borderWidth, 0);
        a.recycle();

        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mSnakePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSnakePaint.setColor(Color.RED);
        mFoodPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFoodPaint.setColor(Color.GREEN);

//        mSnake = new Snake();
        mFood = new Food();
    }

    public void setSnake(Snake snake) {
        mSnake = snake;
    }

    public static void setInitFood(boolean initFood) {
        isInitFood = initFood;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawWall(canvas);
        drawSnake(canvas);
        drawFood(canvas);

    }

    private void drawFood(Canvas canvas) {
        if (!isInitFood) {
            mFood.init(mSnake, mWalkGround.getWidth(), mWalkGround.getHeight());
            isInitFood = true;
        }
        mFood.draw(canvas, mFoodPaint);
    }

    private void drawWall(Canvas canvas) {
        float outLeft = getLeft();
        float outRight = getRight();
        float outTop = getTop();
        float outBottom = getBottom();
        float inLeft = outLeft + mBorderWidth;
        float inRight = outRight - mBorderWidth;
        float inTop = outTop + mBorderWidth;
        float inBottom = outBottom - mBorderWidth;
        mWalkGround.set(inLeft, inTop, inRight, inBottom);
        mSnake.setWalkGround(mWalkGround);

        mBorderPaint.setColor(Color.YELLOW);
        canvas.drawRect(outLeft, outTop, outRight, outBottom, mBorderPaint);
        mBorderPaint.setColor(Color.WHITE);
        canvas.drawRect(inLeft, inTop, inRight, inBottom, mBorderPaint);
    }

    private void drawSnake(Canvas canvas) {
        for (Snake.Node node = mSnake.getHeader(); node != null; node = node.next) {
            canvas.drawRect(node.getLeft(), node.getTop(), node.getRight(), node.getBottom(), mSnakePaint);
        }
    }

    public class WalkGround {
        float left;
        float top;
        float right;
        float bottom;

        void set(float left, float top, float right, float bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        int getWidth() {
            return (int) (right - left);
        }

        int getHeight() {
            return (int) (bottom - top);
        }
    }
}
