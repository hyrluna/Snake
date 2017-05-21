package xh.snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by G1494458 on 2017/5/18.
 */

public class ControllerView extends ViewGroup {

    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int TOP = 3;
    public static final int BOTTOM = 4;

    private int radius;

    private Button btnLeft;
    private Button btnRight;
    private Button btnTop;
    private Button btnBottom;

    public ControllerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void init(Context context, @Nullable AttributeSet attrs) {

        radius = (int) getContext().getResources().getDimension(R.dimen.controller_radius);
        Drawable backgroundL = context.getResources().getDrawable(R.drawable.btn_l_select_shape, context.getTheme());
        Drawable backgroundT = context.getResources().getDrawable(R.drawable.btn_t_select_shape, context.getTheme());
        Drawable backgroundR = context.getResources().getDrawable(R.drawable.btn_r_select_shape, context.getTheme());
        Drawable backgroundB = context.getResources().getDrawable(R.drawable.btn_b_select_shape, context.getTheme());

        btnLeft = new Button(context);
        btnLeft.setText("L");
        btnLeft.setTag(LEFT);
        btnLeft.setBackground(backgroundL);
        addView(btnLeft);

        btnTop = new Button(context);
        btnTop.setText("T");
        btnTop.setTag(TOP);
        btnTop.setBackground(backgroundT);
        addView(btnTop);

        btnRight = new Button(context);
        btnRight.setText("R");
        btnRight.setTag(RIGHT);
        btnRight.setBackground(backgroundR);
        addView(btnRight);

        btnBottom = new Button(context);
        btnBottom.setText("B");
        btnBottom.setTag(BOTTOM);
        btnBottom.setBackground(backgroundB);
        addView(btnBottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wrapWidth = radius * 8;
        int wrapHeight = radius * 6;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(wrapWidth, wrapHeight);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(wrapWidth, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, wrapHeight);
        } else {
            setMeasuredDimension(widthSize, heightSize);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int centerX = w / 2;
        int centerY = h / 2;
        int btnLeftX = centerX - radius * 2;
        int btnLeftY = centerY;
        int btnRightX = centerX + radius * 2;
        int btnRightY = centerY;
        int btnTopX = centerX;
        int btnTopY = centerY - radius * 2 + radius / 2;
        int btnBottomX = centerX;
        int btnBottomY = centerY + radius * 2 - radius / 2;
        buttonLayout(btnLeft, btnLeftX, btnLeftY);
        buttonLayout(btnRight, btnRightX, btnRightY);
        buttonLayout(btnTop, btnTopX, btnTopY);
        buttonLayout(btnBottom, btnBottomX, btnBottomY);
    }

    public void buttonLayout(Button btn, int x, int y) {
        int r = radius;
        btn.layout(x - r, y - r, x + r, y + r);
    }

    public void setOnBtnClickListener(final OnBtnClickListener listener) {
        class BtnListener implements OnClickListener {

            @Override
            public void onClick(View v) {
                int vTag = (int) v.getTag();
                switch (vTag) {
                    case LEFT:
                        listener.onLeft(v);
                        break;
                    case RIGHT:
                        listener.onRight(v);
                        break;
                    case TOP:
                        listener.onTop(v);
                        break;
                    case BOTTOM:
                        listener.onBottom(v);
                        break;
                }
            }
        }

        BtnListener btnListener = new BtnListener();

        btnLeft.setOnClickListener(btnListener);
        btnRight.setOnClickListener(btnListener);
        btnTop.setOnClickListener(btnListener);
        btnBottom.setOnClickListener(btnListener);

    }

    public interface OnBtnClickListener {
        void onLeft(View v);
        void onRight(View v);
        void onTop(View v);
        void onBottom(View v);
    }

//    public void setLeftListener(OnClickListener listener) {
//        btnLeft.setOnClickListener(listener);
//    }
//
//    public void setRightListener(OnClickListener listener) {
//        btnRight.setOnClickListener(listener);
//    }
//
//    public void setTopListener(OnClickListener listener) {
//        btnTop.setOnClickListener(listener);
//    }
//
//    public void setBottomListener(OnClickListener listener) {
//        btnBottom.setOnClickListener(listener);
//    }

}
