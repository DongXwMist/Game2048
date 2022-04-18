package com.dxw.game2048.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import com.dxw.game2048.ui.GameActivity;
import com.dxw.game2048.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class GameView extends GridLayout {

    private Context mContext;

    public GameView(Context context) {
        super(context);
        initGameView();
        this.mContext=context;
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
        this.mContext=context;
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameView();
        this.mContext=context;
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initGameView();
        this.mContext=context;
    }

    /**
     * 游戏初始化方法
     * */
    private void initGameView() {
        setColumnCount(Constant.GameColumnCount);//将面板设置成4列
        setBackgroundColor(0xffbbada0);
        setPadding(0,0,0,20);
        System.out.println("initGameView");

        addCards(getCardWidth(), getCardWidth());//添加卡片

        setOnTouchListener(new View.OnTouchListener() {
            /**
             * startX:手机刚开始在屏幕上的X坐标
             * startY:手机刚开始在屏幕上的Y坐标
             * offsetX,offsetY,分别是手指在屏幕上的X,Y上的偏移量
             */
            private float startX, startY, offsetX, offsetY;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = motionEvent.getX();
                        startY = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = motionEvent.getX() - startX;
                        offsetY = motionEvent.getY() - startY;

                        //if |offsetX| > |offsetY| 水平方向意图
                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            //left
                            if (offsetX < -5) {
                                swipeLeft();
                                System.out.println("left");
                            } else if (offsetX > 5) { //right
                                swipeRight();
                                System.out.println("right");
                            }
                        } else {
                            if (offsetY < -5) { //up
                                swipeUp();
                                System.out.println("up");
                            } else if (offsetY > 5) {//down
                                swipeDown();
                                System.out.println("down");
                            }
                        }
                        break;
                }

                return true;
            }
        });
    }

    /**
     * 当布局的尺寸发生变化时，会执行该方法
     * */
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //由于该函数在构造函数之后，也就是布局完成之后执行，所以不能在这里分配Card的长度和宽度
        //计算每张卡片的宽高
        /*int cardWidth = (Math.min(w, h) - 10) / 4;
        addCards(cardWidth, cardWidth);*/

        //开始游戏
        startGame();
    }

    /**
     * 获取屏幕宽度
     * */
    private int getCardWidth() {
        //屏幕信息的对象(声明屏幕对象)
        DisplayMetrics displayMetrics;
        displayMetrics = getResources().getDisplayMetrics();

        //获取屏幕信息
        int cardWidth;
        //提取屏幕宽
        cardWidth = displayMetrics.widthPixels;

        return (cardWidth - 20) / 4;
    }

    /**
     * 添加卡片的方法
     * */
    private void addCards(int cardWidth, int cardHeight) {
        Card c;
        //遍历每一张卡片
        for (int y = 0; y < Constant.GameColumnCount; y++) {
            for (int x = 0; x < Constant.GameColumnCount; x++) {
                c = new Card(getContext());
                c.setNum(0);//给每一张卡片赋值
                addView(c, cardWidth, cardHeight);
                System.out.println("GameView addCards执行");

                cardsMap[x][y] = c;
            }
        }
    }

    /**
     * 添加随机数
     * */
    private void addRandomNum() {

        emptyPoint.clear();

        for (int y = 0; y < Constant.GameColumnCount; y++) {
            for (int x = 0; x < Constant.GameColumnCount; x++) {
                //空点，只有空点才能添加值
                if (cardsMap[x][y].getNum() <= 0) {
                    emptyPoint.add(new Point(x, y));
                }
            }
        }

        if (emptyPoint.size()>0) {
            // 随机移除一个点
            Point p = emptyPoint.remove((int) (Math.random() * emptyPoint.size()));
            //随机位置生成一个card
            cardsMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);

            GameActivity.getMainActivity().getCardAnimation()
                    .createScaleTo1(cardsMap[p.x][p.y]);    //给新生成的Card添加动画效果
        }
    }

    /**
     * 开始游戏方法
     * */
    public void startGame() {

        //清除之前得分
        GameActivity.getMainActivity().clearScore();
        //清理阶段
        for (int y = 0; y < Constant.GameColumnCount; y++) {
            for (int x = 0; x < Constant.GameColumnCount; x++) {
                cardsMap[x][y].setNum(0);
            }
        }
        //添加随机数
        addRandomNum();
        addRandomNum();
    }

    /**
     * 检查游戏是否结束的方法
     * */
    public void checkComplete() {

        //记录游戏的状态
        boolean complete = true;

        ALL:
        for (int y = 0; y < Constant.GameColumnCount; y++) {
            for (int x = 0; x < Constant.GameColumnCount; x++) {
                //上下左右四个方向是否为空或者是具有相同值，则游戏继续
                if (cardsMap[x][y].getNum() == 0
                        || (x > 0 && cardsMap[x][y].equals(cardsMap[x - 1][y])) //左方向
                        || (y > 0 && cardsMap[x][y].equals(cardsMap[x][y - 1])) //下方向
                        || (x < Constant.GameColumnCount-1 && cardsMap[x][y].equals(cardsMap[x + 1][y])) //右方向
                        || (y < Constant.GameColumnCount-1 && cardsMap[x][y].equals(cardsMap[x][y + 1]))) { //上方向

                    complete = false;
                    break ALL;//跳出所有的循环
                }
            }
        }
        //判断游戏结束
        if (complete) {
            //发送游戏结束的广播
            sendGameOverMsg("Over");
        }
    }

    /**
     * 发送游戏结束消息
     */
    private void sendGameOverMsg(String action) {

        Intent intent = new Intent(action);
        getContext().sendBroadcast(intent);
    }


    /**
     * saveScore
     * */
    private void saveGameScore(){

    }




    /**
     * 从左向右遍历
     * */
    private void swipeLeft() {

        boolean meger = false;

        for (int y = 0; y < Constant.GameColumnCount; y++) {
            for (int x = 0; x < Constant.GameColumnCount; x++) {
                //向右遍历
                for (int xR = x + 1; xR < Constant.GameColumnCount; xR++) {
                    if (cardsMap[xR][y].getNum() > 0) {

                        //判断当前位置值
                        if (cardsMap[x][y].getNum() <= 0) { //当前位置为空

                            GameActivity.getMainActivity().getCardAnimation()
                                    .createMoveAnimate(cardsMap[xR][y],cardsMap[x][y],xR,x,y,y);

                            /*
                             *  将下标为（x，y）所在位置的卡片上的数字
                             * 设置为，坐标为(xR,y)所在位置的卡片上的值；
                             * 第二步，将坐标（xR，y）所在位置的卡片上的数字
                             * 设置为0（即：变成空卡片）
                             */
                            cardsMap[x][y].setNum(cardsMap[xR][y].getNum());
                            cardsMap[xR][y].setNum(0);

                            x--;
                            meger = true;
                        } else if (cardsMap[x][y].equals(cardsMap[xR][y])) { //当前位置值相同

                            GameActivity.getMainActivity().getCardAnimation()
                                    .createMoveAnimate(cardsMap[xR][y],cardsMap[x][y],xR,x,y,y);

                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[xR][y].setNum(0);

                            GameActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            meger = true;
                        }
                        break;
                    }
                }

            }
        }
        //如果有合并，添加随机数
        if (meger) {
            addRandomNum();
            checkComplete();
        }
    }

    //从右向左遍历
    private void swipeRight() {

        boolean meger = false;

        for (int y = 0; y < Constant.GameColumnCount; y++) {
            for (int x = Constant.GameColumnCount-1; x >= 0; x--) {
                //向左遍历
                for (int xL = x - 1; xL >= 0; xL--) {
                    if (cardsMap[xL][y].getNum() > 0) {

                        //判断当前位置值
                        if (cardsMap[x][y].getNum() <= 0) { //当前位置为空

                            GameActivity.getMainActivity().getCardAnimation()
                                    .createMoveAnimate(cardsMap[xL][y],cardsMap[x][y],xL,x,y,y);

                            /*
                             *  将下标为（x，y）所在位置的卡片上的数字
                             * 设置为，坐标为(xL,y)所在位置的卡片上的值；
                             * 第二步，将坐标（xL，y）所在位置的卡片上的数字
                             * 设置为0（即：变成空卡片）
                             */
                            cardsMap[x][y].setNum(cardsMap[xL][y].getNum());
                            cardsMap[xL][y].setNum(0);

                            x++;
                            meger = true;
                        } else if (cardsMap[x][y].equals(cardsMap[xL][y])) { //当前位置值相同

                            GameActivity.getMainActivity().getCardAnimation()
                                    .createMoveAnimate(cardsMap[xL][y],cardsMap[x][y],xL,x,y,y);

                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[xL][y].setNum(0);

                            GameActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            meger = true;
                        }
                        break;
                    }
                }

            }
        }
        //如果有合并，添加随机数
        if (meger) {
            addRandomNum();
            checkComplete();
        }
    }

    //从上向下遍历
    private void swipeUp() {

        boolean meger = false;

        for (int x = 0; x < Constant.GameColumnCount; x++) {
            for (int y = 0; y < Constant.GameColumnCount; y++) {
                //向下遍历
                for (int yD = y + 1; yD < Constant.GameColumnCount; yD++) {
                    if (cardsMap[x][yD].getNum() > 0) {

                        //判断当前位置值
                        if (cardsMap[x][y].getNum() <= 0) {//当前位置为空

                            GameActivity.getMainActivity().getCardAnimation()
                                    .createMoveAnimate(cardsMap[x][yD],cardsMap[x][y],x,x,yD,y);

                            /*
                             *  将下标为（x，y）所在位置的卡片上的数字
                             * 设置为，坐标为(x,yD)所在位置的卡片上的值；
                             * 第二步，将坐标（x，yD）所在位置的卡片上的数字
                             * 设置为0（即：变成空卡片）
                             */
                            cardsMap[x][y].setNum(cardsMap[x][yD].getNum());
                            cardsMap[x][yD].setNum(0);

                            y--;
                            meger = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x][yD])) {//当前位置值相同

                            GameActivity.getMainActivity().getCardAnimation()
                                    .createMoveAnimate(cardsMap[x][yD],cardsMap[x][y],x,x,yD,y);

                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][yD].setNum(0);

                            GameActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            meger = true;
                        }
                        break;
                    }
                }
            }
        }
        //如果有合并，添加随机数
        if (meger) {
            addRandomNum();
            checkComplete();
        }
    }

    //从下往上遍历
    private void swipeDown() {

        boolean meger = false;

        for (int x = 0; x < Constant.GameColumnCount; x++) {
            for (int y = Constant.GameColumnCount-1; y >= 0; y--) {
                //向下遍历
                for (int yU = y - 1; yU >= 0; yU--) {
                    if (cardsMap[x][yU].getNum() > 0) {

                        //判断当前位置值
                        if (cardsMap[x][y].getNum() <= 0) {//当前位置为空

                            GameActivity.getMainActivity().getCardAnimation()
                                    .createMoveAnimate(cardsMap[x][yU],cardsMap[x][y],x,x,yU,y);

                            /*
                             *  将下标为（x，y）所在位置的卡片上的数字
                             * 设置为，坐标为(x,yD)所在位置的卡片上的值；
                             * 第二步，将坐标（x，yD）所在位置的卡片上的数字
                             * 设置为0（即：变成空卡片）
                             */
                            cardsMap[x][y].setNum(cardsMap[x][yU].getNum());
                            cardsMap[x][yU].setNum(0);

                            y++;
                            meger = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x][yU])) {//当前位置值相同

                            GameActivity.getMainActivity().getCardAnimation()
                                    .createMoveAnimate(cardsMap[x][yU],cardsMap[x][y],x,x,yU,y);

                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][yU].setNum(0);

                            GameActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            meger = true;
                        }
                        break;
                    }
                }
            }
        }
        //如果有合并，添加随机数
        if (meger) {
            addRandomNum();
            checkComplete();
        }
    }

    //存放卡片的二维数组,便于对卡片进行操作
    private Card[][] cardsMap = new Card[Constant.GameColumnCount][Constant.GameColumnCount];
    //存放空点位置对象
    private List<Point> emptyPoint = new ArrayList<Point>();

}
