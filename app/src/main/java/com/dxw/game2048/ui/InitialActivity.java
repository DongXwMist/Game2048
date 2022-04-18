package com.dxw.game2048.ui;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.dxw.game2048.R;

public class InitialActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvStartGame,mTvStartChats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        initView();
    }

    /**
     * 初始化页面控件
     */
    private void initView() {
        mTvStartGame = (TextView) findViewById(R.id.startGame);
         mTvStartChats = (TextView) findViewById(R.id.startCharts);

        //设置点击监听
        mTvStartChats.setOnClickListener(this);
        mTvStartGame.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        Intent next;

        switch (view.getId()) {
            case R.id.startGame:
                next = new Intent(this, GameActivity.class);
                startActivity(next);
                break;
            case R.id.startCharts:
                next = new Intent(this, ChartsActivity.class);
                startActivity(next);
                break;
        }

    }
}