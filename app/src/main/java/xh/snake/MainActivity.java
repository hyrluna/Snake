package xh.snake;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements ControllerView.OnBtnClickListener {

    private static final int SPEED = 50;
    private static final int REFRESH_FREQUENCY = 10;

    private SnakeView mSnakeView;
    private ControllerView mControllerView;
    private Snake mSnake;
    private int mDirection;
    private int mOldDirection;
    private TimeTask timeTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSnakeView = (SnakeView) findViewById(R.id.snake_view);
        mControllerView = (ControllerView) findViewById(R.id.controller_view);
        mControllerView.setOnBtnClickListener(this);
        timeTask = new TimeTask();
        timeTask.execute();
    }

    @Override
    public void onLeft(View v) {
        mOldDirection = mDirection;
        mDirection = Snake.Direction.LEFT;
    }

    @Override
    public void onRight(View v) {
        mOldDirection = mDirection;
        mDirection = Snake.Direction.RIGHT;
    }

    @Override
    public void onTop(View v) {
        mOldDirection = mDirection;
        mDirection = Snake.Direction.TOP;
    }

    @Override
    public void onBottom(View v) {
        mOldDirection = mDirection;
        mDirection = Snake.Direction.BOTTOM;
    }

//    @Override
//    public void onClick(View v) {
//        mOldDirection = mDirection;
//        switch (v.getId()) {
//            case R.id.left:
//                mDirection = Snake.Direction.LEFT;
//                break;
//            case R.id.right:
//                mDirection = Snake.Direction.RIGHT;
//                break;
//            case R.id.top:
//                mDirection = Snake.Direction.TOP;
//                break;
//            case R.id.bottom:
//                mDirection = Snake.Direction.BOTTOM;
//                break;
//        }
//    }

    private class TimeTask extends AsyncTask<Void, Void, Void> {
        int sleepCount = 0;

        @Override
        protected void onPreExecute() {
            mSnake = new Snake(mSnakeView.getmBorderWidth());
            mSnakeView.setSnake(mSnake);
            mDirection = Snake.Direction.TOP;
            mOldDirection = mDirection;
            SnakeView.setInitFood(false);
            mSnakeView.invalidate();
        }

        @Override
        protected Void doInBackground(Void... params) {

            //初始化延时
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            while (mSnake.isAlive()) {
                try {
                    Thread.sleep(REFRESH_FREQUENCY);
                    sleepCount ++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //减少睡眠时间，提高按钮事件的响应速度
                if (sleepCount == SPEED || mOldDirection != mDirection) {
                    sleepCount = 0;
                    mOldDirection = mDirection;
                    mSnake.move(mDirection);
                    publishProgress();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            mSnakeView.invalidate();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            showGameOver();
        }
    }

    private void showGameOver() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("GameOver")
                .setMessage("是否重新开始游戏?")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        timeTask.cancel(true);
                        timeTask = null;
                        timeTask = new TimeTask();
                        timeTask.execute();
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

}
