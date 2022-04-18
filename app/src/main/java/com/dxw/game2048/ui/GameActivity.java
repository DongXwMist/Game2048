package com.dxw.game2048.ui;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.dxw.game2048.R;
import com.dxw.game2048.dao.impl.GameScoreDaoImpl;
import com.dxw.game2048.entity.GameScore;
import com.dxw.game2048.util.Constant;
import com.dxw.game2048.util.SPUtil;
import com.dxw.game2048.widget.animation.CardAnimation;
import com.dxw.game2048.widget.dialog.DialogNewGame;
import com.dxw.game2048.widget.dialog.GameOverDialog;
import com.dxw.game2048.widget.GameView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

    private TextView tvCurrentScore, tvBestScore;
    private Button resetGame,newGame;
    private static GameActivity mainActivity = null;
    private CardAnimation cardAnimation = null;
    private int score = 0;//总分
    private GameView gameView;
    private DialogNewGame dialogNewGame;
    private GameOverDialog gameOverDialog;
    private Timer timer;//添加定时器
    private GameStateReceiver gameStateReceiver;

    private GameScoreDaoImpl gameScoreDao;
    private GameScore g = null;
    private int id;//GameScore表的id
    private int historyBestScore = 0;


    //写一个可让外界访问到的实例
    public GameActivity() {
        mainActivity = this;//赋值静态变量
    }

    public static GameActivity getMainActivity() {
        return mainActivity;
    }

    public CardAnimation getCardAnimation() {
        return cardAnimation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameScoreDao = new GameScoreDaoImpl(this);

        initView();
        initData();
        addListener();

    }

    /**
     * 初始化页面控件
     * */
    private void initView() {

        tvCurrentScore = (TextView) findViewById(R.id.tvScore);
        tvBestScore = (TextView) findViewById(R.id.tvBestScore);
        resetGame = (Button) findViewById(R.id.resetGame);
        newGame=(Button)findViewById(R.id.NewGame);

        gameView = (GameView) findViewById(R.id.gameView);
        cardAnimation = (CardAnimation) findViewById(R.id.cardAnimation);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 注册广播
        gameStateReceiver = new GameStateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("Over");
        registerReceiver(gameStateReceiver, filter);

        //得到历史最高分
        historyBestScore = (int) SPUtil.getValue(this, Constant.BESTSCORE, 0);
        System.out.println("historyBestScore is:" + historyBestScore);
        tvBestScore.setText(historyBestScore + "");

    }

    /**
     * 添加监听器
     */
    private void addListener() {

        resetGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //清除本地缓存
                SPUtil.remove(GameActivity.this,Constant.BESTSCORE);
                clearScore();
                tvBestScore.setText(0+"");
            }
        });

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogNewGame();
            }
        });

    }


    /**
     * 分数归零
     */
    public void clearScore() {
        score = 0;
        showScore();
    }

    /**
     * 展示得分结果
     */
    public int showScore() {
        tvCurrentScore.setText(String.valueOf(score));

        return score;
    }

    /**
     * 添加分数的方法
     */
    public void addScore(int s) {
        score += s;
        showScore();

        addTimer(score);
    }

    /**
     * 判断当前分数和历史最高分数
     */
    private void JudgeNowScore(int nowScore) {
        // 当前分数大于最高分
        if (nowScore > historyBestScore) {

            historyBestScore = nowScore;
            SPUtil.putValue(GameActivity.this, Constant.BESTSCORE, nowScore);
            int nowBestScore = (int) SPUtil.getValue(GameActivity.this, Constant.BESTSCORE, 0);

            //更新UI控件
            Message msg = handler.obtainMessage();
            msg.arg1 = nowBestScore;
            System.out.println("msg.arg1 is 1_1:" + msg.arg1);
            handler.sendMessage(msg);

        }
    }

    /**
     * handler更新UI
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {

            tvBestScore.setText(msg.arg1 + "");
            System.out.println("msg.arg1 is:" + msg);

        }
    };

    /**
     * 添加定时器
     */

    private void addTimer(int nowScore) {

        /**
         * 动态的创建TimerTask 对象
         * */
        if (null == timer) {
            timer = new Timer();
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //TODO: 定时做某件事情
                // 当前分数大于最高分
                System.out.println("执行定时任务");
                JudgeNowScore(nowScore);

            }
        }, 0, 5 * 1000);//1分钟后每5秒执行该任务一次
    }

    /**
     * 自定义广播类
     */
    private class GameStateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            System.out.println("MainActivity GameStateReceiver执行");

            String action = intent.getAction();
            assert action != null;//断言，条件为false时执行
            if (action.equals("Over")) {

                showGameOverDialog();
            }

        }
    }

    /**
     * GameOverDialog展示
     */
    private void showGameOverDialog() {

        gameOverDialog = new GameOverDialog(this, R.style.Dialog);

        gameOverDialog.setTitle("Game Over");
        gameOverDialog.setMessage("游戏已结束，您的总分为:" + score + "分,请问是否再战？");
        gameOverDialog.setScore(score + "");

        gameOverDialog.setCancelOnClickListener("取消", new GameOverDialog.onCancelOnClickListener() {
            @Override
            public void onCancelClick() {
                Toast.makeText(GameActivity.this, "取消是明智的选择", Toast.LENGTH_SHORT).show();
                gameOverDialog.dismiss();
            }
        });

        gameOverDialog.setOkOnClickListener("确定", new GameOverDialog.onOkOnClickListener() {
            @Override
            public void onOkClick(String username) {
                Toast.makeText(GameActivity.this, "既然选择了继续，就再战罢", Toast.LENGTH_SHORT).show();
                gameOverDialog.dismiss();


                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(GameActivity.this, "游戏名不能为空", Toast.LENGTH_SHORT).show();
                } else {

                    JudgeUserExists(username);
                    gameView.startGame();

                }

            }
        });
        gameOverDialog.show();

    }


    private void JudgeUserExists(String username) {

        /**
         * 判断写的游戏名是否存在
         * true:  更新数据
         * false: 添加记录
         * */
        GameScore g = new GameScore();
        List<GameScore> list = new ArrayList<GameScore>();
        list = gameScoreDao.findAllGameScore();

        boolean exists = false;
        for (int i = 0; i < list.size(); i++) {
            if (username.equals(list.get(i).getUsername())) {
                exists = true;
                id = list.get(i).getId();
            } else {
                exists = false;

            }
        }

        if (exists == true) {
            g.setId(id);
            g.setUsername(username);
            g.setGameScore(score);
            System.out.println("MainActivity 执行更新操作");
            gameScoreDao.updateGameScore(g);
            SPUtil.putValue(this,Constant.USERNAME,username);
        }
        if (exists == false) {
            g.setUsername(username);
            g.setGameScore(score);
            System.out.println("MainActivity 执行添加操作");
            gameScoreDao.addGameScore(g);
            SPUtil.putValue(this,Constant.USERNAME,username);
        }

    }

    /**
     * DialogNewGame展示
     */
    private void showDialogNewGame() {

        dialogNewGame = new DialogNewGame(this, R.style.Dialog);
        dialogNewGame.setTitle("温馨提示");
        dialogNewGame.setMessage("您确定要再开一局吗？");

        dialogNewGame.setCancelOnClickListener("取消", new DialogNewGame.onCancelOnClickListener() {
            @Override
            public void onCancelClick() {
                Toast.makeText(GameActivity.this, "明智的选择,请再接再厉", Toast.LENGTH_SHORT).show();
                dialogNewGame.dismiss();
            }
        });

        dialogNewGame.setOkOnClickListener("确定", new DialogNewGame.onOkOnClickListener() {
            @Override
            public void onOkClick() {

                dialogNewGame.dismiss();
                Toast.makeText(GameActivity.this, "欢迎再次挑战", Toast.LENGTH_SHORT).show();
                gameView.startGame();

            }
        });

        dialogNewGame.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 取消注册广播
        if (gameStateReceiver != null) {
            unregisterReceiver(gameStateReceiver);
            gameStateReceiver = null;
        }

        //取消定时任务
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}