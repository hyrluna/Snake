package xh.snake;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

/**
 * Created by bamboo on 17-5-18.
 */

public class Food {
    private static final Random random = new Random(48);
    int left;
    int right;
    int top;
    int bottom;
    Point position;

    public Food() {
        position = new Point();
    }

    public void init(Snake snake, int borderWidth, int borderHeight) {
        int step = (int) Snake.STEP;
        int x = random.nextInt(borderWidth);
        int y = random.nextInt(borderHeight);
        x -= x % step;
        y -= y % step;
        for (Snake.Node node = snake.getHeader(); node != null; node = node.next) {
            if (x == node.x && y == node.y) {
                init(snake, borderWidth, borderHeight);
                return;
            }
        }
        position.set(x, y);
        snake.setFood(position);
        left = x - step / 2 + Snake.PADDING;
        right = x + step / 2 - Snake.PADDING;
        top = y - step / 2 + Snake.PADDING;
        bottom = y + step / 2 - Snake.PADDING;
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawRect(left, top, right, bottom, paint);
    }
}
