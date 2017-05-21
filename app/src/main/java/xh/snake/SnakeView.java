package xh.snake;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by G1494458 on 2017/5/18.
 */

public class SnakeView extends View{

    private float mBorderWidth;
    private WalkGround mWalkGround = new WalkGround();
    private Snake mSnake;
    private ScorePanel mScorePanel;
    private Food mFood;
    boolean isDrawFood;
    static boolean isInitFood;
    boolean isCompressed;

    Bitmap bitmapWalkGround;
//    Bitmap bitmapWall;
    Bitmap bitmapFood;
    Bitmap bitmapSnakeHeader;
    Bitmap bitmapSnakeHeaderTop;
    Bitmap bitmapSnakeHeaderBottom;
    Bitmap bitmapSnakeHeaderRight;
    Bitmap bitmapSnakeHeaderLeft;
    Bitmap bitmapSnakeBody;
    Bitmap bitmapSnakeTail;
    Bitmap bitmapSnakeTailTop;
    Bitmap bitmapSnakeTailBottom;
    Bitmap bitmapSnakeTailRight;
    Bitmap bitmapSnakeTailLeft;

    private Paint mBorderPaint;
    private Paint mWalkGroundPaint;
    private Paint mSnakeBodyPaint;
    private Paint mSnakeHeadPaint;
    private Paint mSnakeTailPaint;
    private Paint mFoodPaint;
    private Paint mRedundantPaint;
    Matrix matrix;

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

        bitmapWalkGround = BitmapFactory.decodeResource(getResources(), R.drawable.walk_ground);
//        bitmapWall = BitmapFactory.decodeResource(getResources(), R.drawable.wall);
        bitmapSnakeHeader = BitmapFactory.decodeResource(getResources(), R.drawable.snakehead);
        bitmapSnakeBody = BitmapFactory.decodeResource(getResources(), R.drawable.greenstar);
        bitmapSnakeTail = BitmapFactory.decodeResource(getResources(), R.drawable.tail);
        bitmapFood = BitmapFactory.decodeResource(getResources(), R.drawable.applered);

        matrix = new Matrix();

        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setColor(Color.GREEN);

        mWalkGroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWalkGroundPaint.setStyle(Paint.Style.STROKE);

        mSnakeBodyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSnakeBodyPaint.setColor(Color.RED);

        mSnakeHeadPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSnakeTailPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mFoodPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFoodPaint.setColor(Color.GREEN);

        mRedundantPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRedundantPaint.setColor(context.getResources().getColor(android.R.color.holo_green_dark));

        mSnake = new Snake(mBorderWidth);
        mFood = new Food();
        isInitFood = false;
    }

    public void setScorePanel(ScorePanel panel) {
        mScorePanel = panel;
        mSnake.setScorePanel(panel);
    }

    public Snake getSnake() {
        return mSnake;
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
            mFood.init(mSnake, (int) mWalkGround.right, (int) mWalkGround.bottom, (int) mBorderWidth);
            isInitFood = true;
        }
        mFood.draw(canvas, mFoodPaint, bitmapFood);
    }

    private void drawWall(Canvas canvas) {
        float outLeft = getLeft();
        float outRight = getRight();
        float outTop = getTop();
        float outBottom = getBottom() - getBottom() % Snake.STEP;
        float inLeft = outLeft + mBorderWidth;
        float inRight = outRight - mBorderWidth;
        float inTop = outTop + mBorderWidth;
        float inBottom = outBottom - mBorderWidth;
        mWalkGround.set(inLeft, inTop, inRight, inBottom);
        mSnake.setWalkGround(mWalkGround);

        canvas.drawRect(outLeft, outTop, outRight, outBottom, mBorderPaint);
        canvas.drawRect(outLeft, outBottom, outRight, outBottom + getBottom() % Snake.STEP, mRedundantPaint);
//        mBorderPaint.setColor(Color.WHITE);
//        canvas.drawRect(inLeft, inTop, inRight, inBottom, mBorderPaint);

        if (!isCompressed) {
            bitmapWalkGround = Bitmap.createScaledBitmap(bitmapWalkGround, (int) (inRight - inLeft), (int) (inBottom - inTop), true);
//            bitmapWall = Bitmap.createScaledBitmap(bitmapWall, (int) (outRight - outLeft), (int) (outBottom - outTop), true);

            int scaleSize = (int) Snake.STEP - Snake.PADDING;
            Bitmap scaleHeader = Bitmap.createScaledBitmap(bitmapSnakeHeader, scaleSize, scaleSize, true);
            bitmapSnakeHeaderBottom = scaleHeader;
            matrix.postRotate(180);
            bitmapSnakeHeaderTop = Bitmap.createBitmap(scaleHeader, 0, 0, scaleHeader.getWidth(), scaleHeader.getHeight(), matrix, true);
            matrix.reset();
            matrix.postRotate(-90);
            bitmapSnakeHeaderRight = Bitmap.createBitmap(scaleHeader, 0, 0, scaleHeader.getWidth(), scaleHeader.getHeight(), matrix, true);
            matrix.reset();
            matrix.postRotate(90);
            bitmapSnakeHeaderLeft = Bitmap.createBitmap(scaleHeader, 0, 0, scaleHeader.getWidth(), scaleHeader.getHeight(), matrix, true);

            bitmapSnakeBody = Bitmap.createScaledBitmap(bitmapSnakeBody, scaleSize, scaleSize, true);

            matrix.reset();
            Bitmap scaleTail = Bitmap.createScaledBitmap(bitmapSnakeTail, scaleSize, scaleSize, true);
            bitmapSnakeTailBottom = scaleTail;
            matrix.postRotate(180);
            bitmapSnakeTailTop = Bitmap.createBitmap(scaleTail, 0, 0, scaleTail.getWidth(), scaleTail.getHeight(), matrix, true);
            matrix.reset();
            matrix.postRotate(-90);
            bitmapSnakeTailRight = Bitmap.createBitmap(scaleTail, 0, 0, scaleTail.getWidth(), scaleTail.getHeight(), matrix, true);
            matrix.reset();
            matrix.postRotate(90);
            bitmapSnakeTailLeft = Bitmap.createBitmap(scaleTail, 0, 0, scaleTail.getWidth(), scaleTail.getHeight(), matrix, true);

            bitmapFood = Bitmap.createScaledBitmap(bitmapFood, scaleSize, scaleSize, true);

            isCompressed = true;
        }
//        int bitmapWidth = bitmapWalkGround.getWidth();
//        int bitmapHeight = bitmapWalkGround.getHeight();
//        canvas.drawBitmap(bitmapWall, outLeft, outTop, mBorderPaint);
        canvas.drawBitmap(bitmapWalkGround, inLeft, inTop, mWalkGroundPaint);
    }

    private void drawSnake(Canvas canvas) {
        for (Snake.Node node = mSnake.getHeader(); node != null; node = node.next) {
            if (node.getMark() == Snake.MARK_HEAD) {
                switch (node.direction) {
                    case Snake.Direction.BOTTOM:
                        canvas.drawBitmap(bitmapSnakeHeaderBottom, node.getLeft(), node.getTop(), mSnakeHeadPaint);
                        break;
                    case Snake.Direction.TOP:
                        canvas.drawBitmap(bitmapSnakeHeaderTop, node.getLeft(), node.getTop(), mSnakeHeadPaint);
                        break;
                    case Snake.Direction.LEFT:
                        canvas.drawBitmap(bitmapSnakeHeaderLeft, node.getLeft(), node.getTop(), mSnakeHeadPaint);
                        break;
                    case Snake.Direction.RIGHT:
                        canvas.drawBitmap(bitmapSnakeHeaderRight, node.getLeft(), node.getTop(), mSnakeHeadPaint);
                        break;
                }
            } else if (node.getMark() == Snake.MARK_TAIL) {
                switch (node.direction) {
                    case Snake.Direction.BOTTOM:
                        canvas.drawBitmap(bitmapSnakeTailBottom, node.getLeft(), node.getTop(), mSnakeTailPaint);
                        break;
                    case Snake.Direction.TOP:
                        canvas.drawBitmap(bitmapSnakeTailTop, node.getLeft(), node.getTop(), mSnakeTailPaint);
                        break;
                    case Snake.Direction.LEFT:
                        canvas.drawBitmap(bitmapSnakeTailLeft, node.getLeft(), node.getTop(), mSnakeTailPaint);
                        break;
                    case Snake.Direction.RIGHT:
                        canvas.drawBitmap(bitmapSnakeTailRight, node.getLeft(), node.getTop(), mSnakeTailPaint);
                        break;
                }
            } else {
//                canvas.drawRect(node.getLeft(), node.getTop(), node.getRight(), node.getBottom(), mSnakeBodyPaint);
                canvas.drawBitmap(bitmapSnakeBody, node.getLeft(), node.getTop(), mSnakeBodyPaint);
            }
        }
    }

    public float getBorderWidth() {
        return mBorderWidth;
    }

    public void reset() {
        mSnake = null;
        mFood = null;
        mSnake = new Snake(mBorderWidth);
        mSnake.setScorePanel(mScorePanel);
        mFood = new Food();
        isInitFood = false;
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
