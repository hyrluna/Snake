package xh.snake;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.Random;

/**
 * Created by bamboo on 17-5-18.
 */

public class Food {
    private static final Random random = new Random(48);
    PointF position;
    RectF foodRect;

    public Food() {
        position = new PointF();
        foodRect = new RectF();
    }

    public void init(Snake snake, int rightBorder, int bottomBorder, int border) {
        int step = (int) Snake.STEP;
        int xRand = random.nextInt(rightBorder);
        int yRand = random.nextInt(bottomBorder);

        xRand -= xRand % step;
        yRand -= yRand % step;

        float x = border + step / 2 + xRand;
        float y = border + step / 2 + yRand;


        for (Snake.Node node = snake.getHeader(); node != null; node = node.next) {
            if (x == node.x && y == node.y) {
                init(snake, rightBorder, bottomBorder, border);
                return;
            }
        }
        position.set(x, y);
        snake.setFood(position);
        float left = x - step / 2 + Snake.PADDING;
        float right = x + step / 2 - Snake.PADDING;
        float top = y - step / 2 + Snake.PADDING;
        float bottom = y + step / 2 - Snake.PADDING;
        foodRect.set(left, top, right, bottom);
    }

    public void draw(Canvas canvas, Paint paint, Bitmap bitmap) {
        canvas.drawBitmap(bitmap, foodRect.left, foodRect.top, paint);
    }
}
