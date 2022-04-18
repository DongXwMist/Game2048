package com.dxw.game2048.ui;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dxw.game2048.R;
import com.dxw.game2048.adapter.GameChartsAdapter;
import com.dxw.game2048.dao.impl.GameScoreDaoImpl;
import com.dxw.game2048.entity.GameScore;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChartsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GameScoreDaoImpl gameScoreDao;
    private List<GameScore> list = new ArrayList<GameScore>();
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        gameScoreDao = new GameScoreDaoImpl(this);

        initView();
        TimerWork();

    }

    private void initView() {

        recyclerView = (RecyclerView) findViewById(R.id.ryScores);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            System.out.println("GameChartsAdapter list is:" + list);
            GameChartsAdapter adapter = new GameChartsAdapter(ChartsActivity.this, list);
            //线性布局管理器，可以设置横向还是纵向，RecyclerView默认是纵向的，所以不用处理,如果不需要设置方向，代码还可以更加的精简如下
            recyclerView.setLayoutManager(new LinearLayoutManager(ChartsActivity.this));
            recyclerView.setAdapter(adapter);

        }
    };

    /**
     * 定时任务
     */
    private void TimerWork() {
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

                list = gameScoreDao.findAllGameScore();
                System.out.println("Score list is:" + list);
                Message msg = handler.obtainMessage();
                msg.obj = list;
                handler.sendMessage(msg);

            }
        }, 0, 60 * 1000);//1分钟后每5秒执行该任务一次
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
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