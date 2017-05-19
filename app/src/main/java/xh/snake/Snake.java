package xh.snake;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by G1494458 on 2017/5/18.
 */

public class Snake {
    public static final float STEP = 50;
    public static final int PADDING = 5;
    private static final float NODE_SIZE = STEP;
    private Set<Node> nodes = new HashSet<>();
    private List<Node> turnNodes = new ArrayList<>();
    private Node header;
    private Node tail;
    private SnakeView.WalkGround walkGround;
    private Point foodPosition;
    private boolean isAlive = true;

    public Snake() {
        init();
    }

    public void init() {
        header = new Node(200, 300, Direction.TOP);
        Node body1 = new Node(200, 300 + NODE_SIZE, header.direction);
        header.next = body1;
        body1.pre = header;
        tail = new Node(200, 300 + NODE_SIZE + NODE_SIZE, body1.direction);
        body1.next = tail;
        tail.pre = body1;
        addNode(new Node(200, 300 + NODE_SIZE + NODE_SIZE, -1));
        addNode(new Node(200, 300 + NODE_SIZE*3, -1));
        addNode(new Node(200, 300 + NODE_SIZE*4, -1));
        addNode(new Node(200, 300 + NODE_SIZE*5, -1));
        addNode(new Node(200, 300 + NODE_SIZE*6, -1));
        addNode(new Node(200, 300 + NODE_SIZE*7, -1));
        addNode(new Node(200, 300 + NODE_SIZE*8, -1));

    }

    public Node getHeader() {
        return header;
    }

    public void setFood(Point point) {
        this.foodPosition = point;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void addNode(Node node) {
        node.direction = tail.direction;
        tail.next = node;
        node.pre = tail;
        tail = node;
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

        if (!isOutBorder() && !isEatSelf()) {
            for (Node node = header; node != null; node = node.next) {

                if (header.x == foodPosition.x && header.y == foodPosition.y) {
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
            addNode(oldTailNode);
        }

        //移除不需要的拐点
        turnNodes.remove(dirtyNode);
    }

    public boolean isOutBorder() {
        if (header.x < walkGround.left
                || header.x > walkGround.right
                || header.y < walkGround.top
                || header.y > walkGround.bottom) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isEatSelf() {
        for (Node node = header.next; node != null; node = node.next) {
            if (header.x == node.x && header.y == node.y) {
                return true;
            }
        }
        return false;
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

        public Node(float x, float y, int direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
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
