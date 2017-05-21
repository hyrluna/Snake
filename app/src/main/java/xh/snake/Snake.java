package xh.snake;

import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by G1494458 on 2017/5/18.
 */

public class Snake {
    public static final float STEP = 60;
    public static final int PADDING = 2;
    public static final int MARK_HEAD = 0;
    public static final int MARK_TAIL = 1;
    private static final float NODE_SIZE = STEP;
    private Set<Node> nodes = new HashSet<>();
    private List<Node> turnNodes = new ArrayList<>();
    private Node header;
    private Node tail;
    private SnakeView.WalkGround walkGround;
    private ScorePanel scorePanel;
    private int score = 0;
    private int speed = 0;

    private PointF foodPosition;
    private boolean isAlive = true;

    public Snake(float borderWidth) {
        init(borderWidth);
    }

    public void init(float borderWidth) {
        float x = borderWidth + STEP / 2 + 180;
        float y = borderWidth + STEP / 2 + 600;
        header = new Node(x, y, Direction.TOP);
        header.setMark(MARK_HEAD);
        Node body1 = new Node(x, y + NODE_SIZE, header.direction);
        header.next = body1;
        body1.pre = header;
        tail = new Node(x, y + NODE_SIZE + NODE_SIZE, body1.direction);
        tail.setMark(MARK_TAIL);
        body1.next = tail;
        tail.pre = body1;
//        addNode(new Node(x, y + NODE_SIZE*2, -1));
        addNode(new Node(x, y + NODE_SIZE*3, -1));
//        addNode(new Node(x, y + NODE_SIZE*4, -1));
//        addNode(new Node(x, y + NODE_SIZE*5, -1));
//        addNode(new Node(x, y + NODE_SIZE*6, -1));

    }

    public Node getHeader() {
        return header;
    }

    public void setFood(PointF point) {
        this.foodPosition = point;
    }

    public void setScorePanel(ScorePanel scorePanel) {
        this.scorePanel = scorePanel;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void addNode(Node node) {
        node.direction = tail.direction;
        tail.next = node;
        tail.clearMark();
        node.pre = tail;
        tail = node;
        tail.setMark(MARK_TAIL);
    }

    public void move(int direction) {
        int oldDirection = header.direction;
        int newDirection;
        boolean isEatFood = false;
        Node oldTailNode = new Node(tail.x, tail.y, tail.direction);
        Node dirtyNode = new Node(0, 0, -1);

        switch (direction) {
            case Direction.LEFT:
                if (header.direction != Direction.LEFT && header.direction != Direction.RIGHT) {
                    header.direction = Direction.LEFT;
                }
                break;
            case Direction.RIGHT:
                if (header.direction != Direction.LEFT && header.direction != Direction.RIGHT) {
                    header.direction = Direction.RIGHT;
                }
                break;
            case Direction.TOP:
                if (header.direction != Direction.TOP && header.direction != Direction.BOTTOM) {
                    header.direction = Direction.TOP;
                }
                break;
            case Direction.BOTTOM:
                if (header.direction != Direction.TOP && header.direction != Direction.BOTTOM) {
                    header.direction = Direction.BOTTOM;
                }
                break;
        }
        newDirection = header.direction;
        if (oldDirection != newDirection) {
            turnNodes.add(new Node(header.x, header.y, header.direction));
        }

        if (!isOutBorder(newDirection) && !isEatSelf()) {
            for (Node node = header; node != null; node = node.next) {

                if (Math.abs(header.x - foodPosition.x) < 1 && Math.abs(header.y - foodPosition.y) < 1) {
                    isEatFood = true;
                    SnakeView.setInitFood(false);
                }

                for (Node turnNode : turnNodes) {
                    if (node.x == turnNode.x && node.y == turnNode.y) {
                        node.direction = turnNode.direction;
                    }
                    if (tail.x == turnNode.x && tail.y == turnNode.y) {
                        dirtyNode = turnNode;
                    }
                }
                switch (node.direction) {
                    case Direction.LEFT:
                        node.x -= STEP;
                        break;
                    case Direction.RIGHT:
                        node.x += STEP;
                        break;
                    case Direction.TOP:
                        node.y -= STEP;
                        break;
                    case Direction.BOTTOM:
                        node.y += STEP;
                        break;
                }
            }
        } else {
            isAlive  = false;
        }

        if (isEatFood) {
            isEatFood = false;
            score += 10;
            speed += 5;
            scorePanel.setScore(score + "");
            scorePanel.postInvalidate();
            addNode(oldTailNode);
        }

        //移除不需要的拐点
        turnNodes.remove(dirtyNode);
    }

    public boolean isOutBorder(int direction) {
        switch (direction) {
            case Direction.LEFT:
                return header.x - STEP < walkGround.left;
            case Direction.RIGHT:
                return header.x + STEP > walkGround.right;
            case Direction.TOP:
                return header.y - STEP < walkGround.top;
            case Direction.BOTTOM:
                return header.y + STEP > walkGround.bottom;
            default:
                return false;
        }
//        if (header.x < walkGround.left
//                || header.x > walkGround.right
//                || header.y < walkGround.top
//                || header.y > walkGround.bottom) {
//            return true;
//        } else {
//            return false;
//        }
    }

    public boolean isEatSelf() {
        for (Node node = header.next; node != null; node = node.next) {
            if (header.x == node.x && header.y == node.y) {
                return true;
            }
        }
        return false;
    }

    public int getSpeed() {
        return speed;
    }

    public void setWalkGround(SnakeView.WalkGround walkGround) {
        this.walkGround = walkGround;
    }

    public static class Node {
        float x;
        float y;

        Node pre;
        Node next;
        int direction;

        int mark = -1;

        public Node(float x, float y, int direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
        }

        public int getMark() {
            return mark;
        }

        public void setMark(int mark) {
            this.mark = mark;
        }

        public void clearMark() {
            mark = -1;
        }

        public float getLeft() {
            return x - NODE_SIZE / 2 + PADDING;
        }

        public float getRight() {
            return x + NODE_SIZE / 2 - PADDING;
        }

        public float getTop() {
            return y - NODE_SIZE / 2 + PADDING;
        }

        public float getBottom() {
            return y + NODE_SIZE / 2 - PADDING;
        }
    }

    public static class Direction {
        static final int LEFT = 0;
        static final int RIGHT = 1;
        static final int TOP = 2;
        static final int BOTTOM = 3;
    }
}
